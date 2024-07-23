# Resource rating stats migration

## Prerequisites

Prepare the environment and configuration file `environment_config.ini` for the scripts according to the instructions [migration readme](../../README.md).

## Export data

Place the configuration file in the script directory or run the script with the path to the configuration file as an argument.

```sh
python export_resource_rating_stats_from_liferay.py environment_config.ini
```

A file with the exported data `geoss_ResourceRatingStats.json` should be created.

## Import data

The file with the exported data `geoss_ResourceRatingStats.json` should be in the script directory.
Place the configuration file in the script directory or run the script with the path to the configuration file as an argument.

```sh
python import_entry_stats.py environment_config.ini
```

The script processing is finished when the message 'Total execution time' appears.
If there are any problems with uploading records, failed attempts will be stored in the `resource_rating_stats_failed_records.json` file.
In case of failed records, they will be reviewed and manually added via the API after analysis.

The `geoss_ResourceRatingStats.json` data file needs to be archived.
