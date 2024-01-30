<div class="header">
    Bugs per team
</div>
<div class="body">
    <table>
        <tr>
            <th>Team</th>
            <#list bugTeamStats[0].columns as col>
            <th class="center">${col.label}</th>
            </#list>
        </tr>
        <#list bugTeamStats as bugTeamStat>
        <tr>
            <td><a href="${bugTeamStat.teamGhLink}">${bugTeamStat.title}</a></td>
            <#list bugTeamStat.columns as col>
            <td class="count ${col.cssClasses} center"><a href="${col.ghLink}">${col.count}</a></td>
            </#list>
        </tr>
        </#list>
    </table>
</div>
