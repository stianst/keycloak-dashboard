package org.keycloak.dashboard;

import freemarker.template.TemplateException;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import org.keycloak.dashboard.util.FreeMarker;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.kohsuke.github.extras.okhttp3.OkHttpGitHubConnector;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Hello world!
 */
public class Dashboard {

    public static void main(String[] args) throws IOException, TemplateException {
        boolean mockGitHub = true;

        File cacheDirectory = new File(".cache");
        Cache cache = new Cache(cacheDirectory, 10 * 1024 * 1024);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().cache(cache).build();

        GitHub gitHub = GitHubBuilder.fromEnvironment()
                .withConnector(new OkHttpGitHubConnector(okHttpClient))
                .build();

        List<String> warnings = new LinkedList<>();

        GitHubWorkflows workflows = new GitHubWorkflows();

        PR pr = new PR(gitHub, mockGitHub);
        warnings.addAll(pr.getWarnings());

        Bugs bugs = new Bugs(gitHub, mockGitHub);

        Map<String, Object> attributes = new HashMap<>();

        attributes.put("workflows", workflows.getWorkflows());
        attributes.put("prStats", pr.getStats());
        attributes.put("bugStats", bugs.getStats());
        attributes.put("bugAreaStats", bugs.getAreaStats());
        attributes.put("warnings", warnings);

        FreeMarker freeMarker = new FreeMarker(new File("docs"), attributes);

        freeMarker.template("index.ftl");

        okHttpClient.dispatcher().executorService().shutdown();
        okHttpClient.connectionPool().evictAll();
        okHttpClient.cache().close();
    }

    private static GitHub createGitHub() throws IOException {
        File cacheDirectory = new File(".cache");
        Cache cache = new Cache(cacheDirectory, 10 * 1024 * 1024);
        return GitHubBuilder.fromEnvironment()
                .withConnector(new OkHttpGitHubConnector(new OkHttpClient.Builder().cache(cache).build()))
                .build();
    }

}
