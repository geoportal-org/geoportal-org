# [GEOSS DB](geoss-db/README.md)

Database module. (MariaDB)

## Restore database
Image has ability to initialize database from 3 sources:
- download database dump from url
- download database dump from s3
- dump database from remote db

This functionality is useful in local environment.
Allows to onboard new developer and establish his local environment quite fast.
To configure automatic restore you need to provide environment variables described below.

### Environment variables for url to download db dump
- REMOTE_DB_DUMP_URL    - url to db dump zipped with *.zip format

### Environment variables for db remote server
- REMOTE_DB_HOST        - ip of remote database
- REMOTE_DB_USERNAME    - username used to connect to remote database
- REMOTE_DB_PASSWORD    - password used to connect to remote database
- REMOTE_DB_NAME        - remote database name

### Environment variables for aws db buckup
- AWS_ACCESS_KEY_ID     - aws access key
- AWS_SECRET_ACCESS_KEY - aws secret key
- AWS_DEFAULT_REGION    - aws region
- AWS_S3_FILE           - link to aws s3 file with db (sql format) file packed with tar.gz

### Environment variables with paramters to local database
- MARIADB_ROOT_PASSWORD   - local (docker container) password
