package org.keycloak.dashboard;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.keycloak.dashboard.ci.ResolvedIssue;
import org.keycloak.dashboard.ci.ResolvedIssues;
import org.keycloak.dashboard.gh.TokenUtil;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ReportFailedJob {

    public static void main(String[] args) throws IOException {
        String[] jobRefs = System.getProperty("job").split(",");
        for (int i = 0; i < jobRefs.length; i++) {
            jobRefs[i] = jobRefs[i].trim();
            if (jobRefs[i].indexOf('/') == -1) {
                jobRefs[i] = jobRefs[i] + "/*";
            }
        }

        final Integer issueNumber = Integer.parseInt(System.getProperty("issue").trim());

        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        yamlMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        ResolvedIssues resolvedIssues = yamlMapper.readValue(new File("failed-jobs-reporting.yml"), ResolvedIssues.class);

        boolean updated = false;

        Optional<ResolvedIssue> existingIssue = resolvedIssues.getIssues().stream().filter(i -> issueNumber.equals(i.getIssue())).findFirst();
        if (existingIssue.isPresent()) {
            ResolvedIssue resolvedIssue = existingIssue.get();
            for (String jobRef : jobRefs) {
                Optional<String> existingResolves = resolvedIssue.getResolves().stream().filter(r -> Objects.equals(r, jobRef)).findFirst();
                if (existingResolves.isPresent()) {
                    System.err.println("job [" + jobRef + "] already linked to issue [" + issueNumber + "]");
                } else {
                    resolvedIssue.getResolves().add(0, jobRef);
                    updated = true;
                    System.out.println("job [" + jobRef + "] added to existing issue [" + resolvedIssue.getId() + " " + resolvedIssue.getDescription() + "]");
                }
            }
        } else {
            GitHub gitHub = GitHubBuilder.fromEnvironment().withJwtToken(TokenUtil.token()).build();

            GHIssue ghIssue = null;
            try {
                 ghIssue = gitHub.getRepository("keycloak/keycloak").getIssue(issueNumber);
            } catch (FileNotFoundException e) {
                System.err.println("issue [" + issueNumber + "] not found");
                System.exit(1);
            }

            ResolvedIssue resolvedIssue = new ResolvedIssue();
            resolvedIssue.setIssue(issueNumber);
            resolvedIssue.setId(Long.toString(System.currentTimeMillis()));
            resolvedIssue.setDescription(ghIssue.getTitle());

            List<String> resolves = new LinkedList<>();
            for (String jobRef : jobRefs) {
                resolves.add(jobRef);
                System.out.println("job [" + jobRef + "] added to new issue [" + resolvedIssue.getId() + " " + resolvedIssue.getDescription() + "]");
            }
            resolvedIssue.setResolves(resolves);

            resolvedIssues.getIssues().add(0, resolvedIssue);
            updated = true;
        }

        if (updated) {
            yamlMapper.writeValue(new File("failed-jobs-reporting.yml"), resolvedIssues);
        }
    }

}
