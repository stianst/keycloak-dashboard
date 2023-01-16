<div class="header">
    Bugs per area
</div>
<div class="body">
    <table>
        <#list bugAreaStats as bugAreaStat>
        <tr>
            <td class="title"><a href="${bugAreaStat.ghLink}">${bugAreaStat.title}</a></td>
            <td class="count ${bugAreaStat.openCssClasses}"><a href="${bugAreaStat.ghOpenLink}">${bugAreaStat.open}</a></td>
            <td class="count ${bugAreaStat.triageCssClasses}"><a href="${bugAreaStat.ghTriageLink}">${bugAreaStat.triage}</a></td>
        </tr>
        </#list>
    </table>
</div>
