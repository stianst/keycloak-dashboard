package org.keycloak.dashboard;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.keycloak.dashboard.gh.GitHubLoader;
import org.keycloak.dashboard.rep.TeamMembers;
import org.keycloak.dashboard.reports.utils.Data;

import java.io.File;
import java.io.IOException;

public class UpdateTeamMembers {

    public static void main(String[] args) throws IOException {
        GitHubLoader gitHubLoader = new GitHubLoader();

        TeamMembers teamMembers = Data.loadTeamMembers();

        teamMembers.put("developers", gitHubLoader.queryTeam("developers"));
        teamMembers.put("testing", gitHubLoader.queryTeam("testing"));

        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        yamlMapper.writeValue(new File("team-members.yml"), teamMembers);
    }

}
