package org.keycloak.dashboard;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.keycloak.dashboard.gh.GHRunAttempt;
import org.keycloak.dashboard.gh.GHRunJobs;
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class RetriedPrsLoader {

    private static final int DAYS = 14;
    private final GitHub gh;
    private final GitHubCli ghCli;

    public RetriedPrsLoader(GitHub gh, GitHubCli ghCli) {
        this.gh = gh;
        this.ghCli = ghCli;
    }

    public void load() throws IOException, InterruptedException {
        System.out.print("Fetching retried-prs: ");

        String from = DateUtil.minusDaysString(DAYS);
        String to = DateUtil.toString(new Date());

        File logsDir = new File("logs");

        GHWorkflowRuns runs = GHWorkflowRuns.combine(ghCli.apiGet(GHWorkflowRuns.class, "actions/workflows/ci.yml/runs", "--paginate", "-f", "status=success", "-f", "event=pull_request", "-f", "created=" + from + ".." + to, "-f", "per_page=10"));

        Set<String> retriedPrIds = new HashSet<>();

        for (GHWorkflowRun r : runs.getWorkflowRuns()) {
            if (r.getRunAttempt() > 1) {
                GHRunAttempt ghRunAttempt = ghCli.apiGet(GHRunAttempt.class, "actions/runs/" + r.getId() + "/attempts/" + (r.getRunAttempt() - 1) + "?exclude_pull_requests=true").get(0);

                if (ghRunAttempt.getConclusion().equals("failure")) {
                    retriedPrIds.add(ghRunAttempt.getId());

                    File jobsFile = new File(logsDir, "pr-jobs-" + r.getId());
                    File jobsLog = new File(logsDir, "pr-log-" + r.getId());

                    if (!jobsFile.isFile()) {
                        PrintStream jobsOutput = new PrintStream(new FileOutputStream(jobsFile));

                        jobsOutput.println("# " + r.getCreatedAt() + " " + r.getEvent() + " " + (r.getRunAttempt() - 1));

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
