#!/bin/sh

BASEDIR=$(dirname "$0")
cd "$BASEDIR" || exit 126
BASEDIR=$PWD

echo "Import start at $(date)"

echo 'Import community sites from liferay started'
python "$BASEDIR/community/import_community_sites_from_liferay.py" "$BASEDIR/devel_environment_config.ini"
echo 'Import community sites from liferay ended'

echo 'Import global views from liferay started'
python "$BASEDIR/settings/views/import_global_views_from_liferay.py" "$BASEDIR/devel_environment_config.ini"
echo 'Import global views from liferay ended'

echo 'Import tutorial tags from liferay started'
python "$BASEDIR/settings/tutorialtags/import_tutorial_tags_from_liferay.py" "$BASEDIR/devel_environment_config.ini"
echo 'Import tutorial tags from liferay ended'

echo 'Import default layers from liferay started'
python "$BASEDIR/settings/layers/import_default_layers_from_liferay.py" "$BASEDIR/devel_environment_config.ini"
echo 'Import default layers from liferay ended'

echo 'Import resource rating stats from liferay started'
python "$BASEDIR/resources/rating/stats/import_entry_stats.py" "$BASEDIR/devel_environment_config.ini"
echo 'Import resource rating stats from liferay ended'

echo 'Import resource rating entries from liferay started'
python "$BASEDIR/resources/rating/entry/import_entry_rating.py" "$BASEDIR/devel_environment_config.ini"
echo 'Import resource rating entries from liferay ended'

echo 'Import user accounts from liferay started'
python "$BASEDIR/accounts/import_users_from_liferay.py" "$BASEDIR/devel_environment_config.ini"
echo 'Import user accounts from liferay ended'

echo 'Import user settings from liferay started'
python "$BASEDIR/personaldata/settings/import_users_settings_from_liferay.py" "$BASEDIR/devel_environment_config.ini"
echo 'Import user settings from liferay ended'

echo 'Import saved searches from liferay started'
python "$BASEDIR/personaldata/savedsearches/import_saved_searches_from_liferay.py" "$BASEDIR/devel_environment_config.ini"
echo 'Import saved searches from liferay ended'

echo 'Import saved runs from liferay started'
python "$BASEDIR/personaldata/savedruns/import_saved_runs_from_liferay.py" "$BASEDIR/devel_environment_config.ini"
echo 'Import saved runs from liferay ended'

echo 'Import bookmarks from liferay started'
python "$BASEDIR/resources/bookmarks/import_bookmarks.py" "$BASEDIR/devel_environment_config.ini"
echo 'Import bookmarks from liferay ended'

echo 'Import surveys from liferay started'
python "$BASEDIR/surveys/import_surveys.py" "$BASEDIR/devel_environment_config.ini"
echo 'Import surveys from liferay ended'

echo "Import ended at $(date)"
