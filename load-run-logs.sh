#!/bin/bash -e

FROM=`date -d "-30 days" +%Y-%m-%d`
TO=`date +%Y-%m-%d`

RUN_IDS=()

if [ ! -d logs ]; then
  mkdir logs
fi

for i in $(gh api -X GET repos/keycloak/keycloak/actions/workflows/ci.yml/runs --paginate -f status=failure -f branch=main -f created=$FROM..$TO --jq '.workflow_runs[] | [.id, .created_at, .event] | @csv'); do
  RUN_ID=$(echo $i | cut -d ',' -f 1)
  RUN_DATE=$(echo $i | cut -d ',' -f 2 | sed 's/"//g')
  EVENT=$(echo $i | cut -d ',' -f 3 | sed 's/"//g')

  if [ "$EVENT" != "pull_request" ]; then
    RUN_IDS+=($RUN_ID)

    if [ ! -f logs/jobs-$RUN_ID ]; then
        echo "Downloading run: $RUN_ID"

        echo "# $RUN_DATE" > logs/jobs-$RUN_ID
        gh run view -R keycloak/keycloak $RUN_ID --json jobs --jq '.jobs[] | .name + ": [" + .conclusion + "]"' >> logs/jobs-$RUN_ID
        gh run view -R keycloak/keycloak $RUN_ID --log-failed > logs/log-$RUN_ID
      else
        echo "Run exits: $RUN_ID"
      fi
  fi
done

for i in `ls logs/jobs-*`; do
  RUN_ID=`echo $i | sed 's|logs/jobs-||g'`
  if [[ ! " ${RUN_IDS[*]} " =~ " ${RUN_ID} " ]]; then
    echo "Purging old run: $RUN_ID"
    rm logs/*-$RUN_ID
  fi
done
