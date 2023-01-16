    <div class="header">
        Bugs
    </div>
    <div class="body">
        <table>
            <tr>
                <th>Description</th>
                <th>Open</th>
                <th>Closed</th>
            </tr>
            <#list bugStats as bugStat>
            <tr>
                <td class="title"><a href="${bugStat.openGhLink}">${bugStat.title}</a></td>
                <td class="openCount ${bugStat.openCssClasses} size5"><a href="${bugStat.openGhLink}">${bugStat.openCount}</a></td>
                <#if bugStat.closedCount?has_content>
                <td class="closedCount ${bugStat.closedCssClasses} size5"><a href="${bugStat.closedGhLink}">${bugStat.closedCount}</a></td>
                <#else>
                <td class="size5"></td>
                </#if>
            </tr>
            </#list>
        </table>
    </div>