<div class="module">
<table>
<tr>
    <th colspan="2">GitHub Workflows</th>
</tr>
<#list workflows as workflow>
<tr>
    <td class="title"><a href="${workflow.ghLink}">${workflow.title}</a></td>
    <td class="shield"><a href="${workflow.ghLink}"><img src="${workflow.shield}"/></a></td>
</tr>
</#list>
</table>
</div>