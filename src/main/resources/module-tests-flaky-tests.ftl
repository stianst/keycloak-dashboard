<a id="flaky-tests"></a>
<div class="header">
    Flaky tests
</div>
<div class="body">
    <table>
        <tr>
            <th>Package</th>
            <th>Class</th>
            <th>Method</th>
            <th>Milestone</th>
            <th>Labels</th>
            <th>Created</th>
            <th>Updated</th>
            <th class="center">Count</th>
        </tr>
        <#list flakyTests as flakyTest>
        <tr>
            <td><a href="https://github.com/keycloak/keycloak/issues/${flakyTest.number?string.computer}">${flakyTest.package}</a></td>
            <td><a href="https://github.com/keycloak/keycloak/issues/${flakyTest.number?string.computer}">${flakyTest.testClass}</a></td>
            <td><a href="https://github.com/keycloak/keycloak/issues/${flakyTest.number?string.computer}">${flakyTest.testMethod}</a></td>
            <td>${flakyTest.milestone!}</td>
            <td>${flakyTest.labels?join(", ")}</td>
            <td>${flakyTest.createdAt?date}</td>
            <td>${flakyTest.updatedAt?date}</td>
            <td class="center">${flakyTest.count}</td>
        </tr>
        </#list>
    </table>
</div>
