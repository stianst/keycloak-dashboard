    <div class="header">
        Bugs
    </div>
    <div class="body">
        <table>
            <tr>
                <th>Description</th>
                <th class="center">Open</th>
                <th class="center">Closed</th>
            </tr>
            <#list bugStats as bugStat>
            <tr>
                <td class="title"><a href="${bugStat.openGhLink}">${bugStat.title}</a></td>
                <td class="count ${bugStat.openCssClasses} center"><a href="${bugStat.openGhLink}">${bugStat.openCount}</a></td>
                <#if bugStat.closedCount?has_content>
                <td class="count ${bugStat.closedCssClasses} center"><a href="${bugStat.closedGhLink}">${bugStat.closedCount}</a></td>
                <#else>
                <td class="count"></td>
                </#if>
            </tr>
            </#list>
        </table>
    </div>