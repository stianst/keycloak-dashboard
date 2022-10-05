<!DOCTYPE html>
<html>
<head>
    <title>Keycloak Dashboard</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>

<table>
<tr>
    <th colspan="2">GitHub Workflows</th>
</tr>
<#list workflows as workflow>
<tr>
    <td><a href="${workflow.ghLink}">${workflow.title}</a></td>
    <td><img src="${workflow.shield}"/></td>
</tr>
</#list>
</table>

<table>
<tr>
    <th colspan="2">PRs</th>
</tr>
<#list prStats as prStat>
<tr>
    <td><a href="${prStat.ghLink}">${prStat.title}</a></td>
    <td class="${prStat.cssClasses}">${prStat.count}</td>
</tr>
</#list>
</table>

<table>
<tr>
    <th colspan="2">Bugs</th>
</tr>
<#list bugStats as bugStat>
<tr>
    <td><a href="${bugStat.ghLink}">${bugStat.title}</a></td>
    <td class="${bugStat.cssClasses}">${bugStat.count}</td>
</tr>
</#list>
</table>

<table>
<tr>
    <th>Bugs per area</th>
    <th>Open</th>
    <th>Triage</th>
</tr>
<#list bugAreaStats as bugAreaStat>
<tr>
    <td><a href="${bugAreaStat.ghLink}">${bugAreaStat.title}</a></td>
    <td class="${bugAreaStat.openCssClasses}"><a href="${bugAreaStat.ghOpenLink}">${bugAreaStat.open}</a></td>
    <td class="${bugAreaStat.triageCssClasses}"><a href="${bugAreaStat.ghTriageLink}">${bugAreaStat.triage}</a></td>
</tr>
</#list>
</table>

</body>
</html>