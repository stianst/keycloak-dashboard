<a id="failed-tests"></a>
<div class="header">
    Failed tests
</div>
<div class="body">
    <table>
        <tr>
            <th>Test</th>
            <th>Count</th>
            <th>Failures</th>
        </tr>
        <#list failedTests?keys as test>
        <tr>
            <td>${test}</a></td>
            <td class="size10">${failedTests[test]?size}</td>
            <td class="nopadding">
                <table class="nested">
                    <#list failedTests[test] as job>
                    <tr>
                        <td class="size10">${job.failedRun.date?date}</td>
                        <td class="size10"><a href="#failed-run-${job.failedRun.runId}">${job.failedRun.runId}</a></td>
                        <td><a href="#failed-job-${job.anchor}">${job.name}</a></td>
                    </tr>
                    </#list>
                    </tr>
                </table>
            </td>
        </tr>
        </#list>
    </table>
</div>