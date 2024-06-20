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
Usage: $PROGNAME [OPTIONS] keycloak_user_id
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
    echo 'error: missing argument -- keycloak_user_id' 1>&2
    usage
fi

bearer_token() {
    baseurl=$1
    clientid=$2
    username=$3
    password=$4
    response=$(curl -X POST "$baseurl/realms/geoss/protocol/openid-connect/token" \
    --header 'Content-Type: application/x-www-form-urlencoded' \
    --data-urlencode 'client_id='"$clientid" \
    --data-urlencode 'grant_type=password' \
    --data-urlencode 'username='"$username" \
    --data-urlencode 'password='"$password" \
    --data-urlencode 'scope=openid profile roles' \
    )
    status=$?
    if [ $status -ne 0 ]; then
        echo '{"error":"curl_error","error_description":"Error getting access token"}' 1>&2
        exit $status;
    fi
    token=$(echo "$response" | jq -r '.access_token')
    if [ "null" = "${token}" ]; then
        echo "$response" 1>&2
        exit 9
    else
        echo "$token"
    fi
    return $status
}

impersonation() {
    baseurl=$1
    accesstoken=$2
    userid=$3
    response=$(curl -X POST "$baseurl/realms/geoss/protocol/openid-connect/token" \
    --header 'Content-Type: application/x-www-form-urlencoded' \
    --data-urlencode 'client_id=geoss-ui' \
    --data-urlencode 'grant_type=urn:ietf:params:oauth:grant-type:token-exchange' \
    --data-urlencode 'subject_token='"$accesstoken" \
    --data-urlencode 'subject_token_type=urn:ietf:params:oauth:token-type:access_token' \
    --data-urlencode 'requested_token_type=urn:ietf:params:oauth:token-type:access_token' \
    --data-urlencode 'requested_subject='"$userid" \
    --data-urlencode 'scope=openid profile roles' \
    )
    status=$?
    if [ $status -ne 0 ]; then
        echo '{"error":"curl_error","error_description":"Error getting access token"}' 1>&2
        exit $status;
    fi
    token=$(echo "$response" | jq -r '.access_token')
    if [ "null" = "${token}" ]; then
        echo "$response" 1>&2
        exit 9
    else
        echo "$token"
    fi
    return $status
}

userinfo() {
    baseurl=$1
    accesstoken=$2
    response=$(curl -X GET "$baseurl/realms/geoss/protocol/openid-connect/userinfo" \
    --header 'Content-Type: application/x-www-form-urlencoded' \
    --header 'Authorization: Bearer '"$accesstoken" \
    )
    status=$?
    if [ $status -ne 0 ]; then
        echo '{"error":"curl_error","error_description":"Error getting user info"}' 1>&2
        exit $status;
    fi
    echo "$response"
}

# to get roles in access token use geoss-ui client id instead of admin-cli
KC_CLIENT_ID="geoss-ui"
access_token=$(bearer_token "$KC_BASE_URL" "$KC_CLIENT_ID" "$KC_USER_NAME" "$KC_USER_PASS")
status=$?
if [ $status -ne 0 ]; then
    exit $status;
fi
#echo "$access_token"

#user=$(userinfo "$KC_BASE_URL" "$access_token")
#echo "$user"

user_id=$1
impersonation_access_token=$(impersonation "$KC_BASE_URL" "$access_token" "$user_id")
echo "$impersonation_access_token"

#user=$(userinfo "$KC_BASE_URL" "$impersonation_access_token")
#echo "$user"
