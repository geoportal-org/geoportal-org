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

access_token=$(bearer_token "$KC_BASE_URL" "$KC_USER_NAME" "$KC_USER_PASS")

keycloak_users=$(curl --location --request GET "$KC_BASE_URL/admin/realms/geoss/users" \
--header "Authorization: Bearer $access_token" \
2>/dev/null \
| jq -r '.[] | "\(.attributes.liferay_user_id[]?),\(.id)"')
echo "$keycloak_users"
