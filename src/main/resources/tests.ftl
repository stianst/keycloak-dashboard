<#import "template.ftl" as template>

<@template.page title="Tests">

<div class="tests">
<table>
<tr>
    <th>Failed test</th>
    <th>Count</th>
</tr>
<#list failedTests?keys as test>
<tr>
    <td class="title">${test}</a></td>
    <td class="count">${failedTests[test]}</td>
</tr>
</#list>
</table>
</div>

<#list failedRuns as run>
<div class="tests">
<h2><a href="https://github.com/keycloak/keycloak/actions/runs/${run.runId}">Run ${run.runId}</a></h2>

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

</@template.page>