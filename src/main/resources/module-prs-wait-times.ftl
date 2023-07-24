<div class="header">
    Pull Request CI Waiting times
</div>
<div class="body">
    <table>
        <tr>
            <th>Month</th>
            <th>Count</th>
            <th>Min</th>
            <th>Max</th>
            <th>Average</th>
            <th>&#60;60m</th>
            <th>&#60;90m</th>
            <th>&#60;120m</th>
            <th>&#60;180m</th>
            <th>Slowest (excluding recently retried)</th>
        </tr>
        <#list workflowWaitTimes as workflowWaitTime>
        <tr>
            <td>${workflowWaitTime.month}</td>
            <td>${workflowWaitTime.count}</td>
            <td>${workflowWaitTime.min}</td>
            <td>${workflowWaitTime.max}</td>
            <td>${workflowWaitTime.average}</td>
            <td>${workflowWaitTime.percentage60m}%</td>
            <td>${workflowWaitTime.percentage90m}%</td>
            <td>${workflowWaitTime.percentage120m}%</td>
            <td>${workflowWaitTime.percentage180m}%</td>
            <td>
                <#list workflowWaitTime.slowest as slow>
                    <a href="https://github.com/keycloak/keycloak/pull/${slow.number?c}">${slow.minutes?c}</a><#sep>, </#sep>
                </#list>
            </td>
        </tr>
        </#list>
    </table>
</div>