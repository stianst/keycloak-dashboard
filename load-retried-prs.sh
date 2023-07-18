#!/bin/bash -e

for i in $(gh api -X GET repos/keycloak/keycloak/actions/workflows/ci.yml/runs -f per_page=100 -f status=success -f event=pull_request | jq -r '.workflow_runs[] | [.id, .run_attempt] | @csv'); do
    ID=$(echo $i | cut -d ',' -f 1)
    RUN_ATTEMPTS=$(echo $i | cut -d ',' -f 2)
    
    if [ "$RUN_ATTEMPTS" -gt 1 ]; then
        RUN_ATTEMPT=1
        while [ $RUN_ATTEMPT -lt $RUN_ATTEMPTS ]; do
            CONCLUSION=$(gh run view -R keycloak/keycloak $ID --attempt $RUN_ATTEMPT --json conclusion | jq -r .conclusion)
            if [ "$CONCLUSION" == "failure" ]; then
                echo "https://github.com/keycloak/keycloak/actions/runs/$ID/attempts/$RUN_ATTEMPT"
            fi
            RUN_ATTEMPT=$(( $RUN_ATTEMPT + 1 )) 
        done
    fi
done

#RUN_ATTEMPT=5
#ID=5535477339


##while [ $RUN_ATTEMPT -gt 1 ]; do
##    RUN_ATTEMPT=$(( $RUN_ATTEMPT - 1 )) 
 #   echo gh run view -R keycloak/keycloak $ID --attempt $RUN_ATTEMPT --json status,conclusion
#done
