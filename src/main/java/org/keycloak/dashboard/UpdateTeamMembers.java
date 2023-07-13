package org.keycloak.dashboard;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.keycloak.dashboard.gh.GitHubLoader;
import org.keycloak.dashboard.rep.TeamMembers;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class UpdateTeamMembers {

    public static void main(String[] args) throws IOException {
        GitHubLoader gitHubLoader = new GitHubLoader();
        List<String> devTeam = gitHubLoader.queryDevTeam();

        TeamMembers teamMembers = new TeamMembers();
        teamMembers.put("developers", devTeam);

        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        yamlMapper.writeValue(new File("team-members.yml"), teamMembers);
    }

}
