import json
import time
import urllib.parse

import requests
from keycloak import KeycloakAdmin
from keycloak import KeycloakOpenID
from keycloak import KeycloakOpenIDConnection

SAVED_SEARCHES_FILE = 'saved_searches.json'
SAVED_SEARCHES_FAILED_RECORDS_FILE = 'saved_searches_failed_records.json'
API_URL = 'https://gpp.devel.esaportal.eu/personaldata/rest/saved-searches'
KC_BASE_URL = 'https://gpp-idp.devel.esaportal.eu'
KC_USER_NAME = 'geoss'
KC_USER_PASS = ''


M_ACTIVE_LAYER_TILE_ID = "m:activeLayerTileId"
F_PHRASE = "f:phrase"
F_DATA_SOURCE = "f:dataSource"
F_PARENT_REFS = "f:parentRefs"
F_GEOSS_DATA_CORE = "f:geossDataCore"
F_DATE_FROM = "f:dateFrom"
F_DATE_TO = "f:dateTo"
F_DATE_PERIOD = "f:datePeriod"
F_LOCATION_TYPE = "f:locationType"
F_SELECTED_AREA_COORDINATES = "f:selectedAreaCoordinates"
F_GOOGLE_PLACES_INPUT = "f:googlePlacesInput"
F_BOUNDING_BOX_RELATION = "f:boundingBoxRelation"
F_SOURCES = "f:sources"
F_VIEW_ID = "f:viewId"
F_KEYWORD = "f:keyword"
F_FORMAT = "f:format"
F_SOURCE = "f:source"
F_PROTOCOL = "f:protocol"
F_ORGANISATION = "f:organisation"
F_SCORE = "f:score"
F_PRODUCT_TYPE = "f:productType"
F_SENSOR_POLARISATION = "f:sensorPolarisation"
F_SENSOR_MODE = "f:sensorMode"
F_SENSOR_SWATH = "f:sensorSwath"
F_RELATIVE_ORBIT = "f:relativeOrbit"
F_ROW = "f:row"
F_PATH = "f:path"
F_INSTRUMENT = "f:instrument"
F_PRODUCT_LEVEL = "f:productLevel"
F_TIMELINES = "f:timeliness"
F_CLOUD_COVERAGE = "f:cloudCoverage"
F_MAGNITUDE_TYPE = "f:magnitudeType"
F_SORT = "f:sort"
F_MIN_MAGNITUDE = "f:minMagnitude"
F_MAX_MAGNITUDE = "f:maxMagnitude"
F_MIN_DEPTH = "f:minDepth"
F_MAX_DEPTH = "f:maxDepth"


class SearchFilterKeys:
    # query params
    SOURCES = "sources"
    VIEWID = "viewId"
    # aoiOption values
    AOI_OPTION_CONTINENT_COUNTRY = "continent_country"
    AOI_OPTION_COORDINATES = "coordinates"
    AOI_OPTION_GOLOCATION = "geolocation"
    # filter set: faceted-filters
    KEYWORD = "keyword"
    FORMAT = "format"
    SOURCE = "source"
    PROTOCOL = "protocol"
    ORGANISATION = "organisation"
    SCORE = "score"
    # filter set: granula-filters
    PROD_TYPE = "prodType"
    SAR_POL_CH = "sarPolCh"
    SENSOR_OP_MODE = "sensorOpMode"
    SENSOR_SWATH = "sensorSwath"
    REL_ORBIT = "relOrbit"
    ROW = "row"
    PATH = "path"
    S3_INSTRUMENT_IDX = "s3InstrumentIdx"
    S3_PRODUCT_LEVEL = "s3ProductLevel"
    S3_TIMELINESS = "s3Timeliness"
    CLOUDCP = "cloudcp"
    DATE_FROM_GRANULA = "dateFromGranula"
    DATE_TO_GRANULA = "dateToGranula"
    # filter set: iris-filters
    MAGT = "magt"
    EVT_ORD = "evtOrd"
    MIN_MAG = "minMag"
    MAX_MAG = "maxMag"
    MIN_DEP = "minDep"
    MAX_DEP = "maxDep"
    DATE_FROM_IRIS = "dateFromIris"
    DATE_TO_IRIS = "dateToIris"


def main():
    start_time = log_start_time()

    keycloak_admin = get_keycloak_admin()
    keycloak_openid = get_keycloak_openid()
    admin_access_token = get_admin_access_token(keycloak_openid)

    data = load_data(SAVED_SEARCHES_FILE)
    failed_records = process_records(data, keycloak_admin, keycloak_openid, admin_access_token)

    log_end_time(start_time)

    if failed_records:
        save_failed_records(failed_records, SAVED_SEARCHES_FAILED_RECORDS_FILE)


def get_keycloak_openid():
    keycloak_openid = KeycloakOpenID(
        server_url=KC_BASE_URL,
        realm_name="geoss",
        client_id="geoss-ui"
    )
    return keycloak_openid


def get_admin_access_token(keycloak_openid):
    token = keycloak_openid.token(
        KC_USER_NAME,
        KC_USER_PASS,
        scope="openid profile roles"
    )
    return token['access_token']


def get_impersonation_access_token(keycloak_openid, admin_access_token, impersonation_user_id):
    impersonation_token = keycloak_openid.exchange_token(
        token=admin_access_token,
        subject=impersonation_user_id,
        subject_token_type="urn:ietf:params:oauth:token-type:access_token",
        requested_token_type="urn:ietf:params:oauth:token-type:access_token",
        scope="openid profile roles"
    )
    return impersonation_token['access_token']


def get_user_info(keycloak_openid, access_token):
    user_info = keycloak_openid.userinfo(access_token)
    return user_info


def get_keycloak_admin():
    keycloak_connection = KeycloakOpenIDConnection(
        server_url=KC_BASE_URL,
        username=KC_USER_NAME,
        password=KC_USER_PASS,
        realm_name="geoss",
        user_realm_name="geoss",
        client_id="admin-cli",
        verify=True)
    keycloak_admin = KeycloakAdmin(connection=keycloak_connection)
    return keycloak_admin


def get_user_id_by_liferay_user_id(keycloak_admin, liferay_user_id):
    users = keycloak_admin.get_users(query={
        "max": 1,
        "q": "liferay_user_id:" + str(liferay_user_id)
    })
    if not users:
        raise Exception("User not found for liferay user id " + str(liferay_user_id))
    user = users[0]
    user_id = user.get("id")
    return user_id


def log_start_time():
    start_time = time.time()
    print(f"Script started at: {time.ctime(start_time)}")
    return start_time


def log_end_time(start_time):
    end_time = time.time()
    print(f"Script ended at: {time.ctime(end_time)}")
    print(f"Total execution time: {end_time - start_time:.2f} seconds")


def load_data(file_path):
    try:
        with open(file_path, 'r', encoding='utf-8') as file:
            return json.load(file)
    except (FileNotFoundError, json.JSONDecodeError) as e:
        print(f"Error loading data from {file_path}: {e}")
        return []


def process_records(data, keycloak_admin, keycloak_openid, admin_access_token):
    failed_records = []
    for record in data:
        if not send_data(record, keycloak_admin, keycloak_openid, admin_access_token):
            failed_records.append(record)
    return failed_records


def send_data(record, keycloak_admin, keycloak_openid, admin_access_token):
    try:
        lf_user_id = record.get('userId', '')
        print(f"lf_user_id: {lf_user_id}")
        user_id = get_user_id_by_liferay_user_id(keycloak_admin, lf_user_id)
        print(f"user_id: {user_id}")
        impersonation_access_token = get_impersonation_access_token(keycloak_openid, admin_access_token, user_id)
        headers = create_headers(impersonation_access_token)
        payload = create_payload(record)
        response = requests.post(API_URL, headers=headers, json=payload)
        if response.status_code == 201:
            print_response_status(response)
            return True
        else:
            print('Unexpected status code:', response.status_code, response.text)
            return False
    except requests.exceptions.RequestException as e:
        print(f'Failed to send data: {e}')
        return False
    except Exception as e:
        print(f'Failed to send data: {e}')
        return False


def create_headers(access_token):
    return {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + access_token
    }


def create_payload(record):
    phrase = record.get('query', '')
    if not phrase:
        phrase = record.get('name', '')
    return {
        "name": record.get('name', ''),
        "phrase": phrase,
        "url": create_url(record),
    }


def create_url(record):
    query_string = create_query_string(record)
    url = '/?' + query_string
    return url


def create_query_string(record):
    query_string = ''
    query_params = create_query_params(record)
    for (key, val) in query_params:
        value = urllib.parse.quote(str(val), encoding='utf-8')
        value = value.replace("+", "%20")
        query_string += key + '=' + value + '&'
    query_string = query_string[:-1]
    return query_string


def create_query_params(record):
    query_params = []
    query = record.get('query', '')
    if query:
        query_params.append((F_PHRASE, query))
    curr_map = record.get('currMap', '')
    if curr_map:
        query_params.append((M_ACTIVE_LAYER_TILE_ID, curr_map))
    full_and_open_dataset = record.get('fullAndOpenDataset', '')
    if full_and_open_dataset and full_and_open_dataset == 1:
        query_params.append((F_GEOSS_DATA_CORE, 'true'))
    date_from = record.get('dateFrom', '')
    if date_from:
        query_params.append((F_DATE_FROM, date_from))
    date_to = record.get('dateTo', '')
    if date_to:
        query_params.append((F_DATE_TO, date_to))
    date_period = record.get('datePeriod', '')
    if date_period:
        query_params.append((F_DATE_PERIOD, date_period))
    aoi_option = record.get('aoiOption', '')
    aoi_bounding_box = record.get('aoiBoundingBox', '')
    if aoi_option and aoi_bounding_box:
        if SearchFilterKeys.AOI_OPTION_CONTINENT_COUNTRY == aoi_option\
                or SearchFilterKeys.AOI_OPTION_COORDINATES == aoi_option:
            query_params.append((F_LOCATION_TYPE, aoi_option))
            query_params.append((F_SELECTED_AREA_COORDINATES, aoi_bounding_box))
        elif SearchFilterKeys.AOI_OPTION_GOLOCATION == aoi_option:
            query_params.append((F_LOCATION_TYPE, aoi_option))
            query_params.append((F_SELECTED_AREA_COORDINATES, aoi_bounding_box))
            aoi_geolocation = record.get('aoiGeolocation', '')
            query_params.append((F_GOOGLE_PLACES_INPUT, aoi_geolocation))
        aoi_relation = record.get('aoiRelation', '')
        if aoi_relation:
            query_params.append((F_BOUNDING_BOX_RELATION, aoi_relation))
    target_id = record.get('targetId', '')
    data_source = record.get('dataSource', '')
    if target_id:
        query_params.append((F_PARENT_REFS, build_parent_ref_json(target_id, data_source)))
    else:
        query_params.append((F_DATA_SOURCE, data_source))
    q_params = record.get('queryParams', '')
    if q_params:
        for q_param in q_params:
            param_name = q_param.get('paramName', '')
            param_value = q_param.get('paramValue', '')
            if param_value:
                match param_name:
                    case SearchFilterKeys.SOURCES:
                        query_params.append((F_SOURCES, param_value))
                    case SearchFilterKeys.VIEWID:
                        query_params.append((F_VIEW_ID, param_value))
    filter_params = record.get('filterParams', '')
    if filter_params:
        for filter_param in filter_params:
            param_name = filter_param.get('paramName', '')
            param_value = filter_param.get('paramValue', '')
            if param_value:
                # filter set: faceted-filters
                match param_name:
                    case SearchFilterKeys.KEYWORD:
                        query_params.append((F_KEYWORD, param_value))
                    case SearchFilterKeys.FORMAT:
                        query_params.append((F_FORMAT, param_value))
                    case SearchFilterKeys.SOURCE:
                        query_params.append((F_SOURCE, param_value))
                    case SearchFilterKeys.PROTOCOL:
                        query_params.append((F_PROTOCOL, param_value))
                    case SearchFilterKeys.ORGANISATION:
                        query_params.append((F_ORGANISATION, param_value))
                    case SearchFilterKeys.SCORE:
                        query_params.append((F_SCORE, param_value))
                # filter set: granula-filters
                match param_name:
                    case SearchFilterKeys.PROD_TYPE:
                        query_params.append((F_PRODUCT_TYPE, param_value))
                    case SearchFilterKeys.SAR_POL_CH:
                        query_params.append((F_SENSOR_POLARISATION, param_value))
                    case SearchFilterKeys.SENSOR_OP_MODE:
                        query_params.append((F_SENSOR_MODE, param_value))
                    case SearchFilterKeys.SENSOR_SWATH:
                        query_params.append((F_SENSOR_SWATH, param_value))
                    case SearchFilterKeys.REL_ORBIT:
                        query_params.append((F_RELATIVE_ORBIT, param_value))
                    case SearchFilterKeys.ROW:
                        query_params.append((F_ROW, param_value))
                    case SearchFilterKeys.PATH:
                        query_params.append((F_PATH, param_value))
                    case SearchFilterKeys.S3_INSTRUMENT_IDX:
                        query_params.append((F_INSTRUMENT, param_value))
                    case SearchFilterKeys.S3_PRODUCT_LEVEL:
                        query_params.append((F_PRODUCT_LEVEL, param_value))
                    case SearchFilterKeys.S3_TIMELINESS:
                        query_params.append((F_TIMELINES, param_value))
                    case SearchFilterKeys.CLOUDCP:
                        query_params.append((F_CLOUD_COVERAGE, param_value))
                    case SearchFilterKeys.DATE_FROM_GRANULA:
                        query_params.append((F_DATE_FROM, param_value))
                    case SearchFilterKeys.DATE_TO_GRANULA:
                        query_params.append((F_DATE_TO, param_value))
                    # filter set: iris-filters
                match param_name:
                    case SearchFilterKeys.MAGT:
                        query_params.append((F_MAGNITUDE_TYPE, param_value))
                    case SearchFilterKeys.EVT_ORD:
                        query_params.append((F_SORT, param_value))
                    case SearchFilterKeys.MIN_MAG:
                        query_params.append((F_MIN_MAGNITUDE, param_value))
                    case SearchFilterKeys.MAX_MAG:
                        query_params.append((F_MAX_MAGNITUDE, param_value))
                    case SearchFilterKeys.MIN_DEP:
                        query_params.append((F_MIN_DEPTH, param_value))
                    case SearchFilterKeys.MAX_DEP:
                        query_params.append((F_MAX_DEPTH, param_value))
                    case SearchFilterKeys.DATE_FROM_IRIS:
                        query_params.append((F_DATE_FROM, param_value))
                    case SearchFilterKeys.DATE_TO_IRIS:
                        query_params.append((F_DATE_TO, param_value))
    query_params.sort()
    return query_params


def build_parent_ref_json(target_id, data_source):
    json_array = []
    json_obj = {
        "id": target_id,
        "dataSource": data_source,
    }
    json_array.append(json_obj)
    parent_ref_json = json.dumps(json_array, separators=(',', ':'))
    return parent_ref_json


def print_response_status(response):
    try:
        if response.content:
            response_json = response.json()
            print('Data sent successfully:', response_json)
        else:
            print('Data sent successfully, but no response content.')
    except json.JSONDecodeError:
        print('Failed to decode JSON response')
        print('Status code:', response.status_code)
        print('Response content:', response.content)


def save_failed_records(failed_records, file_path):
    try:
        with open(file_path, 'w', encoding='utf-8') as outfile:
            json.dump(failed_records, outfile, ensure_ascii=False, indent=4)
        print(f"Failed records saved to {file_path}")
    except IOError as e:
        print(f"Error saving failed records: {e}")


if __name__ == "__main__":
    main()
