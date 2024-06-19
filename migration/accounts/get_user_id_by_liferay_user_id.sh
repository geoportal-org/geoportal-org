#!/bin/sh

for cmd in basename dirname cat getopt curl jq; do
    if ! [ -x "$(command -v $cmd)" ]; then
        echo "Error: $cmd is not installed." 1>&2
        exit 1
    fi
done

PROGDIR=$(dirname "$0")
PROGNAME=$(basename "$0")

usage() {
    cat >&2 << EOF
Usage: $PROGNAME [OPTIONS] liferay_user_id
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
if [ $# -ne 1 ]; then
    usage
fi

ADMIN_ACCESS_TOKEN="$PROGDIR/get_admin_access_token.sh"
export KC_BASE_URL=$KC_BASE_URL
export KC_USER_NAME=$KC_USER_NAME
export KC_USER_PASS=$KC_USER_PASS
access_token=$($ADMIN_ACCESS_TOKEN)

liferay_user_id=$1
keycloak_response=$(curl -X GET "$KC_BASE_URL/admin/realms/geoss/users?q=liferay_user_id:$liferay_user_id" \
--header "Authorization: Bearer $access_token")
keycloak_user_id=$(echo "$keycloak_response" | jq -r '.[].id')
if [ "" = "${keycloak_user_id}" ]; then
    echo '{"error":"invalid_liferay_user_id","error_description":"User not found"}' 1>&2
    exit 1
else
    echo "$keycloak_user_id"
fi
