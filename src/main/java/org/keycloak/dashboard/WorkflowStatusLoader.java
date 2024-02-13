package org.keycloak.dashboard;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.keycloak.dashboard.gh.GHWorkflowRun;
import org.keycloak.dashboard.gh.GHWorkflowRuns;
import org.keycloak.dashboard.gh.GitHubCli;
import org.keycloak.dashboard.gh.TokenUtil;
import org.keycloak.dashboard.util.DateUtil;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class WorkflowStatusLoader {

    private final GitHubCli ghCli;

    public WorkflowStatusLoader(GitHubCli ghCli) {
        this.ghCli = ghCli;
    }

    public void load() throws IOException, InterruptedException {
        System.out.print("Fetching workflow-status: ");

        List<GHWorkflowRuns> list = ghCli.apiGet(GHWorkflowRuns.class, "actions/runs",
                "-F", "status=completed",
                "-F", "created=>=" + DateUtil.minusDaysString(3),
                "--paginate");

        System.out.print(".");

        list.addAll(ghCli.apiGet(GHWorkflowRuns.class, "repos/keycloak-rel/keycloak-rel/actions/runs",
                "-F", "status=completed",
                "-F", "created=>=" + DateUtil.minusDaysString(3),
                "--paginate"));

        List<GHWorkflowRun> workflowRuns = GHWorkflowRuns.combine(list).getWorkflowRuns().stream()
                .filter(r -> !r.getEvent().equals("pull_request") && !r.getEvent().equals("pull_request_target"))
                .filter(distinctByWorkflowAndBranch()).toList();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("workflow-status.json"), workflowRuns);

        System.out.println(".");
    }

    public static Predicate<GHWorkflowRun> distinctByWorkflowAndBranch() {
        Set<String> seen = new HashSet<>();
        return t -> {
            String key = t.getName() + ":" + t.getHeadBranch();
            if (seen.contains(key)) {
                return false;
            } else {
                seen.add(key);
                return true;
            }
        };
    }

}
