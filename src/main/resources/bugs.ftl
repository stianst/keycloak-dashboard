<#import "template.ftl" as template>

<@template.page title="Bugs">
<div class="content">
    <div class="modal-float">
        <div class="modal-float-margin"><#include "module-bugs.ftl"></div>
    </div>
    <div class="modal-float">
        <div class="modal-float-margin"><#include "module-bugs-teams.ftl"></div>
    </div>
    <div class="modal-float">
        <div class="modal-float-margin"><#include "module-bugs-areas.ftl"></div>
    </div>
</div>
</@template.page>