<a id="unlinked-failed-jobs"></a>
<div class="header">
    Non-reported failed jobs
</div>
<div class="body">
    <ul class="body-menu">
        <#list failedJobs?keys as jobName>
        <li><a href="#failed-job-${failedJobs[jobName][0].anchor}">${jobName}</a></li>
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
        <#list failedJobs?keys as jobName>
        <tr>
            <th colspan="7">
                <a id="failed-job-${failedJobs[jobName][0].anchor}"></a>
                ${jobName}
            </th>
        </tr>
        <#list failedJobs[jobName] as job>
        <tr>
            <td class="size10">${job.failedRun.date?date}</td>
            <td class="size10"><a href="https://github.com/keycloak/keycloak/actions/runs/${job.failedRun.runId}<#if job.failedRun.attempt?has_content>/attempts/${job.failedRun.attempt}</#if>">${job.failedRun.runId}</a></td>
            <td class="size10">${job.failedRun.event!}</td>
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
