<#import "template.ftl" as template>

<@template.page>
    <div class="content">
        <div class="modal-float">
            <div class="modal-float-margin"><#include "module-workflows.ftl"></div>
        </div>
        <div class="modal-float">
            <div class="modal-float-margin"><#include "module-tests-recently-failed.ftl"></div>
            <div class="modal-float-margin"><#include "module-tests-flaky-tests.ftl"></div>
        </div>
        <div class="modal-float">
            <div class="modal-float-margin"><#include "module-prs.ftl"></div>
        </div>
        <div class="modal-float">
            <div class="modal-float-margin"><#include "module-bugs.ftl"></div>
        </div>
    </div>
</@template.page>