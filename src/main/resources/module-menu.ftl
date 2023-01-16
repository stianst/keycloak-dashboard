<table>
    <tr>
        <th colspan="2">Bugs</th>
    </tr>
    <#list bugStats as bugStat>
    <tr>
        <td class="title"><a href="${bugStat.ghLink}">${bugStat.title}</a></td>
        <td class="count ${bugStat.cssClasses}"><a href="${bugStat.ghLink}">${bugStat.count}</a></td>
    </tr>
    </#list>
</table>