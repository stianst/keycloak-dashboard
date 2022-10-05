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
        boolean mockGitHub = false;

        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        GitHub gitHub = GitHubBuilder.fromEnvironment()
                .withConnector(new OkHttpGitHubConnector(okHttpClient))
                .build();

        GitHubWorkflows workflows = new GitHubWorkflows();

        PR pr = new PR(gitHub, mockGitHub);

        Bugs bugs = new Bugs(gitHub, mockGitHub);

        Map<String, Object> attributes = new HashMap<>();

        attributes.put("workflows", workflows.getWorkflows());
        attributes.put("prStats", pr.getStats());
        attributes.put("bugStats", bugs.getStats());
        attributes.put("bugAreaStats", bugs.getAreaStats());

        FreeMarker freeMarker = new FreeMarker(new File("docs"), attributes);

        freeMarker.template("index.ftl");

        okHttpClient.dispatcher().executorService().shutdown();
        okHttpClient.connectionPool().evictAll();
    }

}