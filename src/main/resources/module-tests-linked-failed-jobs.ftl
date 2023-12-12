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
            <th>Run</th>
            <th>Event</th>
            <th>Profile</th>
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
            <td class="size10">${job.failedRun.date?date}</td>
            <td class="size10"><a href="https://github.com/keycloak/keycloak/actions/runs/${job.failedRun.runId}<#if job.failedRun.attempt?has_content>/attempts/${job.failedRun.attempt}</#if>">${job.failedRun.runId}</a></td>
            <td class="size10">${job.failedRun.event!}</td>
            <td class="size10">
                <#if job.resolvedBy??>
                    <#if job.resolvedBy.issue?has_content><a href="https://github.com/keycloak/keycloak/issues/${job.resolvedBy.issue?string.computer}">#${job.resolvedBy.issue?string.computer}</a></#if>
                    <#if job.resolvedBy.resolution?has_content>${job.resolvedBy.resolution}</#if>
                </#if>
            </td>
            <td class="size20"><#if job.profileName?has_content>${job.profileName}</#if></td>
            <td class="size10">${job.conclusion}</td>
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
