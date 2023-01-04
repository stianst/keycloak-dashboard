package org.keycloak.dashboard;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import freemarker.template.TemplateException;
import org.keycloak.dashboard.beans.Bugs;
import org.keycloak.dashboard.beans.PR;
import org.keycloak.dashboard.beans.Workflows;
import org.keycloak.dashboard.ci.FailedRun;
import org.keycloak.dashboard.ci.LogFailedParser;
import org.keycloak.dashboard.rep.GitHubData;
import org.keycloak.dashboard.rep.Teams;
import org.keycloak.dashboard.util.FreeMarker;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Hello world!
 */
public class Dashboard {

    public static void main(String[] args) throws IOException, TemplateException, ParseException {
        Dashboard dashboard = new Dashboard();
        dashboard.createDashboard();
    }

    public void createDashboard() throws IOException, TemplateException, ParseException {
        ObjectMapper objectMapper = new ObjectMapper();
        GitHubData data = objectMapper.readValue(new File("data.json"), GitHubData.class);

        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        Teams teams = yamlMapper.readValue(new File("teams.yml"), Teams.class);

        Workflows workflows = new Workflows();
        PR pr = new PR(data);
        Bugs bugs = new Bugs(data, teams);

        LogFailedParser logFailedParser = new LogFailedParser();
        logFailedParser.parseAll();

        Map<String, Object> attributes = new HashMap<>();

        attributes.put("publish", Config.PUBLISH);
        attributes.put("updatedDate", data.getUpdatedDate());
        attributes.put("workflows", workflows.getWorkflows());
        attributes.put("prStats", pr.getStats());
        attributes.put("bugStats", bugs.getStats());
        attributes.put("bugAreaStats", bugs.getAreaStats());
        attributes.put("bugTeamStats", bugs.getTeamStats());
        attributes.put("failedRuns", logFailedParser.getFailedRuns());
        attributes.put("failedJobs", logFailedParser.getFailedJobs());
        attributes.put("failedTests", logFailedParser.getFailedTests());
        attributes.put("flakyTests", bugs.getFlakyTests());

        File output = new File("docs/index.html");
        FreeMarker freeMarker = new FreeMarker(attributes);
        freeMarker.template("index.ftl", output);
        freeMarker.template("bugs.ftl", new File("docs/bugs.html"));
        freeMarker.template("prs.ftl", new File("docs/prs.html"));
        freeMarker.template("workflows.ftl", new File("docs/workflows.html"));
        freeMarker.template("tests.ftl", new File("docs/tests.html"));

        System.out.println("Created dashboard: " + output.toURI());
    }

}
