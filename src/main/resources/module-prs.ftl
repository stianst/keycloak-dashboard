<div class="header">
    Pull Requests
</div>
<div class="body">
    <table>
        <tr>
            <th>Description</th>
            <th>Open</th>
            <th>Closed</th>
        </tr>
        <#list prStats as prStat>
        <tr>
            <td class="title"><a href="${prStat.openGhLink}">${prStat.title}</a></td>
            <td class="count ${prStat.openCssClasses} size5"><a href="${prStat.openGhLink}">${prStat.openCount}</a></td>
            <#if prStat.closedCount?has_content>
            <td class="closedCount ${prStat.closedCssClasses} size5"><a href="${prStat.closedGhLink}">${prStat.closedCount}</a></td>
            <#else>
            <td class="size5"></td>
        </#if>
        </tr>
        </#list>
    </table>
</div>