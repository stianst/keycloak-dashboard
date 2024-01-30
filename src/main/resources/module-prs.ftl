<div class="header">
    Pull Requests
</div>
<div class="body">
    <table>
        <tr>
            <th>Description</th>
            <th class="center">Open</th>
            <th class="center">Closed</th>
        </tr>
        <#list prStats as prStat>
        <tr>
            <td class="title"><a href="${prStat.openGhLink}">${prStat.title}</a></td>
            <td class="count ${prStat.openCssClasses} center"><a href="${prStat.openGhLink}">${prStat.openCount}</a></td>
            <#if prStat.closedCount?has_content>
            <td class="closedCount ${prStat.closedCssClasses} center"><a href="${prStat.closedGhLink}">${prStat.closedCount}</a></td>
            <#else>
            <td class="count"></td>
        </#if>
        </tr>
        </#list>
    </table>
</div>