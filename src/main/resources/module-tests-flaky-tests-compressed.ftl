<a id="flaky-tests"></a>
<div class="header">
    Flaky tests
</div>
<div class="body">
    <table>
        <#list flakyTests as flakyTest>
        <tr>
            <td class="nopadding">
                <table class="nested">
                    <tr>
                        <td>Created: ${flakyTest.createdAt?date}</td>
                        <td>Updated: ${flakyTest.updatedAt?date}</td>
                        <td>Count: ${flakyTest.count}</td>
                    </tr>
                    <tr>
                        <td colspan="3">
                            <a href="https://github.com/keycloak/keycloak/issues/${flakyTest.number?string.computer}">
                                ${flakyTest.package}<br/>
                                ${flakyTest.testClass}<br/>
                                <b>${flakyTest.testMethod}</b>
                            </a>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        </#list>
    </table>
</div>
