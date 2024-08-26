<div class="header">
    GitHub Workflows
</div>
<div class="body">
    <table>
        <tr>
            <th>&nbsp;</th>
            <#list workflowStatus.branches as branch>
            <th>${branch}</th>
            </#list>
        </tr>
        <#list workflowStatus.workflows as workflow>
        <tr>
            <td><a href="${workflow.workflowUrl}">${workflow.repository}/${workflow.file}</a></td>
            <#list workflowStatus.branches as branch>
            <#if workflow.branchStatus[branch]??>
                <td class="count center <#if workflow.branchStatus[branch].conclusion == "success">success</#if> <#if workflow.branchStatus[branch].conclusion == "failure">error</#if>">
                    <a href="${workflow.getBranchUrl(branch)}">${workflow.branchStatus[branch].conclusion}</a>
                </td>
            <#else><td></td>
            </#if>
            </#list>
        </tr>
        </#list>
    </table>
</div>