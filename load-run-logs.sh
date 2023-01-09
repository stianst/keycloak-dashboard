#!/bin/bash -e

#FROM=`date -d "-7 days" +%Y-%m-%d`
FROM=2022-12-22
TO=`date +%Y-%m-%d`

RUNS=`gh api -X GET repos/keycloak/keycloak/actions/workflows/ci.yml/runs -f status=failure -f event=schedule -f created=$FROM..$TO --jq '.workflow_runs[] | (.id|tostring) + ":::" + .updated_at'`

RUN_IDS=(`echo $RUNS | sed 's|:::[^ ]*||g'`)
RUN_DATES=(`echo $RUNS | sed 's|[^ ]*:::||g'`)

if [ ! -d logs ]; then
  mkdir logs
else
  for i in `ls logs/jobs-*`; do
    RUN_ID=`echo $i | sed 's|logs/jobs-||g'`
    if [[ ! " ${RUN_IDS[*]} " =~ " ${RUN_ID} " ]]; then
      echo "Purging old run: $RUN_ID"
      rm logs/*-$RUN_ID
    fi
  done
fi


for((i=0;i<${#RUN_IDS[@]};i++)); do
  RUN_ID=${RUN_IDS[$i]}
  RUN_DATE=${RUN_DATES[$i]}

  if [ ! -f logs/jobs-$RUN_ID ]; then
    echo "Downloading run: $RUN_ID"

    echo "# $RUN_DATE" > logs/jobs-$RUN_ID
    gh run view -R keycloak/keycloak $RUN_ID --json jobs --jq '.jobs[] | .name + ": [" + .conclusion + "]"' >> logs/jobs-$RUN_ID
    gh run view -R keycloak/keycloak $RUN_ID --log-failed > logs/log-$RUN_ID
  else
    echo "Run exits: $RUN_ID"
  fi
done
