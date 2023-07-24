#!/bin/bash -e

DATE_30_DAYS_AGO=$(date --date="30 days ago" "+%Y-%m-%d")
DATE_TODAY=$(date "+%Y-%m-%d")

CREATED="$DATE_30_DAYS_AGO..$DATE_TODAY"

REPORT='retried-prs'

echo "Searching for retried workflow runs in $CREATED"
echo ""

echo 'created_at,pr_number,run_id,attempt' | tee $REPORT

for i in $(gh api -X GET repos/keycloak/keycloak/actions/workflows/ci.yml/runs --paginate -f status=success -f event=pull_request -f created="$CREATED" | jq -r '.workflow_runs[] | [.id, .run_attempt, .created_at, .head_sha] | @csv'); do
    ID=$(echo $i | cut -d ',' -f 1)
    RUN_ATTEMPTS=$(echo $i | cut -d ',' -f 2)
    CREATED_AT=$(echo $i | cut -d ',' -f 3)
    HEAD_SHA=$(echo $i | cut -d ',' -f 4)

    if [ "$RUN_ATTEMPTS" -gt 1 ]; then
        PR_NUMBER=$(gh api -X GET search/issues -f q="repo:keycloak/keycloak is:pr SHA:$HEAD_SHA" | jq .items[0].number)

        RUN_ATTEMPT=1
        while [ $RUN_ATTEMPT -lt $RUN_ATTEMPTS ]; do
            CONCLUSION=$(gh run view -R keycloak/keycloak $ID --attempt $RUN_ATTEMPT --json conclusion | jq -r .conclusion)
            if [ "$CONCLUSION" == "failure" ]; then
                echo "$CREATED_AT,$PR_NUMBER,$ID,$RUN_ATTEMPT" | tee -a $REPORT
            fi
            RUN_ATTEMPT=$(( $RUN_ATTEMPT + 1 )) 
        done
    fi
done

