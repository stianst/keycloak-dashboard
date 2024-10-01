<a id="flaky-tests"></a>
<div class="header">
    Flaky tests
</div>
<div class="body">
    <table>
        <tr>
            <#list flakyTestCountsByTeam?keys as team>
            <th>${team}</th>
            </#list>
        </tr>
        <tr>
            <#list flakyTestCountsByTeam?values as count>
            <td>${count}</td>
            </#list>
        </tr>
    </table>

    <table>
        <tr>
            <th>Package</th>
            <th>Class</th>
            <th>Method</th>
            <th>Team</th>
            <th>Status</th>
            <th>Created</th>
            <th>Updated</th>
            <th class="center">Count</th>
        </tr>
        <#list flakyTests as flakyTest>
        <tr>
            <td><a href="https://github.com/keycloak/keycloak/issues/${flakyTest.number?string.computer}">${flakyTest.package}</a></td>
            <td><a href="https://github.com/keycloak/keycloak/issues/${flakyTest.number?string.computer}">${flakyTest.testClass}</a></td>
            <td><a href="https://github.com/keycloak/keycloak/issues/${flakyTest.number?string.computer}">${flakyTest.testMethod}</a></td>
            <td>${flakyTest.teams?join(", ")}</td>
            <td>${flakyTest.status}</td>
            <td class="${flakyTest.createdAtClass}">${flakyTest.createdAt?date}</td>
            <td class="${flakyTest.updatedAtClass}">${flakyTest.updatedAt?date}</td>
            <td class="count ${flakyTest.countClass} center">${flakyTest.count}</td>
        </tr>
        </#list>
    </table>
</div>
