#!/bin/sh

echo 'Export global views from liferay started'
python settings/views/export_global_views_from_liferay.py local_environment_config.ini
echo 'Export global views from liferay ended'

echo 'Export tutorial tags from liferay started'
python settings/tutorialtags/export_tutorial_tags_from_liferay.py local_environment_config.ini
echo 'Export tutorial tags from liferay ended'

echo 'Export default layers from liferay started'
python settings/layers/export_default_layers_from_liferay.py local_environment_config.ini
echo 'Export default layers from liferay ended'

echo 'Export resource rating stats from liferay started'
python resources/rating/stats/export_resource_rating_stats_from_liferay.py local_environment_config.ini
echo 'Export resource rating stats from liferay ended'

echo 'Export resource rating entries from liferay started'
python resources/rating/entry/export_resource_rating_entry_from_liferay.py local_environment_config.ini
echo 'Export resource rating entries from liferay ended'

echo 'Export user accounts from liferay started'
python accounts/export_users_from_liferay.py local_environment_config.ini
echo 'Export user accounts from liferay ended'

echo 'Export user settings from liferay started'
python personaldata/settings/export_users_settings_from_liferay.py local_environment_config.ini
echo 'Export user settings from liferay ended'

echo 'Export saved searches from liferay started'
python personaldata/savedsearches/export_saved_searches_from_liferay.py local_environment_config.ini
echo 'Export saved searches from liferay ended'

echo 'Export saved runs from liferay started'
python personaldata/savedruns/export_saved_runs_from_liferay.py local_environment_config.ini
echo 'Export saved runs from liferay ended'

echo 'Export bookmarks from liferay started'
python resources/bookmarks/export_bookmarked_result_from_liferay.py local_environment_config.ini
echo 'Export bookmarks from liferay ended'
