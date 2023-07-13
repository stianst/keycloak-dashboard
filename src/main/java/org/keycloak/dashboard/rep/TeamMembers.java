package org.keycloak.dashboard.rep;

import java.util.HashMap;
import java.util.List;

public class TeamMembers extends HashMap<String, List<String>> {

    public boolean isDeveloper(String login) {
        return get("developers").contains(login);
    }

}
