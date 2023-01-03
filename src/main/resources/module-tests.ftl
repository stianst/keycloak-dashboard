<div class="tests">
<h2>Failed tests</h2>
<table>
<tr>
    <th>Test</th>
    <th>Count</th>
    <th>Jobs</th>
</tr>
<#list failedTests?keys as test>
<tr>
    <td>${test}</a></td>
    <td>${failedTests[test]?size}</td>
    <td>
<#list failedTests[test] as job>
<a href="#${job.failedRun.runId}">${job.failedRun.date?date} / ${job.failedRun.runId} / ${job.name}<#if job?has_next></a><br/></#if>
</#list>
    </td>
</tr>
</#list>
</table>
</div>

<div class="tests">
<h2>Flaky tests</h2>
<table>
<tr>
    <th>Issues</th>
    <th>Comments</th>
</tr>
<#list flakyTests as flakyTest>
<tr>
    <td class="title"><a href="https://github.com/keycloak/keycloak/issues/${flakyTest.number?string.computer}">${flakyTest.title}</a></td>
    <td class="count">${flakyTest.commentsCount}</td>
</tr>
</#list>
</table>
</div>

<#list failedRuns as run>
<a id="${run.runId}">
<div class="tests">
<h2></a><a href="https://github.com/keycloak/keycloak/actions/runs/${run.runId}">${run.date?date} - Run ${run.runId}</a></h2>

<#list run.failedJobs as job>
<h3>${job.name} - ${job.conclusion}</h3>
<#if job.errorLog?has_content>
${job.failedGoal}

<pre>
<#list job.errorLog as log>
${log}
</#list>
</pre>
</#if>
</#list>
</div>
</#list>