<#import "template.ftl" as template>

<@template.page title="PRs">
<div class="content">
    <div class="modal-float">
        <div class="modal-float-margin"><#include "module-prs.ftl"></div>
    </div>
    <div class="modal-float size50">
        <div class="modal-float-margin"><#include "module-prs-wait-times.ftl"></div>
    </div>
</div>
</@template.page>