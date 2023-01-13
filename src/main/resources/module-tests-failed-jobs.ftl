<a id="failed-jobs"></a>
<div class="modal">
    <div class="header">
        Failed jobs
    </div>
    <div class="body">
        <ul class="body-menu">
            <#list failedJobs?keys as jobName>
            <li><a href="#failed-job-${failedJobs[jobName][0].anchor}">${jobName}</a></li>
            </#list>
        </ul>

        <#list failedJobs?keys as jobName>
        <a id="failed-job-${failedJobs[jobName][0].anchor}">
        <div class="section-heading">
            ${jobName}
        </div>
        <table>
            <#list failedJobs[jobName] as job>
            <tr>
                <td class="size10">${job.failedRun.date?date}</td>
                <td class="size10"><a href="#failed-run-${job.failedRun.runId}">${job.failedRun.runId}</a></td>
                <td class="size20"><#if job.profileName?has_content>${job.profileName}</#if><td>
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
            </td>
            </tr>
            </#list>
        </table>
        </#list>
    </div>
</div>

