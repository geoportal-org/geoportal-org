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
    | jq -r '.access_token')
    echo "$token"
}
user_representation() {
    first_name=$1
    last_name=$2
    user_name=$3
    enabled=$4
    email=$5
    email_verified=$6
    locale=$7
    user_id=$8
    password=$9
    secret_data="{\"value\":\"$password\",\"salt\":null}"
    user_json=$(jq -n \
    --arg firstName "$first_name" \
    --arg lastName "$last_name" \
    --arg userName "$user_name" \
    --arg enabled "$enabled" \
    --arg email "$email" \
    --arg emailVerified "$email_verified" \
    --arg locale "$locale" \
    --arg userId "$user_id" \
    --arg secretData "$secret_data" \
    '{
        firstName: $firstName,
        lastName: $lastName,
        username: $userName,
        enabled: $enabled,
        email: $email,
        emailVerified: $emailVerified,
        attributes: {
            locale: [ $locale ],
            liferay_user_id: [ $userId ]
        },
        credentials: [
            {
                type: "password",
                userLabel: "Liferay import",
                credentialData: "{\"algorithm\":\"liferay-sha1\",\"hashIterations\":0}",
                secretData: $secretData
            }
        ],
        groups: [
            "registered-user"
        ]
    }')
    echo "$user_json"
}
import_user() {
    user_representation=$1
    response=$(curl --location --request POST $KEYCLOAK_URL'/admin/realms/geoss/users' \
    --header 'Content-Type: application/json' \
    --header 'Authorization: Bearer '"$access_token" \
    --data-raw "$user_representation" \
    )
    echo "$response"
}

access_token=$(bearer_token geoss geoss)
echo "$access_token"

INPUT=liferay_users.csv
[ ! -f $INPUT ] && { echo "$INPUT file not found"; exit 99; }
OLDIFS=$IFS
IFS=','
while read -r firstName lastName username enabled email emailVerified locale liferay_user_id password
do
	echo "USER: $firstName $lastName $username $enabled $email $emailVerified $locale $liferay_user_id $password"
    user_data=$(user_representation "$firstName" "$lastName" "$username" "$enabled" "$email" "$emailVerified" "$locale" "$liferay_user_id" "$password")
#    echo "$user_data"
    import_user "$user_data"
done < $INPUT
IFS=$OLDIFS
