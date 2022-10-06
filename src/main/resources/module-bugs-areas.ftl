<table>
<tr>
    <th>Bugs: Areas</th>
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