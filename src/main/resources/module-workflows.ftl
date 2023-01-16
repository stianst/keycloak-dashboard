<div class="header">
    GitHub Workflows
</div>
<div class="body">
    <table>
        <#list workflows as workflow>
        <tr>
            <td class="title"><a href="${workflow.ghLink}">${workflow.title}</a></td>
            <td class="shield nopadding"><a href="${workflow.ghLink}"><img src="${workflow.shield}"/></a></td>
        </tr>
        </#list>
    </table>
</div>