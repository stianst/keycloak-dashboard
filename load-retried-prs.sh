#!/bin/bash -e

DATE_30_DAYS_AGO=$(date --date="14 days ago" "+%Y-%m-%d")
DATE_TODAY=$(date "+%Y-%m-%d")

CREATED="$DATE_30_DAYS_AGO..$DATE_TODAY"

REPORT='retried-prs'
if [ -f $REPORT ]; then
  PREVIOUS=$(cat $REPORT)
else
  PREVIOUS=""
fi
REPORT_TMP='retried-prs.tmp'

echo "Searching for retried workflow runs in $CREATED"
echo ""

echo 'created_at,pr_number,run_id,attempt,conclusion,failed_jobs' | tee $REPORT_TMP

for i in $(gh api -X GET repos/keycloak/keycloak/actions/workflows/ci.yml/runs --paginate -f status=success -f event=pull_request -f created="$CREATED" -f per_page=100 | jq -r '.workflow_runs[] | [.id, .run_attempt, .created_at, .head_sha] | @csv'); do
  ID=$(echo $i | cut -d ',' -f 1)
  RUN_ATTEMPTS=$(echo $i | cut -d ',' -f 2)
  CREATED_AT=$(echo $i | cut -d ',' -f 3)
  HEAD_SHA=$(echo $i | cut -d ',' -f 4)

  if [ "$RUN_ATTEMPTS" -gt 1 ]; then
    RUN_ATTEMPT=1
    while [ $RUN_ATTEMPT -lt $RUN_ATTEMPTS ]; do
      PREVIOUS_RESULT=$(echo "$PREVIOUS" | grep ",$ID,$RUN_ATTEMPT" || echo 'null')
      if [ "$PREVIOUS_RESULT" != "null" ]; then
        echo "$PREVIOUS_RESULT" | tee -a $REPORT_TMP
      else
        if [ "$PR_NUMBER" == "" ]; then
          PR_NUMBER=$(gh api -X GET search/issues -f q="repo:keycloak/keycloak is:pr SHA:$HEAD_SHA" | jq .items[0].number)
        fi
        CONCLUSION=$(gh run view -R keycloak/keycloak $ID --attempt $RUN_ATTEMPT --json conclusion | jq -r .conclusion)
        if [ "$CONCLUSION" == "failure" ]; then
          FAILED_JOBS=$(gh api -X GET --paginate repos/keycloak/keycloak/actions/runs/$ID/attempts/$RUN_ATTEMPT/jobs | jq '.jobs[] | select(.name != "Status Check - Keycloak CI" and (.conclusion == "failure" or .conclusion == "cancelled")) | (.name + " (" + .conclusion + ")")' | jq -s '.' | jq 'join("; ")')
        else
          FAILED_JOBS="null"
        fi
        echo "$CREATED_AT,$PR_NUMBER,$ID,$RUN_ATTEMPT,$CONCLUSION,$FAILED_JOBS" | tee -a $REPORT_TMP
      fi
      RUN_ATTEMPT=$(( $RUN_ATTEMPT + 1 ))
    done

    PR_NUMBER=""
  fi
done

mv $REPORT_TMP $REPORT

