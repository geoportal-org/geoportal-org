#!/bin/sh

HOST=10.254.2.196
PORT=3306
USER=geoss_devel
DB=geoss_devel

query="SELECT firstName, lastName, screenName as username, (case when status = '0' then 'true' else 'false' end) as enabled, emailAddress as email, (case when emailAddressVerified = '0' then 'true' else 'false' end) as emailVerified, SUBSTRING(languageId, 1, 2) as locale, userId as liferay_user_id, password_ as password FROM User_;"

mysql -u$USER -p -h$HOST --port=$PORT --database=$DB --batch -e "$query" | tr '\t' ',' > liferay_users.csv
