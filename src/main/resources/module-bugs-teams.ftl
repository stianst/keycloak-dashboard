<div class="header">
    Bugs per team
</div>
<div class="body">
    <table>
        <tr>
            <th>Area</th>
            <th>Open</th>
            <th>Triage</th>
        </tr>
        <#list bugTeamStats as bugTeamStat>
        <tr>
            <td class="title"><a href="${bugTeamStat.ghLink}">${bugTeamStat.title}</a></td>
            <td class="count ${bugTeamStat.openCssClasses} size5"><a href="${bugTeamStat.ghOpenLink}">${bugTeamStat.open}</a></td>
            <td class="count ${bugTeamStat.triageCssClasses} size5"><a href="${bugTeamStat.ghTriageLink}">${bugTeamStat.triage}</a></td>
        </tr>
        </#list>
    </table>
</div>
