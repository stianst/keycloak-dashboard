#!/bin/bash -e

MAX_PRS=200

DATE_6_EXPIRED=`date -d "-6 month" +%Y-%m-%d`
DATE_12_EXPIRED=`date -d "-12 month" +%Y-%m-%d`
DATE_18_EXPIRED=`date -d "-18 month" +%Y-%m-%d`
DATE_8_DAYS_AGO=`date -d "-8 day" +%Y-%m-%d`
DATE_31_DAYS_AGO=`date -d "-31 day" +%Y-%m-%d`

PR_COUNT=`gh api -X GET search/issues -f q="repo:keycloak/keycloak is:pr is:open" -f per_page=1 -q .total_count`
PR_EXPIRED_6_COUNT=`gh api -X GET search/issues -f q="repo:keycloak/keycloak is:pr is:open created:<$DATE_6_EXPIRED" -f per_page=1 -q .total_count`
PR_EXPIRED_12_COUNT=`gh api -X GET search/issues -f q="repo:keycloak/keycloak is:pr is:open created:<$DATE_12_EXPIRED" -f per_page=1 -q .total_count`
PR_EXPIRED_18_COUNT=`gh api -X GET search/issues -f q="repo:keycloak/keycloak is:pr is:open created:<$DATE_18_EXPIRED" -f per_page=1 -q .total_count`

PR_IMPORTANT_COUNT=`gh api -X GET search/issues -f q="repo:keycloak/keycloak is:pr is:open label:priority/important,priority/critical" -f per_page=1 -q .total_count`

PR_CLOSED_LAST_7=`gh api -X GET search/issues -f q="repo:keycloak/keycloak is:pr is:closed closed:>$DATE_8_DAYS_AGO" -f per_page=1 -q .total_count`
PR_OPENED_LAST_7=`gh api -X GET search/issues -f q="repo:keycloak/keycloak is:pr created:>$DATE_8_DAYS_AGO" -f per_page=1 -q .total_count`
PR_CLOSED_LAST_30=`gh api -X GET search/issues -f q="repo:keycloak/keycloak is:pr is:closed closed:>$DATE_31_DAYS_AGO" -f per_page=1 -q .total_count`
PR_OPENED_LAST_30=`gh api -X GET search/issues -f q="repo:keycloak/keycloak is:pr created:>$DATE_31_DAYS_AGO" -f per_page=1 -q .total_count`

BUG_COUNT=`gh api -X GET search/issues -f q="repo:keycloak/keycloak is:issue is:open label:kind/bug -label:status/triage" -f per_page=1 -q .total_count`
BUG_TRIAGE_COUNT=`gh api -X GET search/issues -f q="repo:keycloak/keycloak is:issue is:open label:kind/bug label:status/triage" -f per_page=1 -q .total_count`

echo "|Warnings|"
echo "|--------|"
if [ "$PR_IMPORTANT_COUNT" -ge 0 ]; then
  echo "|Several open priority PRs (#$PR_IMPORTANT_COUNT)|"
fi
if [ "$PR_COUNT" -ge "$MAX_PRS" ]; then
  echo "|More than $MAX_PRS open PRs (#$PR_COUNT) |";
fi
if [ "$PR_EXPIRED_12_COUNT" -gt "0" ]; then
  echo "|PRs have been open for more than 12 months (#$PR_EXPIRED_12_COUNT)|"
fi
echo ""

echo "|GitHub Workflows| |"
echo "|----------------|-|"
echo "|[Nightly release](https://github.com/keycloak-rel/keycloak-rel/actions/workflows/release-nightly.yml)|![GitHub Workflow Status](https://img.shields.io/github/workflow/status/keycloak-rel/keycloak-rel/Release%20Nightly)|"
echo "|[Keycloak CI](https://github.com/keycloak/keycloak/actions/workflows/ci.yml?query=event%3Aschedule+branch%3Amain)|![GitHub Workflow Status (event)](https://img.shields.io/github/workflow/status/keycloak/keycloak/Keycloak%20CI?event=schedule&branch=main)|"
echo "|[Keycloak Operator CI](https://github.com/keycloak/keycloak/actions/workflows/operator-ci.yml?query=event%3Aschedule+branch%3Amain)|![GitHub Workflow Status](https://img.shields.io/github/workflow/status/keycloak/keycloak/Keycloak%20Operator%20CI?event=schedule&branch=main)|"
echo "|[Keycloak QuickStarts CI](https://github.com/keycloak/keycloak-quickstarts/actions?query=event%3Aschedule)|![GitHub Workflow Status (event)](https://img.shields.io/github/workflow/status/keycloak/keycloak-quickstarts/Quickstarts%20tests?event=schedule)|"
echo "|[Keycloak UI CI](https://github.com/keycloak/keycloak/actions/workflows/ci.yml?query=event%3Aschedule+branch%3Amain)|![GitHub Workflow Status (event)](https://img.shields.io/github/workflow/status/keycloak/keycloak/Keycloak%20CI?event=schedule&branch=main)|"
echo "|[CodeQL JS Adapter](https://github.com/keycloak/keycloak-ui/actions/workflows/cypress.yml?query=event%3Aschedule+branch%3Amain)|![GitHub Workflow Status](https://img.shields.io/github/workflow/status/keycloak/keycloak-ui/Cypress?event=schedule&branch=main)|"
echo "|[CodeQL Java](https://github.com/keycloak/keycloak/actions/workflows/codeql-java-analysis.yml?query=event%3Aschedule+branch%3Amain)|![GitHub Workflow Status](https://img.shields.io/github/workflow/status/keycloak/keycloak/CodeQL%20Java?event=schedule&branch=main)|"
echo "|[CodeQL Themes](https://github.com/keycloak/keycloak/actions/workflows/codeql-theme-analysis.yml?query=event%3Aschedule+branch%3Amain)|![GitHub Workflow Status](https://img.shields.io/github/workflow/status/keycloak/keycloak/CodeQL%20Themes?event=schedule&branch=main)|"
echo "|[Snyk](https://github.com/keycloak/keycloak/actions/workflows/snyk.yml?query=event%3Aschedule+branch%3Amain)|![GitHub Workflow Status](https://img.shields.io/github/workflow/status/keycloak/keycloak/Snyk?event=schedule&branch=main)|"
echo "|[Trivy](https://github.com/keycloak/keycloak/actions/workflows/trivy-analysis.yml?query=event%3Aschedule+branch%3Amain)|![GitHub Workflow Status](https://img.shields.io/github/workflow/status/keycloak/keycloak/Trivy?event=schedule&branch=main)|"
echo ""

echo "|PRs| |"
echo "|---|-|"
echo "|[Open PRs](https://github.com/keycloak/keycloak/pulls)|$PR_COUNT|"
echo "|[Priority PRs](https://github.com/keycloak/keycloak/pulls?q=is%3Apr+is%3Aopen+label%3Apriority%2Fimportant%2Cpriority%2Fcritical)|$PR_IMPORTANT_COUNT|"
echo "|[Older than 6 months](https://github.com/keycloak/keycloak/pulls?q=is%3Apr+is%3Aopen+created%3A%3C$DATE_6_EXPIRED)|$PR_EXPIRED_6_COUNT|"
echo "|[Older than 12 months](https://github.com/keycloak/keycloak/pulls?q=is%3Apr+is%3Aopen+created%3A%3C$DATE_12_EXPIRED)|$PR_EXPIRED_12_COUNT|"
echo "|[Older than 18 months](https://github.com/keycloak/keycloak/pulls?q=is%3Apr+is%3Aopen+created%3A%3C$DATE_18_EXPIRED)|$PR_EXPIRED_18_COUNT|"
echo "|[Created last 7 days](https://github.com/keycloak/keycloak/pulls?q=is%3Apr+created%3A%3E$DATE_8_DAYS_AGO)|$PR_OPENED_LAST_7|"
echo "|[Closed last 7 days](https://github.com/keycloak/keycloak/pulls?q=is%3Apr+is%3Aclosed+closed%3A%3E$DATE_8_DAYS_AGO)|$PR_CLOSED_LAST_7|"
echo "|[Created last 30 days](https://github.com/keycloak/keycloak/pulls?q=is%3Apr+created%3A%3E$DATE_31_DAYS_AGO)|$PR_OPENED_LAST_30|"
echo "|[Closed last 30 days](https://github.com/keycloak/keycloak/pulls?q=is%3Apr+is%3Aclosed+closed%3A%3E$DATE_31_DAYS_AGO)|$PR_CLOSED_LAST_30|"
echo ""

echo "|Bugs| |"
echo "|----|-|"
echo "|[Open bugs](https://github.com/keycloak/keycloak/issues?q=is%3Aissue+is%3Aopen+label%3Akind%2Fbug+-label%3Astatus%2Ftriage+)|$BUG_COUNT|"
echo "|[Non-triaged bugs](https://github.com/keycloak/keycloak/issues?q=is%3Aissue+is%3Aopen+label%3Akind%2Fbug+label%3Astatus%2Ftriage)|$BUG_TRIAGE_COUNT|"
echo ""
