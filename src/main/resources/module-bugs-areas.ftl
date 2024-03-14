<div class="header">
    Bugs per area
</div>
<div class="body">
    <table>
        <tr>
            <th>Area</th>
            <#list bugAreaStats[0].columns as col>
            <th class="center">${col.label}</th>
            </#list>
        </tr>
        <#list bugAreaStats as bugAreaStat>
        <tr>
            <td><a href="${bugAreaStat.areaGhLink}">${bugAreaStat.title}</a></td>
            <#list bugAreaStat.columns as col>
            <td class="count ${col.cssClasses} center"><a href="${col.ghLink}">${col.count}</a></td>
            </#list>
        </tr>
        </#list>
    </table>
</div>
