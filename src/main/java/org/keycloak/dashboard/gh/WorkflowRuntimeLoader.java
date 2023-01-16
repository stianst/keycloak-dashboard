package org.keycloak.dashboard.gh;

import io.smallrye.graphql.client.dynamic.api.DynamicGraphQLClient;
import io.smallrye.graphql.client.vertx.dynamic.VertxDynamicGraphQLClientBuilder;
import io.vertx.core.Vertx;
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

public class WorkflowRuntimeLoader {

    private static final String[] AUTH_ENV_KEYS = { "GITHUB_TOKEN", "GH_TOKEN", "GITHUB_OAUTH" };

    public static void main(String[] args) throws Exception {
        WorkflowRuntimeLoader loader = new WorkflowRuntimeLoader();
        List<PullRequestWait> list = loader.load();
        list = loader.update(list);
        for (PullRequestWait p : list) {
            System.out.println(p.getNumber() + "\t" + p.getMergedAt() + "\t" + p.getMinutes() + "\t" + p.getTitle());
        }
    }

    public List<PullRequestWait> update(List<PullRequestWait> currentList) throws Exception {
        Optional<PullRequestWait> mostRecent = currentList.stream().max(Comparator.comparing(PullRequestWait::getMergedAt));
        String fromDate = DateUtil.toString(mostRecent.get().getMergedAt());

        System.out.print("Updating pull request wait times since " + fromDate + ": ");

        Map<Integer, PullRequestWait> map = new HashMap<>();
        currentList.stream().forEach(c -> map.put(c.getNumber(), c));

        List<PullRequestWait> updatedList = load(fromDate);
        updatedList.stream().filter(c -> map.containsKey(c.getNumber())).forEach(c -> map.put(c.getNumber(), c));

        System.out.println(".");

        return map.values().stream().toList();
    }

    public List<PullRequestWait> load() throws Exception {
        System.out.print("Loading pull request wait times for last 7 days: ");
        List<PullRequestWait> list = load(DateUtil.MINUS_7_DAYS_STRING);
        System.out.println(".");
        return list;
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
                String title = pullRequest.getString("title");
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

                int minutes = (int) ((completedAt.getTime() - startedAt.getTime()) / (60 * 1000));

                waitTimes.add(new PullRequestWait(number, title, minutes, mergedAt));
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
