# Community sites migration

## Prerequisites

Prepare the environment and configuration file `environment_config.ini` for the scripts according to the instructions [migration readme](../README.md).

## Import data

The file with the community data `community_site.json` should be in the script directory.
Place the configuration file in the script directory or run the script with the path to the configuration file as an argument.

```sh
python import_community_sites_from_liferay.py environment_config.ini
```

The script processing is finished when the message 'Total execution time' appears.
If there are any problems with uploading records, failed attempts will be stored in the `community_sites_failed_records.json` file.
In case of failed records, they will be reviewed and manually added via the API after analysis.

The `community_site.json` data file needs to be archived.
