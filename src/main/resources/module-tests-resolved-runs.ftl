<a id="resolved-runs"></a>
<div class="header">
    Resolved runs
</div>
<div class="body">
    <ul class="body-menu">
        <#list resolvedRuns as run>
        <li> <a href="#resolved-run-${run.runId}" class="nowrap">${run.date?date} - ${run.runId}</a></li>
        </#list>
    </ul>

    <table>
        <#list resolvedRuns as run>
        <tr>
            <th colspan="4">
                <a id="resolved-run-${run.runId}"></a>
                <a href="https://github.com/keycloak/keycloak/actions/runs/${run.runId}">${run.date?date} - ${run.runId}</a>
            </th>
        </tr>
        <#list run.failedJobs as job>
        <tr>
            <td class="size20">${job.name}</td>
            <td>
            <#if job.resolvedBy??>
                <#if job.resolvedBy.issue?has_content><a href="https://github.com/keycloak/keycloak/issues/${job.resolvedBy.issue?string.computer}">#${job.resolvedBy.issue?string.computer}</a></#if>
                <#if job.resolvedBy.resolution?has_content>${job.resolvedBy.resolution}</#if>
            </#if>
            </td>
            <td class="size10">${job.conclusion}</td>
            <#if job.errorLog?has_content>
            <td class="failures">
                <div class="failures">
                    <#list job.errorLog as log>
                    ${log}&nbsp;<br/><br/>
                </#list>
                ${job.failedGoal}
                </div>
            </td>
            <#else>
            <td></td>
            </#if>
        </tr>
        </#list>
    </#list>
    </table>
</div>