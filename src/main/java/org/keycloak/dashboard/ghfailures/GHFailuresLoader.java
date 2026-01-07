package org.keycloak.dashboard.ghfailures;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.keycloak.dashboard.gh.GHRunAttempt;
import org.keycloak.dashboard.gh.GHWorkflowRun;
import org.keycloak.dashboard.gh.GHWorkflowRuns;
import org.keycloak.dashboard.gh.GitHubCli;
import org.keycloak.dashboard.gh.TokenUtil;
import org.keycloak.dashboard.util.DateUtil;
import org.kohsuke.github.GHWorkflowJob;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.kohsuke.github.PagedIterable;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class GHFailuresLoader {

    private static final Set<String> WORKFLOWS = Set.of("ci.yml", "js-ci.yml", "operator-ci.yml");
    private static final File LOGS_DIR = new File("ghfailures");

    private static final int MAX_DAYS = 14;
    private static final int DAYS = 3;

    private final GitHub gh;
    private final GitHubCli ghCli;

    private ObjectMapper objectMapper = new ObjectMapper();

    private String from;
    private String to;

    public static void main(String[] args) throws IOException, InterruptedException {
        GitHub gitHub = GitHubBuilder.fromEnvironment().withJwtToken(TokenUtil.token()).build();
        GitHubCli ghCli = new GitHubCli();
        GHFailuresLoader ghFailuresLoader = new GHFailuresLoader(gitHub, ghCli);
        ghFailuresLoader.loadRuns();
    }

    public GHFailuresLoader(GitHub gh, GitHubCli ghCli) {
        this.gh = gh;
        this.ghCli = ghCli;

        from = DateUtil.minusDaysString(DAYS);
        to = DateUtil.toString(new Date());
    }

    public void loadRuns() throws IOException, InterruptedException {
        System.out.print("Fetching failed jobs: ");

        GHRuns runs;

        File cachedRunsFile = new File(LOGS_DIR, "gh-job-failures.json");
        if (cachedRunsFile.isFile()) {
            runs = objectMapper.readValue(cachedRunsFile, GHRuns.class);
            from = runs.getLastUpdated();

            List<GHRun> newRuns = new LinkedList<>();
            newRuns.addAll(getPullRequestRuns());
            System.out.print(".");
            newRuns.addAll(getFailedRuns());
            System.out.print(".");

            for (GHRun run : newRuns) {
                boolean exists = runs.getRuns().stream().anyMatch(r -> r.getId().equals(run.getId()));
                if (!exists) {
                    addWorkflowJobs(run);
                    runs.getRuns().add(run);
                }
            }

            Date expire = DateUtil.minusdays(DAYS);
            runs.setRuns(runs.getRuns().stream().filter(r -> DateUtil.fromJson(r.getCreatedAt()).after(expire)).toList());
        } else {
            runs = new GHRuns();
            runs.getRuns().addAll(getPullRequestRuns());
            runs.getRuns().addAll(getFailedRuns());
            runs.setLastUpdated(DateUtil.toString(new Date()));

            for (GHRun run : runs.getRuns()) {
                addWorkflowJobs(run);
            }
        }

        objectMapper.writeValue(cachedRunsFile, runs);
        System.out.println();

        System.out.print("Fetching failed job logs: ");
        loadLogs(runs);
        System.out.println();

//        for (GHRun r : runs.getRuns()) {
//            String conclusion = r.getPreviousAttemptConclusion() != null ? r.getPreviousAttemptConclusion() : r.getConclusion();
//            if (!(conclusion.equals("success") || conclusion.equals("action_required"))) {
//                System.out.println(r.getWorkflow() + " " + r.getId() + " " + r.getRunAttempts() + " " + r.getConclusion() + " " + r.getEvent() + " " + r.getPreviousAttemptConclusion());
//                r.getJobs().stream().forEach(j -> {
//                    System.out.println(" - " + j.getName() + " " + j.getConclusion());
//                });
//            }
//        }
    }

    private void loadLogs(GHRuns runs) throws IOException, InterruptedException {
        for (GHRun r : runs.getRuns()) {
            File runLog = new File(LOGS_DIR, r.getId() + "-logs.zip");
            if (!runLog.isFile()) {
                int attempt = r.getRunAttempts();
                Long runId = r.getId();
                if (r.getPreviousAttemptId() != null) {
                    runId = r.getPreviousAttemptId();
                    attempt = attempt - 1;
                }
                ghCli.download(runLog, "gh", "api", "/repos/keycloak/keycloak/actions/runs/" + runId + "/attempts/" + attempt + "/logs");
                System.out.print(".");
            }
        }

        for (File f : LOGS_DIR.listFiles(n -> n.getName().endsWith("-logs.zip"))) {
            Long runId = Long.valueOf(f.getName().split("-")[0]);
            if (runs.getRuns().stream().noneMatch(r -> r.getId().equals(runId))) {
                f.delete();
            }
        }
    }

    private void addWorkflowJobs(GHRun run) throws IOException, InterruptedException {
        Long runId = run.getId();
        String conclusion = run.getConclusion();

        if (run.getConclusion().equals("action_required")) {
            return;
        }

        if (run.getConclusion().equals("success") && run.getRunAttempts() > 1) {
            GHRunAttempt ghRunAttempt = ghCli.apiGet(GHRunAttempt.class, "actions/runs/" + runId + "/attempts/" + (run.getRunAttempts() - 1) + "?exclude_pull_requests=true").get(0);
            runId = Long.valueOf(ghRunAttempt.getId());
            conclusion = ghRunAttempt.getConclusion();
            run.setPreviousAttemptId(runId);
            run.setPreviousAttemptConclusion(conclusion);
            System.out.print(".");
        }

        if (!conclusion.equals("success")) {
            PagedIterable<GHWorkflowJob> ghWorkflowJobs = gh.getRepository("keycloak/keycloak").getWorkflowRun(runId).listAllJobs();

            List<GHRun.GHRunJob> jobs = ghWorkflowJobs.toList().stream()
                    .filter(j -> !(j.getConclusion().equals(org.kohsuke.github.GHWorkflowRun.Conclusion.SUCCESS) || j.getConclusion().equals(org.kohsuke.github.GHWorkflowRun.Conclusion.SKIPPED)))
                    .map(GHFailuresLoader::convert).toList();
            run.setJobs(jobs);
            System.out.print(".");
        }
    }

    private List<GHRun> getPullRequestRuns() throws IOException, InterruptedException {
        List<GHRun> workflowRuns = new LinkedList<>();
        for (String workflow : WORKFLOWS) {
            List<GHWorkflowRuns> list = ghCli.apiGet(GHWorkflowRuns.class, "actions/workflows/" + workflow + "/runs", "--paginate", "-f", "status=success", "-f", "event=pull_request", "-f", "created=" + from + ".." + to, "-f", "per_page=100");
            workflowRuns.addAll(GHWorkflowRuns.combine(list).getWorkflowRuns().stream().map(r -> convert(workflow, r)).toList());
        }
        return workflowRuns;
    }

    private List<GHRun> getFailedRuns() throws IOException, InterruptedException {
        List<GHRun> workflowRuns = new LinkedList<>();
        for (String workflow : WORKFLOWS) {
            List<GHWorkflowRuns> list = ghCli.apiGet(GHWorkflowRuns.class, "actions/workflows/" + workflow + "/runs", "--paginate", "-f", "status=failure", "-f", "branch=main", "-f", "created=" + from + ".." + to, "-f", "per_page=100");
            workflowRuns.addAll(GHWorkflowRuns.combine(list).getWorkflowRuns().stream().map(r -> convert(workflow, r)).toList());
        }
        return workflowRuns;
    }

    private static GHRun convert(String workflow, GHWorkflowRun run) {
        GHRun r = new GHRun();
        r.setId(run.getId());
        r.setCreatedAt(run.getCreatedAt());
        r.setConclusion(run.getConclusion());
        r.setEvent(run.getEvent());
        r.setWorkflow(workflow);
        r.setRunAttempts(run.getRunAttempt());
        return r;
    }

    private static GHRun.GHRunJob convert(GHWorkflowJob job) {
        GHRun.GHRunJob j = new GHRun.GHRunJob();
        j.setName(job.getName());
        j.setConclusion(job.getConclusion().name().toLowerCase());
        return j;
    }

}
