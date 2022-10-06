<#macro page title="">
<!DOCTYPE html>
<html>
<head>
    <title><#if title?has_content>${title} - </#if>Keycloak Dashboard</title>
    <link rel="stylesheet" href="styles.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>

<div class="menu">
    <a href="<#if publish>./<#else>index.html</#if>">Overview</a>|<a href="workflows<#if !publish>.html</#if>">Workflows</a>|<a href="prs<#if !publish>.html</#if>">PRs</a>|<a href="bugs<#if !publish>.html</#if>">Bugs</a>
</div>

<#nested>

</body>
</html>
</#macro>