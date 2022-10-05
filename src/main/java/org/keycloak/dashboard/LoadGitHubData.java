package org.keycloak.dashboard;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.keycloak.dashboard.gh.GitHubLoader;
import org.keycloak.dashboard.rep.GitHubData;

import java.io.File;
import java.io.IOException;

public class LoadGitHubData {

    public static void main(String[] args) throws IOException {
        LoadGitHubData data = new LoadGitHubData();
        data.createData();
    }

    public void createData() throws IOException {
        GitHubLoader gitHubLoader = new GitHubLoader();
        GitHubData gitHubData = gitHubLoader.query();

        ObjectMapper objectMapper = new ObjectMapper();

        File output = new File("data.json");
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(output, gitHubData);

        System.out.println("Created data: " + output.toURI());
    }

}
