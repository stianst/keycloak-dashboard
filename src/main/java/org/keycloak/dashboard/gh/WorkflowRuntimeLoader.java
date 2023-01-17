package org.keycloak.dashboard.gh;

import io.smallrye.graphql.client.dynamic.api.DynamicGraphQLClient;
import io.smallrye.graphql.client.vertx.dynamic.VertxDynamicGraphQLClientBuilder;
import io.vertx.core.Vertx;
import org.keycloak.dashboard.Config;
import org.keycloak.dashboard.rep.PullRequestWait;
import org.keycloak.dashboard.util.DateUtil;

import javax.json.JsonObject;
import javax.json.JsonValue;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.keycloak.dashboard.Config.EXPIRATION_OLD_ISSUES;

public class WorkflowRuntimeLoader {

    private static final String[] AUTH_ENV_KEYS = { "GITHUB_TOKEN", "GH_TOKEN", "GITHUB_OAUTH" };

    public List<PullRequestWait> update(List<PullRequestWait> currentList) throws Exception {
        currentList = currentList.stream().filter(c -> c.getMergedAt().after(EXPIRATION_OLD_ISSUES)).collect(Collectors.toList());

        Optional<PullRequestWait> mostRecent = currentList.stream().max(Comparator.comparing(PullRequestWait::getMergedAt));
        String fromDate = DateUtil.toString(mostRecent.get().getMergedAt());

        System.out.print("Updating pull request wait times since " + fromDate + ": ");

        List<PullRequestWait> updatedList = load(">=" + fromDate);

        System.out.println(".");

        return merge(currentList, updatedList);
    }

    public List<PullRequestWait> load() throws Exception {
        System.out.print("Loading pull request wait times for last " + Config.MAX_HISTORY + " days: ");

        List<PullRequestWait> list = new LinkedList<>();

        for (int fromDays = Config.MAX_HISTORY; fromDays > 0; fromDays = fromDays - 8) {
            int toDays = (fromDays - 7) >= 0 ? (fromDays - 7) : 0;

            String fromDate = DateUtil.minusDaysString(fromDays);
            String toDate = DateUtil.minusDaysString(toDays);

            List<PullRequestWait> updated = load(fromDate + ".." + toDate);
            list = merge(list, updated);

            System.out.print(".");
        }

        System.out.println();

        return list;
    }

    private List<PullRequestWait> merge(List<PullRequestWait> first, List<PullRequestWait> second) {
        Map<Integer, PullRequestWait> map = new HashMap<>();
        first.stream().forEach(c -> map.put(c.getNumber(), c));
        second.stream().filter(c -> !map.containsKey(c.getNumber())).forEach(c -> map.put(c.getNumber(), c));
        return map.values().stream().toList();
    }

    private List<PullRequestWait> load(String fromDate) throws Exception {
        Vertx vertx = Vertx.vertx();
        DynamicGraphQLClient client = new VertxDynamicGraphQLClientBuilder()
                .vertx(vertx)
                .url("https://api.github.com/graphql")
                .header("Authorization", "bearer " + token()).build();
        List<PullRequestWait> waitTimes = new LinkedList<>();

        try {
            int first = 100;

            String query = new String(WorkflowRuntimeLoader.class.getResourceAsStream("workflow-runtimes-query").readAllBytes(), StandardCharsets.UTF_8)
                    .replace("$$FROM_DATE$$", fromDate)
                    .replace("$$FIRST$$", Integer.toString(first));

            JsonObject jsonObject = client.executeSync(query).getData();

            Iterator<JsonValue> itr = jsonObject
                    .get("search").asJsonObject()
                    .get("edges").asJsonArray()
                    .iterator();

            while (itr.hasNext()) {

                JsonObject pullRequest = itr.next().asJsonObject()
                        .get("node").asJsonObject()
                        .get("commits").asJsonObject()
                        .get("edges").asJsonArray().get(0).asJsonObject()
                        .get("node").asJsonObject()
                        .get("pullRequest").asJsonObject();

                int number = pullRequest.getInt("number");
                Date mergedAt = DateUtil.fromJson(pullRequest.getString("mergedAt"));

                Iterator<JsonValue> checkSuiteItr = pullRequest.get("commits").asJsonObject()
                        .get("edges").asJsonArray().get(0).asJsonObject()
                        .get("node").asJsonObject()
                        .get("commit").asJsonObject()
                        .get("checkSuites").asJsonObject()
                        .get("edges").asJsonArray().iterator();

                Date startedAt = null;
                Date completedAt = null;

                while (checkSuiteItr.hasNext()) {
                    JsonObject checkSuite = checkSuiteItr.next().asJsonObject()
                            .get("node").asJsonObject();

                    Date createdAt = DateUtil.fromJson(checkSuite.getString("createdAt"));
                    Date updatedAt = DateUtil.fromJson(checkSuite.getString("updatedAt"));

                    if (startedAt == null || createdAt.before(startedAt)) {
                        startedAt = createdAt;
                    }

                    if (completedAt == null || updatedAt.after(completedAt)) {
                        completedAt = updatedAt;
                    }
                }

                if (completedAt != null && startedAt != null) {
                    int minutes = (int) ((completedAt.getTime() - startedAt.getTime()) / (60 * 1000));
                    waitTimes.add(new PullRequestWait(number, minutes, mergedAt, completedAt));
                }
            }
        } finally {
            client.close();
            vertx.close();
        }

        return waitTimes;
    }

    private static String token() throws IOException {
        String token = tokenFromEnv();
        if (token != null) {
            return token;
        }
        token = tokenFromGhCli();
        if (token != null) {
            return token;
        }

        throw new RuntimeException("Failed to get token for GitHub APIs");
    }

    private static String tokenFromEnv() {
        for (String k : AUTH_ENV_KEYS) {
            String v = System.getenv(k);
            if (v != null) {
                return v;
            }
        }
        return null;
    }

    private static String tokenFromGhCli() throws IOException {
        return new String(Runtime.getRuntime().exec("gh auth token").getInputStream().readAllBytes(), StandardCharsets.UTF_8).trim();
    }

}
