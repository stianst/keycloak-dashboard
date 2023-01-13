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

<#include "module-tests-recently-failed.ftl">
<#include "module-tests-failed-tests.ftl">
<#include "module-tests-flaky-tests.ftl">
<#include "module-tests-failed-jobs.ftl">
<#include "module-tests-failed-runs.ftl">
<#include "module-tests-resolved-runs.ftl">

</@template.page>