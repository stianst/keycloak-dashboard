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
    <td class="title"><a href="${workflow.ghLink}">${workflow.title}</a></td>
    <td class="shield"><a href="${workflow.ghLink}"><img src="${workflow.shield}"/></a></td>
</tr>
</#list>
</table>

<table>
<tr>
    <th colspan="2">PRs</th>
</tr>
<#list prStats as prStat>
<tr>
    <td class="title"><a href="${prStat.ghLink}">${prStat.title}</a></td>
    <td class="count ${prStat.cssClasses}"><a href="${prStat.ghLink}">${prStat.count}</a></td>
</tr>
</#list>
</table>

<table>
<tr>
    <th colspan="2">Bugs</th>
</tr>
<#list bugStats as bugStat>
<tr>
    <td class="title"><a href="${bugStat.ghLink}">${bugStat.title}</a></td>
    <td class="count ${bugStat.cssClasses}"><a href="${bugStat.ghLink}">${bugStat.count}</a></td>
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
    <td class="title"><a href="${bugAreaStat.ghLink}">${bugAreaStat.title}</a></td>
    <td class="count ${bugAreaStat.openCssClasses}"><a href="${bugAreaStat.ghOpenLink}">${bugAreaStat.open}</a></td>
    <td class="count ${bugAreaStat.triageCssClasses}"><a href="${bugAreaStat.ghTriageLink}">${bugAreaStat.triage}</a></td>
</tr>
</#list>
</table>

</body>
</html>