<div class="header">
    Bugs per area
</div>
<div class="body">
    <table>
        <tr>
            <th>Area</th>
            <th class="center">Open</th>
            <th class="center">Triage</th>
        </tr>
        <#list bugAreaStats as bugAreaStat>
        <tr>
            <td class="title"><a href="${bugAreaStat.ghLink}">${bugAreaStat.title}</a></td>
            <td class="count ${bugAreaStat.openCssClasses} size5 center"><a href="${bugAreaStat.ghOpenLink}">${bugAreaStat.open}</a></td>
            <td class="count ${bugAreaStat.triageCssClasses} size5 center"><a href="${bugAreaStat.ghTriageLink}">${bugAreaStat.triage}</a></td>
        </tr>
        </#list>
    </table>
</div>
