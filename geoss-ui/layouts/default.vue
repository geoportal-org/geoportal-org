<template>
    <main>
        <client-only>
            <RocketSpinner v-if="!storeInitialized" />
        </client-only>
        <Header />
        <Menu />
        <UserWelcome />
        <Notification />
        <Spinner />
        <Popup />
        <IrisLegend />
		<LayerLegend />
        <ImagePreview />
        <PrivacyPolicy />
        <SendFeedback />
        <TakeATour />
        <TutorialTags />

        <Nuxt v-if="storeInitialized" />

        <div class="geoss-data-pickers"></div>
        <portal-target name="custom-select-container"></portal-target>
    </main>
</template>

<script lang="ts">
// @ts-nocheck
import { Component, Vue } from 'nuxt-property-decorator'
import { MapActions } from '@/store/map/map-actions'
import { MapGetters } from '@/store/map/map-getters'
import { MyWorkspaceGetters } from '@/store/myWorkspace/my-workspace-getters'
import { SearchEngineActions } from '@/store/searchEngine/search-engine-actions'
import { SearchEngineGetters } from '@/store/searchEngine/search-engine-getters'
import { MyWorkspaceActions } from '@/store/myWorkspace/my-workspace-actions'
import { LayerTypes } from '@/interfaces/LayerTypes'
import LayersUtils from '@/services/map/layer-utils'
import { GeneralFiltersActions } from '@/store/generalFilters/general-filters-actions'
import { GranulaFiltersActions } from '@/store/granulaFilters/granula-filters-actions'
import { IrisFiltersActions } from '@/store/irisFilters/iris-filters-actions'
import { GeneralFiltersGetters } from '@/store/generalFilters/general-filters-getters'
import { SearchActions } from '@/store/search/search-actions'
import { PopupGetters } from '@/store/popup/popup-getters'
import { UserActions } from '@/store/user/user-actions'
import { AppVueObj, Liferay } from '@/data/global'
import LogService from '@/services/log.service'
import { PopupActions } from '@/store/popup/popup-actions'
import { FacetedFiltersActions } from '@/store/facetedFilters/faceted-filters-actions'
import GeossSearchApiService from '@/services/geoss-search.api.service'
import { GeneralGetters } from '@/store/general/general-getters'
import UtilsService from '@/services/utils.service'
import NotificationService from '@/services/notification.service'
import SearchEngineService from '@/services/search-engine.service'
import { UserGetters } from '@/store/user/user-getters'
import LiferayService from '@/services/liferay.service'
import { ParentRef } from '@/interfaces/ParentRef'
import { DataSources, DataOrigin } from '@/interfaces/DataSources'
import { SearchGetters } from '@/store/search/search-getters'
import { GeneralApiService } from '@/services/general.api.service'
import { GeneralActions } from '@/store/general/general-actions'
import { BulkDownloadActions } from '@/store/bulkDownload/bulk-download-actions'
import { FileDownloadActions } from '@/store/fileDownload/file-download-actions'
import to from '@/utils/to'
import WelcomePopup from '@/components/WelcomePopup.vue'
import { Timers } from '@/data/timers'
import { LayerData } from '@/interfaces/LayerData'
import search from '@/store/search'
import { InSituFiltersActions } from '~/store/inSituFilters/inSitu-filters.actions'
import MatomoPlugin from '~/plugins/MatomoPlugin'
import VueMatomo from 'vue-matomo'

export default {
    computed: {
        storeInitialized() {
            return this.$store.getters[GeneralGetters.storeInitialized]
        },
        isBulkDownloadEnabled() {
            return this.$store.getters[GeneralGetters.isBulkDownloadEnabled]
        },
        route() {
            return this.$route
        },
        siteUrl() {
            return this.$store.getters[SearchEngineGetters.siteUrl]
        },
        siteId() {
            return this.$store.getters[SearchEngineGetters.siteId]
        }
    },

    methods: {
        parseQueryParams() {
            let params = UtilsService.getUrlParams()
            if (
                sessionStorage.getItem('RECENT_SEARCH_PARAMS') &&
                window.location.search === ''
            ) {
                const recentSearchParams = UtilsService.getUrlParams(
                    sessionStorage.getItem('RECENT_SEARCH_PARAMS') as string
                )
                params = recentSearchParams.map((item, i) =>
                    Object.assign({}, item, params[i])
                )
                params = params.filter((e) => e.name !== 'f:viewId')
            }
            return new Promise((resolve) => {
                const savedSearchId = UtilsService.getUrlParam('savedSearchId')
                const popularSearchId =
                    UtilsService.getUrlParam('popularSearchId')
                const targetId = UtilsService.getUrlParam('targetId')
                const layers = UtilsService.getUrlParam('m:layers')
                const relationType = UtilsService.getUrlParam('crRelation')

                if (relationType) {
                    this.$store.dispatch(SearchActions.setCrRelationParams, {
                        relationType,
                        crRelationSrcId:
                            UtilsService.getUrlParam('crRelationSrcId'),
                        crRelationSrcDs:
                            UtilsService.getUrlParam('crRelationSrcDs'),
                        workflowInstanceId:
                            UtilsService.getUrlParam('workflowInstanceId'),
                        endpoint: UtilsService.getUrlParam('endpoint'),
                        getPendingRelations: !!UtilsService.getUrlParam(
                            'getPendingRelations'
                        )
                    })
                    this.$store.dispatch(SearchActions.setCrRelation, {
                        relationType,
                        srcEntry: {
                            id: null,
                            dataSource: null
                        },
                        destEntry: []
                    })
                    NotificationService.show(
                        `${this.$tc('popupTitles.entryRelations')}`,
                        `${this.$tc('popupContent.entryRelationsStart')}`,
                        10000,
                        undefined,
                        9999,
                        'info'
                    )
                    if (
                        this.$store.getters[SearchGetters.crRelationParams]
                            .crRelationSrcId &&
                        this.$store.getters[SearchGetters.crRelationParams]
                            .crRelationSrcDs
                    ) {
                        GeossSearchApiService.getTargetById(
                            this.$store.getters[SearchGetters.crRelationParams]
                                .crRelationSrcId,
                            this.$store.getters[SearchGetters.crRelationParams]
                                .crRelationSrcDs
                        ).then((results) => {
                            if (results && results.entry) {
                                const result = results.entry[0]
                                const dataOrigin =
                                    UtilsService.getUrlParam('crRelationSrcDs')
                                let dataSource = dataOrigin
                                for (const source of Object.keys(DataOrigin)) {
                                    if (source === dataOrigin) {
                                        if (dataOrigin === 'geoss_cr') {
                                            const hub = result.category
                                                .filter(
                                                    (el: {
                                                        _attributes: {
                                                            term: string
                                                        }
                                                    }) =>
                                                        el._attributes.term ===
                                                        'geoss_cr'
                                                )[0]
                                                ._attributes.label.split('_')[0]
                                            dataSource =
                                                hub === 'service'
                                                    ? 'services'
                                                    : hub
                                        } else {
                                            dataSource = source
                                        }
                                    }
                                }
                                result.dataSource = dataSource
                                this.$store.dispatch(
                                    SearchActions.setCrRelation,
                                    {
                                        relationType,
                                        srcEntry: result,
                                        destEntry: []
                                    }
                                )
                                NotificationService.show(
                                    `${this.$tc('popupTitles.entryRelations')}`,
                                    `${this.$tc(
                                        'popupContent.entryRelationsSrcSet1'
                                    )}: <br /><b>"${
                                        result.title
                                    }"</b><br />${this.$tc(
                                        'popupContent.entryRelationsSrcSet2'
                                    )}`,
                                    10000,
                                    undefined,
                                    9999,
                                    'success'
                                )

                                if (
                                    this.$store.getters[
                                        SearchGetters.crRelationParams
                                    ].getPendingRelations
                                ) {
                                    GeossSearchApiService.entryRelationsAPI(
                                        true
                                    ).then(
                                        (pendingRelations: {
                                            destEntry: any
                                            srcEntry: any
                                        }) => {
                                            this.$store.dispatch(
                                                SearchActions.setCrRelation,
                                                {
                                                    relationType,
                                                    srcEntry: result,
                                                    destEntry:
                                                        pendingRelations.destEntry
                                                }
                                            )

                                            const targetEntries = [
                                                pendingRelations.srcEntry,
                                                ...pendingRelations.destEntry
                                            ]
                                            const targetIdsObject = {}

                                            for (const entry of targetEntries) {
                                                let source: string | null =
                                                    entry.dataSource
                                                let hub =
                                                    entry.type.split('_')[0]
                                                hub =
                                                    hub === 'service'
                                                        ? 'services'
                                                        : hub

                                                if (
                                                    entry.dataSource ===
                                                    'geoss_cr'
                                                ) {
                                                    source = hub
                                                }

                                                if (source) {
                                                    if (
                                                        !(
                                                            targetIdsObject as any
                                                        )[source]
                                                    ) {
                                                        ;(
                                                            targetIdsObject as any
                                                        )[source] = []
                                                    }
                                                    ;(targetIdsObject as any)[
                                                        source
                                                    ].push(entry.id)
                                                }
                                            }

                                            this.$store.dispatch(
                                                SearchActions.setTargetIds,
                                                JSON.stringify(targetIdsObject)
                                            )
                                            this.$store.dispatch(
                                                SearchActions.getResults
                                            )
                                        }
                                    )
                                }
                            }
                        })
                    }
                }

                if (savedSearchId || popularSearchId) {
                    new Promise((resolve) => {
                        if (savedSearchId) {
                            GeossSearchApiService.getSavedSearchById(
                                savedSearchId
                            ).then(resolve)
                        } else {
                            GeossSearchApiService.getPopularSearchById(
                                popularSearchId
                            ).then(resolve)
                        }
                    }).then((data: any) => {
                        if (data) {
                            const promises = []
                            if (data.currMap) {
                                this.$store.dispatch(
                                    MapActions.setActiveLayerTileId,
                                    data.currMap
                                )
                            }
                            if (data.query) {
                                this.$store.dispatch(
                                    GeneralFiltersActions.setPhrase,
                                    data.query
                                )
                            }
                            if (data.aoiRelation) {
                                this.$store.dispatch(
                                    GeneralFiltersActions.setBoundingBoxRelation,
                                    data.aoiRelation
                                )
                            }
                            if (data.dateFrom) {
                                this.$store.dispatch(
                                    GeneralFiltersActions.setDateFrom,
                                    data.dateFrom
                                )
                            }
                            if (data.dateTo) {
                                this.$store.dispatch(
                                    GeneralFiltersActions.setDateTo,
                                    data.dateTo
                                )
                            }
                            if (data.datePeriod) {
                                this.$store.dispatch(
                                    GeneralFiltersActions.setDatePeriod,
                                    data.datePeriod
                                )
                            }
                            if (data.aoiOption) {
                                const oldType = [
                                    'ContinentAndCountry',
                                    'Coordinates',
                                    'Geolocation'
                                ].indexOf(data.aoiOption)
                                if (oldType !== -1) {
                                    data.aoiOption = [
                                        'continent_country',
                                        'coordinates',
                                        'geolocation'
                                    ][oldType]
                                }
                                this.$store.dispatch(
                                    GeneralFiltersActions.setLocationType,
                                    data.aoiOption
                                )
                            }
                            if (data.folder) {
                                const parentRef: ParentRef = {
                                    id: data.folder,
                                    dataSource: DataSources.DAB
                                }
                                this.$store.dispatch(
                                    SearchActions.addParentRef,
                                    parentRef
                                )
                            }

                            const coordinates = data.aoiBoundingBox
                            if (
                                coordinates &&
                                coordinates.split(',').length === 4
                            ) {
                                const [S, W, N, E] = coordinates
                                    .split(',')
                                    .map((item: string) => parseFloat(item))
                                promises.push(
                                    this.$store.dispatch(
                                        GeneralFiltersActions.setSelectedAreaCoordinates,
                                        { W, S, E, N }
                                    )
                                )
                            }

                            Promise.all(promises).then(() => resolve(true))
                        } else {
                            resolve(false)
                        }
                    })
                } else if (targetId) {
                    const phrase = UtilsService.getUrlParam('f:phrase')
                    if (phrase) {
                        this.$store.dispatch(
                            GeneralFiltersActions.setPhrase,
                            phrase
                        )
                    }

                    const dataSource = UtilsService.getUrlParam('f:dataSource')
                    if (dataSource) {
                        this.$store.dispatch(SearchActions.setDataSource, {
                            value: dataSource
                        })
                    }

                    const currentMap = UtilsService.getUrlParam(
                        'm:activeLayerTileId'
                    )
                    if (currentMap) {
                        this.$store.dispatch(
                            MapActions.setActiveLayerTileId,
                            currentMap
                        )
                    }

                    this.$store.dispatch(
                        SearchActions.setHighlightResult,
                        targetId
                    )
                    resolve(true)
                } else if (layers) {
                    const {
                        outputName,
                        url,
                        name,
                        protocol,
                        id,
                        runId,
                        legend
                    } = JSON.parse(layers)
                    let version = '1.1.1'
                    if (protocol) {
                        if (protocol.indexOf('WebMapService') > -1) {
                            const match = /((\d)+\.(\d)+(\.(\d)+)?)/g.exec(
                                protocol
                            )
                            if (match) {
                                version = match[0]
                            }
                        }
                    }

                    const layerUrl = `${url}VERSION=${version}&LAYERS=${name}&TILED=true&`
                    const layer = LayersUtils.createWMS(name, layerUrl)

                    GeossSearchApiService.getRunCoordinates(runId).then(
                        (coordinate: any) => {
                            this.$store.dispatch(MapActions.addLayer, {
                                layer,
                                id: name,
                                type: LayerTypes.CUSTOM,
                                coordinate,
                                title: outputName,
                                legend: {
                                    type: 'img',
                                    data: legend
                                }
                            })

                            this.$store.dispatch(
                                MapActions.changeLayerVisibility,
                                { id: LayerTypes.BOUNDING, value: false }
                            )
                            this.$store.dispatch(MapActions.setShowFull, true)

                            setTimeout(() => {
                                this.$store.dispatch(
                                    MapActions.zoomInLayer,
                                    name
                                )

                                const mapLayers = this.$store.getters[
                                    MapGetters.layers
                                ].filter(
                                    (layerData: LayerData) =>
                                        layerData.type.indexOf(
                                            LayerTypes.BOUNDING
                                        ) === -1 ||
                                        layerData.type === LayerTypes.BOUNDING
                                )
                                const supportedLegendTypes =
                                    this.$store.getters[
                                        MapGetters.supportedLegendTypes
                                    ]
                                const recentLayerIndex = mapLayers.length - 1
                                if (
                                    mapLayers[recentLayerIndex].legend &&
                                    supportedLegendTypes.includes(
                                        mapLayers[recentLayerIndex].legend.type
                                    )
                                ) {
                                    this.$store.dispatch(
                                        MapActions.setActiveLayerLegend,
                                        mapLayers[recentLayerIndex]
                                    )
                                }
                            }, Timers.hideSearchContainer)

                            resolve(true)
                        }
                    )
                } else if (params.length) {
                    const promises = []
                    for (const param of params) {
                        if (param.name.indexOf('f:') === 0) {
                            const cuttedParamName = param.name.substr(2)
                            const storeParamName =
                                cuttedParamName.charAt(0).toUpperCase() +
                                cuttedParamName.substr(1)

                            if (
                                (GeneralFiltersActions as any)[
                                    'set' + storeParamName
                                ]
                            ) {
                                if (cuttedParamName === 'sources') {
                                    param.value = [...param.value.split(',')]
                                }

                                if (
                                    cuttedParamName ===
                                    'selectedAreaCoordinates'
                                ) {
                                    if (
                                        param.value &&
                                        param.value.split(',').length === 4
                                    ) {
                                        const [S, W, N, E] = param.value
                                            .split(',')
                                            .map((item: string) =>
                                                parseFloat(item)
                                            )
                                        promises.push(
                                            this.$store.dispatch(
                                                GeneralFiltersActions.setSelectedAreaCoordinates,
                                                { W, S, E, N }
                                            )
                                        )
                                    }
                                } else {
                                    const oldType = [
                                        'ContinentAndCountry',
                                        'Coordinates',
                                        'Geolocation'
                                    ].indexOf(param.value)
                                    if (oldType !== -1) {
                                        param.value = [
                                            'continent_country',
                                            'coordinates',
                                            'geolocation'
                                        ][oldType]
                                    }
                                    promises.push(
                                        this.$store.dispatch(
                                            (GeneralFiltersActions as any)[
                                                'set' + storeParamName
                                            ],
                                            param.value
                                        )
                                    )
                                }
                            } else if (
                                (FacetedFiltersActions as any)[
                                    'set' + storeParamName
                                ]
                            ) {
                                promises.push(
                                    this.$store.dispatch(
                                        (FacetedFiltersActions as any)[
                                            'set' + storeParamName
                                        ],
                                        param.value
                                    )
                                )
                            } else if (
                                (GranulaFiltersActions as any)[
                                    'set' + storeParamName
                                ]
                            ) {
                                promises.push(
                                    this.$store.dispatch(
                                        (GranulaFiltersActions as any)[
                                            'set' + storeParamName
                                        ],
                                        param.value
                                    )
                                )
                            } else if (
                                (IrisFiltersActions as any)[
                                    'set' + storeParamName
                                ]
                            ) {
                                promises.push(
                                    this.$store.dispatch(
                                        (IrisFiltersActions as any)[
                                            'set' + storeParamName
                                        ],
                                        param.value
                                    )
                                )
                            } else if (
                                (InSituFiltersActions as any)[
                                    'set' + storeParamName
                                ]
                            ) {
                                promises.push(
                                    this.$store.dispatch(
                                        (InSituFiltersActions as any)[
                                            'set' + storeParamName
                                        ],
                                        param.value
                                    )
                                )
                            } else if (
                                (SearchActions as any)['set' + storeParamName]
                            ) {
                                if (storeParamName === 'ParentRefs') {
                                    const value = JSON.parse(param.value)
                                    if (
                                        value[0].dataSource === DataSources.DAB
                                    ) {
                                        promises.push(
                                            this.$store.dispatch(
                                                SearchActions.setParentRefs,
                                                value
                                            )
                                        )
                                    }
                                } else if (storeParamName === 'StartIndex') {
                                    promises.push(
                                        this.$store.dispatch(
                                            SearchActions.setStartIndex,
                                            {
                                                value: param.value,
                                                dataSource:
                                                    this.$store.getters[
                                                        SearchGetters.dataSource
                                                    ]
                                            }
                                        )
                                    )
                                } else if (storeParamName === 'DataSource') {
                                    promises.push(
                                        this.$store.dispatch(
                                            (SearchActions as any)[
                                                'set' + storeParamName
                                            ],
                                            { value: param.value }
                                        )
                                    )
                                } else {
                                    promises.push(
                                        this.$store.dispatch(
                                            (SearchActions as any)[
                                                'set' + storeParamName
                                            ],
                                            param.value
                                        )
                                    )
                                }
                            }
                        } else if (param.name.indexOf('m:') === 0) {
                            const cuttedParamName = param.name.substr(2)
                            const storeParamName =
                                cuttedParamName.charAt(0).toUpperCase() +
                                cuttedParamName.substr(1)

                            if ((MapActions as any)['set' + storeParamName]) {
                                promises.push(
                                    this.$store.dispatch(
                                        (MapActions as any)[
                                            'set' + storeParamName
                                        ],
                                        param.value
                                    )
                                )
                            }
                        }
                    }

                    Promise.all(promises).then(() => resolve(true))
                } else {
                    resolve(false)
                }
            })
        },
        getSettings() {
            const promises = [
                this.parseQueryParams(),
                LogService.createElasticSearchClient(),
                GeneralApiService.getUserSettings(),
                GeneralApiService.getSiteData(this.$route.params.siteurl)
            ]

            if (this.$nuxt.$auth.loggedIn) {
                promises.push(GeossSearchApiService.getBookmarks())
            }

            Promise.all(promises).then(
                async ([paramsParsed, , userSettings, siteData, bookmarks]) => {
                    if (
                        this.$store.getters[GeneralFiltersGetters.state].phrase
                    ) {
                        this.$store.dispatch(
                            SearchActions.setCancelConfirmSearch,
                            true
                        )
                    }

                    if (siteData) {
                        if (siteData.name && siteData.name !== '') {
                            this.$store.dispatch(
                                SearchEngineActions.setSiteName,
                                siteData.name
                            )
                        }
                        if (siteData.logoUrl && siteData.logoUrl !== '') {
                            this.$store.dispatch(
                                SearchEngineActions.setSiteLogo,
                                siteData.logoUrl
                            )
                        }
                        if (siteData.url && siteData.url !== '') {
                            this.$store.dispatch(
                                SearchEngineActions.setSiteUrl,
                                siteData.url
                            )
                        }
                        if (siteData.siteId !== '') {
                            const siteId = siteData.siteId
                                ? siteData.siteId * 1
                                : 0
                            this.$store.dispatch(
                                SearchEngineActions.setSiteId,
                                siteId
                            )

                            const siteSettings =
                                await GeneralApiService.getSiteSettings(siteId)

                            if (siteSettings) {
                                if (
                                    siteSettings.defaultDataSource &&
                                    siteSettings.defaultDataSource !== ''
                                ) {
                                    if (
                                        !this.$store.getters[
                                            SearchGetters.dataSource
                                        ]
                                    ) {
                                        this.$store.dispatch(
                                            SearchEngineActions.setDefaultSourceName,
                                            siteSettings.defaultDataSource
                                        )
                                        this.$store.dispatch(
                                            SearchActions.setDataSource,
                                            {
                                                value: DataSources.DAB,
                                                checkDefault: true
                                            }
                                        )
                                    }
                                } else {
                                    this.$store.dispatch(
                                        SearchActions.setDataSource,
                                        { value: DataSources.DAB }
                                    )
                                }
                                if (siteSettings.mapZoom) {
                                    this.$store.dispatch(
                                        MapActions.setInitialZoom,
                                        siteSettings.mapZoom
                                    )
                                }
                                if (
                                    siteSettings.longitude &&
                                    siteSettings.latitude
                                ) {
                                    this.$store.dispatch(MapActions.setCenter, [
                                        siteSettings.longitude,
                                        siteSettings.latitude
                                    ])
                                }
                                Vue.use(VueMatomo, {
                                    router: this.$router,
                                    host: `${this.$nuxt.$config.matomoUrl}`,
                                    siteId: siteSettings.matomoSiteId,
                                    enableLinkTracking: true,
                                    requireConsent: false,
                                    trackInitialView: true,
                                    disableCookies: true,
                                    requireCookieConsent: false
                                })
                                if (
                                    siteSettings.matomoSiteId &&
                                    window._paq &&
                                    window._paq.push
                                ) {
                                    await window._paq.push([
                                        'setSiteId',
                                        siteSettings.matomoSiteId
                                    ])
                                    this.$store.dispatch(
                                        GeneralActions.setMatomoSiteId,
                                        siteSettings.matomoSiteId
                                    )
                                }
                            }

                            const searchSettings =
                                await GeneralApiService.getSearchSettings(
                                    siteId
                                )

                            if (searchSettings) {
                                this.$store.dispatch(
                                    SearchEngineActions.setDabBaseUrl,
                                    searchSettings['dabBaseUrl']
                                )
                                this.$store.dispatch(
                                    SearchEngineActions.setDabDataProvidersUrl,
                                    searchSettings['dabDataProvidersUrl']
                                )
                                this.$store.dispatch(
                                    SearchEngineActions.setDabDataProvidersUsername,
                                    searchSettings['dabDataProvidersUsername']
                                )
                                this.$store.dispatch(
                                    SearchEngineActions.setDabDataProvidersPassword,
                                    searchSettings['dabDataProvidersPassword']
                                )
                                this.$store.dispatch(
                                    SearchEngineActions.setInternalOpenSearchUrl,
                                    searchSettings['geossCrOpensearchUrl']
                                )

                                this.$store.dispatch(
                                    SearchEngineActions.setW3wKey,
                                    searchSettings['w3wKey']
                                )
                                this.$store.dispatch(
                                    SearchEngineActions.setTourUrl,
                                    searchSettings['tourUrl']
                                )
                                this.$store.dispatch(
                                    SearchEngineActions.setKpBaseUrl,
                                    searchSettings['kpBaseUrl']
                                )
                                this.$store.dispatch(
                                    MapActions.setBoxAccessToken,
                                    searchSettings['mapBoxAccessToken']
                                )
                                this.$store.dispatch(
                                    MapActions.setGooglesApiKey,
                                    searchSettings['googleMapsApiKey']
                                )

                                const defaultLayers = await GeneralApiService.getDefaultLayers(
                                    siteId
                                )

                                if (
                                    defaultLayers &&
                                    defaultLayers.length
                                ) {
                                    for (const [index, layer] of defaultLayers.entries()) {
                                        let mapLayer = null
                                        layer.zIndex = index + 1
                                        layer.type = layer.type || layer.url.split('.').pop().toLowerCase()
                                        if (layer.type === LayerTypes.WMS) {
                                            mapLayer =
                                                LayersUtils.createWMS(
                                                    layer.title,
                                                    layer.url
                                                )
                                        } else if (
                                            layer.type === LayerTypes.TMS
                                        ) {
                                            mapLayer =
                                                LayersUtils.createTMS(
                                                    layer.url
                                                )
                                        } else if (
                                            layer.type === LayerTypes.KML
                                        ) {
                                            mapLayer =
                                                LayersUtils.createKML(
                                                    layer.url
                                                )
                                        } else if (
                                            layer.type === LayerTypes.KMZ
                                        ) {
                                            mapLayer =
                                                LayersUtils.createKMZ(
                                                    layer.url
                                                )
                                        } else {
                                            continue
                                        }

                                        mapLayer.setZIndex(layer.zIndex)

                                        const layerData = {
                                            layer: mapLayer,
                                            id: layer.url,
                                            type: layer.type,
                                            title: layer.name,
                                            visible: layer.visible
                                        }
                                        this.$store.dispatch(
                                            MapActions.addLayer,
                                            layerData
                                        )
                                    }
                                }

                                // if (searchSettings.popularSearch) {
                                //     this.$store.dispatch(MyWorkspaceActions.setPopularSearchId, searchSettings.popularSearch.id);
                                //     if (searchSettings.popularSearch.currMap) {
                                //         const notDeclared = this.$store.getters[MapGetters.activeLayerTileId] === '';
                                //         const newCommunitySite = sessionStorage.getItem('COMMUNITY_SITE_ID') !== '' + this.$store.getters[UserGetters.groupId];
                                //         if (notDeclared || newCommunitySite) {
                                //             this.$store.dispatch(MapActions.setActiveLayerTileId, searchSettings.popularSearch.currMap);
                                //         }
                                //     }
                                // }
                            }
                        }
                    }

                    if (bookmarks && bookmarks.items) {
                        this.$store.dispatch(
                            UserActions.setBookmarks,
                            bookmarks.items
                        )
                    }

                    if (userSettings) {
                        if (
                            userSettings.dhus &&
                            userSettings.dhus.credentialsAvailable
                        ) {
                            this.$store.dispatch(
                                MyWorkspaceActions.setDhusUsername,
                                userSettings.dhus.username
                            )
                        }
                        if (userSettings.geossVersion) {
                            const savedGeossVersion =
                                this.$cookies.get('geoss_version')
                            if (
                                savedGeossVersion &&
                                userSettings.geossVersion.geossVersion &&
                                userSettings.geossVersion
                                    .showNewVersionTooltips &&
                                savedGeossVersion !==
                                    userSettings.geossVersion.geossVersion
                            ) {
                                this.$store.dispatch(PopupActions.openPopup, {
                                    contentId: 'welcome',
                                    component: WelcomePopup
                                })
                            }
                            if (userSettings.geossVersion.geossVersion) {
                                const date = new Date()
                                date.setFullYear(date.getFullYear() + 20)
                                this.$cookies.set(
                                    'geoss_version',
                                    userSettings.geossVersion.geossVersion,
                                    date
                                )
                            }
                        }

                        if (
                            userSettings.bbox &&
                            typeof userSettings.bbox.opacity !== 'undefined' &&
                            userSettings.bbox.opacity !== null &&
                            !Number.isNaN(
                                parseInt(userSettings.bbox.opacity, 10)
                            )
                        ) {
                            this.$store.dispatch(
                                MapActions.changeLayerTransparency,
                                {
                                    id: LayerTypes.BOUNDING,
                                    value: parseInt(
                                        userSettings.bbox.opacity,
                                        10
                                    )
                                }
                            )
                        }
                    }
                    this.$store.dispatch(
                        GeneralActions.setStoreInitialized,
                        true
                    )
                    if (!paramsParsed) {
                        UtilsService.pushToHistory(true)
                    }

                    if (
                        !siteData.name ||
                        siteData.name === '' ||
                        !siteData.logoUrl ||
                        siteData.logoUrl === ''
                    ) {
                        this.$router.push('/creator?siteUrl='+this.$route.params.siteurl)
                    }
                }
            )

            // this.$ga.page(window.location.pathname + window.location.search);

            LogService.logRecommendationData('Search', 'Search', 'external')
            if (this.isBulkDownloadEnabled) {
                const bulkDownloadItems = JSON.parse(
                    sessionStorage.getItem('bulkDownload') as string
                )
                if (
                    this.$nuxt.$auth.loggedIn &&
                    bulkDownloadItems &&
                    bulkDownloadItems.length
                ) {
                    bulkDownloadItems.forEach((link: any) => {
                        this.$store.dispatch(BulkDownloadActions.addLink, link)
                    })
                }
            }

            const fileDownloadItems = JSON.parse(
                sessionStorage.getItem('fileDownload') as string
            )
            if (
                this.$nuxt.$auth.loggedIn &&
                fileDownloadItems &&
                fileDownloadItems.length
            ) {
                fileDownloadItems.forEach((file: any) => {
                    this.$store.dispatch(FileDownloadActions.addFile, file)
                })
            }

            const spinnerElem: HTMLElement | null = document.querySelector(
                '.earch-rocket-spinner'
            )
            if (spinnerElem) {
                spinnerElem.classList.add('inactive')
            }
        }
    },

    watch: {
        storeInitialized(newVal) {
            if (!newVal) {
                this.getSettings()
            }
        },
        route(newVal) {
            if (newVal.params.siteurl !== this.siteUrl) {
                this.getSettings()
            }
        }
    },

    mounted() {
        AppVueObj.app.$store = this.$store

        if (!this.storeInitialized) {
            this.getSettings()
        }
    }
}
</script>

<style lang="scss">
@import '~/assets/scss/reset.scss';
@import '~/assets/scss/general.scss';

body {
    @include regular-page-body;
}

.sub-page {
    @include regular-page;

    &__content {
        @include regular-page-content;
    }
}
</style>
