<table>
<tr>
    <th>Bugs: Teams</th>
    <th>Open</th>
    <th>Triage</th>
</tr>
<#list bugTeamStats as bugTeamStat>
<tr>
    <td class="title"><a href="${bugTeamStat.ghLink}">${bugTeamStat.title}</a></td>
    <td class="count ${bugTeamStat.openCssClasses}"><a href="${bugTeamStat.ghOpenLink}">${bugTeamStat.open}</a></td>
    <td class="count ${bugTeamStat.triageCssClasses}"><a href="${bugTeamStat.ghTriageLink}">${bugTeamStat.triage}</a></td>
</tr>
</#list>
</table>