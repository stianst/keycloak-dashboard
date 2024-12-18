<a id="linked-failed-jobs"></a>
<div class="header">
    Reported failed jobs
</div>
<div class="body">
    <ul class="body-menu">
        <#list linkedFailedJobs?keys as jobName>
        <li><a href="#failed-job-${linkedFailedJobs[jobName][0].anchor}">${jobName}</a></li>
        </#list>
    </ul>

    <table>
        <tr>
            <th>Date</th>
            <th>Workflow</th>
            <th>Run</th>
            <th>Event</th>
            <th>Job</th>
            <th>Conclusion</th>
            <th>Error</th>
        </tr>
        <#list linkedFailedJobs?keys as jobName>
        <tr>
            <th colspan="7">
                <a id="failed-job-${linkedFailedJobs[jobName][0].anchor}"></a>
                ${jobName} /
                <#if linkedFailedJobs[jobName][0].resolvedBy.issue?has_content><a href="https://github.com/keycloak/keycloak/issues/${linkedFailedJobs[jobName][0].resolvedBy.issue?string.computer}">#${linkedFailedJobs[jobName][0].resolvedBy.issue?string.computer}</a></#if>
                <#if linkedFailedJobs[jobName][0].resolvedBy.resolution?has_content>${linkedFailedJobs[jobName][0].resolvedBy.resolution}</#if>
                / ${linkedFailedJobs[jobName][0].resolvedBy.description!}
            </th>
        </tr>
        <#list linkedFailedJobs[jobName] as job>
        <tr>
            <td>${job.failedRun.date?date}</td>
            <td>${job.failedRun.workflow}</td>
            <td><a href="https://github.com/keycloak/keycloak/actions/runs/${job.failedRun.runId}<#if job.failedRun.attempt?has_content>/attempts/${job.failedRun.attempt}</#if>">${job.failedRun.runId}</a></td>
            <td>${job.failedRun.event!}</td>
            <td>${job.name}</td>
            <td>${job.conclusion}</td>
        <#if job.errorLog?has_content>
        <td class="failures">
            <div class="failures">
                <#list job.errorLog as log>
                ${log}&nbsp;<br/><br/>
            </#list>
            ${job.failedGoal!}
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
