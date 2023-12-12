<a id="recently-failed"></a>
<div class="header">
    Recently failed jobs
</div>
<div class="body">
    <table>
        <#list recentFailedJobs as job>
        <tr>
            <td class="size10">${job.failedRun.date?date}</td>
            <td class="size10">${job.failedRun.event!}</td>
            <td class="size10"><a href="https://github.com/keycloak/keycloak/actions/runs/${job.failedRun.runId}<#if job.failedRun.attempt?has_content>/attempts/${job.failedRun.attempt}</#if>">${job.failedRun.runId}</a></td>
            <td class="size10 warn">${job.conclusion}</td>
            <td><a href="#failed-job-${job.anchor}">${job.name}</a></td>
        </tr>
    </#list>
    </table>
</div>