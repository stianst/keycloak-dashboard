package org.keycloak.dashboard;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.keycloak.dashboard.gh.GitHubLoader;
import org.keycloak.dashboard.rep.GitHubData;

import java.io.File;
import java.util.Date;

public class LoadGitHubData {

    public static void main(String[] args) throws Exception {
        LoadGitHubData data = new LoadGitHubData();
        data.createData();
    }

    public void createData() throws Exception {
        GitHubLoader gitHubLoader = new GitHubLoader();
        ObjectMapper objectMapper = new ObjectMapper();
        File dataFile = new File("data.json");

        GitHubData data;
        if (dataFile.isFile()) {
            data = objectMapper.readValue(dataFile, GitHubData.class);
            data = gitHubLoader.update(data);
        } else {
            data = gitHubLoader.load();
        }

        data.setUpdatedDate(new Date());

        objectMapper.writerWithDefaultPrettyPrinter().writeValue(dataFile, data);

        System.out.println("Created data: " + dataFile.toURI());
    }

}
