<#import "template.ftl" as template>

<@template.page>
    <div class="modal-float"><#include "module-workflows.ftl"></div>
    <div class="modal-float"><#include "module-tests-recently-failed.ftl"></div>
    <div class="modal-float"><#include "module-prs.ftl"></div>
    <div class="modal-float"><#include "module-bugs.ftl"></div>
</@template.page>