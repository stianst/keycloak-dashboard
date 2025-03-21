#!/bin/bash -e


PAGES=$( gh api -X GET -F per_page=100 /repos/keycloak/keycloak/stargazers -i | grep 'Link:' | cut -d '=' -f 6 | cut -d '>' -f 1 )

echo "Total pages: $PAGES"

rm -f /tmp/ghstars

if [ -f stars ]; then
    CURRENT_PAGE=$( cat stars | cut -d ',' -f 1 | sort -n -r | uniq | head -n 1 )
    cat stars | grep -v "^$CURRENT_PAGE," > /tmp/ghstars
else
    CURRENT_PAGE=1
fi


for i in $( seq $CURRENT_PAGE $PAGES); do
    echo "Fetching page $i"
    gh api -F page=$i -X GET -F per_page=100 /repos/keycloak/keycloak/stargazers -H Accept:application/vnd.github.star+json -q .[].starred_at | xargs -I {} echo $i,{} >> /tmp/ghstars
done

mv /tmp/ghstars stars
