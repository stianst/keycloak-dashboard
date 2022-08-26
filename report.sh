#!/bin/bash

MAX_PRS=200
MAX_AGE="12"

DATE_EXPIRED=`date -d "-$MAX_AGE month" +%Y-%m-%d`

PR_COUNT=`gh api -X GET search/issues -f q="repo:keycloak/keycloak is:pr is:open" -f per_page=1 -q .total_count`
PR_EXPIRED_COUNT=`gh api -X GET search/issues -f q="repo:keycloak/keycloak is:pr is:open created:<$DATE_EXPIRED" -f per_page=1 -q .total_count`

echo "# Keycloak Community Dashboard"
echo ""
echo "## PRs"
echo ""
echo "[Total PRs](https://github.com/keycloak/keycloak/pulls): $MAX_PRS"
echo "[Older than $MAX_AGE months](https://github.com/keycloak/keycloak/pulls?q=created%3A<$DATE_EXPIRED): $PR_EXPIRED_COUNT"

#echo ""
#echo "PRs more than one year old:"

#gh pr -R keycloak/keycloak list --search "created:<`date -d "-12 month" +%Y-%m-%d`" -L 2 --json number,author,url,createdAt,updatedAt,title
#gh pr -R keycloak/keycloak list --search "created:<`date -d "-12 month" +%Y-%m-%d`" -L 100
