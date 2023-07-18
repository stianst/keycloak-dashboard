package org.keycloak.dashboard.rep;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class TeamMembers extends HashMap<String, List<String>> {

    private static List<String> bots = new LinkedList<>();
    static {
        bots.add("dependabot[bot]");
        bots.add("keycloak-github-bot[bot]");
    }

    public boolean isDeveloper(String login, boolean includePast) {
        return get("developers").contains(login) || get("testing").contains(login);
    }

    public boolean isBot(String login) {
        return bots.contains(login);
    }

}
