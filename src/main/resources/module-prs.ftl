<table>
<tr>
    <th colspan="2">PRs</th>
</tr>
<#list prStats as prStat>
<tr>
    <td class="title"><a href="${prStat.ghLink}">${prStat.title}</a></td>
    <td class="count ${prStat.cssClasses}"><a href="${prStat.ghLink}">${prStat.count}</a></td>
</tr>
</#list>
</table>