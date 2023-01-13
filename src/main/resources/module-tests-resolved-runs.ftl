<a id="failed-runs"></a>
<div class="modal">
    <div class="header">
        Resolved runs
    </div>
    <div class="body">
        <ul class="body-menu">
            <#list resolvedRuns as run>
            <li> <a href="#resolved-run-${run.runId}" class="nowrap">${run.date?date} - ${run.runId}</a></li>
            </#list>
        </ul>

        <#list resolvedRuns as run>
        <a id="failed-run-${run.runId}"></a>
        <div class="section-heading">
            <a href="https://github.com/keycloak/keycloak/actions/runs/${run.runId}">${run.date?date} - ${run.runId}</a>
        </div>
        <table>
            <#list run.failedJobs as job>
            <tr>
                <td class="size20">${job.name}</td>
                <td class="size10">${job.conclusion}<td>
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
        </table>
        </#list>
    </div>
</div>