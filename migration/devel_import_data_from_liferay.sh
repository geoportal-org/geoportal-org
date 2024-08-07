#!/bin/sh

echo "Import start at $(date)"

echo 'Import community sites from liferay started'
python community/import_community_sites_from_liferay.py devel_environment_config.ini
echo 'Import community sites from liferay ended'

echo 'Import global views from liferay started'
python settings/views/import_global_views_from_liferay.py devel_environment_config.ini
echo 'Import global views from liferay ended'

echo 'Import tutorial tags from liferay started'
python settings/tutorialtags/import_tutorial_tags_from_liferay.py devel_environment_config.ini
echo 'Import tutorial tags from liferay ended'

echo 'Import default layers from liferay started'
python settings/layers/import_default_layers_from_liferay.py devel_environment_config.ini
echo 'Import default layers from liferay ended'

echo 'Import resource rating stats from liferay started'
python resources/rating/stats/import_entry_stats.py devel_environment_config.ini
echo 'Import resource rating stats from liferay ended'

echo 'Import resource rating entries from liferay started'
python resources/rating/entry/import_entry_rating.py devel_environment_config.ini
echo 'Import resource rating entries from liferay ended'

echo 'Import user accounts from liferay started'
python accounts/import_users_from_liferay.py devel_environment_config.ini
echo 'Import user accounts from liferay ended'

echo 'Import user settings from liferay started'
python personaldata/settings/import_users_settings_from_liferay.py devel_environment_config.ini
echo 'Import user settings from liferay ended'

echo 'Import saved searches from liferay started'
python personaldata/savedsearches/import_saved_searches_from_liferay.py devel_environment_config.ini
echo 'Import saved searches from liferay ended'

echo 'Import saved runs from liferay started'
python personaldata/savedruns/import_saved_runs_from_liferay.py devel_environment_config.ini
echo 'Import saved runs from liferay ended'

echo 'Import bookmarks from liferay started'
python resources/bookmarks/import_bookmarks.py devel_environment_config.ini
echo 'Import bookmarks from liferay ended'

echo 'Import surveys from liferay started'
python surveys/import_surveys.py devel_environment_config.ini
echo 'Import surveys from liferay ended'

echo "Import ended at $(date)"
