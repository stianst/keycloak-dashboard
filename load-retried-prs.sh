#!/bin/bash -e

DATE_30_DAYS_AGO=$(date --date="14 days ago" "+%Y-%m-%d")
DATE_TODAY=$(date "+%Y-%m-%d")

CREATED="$DATE_30_DAYS_AGO..$DATE_TODAY"

RUN_IDS=()

echo "Searching for retried workflow runs in $CREATED"
echo ""

for i in $(gh api -X GET repos/keycloak/keycloak/actions/workflows/ci.yml/runs --paginate -f status=success -f event=pull_request -f created="$CREATED" -f per_page=100 | jq -r '.workflow_runs[] | [.id, .run_attempt] | @csv'); do
  RUN_ID=$(echo $i | cut -d ',' -f 1)
  RUN_ATTEMPTS=$(echo $i | cut -d ',' -f 2)

  if [ "$RUN_ATTEMPTS" -gt 1 ]; then
    RUN_ATTEMPT=$(( $RUN_ATTEMPTS - 1 ))

    RUN=$(gh run view -R keycloak/keycloak $RUN_ID --attempt $RUN_ATTEMPT --json createdAt,conclusion,event --jq '[.createdAt, .conclusion, .event] | @csv')
    RUN_DATE=$(echo $RUN | cut -d ',' -f 1 | sed 's/"//g')
    CONCLUSION=$(echo $RUN | cut -d ',' -f 2 | sed 's/"//g')
    EVENT=$(echo $RUN | cut -d ',' -f 3 | sed 's/"//g')

    echo $RUN_ID $RUN_ATTEMPT $RUN_DATE $CONCLUSION

    if [ "$CONCLUSION" == "failure" ]; then
      RUN_IDS+=($RUN_ID)

      if [ ! -f logs/pr-jobs-$RUN_ID ]; then
        echo "# $RUN_DATE $EVENT" > logs/pr-jobs-$RUN_ID
        gh api -X GET repos/keycloak/keycloak/actions/runs/$RUN_ID/attempts/$RUN_ATTEMPT/jobs --paginate --jq '.jobs[] | .name + ": [" + .conclusion + "]"' >> logs/pr-jobs-$RUN_ID
      fi

      if [ ! -f logs/pr-log-$RUN_ID ]; then
        gh run view -R keycloak/keycloak $RUN_ID --attempt $RUN_ATTEMPT --log-failed > logs/pr-log-$RUN_ID
      fi
    fi
  fi
done

for i in `ls logs/pr-jobs-*`; do
  RUN_ID=`echo $i | sed 's|logs/pr-jobs-||g'`
  if [[ ! " ${RUN_IDS[*]} " =~ " ${RUN_ID} " ]]; then
    echo "Purging old run: $RUN_ID"
    rm logs/pr-jobs-$RUN_ID
    rm logs/pr-log-$RUN_ID
  fi
done


