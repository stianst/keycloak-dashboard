<div class="header">
    Retried PRs last 30 days
</div>
<div class="body">
    <table>
        <tr>
            <th>Date</th>
            <th>PR</th>
            <th>Resolved by</th>
            <th>Run</th>
            <th>Attempt</th>
        </tr>
        <#list retriedPRs as retriedPR>
        <tr>
            <td>${retriedPR.date?date}</td>
            <td><#if retriedPR.prNumber?has_content><a href="https://github.com/keycloak/keycloak/pull/${retriedPR.prNumber?string.computer}">#${retriedPR.prNumber?string.computer}</a></#if></td>
            <td>
            <#if retriedPR.resolvedBy??>
                <#if retriedPR.resolvedBy.issue?has_content><a href="https://github.com/keycloak/keycloak/issues/${retriedPR.resolvedBy.issue?string.computer}">#${retriedPR.resolvedBy.issue?string.computer}</a></#if>
                <#if retriedPR.resolvedBy.resolution?has_content>${retriedPR.resolvedBy.resolution}</#if>
            </#if>
            </td>
            <td><a href="https://github.com/keycloak/keycloak/actions/runs/${retriedPR.runId}">${retriedPR.runId}</a></td>
            <td><a href="https://github.com/keycloak/keycloak/actions/runs/${retriedPR.runId}/attempts/${retriedPR.attempt}">${retriedPR.attempt}</a></td>
        </tr>
        </#list>
    </table>
</div>