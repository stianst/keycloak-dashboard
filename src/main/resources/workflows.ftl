<#import "template.ftl" as template>

<@template.page title="Workflows">
<div class="content">
    <div class="modal-float">
        <div class="modal-float-margin"><#include "module-workflows.ftl"></div>
    </div>
    <div class="modal-float">
        <div class="modal-float-margin"><#include "module-prs-wait-times.ftl"></div>
    </div>
</div>
</@template.page>