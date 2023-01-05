<div class="sub-menu">
    <a href="#recently-failed">Recently failed</a>|
    <a href="#failed-tests">Failed tests</a>|
    <a href="#flaky-tests">Flaky tests</a>|
    <a href="#failed-jobs">Failed jobs</a>|
    <a href="#failed-runs">Failed runs</a>|
    <a href="#resolved-runs">Resolved runs</a>
</div>

<a id="recently-failed"></a>
<h1 class="tests">Recently failed jobs</h1>
<div class="tests">
<table>
<tr>
    <th>Date</th>
    <th>Run</th>
    <th>Job</th>
    <th>Conclusion</th>
</tr>
<#list recentFailedJobs as job>
    <tr>
        <td>${job.failedRun.date?date}</td>
        <td><a href="#failed-run-${job.failedRun.runId}">${job.failedRun.runId}</a></td>
        <td><a href="#failed-job-${job.anchor}">${job.name}</a></td>
        <td>${job.conclusion}</td>
    </tr>
</#list>
</table>
</div>

<a id="failed-tests"></a>
<h1 class="tests">Failed tests</h1>
<div class="tests">
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
${job.failedRun.date?date} / <a href="#failed-run-${job.failedRun.runId}">${job.failedRun.runId}</a> / <a href="#failed-job-${job.anchor}">${job.name}</a><#if job?has_next><br/></#if>
</#list>
    </td>
</tr>
</#list>
</table>
</div>

<a id="flaky-tests"></a>
<h1 class="tests">Flaky tests</h1>
<div class="tests">
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


<a id="failed-jobs"></a>
<h1 class="tests">Failed jobs</h1>
<div class="sub-menu">
    <#list failedJobs?keys as jobName>
        <a href="#failed-job-${failedJobs[jobName][0].anchor}">${jobName}</a><#if jobName?has_next>|</#if>
    </#list>
</div>
<#list failedJobs?keys as jobName>
<div class="tests">
<a id="failed-job-${failedJobs[jobName][0].anchor}"></a>
<h2>${jobName}</h2>

<#list failedJobs[jobName] as job>
<h3>${job.failedRun.date?date} / <a href="#failed-run-${job.failedRun.runId}">${job.failedRun.runId}</a> <#if job.profileName?has_content>/ ${job.profileName}</#if> / ${job.conclusion}</h3>
<#if job.errorLog?has_content>
<pre>
<#list job.errorLog as log>
${log}
</#list>

${job.failedGoal}
</pre>
</#if>
</#list>
</div>
</#list>


<a id="failed-runs"></a>
<h1 class="tests">Failed runs</h1>
<div class="sub-menu">
    <#list failedRuns as run>
        <a href="#failed-run-${run.runId}" class="nowrap">${run.date?date} - ${run.runId}</a><#if run?has_next>|</#if>
    </#list>
</div>
<#list failedRuns as run>
<div class="tests">
<a id="failed-run-${run.runId}"></a>
<h2><a href="https://github.com/keycloak/keycloak/actions/runs/${run.runId}">${run.date?date} - ${run.runId}</a></h2>

<#list run.failedJobs as job>
<h3>${job.name} - ${job.conclusion}</h3>
<#if job.errorLog?has_content>
<pre>
<#list job.errorLog as log>
${log}
</#list>

${job.failedGoal}
</pre>
</#if>
</#list>
</div>
</#list>


<a id="resolved-runs"></a>
<h1 class="tests">Resolved runs</h1>
<div class="sub-menu">
    <#list resolvedRuns as run>
        <a href="#failed-run-${run.runId}" class="nowrap">${run.date?date} - ${run.runId}</a><#if run?has_next>|</#if>
    </#list>
</div>
<#list resolvedRuns as run>
<div class="tests">
<a id="resolved-run-${run.runId}"></a>
<h2><a href="https://github.com/keycloak/keycloak/actions/runs/${run.runId}">${run.date?date} - ${run.runId}</a></h2>

<#list run.failedJobs as job>
<h3>${job.name} - ${job.conclusion}</h3>
<#if job.errorLog?has_content>
<pre>
<#list job.errorLog as log>
${log}
</#list>

${job.failedGoal}
</pre>
</#if>
</#list>
</div>
</#list>