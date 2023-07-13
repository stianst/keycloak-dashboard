package org.keycloak.dashboard.reports;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.keycloak.dashboard.rep.GitHubData;
import org.keycloak.dashboard.rep.TeamMembers;

import java.io.File;
import java.io.IOException;

public class PRsFromTeam {

    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        GitHubData data = objectMapper.readValue(new File("data.json"), GitHubData.class);

        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        TeamMembers teamMembers = yamlMapper.readValue(new File("team-members.yml"), TeamMembers.class);


    }

}
