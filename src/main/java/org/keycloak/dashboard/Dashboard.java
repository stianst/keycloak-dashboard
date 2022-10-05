package org.keycloak.dashboard;

import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.TemplateException;
import org.keycloak.dashboard.beans.Bugs;
import org.keycloak.dashboard.beans.PR;
import org.keycloak.dashboard.beans.Workflows;
import org.keycloak.dashboard.rep.GitHubData;
import org.keycloak.dashboard.util.FreeMarker;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 */
public class Dashboard {

    public static void main(String[] args) throws IOException, TemplateException {
        Dashboard dashboard = new Dashboard();
        dashboard.createDashboard();
    }

    public void createDashboard() throws IOException, TemplateException {
        ObjectMapper objectMapper = new ObjectMapper();
        GitHubData data = objectMapper.readValue(new File("data.json"), GitHubData.class);

        Workflows workflows = new Workflows();
        PR pr = new PR(data);
        Bugs bugs = new Bugs(data);

        Map<String, Object> attributes = new HashMap<>();

        attributes.put("workflows", workflows.getWorkflows());
        attributes.put("prStats", pr.getStats());
        attributes.put("bugStats", bugs.getStats());
        attributes.put("bugAreaStats", bugs.getAreaStats());

        File output = new File("docs/index.html");
        FreeMarker freeMarker = new FreeMarker(attributes);
        freeMarker.template("index.ftl", output);

        System.out.println("Created dashboard: " + output.toURI());
    }

}
