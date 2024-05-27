#!/bin/sh

for cmd in basename cat getopt mysql tr; do
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
  --host=HOST
  --port=PORT
  --database=DATABASE
  --username=USERNAME
  --password=PASSWORD
Environments:
  LF_DB_HOST
  LF_DB_PORT
  LF_DB_NAME
  LF_DB_USER
  LF_DB_PASS
EOF
    exit 1
}

options=$(getopt -a -o '' -l 'help,host:,port:,database:,username:,password:' -- "$@")
if [ $? -ne 0 ]; then
    usage
fi
eval set -- "$options"
while true; do
    case $1 in
    --help)
        usage
        ;;
    --host)
        LF_DB_HOST=$2
        shift 2
        ;;
    --port)
        LF_DB_PORT=$2
        shift 2
        ;;
    --database)
        LF_DB_NAME=$2
        shift 2
        ;;
    --username)
        LF_DB_USER=$2
        shift 2
        ;;
    --password)
        LF_DB_PASS=$2
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
if [ -z "${LF_DB_HOST}" ]; then
    echo 'error: missing environment -- LF_DB_HOST' 1>&2
    usage
fi
if [ -z "${LF_DB_PORT}" ]; then
    echo 'error: missing environment -- LF_DB_PORT' 1>&2
    usage
fi
if [ -z "${LF_DB_NAME}" ]; then
    echo 'error: missing environment -- LF_DB_NAME' 1>&2
    usage
fi
if [ -z "${LF_DB_USER}" ]; then
    echo 'error: missing environment -- LF_DB_USER' 1>&2
    usage
fi
if [ -z "${LF_DB_PASS}" ]; then
    echo 'error: missing environment -- LF_DB_PASS' 1>&2
    usage
fi

query="SELECT firstName, lastName, screenName AS username, (CASE WHEN status = '0' THEN 'true' ELSE 'false' END) AS enabled, emailAddress AS email, (CASE WHEN emailAddressVerified = '0' THEN 'true' ELSE 'false' END) AS emailVerified, SUBSTRING(languageId, 1, 2) AS locale, userId AS liferay_user_id, password_ AS password FROM User_ WHERE defaultUser != 1;"

mysql --host=$LF_DB_HOST --port=$LF_DB_PORT --database=$LF_DB_NAME --user=$LF_DB_USER --password=$LF_DB_PASS --batch -e "$query" | tr '\t' ',' | tr -d '\r' >liferay_users.csv
