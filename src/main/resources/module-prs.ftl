<div class="header">
    Pull Requests
</div>
<div class="body">
    <table>
        <#list prStats as prStat>
        <tr>
            <td class="title"><a href="${prStat.ghLink}">${prStat.title}</a></td>
            <td class="count ${prStat.cssClasses}"><a href="${prStat.ghLink}">${prStat.count}</a></td>
        </tr>
        </#list>
    </table>
</div>