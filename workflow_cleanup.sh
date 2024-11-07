#!/bin/bash

source .env
TOKEN="${GITHUB_TOKEN}"
REPO="dailyge/dailyge-server"

if [[ "$OSTYPE" == "darwin"* ]]; then
  YESTERDAY=$(date -v -1d +%Y-%m-%dT%H:%M:%SZ)
else
  YESTERDAY=$(date -d "1 day ago" +%Y-%m-%dT%H:%M:%SZ)
fi

for run_id in $(curl -s -H "Authorization: token $TOKEN" \
  "https://api.github.com/repos/$REPO/actions/runs?per_page=100" | \
  jq -r --arg yesterday "$YESTERDAY" '.workflow_runs[] | select(.created_at < $yesterday) | .id'); do
    curl -X DELETE -H "Authorization: token $TOKEN" \
      "https://api.github.com/repos/$REPO/actions/runs/$run_id"
done
