# Keycloak Dashboard

## Generating GitHub data

LoadGitHubData queries data from GitHub and caches it in [data.json]. You can either run this class from your IDE, or 
with Maven:

```
mvn clean install exec:java -Pgithub
```

One thing to remember is GitHub rate-limits API requests, so updating the data to frequently can temporarily block you.

Generally when contributing you should not include the updates to `data.json`, as this is updated every 6 hours by
a GitHub Action.

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

## Reporting a failed job

Run the following to link a failed job to a GitHub Issue:

```
mvn clean install exec:java -Preport-failed-job -Djob="<JOB REF>" -Dissue=<ISSUE NUMBER>
```

For example: If it's the only job failing for the workflow run (or all failed jobs are linked to the same issue) run:

```
mvn clean install exec:java -Preport-failed-job -Djob="7384490397" -Dissue=123456
```

For example: If there are multiple failed jobs for the workflow run then specify the job as well:

```
mvn clean install exec:java -Preport-failed-job -Djob="5651128174/WebAuthn IT (firefox)" -Dissue=123456
```
