<template>
    <div v-bar class="bookmark-results__wrapper">
        <div class="bookmark-results">
            <div
                v-if="resultsLoading || !results.length"
                class="bookmark-results__empty"
                :class="{ loading: resultsLoading }"
            >
                <div v-if="!resultsLoading && !results.length">
                    <img :src="`/svg/data-gray.svg`" alt="Geoss Portal" />
                    <span v-if="!resultsLoading && !results.length">
                        {{ $tc('bookmarks.yourListIsEmpty') }}
                    </span>
                </div>
            </div>
            <div
                v-for="result of results"
                :key="result.id"
                class="bookmark-result"
                :class="{ 'open-map': toggleMapId == result.id }"
                :data-id="result.id"
            >
                <div
                    class="bookmark-result__wrapper"
                    :class="{
                        hover:
                            toggleShare === result.id ||
                            toggleDownloads === result.id,
                    }"
                >
                    <div class="bookmark-result__checkbox">
                        <input
                            type="checkbox"
                            :id="result.id"
                            autocomplete="off"
                            :checked="isChecked(result.targetId)"
                            @change="checkboxChange(result.targetId)"
                        />
                        <label :for="result.id"></label>
                    </div>
                    <div
                        class="bookmark-result__image"
                        :class="{
                            'bookmark-result__image--default':
                                getImage(result.logo) !== result.logo,
                        }"
                    >
                        <img
                            :src="getImage(result.logo)"
                            @error="imageLoadError(result.logo)"
                            :alt="result.name"
                            v-image-preview
                        />
                    </div>
                    <div class="bookmark-result__content">
                        <div class="bookmark-result__text-data">
                            <div class="bookmark-result__text-data--title">
                                <div
                                    v-if="result.name"
                                    class="bookmark-result__title"
                                >
                                    {{ result.name }}
                                </div>
                                <div
                                    v-if="
                                        result.metadata && result.metadata.title
                                    "
                                    class="bookmark-result__title"
                                >
                                    {{ result.metadata.title }}
                                </div>
                                <div
                                    class="bookmark-result__text-data--datasource"
                                    :data-source="getResultsDataSource(result)"
                                ></div>
                            </div>
                            <div
                                v-if="
                                    result.contributor &&
                                    result.contributor.orgName
                                "
                                class="bookmark-result__contributor"
                            >
                                ({{ $tc('dabResult.organisation') }}:
                                {{ result.contributor.orgName }})
                            </div>
                            <div
                                v-if="
                                    result.metadata && result.metadata.creators
                                "
                                class="bookmark-result__contributor"
                            >
                                ({{ $tc('dabResult.creators') }}:
                                <span
                                    v-for="(creator, index) of result.metadata
                                        .creators"
                                    :key="index"
                                >
                                    {{ creator.name }}
                                </span>
                                )
                            </div>
                            <div
                                v-if="
                                    result.summary &&
                                    typeof result.summary === 'string'
                                "
                                class="bookmark-result__summary"
                                v-html-to-text="result.summary"
                            ></div>
                            <div
                                v-if="
                                    result.metadata &&
                                    result.metadata.description &&
                                    typeof result.metadata.description ===
                                        'string'
                                "
                                class="bookmark-result__summary"
                                v-html-to-text="result.metadata.description"
                            ></div>
                        </div>
                        <div>
                            <button
                                @click="showDetails(result)"
                                class="bookmark-result-details__more"
                            >
                                <span class="">
                                    {{ $tc('dabResult.seeMore') }}
                                </span>
                                <span class="arrow"></span>
                            </button>
                        </div>
                        <div class="bookmark-result__tools">
                            <div class="reviews">
                                <div class="recent_views">
                                    <i class="icomoon-eye"></i>
                                    <span>{{ result.views || 0 }}</span>
                                    <span>
                                        {{ $tc('bookmarks.recentViews') }}
                                    </span>
                                </div>
                                <div
                                    class="comments"
                                    v-if="result.rating"
                                    @click="setScore(result)"
                                >
                                    <i
                                        v-for="score in [1, 2, 3, 4, 5]"
                                        :key="score"
                                        class="icomoon-star"
                                        :class="{
                                            active:
                                                Math.floor(
                                                    result.rating.averageScore
                                                ) >= score,
                                        }"
                                    />
                                    <span>
                                        {{
                                            result.rating.averageScore.toFixed(
                                                1
                                            )
                                        }}
                                    </span>
                                </div>
                                <div class="persons" v-if="result.rating">
                                    <span>
                                        {{ result.rating.totalEntries }}
                                    </span>
                                </div>
                            </div>
                            <div class="actions">
                                <div class="actions--main">
                                    <a
                                        class="show-on-map"
                                        target="_blank"
                                        :href="
                                            createHref(result, 'show-on-map')
                                        "
                                        :title="$tc('dabResult.showOnMap')"
                                    >
                                        <i class="icomoon-show-on-map"></i>
                                    </a>
                                    <a
                                        v-if="addParentRefAvailable(result)"
                                        class="drill-down"
                                        target="_blank"
                                        :href="createHref(result, 'drill-down')"
                                        :title="
                                            $tc('dabResult.showInsideFolder')
                                        "
                                    >
                                        <i class="icomoon-drill-down"></i>
                                    </a>
                                </div>
                                <div class="actions--side">
                                    <Share :url="prepareShareUrl(result)" />
                                    <a
                                        class="toggle-downloads"
                                        @click="toggleShowDownloads(result)"
                                        :class="{
                                            active:
                                                toggleDownloads === result.id,
                                        }"
                                        :disabled="
                                            !result.downloads ||
                                            !result.downloads.length
                                        "
                                        :title="$tc('dabResult.downloads')"
                                    >
                                        <i class="icomoon-arrow down"></i>
                                    </a>
                                </div>
                                <div class="toggle-wrapper">
                                    <CollapseTransition>
                                        <div
                                            class="downloads-wrapper"
                                            v-show="
                                                toggleDownloads === result.id
                                            "
                                            v-if="
                                                result.downloads &&
                                                result.downloads.length
                                            "
                                        >
                                            <div>
                                                <span
                                                    v-for="(
                                                        download, index
                                                    ) of result.downloads"
                                                    :key="index"
                                                >
                                                    <a
                                                        v-if="download.links"
                                                        @click="
                                                            openDownloadLinksPopup(
                                                                result,
                                                                download.links
                                                            )
                                                        "
                                                        :title="
                                                            download.type.toUpperCase()
                                                        "
                                                    >
                                                        <i
                                                            v-if="
                                                                FileFormatsIcons.indexOf(
                                                                    download.type
                                                                ) !== -1
                                                            "
                                                            :class="`icomoon-doc-${download.type}`"
                                                        ></i>
                                                        <i
                                                            v-else
                                                            class="icomoon-doc-file"
                                                        ></i>
                                                    </a>
                                                    <a
                                                        v-else-if="
                                                            download.url.indexOf(
                                                                '/dhus/odata/'
                                                            ) !== -1
                                                        "
                                                        @click="
                                                            openSentinelLoginPopup(
                                                                download.url,
                                                                result
                                                            )
                                                        "
                                                        :title="
                                                            download.type.toUpperCase()
                                                        "
                                                    >
                                                        <i
                                                            v-if="
                                                                FileFormatsIcons.indexOf(
                                                                    download.type
                                                                ) !== -1
                                                            "
                                                            :class="`icomoon-doc-${download.type}`"
                                                        ></i>
                                                        <i
                                                            v-else
                                                            class="icomoon-doc-file"
                                                        ></i>
                                                    </a>
                                                    <a
                                                        v-else-if="
                                                            download.url.indexOf(
                                                                '/sdg/Series/DataCSV'
                                                            ) !== -1
                                                        "
                                                        @click="
                                                            getUnepFile(
                                                                download.postData,
                                                                result
                                                            )
                                                        "
                                                        :title="
                                                            download.type.toUpperCase()
                                                        "
                                                    >
                                                        <i
                                                            v-if="
                                                                FileFormatsIcons.indexOf(
                                                                    download.type
                                                                ) !== -1
                                                            "
                                                            :class="`icomoon-doc-${download.type}`"
                                                        ></i>
                                                        <i
                                                            v-else
                                                            class="icomoon-doc-file"
                                                        ></i>
                                                    </a>
                                                    <a
                                                        @click="
                                                            downloadLinkClicked(
                                                                result
                                                            )
                                                        "
                                                        :href="download.url"
                                                        target="_blank"
                                                        v-else-if="
                                                            download.type !==
                                                            'custom-download'
                                                        "
                                                        :title="
                                                            download.type.toUpperCase()
                                                        "
                                                    >
                                                        <i
                                                            v-if="
                                                                FileFormatsIcons.indexOf(
                                                                    download.type
                                                                ) !== -1
                                                            "
                                                            :class="`icomoon-doc-${download.type}`"
                                                        ></i>
                                                        <i
                                                            v-else
                                                            class="icomoon-doc-file"
                                                        ></i>
                                                    </a>
                                                    <a
                                                        v-else
                                                        @click="
                                                            initCustomDownloadPopup(
                                                                result,
                                                                download.url
                                                            )
                                                        "
                                                        :title="
                                                            $tc(
                                                                'general.customDownload'
                                                            )
                                                        "
                                                    >
                                                        <i
                                                            class="icomoon-custom-download"
                                                        ></i>
                                                    </a>
                                                </span>
                                            </div>
                                        </div>
                                    </CollapseTransition>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="toggle-map" @click="toggleMap(result.id)">
                        <i></i>
                    </div>
                    <div
                        class="bookmark-result__map"
                        :id="`map-${result.targetId}`"
                    ></div>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import to from '@/utils/to'
import { Component, Vue, Watch } from 'vue-property-decorator'
import { SearchGetters } from '@/store/search/search-getters'
import { BookmarksActions } from '@/store/bookmarks/bookmarks-actions'
import { BookmarksGetters } from '@/store/bookmarks/bookmarks-getters'
import { DataSources } from '@/interfaces/DataSources'
import LayerTilesService from '@/services/map/layer-tiles.service'
import GeossSearchApiService from '@/services/geoss-search.api.service'
import { GeneralApiService } from '@/services/general.api.service'
import DabResultRating from '@/components/Search/Results/DabResultRating.vue'
import { PopupActions } from '@/store/popup/popup-actions'
import { Liferay } from '@/data/global'
import LogService from '@/services/log.service'
import SpinnerService from '../../services/spinner.service'
import DabResultDownloads from '@/components/Search/Results/DabResultDownloads.vue'
import DabResultCustomDownload from '@/components/Search/Results/DabResultCustomDownload.vue'
import CustomDownloadWCS from '@/components/Search/Results/CustomDownloadWCS.vue'
import UtilsService from '@/services/utils.service'
import { FileFormatsIcons } from '@/data/file-formats-icons'
import MouseLeaveService from '@/services/mouse-leave.service'
import ErrorPopup from '@/components/ErrorPopup.vue'
import { MyWorkspaceGetters } from '@/store/myWorkspace/my-workspace-getters'
import SearchEngineService from '@/services/search-engine.service'
import SentinelLogin from '@/components/Search/SentinelLogin.vue'
import DabResultMetadata from '@/components/Search/Results/DabResultMetadata.vue'
import { SearchEngineGetters } from '~/store/searchEngine/search-engine-getters'
import { ResultService } from '../../services/result.service'
import MapCoordinatesUtils from '@/services/map/coordinates-utils'
import { AppVueObj } from '~/data/global'
import CollapseTransition from '~/plugins/CollapseTransition'
import RatingService from '~/services/ratings.service'
import { SearchActions } from '~/store/search/search-actions'
import { PopupGetters } from '~/store/popup/popup-getters'

@Component({
    components: {
        CollapseTransition,
    },
})
export default class BookmarksList extends Vue {
    [x: string]: any

    public startIndex = 0
    public bookmarksPerPage = 10
    public bookmarksTotal = 0
    public toggleMapId = 0
    public mapsObject: any = {}
    public FileFormatsIcons = FileFormatsIcons
    public toggleDownloads = 0
    public toggleShare = 0
    public shareUrl = ''

    get results() {
        return this.$store.getters[BookmarksGetters.results]
    }

    get pageOffset() {
        return this.$store.getters[BookmarksGetters.pageOffset]
    }

    get resultsLoading() {
        return this.$store.getters[BookmarksGetters.resultsLoading]
    }

    get resultsOrigin() {
        return this.$store.getters[BookmarksGetters.resultsOrigin]
    }

    public imageLoadError(imagePath: string) {
        return AppVueObj.imageLoadError(imagePath)
    }

    public isChecked(id: any) {
        return this.$store.getters[BookmarksGetters.checkedResults].includes(
            id.toString()
        )
    }

    public checkboxChange(id: any) {
        this.$store.dispatch(BookmarksActions.checkboxChange, id.toString())
    }

    // public getResultsDataOrigin(id: any, name: any) {
    //     const safeId = GeossSearchApiService.safeString(id)
    //     const safeName = GeossSearchApiService.safeString(name)
    //     if (this.resultsOrigin.byId[safeId]) {
    //         return this.resultsOrigin.byId[safeId].dataOrigin
    //     } else if (this.resultsOrigin.byName[safeName]) {
    //         return this.resultsOrigin.byName[safeName].dataOrigin
    //     }
    // }

    public getResultsDataSource(result: any) {
        return result.dataSource ? result.dataSource : ''
        // const safeId = GeossSearchApiService.safeString(id)
        // const safeName = GeossSearchApiService.safeString(name)
        // if (this.resultsOrigin.byId[safeId]) {
        //     return this.resultsOrigin.byId[safeId].dataSource
        // } else if (this.resultsOrigin.byName[safeName]) {
        //     return this.resultsOrigin.byName[safeName].dataSource
        // }
    }

    public getResultsCurrMap(result: any) {
        return 'osm'
        // const safeId = GeossSearchApiService.safeString(id)
        // const safeName = GeossSearchApiService.safeString(name)
        // if (this.resultsOrigin.byId[safeId]) {
        //     return this.resultsOrigin.byId[safeId].currMap
        // } else if (this.resultsOrigin.byName[safeName]) {
        //     return this.resultsOrigin.byName[safeName].currMap
        // }
    }

    public toggleMap(id: any) {
        if (this.toggleMapId === id.toString()) {
            this.toggleMapId = 0
        } else {
            this.toggleMapId = id.toString()
        }
    }

    public createHref(result: any, action: any) {
        const targetId = result.targetId
        const phrase = result.name || result.metadata.title
        const categories = result.category
        const dataSource = this.getResultsDataSource(result)
        const currMap = this.getResultsCurrMap(result)
        const parentRefs = encodeURIComponent(
            `[{"id":"${targetId}","dataSource":"${dataSource}"}]`
        )
        const additionalParams = `&f:dataSource=${dataSource}&m:activeLayerTileId=${currMap}`

        if (action === 'show-on-map') {
            if (!result.hasLayer) {
                return ''
            } else {
                return `/?targetId=${targetId}${additionalParams}`
            }
        } else if (action === 'drill-down') {
            return `/?f:parentRefs=${parentRefs}${additionalParams}`
        } else if (action === 'share') {
            return `${window.location.origin}/?targetId=${targetId}${additionalParams}`
        } else {
            return ''
        }
    }

    public zoomOnFeatures(map: any, layer: any) {
        const feature = layer.getSource().getFeatures()[0]
        if (
            (layer.boundingType === 'boundingPin' ||
                layer.boundingType === 'boundingCircle') &&
            feature.getGeometry().getCoordinates().length === 1
        ) {
            let pin = feature.getGeometry().getCoordinates()[0]
            pin = AppVueObj.ol.proj.transform(pin, 'EPSG:3857', 'EPSG:4326')
            const coordinatesForDrawing: any =
                MapCoordinatesUtils.coordinatesForDrawing([
                    pin[0] - 10,
                    Math.max(pin[1] - 10, -90),
                    pin[0] + 10,
                    Math.min(pin[1] + 10, 90),
                ])
            let geometry: any = new AppVueObj.ol.geom.Polygon(
                coordinatesForDrawing
            )
            geometry = geometry.transform('EPSG:4326', 'EPSG:3857')
            map.getView().fit(geometry)
        } else {
            const extent = layer.getSource().getExtent()
            map.getView().fit(extent, map.getSize())
        }
    }

    public addParentRefAvailable(result: any) {
        const parentRefAvailable =
            UtilsService.extractCategoriesByAttributeValue(
                result,
                'label',
                'series'
            )
        return !(parentRefAvailable.indexOf('hlevel') === -1)
    }

    public buildMaps() {
        this.$nextTick(() => {
            this.results.forEach((result: any, index: number) => {
                const currMap = this.getResultsCurrMap(result)
                const feature = ResultService.getFeature(result, index)
                const layers = [LayerTilesService[currMap].getLayerTile()]
                if (feature) {
                    layers.push(feature)
                    this.$set(result, 'hasLayer', true)
                } else {
                    this.$set(result, 'hasLayer', false)
                }
                console.log(this.mapsObject[result.targetId])

                if (!this.mapsObject[result.targetId]) {
                    this.mapsObject[result.targetId] = new AppVueObj.ol.Map({
                        layers,
                        target: `map-${result.targetId}`,
                        view: new AppVueObj.ol.View({
                            center: [0, 0],
                            zoom: 0,
                            maxZoom: 10,
                        }),
                        controls: [],
                        interactions: [],
                    })

                    if (feature) {
                        this.zoomOnFeatures(this.mapsObject[result.id], feature)
                    }
                }
            })
        })
    }

    public async setScore(result: any) {
        const dataOrigin = this.getResultsDataSource(result)
        const [, comments] = await to(
            GeossSearchApiService.getComments(result.id, dataOrigin)
        )
        const props = {
            id: result.id,
            title: result.name || result.metadata.title,
            comments,
            userScore: 5,
            userComment: result.rating.comment,
            dataSource: dataOrigin,
            referer: 'bookmarks',
        }
        this.$store.dispatch(PopupActions.openPopup, {
            contentId: 'rating',
            title: this.$tc('popupTitles.rateResouce'),
            component: DabResultRating,
            props,
        })
    }

    public prepareShareUrl(result: any) {
        return this.createHref(result, 'share')
    }

    public toggleShowDownloads(result: any) {
        this.toggleShare = 0
        if (this.toggleDownloads === result.id) {
            this.toggleDownloads = 0
        } else {
            this.toggleDownloads = result.id
        }
    }

    public initResultDownloads(results: any) {
        for (const result of results) {
            let resultDownloads = []
            const timeSeriesArray = []

            const downloads: any = []
            const dataSource = this.getResultsDataSource(result)
            if (dataSource === DataSources.NEXTGEOSS) {
                const data: any = UtilsService.getArrayByString(result, 'link')
                for (const item of data) {
                    if (
                        item._attributes &&
                        item._attributes.rel &&
                        item._attributes.rel === 'enclosure'
                    ) {
                        const linkText = item._attributes.href.replace(
                            '&amp;',
                            '&'
                        )
                        const linkTitle = item._attributes.title
                        const linkDescription = ''
                        const titleBox = result.name
                        const linkScore = -1

                        /* Simple resources aggregation */
                        if (item._attributes.type) {
                            if (item._attributes.type === 'text/html') {
                                downloads.push({
                                    name: linkTitle,
                                    url: linkText,
                                    desc: linkDescription,
                                    type: 'html',
                                    title: titleBox,
                                    score: linkScore,
                                })
                            } else if (
                                item._attributes.type.indexOf('image') > -1
                            ) {
                                downloads.push({
                                    name: linkTitle,
                                    url: linkText,
                                    desc: linkDescription,
                                    type: 'img',
                                    title: titleBox,
                                    score: linkScore,
                                })
                            } else if (item._attributes.type === 'text/xml') {
                                downloads.push({
                                    name: linkTitle,
                                    url: linkText,
                                    desc: linkDescription,
                                    type: 'xml',
                                    title: titleBox,
                                    score: linkScore,
                                })
                            } else {
                                downloads.push({
                                    name: linkTitle,
                                    url: linkText,
                                    desc: linkDescription,
                                    type: 'other',
                                    title: titleBox,
                                    score: linkScore,
                                })
                            }
                        }
                    }
                }
                resultDownloads = downloads
            } else if (dataSource === DataSources.ZENODO) {
                // TODO: Verify if it works
                if (result.files) {
                    for (const file of result.files) {
                        const knownFileTypes = [
                            'pdf',
                            'png',
                            'jpg',
                            'xml',
                            'txt',
                        ]
                        const linkTitle = file.key
                        const linkText = file.links.self
                        const linkDescription = file.key
                        const linkType = knownFileTypes.includes(file.type)
                            ? file.type
                            : 'file' // supported individual icons, 'file' if default
                        downloads.push({
                            name: linkTitle,
                            url: linkText,
                            desc: linkDescription,
                            type: linkType,
                        })
                    }
                    downloads.push({
                        name: result.metadata.title,
                        url: result.links.doi,
                        desc: result.metadata.description,
                        type: 'html',
                    })
                }
                resultDownloads = downloads
            } else {
                const data = UtilsService.getArrayByString(
                    result,
                    'gmd:distributionInfo.gmd:MD_Distribution.gmd:transferOptions.gmd:MD_DigitalTransferOptions.gmd:onLine'
                )
                for (const item of data) {
                    const linkText = UtilsService.getPropByString(
                        item,
                        'gmd:CI_OnlineResource.gmd:linkage.gmd:URL'
                    )
                    let linkTitle = UtilsService.getPropByString(
                        item,
                        'gmd:CI_OnlineResource.gmd:name.gco:CharacterString'
                    )
                    let protocol =
                        UtilsService.getPropByString(
                            item,
                            'gmd:CI_OnlineResource.gmd:protocol.gco:CharacterString'
                        ) || ''
                    if (linkText) {
                        let linkScore = UtilsService.getPropByString(
                            item,
                            'gmd:CI_OnlineResource.gmd:status.gmd:score'
                        )
                        if (!linkScore || linkScore > 100 || linkScore < 0) {
                            linkScore = -1
                        }
                        const wmsAllLayerName = result.name
                        let anchor =
                            UtilsService.getPropByString(
                                item,
                                'gmd:CI_OnlineResource.gmd:description.gmx:Anchor'
                            ) || ''
                        anchor =
                            anchor && anchor._attributes
                                ? anchor._attributes['xlink:href']
                                : anchor
                        protocol = typeof protocol === 'string' ? protocol : ''
                        const titleBox = wmsAllLayerName
                        let kmlKeyword = false
                        for (const category of UtilsService.getArrayByString(
                            result,
                            'category._attributes'
                        )) {
                            if (
                                category.label === 'kml' &&
                                category.term === 'keywords'
                            ) {
                                kmlKeyword = true
                                break
                            }
                        }

                        // parse file extension if any
                        let url = linkText + ''
                        url = url.substring(
                            0,
                            url.indexOf('#') === -1
                                ? url.length
                                : url.indexOf('#')
                        )
                        url = url.substring(
                            0,
                            url.indexOf('?') === -1
                                ? url.length
                                : url.indexOf('?')
                        )
                        url = url.substring(
                            url.lastIndexOf('/') + 1,
                            url.length
                        )
                        const extension =
                            url !== undefined && url.indexOf('.') > -1
                                ? //@ts-ignore

                                  url?.split('.')?.pop().toLowerCase()
                                : ''

                        const complex = anchor.indexOf('complex') > 0

                        const linkTitle2 = UtilsService.getPropByString(
                            item,
                            'gmd:CI_OnlineResource.gmd:function.gmd:CI_OnLineFunctionCode.codeList'
                        )
                        if (!linkTitle || typeof linkTitle !== 'string') {
                            if (linkTitle2) {
                                linkTitle = linkTitle2
                            } else {
                                linkTitle = titleBox
                            }
                        }

                        let linkDescription = UtilsService.getPropByString(
                            item,
                            'gmd:CI_OnlineResource.gmd:description.gco:CharacterString'
                        )
                        const linkDescription2 = UtilsService.getPropByString(
                            item,
                            'gmd:CI_OnlineResource.gmd:name.gco:CharacterString'
                        )
                        if (
                            !linkDescription ||
                            typeof linkDescription !== 'string'
                        ) {
                            if (linkDescription2) {
                                linkDescription = linkDescription2
                            }
                        }

                        if (extension === 'pdf' && !complex) {
                            downloads.push({
                                name: linkTitle,
                                url: linkText,
                                desc: linkDescription,
                                type: 'pdf',
                                title: titleBox,
                                score: linkScore,
                            })
                        } else if (extension === 'txt' && !complex) {
                            downloads.push({
                                name: linkTitle,
                                url: linkText,
                                desc: linkDescription,
                                type: 'txt',
                                title: titleBox,
                                score: linkScore,
                            })
                        } else if (
                            (extension === 'htm' ||
                                extension === 'html' ||
                                extension === 'shtml') &&
                            !complex
                        ) {
                            if (linkText.indexOf('opendap') > -1) {
                                downloads.push({
                                    name: linkTitle,
                                    url: linkText,
                                    desc: linkDescription,
                                    type: 'link',
                                    title: titleBox,
                                    score: linkScore,
                                })
                            } else {
                                downloads.push({
                                    name: linkTitle,
                                    url: linkText,
                                    desc: linkDescription,
                                    type: 'html',
                                    title: titleBox,
                                    score: linkScore,
                                })
                            }
                        } else if (
                            (extension === 'jpg' || extension === 'jpeg') &&
                            linkText.indexOf('tile') === -1 &&
                            !complex
                        ) {
                            if (linkDescription) {
                                downloads.push({
                                    name: linkTitle,
                                    url: linkText,
                                    desc: linkDescription,
                                    type: 'jpg',
                                    title: titleBox,
                                    score: linkScore,
                                })
                            }
                        } else if (
                            extension === 'png' &&
                            linkText.indexOf('tile') === -1 &&
                            !complex
                        ) {
                            if (linkDescription) {
                                downloads.push({
                                    name: linkTitle,
                                    url: linkText,
                                    desc: linkDescription,
                                    type: 'png',
                                    title: titleBox,
                                    score: linkScore,
                                })
                            }
                        } else if (extension === 'xml' && !complex) {
                            downloads.push({
                                name: linkTitle,
                                url: linkText,
                                desc: linkDescription,
                                type: 'xml',
                                title: titleBox,
                                score: linkScore,
                            })
                        } else if (
                            protocol.indexOf('information-html') > -1 &&
                            !complex
                        ) {
                            downloads.push({
                                name: linkTitle,
                                url: linkText,
                                desc: linkDescription,
                                type: 'html',
                                title: titleBox,
                                score: linkScore,
                            })
                        } else if (
                            protocol.indexOf('WebMapService') > -1 &&
                            linkText
                        ) {
                            const urlName = linkText
                            const allName = wmsAllLayerName
                            const wmsName = UtilsService.getPropByString(
                                item,
                                'gmd:CI_OnlineResource.gmd:name.gco:CharacterString'
                            )
                            const wmsNameDesc = UtilsService.getPropByString(
                                item,
                                'gmd:CI_OnlineResource.gmd:description.gco:CharacterString'
                            )

                            // Regex to pick WMS version
                            const wmsProtocol = UtilsService.getPropByString(
                                item,
                                'gmd:CI_OnlineResource.gmd:protocol.gco:CharacterString'
                            )
                            let wmsVersion = '1.0'
                            const match = /((\d)+\.(\d)+(\.(\d)+)?)/g.exec(
                                wmsProtocol
                            )
                            if (match) {
                                wmsVersion = match[0]
                            }

                            let wmsAnchor = null
                            if (anchor !== undefined) {
                                if (anchor.indexOf('simple') > -1) {
                                    wmsAnchor = 'simple'
                                } else if (anchor.indexOf('complex') > -1) {
                                    wmsAnchor = 'complex'
                                }
                            }
                        } else if (
                            protocol.indexOf('WebCoverageService') > -1
                        ) {
                            let wcsUrl = linkText
                            const wcsId = UtilsService.getPropByString(
                                item,
                                'gmd:CI_OnlineResource.gmd:name.gco:CharacterString'
                            )

                            // Regex to pick WCS version
                            const wcsProtocol = UtilsService.getPropByString(
                                item,
                                'gmd:CI_OnlineResource.gmd:protocol.gco:CharacterString'
                            )
                            let wcsVersion = '1.0'
                            const match = /((\d)+\.(\d)+(\.(\d)+)?)/g.exec(
                                wcsProtocol
                            )
                            if (match) {
                                wcsVersion = match[0]
                            }

                            let wcsAnchor = null
                            if (anchor !== undefined) {
                                if (anchor.indexOf('simple') > -1) {
                                    wcsAnchor = 'simple'
                                } else if (anchor.indexOf('complex') > -1) {
                                    wcsAnchor = 'complex'
                                }
                            }
                            if (!wcsAnchor || wcsAnchor === 'simple') {
                                downloads.push({
                                    name: linkTitle,
                                    url: wcsUrl,
                                    desc: linkDescription,
                                    type: 'wcs',
                                    title: titleBox,
                                    score: linkScore,
                                })
                            } else if (wcsAnchor === 'complex') {
                                wcsUrl = `${wcsUrl}&version=${wcsVersion}&coverageId=${wcsId}`
                                downloads.push({
                                    name: linkTitle,
                                    url: wcsUrl,
                                    desc: linkDescription,
                                    type: 'custom-download',
                                    boxTitle: titleBox,
                                    score: 0,
                                })
                            }
                        } else if (
                            protocol.indexOf('WWW:LINK-1.0-http--link') > -1 &&
                            !(complex && linkText) &&
                            !(linkText.indexOf('WMS') > -1) &&
                            !(linkText.indexOf('wms') > -1) &&
                            !(linkText.indexOf('WCS') > -1) &&
                            !(linkText.indexOf('wcs') > -1) &&
                            !(linkText.indexOf('TMS') > -1) &&
                            !(linkText.indexOf('tms') > -1)
                        ) {
                            downloads.push({
                                name: linkTitle,
                                url: linkText,
                                desc: linkDescription,
                                type: 'link',
                                title: titleBox,
                                score: linkScore,
                            })
                        } else if (
                            linkText &&
                            !(linkText.indexOf('WMS') > -1) &&
                            !(linkText.indexOf('wms') > -1) &&
                            !(linkText.indexOf('WCS') > -1) &&
                            !(linkText.indexOf('wcs') > -1) &&
                            !(linkText.indexOf('TMS') > -1) &&
                            !(linkText.indexOf('tms') > -1) &&
                            !(anchor.indexOf('complex') > 0)
                        ) {
                            const lastChar = linkText.substr(
                                linkText.length - 1
                            )
                            if (lastChar === '/' && linkDescription) {
                                if (linkTitle.indexOf('GET DATA') > -1) {
                                    downloads.push({
                                        name: linkTitle,
                                        url: linkText,
                                        desc: linkDescription,
                                        type: 'link',
                                        title: titleBox,
                                        score: linkScore,
                                    })
                                } else if (linkTitle.indexOf('VIEW') > -1) {
                                    downloads.push({
                                        name: linkTitle,
                                        url: linkText,
                                        desc: linkDescription,
                                        type: 'other',
                                        title: titleBox,
                                        score: linkScore,
                                    })
                                }
                            } else {
                                downloads.push({
                                    name: linkTitle,
                                    url: linkText,
                                    desc: linkDescription,
                                    type: 'other',
                                    title: titleBox,
                                    score: linkScore,
                                })
                            }
                        }

                        if (protocol === 'gwp_un_sd_/v1/sdg') {
                            const wordsArray = result.name.split(' ')
                            if (
                                wordsArray.length === 3 &&
                                wordsArray[0] === 'SDG' &&
                                wordsArray[2] === 'Indicator'
                            ) {
                                let seriesId = UtilsService.getArrayByString(
                                    item,
                                    'gmd:CI_OnlineResource.gmd:name.gco:CharacterString'
                                )[0]
                                const seriesDesc =
                                    UtilsService.getArrayByString(
                                        item,
                                        'gmd:CI_OnlineResource.gmd:description.gco:CharacterString'
                                    )[0]
                                // Quick fix: take key from url
                                if (
                                    seriesId.includes(
                                        'https://unstats.un.org/SDGAPI/v1/sdg/Series/Data?seriesCode='
                                    )
                                ) {
                                    seriesId = seriesId.split('seriesCode=')[1]
                                }
                                timeSeriesArray.push({
                                    id: seriesId,
                                    text: seriesDesc,
                                })
                                const postData = {
                                    indicator: wordsArray[1],
                                    series: [seriesId],
                                }
                                downloads.push({
                                    name: `SDG-${wordsArray[1]}-${seriesId}.csv`,
                                    url: 'https://unstats.un.org/SDGAPI/v1/sdg/Series/DataCSV',
                                    postData,
                                    desc: `${wordsArray[1]}, ${seriesDesc}`,
                                    type: 'csv',
                                })
                            }
                        }
                    }

                    // Direct Link - replace existing or add new URL
                    const directLink = UtilsService.getPropByString(
                        item,
                        'gmd:CI_OnlineResource.gmd:directAccessLink'
                    )
                    if (directLink) {
                        if (downloads.length === 1) {
                            downloads[0].url = directLink
                        } else if (!downloads.length) {
                            let linkScore = UtilsService.getPropByString(
                                item,
                                'gmd:CI_OnlineResource gmd:status gmd:score'
                            )
                            if (
                                !linkScore ||
                                linkScore > 100 ||
                                linkScore < 0
                            ) {
                                linkScore = -1
                            }
                            downloads.push({
                                name: 'Link',
                                url: directLink,
                                desc: '',
                                type: 'wcs',
                                boxTitle: '',
                                score: linkScore,
                            })
                        }
                    }

                    // Custom Download
                    const advancedLink = UtilsService.getPropByString(
                        item,
                        'gmd:CI_OnlineResource.gmd:advancedAccessLink'
                    )
                    if (advancedLink) {
                        if (protocol.indexOf('sentinel') > -1) {
                            // Custom Download for Sentinel collection
                            const sentinelCustomDownloadSuffix =
                                '?service=WPS&request=execute&identifier=gi-axe-transform&storeexecuteresponse=true&DataInputs=outputFormat%3DIMAGE_PNG%3BoutputSize%3D256%252C256'
                            downloads.push({
                                name: linkTitle,
                                url:
                                    advancedLink + sentinelCustomDownloadSuffix,
                                desc: '',
                                type: 'Advanced access link',
                                boxTitle: '',
                                score: 0,
                            })
                        } else {
                            downloads.push({
                                name: 'Custom download',
                                url: advancedLink,
                                desc: '',
                                type: 'custom-download',
                                boxTitle: '',
                                score: 0,
                            })
                        }
                    }
                }

                for (const download of downloads) {
                    const downloadFileSameFormatIndex: any =
                        resultDownloads.findIndex(
                            (file) => file.type === download.type
                        )

                    const { scoreText, scoreClass } =
                        this.getDownloadLinkStatus(download.score)

                    download.scoreText = scoreText
                    download.scoreClass = scoreClass

                    if (
                        downloadFileSameFormatIndex !== -1 &&
                        !resultDownloads[downloadFileSameFormatIndex].links
                    ) {
                        const file: any = resultDownloads.splice(
                            downloadFileSameFormatIndex,
                            1
                        )[0]
                        resultDownloads.push({
                            type: download.type,
                            links: [download, file],
                        })
                    } else if (downloadFileSameFormatIndex !== -1) {
                        const downloadFileSameFormat =
                            resultDownloads[downloadFileSameFormatIndex]
                        downloadFileSameFormat.links.push(download)
                    } else {
                        resultDownloads.push(download)
                    }
                }
            }
            result.downloads = resultDownloads
        }
    }

    public getDownloadLinkStatus(score: any) {
        let scoreText
        let scoreClass

        if (score >= 0 && score < 20) {
            scoreText = `${this.$tc(
                'fileDownloadsPopup.veryUnreliable'
            )} (${this.$tc('fileDownloadsPopup.score')} ${score}%`
            scoreClass = 'av-lowest'
        } else if (score >= 20 && score < 40) {
            scoreText = `${this.$tc(
                'fileDownloadsPopup.frequentlyUnavailable'
            )} (${this.$tc('fileDownloadsPopup.score')} ${score}%`
            scoreClass = 'av-low'
        } else if (score >= 40 && score < 60) {
            scoreText = `${this.$tc(
                'fileDownloadsPopup.sometimesUnavailable'
            )} (${this.$tc('fileDownloadsPopup.score')} ${score}%`
            scoreClass = 'av-med'
        } else if (score >= 60 && score < 80) {
            scoreText = `${this.$tc(
                'fileDownloadsPopup.mostlyAvailable'
            )} (${this.$tc('fileDownloadsPopup.score')} ${score}%`
            scoreClass = 'av-high'
        } else if (score >= 80 && score <= 100) {
            scoreText = `${this.$tc(
                'fileDownloadsPopup.veryReliable'
            )} (${this.$tc('fileDownloadsPopup.score')} ${score}%`
            scoreClass = 'av-highest'
        } else {
            scoreText = `${this.$tc('fileDownloadsPopup.noInfo')}`
            scoreClass = 'av-no-info'
        }
        return {
            scoreText,
            scoreClass,
        }
    }

    public openDownloadLinksPopup(result: any, links: any) {
        const props = {
            result,
            links,
        }

        this.$store
            .dispatch(PopupActions.openPopup, {
                contentId: 'download-links',
                title: this.$tc('popupTitles.downloadLinks'),
                component: DabResultDownloads,
                props,
            })
            .then((sentinelUrl: string) => {
                if (sentinelUrl) {
                    this.openSentinelLoginPopup(sentinelUrl, result)
                }
            })

        const actionAfterDownloadPopupShow =
            this.$store.getters[SearchGetters.actionAfterDownloadPopupShow]
        if (actionAfterDownloadPopupShow) {
            actionAfterDownloadPopupShow()
        }
    }

    public openSentinelLoginPopup(url: string, result: any) {
        if (
            !Liferay.ThemeDisplay.isSignedIn() ||
            !this.$store.getters[MyWorkspaceGetters.settings].dhusUsername
        ) {
            const props = {
                url,
                result,
            }

            this.$store.dispatch(PopupActions.openPopup, {
                contentId: 'sentinel-login',
                title: this.$tc('popupTitles.sentinelDataAccess'),
                component: SentinelLogin,
                props,
            })
        } else {
            window.open(SearchEngineService.getDhusProxyUrl(url))
        }
    }

    public async getUnepFile(params: any, result: any) {
        const [, data] = await to(
            GeossSearchApiService.getUnepFileUrl(params.series)
        )
        if (data) {
            UtilsService.createAndOpenFile(
                data,
                `SDG-${params.indicator}-${params.series[0]}.csv`,
                'text/csv'
            )
        }

        LogService.logElementClick(
            null,
            null,
            result.id,
            null,
            'Direct download',
            null,
            result.contributor.orgName,
            result.name
        )
    }

    public downloadLinkClicked(result: any) {
        const dataSource = this.getResultsDataSource(result)
        const id = result.id
        let orgName = ''
        let title = ''
        if (dataSource === 'zenodo') {
            const orgNameArray = result.metadata.creators
            orgName = orgNameArray.join(', ')
            title = result.metadata.title
        } else if (dataSource === 'nextgeoss') {
            orgName = result['dc:publisher']
        } else {
            orgName = result.contributor.orgName
            title = result.name
        }

        LogService.logElementClick(
            null,
            null,
            id,
            null,
            'Direct download',
            null,
            orgName,
            title
        )
        MouseLeaveService.initSurvey()
    }

    public async initCustomDownloadPopup(result: any, baseUrl: string) {
        if (baseUrl.startsWith('http:')) {
            baseUrl = baseUrl.replace('http:', 'https:')
        }
        let customDownloadOptions = null
        if (!customDownloadOptions) {
            if (baseUrl.includes('service=WCS&version=2.0.1')) {
                const [err, data] = await to(
                    GeossSearchApiService.getCustomDownloadOptionsWCS(baseUrl)
                )
                if (err) {
                    const props = {
                        title: this.$tc('general.error'),
                        subtitle: err,
                    }
                    return this.$store.dispatch(PopupActions.openPopup, {
                        contentId: 'error',
                        component: ErrorPopup,
                        props,
                    })
                }

                let formatOptions = UtilsService.getArrayByString(
                    data.getCapabilities,
                    'wcs:Capabilities.wcs:ServiceMetadata.wcs:formatSupported'
                )
                formatOptions = formatOptions.map((option) => ({
                    id: option,
                    text: option,
                }))

                const nativeFormat = UtilsService.getPropByString(
                    data.describeCoverage,
                    'wcs:CoverageDescriptions.wcs:CoverageDescription.wcs:ServiceParameters.wcs:nativeFormat'
                )

                let rangeSubset = UtilsService.getArrayByString(
                    data.describeCoverage,
                    'wcs:CoverageDescriptions.wcs:CoverageDescription.gmlcov:rangeType.swe:DataRecord.swe:field.swe:Quantity.swe:description'
                )
                rangeSubset = rangeSubset.map((option) => ({
                    id: option,
                    text: option,
                }))

                const outputCRS = []
                const outputCRSRaw = UtilsService.getArrayByString(
                    data.getCapabilities,
                    'wcs:Capabilities.wcs:ServiceMetadata.wcs:Extension.wcscrs:crsSupported'
                )
                for (const option of outputCRSRaw) {
                    const arrayCRS = option.split('/')
                    const nameCRS = `${arrayCRS[arrayCRS.length - 3]}:${
                        arrayCRS[arrayCRS.length - 1]
                    }`
                    outputCRS.push({ id: option, text: nameCRS })
                }

                const lowerCorner = UtilsService.getPropByString(
                    data.describeCoverage,
                    'wcs:CoverageDescriptions.wcs:CoverageDescription.gml:boundedBy.gml:Envelope.gml:lowerCorner'
                )

                const upperCorner = UtilsService.getPropByString(
                    data.describeCoverage,
                    'wcs:CoverageDescriptions.wcs:CoverageDescription.gml:boundedBy.gml:Envelope.gml:upperCorner'
                )

                customDownloadOptions = {
                    formatOptions,
                    nativeFormat,
                    scaleFactor: 0.5,
                    rangeSubset,
                    outputCRS,
                    lowerCorner,
                    upperCorner,
                    baseUrl,
                }
            } else {
                const [err, data] = await to(
                    GeossSearchApiService.getCustomDownloadOptions(baseUrl)
                )
                if (err) {
                    const props = {
                        title: this.$tc('general.error'),
                        subtitle: err,
                    }
                    return this.$store.dispatch(PopupActions.openPopup, {
                        contentId: 'error',
                        component: ErrorPopup,
                        props,
                    })
                }
                let formatOptions = UtilsService.getArrayByString(
                    data,
                    'ns2:format'
                )
                formatOptions = formatOptions.map((option) => ({
                    id: option,
                    text: option,
                }))

                const options = UtilsService.getArrayByString(
                    data,
                    'ns2:spatialGrid'
                )

                const outputSize = []
                const subsetCRSOptions = [{ id: null, text: 'Default subset' }]
                const subsetLowerCoordinates = []
                const subsetUpperCoordinates = []
                const crs = []

                for (const item of options) {
                    let outputSizeVal: any | string[] =
                        UtilsService.getArrayByString(
                            item,
                            'spatialAxis.numberOfPoints'
                        )
                    outputSizeVal = outputSizeVal.join(',')
                    outputSize.push(outputSizeVal)

                    const subsetCRSOption = UtilsService.getPropByString(
                        item,
                        'crs.identifier'
                    )
                    subsetCRSOptions.push({
                        id: subsetCRSOption,
                        text: subsetCRSOption,
                    })

                    let subsetLowerCoordinatesVal: string | number[] =
                        UtilsService.getArrayByString(
                            item,
                            'totalExtent.lowerCorner.coordinates'
                        )
                    let subsetUpperCoordinatesVal: string | number[] =
                        UtilsService.getArrayByString(
                            item,
                            'totalExtent.upperCorner.coordinates'
                        )
                    subsetLowerCoordinatesVal = subsetLowerCoordinatesVal
                        .map((item) => Math.round(10 * item) / 10)
                        .join(',')
                    subsetUpperCoordinatesVal = subsetUpperCoordinatesVal
                        .map((item) => Math.round(10 * item) / 10)
                        .join(',')
                    subsetLowerCoordinates.push(subsetLowerCoordinatesVal)
                    subsetUpperCoordinates.push(subsetUpperCoordinatesVal)
                }

                customDownloadOptions = {
                    formatOptions,
                    subsetCRSOptions,
                    outputSize,
                    subsetLowerCoordinates,
                    subsetUpperCoordinates,
                    crs: subsetCRSOptions[1].id,
                    baseUrl,
                }
            }

            Object.freeze(customDownloadOptions)
        }

        const props = {
            options: customDownloadOptions,
            resultId: result.id,
            resultOrgName: result.contributor.orgName,
            resultTitle: result.name,
            bookmarksMode: true,
        }

        if (baseUrl.includes('service=WCS&version=2.0.1')) {
            this.$store.dispatch(PopupActions.openPopup, {
                contentId: 'custom-download',
                title: this.$tc('popupTitles.customizeDownload'),
                component: CustomDownloadWCS,
                props,
            })
        } else {
            this.$store.dispatch(PopupActions.openPopup, {
                contentId: 'custom-download',
                title: this.$tc('popupTitles.customizeDownload'),
                component: DabResultCustomDownload,
                props,
            })
        }
    }

    public async showDetails(result: any) {
        const dataSource = this.getResultsDataSource(result)
        const title =
            dataSource === 'zenodo'
                ? UtilsService.getPropByString(result, 'metadata.title')
                : result.name
        let metadata = null

        LogService.logElementClick(
            null,
            null,
            result.id,
            null,
            'Show details',
            null,
            null,
            title
        )
        // MouseLeaveService.initSurvey()

        let isSatellite = false

        if (!metadata) {
            if (dataSource === DataSources.ZENODO) {
                isSatellite = false
                metadata = {
                    title: this.$tc('popupTitles.resourceDetails'),
                    data: {
                        ...result,
                        acquisition: {
                            platform: 'ZENODO',
                        },
                    },
                }
            } else if (dataSource === DataSources.WIKIPEDIA) {
                isSatellite = false
                metadata = {
                    title: this.$tc('popupTitles.resourceDetails'),
                    data: {
                        ...result,
                        acquisition: {
                            platform: 'WIKIPEDIA',
                        },
                    },
                }
            } else if (dataSource === DataSources.NEXTGEOSS) {
                isSatellite = false
                metadata = {
                    title: this.$tc('popupTitles.resourceDetails'),
                    data: {
                        ...result,
                        acquisition: {
                            platform: 'NEXTGEOSS',
                        },
                    },
                }
            } else if (
                result.acquisition &&
                Object.keys(result.acquisition.platform).length
            ) {
                if (result.acquisition.platform === 'GEOSS_CR') {
                    isSatellite = false
                } else {
                    isSatellite = true
                }
                metadata = {
                    title: this.$tc('popupTitles.resourceDetails'),
                    data: {
                        ...result,
                    },
                }
            } else {
                const [, data] = await to(
                    GeossSearchApiService.getDabResultMetadata(result.targetId)
                )
                if (data) {
                    metadata = {
                        title: `<div class="d-flex flex--justify-between flex--align-center padding-right-30">
									<span>${this.$tc('popupTitles.resourceDetails')}</span>
									<a class="link-white" target="_blank" href="${SearchEngineService.getMetaDataUrl(
                                        result.targetId
                                    )}">${this.$tc(
                            'popupTitles.rawMetadata'
                        )}</a>
								</div>`,
                        data,
                    }
                }
            }
            Object.freeze(metadata)
        }
        if (metadata) {
            const comments = await this.getComments(result.targetId)
            const props = {
                data: metadata.data,
                isSatellite,
                resultTitle: title,
                resultImage: typeof result.logo === 'string' ? result.logo : '',
                popupTitle: metadata.title,
                comments,
            }
            this.$store.dispatch(PopupActions.openPopup, {
                contentId: 'metadata-bookmarks',
                title: metadata.title,
                component: DabResultMetadata,
                props,
            })
            setTimeout(() => {
                console.log(this.$store.getters[PopupGetters.queue])
            }, 2)
        }
    }

    public async getComments(id: string) {
        const res = await RatingService.fetchComments(id)
        if (res) {
            return res
        } else {
            return 0
        }
    }

    public async mounted() {
        SpinnerService.showSpinner()
        await this.$store.dispatch(BookmarksActions.getResults)
        SpinnerService.hideSpinner()
    }

    @Watch('results')
    public onResultsChange() {
        // this.initResultDownloads(this.results)
        this.buildMaps()
    }

    @Watch('pageOffset')
    public clearMaps() {
        this.mapsObject = {}
    }
}
</script>

<style lang="scss" scoped>
.bookmark-results {
    display: flex !important;
    flex-wrap: wrap;
    justify-content: space-between;
    align-content: flex-start;

    &__wrapper {
        height: 100%;
    }

    &__empty {
        display: flex;
        justify-content: space-around;
        align-items: center;
        text-align: center;
        background: $white-transparent;
        min-height: 75vh;
        margin-top: 5px;

        img {
            opacity: 0.5;
            display: block;
            margin: -50px auto 20px;
            max-width: 60%;
        }

        span {
            font-size: 1.5em;
            color: $grey-darker;
        }
    }
}

.bookmark-result {
    width: 100%;
    margin-bottom: 1px;
    transition: transform 0.3s ease;
    transform: translateX(0);

    &__checkbox {
        padding: 15px 25px;
        width: 70px;

        @media (max-width: $breakpoint-md) {
            padding-right: 15px;
            width: 60px;
        }

        @media (max-width: $breakpoint-sm) {
            padding-top: 11px;
        }

        input {
            display: none;

            &:checked + label:before {
                content: '✖';
            }
        }

        label {
            color: $green;
            position: relative;
            cursor: pointer;
            font-size: 1.2em;
            line-height: 1.5em;
            transition: 0.1s ease all;

            &:before {
                content: '';
                display: inline-block;
                width: 13px;
                height: 13px;
                border: 1px solid $green;
                vertical-align: middle;
                margin-bottom: 4px;
                font-size: 16px;
                line-height: 13px;
            }
        }
    }

    &:last-child {
        margin-bottom: 0;
    }

    &__wrapper {
        background-color: rgba(255, 255, 255, 0.9);
        transition: background-color 0.2s ease-in-out;
        display: flex;
        position: relative;

        &:hover,
        &.hover {
            background-color: white;
            transition: background-color 0s ease-in-out;

            .bookmark-result__tools {
                background: -moz-linear-gradient(
                    top,
                    rgba(255, 255, 255, 0.25) 0%,
                    rgba(255, 255, 255, 1) 12%
                ); /* FF3.6-15 */
                background: -webkit-linear-gradient(
                    top,
                    rgba(255, 255, 255, 0.25) 0%,
                    rgba(255, 255, 255, 1) 12%
                ); /* Chrome10-25,Safari5.1-6 */
                background: linear-gradient(
                    to bottom,
                    rgba(255, 255, 255, 0.25) 0%,
                    rgba(255, 255, 255, 1) 12%
                ); /* W3C, IE10+, FF16+, Chrome26+, Opera12+, Safari7+ */
                filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#40ffffff', endColorstr='#ffffff',GradientType=0 ); /* IE6-9 */
            }
        }
    }

    &__image {
        width: 200px;
        height: 200px;
        background: $grey-light;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        flex: 0 0 auto;

        @media (max-width: $breakpoint-md) {
            width: 150px;
        }

        @media (max-width: $breakpoint-sm) {
            display: none;
        }

        &--default {
            padding: 10px;
        }

        img {
            max-width: 100%;
            max-height: 100%;
        }
    }

    &__content {
        padding: 12px 25px;
        width: calc(100% - 625px);
        position: relative;

        @media (max-width: $breakpoint-xxl) {
            width: calc(100% - 300px);
        }

        @media (max-width: $breakpoint-md) {
            width: calc(100% - 240px);
        }
        @media (max-width: $breakpoint-sm) {
            width: calc(100% - 90px);
            padding-left: 0;
        }
    }

    &__tools {
        padding-top: 5px;

        @media (max-width: $breakpoint-sm) {
            left: 0;
        }

        .reviews {
            margin-bottom: 10px;
            display: flex;
            align-items: center;
            font-size: 0.85em;
            margin-top: 5px;

            > div {
                display: flex;
                align-items: center;
                margin-right: 15px;
                &:before {
                    width: 25px;
                    height: 25px;
                    background-size: contain !important;
                    display: inline-block;
                    content: '';
                    margin-right: 5px;
                    display: none;
                }
                i {
                    font-size: 1.4em;
                    color: $green;
                    margin-right: 5px;
                }
            }

            .recent_views {
                &:before {
                    background: url('/img/review.svg') center center no-repeat;
                }
                span {
                    &:nth-child(2) {
                        margin-right: 3px;
                    }
                }

                @media (max-width: $breakpoint-md) {
                    span {
                        &:last-child {
                            display: none;
                        }
                    }
                }
            }
            .comments {
                cursor: pointer;

                &:before {
                    background: url('/img/rating-comment.svg') center center
                        no-repeat;
                }
                span {
                    margin-left: 3px;
                }
                i {
                    font-style: normal;
                    font-size: 1em;
                    color: $grey;
                    margin-right: 3px;

                    &.active,
                    &.hover {
                        color: $yellow;
                    }
                }
            }
            .persons {
                display: flex;
                align-items: center;

                @media (max-width: $breakpoint-xxs) {
                    display: none;
                }

                &:before {
                    background: url('/img/person.svg') center center no-repeat;
                    width: 16px;
                    height: 16px;
                    margin-bottom: 3px;
                    display: block;
                }
            }
        }
        .actions {
            align-items: center;
            display: flex;
            flex: 0 0 auto;
            flex-wrap: wrap;
            justify-content: space-between;
            margin-top: 10px;

            & > div {
                display: flex;
            }

            &--side {
                margin-bottom: 5px;
                position: relative;
            }

            .toggle-wrapper {
                width: 100%;

                > div {
                    width: 100%;

                    > div {
                        border-top: 1px solid #ddd;
                        display: flex;
                        justify-content: flex-end;
                        margin-top: 10px;
                        padding: 10px 0 0;
                        width: 100%;
                        height: 46px;

                        a {
                            margin-right: 0;
                            margin-left: 10px;
                        }
                    }
                }
            }

            a,
            button {
                cursor: pointer;
                background: $yellow;
                border: 1px solid $yellow;
                color: white;
                width: 32px;
                height: 32px;
                text-align: center;
                line-height: 34px;
                border-radius: 50%;
                overflow: hidden;
                margin-right: 10px;
                font-size: 1.2em;
                text-indent: -1px;
                text-decoration: none;
                display: inline-block;
                transition: all 0.25s ease-in-out;

                &:hover {
                    background: white;
                    border: 1px solid $yellow;
                    color: $yellow;
                    transition: all 0.25s ease-in-out;
                }

                &.show-on-map,
                &.drill-down {
                    background: $red;
                    border: 1px solid $red;

                    &:hover {
                        background: white;
                        border: 1px solid $red;
                        color: $red;
                    }
                }

                &.show-on-map {
                    font-size: 1.1em;
                }

                &.drill-down {
                    text-indent: 2px;
                    i:before {
                        font-weight: bold;
                    }
                }

                &.toggle-downloads {
                    margin-right: 0;
                }

                &[disabled] {
                    background-color: $grey !important;
                    border-color: $grey !important;
                    color: white !important;
                    pointer-events: none;
                }
            }
        }
    }

    &__text-data {
        flex: 1 1 auto;
        overflow: hidden;

        &--title {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        &--datasource {
            width: 110px;
            height: 35px;
            background-size: contain;
            background-repeat: no-repeat;
            background-position: right top;
            margin-bottom: 5px;
            margin-left: 5px;

            @media (max-width: $breakpoint-sm) {
                width: 75px;
                height: 25px;
            }

            @media (max-width: $breakpoint-xs) {
                display: none;
            }

            &[data-source='dab'] {
                background-image: url('/img/logo_dab.png');
            }

            &[data-source='geoss_cr'] {
                background-image: url('/img/logo_geoss_cr.png');
            }

            &[data-source='amerigeoss_ckan'] {
                background-image: url('/img/logo_amerigeoss_ckan.png');
            }

            &[data-source='next_geoss'] {
                background-image: url('/img/logo_geoss_cr.svg');
            }

            &[data-source='zenodo'] {
                background-image: url('/img/logo_zenodo.png');
            }

            &[data-source='wikipedia'] {
                background-image: url('/img/logo_wikipedia.png');
            }
        }
    }

    &__title {
        color: $blue;
        line-height: 1.5em;
        font-size: 1.2em;
        font-weight: bold;
        max-width: calc(100% - 110px);
        cursor: pointer;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
        overflow: hidden;
        @media (max-width: $breakpoint-md) {
            -webkit-line-clamp: 1 !important;
        }
    }

    &__contributor {
        font-size: 12px;
        margin-bottom: 5px;
        width: auto;
        display: block;
        color: #777;
    }

    &__summary {
        font-size: 13px;
        line-height: 15px;
        height: 30px;
    }

    &__map {
        width: 356px;
        height: auto;
        box-shadow: 0 0 50px rgba(0, 0, 0, 0.2) inset;
        background-color: white;

        @media (max-width: $breakpoint-xxl) {
            transform: translateX(356px);
        }

        @media (max-width: $breakpoint-xs) {
            width: 280px;
            transform: translateX(280px);
        }
    }

    .bookmark-result-details {
        &__more {
            color: #0661a9;
            font-weight: 700;
            margin-top: 5px;
            margin-bottom: 5px;
            display: -webkit-box;
            display: -ms-flexbox;
            display: flex;
            -webkit-box-align: center;
            -ms-flex-align: center;
            align-items: center;

            .arrow {
                position: relative;
                width: 15px;
                height: 15px;
                border: 1px solid #0661a9;
                border-radius: 50%;
                display: inline-block;
                margin-left: 3px;
                &:before,
                &::after {
                    content: '';
                    width: 5px;
                    height: 2px;
                    background: #0661a9;
                    position: absolute;
                    left: 4px;
                    top: 4px;
                    -webkit-transform: rotate(45deg);
                    transform: rotate(45deg);
                }

                &:after {
                    top: 7px;
                    -webkit-transform: rotate(-45deg);
                    transform: rotate(-45deg);
                }
            }
        }
    }

    .toggle-map {
        display: none;
        height: auto;
        width: 30px;
        line-height: 200px;
        font-size: 3em;
        color: $green;
        margin-right: -356px;
        text-align: center;
        box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        transition: background-color 0.2s ease-in-out;
        background: white;
        cursor: pointer;

        @media (max-width: $breakpoint-xs) {
            margin-right: -280px;
        }

        &:hover {
            color: $green-dark;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
            background: #f3f3f3;
            transition: background-color 0.1s ease-in-out;
        }

        i {
            font-style: normal;

            &:before {
                content: '‹';
            }
        }

        @media (max-width: $breakpoint-xxl) {
            display: block;
        }
    }

    &.open-map {
        transform: translateX(-356px);
        transition: transform 0.3s ease;

        @media (max-width: $breakpoint-xs) {
            transform: translateX(-280px);
        }

        .toggle-map {
            i:before {
                content: '›';
            }
        }
    }
}
</style>

<style lang="scss">
.bookmark-result {
    .result-details {
        width: 100%;
    }
}
</style>
