package org.keycloak.dashboard;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.keycloak.dashboard.gh.GHRunAttempt;
import org.keycloak.dashboard.gh.GHWorkflowRun;
import org.keycloak.dashboard.gh.GHWorkflowRuns;
import org.keycloak.dashboard.gh.GitHubCli;
import org.keycloak.dashboard.util.DateUtil;
import org.kohsuke.github.GHWorkflowJob;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class RetriedPrsLoader {

    private static final Set<String> WORKFLOWS = Set.of("ci.yml", "js-ci.yml", "operator-ci.yml");

    private static final int DAYS = 14;
    private final GitHub gh;
    private final GitHubCli ghCli;

    public RetriedPrsLoader(GitHub gh, GitHubCli ghCli) {
        this.gh = gh;
        this.ghCli = ghCli;
    }

    public void load() throws IOException, InterruptedException, ParseException {
        ObjectMapper objectMapper = new ObjectMapper();

        System.out.print("Fetching retried-prs: ");

        File logsDir = new File("logs");
        File cachedRunsFile = new File(logsDir, "retried-prs.json");

        GHWorkflowRuns cachedRuns;
        if (cachedRunsFile.isFile()) {
            cachedRuns = objectMapper.readValue(cachedRunsFile, GHWorkflowRuns.class);
        } else {
            cachedRuns = new GHWorkflowRuns();
            cachedRuns.setWorkflowRuns(new LinkedList<>());
        }

        Date expirationDate = DateUtil.minusdays(DAYS);
        cachedRuns.setWorkflowRuns(cachedRuns.getWorkflowRuns().stream().filter(r -> DateUtil.fromJson(r.getCreatedAt()).after(expirationDate)).toList());

        String to = DateUtil.toString(new Date());
        Optional<GHWorkflowRun> lastRun = cachedRuns.getWorkflowRuns().stream().max(Comparator.comparing(GHWorkflowRun::getCreatedAt));
        String from = lastRun.isPresent() ? DateUtil.toString(DateUtil.fromJson(lastRun.get().getCreatedAt())) : DateUtil.minusDaysString(DAYS);

        List<GHWorkflowRuns> l = new LinkedList<>();
        l.add(cachedRuns);
        for (String workflow : WORKFLOWS) {
            l.addAll(ghCli.apiGet(GHWorkflowRuns.class, "actions/workflows/" + workflow + "/runs", "--paginate", "-f", "status=success", "-f", "event=pull_request", "-f", "created=" + from + ".." + to, "-f", "per_page=10"));
        }

        GHWorkflowRuns runs = GHWorkflowRuns.combine(l);

        runs.setWorkflowRuns(runs.getWorkflowRuns().stream().filter(r -> r.getRunAttempt() > 1).toList());
        runs.setTotalCount(runs.getWorkflowRuns().size());

        objectMapper.writeValue(cachedRunsFile, runs);

        for (GHWorkflowRun r : runs.getWorkflowRuns()) {
            File jobsFile = new File(logsDir, "pr-jobs-" + r.getId());
            File jobsLog = new File(logsDir, "pr-log-" + r.getId());

            if (!jobsFile.isFile() || !jobsLog.isFile()) {
                GHRunAttempt ghRunAttempt = ghCli.apiGet(GHRunAttempt.class, "actions/runs/" + r.getId() + "/attempts/" + (r.getRunAttempt() - 1) + "?exclude_pull_requests=true").get(0);

                if ("failure".equals(ghRunAttempt.getConclusion())) {
                    if (!jobsFile.isFile()) {
                        PrintStream jobsOutput = new PrintStream(new FileOutputStream(jobsFile));

                        String workflow = r.getPath().substring(r.getPath().lastIndexOf('/') + 1);

                        jobsOutput.println("# " + workflow + " " + r.getCreatedAt() + " " + r.getEvent() + " " + (r.getRunAttempt() - 1));

                        PagedIterable<GHWorkflowJob> ghWorkflowJobs = gh.getRepository("keycloak/keycloak").getWorkflowRun(r.getId()).listAllJobs();

                        for (GHWorkflowJob j : ghWorkflowJobs.toList()) {
                            if (j.getRunAttempt() == 1) {
                                jobsOutput.println(j.getName() + ": [" + j.getConclusion() + "]");
                            }
                        }

                        ghCli.download(jobsLog, "gh", "run", "view", "-R", "keycloak/keycloak", Long.toString(r.getId()), "--attempt", Integer.toString(r.getRunAttempt() - 1), "--log-failed");

                        System.out.print(".");
                    }

                }

            }
        }

        System.out.println();

        System.out.print("Deleting expired retried-prs: ");

        List<String> retriedPrIds = runs.getWorkflowRuns().stream().map(r -> r.getId().toString()).toList();

        File[] jobFiles = logsDir.listFiles((dir, name) -> name.startsWith("pr-jobs-"));
        for (File jobFile : jobFiles) {
            String runId = jobFile.getName().split("-")[2];
            if (!retriedPrIds.contains(runId)) {
                jobFile.delete();
                System.out.print(".");
            }
        }

        File[] logFiles = logsDir.listFiles((dir, name) -> name.startsWith("pr-log-"));
        for (File logFile : logFiles) {
            String runId = logFile.getName().split("-")[2];
            if (!retriedPrIds.contains(runId)) {
                logFile.delete();
                System.out.print(".");
            }
        }

        System.out.println();
    }

}
