package org.keycloak.dashboard.beans;

import java.util.LinkedList;
import java.util.List;

public class Workflows {

    List<Workflow> workflows;

    public Workflows() {
        workflows = new LinkedList<>();

        workflows.add(new Workflow("Nightly release", "keycloak-rel/keycloak-rel", "release-nightly.yml", "Release Nightly", "branch=main"));

        workflows.add(new Workflow("Keycloak CI", "keycloak/keycloak", "ci.yml", "Keycloak CI", "event=schedule branch=main"));
        workflows.add(new Workflow("Keycloak Operator CI", "keycloak/keycloak", "operator-ci.yml", "Keycloak Operator CI", "event=schedule branch=main"));
        workflows.add(new Workflow("Keycloak QuickStarts CI", "keycloak/keycloak-quickstarts", "ci.yml", "Quickstarts tests", "event=workflow_dispatch branch=main"));
        workflows.add(new Workflow("Keycloak QuickStarts Schedule", "keycloak/keycloak-quickstarts", "schedule.yml", "Scheduled workflows", "event=schedule"));
        workflows.add(new Workflow("Keycloak UI CI", "keycloak/keycloak-ui", "cypress.yml", "Cypress", "event=schedule branch=main"));

        workflows.add(new Workflow("CodeQL", "keycloak/keycloak", "codeql-analysis.yml", "CodeQL", "event=schedule branch=main"));
        workflows.add(new Workflow("Snyk", "keycloak/keycloak", "snyk-analysis.yml", "Snyk", "event=schedule branch=main"));
        workflows.add(new Workflow("Trivy", "keycloak/keycloak", "trivy-analysis.yml", "Trivy", "event=schedule branch=main"));

        workflows.add(new Workflow("Dashboard", "stianst/keycloak-dashboard", "data.yml", "Update data", "event=schedule branch=main"));
    }

    public List<Workflow> getWorkflows() {
        return workflows;
    }

    public static class Workflow {

        private String title;
        private String ghLink;
        private String shield;

        public Workflow(String title, String repository, String workflowFile, String workflowName, String query) {
            this.title = title;

            ghLink = "https://github.com/" + repository + "/actions/workflows/" + workflowFile;
            if (query != null) {
                ghLink += "?query=" + query.replaceAll("=", "%3A").replaceAll(" ", "+");
            }

            shield = "https://img.shields.io/github/actions/workflow/status/" + repository + "/" + workflowFile + "?label=&style=flat-square";
            if (query != null) {
                shield += "&" + query.replaceAll(" ", "&");
            }
        }

        public String getTitle() {
            return title;
        }

        public String getGhLink() {
            return ghLink;
        }

        public String getShield() {
            return shield;
        }
    }

}
