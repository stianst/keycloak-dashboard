package org.keycloak.dashboard.reports.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.keycloak.dashboard.rep.GitHubData;
import org.keycloak.dashboard.rep.TeamMembers;

import java.io.File;
import java.io.IOException;

public class Data {

    public static GitHubData loadGitHubData() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File("data.json"), GitHubData.class);
    }

    public static TeamMembers loadTeamMembers() throws IOException {
        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        return yamlMapper.readValue(new File("team-members.yml"), TeamMembers.class);
    }

}
