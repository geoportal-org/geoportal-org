# Surveys migration

## Prerequisites

Prepare the environment and configuration file `environment_config.ini` for the scripts according to the instructions [migration readme](../../README.md).

## Export data

Download the current production data file `prod_survey_results.txt` from Google Docs and place it in the script folder.
Google Drive username: geoportal2x@gmail.com, password: syspass.
Rename the file `prod_survey_results.txt` to `survey_results.json`

## Import data

The file with the exported data `survey_results.json` should be in the script directory.
Place the configuration file in the script directory or run the script with the path to the configuration file as an argument.

```sh
python import_surveys.py environment_config.ini
```

The script processing is finished when the message 'Total execution time' appears.
If there are any problems with uploading records, failed attempts will be stored in the `survey_failed_records.json` file.
In case of failed records, they will be reviewed and manually added via the API after analysis.

The `prod_survey_results.json` data file needs to be archived.
