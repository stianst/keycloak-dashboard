<#macro page title="">
<!DOCTYPE html>
<html>
<head>
    <title><#if title?has_content>${title} - </#if>Keycloak Dashboard</title>
    <link rel="stylesheet" href="styles.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv='cache-control' content='no-cache'>
    <meta http-equiv='expires' content='0'>
    <meta http-equiv='pragma' content='no-cache'>
</head>
<body>

<div class="menu">
    <a href="<#if publish>./<#else>index.html</#if>">Overview</a>|<a href="workflows<#if !publish>.html</#if>">Workflows</a>|<a href="prs<#if !publish>.html</#if>">PRs</a>|<a href="bugs<#if !publish>.html</#if>">Bugs</a>|<a href="tests<#if !publish>.html</#if>">Tests</a>
</div>

<#nested>

<div class="footer">Last updated: ${updatedDate?string["dd MMMM yyyy, HH:mm '('zzz')'"]}</div>
</body>
</html>
</#macro>