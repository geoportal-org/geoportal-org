#!/bin/sh

for cmd in basename cat getopt curl jq; do
    if ! [ -x "$(command -v $cmd)" ]; then
        echo "Error: $cmd is not installed." 1>&2
        exit 1
    fi
done

PROGNAME=$(basename "$0")

usage() {
    cat >&2 << EOF
Usage: $PROGNAME [OPTIONS]
Options:
  --help
  --baseurl=KEYCLOAK
  --username=USERNAME
  --password=PASSWORD
Environments:
  KC_BASE_URL
  KC_USER_NAME
  KC_USER_PASS
EOF
    exit 1
}

options=$(getopt -a -o '' -l 'help,baseurl:,username:,password:' -- "$@")
if [ $? -ne 0 ]; then
    usage
fi
eval set -- "$options"
while true; do
    case $1 in
    --help)
        usage
        ;;
    --baseurl)
        KC_BASE_URL=$2
        shift 2
        ;;
    --username)
        KC_USER_NAME=$2
        shift 2
        ;;
    --password)
        KC_USER_PASS=$2
        shift 2
        ;;
    --)
        shift
        break
        ;;
    *)
        echo >&2 Unsupported option: "$1"
        usage
        ;;
    esac
done
if [ -z "${KC_BASE_URL}" ]; then
    echo 'error: missing environment -- KC_BASE_URL' 1>&2
    usage
fi
if [ -z "${KC_USER_NAME}" ]; then
    echo 'error: missing environment -- KC_USER_NAME' 1>&2
    usage
fi
if [ -z "${KC_USER_PASS}" ]; then
    echo 'error: missing environment -- KC_USER_PASS' 1>&2
    usage
fi

bearer_token() {
    baseurl=$1
    username=$2
    password=$3
    token=$(curl --location --request POST "$baseurl/realms/geoss/protocol/openid-connect/token" \
    --header 'Content-Type: application/x-www-form-urlencoded' \
    --data-urlencode 'client_id=admin-cli' \
    --data-urlencode 'grant_type=password' \
    --data-urlencode 'username='"$username" \
    --data-urlencode 'password='"$password" \
    2>/dev/null \
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
    baseurl=$1
    accesstoken=$2
    userrepresentation=$3
    response=$(curl --location --request POST "$baseurl/admin/realms/geoss/users" \
    --header 'Content-Type: application/json' \
    --header 'Authorization: Bearer '"$accesstoken" \
    --data-raw "$userrepresentation" \
    )
    echo "$response"
}

access_token=$(bearer_token "$KC_BASE_URL" "$KC_USER_NAME" "$KC_USER_PASS")

INPUT=liferay_users.csv
[ ! -f $INPUT ] && { echo "$INPUT file not found"; exit 99; }
OLDIFS=$IFS
IFS=','
exec < $INPUT
read -r header
echo "HEAD: $header"
while read -r firstName lastName username enabled email emailVerified locale liferay_user_id password
do
	echo "USER: $firstName $lastName $username $enabled $email $emailVerified $locale $liferay_user_id $password"
    user_data=$(user_representation "$firstName" "$lastName" "$username" "$enabled" "$email" "$emailVerified" "$locale" "$liferay_user_id" "$password")
#    echo "$user_data"
    user=$(import_user "$KC_BASE_URL" "$access_token" "$user_data")
    echo "$user"
done
IFS=$OLDIFS
