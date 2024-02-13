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
            <td>${workflow.name}</td>
            <#list workflowStatus.branches as branch>
            <#if workflow.branchStatus[branch]??>
                <td class="count center <#if workflow.branchStatus[branch].conclusion == "success">success</#if> <#if workflow.branchStatus[branch].conclusion == "failure">error</#if>">
                    <a href="https://github.com/${workflow.branchStatus[branch].repository.fullName}/actions/runs/${workflow.branchStatus[branch].id?c}">${workflow.branchStatus[branch].conclusion}</a>
                </td>
            <#else><td></td>
            </#if>
            </#list>
        </tr>
        </#list>
    </table>
</div>