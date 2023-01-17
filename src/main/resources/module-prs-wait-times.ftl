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
            <th>&#60;90m</th>
            <th>Average</th>
            <th>Avg. Fast</th>
            <th>Avg. Slow</th>
            <th>Slowest</th>
        </tr>
        <#list workflowWaitTimes as workflowWaitTime>
        <tr>
            <td>${workflowWaitTime.month}</td>
            <td>${workflowWaitTime.count}</td>
            <td>${workflowWaitTime.min}</td>
            <td>${workflowWaitTime.max}</td>
            <td>${workflowWaitTime.fastPercentage}%</td>
            <td>${workflowWaitTime.average}</td>
            <td>${workflowWaitTime.averageFast!}</td>
            <td>${workflowWaitTime.averageSlow!}</td>
            <td>
                <#list workflowWaitTime.slowest as slow>
                    <a href="https://github.com/keycloak/keycloak/pull/${slow.number?c}">#${slow.number?c} (${slow.minutes})</a><#sep>, </#sep>
                </#list>
            </td>
        </tr>
        </#list>
    </table>
</div>