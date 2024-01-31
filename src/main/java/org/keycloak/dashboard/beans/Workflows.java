package org.keycloak.dashboard.beans;

import java.util.LinkedList;
import java.util.List;

public class Workflows {

    List<Workflow> workflows;

    public Workflows() {
        workflows = new LinkedList<>();

        workflows.add(new Workflow("Nightly release", "keycloak-rel/keycloak-rel", "release-nightly.yml", "branch=main"));

        workflows.add(new Workflow("Keycloak CI", "keycloak/keycloak", "ci.yml", "branch=main"));
        workflows.add(new Workflow("Keycloak CI - Quarkus nightly", "keycloak/keycloak", "ci.yml", "branch=quarkus-next"));
        workflows.add(new Workflow("Keycloak Documentation", "keycloak/keycloak", "documentation.yml", "branch=main"));
        workflows.add(new Workflow("Keycloak Guides", "keycloak/keycloak", "guides.yml", "branch=main"));
        workflows.add(new Workflow("Keycloak Operator CI", "keycloak/keycloak", "operator-ci.yml", "branch=main"));
        workflows.add(new Workflow("Keycloak Operator CI - Quarkus nightly", "keycloak/keycloak", "operator-ci.yml", "branch=quarkus-next"));
        workflows.add(new Workflow("Keycloak JavaScript CI", "keycloak/keycloak", "js-ci.yml", "branch=main"));

        workflows.add(new Workflow("Keycloak QuickStarts CI", "keycloak/keycloak-quickstarts", "ci.yml", "branch=main"));

        workflows.add(new Workflow("CodeQL", "keycloak/keycloak", "codeql-analysis.yml", "branch=main"));
        workflows.add(new Workflow("Snyk", "keycloak/keycloak", "snyk-analysis.yml", "branch=main"));
        workflows.add(new Workflow("Trivy", "keycloak/keycloak", "trivy-analysis.yml", "branch=main"));

        workflows.add(new Workflow("Schedule nightly workflows", "keycloak/keycloak", "schedule-nightly.yml", "branch=main"));
        workflows.add(new Workflow("Schedule QuickStarts nightly", "keycloak/keycloak-quickstarts", "schedule.yml", ""));

        workflows.add(new Workflow("Update dashboard", "stianst/keycloak-dashboard", "data.yml", "branch=main"));
    }

    public List<Workflow> getWorkflows() {
        return workflows;
    }

    public static class Workflow {

        private String title;
        private String ghLink;
        private String shield;

        public Workflow(String title, String repository, String workflowFile, String query) {
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
