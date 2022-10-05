# Keycloak Dashboard

## Generating GitHub data

LoadGitHubData queries data from GitHub and caches it in [data.json]. You can either run this class from your IDE, or 
with Maven:

```
mvn clean install exec:java -Pgithub
```

One thing to remember is GitHub rate-limits API requests, so updating the data to frequently can temporarily block you.

## Generating the dashboard

Dashboard uses the cached data to create the static dashboard. You can either run this class from your IDE, or with
Maven:

```
mvn clean install exec:java -Pdashboard
```

As the dashboard works on the cached data it is also more convinient to update the dashboard, unless of course you want
to add additional data. In this case you should add the new data first, then start using it in the dashboard second.

Styles are in `docs/styles.css` alongside the generated dashboard to make it as easiy as possible to tune the styles
without having to generate the dashboard repeatedly.