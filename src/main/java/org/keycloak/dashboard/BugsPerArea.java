package org.keycloak.dashboard;

import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHLabel;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedSearchIterable;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class BugsPerArea {

    private GitHub gitHub;

    private int open;
    private int missingArea;
    private int nonTriaged;
    private Map<String, Area> areas = new LinkedHashMap<>();

    public BugsPerArea(GitHub gitHub, boolean mockGitHub) {
        this.gitHub = gitHub;
    }

    public void print() throws IOException {
        init();

//        report.tableHead("Bugs", "");
//        report.tableRow("[Open bugs](" + link("is:issue is:open label:kind/bug -label:status/triage") + ")", Integer.toString(open));
//        report.tableRow("[Non-triaged](" + link("is:issue is:open label:kind/bug label:status/triage") + ")", Integer.toString(nonTriaged));
//        report.tableRow("[Bugs without area label](" + link("is:issue is:open label:kind/bug" + minusAreaLabels()) + ")", Integer.toString(missingArea));
//
//        report.tableHead("Bugs per area", "Open", "Triage");
//
//        areas.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(
//                e -> {
//                    String open = "[" + e.getValue().open + "](" + link("is:issue is:open label:kind/bug -label:status/triage label:" + e.getKey() + ")");
//                    String triage = "[" + e.getValue().triage + "](" + link("is:issue is:open label:kind/bug label:status/triage label:" + e.getKey() + ")");
//                    report.tableRow(e.getKey(), open, triage);
//                }
//        );
    }

    private String link(String query) {
        query = query.replaceAll("=", "%3A").replaceAll("<", "%3C").replaceAll(" ", "+");
        return "https://github.com/keycloak/keycloak/issues?q=is%3Aissue+" + query;
    }

    private  void init() {
        System.out.print("Fetching bugs; ");

        PagedSearchIterable<GHIssue> list = gitHub.searchIssues().q("repo:keycloak/keycloak is:issue is:open label:kind/bug").list();
        list.withPageSize(100);

        int count = 0;
        for (GHIssue i : list) {
            Set<String> issuesAreas = new HashSet<>();
            boolean triage = false;
            for (GHLabel l : i.getLabels()) {
                if (l.getName().startsWith("area/")) {
                    issuesAreas.add(l.getName());
                } else if (l.getName().equals("status/triage")) {
                    triage = true;
                }
            }

            if (triage) {
                nonTriaged++;
            } else {
                open++;
            }

            if (issuesAreas.isEmpty()) {
                missingArea++;
            } else {
                for (String a : issuesAreas) {
                    Area area = areas.computeIfAbsent(a, s -> new Area(a));
                    if (triage) {
                        area.triage++;
                    } else {
                        area.open++;
                    }
                }
            }

            count++;
            if (count % 100 == 0) {
                System.out.print(".");
            }
        }

        System.out.println();
    }

    private String minusAreaLabels() throws IOException {
        GHRepository repository = gitHub.getRepository("keycloak/keycloak");
        StringBuilder sb = new StringBuilder();
        for (GHLabel l : repository.listLabels()) {
            if (l.getName().startsWith("area/")) {
                sb.append(" -label:" + l.getName());
            }
        }
        return sb.toString();
    }

    private static class Area {
        private String area;
        private int triage;
        private int open;

        public Area(String area) {
            this.area = area;
        }
    }

}