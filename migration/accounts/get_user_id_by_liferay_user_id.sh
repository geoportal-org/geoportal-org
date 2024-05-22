#!/bin/sh

KEYCLOAK_URL='http:///geoss-keycloak:8080'

bearer_token() {
    username=$1
    password=$2
    token=$(curl --location --request POST $KEYCLOAK_URL'/realms/geoss/protocol/openid-connect/token' \
    --header 'Content-Type: application/x-www-form-urlencoded' \
    --data-urlencode 'client_id=admin-cli' \
    --data-urlencode 'grant_type=password' \
    --data-urlencode 'username='"$username" \
    --data-urlencode 'password='"$password" \
    2>/dev/null \
    | jq -r '.access_token')
    echo "$token"
}

access_token=$(bearer_token geoss geoss)
#echo "$access_token"

liferay_user_id=$1

keycloak_user_id=$(curl --location --request GET "$KEYCLOAK_URL/admin/realms/geoss/users?q=liferay_user_id:$liferay_user_id" \
--header "Authorization: Bearer $access_token" \
2>/dev/null \
| jq -r '.[].id')
echo "$keycloak_user_id"
