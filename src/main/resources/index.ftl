<#import "template.ftl" as template>

<@template.page>
    <div class="modal-wrapper"><#include "module-workflows.ftl"></div>
    <div class="modal-wrapper"><#include "module-tests-recently-failed.ftl"></div>
    <div class="modal-wrapper"><#include "module-prs.ftl"></div>
    <div class="modal-wrapper"><#include "module-bugs.ftl"></div>
</@template.page>