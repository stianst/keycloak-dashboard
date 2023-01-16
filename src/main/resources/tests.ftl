<#import "template.ftl" as template>
<@template.page title="Tests">

<ul class="sub-menu">
    <li><a href="#recently-failed">Recently failed</a></li>
    <li><a href="#failed-tests">Failed tests</a></li>
    <li><a href="#flaky-tests">Flaky tests</a></li>
    <li><a href="#failed-jobs">Failed jobs</a></li>
    <li><a href="#failed-runs">Failed runs</a></li>
    <li><a href="#resolved-runs">Resolved runs</a></li>
</ul>

<div class="modal"><#include "module-tests-recently-failed.ftl"></div>
<div class="modal"><#include "module-tests-failed-tests.ftl"></div>
<div class="modal"><#include "module-tests-flaky-tests.ftl"></div>
<div class="modal"><#include "module-tests-failed-jobs.ftl"></div>
<div class="modal"><#include "module-tests-failed-runs.ftl"></div>
<div class="modal"><#include "module-tests-resolved-runs.ftl"></div>

</@template.page>