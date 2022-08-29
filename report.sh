#!/bin/bash

MAX_PRS=200

DATE_6_EXPIRED=`date -d "-6 month" +%Y-%m-%d`
DATE_12_EXPIRED=`date -d "-12 month" +%Y-%m-%d`
DATE_18_EXPIRED=`date -d "-18 month" +%Y-%m-%d`
DATE_8_DAYS_AGO=`date -d "-8 day" +%Y-%m-%d`

PR_COUNT=`gh api -X GET search/issues -f q="repo:keycloak/keycloak is:pr is:open" -f per_page=1 -q .total_count`
PR_EXPIRED_6_COUNT=`gh api -X GET search/issues -f q="repo:keycloak/keycloak is:pr is:open created:<$DATE_6_EXPIRED" -f per_page=1 -q .total_count`
PR_EXPIRED_12_COUNT=`gh api -X GET search/issues -f q="repo:keycloak/keycloak is:pr is:open created:<$DATE_12_EXPIRED" -f per_page=1 -q .total_count`
PR_EXPIRED_18_COUNT=`gh api -X GET search/issues -f q="repo:keycloak/keycloak is:pr is:open created:<$DATE_18_EXPIRED" -f per_page=1 -q .total_count`

PR_CLOSED_LAST_7=`gh api -X GET search/issues -f q="repo:keycloak/keycloak is:pr is:closed closed:>$DATE_8_DAYS_AGO" -f per_page=1 -q .total_count`
PR_OPENED_LAST_7=`gh api -X GET search/issues -f q="repo:keycloak/keycloak is:pr created:>$DATE_8_DAYS_AGO" -f per_page=1 -q .total_count`

BUG_COUNT=`gh api -X GET search/issues -f q="repo:keycloak/keycloak is:issue is:open label:kind/bug -label:status/triage" -f per_page=1 -q .total_count`
BUG_TRIAGE_COUNT=`gh api -X GET search/issues -f q="repo:keycloak/keycloak is:issue is:open label:kind/bug label:status/triage" -f per_page=1 -q .total_count`

echo "## Warnings"
echo ""

if [ "$PR_COUNT" -ge "$MAX_PRS" ]; then
  echo "* Too many open PRs";
fi

if [ "$PR_EXPIRED_12_COUNT" -gt "0" ]; then
  echo "* Some PRs have been around for too long"
fi

echo ""
echo "## GitHub Workflows"
echo ""
echo "* [Nightly release: ![GitHub Workflow Status](https://img.shields.io/github/workflow/status/keycloak-rel/keycloak-rel/Release%20Nightly)](https://github.com/keycloak-rel/keycloak-rel/actions/workflows/release-nightly.yml)"
echo "* [Keycloak CI: ![GitHub Workflow Status (event)](https://img.shields.io/github/workflow/status/keycloak/keycloak/Keycloak%20CI?event=schedule&branch=main)](https://github.com/keycloak/keycloak/actions/workflows/ci.yml?query=event%3Aschedule+branch%3Amain)"
echo "* [Keycloak Operator CI: ![GitHub Workflow Status](https://img.shields.io/github/workflow/status/keycloak/keycloak/Keycloak%20Operator%20CI?event=schedule&branch=main)](https://github.com/keycloak/keycloak/actions/workflows/operator-ci.yml?query=event%3Aschedule+branch%3Amain)"
echo "* [CodeQL JS Adapter: ![GitHub Workflow Status](https://img.shields.io/github/workflow/status/keycloak/keycloak/CodeQL%20JS%20Adapter?event=schedule&branch=main)](https://github.com/keycloak/keycloak/actions/workflows/codeql-js-adapter-analysis.yml?query=event%3Aschedule+branch%3Amain)"
echo "* [CodeQL Java: ![GitHub Workflow Status](https://img.shields.io/github/workflow/status/keycloak/keycloak/CodeQL%20Java?event=schedule&branch=main)](https://github.com/keycloak/keycloak/actions/workflows/codeql-java-analysis.yml?query=event%3Aschedule+branch%3Amain)"
echo "* [CodeQL Themes: ![GitHub Workflow Status](https://img.shields.io/github/workflow/status/keycloak/keycloak/CodeQL%20Themes?event=schedule&branch=main)](https://github.com/keycloak/keycloak/actions/workflows/codeql-theme-analysis.yml?query=event%3Aschedule+branch%3Amain)"
echo "* [Snyk: ![GitHub Workflow Status](https://img.shields.io/github/workflow/status/keycloak/keycloak/Snyk?event=schedule&branch=main)](https://github.com/keycloak/keycloak/actions/workflows/snyk.yml?query=event%3Aschedule+branch%3Amain)"
echo "* [Trivy: ![GitHub Workflow Status](https://img.shields.io/github/workflow/status/keycloak/keycloak/Trivy?event=schedule&branch=main)](https://github.com/keycloak/keycloak/actions/workflows/trivy-analysis.yml?query=event%3Aschedule+branch%3Amain)"

echo ""
echo "## PRs"
echo ""
echo "* [Open PRs](https://github.com/keycloak/keycloak/pulls): $PR_COUNT"
echo ""
echo "* [Older than 6 months](https://github.com/keycloak/keycloak/pulls?q=is%3Apr+is%3Aopen+created%3A%3C$DATE_6_EXPIRED): $PR_EXPIRED_6_COUNT"
echo "* [Older than 12 months](hhttps://github.com/keycloak/keycloak/pulls?q=is%3Apr+is%3Aopen+created%3A%3C$DATE_12_EXPIRED): $PR_EXPIRED_12_COUNT"
echo "* [Older than 18 months](https://github.com/keycloak/keycloak/pulls?q=is%3Apr+is%3Aopen+created%3A%3C$DATE_18_EXPIRED): $PR_EXPIRED_18_COUNT"
echo ""
echo "* [Created last 7 days](https://github.com/keycloak/keycloak/pulls?q=is%3Apr+created%3A%3E$DATE_8_DAYS_AGO): $PR_OPENED_LAST_7"
echo "* [Closed last 7 days](https://github.com/keycloak/keycloak/pulls?q=is%3Apr+is%3Aclosed+closed%3A%3E$DATE_8_DAYS_AGO): $PR_CLOSED_LAST_7"

echo ""
echo "## Bugs"
echo ""
echo "* [Open bugs](https://github.com/keycloak/keycloak/issues?q=is%3Aissue+is%3Aopen+label%3Akind%2Fbug+-label%3Astatus%2Ftriage+): $BUG_COUNT"
echo ""
echo "* [Non-triaged bugs](https://github.com/keycloak/keycloak/issues?q=is%3Aissue+is%3Aopen+label%3Akind%2Fbug+label%3Astatus%2Ftriage): $BUG_TRIAGE_COUNT"
echo ""
