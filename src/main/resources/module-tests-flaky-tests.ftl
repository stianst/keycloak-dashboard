<a id="flaky-tests"></a>
<div class="header">
    Flaky tests
</div>
<div class="body">
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
