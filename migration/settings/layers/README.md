# Default Layers migration

## Prerequisites

Prepare the environment and configuration file `environment_config.ini` for the scripts according to the instructions [migration readme](../../README.md).

## Export data

Place the configuration file in the script directory or run the script with the path to the configuration file as an argument.

```sh
python export_default_layers_from_liferay.py environment_config.ini
```

A file with the exported data `default_layers.json` should be created.
A directory `default_layers_kml` with the downloaded kml files from liferay should be created.

## Import data

The file with the exported data `default_layers.json` should be in the script directory.
Place the configuration file in the script directory or run the script with the path to the configuration file as an argument.

```sh
python import_default_layers_from_liferay.py environment_config.ini
```

This script creates folder `layers` in the geoss-contents for the community site and put in them the kml files downloaded from liferay.

The script processing is finished when the message 'Total execution time' appears.
If there are any problems with uploading records, failed attempts will be stored in the `default_layers_failed_records.json` file.
In case of failed records, they will be reviewed and manually added via the API after analysis.

The `default_layers.json` data file with default_layers_kml needs to be archived.
