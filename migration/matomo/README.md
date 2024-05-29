Migration plan from Google Analytics to Matomo.

Google Analytics data will be imported to its own website in Matomo, and future data will be track to a separate website. This means you can setup tracking first, and have a GA import running in the background to store your historical GA data. The GA Importer requires no special steps to support this use case. You can simply set up your import, which will create a separate website in Matomo and import data to it. You can use any other existing or new website in Matomo to track new data to.

Install to Matomo a Google Analytics Importer plugin from Marketplace -> https://matomo.org/guide/installation-maintenance/import-google-analytics/

Current Matomo version in the system is 4.15.1.

Matomo DEV URL: https://gpp-admin.devel.esaportal.eu/matomo

Be sure that Squid is not blocking googleapis.com endpoint. Add googleapis.com to the whitelist.

Setup Google Analytics import in Matomo using account geoportal2x@gmail.com (pass on syspass) -> https://matomo.org/faq/general/set-up-google-analytics-import/

This authorizes Matomo install to access Google Analytics data. Create a published, external app.

GEOSS Google Analytics -> https://analytics.google.com/analytics/web/?authuser=5#/p386425157/reports/intelligenthome

Property ID for site https://www.geoportal.org is 386425157.

Start date is 2015-08-14.

Schedule the Google Analytics import to run -> https://matomo.org/faq/general/running-the-google-analytics-import/

A GA import takes several days to complete.

Back up Matomo’s database. If the imported report data were to be purged or somehow deleted, you would have to re-import the data from GA. And if your GA property has been deleted, the data will be gone forever.

Limitations when importing Google Analytics data -> https://matomo.org/faq/general/limitations-when-importing-google-analytics-data/