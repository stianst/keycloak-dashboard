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
        workflows.add(new Workflow("Keycloak QuickStarts CI", "keycloak/keycloak-quickstarts", "ci.yml", "Quickstarts tests", "event=schedule branch=latest"));
        workflows.add(new Workflow("Keycloak UI CI", "keycloak/keycloak-ui", "cypress.yml", "Cypress", "event=schedule branch=main"));

        workflows.add(new Workflow("CodeQL JS Adapter", "keycloak/keycloak", "codeql-js-adapter-analysis.yml", "CodeQL JS Adapter", "event=schedule branch=main"));
        workflows.add(new Workflow("CodeQL Java", "keycloak/keycloak", "codeql-java-analysis.yml", "CodeQL Java", "event=schedule branch=main"));
        workflows.add(new Workflow("CodeQL Themes", "keycloak/keycloak", "codeql-theme-analysis.yml", "CodeQL Themes", "event=schedule branch=main"));
        workflows.add(new Workflow("Snyk", "keycloak/keycloak", "snyk.yml", "Snyk", "event=schedule branch=main"));
        workflows.add(new Workflow("Trivy", "keycloak/keycloak", "trivy-analysis.yml", "Trivy", "event=schedule branch=main"));
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
