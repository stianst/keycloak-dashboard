<div class="header">
    Retried PRs last 7 days
</div>
<div class="body">
    <table>
        <tr>
            <th>Date</th>
            <th>PR</th>
            <th>Run</th>
            <th>Attempt</th>
        </tr>
        <#list retriedPRs as retriedPR>
        <tr>
            <td>${retriedPR.date?date}</td>
            <td><a href="https://github.com/keycloak/keycloak/pull/${retriedPR.prNumber?string.computer}">#${retriedPR.prNumber?string.computer}</a></td>
            <td><a href="https://github.com/keycloak/keycloak/actions/runs/${retriedPR.runId}">${retriedPR.runId}</a></td>
            <td><a href="https://github.com/keycloak/keycloak/actions/runs/${retriedPR.runId}/attempts/${retriedPR.attempt}">${retriedPR.attempt}</a></td>
        </tr>
        </#list>
    </table>
</div>