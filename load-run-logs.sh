#!/bin/bash -e

FROM=`date -d "-7 days" +%Y-%m-%d`
TO=`date +%Y-%m-%d`

FAILURES=(`gh api -X GET repos/keycloak/keycloak/actions/workflows/ci.yml/runs -f status=failure -f event=schedule -f created=$FROM..$TO --jq .workflow_runs[].id`)

for i in `ls logs/jobs-*`; do
  RUN_ID=`echo $i | sed 's|logs/jobs-||g'`
  if [[ ! " ${FAILURES[*]} " =~ " ${RUN_ID} " ]]; then
    echo "Purging old run: $RUN_ID"
    rm logs/*-$RUN_ID
  fi
done

for RUN_ID in "${FAILURES[@]}"; do
  if [ ! -f logs/jobs-$RUN_ID ]; then
    echo "Downloading run: $RUN_ID"

    gh run view -R keycloak/keycloak $RUN_ID --json jobs --jq '.jobs[] | .name + ": [" + .conclusion + "]"' > logs/jobs-$RUN_ID
    gh run view -R keycloak/keycloak $RUN_ID --log-failed > logs/log-$RUN_ID
  else
    echo "Run exits: $RUN_ID"
  fi
done
