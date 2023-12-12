<#import "template.ftl" as template>
<@template.page title="Tests">

<ul class="sub-menu">
    <li><a href="#unlinked-failed-jobs">Non-reported failed jobs</a></li>
    <li><a href="#linked-failed-jobs">Reported Failed jobs</a></li>
    <li><a href="#flaky-tests">Flaky tests</a></li>
    <li><a href="#resolved-runs">Resolved failed jobs</a></li>
</ul>

<div class="content">
    <div class="modal"><#include "module-tests-unlinked-failed-jobs.ftl"></div>
    <div class="modal"><#include "module-tests-linked-failed-jobs.ftl"></div>
    <div class="modal"><#include "module-tests-flaky-tests.ftl"></div>
    <div class="modal"><#include "module-tests-resolved-runs.ftl"></div>
</div>

</@template.page>