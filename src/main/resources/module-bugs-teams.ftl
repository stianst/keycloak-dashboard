<div class="header">
    Bugs per team
</div>
<div class="body">
    <table>
        <tr>
            <th>Area</th>
            <th class="center">${nextRelease}</th>
            <th class="center">Open</th>
            <th class="center">Triage</th>
            <th class="center">Backlog</th>
            <th class="center">Triage Backlog</th>
        </tr>
        <#list bugTeamStats as bugTeamStat>
        <tr>
            <td class="title"><a href="${bugTeamStat.ghLink}">${bugTeamStat.title}</a></td>
            <td class="count ${bugTeamStat.nextCssClasses} size5 center"><a href="${bugTeamStat.ghNextReleaseLink}">${bugTeamStat.nextRelease}</a></td>
            <td class="count ${bugTeamStat.openCssClasses} size5 center"><a href="${bugTeamStat.ghOpenLink}">${bugTeamStat.open}</a></td>
            <td class="count ${bugTeamStat.triageCssClasses} size5 center"><a href="${bugTeamStat.ghTriageLink}">${bugTeamStat.triage}</a></td>
            <td class="count ${bugTeamStat.backlogCssClasses} size5 center"><a href="${bugTeamStat.ghBacklogLink}">${bugTeamStat.backlog}</a></td>
            <td class="count ${bugTeamStat.backlogTriageCssClasses} size5 center"><a href="${bugTeamStat.ghTriageBacklogLink}">${bugTeamStat.backlogTriage}</a></td>
        </tr>
        </#list>
    </table>
</div>
