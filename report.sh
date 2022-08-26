#!/bin/bash

MAX_PRS=200
MAX_AGE="12"

DATE_EXPIRED=`date -d "-$MAX_AGE month" +%Y-%m-%d`

PR_COUNT=`gh api -X GET search/issues -f q="repo:keycloak/keycloak is:pr is:open" -f per_page=1 -q .total_count`
PR_EXPIRED_COUNT=`gh api -X GET search/issues -f q="repo:keycloak/keycloak is:pr is:open created:<$DATE_EXPIRED" -f per_page=1 -q .total_count`

BUG_COUNT=`gh api -X GET search/issues -f q="repo:keycloak/keycloak is:issue is:open label:kind/bug -label:status/triage" -f per_page=1 -q .total_count`
BUG_TRIAGE_COUNT=`gh api -X GET search/issues -f q="repo:keycloak/keycloak is:issue is:open label:kind/bug label:status/triage" -f per_page=1 -q .total_count`

echo "# Keycloak Community Dashboard"
echo ""

echo ""
echo "## Warnings"
echo ""

if [ "$PR_COUNT" -ge "$MAX_PRS" ]; then
  echo "* Too many open PRs";
fi

if [ "$PR_EXPIRED_COUNT" -gt "0" ]; then
  echo "* Some PRs have been around for too long"
fi

echo ""
echo "## GitHub Workflows"
echo ""
echo "* Nightly release: ![GitHub Workflow Status](https://img.shields.io/github/workflow/status/keycloak-rel/keycloak-rel/Release%20Nightly)"
echo "* Keycloak CI: ![GitHub Workflow Status (event)](https://img.shields.io/github/workflow/status/keycloak/keycloak/Keycloak%20CI?event=schedule&branch=main)"
echo "* Keycloak Operator CI: ![GitHub Workflow Status](https://img.shields.io/github/workflow/status/keycloak/keycloak/Keycloak%20Operator%20CI?event=schedule&branch=main)"
echo "* CodeQL JS Adapter: ![GitHub Workflow Status](https://img.shields.io/github/workflow/status/keycloak/keycloak/CodeQL%20JS%20Adapter?event=schedule&branch=main)"
echo "* CodeQL Java: ![GitHub Workflow Status](https://img.shields.io/github/workflow/status/keycloak/keycloak/CodeQL%20Java?event=schedule&branch=main)"
echo "* CodeQL Themes: ![GitHub Workflow Status](https://img.shields.io/github/workflow/status/keycloak/keycloak/CodeQL%20Themes?event=schedule&branch=main)"
echo "* Snyk: ![GitHub Workflow Status](https://img.shields.io/github/workflow/status/keycloak/keycloak/Snyk?event=schedule&branch=main)"
echo "* Trivy: ![GitHub Workflow Status](https://img.shields.io/github/workflow/status/keycloak/keycloak/Trivy?event=schedule&branch=main)"

echo ""
echo "## PRs"
echo ""
echo "* [Total PRs](https://github.com/keycloak/keycloak/pulls): $PR_COUNT"
echo ""
echo "* [Older than $MAX_AGE months](https://github.com/keycloak/keycloak/pulls?q=created%3A<$DATE_EXPIRED): $PR_EXPIRED_COUNT"

echo ""
echo "## Bugs"
echo ""
echo "* [Open bugs](https://github.com/keycloak/keycloak/issues?q=is%3Aissue+is%3Aopen+label%3Akind%2Fbug+-label%3Astatus%2Ftriage+): $BUG_COUNT"
echo "* [Non-triaged bugs](https://github.com/keycloak/keycloak/issues?q=is%3Aissue+is%3Aopen+label%3Akind%2Fbug+label%3Astatus%2Ftriage): $BUG_TRIAGE_COUNT"
echo ""
