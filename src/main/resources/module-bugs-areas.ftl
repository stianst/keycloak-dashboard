<div class="header">
    Bugs per area
</div>
<div class="body">
    <table>
        <tr>
            <th>Area</th>
            <th class="center">${nextRelease}</th>
            <th class="center">Open</th>
            <th class="center">Triage</th>
            <th class="center"><p>Cleanup</p><p>Backlog</p></th>
            <th class="center"><p>Cleanup</p>Triage Backlog</p></th>
        </tr>
        <#list bugAreaStats as bugAreaStat>
        <tr>
            <td class="title"><a href="${bugAreaStat.ghLink}">${bugAreaStat.title}</a></td>
            <td class="count ${bugAreaStat.nextCssClasses} center"><a href="${bugAreaStat.ghNextReleaseLink}">${bugAreaStat.nextRelease}</a></td>
            <td class="count ${bugAreaStat.openCssClasses} center"><a href="${bugAreaStat.ghOpenLink}">${bugAreaStat.open}</a></td>
            <td class="count ${bugAreaStat.triageCssClasses} center"><a href="${bugAreaStat.ghTriageLink}">${bugAreaStat.triage}</a></td>
            <td class="count ${bugAreaStat.backlogCssClasses} center"><a href="${bugAreaStat.ghBacklogLink}">${bugAreaStat.backlog}</a></td>
            <td class="count ${bugAreaStat.backlogTriageCssClasses} center"><a href="${bugAreaStat.ghTriageBacklogLink}">${bugAreaStat.backlogTriage}</a></td>
        </tr>
        </#list>
    </table>
</div>
