<template>
    <div
        class="dab-result-details__outer-wrapper"
        :class="{
            'is-parent-ref': isParentRef && separated,
            'extended-view-mode': extendedViewMode
        }"
    >
        <div
            class="dab-result-details__is-parent-ref-trigger"
            v-if="isParentRef && separated"
        >
            <button
                @click="popParentRefResult()"
                data-tutorial-tag="result-back"
            ></button>
            <i :class="`icomoon-data-source--${parentDataSourceGroup}`"></i>
            <span class="title" @click="showDabResultParentRefDetails()">{{
                title
            }}</span>
        </div>
        <div
            class="dab-result-details"
            :data-tutorial-tag="
                resultIdDetails === result.id ? 'result-details' : ''
            "
            :class="{
                'is-parent-ref': isParentRef && separated,
                'odd': index % 2 && typeof index !== 'undefined'
            }"
            :data-id="result.id"
            v-show="resultIdDetails === result.id || extendedViewMode"
        >
            <div class="dab-result-details__wrapper">
                <div>
                    <div
                        v-if="isParentRef && separated"
                        class="dab-result-details__image"
                        :class="{
                            'dab-result-details__image--default':
                                getImage(result.logo) !== result.logo
                        }"
                    >
                        <img
                            :src="getImage(result.logo)"
                            @error="imageLoadError(result.logo)"
                            :alt="title"
                            v-image-preview
                        />
                    </div>
                    <div
                        class="dab-result-details__text-actions"
                        :class="{
                            'set-parent-ref-available':
                                addParentRefAvailable(result)
                        }"
                    >
                        <div class="d-flex flex--column flex--1">
                            <div class="d-flex flex--column flex--1">
                                <div
                                    class="d-flex flex--wrap flex--justify-between flex--align-start flex--no-shrink"
                                >
                                    <div class="dab-result-details__text">
                                        <div
                                            v-if="title"
                                            class="dab-result-details__title"
                                        >
                                            {{ title }}
                                        </div>
                                        <div
                                            v-if="
                                                result.contributor &&
                                                result.contributor.orgName
                                            "
                                            class="dab-result__contributor"
                                            v-line-clamp:20="1"
                                        >
                                            {{ $tc('dabResult.organisation') }}:
                                            {{ result.contributor.orgName }}
                                        </div>
                                        <div
                                            v-if="contributors"
                                            class="dab-result-details__contributor"
                                        >
                                            (<span
                                                v-if="dataSource !== 'zenodo'"
                                                >{{
                                                    $tc(
                                                        'dabResult.organisation'
                                                    )
                                                }}</span
                                            ><span v-else>{{
                                                $tc('dabResult.creators')
                                            }}</span
                                            >: {{ contributors }})
                                        </div>
                                    </div>
                                    <ViewsAndRatings :result="result" :currentId="resultIdDetails"/>
                                </div>
                                <div
                                    v-if="confidence && confidence.length"
                                    class="confidence"
                                >
                                    <div class="confidence__label">
                                        {{ $tc('popupContent.confidence') }}:
                                    </div>
                                    <div class="confidence__box">
                                        <div class="confidence__number">
                                            {{ confidence[0] }}
                                        </div>
                                        <div class="confidence__type">
                                            {{ $tc('popupContent.crop') }}
                                        </div>
                                    </div>
                                    <div class="confidence__box">
                                        <div class="confidence__number">
                                            {{ confidence[1] }}
                                        </div>
                                        <div class="confidence__type">
                                            {{ $tc('popupContent.irrigation') }}
                                        </div>
                                    </div>
                                    <div class="confidence__box">
                                        <div class="confidence__number">
                                            {{ confidence[2] }}
                                        </div>
                                        <div class="confidence__type">
                                            {{ $tc('popupContent.landCover') }}
                                        </div>
                                    </div>
                                </div>
                                <div
                                    v-if="
                                        summary && typeof summary === 'string'
                                    "
                                    class="dab-result-details__summary"
                                >
                                    <div
                                        v-if="isParentRef && separated"
                                        v-line-clamp:20="2"
                                        v-html-to-text="summary"
                                    ></div>
                                    <div v-else v-html-to-text="summary"></div>
                                </div>
                                <div class="dab-result-details__more__wrapper">
                                    <button @click="showDetails()" class="dab-result-details__more" :data-tutorial-tag="resultIdDetails === result.id
                                        ? 'result-see-more'
                                        : ''
                                        ">
                                        <span>{{
                                            $tc('dabResult.seeMore')
                                        }}</span>
                                        <!-- <span class="arrow"></span> -->
                                    </button>
                                </div>
                            </div>
                            <div class="dab-result-details__actions">
                                <div class="dab-result-details__actions--main">
                                    <button
                                        :title="
                                            $tc('dabResult.exploreExtendedView')
                                        "
                                        @click="toggleExtendedView()"
                                        v-if="
                                            isExtendedViewEnabled &&
                                            !isWidget &&
                                            dataSource !== DataSources.WIKIPEDIA &&
                                            !workflowDispatched &&
                                            !hidePocFeatures
                                        "
                                        class="extended-view-switcher"
                                        :class="{
                                            return: isExtendedViewActive
                                        }" :data-tutorial-tag="resultIdDetails === result.id
                                            ? 'result-extended-view'
                                            : ''
                                            ">
                                        <i v-if="!isExtendedViewActive" class="icomoon-expand-view"></i>
                                        <i v-else class="icomoon-collapse-view"></i>
                                    </button>
                                    <button :title="$tc('dabResult.showOnMap')" :disabled="!layerData"
                                        @click="showOnMap()" :data-tutorial-tag="resultIdDetails === result.id
                                            ? 'result-show-on-map'
                                            : ''
                                            ">
                                        <i class="icomoon-show-on-map"></i>
                                    </button>
                                    <button :title="$tc('dabResult.addAsBookmark')" v-if="!isWidget"
                                        v-show="isSignedIn && !resultBookmarked" :disabled="!isSignedIn"
                                        @click="addBookmark()" :data-tutorial-tag="resultIdDetails === result.id
                                            ? 'result-bookmark'
                                            : ''
                                            ">
                                        <i class="icomoon-plus"></i>
                                    </button>
                                    <button :title="$tc('dabResult.removeFromBookmarks')
                                        " v-if="!isWidget" v-show="isSignedIn && resultBookmarked"
                                        :disabled="!isSignedIn" @click="removeBookmark()" :data-tutorial-tag="resultIdDetails === result.id
                                            ? 'result-bookmark'
                                            : ''
                                            ">
                                        <i class="icomoon-minus"></i>
                                    </button>
                                </div>
                                <div class="dab-result-details__actions--side">
                                    <Share :data-tutorial-tag="resultIdDetails === result.id
                                        ? 'result-share'
                                        : ''
                                        " :url="shareUrl" />
                                    <button :title="$tc('dabResult.layers')" @click="layerButtonAction()" :class="{
                                        'single-layer-active':
                                            layers.length === 1 &&
                                            isLayerDisplayed(layers[0].url)
                                    }" :disabled="!layers.length && !statisticsId
                                        " :data-tutorial-tag="resultIdDetails === result.id
                                            ? 'result-layers'
                                            : ''
                                            ">
                                        <i class="icomoon-layers"></i>
                                    </button>
                                    <button
                                        v-if="downloads.length === 1"
                                        :title="
                                            $tc('sentinelLogin.download') +
                                            ' - ' +
                                            getDownloadButtonLabel(
                                                downloads[0].type
                                            )
                                        "
                                        @click="
                                            instantSingleDownload(downloads[0])
                                        "
                                        :class="{ open: showDownloads }"
                                        :data-tutorial-tag="
                                            resultIdDetails === result.id
                                                ? 'result-downloads'
                                                : ''
                                                ">
                                        <i class="icomoon-arrow down"></i>
                                    </button>
                                    <button v-else :title="$tc('dabResult.downloads')" @click="toggleShowDownloads()"
                                        :class="{ open: showDownloads }" :disabled="!downloads.length"
                                        :data-tutorial-tag="resultIdDetails === result.id
                                            ? 'result-downloads'
                                            : ''
                                            ">
                                        <i class="icomoon-arrow down"></i>
                                    </button>

                                    <button class="dab-result-details__actions-workflow" :title="buttonWorkflowTitle"
                                        @click="openWorkflow(true)" v-show="workflow || isEoWorkflow" :data-tutorial-tag="resultIdDetails === result.id
                                                ? 'result-workflow'
                                                : ''
                                        "
                                    ></button>
                                    <template
                                        v-for="hub of [
                                            'data',
                                            'information',
                                            'services'
                                        ]"
                                    >
                                        <button
                                            :key="`${hub}_1`"
                                            v-show="
                                                !hiddenDataSources.includes(
                                                    hub
                                                ) && dataSourceGroup !== hub
                                            "
                                            :title="$tc(`dabResult.${hub}`)"
                                            @click="
                                                switchToDataSource(hub, result)
                                                " :disabled="!isDrillAvailable(
                                                    result,
                                                    `${hub}_hub`
                                                )
                                                    " :data-tutorial-tag="resultIdDetails === result.id
                                                        ? `result-switch-to-${hub}`
                                                        : ''
                                                        ">
                                            <i :class="`icomoon-data-source--${hub}`"></i>
                                        </button>
                                        <button :key="`${hub}_2`" v-if="isEntryExtensionEnabled" v-show="!hiddenDataSources.includes(
                                            hub
                                        ) &&
                                            isUserContributedDrillAvailable(
                                                result,
                                                `${hub}_hub`
                                            )
                                            " :title="$tc(
                                                `dabResult.userContributed${hub}`
                                            )
                                                " @click="
                                                    switchToUserContributedDataSource(
                                                        hub,
                                                        result
                                                    )
                                                    " :data-tutorial-tag="resultIdDetails === result.id
                                                        ? `result-switch-to-contributed-${hub}`
                                                        : ''
                                                        ">
                                            <i :class="`icomoon-data-source--${hub}`"></i>
                                            <i class="icon-small icomoon-editor--user"></i>
                                        </button>
                                    </template>
                                </div>
                                <div
                                    class="dab-result-details__downloads"
                                    v-if="downloads.length"
                                >
                                    <CollapseTransition>
                                        <div
                                            v-show="showDownloads"
                                            class="dab-result-details__downloads-wrapper"
                                        >
                                            <div>
                                                <span
                                                    v-for="(
                                                        download, index
                                                    ) of downloads"
                                                    :key="index"
                                                >
                                                    <button
                                                        v-if="download.links"
                                                        @click="
                                                            openDownloadLinksPopup(
                                                                download.links
                                                            )
                                                        "
                                                        :title="
                                                            getDownloadButtonLabel(
                                                                download.type
                                                            )
                                                        "
                                                    >
                                                        <i
                                                            v-if="
                                                                FileFormatsIcons.indexOf(
                                                                    download.type
                                                                ) !== -1
                                                            "
                                                            :class="`dab-result-details__file-icon icomoon-doc-${download.type}`"
                                                        ></i>
                                                        <i
                                                            v-else
                                                            class="dab-result-details__file-icon icomoon-doc-file"
                                                        ></i>
                                                    </button>
                                                    <button v-else-if="
                                                        download.url.indexOf(
                                                            '/dhus/odata/'
                                                        ) !== -1
                                                    " @click="
                                                        openSentinelLoginPopup(
                                                            download.url
                                                        )
                                                        " :title="getDownloadButtonLabel(
                                                            download.type
                                                        )
                                                            ">
                                                        <i v-if="
                                                            FileFormatsIcons.indexOf(
                                                                download.type
                                                            )
                                                        "
                                                    ></i>
                                                        <i
                                                            v-if="
                                                                FileFormatsIcons.indexOf(
                                                                    download.type
                                                                ) !== -1
                                                            "
                                                            :class="`dab-result-details__file-icon icomoon-doc-${download.type}`"
                                                        ></i>
                                                        <i
                                                            v-else
                                                            class="dab-result-details__file-icon icomoon-doc-file"
                                                        ></i>
                                                    </button>
                                                    <button v-else-if="
                                                        download.url.indexOf(
                                                            '/sdg/Series/DataCSV'
                                                        ) !== -1
                                                    " @click="
                                                        getUnepFile(
                                                            download.postData
                                                        )
                                                        " :title="getDownloadButtonLabel(
                                                            download.type
                                                        )
                                                            ">
                                                        <i v-if="
                                                            FileFormatsIcons.indexOf(
                                                                download.type
                                                            )
                                                        "
                                                    ></i>
                                                        <i
                                                            v-if="
                                                                FileFormatsIcons.indexOf(
                                                                    download.type
                                                                ) !== -1
                                                            "
                                                            :class="`dab-result-details__file-icon icomoon-doc-${download.type}`"
                                                        ></i>
                                                        <i
                                                            v-else
                                                            class="dab-result-details__file-icon icomoon-doc-file"
                                                        ></i>
                                                    </button>
                                                    <button v-else-if="
                                                        download.type ===
                                                        'custom-download'
                                                    " @click="
                                                        initCustomDownloadPopup(
                                                            download.url
                                                        )
                                                        " :title="$tc(
                                                            'general.customDownload'
                                                        )
                                                            ">
                                                        <i
                                                            class="dab-result-details__file-icon icomoon-custom-download"
                                                        ></i>
                                                    </button>
                                                    <a v-else-if="
                                                        download.type ===
                                                        'html'
                                                    " @click="
                                                        downloadLinkClicked(
                                                            download.url
                                                        )
                                                        " target="_blank" :title="getDownloadButtonLabel(
                                                            download.type
                                                        )
                                                            ">
                                                        <i v-if="
                                                            FileFormatsIcons.indexOf(
                                                                download.type
                                                            )
                                                        "
                                                    ></i>
                                                        <i
                                                            v-if="
                                                                FileFormatsIcons.indexOf(
                                                                    download.type
                                                                ) !== -1
                                                            "
                                                            :class="`dab-result-details__file-icon icomoon-doc-${download.type}`"
                                                        ></i>
                                                        <i
                                                            v-else
                                                            class="dab-result-details__file-icon icomoon-doc-file"
                                                        ></i>
                                                    </a>
                                                    <a v-else-if="
                                                        !isBulkDownloadEnabled
                                                    " @click="
                                                        downloadLinkClicked(
                                                            download.url
                                                        )
                                                        " target="_blank" :title="getDownloadButtonLabel(
                                                            download.type
                                                        )
                                                            ">
                                                        <i v-if="
                                                            FileFormatsIcons.indexOf(
                                                                download.type
                                                            )
                                                        "
                                                    ></i>
                                                        <i
                                                            v-if="
                                                                FileFormatsIcons.indexOf(
                                                                    download.type
                                                                ) !== -1
                                                            "
                                                            :class="`dab-result-details__file-icon icomoon-doc-${download.type}`"
                                                        ></i>
                                                        <i
                                                            v-else
                                                            class="dab-result-details__file-icon icomoon-doc-file"
                                                        ></i>
                                                    </a>
                                                    <a v-else-if="
                                                        isBulkDownloadEnabled
                                                    " target="_blank" :title="getDownloadButtonLabel(
                                                        download.type
                                                    )
                                                        " class="expandable expandable-on-click" :class="{
                                                            expanded:
                                                                index ===
                                                                    expandedDownloadIndex ||
                                                                !showExpandableDownload()
                                                        }">
                                                        <i v-if="
                                                            FileFormatsIcons.indexOf(
                                                                download.type
                                                            ) !== -1
                                                        " :class="`dab-result-details__file-icon icomoon-doc-${download.type}`"
                                                            @click="
                                                                setExpandedDownloadIndex(
                                                                    index
                                                                )
                                                            "
                                                        ></i>
                                                        <i
                                                            v-else
                                                            class="dab-result-details__file-icon icomoon-doc-file"
                                                            @click="
                                                                setExpandedDownloadIndex(
                                                                    index
                                                                )
                                                                "></i>
                                                        <p @click="
                                                            downloadLinkClicked(
                                                                download.url,
                                                                download.type
                                                            )
                                                            " class="dab-result-details__direct-download-button"
                                                            :title="$tc(
                                                                'dabResult.downloadNow'
                                                            )
                                                                ">
                                                            <span class="icomoon-arrow arrow-circled"></span>
                                                            Download now
                                                        </p>
                                                        <p class="dab-result-details__bulk-download-button" :title="isSignedIn
                                                            ? $tc(
                                                                'dabResult.downloadLater'
                                                            )
                                                            : $tc(
                                                                'dabResult.thisOptionAvailableForSignedIn'
                                                            )
                                                            " :class="{
                                                                disabled:
                                                                    !isSignedIn
                                                            }"
                                                            @click="
                                                                addToDownloadsList(
                                                                    download.url,
                                                                    download.type
                                                                )
                                                            "
                                                        >
                                                            <i
                                                                class="bulk-download__icon"
                                                            ></i
                                                            >{{
                                                                $tc(
                                                                    'customDownloadOptionsPopup.addToDownloads'
                                                                )
                                                            }}
                                                        </p>
                                                    </a>
                                                </span>
                                            </div>
                                        </div>
                                    </CollapseTransition>
                                </div>
                            </div>
                        </div>
                        <button :data-tutorial-tag="resultIdDetails === result.id
                            ? 'result-drill-down'
                            : ''
                            " :title="$tc('dabResult.showInsideFolder')" class="dab-result-details__drill down"
                            v-if="addParentRefAvailable(result) && !isParentRef"
                            @click="showInsideFolder(result)"
                        ></button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
// @ts-nocheck
import { Component, Prop, Vue, Watch } from 'nuxt-property-decorator'

import DabResultMetadata from '@/components/Search/Results/DabResultMetadata.vue'
import DabResultLayers from '@/components/Search/Results/DabResultLayers.vue'
import DabResultCustomDownload from '@/components/Search/Results/DabResultCustomDownload.vue'
import CustomDownloadWCS from '@/components/Search/Results/CustomDownloadWCS.vue'
import UnitedNationsStatistics from '@/components/Search/Results/UnitedNationsStatistics.vue'
import SentinelLogin from '@/components/Search/SentinelLogin.vue'
import DabResultDownloads from '@/components/Search/Results/DabResultDownloads.vue'
import ServiceWorkflow from '@/components/Search/Results/ServiceWorkflow.vue'
import ErrorPopup from '@/components/ErrorPopup.vue'
import GeneralPopup from '@/components/GeneralPopup.vue'

import { SearchActions } from '@/store/search/search-actions'
import { SearchGetters } from '@/store/search/search-getters'
import { MapActions } from '@/store/map/map-actions'
import GeossSearchApiService from '@/services/geoss-search.api.service'
import { PopupActions } from '@/store/popup/popup-actions'
import UtilsService from '@/services/utils.service'
import { MapGetters } from '@/store/map/map-getters'
import { UserGetters } from '@/store/user/user-getters'
import { FileFormatsIcons } from '@/data/file-formats-icons'
import LogService from '@/services/log.service'
import MouseLeaveService from '@/services/mouse-leave.service'
import { LayerData } from '@/interfaces/LayerData'
import SearchEngineService from '@/services/search-engine.service'
import { GeneralGetters } from '@/store/general/general-getters'
import { UserActions } from '@/store/user/user-actions'
import { FacetedFiltersActions } from '@/store/facetedFilters/faceted-filters-actions'
import {
    DataSource,
    DataSources,
    DataSourceGroup,
    DataOrigin
} from '@/interfaces/DataSources'
import to from '@/utils/to'
import { MyWorkspaceGetters } from '@/store/myWorkspace/my-workspace-getters'
import { ParentRef } from '@/interfaces/ParentRef'
import LayersUtils from '@/services/map/layer-utils'
import { BulkDownloadLink } from '@/interfaces/BulkDownloadLink'
import PopupCloseService from '@/services/popup-close.service'
import { ExtendedViewGetters } from '@/store/extendedView/extended-view-getters'
import { ExtendedViewActions } from '@/store/extendedView/extended-view-actions'
import { BulkDownloadActions } from '@/store/bulkDownload/bulk-download-actions'
import ViewsAndRatings from '@/components/ViewsAndRatings.vue'
import TutorialTagsService from '@/services/tutorial-tags.service'
import DashboardService from '@/services/dashboard.service'
import NotificationService from '@/services/notification.service'
import BulkDownloadPopup from '@/components/BulkDownloadPopup.vue'
import CollapseTransition from '@/plugins/CollapseTransition'
import { InSituFiltersGetters } from '~/store/inSituFilters/inSitu-filters.getters'
import { GeneralFiltersGetters } from '@/store/generalFilters/general-filters-getters'
import date from '@/filters/date'
import { UserGetters } from '@/store/user/user-getters'
import { UserActions } from '@/store/user/user-actions'
import { OidcProvider, OpenEO } from '@openeo/js-client'
import OpenEOWorkflowComponent from './OpenEOWorkflow.vue'
import OpenEOService from '@/services/openeo.service'
import CollapseTransition from '@/plugins/CollapseTransition'
import RatingService from '~/services/ratings.service'
import BookmarksService from '~/services/bookmarks.service'
import { parseXMLToJSON } from '@/services/general.api.service'

@Component({
    components: {
        ViewsAndRatings,
        CollapseTransition,
    },
})
export default class SearchResultDabDetailsComponent extends Vue {
    [x: string]: any
    @Prop({ default: null, type: Object }) public result!: any
    @Prop({ default: false, type: Boolean }) public separated!: boolean
    @Prop({ default: false, type: Boolean }) public extendedViewMode!: boolean
    @Prop(Number) public index!: number
    @Prop(String) public image!: string
    @Prop(String) public currentOpenId!: string

    public layers = []
    public downloads = []
    public workflow = null
    public statisticsId = null
    public timeSeriesArray = []
    public metadata = null
    public shareUrl = ''
    public showDownloads = false
    public customDownloadOptions = null
    public showShare = false
    public expandedDownloadIndex = null
    public isTimeSeries = false

    public score = 0

    public FileFormatsIcons = FileFormatsIcons
    public DataSources = DataSources
    public logo = typeof this.result.logo === 'string' ? this.result.logo : ''
    public isEoWorkflow = this.result.id.toLowerCase().includes('worldcereal cropland')

    get dashboardContent() {
        return UtilsService.getPropByString(this.result, 'dashboard.content')
    }

    get hidePocFeatures() {
        return this.$config.hidePocFeatures
    }

    get title() {
        let data = null
        if (this.isZenodoType) {
            data = UtilsService.getPropByString(this.result, 'metadata.title')
        } else {
            data = UtilsService.getPropByString(this.result, 'title')
        }
        data = data ? data : '-'
        return data
    }

    get contributors() {
        const data = []
        if (
            this.result.contributor &&
            this.result.contributor.orgName &&
            this.isZenodoType
        ) {
            data.push(
                UtilsService.getPropByString(this.result, 'contributor.orgName')
            )
        } else if (
            this.result.metadata &&
            this.result.metadata.creators &&
            this.isZenodoType
        ) {
            for (const item of this.result.metadata.creators) {
                data.push(item.name)
            }
        }
        return data ? data.join(', ') : '-'
    }

    get views() {
        let data = null
        if (this.isZenodoType) {
            data = UtilsService.getPropByString(this.result, 'stats.views')
        } else {
            data = UtilsService.getPropByString(this.result, 'views')
        }
        data = data ? data : '0'
        return data
    }

    get summary() {
        let data = null
        if (this.isZenodoType) {
            data = UtilsService.getPropByString(
                this.result,
                'metadata.description'
            )
        } else {
            data = UtilsService.getPropByString(this.result, 'summary')
        }
        data = data ? data : '-'
        return data
    }

    get confidence() {
        const crop = UtilsService.getPropByString(this.result, 'cropConfidence')
        const irrigation = UtilsService.getPropByString(
            this.result,
            'irrigationConfidence'
        )
        const landCover = UtilsService.getPropByString(
            this.result,
            'landCoverConfidence'
        )

        if (crop === '' || irrigation === '' || landCover === '') {
            return null
        }

        return [crop.toFixed(1), irrigation.toFixed(1), landCover.toFixed(1)]
    }

    get parentRef() {
        return this.$store.getters[SearchGetters.parentRef]
    }

    get isParentRef() {
        return this.parentRef && this.parentRef.id === this.result.id
    }

    get isZenodoType() {
        return (
            (this.isParentRef && this.parentRef === DataSources.ZENODO) ||
            (!this.isParentRef && this.dataSource === DataSources.ZENODO)
        )
    }

    get layerData(): LayerData | undefined {
        return this.$store.getters[MapGetters.layers].find(
            (layerData: LayerData) => layerData.id === this.result.id
        )
    }

    get isWidget() {
        return this.$store.getters[GeneralGetters.isWidget]
    }

    get isSignedIn() {
        return this.$nuxt.$auth.loggedIn;
    }

    get isEntryExtensionEnabled() {
        return this.$store.getters[GeneralGetters.isEntryExtensionEnabled]
    }

    get isExtendedViewEnabled() {
        return this.$store.getters[GeneralGetters.isExtendedViewEnabled]
    }

    get isBulkDownloadEnabled() {
        return this.$store.getters[GeneralGetters.isBulkDownloadEnabled]
    }

    get bookmarks() {
        return this.$store.getters[UserGetters.bookmarks]
    }

    get resultBookmarked() {
        return !!this.bookmarks.find(
            (bookmark) => this.result.id.toString() === bookmark.targetId
        )
    }

    get resultIdDetails() {
        return this.$store.getters[SearchGetters.resultIdDetails]
    }

    get dataSource() {
        return this.$store.getters[SearchGetters.dataSource]
    }

    get dataSourceGroup() {
        return DataSourceGroup[this.dataSource]
    }

    get parentDataSourceGroup() {
        return DataSourceGroup[this.parentRef.dataSource]
    }

    get hiddenDataSources() {
        return this.$store.getters[SearchGetters.hiddenDataSources]
    }

    get showDetailsTrigger() {
        return this.$store.getters[SearchGetters.showDetailsTrigger]
    }

    get buttonWorkflowTitle() {
        return this.isWidget
            ? this.$tc('dabResult.workflowRedirect')
            : this.$tc('dabResult.workflow')
    }

    get rootDataOrigin() {
        // original entry's dataSource exclusively from OpenSearch
        const rootDataOrigin = UtilsService.extractCategoriesByAttributeValue(
            this.result,
            'term',
            'dataSource'
        )[0]
        return rootDataOrigin || DataOrigin[this.dataSource] || this.dataSource
    }

    get isExtendedViewActive() {
        return this.$store.getters[ExtendedViewGetters.isExtendedViewActive]
    }

    get showFullMap() {
        return this.$store.getters[MapGetters.showFull]
    }

    get workflowDispatched() {
        return this.$store.getters[SearchGetters.workflow]
    }

    get dimensions() {
        return this.$store.getters[MapGetters.dimensions]
    }
    
    get currentTime() {
        return this.$store.getters[MapGetters.currentTime]
    }
    
    get showTimeline() {
        return this.$store.getters[MapGetters.showTimeline]
    }

    public setDimensions(value: string[]) {
        this.$store.dispatch(MapActions.setDimensions, value);
    }
    
    public setCurrentTime(value: string) {
        this.$store.dispatch(MapActions.setCurrentTime, value);
    }
    
    public setShowTimeline(value: boolean) {
        this.$store.dispatch(MapActions.setShowTimeline, value);
    }



    public async checkTimeSeries() {
        const attributes: any = UtilsService.getArrayByString(this.result, 'category._attributes')
        this.isTimeSeries = attributes.some(e => e.label === 'timeseries' || e.label === 'ImageMosaic');
    }

    public async handleOpenEOAuth() {
        await OpenEOService.authenticateOpenEO()
    }

    public async toggleExtendedView() {
        this.$store.dispatch(ExtendedViewActions.setResult, this.result)
        this.$store.dispatch(MapActions.setShowFull, !this.showFullMap)
        this.$store.dispatch(
            ExtendedViewActions.setIsExtendedViewActive,
            !this.isExtendedViewActive
        )
        LogService.logElementClick(
            null,
            null,
            this.result.id,
            null,
            'Extended view',
            null,
            this.contributors,
            this.title
        )
    }

    public showDabResultParentRefDetails() {
        if (this.resultIdDetails === this.result.id) {
            this.$store.dispatch(SearchActions.setResultIdDetails, null)
            TutorialTagsService.refreshTagsGroup('result', false)
        } else {
            this.$store.dispatch(
                SearchActions.setResultIdDetails,
                this.result.id
            )
            TutorialTagsService.refreshTagsGroup('result', true, 450)
        }
    }
    public async initResultLayersAndDownloads() {
        this.layers = []
        this.downloads = []
        this.timeSeriesArray = []

        const layers = []
        const downloads = []

        if (this.dataSource === DataSources.NEXTGEOSS) {
            const data: any = UtilsService.getArrayByString(this.result, 'link')
            for (const item of data) {
                if (
                    item._attributes &&
                    item._attributes.rel &&
                    item._attributes.rel === 'enclosure'
                ) {
                    const linkText = item._attributes.href.replace('&amp;', '&')
                    const linkTitle = item._attributes.title
                    const linkDescription = ''
                    const titleBox = this.result.title
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
                                score: linkScore
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
                                score: linkScore
                            })
                        } else if (item._attributes.type === 'text/xml') {
                            downloads.push({
                                name: linkTitle,
                                url: linkText,
                                desc: linkDescription,
                                type: 'xml',
                                title: titleBox,
                                score: linkScore
                            })
                        } else {
                            downloads.push({
                                name: linkTitle,
                                url: linkText,
                                desc: linkDescription,
                                type: 'other',
                                title: titleBox,
                                score: linkScore
                            })
                        }
                    }
                }
            }
        } else if (this.dataSource === DataSources.ZENODO) {
            if (this.result.files) {
                for (const file of this.result.files) {
                    const knownFileTypes = ['pdf', 'png', 'jpg', 'xml', 'txt']
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
                        type: linkType
                    })
                }
                downloads.push({
                    name: this.result.metadata.title,
                    url: this.result.links.doi,
                    desc: this.result.metadata.description,
                    type: 'html'
                })
            }
        } else {
            const data = UtilsService.getArrayByString(
                this.result,
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
                    const wmsAllLayerName = this.result.title
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
                    let titleBox = wmsAllLayerName
                    let kmlKeyword = false
                    for (const category of UtilsService.getArrayByString(
                        this.result,
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
                    let url = linkText
                    url = url.substring(
                        0,
                        url.indexOf('#') === -1 ? url.length : url.indexOf('#')
                    )
                    url = url.substring(
                        0,
                        url.indexOf('?') === -1 ? url.length : url.indexOf('?')
                    )
                    url = url.substring(url.lastIndexOf('/') + 1, url.length)
                    const extension =
                        url.indexOf('.') > -1
                            ? url.split('.').pop().toLowerCase()
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
                            linkTitle = linkText
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

                    if (
                        (extension === 'pdf' || protocol.indexOf('PDF') > -1) &&
                        !complex
                    ) {
                        downloads.push({
                            name: linkTitle,
                            url: linkText,
                            desc: linkDescription,
                            type: 'pdf',
                            title: titleBox,
                            score: linkScore
                        })
                    } else if (
                        (extension === 'geoparquet' || protocol.indexOf('geoparquet') > -1) &&
                        !complex) {
                        downloads.push({
                            name: linkTitle,
                            url: linkText,
                            desc: linkDescription,
                            type: 'geoparquet',
                            title: titleBox,
                            score: linkScore
                        })
                    } else if (
                        (extension === 'txt' || protocol.indexOf('TXT') > -1) &&
                        !complex
                    ) {
                        downloads.push({
                            name: linkTitle,
                            url: linkText,
                            desc: linkDescription,
                            type: 'txt',
                            title: titleBox,
                            score: linkScore
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
                                score: linkScore
                            })
                        } else {
                            downloads.push({
                                name: linkTitle,
                                url: linkText,
                                desc: linkDescription,
                                type: 'html',
                                title: titleBox,
                                score: linkScore
                            })
                        }
                    } else if (
                        (extension === 'jpg' ||
                            extension === 'jpeg' ||
                            protocol.indexOf('JPG') > -1 ||
                            protocol.indexOf('JPEG') > -1) &&
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
                                score: linkScore
                            })
                        }
                    } else if (
                        (extension === 'png' || protocol.indexOf('PNG') > -1) &&
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
                                score: linkScore
                            })
                        }
                    } else if (extension === 'xml' && !complex) {
                        downloads.push({
                            name: linkTitle,
                            url: linkText,
                            desc: linkDescription,
                            type: 'xml',
                            title: titleBox,
                            score: linkScore
                        })
                    } else if (
                        (protocol.indexOf('information-html') > -1 ||
                            protocol.indexOf('information') > -1 ||
                            protocol.indexOf('HTTP') > -1 ||
                            protocol.indexOf('HTML') > -1) &&
                        !complex
                    ) {
                        downloads.push({
                            name: linkTitle,
                            url: linkText,
                            desc: linkDescription,
                            type: 'html',
                            title: titleBox,
                            score: linkScore
                        })
                    } else if (protocol === 'gwp_un_sd_/v1/sdg') {
                        // Take SDG ID from title which is in format "SDG 6.b.1 Indicator".
                        // Enable statistics visualisation and download for this ID.
                        const wordsArray = this.result.title.split(' ')
                        if (
                            wordsArray.length === 3 &&
                            wordsArray[0] === 'SDG' &&
                            wordsArray[2] === 'Indicator'
                        ) {
                            this.statisticsId = wordsArray[1]
                            let seriesId = UtilsService.getArrayByString(
                                item,
                                'gmd:CI_OnlineResource.gmd:name.gco:CharacterString'
                            )[0]
                            const seriesDesc = UtilsService.getArrayByString(
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
                            this.timeSeriesArray.push({
                                id: seriesId,
                                text: seriesDesc
                            })
                            const postData = {
                                indicator: this.statisticsId,
                                series: [seriesId]
                            }
                            downloads.push({
                                name: `SDG-${this.statisticsId}-${seriesId}.csv`,
                                url: 'https://unstats.un.org/SDGAPI/v1/sdg/Series/DataCSV',
                                postData,
                                desc: `${this.statisticsId}, ${seriesDesc}`,
                                type: 'csv'
                            })
                        }
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

                        if (anchor !== undefined) {
                            if (
                                anchor.indexOf('simple') > -1 ||
                                anchor.indexOf('complex') > -1
                            ) {
                                let url = null
                                let legendUrl = null
                                if (
                                    linkText.indexOf('LAYERS=') > -1 ||
                                    linkText.indexOf('layers=') > -1
                                ) {
                                    let startLayers =
                                        linkText.indexOf('LAYERS=')
                                    if (startLayers === -1) {
                                        startLayers =
                                            linkText.indexOf('layers=')
                                    }
                                    const endLayers = linkText.indexOf(
                                        '&',
                                        startLayers
                                    )
                                    const LAYERS = linkText.slice(
                                        startLayers + 7,
                                        endLayers
                                    )
                                    titleBox =
                                        wmsNameDesc !== ''
                                            ? wmsNameDesc
                                            : LAYERS

                                    url = linkText
                                    const urlBeginning = linkText.slice(
                                        0,
                                        startLayers
                                    )
                                    legendUrl = `${urlBeginning}&REQUEST=GetLegendGraphic&FORMAT=image/png&LAYER=${wmsName}`
                                } else {
                                    titleBox =
                                        wmsNameDesc !== ''
                                            ? wmsNameDesc
                                            : wmsName
                                    const LAYERS = wmsName
                                    url = `${linkText}&&&LAYERS=${LAYERS}&VERSION=${wmsVersion}&ANCHOR=${wmsAnchor}`
                                    legendUrl = `${linkText}&REQUEST=GetLegendGraphic&FORMAT=image/png&LAYER=${wmsName}`
                                }
                                if(this.isTimeSeries){
                                    let wmsUrl = ''
                                    UtilsService.getArrayByString(this.result, 'gmd:distributionInfo.gmd:MD_Distribution.gmd:transferOptions').forEach((element: any) => {
                                        const val = UtilsService.getPropByString(element, 'gmd:MD_DigitalTransferOptions.gmd:onLine.gmd:CI_OnlineResource.gmd:protocol.gco:CharacterString')
                                        if(val.includes('WebMapService')){
                                            wmsUrl = UtilsService.getPropByString(element, 'gmd:MD_DigitalTransferOptions.gmd:onLine.gmd:CI_OnlineResource.gmd:linkage.gmd:URL')
                                        }
                                    })
                                    const res = await fetch(`${wmsUrl}service=wms&version=1.3.0&request=GetCapabilities`)
                                    const resTxt = await res.text()
                                    const resJson = JSON.parse(parseXMLToJSON(resTxt))
                                    const dimensions = resJson.WMS_Capabilities.Capability.Layer.Layer.find((element: any) => {
                                        return element.Name === wmsName
                                    }).Dimension.split(',')
                                    this.setDimensions(dimensions.map(date => date.slice(0, 10)))
                                    this.setCurrentTime(dimensions[0].slice(0, 10))

                                }
                                layers.push({
                                    name: wmsName,
                                    url,
                                    type: 'wms',
                                    title: titleBox,
                                    img: this.logo,
                                    legend: { type: 'img', data: legendUrl }
                                })
                            }
                        }
                    } else if (protocol.indexOf('WebCoverageService') > -1) {
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
                                score: linkScore
                            })
                        } else if (wcsAnchor === 'complex') {
                            wcsUrl = `${wcsUrl}&version=${wcsVersion}&coverageId=${wcsId}`
                            downloads.push({
                                name: linkTitle,
                                url: wcsUrl,
                                desc: linkDescription,
                                type: 'custom-download',
                                boxTitle: titleBox,
                                score: 0
                            })
                        }
                    } else if (
                        protocol.indexOf('TiledMapService') > -1 &&
                        !complex &&
                        linkText
                    ) {
                        const tmsName = UtilsService.getPropByString(
                            item,
                            'gmd:CI_OnlineResource.gmd:name.gco:CharacterString'
                        )
                        layers.push({
                            name: tmsName,
                            url: linkText,
                            type: 'tms',
                            title: titleBox,
                            img: this.logo
                        })
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
                            type: 'html',
                            title: titleBox,
                            score: linkScore
                        })
                    } else if (
                        (extension === 'kml' ||
                            linkText.indexOf('format=KML') > -1 ||
                            (linkText.indexOf('/application/xml') > -1 &&
                                kmlKeyword)) &&
                        !complex
                    ) {
                        downloads.push({
                            name: linkTitle,
                            url: linkText,
                            desc: linkDescription,
                            type: 'kml',
                            title: titleBox,
                            score: linkScore
                        })
                        layers.push({
                            name: titleBox,
                            url: linkText,
                            type: 'kml',
                            title: titleBox,
                            img: null
                        })
                    } else if (
                        (extension === 'kmz' ||
                            linkText.indexOf('format=KMZ') > -1 ||
                            (linkText.indexOf('/application/zip') > -1 &&
                                kmlKeyword)) &&
                        !complex
                    ) {
                        downloads.push({
                            name: linkTitle,
                            url: linkText,
                            desc: linkDescription,
                            type: 'kmz',
                            title: titleBox,
                            score: linkScore
                        })
                        layers.push({
                            name: titleBox,
                            url: linkText,
                            type: 'kmz',
                            title: titleBox,
                            img: null
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
                        const lastChar = linkText.substr(linkText.length - 1)
                        if (lastChar === '/' && linkDescription) {
                            if (linkTitle.indexOf('GET DATA') > -1) {
                                downloads.push({
                                    name: linkTitle,
                                    url: linkText,
                                    desc: linkDescription,
                                    type: 'link',
                                    title: titleBox,
                                    score: linkScore
                                })
                            } else if (linkTitle.indexOf('VIEW') > -1) {
                                downloads.push({
                                    name: linkTitle,
                                    url: linkText,
                                    desc: linkDescription,
                                    type: 'other',
                                    title: titleBox,
                                    score: linkScore
                                })
                            }
                        } else {
                            downloads.push({
                                name: linkTitle,
                                url: linkText,
                                desc: linkDescription,
                                type: 'other',
                                title: titleBox,
                                score: linkScore
                            })
                        }
                    }

                    if (protocol === 'ecopotential_workflow_api') {
                        this.workflow = {
                            url:
                                UtilsService.getPropByString(
                                    item,
                                    'gmd:CI_OnlineResource.gmd:linkage.gmd:URL'
                                ) +
                                '/' +
                                encodeURIComponent(
                                    UtilsService.getPropByString(
                                        item,
                                        'gmd:CI_OnlineResource.gmd:name.gco:CharacterString'
                                    )
                                )
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
                        if (!linkScore || linkScore > 100 || linkScore < 0) {
                            linkScore = -1
                        }
                        downloads.push({
                            name: 'Link',
                            url: directLink,
                            desc: '',
                            type: 'wcs',
                            boxTitle: '',
                            score: linkScore
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
                            url: advancedLink + sentinelCustomDownloadSuffix,
                            desc: '',
                            type: 'Advanced access link',
                            boxTitle: '',
                            score: 0
                        })
                    } else {
                        downloads.push({
                            name: 'Custom download',
                            url: advancedLink,
                            desc: '',
                            type: 'custom-download',
                            boxTitle: '',
                            score: 0
                        })
                    }
                }
            }

            // WorldCereal MAPS4GPP collection
            if (this.result.sourceId === 'worldcereal' || this.result.sourceId === 'agrostac') {
                downloads.push({ name: this.result.title, url: `${SearchEngineService.getDabBaseUrl()}/worldcereal/query?searchFields=title,keywords,abstract&reqID=6hnblre3236&si=1&ct=12&rel=OVERLAPS&viewid=&sources=worldcereal&parents=${this.result.id}`, desc: '', type: 'worldcereal-collection' });
            }

            if (!this.workflow) {
                const resources = UtilsService.getArrayByString(
                    this.result,
                    'gmd:distributionInfo.gmd:transferOptions.gmd:MD_DigitalTransferOptions.gmd:onLine.gmd:CI_OnlineResource'
                )
                for (const resource of resources) {
                    if (
                        UtilsService.getPropByString(
                            resource,
                            'gmd:protocol.gco:CharacterString'
                        ) === 'ecopotential_workflow_api'
                    ) {
                        this.workflow = {
                            url:
                                UtilsService.getPropByString(
                                    resource,
                                    'gmd:linkage.gmd:URL'
                                ) +
                                '/' +
                                encodeURIComponent(
                                    UtilsService.getPropByString(
                                        resource,
                                        'gmd:name.gco:CharacterString'
                                    )
                                )
                        }
                    }
                }
            }
        }

        this.layers = layers
        if(!this.isEoWorkflow){
            for (const download of downloads) {
                const downloadFileSameFormatIndex = this.downloads.findIndex(
                    (file) => file.type === download.type
                )

                const { scoreText, scoreClass } = this.getDownloadLinkStatus(
                    download.score
                )

                download.scoreText = scoreText
                download.scoreClass = scoreClass

                if (
                    downloadFileSameFormatIndex !== -1 &&
                    !this.downloads[downloadFileSameFormatIndex].links
                ) {
                    const file = this.downloads.splice(
                        downloadFileSameFormatIndex,
                        1
                    )[0]
                    this.downloads.push({
                        type: download.type,
                        links: [download, file]
                    })
                } else if (downloadFileSameFormatIndex !== -1) {
                    const downloadFileSameFormat =
                        this.downloads[downloadFileSameFormatIndex]
                    downloadFileSameFormat.links.push(download)
                } else {
                    this.downloads.push(download)
                }
            }
        }

    }

    public showOnMap() {
        if (this.layerData.coordinate) {
            if (this.isExtendedViewActive) {
                this.toggleExtendedView()
            }
            this.$store.dispatch(
                MapActions.centerMap,
                this.layerData.coordinate
            )
            this.$store.dispatch(MapActions.setClickedLayerId, this.result.id)
            this.$store.dispatch(MapActions.setShowFull, true)
            const coordinates = `${this.layerData.coordinate.S} ${this.layerData.coordinate.W} ${this.layerData.coordinate.N} ${this.layerData.coordinate.E}`
            LogService.logElementClick(
                coordinates,
                null,
                this.result.id,
                'dab',
                'Show on map',
                null,
                this.result.contributor.orgName,
                this.result.title
            )
        }
        MouseLeaveService.initSurvey()
    }

    public async showDetails() {
        LogService.logElementClick(
            null,
            null,
            this.result.id,
            null,
            'Show details',
            null,
            this.contributors,
            this.title
        )
        this.$store.dispatch(SearchActions.showDetailsTrigger, false)
        MouseLeaveService.initSurvey()
        const actionAfterMetadataShow =
            this.$store.getters[SearchGetters.actionAfterMetadataShow]
        let isSatellite = false
        if (!this.metadata) {
            if (this.dataSource === DataSources.ZENODO) {
                isSatellite = false
                this.metadata = {
                    title: this.$tc('popupTitles.resourceDetails'),
                    data: {
                        ...this.result,
                        acquisition: {
                            platform: 'ZENODO'
                        }
                    }
                }
            } else if (this.dataSource === DataSources.WIKIPEDIA) {
                isSatellite = false
                this.metadata = {
                    title: this.$tc('popupTitles.resourceDetails'),
                    data: {
                        ...this.result,
                        acquisition: {
                            platform: 'WIKIPEDIA'
                        }
                    }
                }
            } else if (this.dataSource === DataSources.NEXTGEOSS) {
                isSatellite = false
                this.metadata = {
                    title: this.$tc('popupTitles.resourceDetails'),
                    data: {
                        ...this.result,
                        acquisition: {
                            platform: 'NEXTGEOSS'
                        }
                    }
                }
            } else if (
                this.result.acquisition &&
                Object.keys(this.result.acquisition.platform).length
            ) {
                if (this.result.acquisition.platform === 'GEOSS_CR') {
                    isSatellite = false
                } else {
                    isSatellite = true
                }
                this.metadata = {
                    title: this.$tc('popupTitles.resourceDetails'),
                    data: {
                        ...this.result
                    }
                }
            } else {
                const [, data] = await to(
                    GeossSearchApiService.getDabResultMetadata(this.result.id)
                )
                if (data) {
                    data.userContributions = this.result.userContributions
                    this.metadata = {
                        title: `<div class="d-flex flex--justify-between flex--align-center padding-right-30">
                                    <span>${this.$tc(
                                        'popupTitles.resourceDetails'
                                    )}</span>
                                    <a class="link-white" target="_blank" href="${SearchEngineService.getMetaDataUrl(
                                        this.result.id
                                    )}">${this.$tc(
                            'popupTitles.rawMetadata'
                        )}</a>
                                </div>`,
                        data
                    }
                }
            }

            Object.freeze(this.metadata)

            if (actionAfterMetadataShow) {
                actionAfterMetadataShow()
            }
        }

        if (this.dashboardContent && this.dashboardContent !== '') {
            DashboardService.showDashboard(this.dashboardContent, null)
        } else if (this.metadata) {
            const comments = await this.getComments()

            const props = {
                data: this.metadata.data,
                isSatellite,
                resultTitle: this.result.title,
                resultImage: this.logo,
                popupTitle: this.metadata.title,
                comments,
            }
            this.$store.dispatch(PopupActions.openPopup, {
                contentId: 'metadata',
                title: props.popupTitle,
                component: DabResultMetadata,
                props
            })
        }
    }

    public async getComments() {
        const res = await RatingService.fetchComments(this.result.id)
        if (res) {
            return res
        } else {
            return 0
        }
    }


    public addParentRefAvailable(result) {
        const parentRefAvailable =
            UtilsService.extractCategoriesByAttributeValue(
                result,
                'label',
                'series'
            )
        return !(parentRefAvailable.indexOf('hlevel') === -1)
    }

    public isDrillAvailable(result, dataHub) {
        dataHub = dataHub.replace('services', 'service')
        const drillAvailable = UtilsService.extractCategoriesByAttributeValue(
            result,
            'label',
            'series'
        )
        return !(drillAvailable.indexOf(dataHub) === -1)
    }

    public isUserContributedDrillAvailable(result, dataHub) {
        dataHub = dataHub.replace('services', 'service')
        const drillAvailable = []
        if (
            result.userContributions &&
            result.userContributions.relations &&
            result.userContributions.relations.length
        ) {
            for (const relation of result.userContributions.relations) {
                if (relation.destEntryCode !== result.id) {
                    drillAvailable.push(
                        relation.destType.replace('resource', 'hub')
                    )
                }
            }
            return !(drillAvailable.indexOf(dataHub) === -1)
        } else {
            return false
        }
    }

    public showInsideFolder(result, dataSource?) {
        if (this.isExtendedViewActive) {
            this.toggleExtendedView()
        }

        const parentRef: ParentRef = {
            id: result.id,
            entry: result,
            dataSource: this.dataSource
        }
        /*
         * regular drill-down doesn't have dataSource parameter
         * if regular drill-down, fetch only within the same dataSource (not same origin, not same tab)
         * else if switching data sources, fetch other sources asynchronously
         */
        const targetSource = !dataSource ? this.dataSource : dataSource
        const fetchOtherSources = !dataSource ? false : true
        const theSameSource = !dataSource ? true : false

        this.$store.dispatch(SearchActions.addParentRef, parentRef)
        this.$store.dispatch(SearchActions.setResultIdDetails, null)
        this.$store.dispatch(FacetedFiltersActions.reset)
        this.$store.dispatch(SearchActions.getResults, {
            fetchOtherSources,
            targetSource,
            theSameSource
        })
    }

    // Switches to related results within GEOSS Curated
    public switchToDataSource(switchTarget: DataSource, result) {
        if (this.isExtendedViewActive) {
            this.toggleExtendedView()
        }

        this.showInsideFolder(result, switchTarget)
        this.$store.dispatch(SearchActions.setDataSource, {
            value: switchTarget
        })
    }

    public switchToUserContributedDataSource(switchTarget: DataSource, result) {
        if (this.isExtendedViewActive) {
            this.toggleExtendedView()
        }

        const hub = switchTarget === 'services' ? 'service' : switchTarget
        const targetIdsObject = {}
        const filteredRelations = result.userContributions.relations.filter(
            (el) => el.destType === `${hub}_resource`
        )

        for (const relation of filteredRelations) {
            for (const source in DataOrigin) {
                if (DataOrigin[source] === relation.destDataSource) {
                    if (
                        (DataOrigin[source] === 'geoss_cr' &&
                            source === switchTarget) ||
                        DataOrigin[source] !== 'geoss_cr'
                    ) {
                        if (!targetIdsObject[source]) {
                            targetIdsObject[source] = []
                        }
                        targetIdsObject[source].push(relation.destEntryCode)
                    }
                }
            }
        }

        this.$store.dispatch(
            SearchActions.setTargetIds,
            JSON.stringify(targetIdsObject)
        )
        this.showInsideFolder(result, null)
    }

    public popParentRefResult() {
        this.$store.dispatch(SearchActions.setDataSource, {
            value: this.parentRef.dataSource
        })
        this.$store.dispatch(SearchActions.popParentRef)
        this.$store.dispatch(FacetedFiltersActions.reset)
        this.$store.dispatch(SearchActions.getResults)
    }

    public toggleShowDownloads() {
        this.showDownloads = !this.showDownloads
    }

    public instantSingleDownload(download) {
        if (download.links) {
            this.openDownloadLinksPopup(download.links)
        } else if (download.url.indexOf('/dhus/odata/') !== -1) {
            this.openSentinelLoginPopup(download.url)
        } else if (download.url.indexOf('/sdg/Series/DataCSV') !== -1) {
            this.getUnepFile(download.postData)
        } else if (download.type === 'custom-download') {
            this.initCustomDownloadPopup(download.url)
        } else if (download.type === 'html') {
            this.downloadLinkClicked(download.url)
        } else {
            this.downloadLinkClicked(download.url)
        }
    }

    public showExpandableDownload() {
        if (this.downloads && this.downloads.length) {
            if (this.downloads.length > 1) {
                return true
            } else {
                return false
            }
        } else {
            return false
        }
    }

    public async initCustomDownloadPopup(baseUrl) {
        if (baseUrl.startsWith('http:')) {
            baseUrl = baseUrl.replace('http:', 'https:')
        }
        if (!this.customDownloadOptions) {
            if (baseUrl.includes('service=WCS&version=2.0.1')) {
                const [err, data] = await to(
                    GeossSearchApiService.getCustomDownloadOptionsWCS(baseUrl)
                )
                if (err) {
                    const props = {
                        title: this.$tc('general.error'),
                        subtitle: err
                    }
                    return this.$store.dispatch(PopupActions.openPopup, {
                        contentId: 'error',
                        component: ErrorPopup,
                        props
                    })
                }

                let formatOptions = UtilsService.getArrayByString(
                    data.getCapabilities,
                    'wcs:Capabilities.wcs:ServiceMetadata.wcs:formatSupported'
                )
                formatOptions = formatOptions.map((option) => ({
                    id: option,
                    text: option
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
                    text: option
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

                this.customDownloadOptions = {
                    formatOptions,
                    nativeFormat,
                    scaleFactor: 0.5,
                    rangeSubset,
                    outputCRS,
                    lowerCorner,
                    upperCorner,
                    baseUrl
                }
            } else {
                const [err, data] = await to(
                    GeossSearchApiService.getCustomDownloadOptions(baseUrl)
                )
                if (err) {
                    const props = {
                        title: this.$tc('general.error'),
                        subtitle: err
                    }
                    return this.$store.dispatch(PopupActions.openPopup, {
                        contentId: 'error',
                        component: ErrorPopup,
                        props
                    })
                }
                let formatOptions = UtilsService.getArrayByString(
                    data,
                    'ns2:format'
                )
                formatOptions = formatOptions.map((option) => ({
                    id: option,
                    text: option
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
                    let outputSizeVal: string | string[] =
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
                        text: subsetCRSOption
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

                this.customDownloadOptions = {
                    formatOptions,
                    subsetCRSOptions,
                    outputSize,
                    subsetLowerCoordinates,
                    subsetUpperCoordinates,
                    crs: subsetCRSOptions[0].id,
                    baseUrl
                }
            }

            Object.freeze(this.customDownloadOptions)
        }

        const props = {
            options: this.customDownloadOptions,
            resultId: this.result.id,
            resultOrgName: this.result.contributor.orgName,
            resultTitle: this.result.title
        }

        if (baseUrl.includes('service=WCS&version=2.0.1')) {
            this.$store.dispatch(PopupActions.openPopup, {
                contentId: 'custom-download',
                title: this.$tc('popupTitles.customizeDownload'),
                component: CustomDownloadWCS,
                props
            })
        } else {
            this.$store.dispatch(PopupActions.openPopup, {
                contentId: 'custom-download',
                title: this.$tc('popupTitles.customizeDownload'),
                component: DabResultCustomDownload,
                props
            })
        }
    }

    public prepareShareUrl() {
        this.shareUrl = SearchEngineService.getShareUrl(
            this.result.id.toString()
        )
    }

    public async addBookmark() {
        MouseLeaveService.initSurvey()
        const resultId = this.result.id.toString()
        const resultTitle =
            this.dataSource === DataSources.ZENODO
                ? this.result.metadata.title
                : this.result.title

        const success = await BookmarksService.addBookmark(
            resultTitle,
            resultId,
            this.result.currMap,
            DataOrigin[this.dataSource]
        )

        if (success) {
            this.$store.dispatch(UserActions.addBookmark, {
                name: resultTitle,
                targetId: resultId,
                currMap: this.result.currMap,
                dataSource: DataOrigin[this.dataSource]
            })

            let organisation = null
            if (this.result.contributor && this.result.contributor.orgName) {
                organisation = this.result.contributor.orgName
            }
            LogService.logElementClick(
                null,
                null,
                resultId,
                null,
                'Bookmark saved in My Workspace',
                null,
                organisation,
                resultTitle
            )
            NotificationService.show(
                `${this.$tc('popupTitles.bookmarkResult')}`,
                `${this.$tc('popupContent.bookmarkSavedSuccess')}`,
                10000,
                null,
                9999,
                'success'
            )
        } else {
            NotificationService.show(
                `${this.$tc('popupTitles.bookmarkResult')}`,
                `${this.$tc('popupContent.bookmarkSavedFail')}`,
                10000,
                null,
                9999,
                'error'
            )
        }
    }

    public async removeBookmark() {
        const resultId = this.result.id.toString()
        const resultTitle =
            this.dataSource === DataSources.ZENODO
                ? this.result.metadata.title
                : this.result.title
        const bookmarkedResult = this.bookmarks.find(
            (bookmark) => bookmark.targetId === resultId
        )

        const dataOrigin = DataOrigin[this.dataSource]

        const success = await BookmarksService.removeBookmark(
            bookmarkedResult.targetId,
            dataOrigin
        )

        if (success) {
            this.$store.dispatch(UserActions.removeBookmark, resultId)

            let organisation = null
            if (this.result.contributor && this.result.contributor.orgName) {
                organisation = this.result.contributor.orgName
            }

            LogService.logElementClick(
                null,
                null,
                resultId,
                null,
                'Bookmark removed from My Workspace',
                null,
                organisation,
                resultTitle
            )

            NotificationService.show(
                `${this.$tc('popupTitles.bookmarkResult')}`,
                `${this.$tc('popupContent.bookmarkRemovedSuccess')}`,
                10000,
                null,
                9999,
                'success'
            )
        } else {

            NotificationService.show(
                `${this.$tc('popupTitles.bookmarkResult')}`,
                `${this.$tc('popupContent.bookmarkRemovedFail')}`,
                10000,
                null,
                9999,
                'error'
            )
        }
    }

    public setExpandedDownloadIndex(index) {
        if (index !== this.expandedDownloadIndex) {
            this.expandedDownloadIndex = index
        } else {
            this.expandedDownloadIndex = null
        }
    }

    public downloadLinkClicked(url, format = null) {
        const id = this.result.id
        let orgName = ''
        let title = ''
        if (this.dataSource === 'zenodo') {
            const orgNameArray = this.result.metadata.creators
            orgName = orgNameArray.join(', ')
            title = this.result.metadata.title
        } else if (this.dataSource === 'nextgeoss') {
            orgName = this.result['dc:publisher']
        } else {
            orgName = this.result.contributor.orgName
            title = this.result.title
        }

        if (format === 'worldcereal-collection') {
            url = this.appendWorldCerealCollectionParameters(url)
        }

        window.open(url)

        LogService.logElementClick(
            null,
            null,
            id,
            null,
            'Sirect download',
            null,
            orgName,
            title
        )
        MouseLeaveService.initSurvey()
    }

    public async openBulkDownloadPopup(e) {
        e.preventDefault()
        const title = this.$tc('popupTitles.downloadsList')
        await this.$store.dispatch(PopupActions.openPopup, {
            contentId: 'bulk-download',
            title,
            component: BulkDownloadPopup
        })
        return false
    }

    public addToDownloadsList(url, format) {
        if (!this.isSignedIn) {
            return
        }

        if (format === 'worldcereal-collection') {
            url = this.appendWorldCerealCollectionParameters(url)
        }

        const link: BulkDownloadLink = {
            name: `${this.title}`,
            desc: `Format: ${format || 'other'}`,
            url
        }

        this.$store.dispatch(BulkDownloadActions.addLink, link)
        PopupCloseService.closePopup('custom-download')

        NotificationService.show(
            `${this.$tc('popupTitles.downloadList')}`,
            `${this.$tc('popupContent.addedToDownloadList')}`,
            10000,
            null,
            9999,
            'success'
        )

        setTimeout(() => {
            const openBulkDownloadPopup = document.querySelectorAll(
                '.openBulkDownloadPopup'
            )
            if (openBulkDownloadPopup && openBulkDownloadPopup.length) {
                openBulkDownloadPopup.forEach((button) => {
                    button.addEventListener('click', this.openBulkDownloadPopup)
                })
            }
        }, 200)

        LogService.logElementClick(
            null,
            null,
            this.result.id,
            null,
            'Direct to bulk download',
            null,
            this.result.contributor.orgName,
            this.result.title
        )
    }

    public openSentinelLoginPopup(url: string) {
        if (this.isWidget) {
            window.open(url)
        } else if (
            !this.isSignedIn ||
            !this.$store.getters[MyWorkspaceGetters.settings].dhusUsername
        ) {
            const props = {
                url,
                result: this.result
            }
            this.$store.dispatch(PopupActions.openPopup, {
                contentId: 'sentinel-login',
                title: this.$tc('popupTitles.sentinelDataAccess'),
                component: SentinelLogin,
                props
            })
        } else {
            window.open(SearchEngineService.getDhusProxyUrl(url))
        }
    }

    public async getUnepFile(params) {
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
            this.result.id,
            null,
            'Direct download',
            null,
            this.result.contributor.orgName,
            this.result.title
        )
    }

    public appendWorldCerealCollectionParameters(url) {
        const concatCoordinates = coordinates => {
            const { W, S, E, N } = coordinates;
            if (W && S && E && N) {
                return `${W},${S},${E},${N}`;
            }
            return ',,,'
        }

        const dateFrom =
            this.$store.getters[GeneralFiltersGetters.state].dateFrom
        const dateTo = this.$store.getters[GeneralFiltersGetters.state].dateTo
        const selectedAreaCoordinates =
            this.$store.getters[GeneralFiltersGetters.state]
                .selectedAreaCoordinates
        const params = {
            cropTypes: this.$store.getters[InSituFiltersGetters.cropTypes],
            quantityTypes: this.$store.getters[InSituFiltersGetters.quantityTypes],
            landCoverTypes: this.$store.getters[InSituFiltersGetters.landCoverTypes],
            irrigationTypes: this.$store.getters[InSituFiltersGetters.irrigationTypes],
            cropConfidence: this.$store.getters[InSituFiltersGetters.cropConfidence],
            landCoverConfidence: this.$store.getters[InSituFiltersGetters.landCoverConfidence],
            irrigationConfidence: this.$store.getters[InSituFiltersGetters.irrigationConfidence],
            ts: dateFrom ? date(dateFrom, 'YYYY-MM-DDThh:mm:ssZ') : null,
            te: dateTo ? date(dateTo, 'YYYY-MM-DDThh:mm:ssZ') : null,
            bbox:
                concatCoordinates(selectedAreaCoordinates) !== ',,,'
                    ? concatCoordinates(selectedAreaCoordinates)
                    : null
        }

        if (params.cropConfidence.join(',') === '0,100') {
            delete params.cropConfidence
        }
        if (params.landCoverConfidence.join(',') === '0,100') {
            delete params.landCoverConfidence
        }
        if (params.irrigationConfidence.join(',') === '0,100') {
            delete params.irrigationConfidence
        }

        for (const param in params) {
            if (param) {
                const appendix = params[param]
                if (appendix && appendix.length) {
                    if (typeof appendix === 'string') {
                        url += `&${param}=${appendix}`
                    } else if (Array.isArray(appendix)) {
                        url += `&${param}=${appendix.join()}`
                    }
                }
            }
        }
        return url
    }

    public openDownloadLinksPopup(links) {
        const props = {
            result: this.result,
            links
        }

        this.$store
            .dispatch(PopupActions.openPopup, {
                contentId: 'download-links',
                title: this.$tc('popupTitles.downloadLinks'),
                component: DabResultDownloads,
                props
            })
            .then((sentinelUrl: string) => {
                if (sentinelUrl) {
                    this.openSentinelLoginPopup(sentinelUrl)
                }
            })

        const actionAfterDownloadPopupShow =
            this.$store.getters[SearchGetters.actionAfterDownloadPopupShow]
        if (actionAfterDownloadPopupShow) {
            actionAfterDownloadPopupShow()
        }
    }

    public workflowHasSavedData() {
        let hasSavedData = false
        if (sessionStorage.getItem('SERVICE_WORKFLOW')) {
            const serviceWorkflowObject = JSON.parse(
                sessionStorage.getItem('SERVICE_WORKFLOW')
            )
            if (serviceWorkflowObject[this.workflow.url]) {
                hasSavedData = true
            }
        }
        return hasSavedData
    }

    public async openWorkflow(reset?: boolean) {
        if (this.isWidget) {
            // Redirect widget users to native portal
            window.open(
                SearchEngineService.getShareUrl(this.result.id.toString())
            )
            return
        } else if (this.extendedViewMode) {
            // Switch to "normal view" and trigger workflow button there.
            const workflowButton = document.querySelector(
                `.search-container .dab-result-details[data-id="${this.result.id}"] .dab-result-details__actions-workflow`
            ) as HTMLElement
            this.toggleExtendedView()
            setTimeout(() => {
                workflowButton.click()
            }, 450)
            return
        } else if (reset) {
            if (!this.workflowHasSavedData) {
                this.$store.dispatch(SearchActions.setWorkflowResource, null)
            }
            this.$store.dispatch(SearchActions.setWorkflowInputId, null)
            this.$store.dispatch(SearchActions.setWorkflowInputType, null)
        }

        if(this.isEoWorkflow) {
            const currDateTime = Math.floor(Date.now() / 1000)
            if (!this.$store.getters[UserGetters.openEOToken] || this.$store.getters[UserGetters.openEOTokenExpireDate] <= currDateTime) {
                await OpenEOService.authenticateOpenEO()
            }
            const data = UtilsService.getArrayByString(
                this.result,
                'gmd:distributionInfo.gmd:MD_Distribution.gmd:transferOptions.gmd:MD_DigitalTransferOptions.gmd:onLine'
            );
            const url = UtilsService.getPropByString(data[0], 'gmd:CI_OnlineResource.gmd:linkage.gmd:URL');
            const props = {
                workflowUrl: url,
                title: this.result.title
            }

            if(this.$store.getters[UserGetters.openEOToken] && this.$store.getters[UserGetters.openEOTokenExpireDate] > currDateTime){
                this.$store.dispatch(PopupActions.openPopup, {
                contentId: "openEOWorkflow",
                title: this.$tc('popupTitles.workflowandruns'),
                noCloseOutside: true,
                component: OpenEOWorkflowComponent,
                props
            })
            }

        }

        if (this.workflow) {
            if (!this.workflow.data) {
                const [err, data] = await to(
                    GeossSearchApiService.getWorkflow(this.workflow.url)
                )
                if (!err) {
                    this.workflow.data = data
                } else {
                    const message =
                        UtilsService.getPropByString(
                            err,
                            'response.data.message'
                        ) || this.$tc('popupContent.serverResponseTimeout')
                    const props = {
                        title: this.$tc('general.error'),
                        subtitle: message ? message : err
                    }
                    return this.$store.dispatch(PopupActions.openPopup, {
                        contentId: 'error',
                        component: ErrorPopup,
                        props
                    })
                }
            }

            let urlToResource = ''
            if (!this.isSignedIn) {
                urlToResource = SearchEngineService.getRedirectUrl(
                    this.result.id.toString(),
                    'openWorkflow'
                )
            }

            const props = {
                workflow: this.workflow.data,
                workflowUrl: this.workflow.url,
                urlToResource
            }

            this.$store.dispatch(PopupActions.openPopup, {
                contentId: 'workflow',
                title: this.$tc('popupTitles.workflowandruns'),
                noCloseOutside: true,
                component: ServiceWorkflow,
                props
            })
        }
    }

    public isLayerDisplayed(id) {
        return LayersUtils.isLayerDisplayed(id)
    }

    public layerButtonAction() {
        if (this.layers.length === 1) {
            this.toggleSingleLayer()
        } else if (this.layers.length > 1) {
            this.toggleLayersPopup()
        } else if (this.statisticsId) {
            this.getStatistics()
        }
    }

    private toggleSingleLayer() {
        const coordinates =
            this.result.box && typeof this.result.box === 'string'
                ? this.result.box
                : null
        if(this.isTimeSeries){
            const currVal = this.showTimeline
            this.setShowTimeline(!currVal)
            LayersUtils.toggleLayer(this.layers[0], coordinates, this.image, this.dimensions[0])
        }else {
            LayersUtils.toggleLayer(this.layers[0], coordinates, this.image)
        }
    }

    private toggleLayersPopup() {
        const props = {
            data: this.layers,
            resultCoordinates:
                this.result.box && typeof this.result.box === 'string'
                    ? this.result.box
                    : null,
            image: this.image
        }

        this.$store.dispatch(PopupActions.openPopup, {
            contentId: 'layers',
            title: this.$tc('popupTitles.layers'),
            component: DabResultLayers,
            props
        })
    }

    private async getStatistics() {
        const props = {
            indicator: this.statisticsId,
            timeSeriesArray: this.timeSeriesArray
        }
        this.$store.dispatch(PopupActions.openPopup, {
            contentId: 'unsd',
            title: this.$tc('popupTitles.unsd'),
            component: UnitedNationsStatistics,
            props
        })
    }

    private getDownloadLinkStatus(score) {
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
            scoreClass
        }
    }

    private getDownloadButtonLabel(downloadType) {
        let label = downloadType
        if (label === 'file') {
            label = this.$tc('dabResult.file')
        } else if (label === 'other') {
            label = this.$tc('dabResult.other')
        } else if (label === 'html') {
            label = this.$tc('dabResult.view')
        } else if (label === 'worldcereal-collection') {
            label = 'Download filtered data'
        } else if (label === 'geoparquet') {
            label = 'Download complete dataset'
        }
        return label.toUpperCase()
    }

    private updateUserContributions(removedExtensionData) {
        const model = removedExtensionData.model
        const target = removedExtensionData.target
        const extensionId = removedExtensionData.target.entryExtensionId || null
        const arrayIndex = removedExtensionData.arrayIndex || null

        const updatedUserContributions = this.result.userContributions
        let elementToUpdate = updatedUserContributions.extensions.find(
            (e) => e.entryExtensionId === extensionId
        )[model]
        switch (model) {
            case 'summary':
                elementToUpdate = ''
                break
            case 'keywords':
                elementToUpdate = []
                break
            case 'transferOptions':
                if (arrayIndex) {
                    elementToUpdate = elementToUpdate.filter(
                        (e) =>
                            JSON.stringify(e) !==
                            JSON.stringify(target[model][arrayIndex])
                    )
                }
                break
            case 'comment':
                updatedUserContributions.comments =
                    updatedUserContributions.comments.filter(
                        (e) => JSON.stringify(e) !== JSON.stringify(target)
                    )
                break
            default:
                break
        }
        this.$set(this.result, 'userContributions', updatedUserContributions)
    }

    private async mounted() {
        if (this.$route.query.code) {
            try {
                // Check whether the page contains the authentication information and make them available to the openEO client
                await OidcProvider.signinCallback()
            } catch (error) {
                console.log(error)
            }
        }
        await this.checkTimeSeries()
        await this.initResultLayersAndDownloads()
        const resultToHighlight =
            this.$store.getters[SearchGetters.highlightResult]

        if (
            this.$store.getters[SearchGetters.workflow] &&
            this.resultIdDetails === this.result.id
        ) {
            this.openWorkflow()
            this.$store.dispatch(SearchActions.setWorkflow, null)
        } else if (resultToHighlight) {
            this.$store.dispatch(
                SearchActions.setResultIdDetails,
                resultToHighlight
            )
            const trigger = UtilsService.getUrlParam('trigger')
            if (trigger && trigger === 'openWorkflow') {
                setTimeout(() => {
                    this.openWorkflow()
                }, 450)
            }
            this.$store.dispatch(SearchActions.setHighlightResult, null)
        }
        PopupCloseService.eventBus.$on(
            'close',
            ({
                contentId,
                response
            }: {
                contentId: string
                response?: any
            }) => {
                if (this.resultIdDetails === this.result.id) {
                    if (
                        (contentId === 'extension-remove' &&
                            response === 'cancel') ||
                        (contentId === 'error' &&
                            response === 'extension-remove-error') ||
                        (contentId === 'general' &&
                            response &&
                            response.id === 'extension-remove-success') ||
                        (contentId === 'general' &&
                            response &&
                            response.id === 'extension-remove-unavailable')
                    ) {
                        if (response && response.data) {
                            this.updateUserContributions(response.data)
                        }
                        this.showDetails()
                    }
                }
            }
        )
        this.prepareShareUrl()
    }

    @Watch('currentTime')
    private updateLayerForTimeseries(){
        this.$store.dispatch(MapActions.changeLayerTime, {
            id: this.layers[0].url,
            value: this.currentTime
        })    
    }

    @Watch('dataSource')
    private onDataSourceChange() {
        this.initResultLayersAndDownloads()
    }

    @Watch('showDetailsTrigger')
    private onShowDetailsTrigger() {
        if (this.showDetailsTrigger) {
            this.showDetails()
        }
    }

    @Watch('resultIdDetails')
    private onResultIdDetailsChange() {
        if (
            this.resultIdDetails === this.result.id &&
            !this.result.userContributions
        ) {
            if (this.isEntryExtensionEnabled) {
                GeossSearchApiService.getUserContributions(
                    this.result.id,
                    this.rootDataOrigin
                ).then((data) => {
                    this.$set(this.result, 'userContributions', data)
                })
            } else {
                this.$set(this.result, 'userContributions', {})
            }
        }
        this.prepareShareUrl()
    }
}
</script>

<style lang="scss" scoped>
.dab-result-details {
    position: relative;

    .dab-result__contributor {
        font-size: 12px;
        margin-bottom: 10px;
        width: auto;
        display: block;
        color: #777;
    }

    &__is-parent-ref-trigger {
        color: white;
        font-size: 20px;
        display: flex;
        align-items: center;

        button {
            min-width: 23px;
            width: 23px;
            height: 23px;
            border-radius: 50%;
            border: 2px solid white;
            position: relative;
            margin-right: 15px;

            &:before,
            &:after {
                content: '';
                top: 7px;
                width: 8px;
                height: 2px;
                background: white;
                left: 5px;
                display: block;
                position: absolute;
                transform: rotate(-45deg);
            }

            &:after {
                transform: rotate(45deg);
                top: 11px;
            }
        }

        .title {
            overflow: hidden;
            text-overflow: ellipsis;
            padding: 0 10px;
            background: none;
            border: none;
            outline: 0;
            cursor: pointer;

            &:hover {
                text-decoration: underline;
            }
        }
    }

    @media (max-width: $breakpoint-sm) {
        width: 100%;
    }

    &.is-parent-ref {
        width: 100%;
    }

    &__outer-wrapper {
        &.is-parent-ref {
            margin-top: 5px;
            background: $blue-transparent;
            padding: 8px 20px;
        }
    }

    &__wrapper > div {
        display: flex;
        margin-top: -1px;
        background: rgba(255, 255, 255, 0.9);
        background-clip: content-box;
        height: 250px;
        outline: 2px solid $blue;

        .is-parent-ref & {
            margin-top: 5px;
            background: none;
            margin-bottom: 10px;
            height: 230px;

            @media (max-width: $breakpoint-sm) {
                flex-direction: column;
            }
        }

        @media (max-width: $breakpoint-sm) {
            height: auto;
            min-height: 340px;
            max-height: 400px;
        }
    }

    &__image {
        width: 33.333%;
        height: 100%;
        background: $grey;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        flex: 0 0 auto;

        @media (max-width: $breakpoint-sm) {
            width: 100%;
        }

        &--default {
            padding: 30px;

            @media (max-width: $breakpoint-sm) {
                height: 150px;
                padding: 10px;
            }
        }

        img {
            width: auto;
            height: auto;
            display: block;
            max-width: 100%;
            max-height: 100%;
        }
    }

    &__text-actions {
        background: white;
        padding: 15px;
        display: flex;
        flex: 1 1 auto;
        overflow: hidden;

        .is-parent-ref & {
            background: none;
            padding: 0 30px 10px;

            @media (max-width: $breakpoint-sm) {
                padding: 0 0 10px;
            }
        }

        @media (max-width: $breakpoint-md) {
            padding: 20px;
        }

        &.set-parent-ref-available {
            padding-right: 0px;
            position: relative;

            & > div {
                padding-right: 25px;
                margin-right: 50px;

                @media (max-width: $breakpoint-md) {
                    padding-right: 15px;
                }

                .is-parent-ref & {
                    padding-right: 0px;
                    border-right: none;
                }
            }
        }
    }

    &__text {
        flex: 1 0 0;
        display: flex;
        flex-direction: column;
        min-height: 0;
        margin: 5px 15px 5px 0;
    }

    &__title {
        color: $blue;
        line-height: 20px;
        font-size: 17px;
        font-weight: 700;
        word-break: break-word;
        max-height: 42px;
        overflow: hidden;
        text-overflow: ellipsis;
        -webkit-line-clamp: 2;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        margin-bottom: 3px;
        margin-top: -4px;

        .is-parent-ref & {
            color: white;
        }
    }

    &__contributor {
        font-size: 13px;
        margin-bottom: 5px;
        width: auto;
        display: block;
        color: #777;

        .is-parent-ref & {
            color: white;
        }

        span + span {
            &:before {
                content: ', ';
            }
        }
    }

    &__summary {
        font-size: 14px;
        line-height: 14px;
        color: black;
        flex: 1 1 auto;
        overflow: hidden;
        margin-top: 10px;
        position: relative;
        width: 100%;

        @media (max-width: $breakpoint-sm) {
            word-break: break-word;
        }

        .is-parent-ref & {
            color: white;

            &:after {
                display: none;
            }
        }

        &:after {
            position: absolute;
            bottom: 0;
            left: 0;
            content: '';
            background: linear-gradient(
                rgba(white, 0.001),
                rgba(white, 0.75) 50%,
                white 100%
            );
            z-index: 0;
            height: 30px;
            width: 100%;
        }
    }

    &__more__wrapper {
        display: flex;
        justify-content: flex-end;
    }

    &__more {
        color: $blue;
        font-weight: 700;
        margin-top: 5px;
        margin-bottom: 5px;
        display: flex;
        align-items: center;

        .is-parent-ref & {
            color: white;
        }

        &:hover {
            span {
                text-decoration: underline;
            }
        }

        .arrow {
            position: relative;
            width: 15px;
            height: 15px;
            border: 1px solid $blue;
            border-radius: 50%;
            display: inline-block;
            margin-left: 3px;

            .is-parent-ref & {
                border-color: white;
            }

            &:before,
            &:after {
                content: '';
                width: 5px;
                height: 2px;
                background: $blue;
                position: absolute;
                left: 4px;
                top: 4px;
                transform: rotate(45deg);

                .is-parent-ref & {
                    background: white;
                }
            }

            &:after {
                top: 7px;
                transform: rotate(-45deg);
            }
        }
    }

    &__actions {
        display: flex;
        margin-top: 10px;
        justify-content: space-between;
        align-items: center;
        flex: 0 0 auto;
        flex-wrap: wrap;

        & > div {
            display: flex;
        }

        &--main,
        &--side {
            margin-bottom: 5px;
        }

        &--main button {
            background: $red;

            &.active,
            &:hover {
                color: $red;
                background: white;
                border-color: $red;
            }

            &.extended-view-switcher {
                background: $blue;

                i {
                    font-size: 26px;
                }

                &.active,
                &:hover {
                    color: $blue;
                    background: white;
                    border-color: $blue;
                }
            }
        }

        &--side button {
            background: $yellow;
            position: relative;

            &.user-contributed-drill-down {
                &:after {
                    content: '';
                    width: 8px;
                    height: 8px;
                    border: 2px solid white;
                    position: absolute;
                    left: 50%;
                    top: 50%;
                    transform: translate(-75%, -50%) rotate(45deg);
                    border-bottom: 0;
                    border-left: 0;
                    transition: 0.2s ease all;
                }
            }

            .icon-small {
                position: absolute;
                bottom: -2px;
                right: -4px;
                background: #fff;
                color: $yellow;
                border-radius: 50%;
                padding: 3px;
                font-size: 10px;
                border: 1px solid $yellow;
                transition: all 250ms ease-in-out;
            }

            &.open,
            &:hover {
                background: white;
                border-color: $yellow;
                color: $yellow;

                .icon-small {
                    color: white;
                    background: $yellow;
                    transition: all 250ms ease-in-out;
                }

                &.user-contributed-drill-down {
                    &:after {
                        border-color: $yellow;
                    }
                }
            }

            &.single-layer-active {
                background: white;
                border-color: $yellow;
                color: $yellow;
            }

            &[disabled] {
                .icon-small {
                    color: $grey;
                    background: white;
                    border-color: $grey;
                }

                &.user-contributed-drill-down {
                    &:after {
                        border-color: white;
                    }
                }
            }

            &.active {
                color: $green;
                background: white;
                border-color: $green;
            }
        }

        button {
            width: 32px;
            height: 32px;
            border-radius: 50%;
            color: white;
            font-size: 18px;
            display: flex;
            align-items: center;
            justify-content: center;
            transition: all 250ms ease-in-out;
            border: 1px solid transparent;

            & + button {
                margin-left: 10px;
            }
        }

        &-workflow {
            position: relative;

            &:before {
                content: '';
                border-left: 10px solid white;
                border-top: 8px solid transparent;
                border-bottom: 8px solid transparent;
                position: absolute;
                top: 7px;
                left: 11px;
            }

            &:hover:before {
                border-left-color: $yellow;
            }
        }

        .icomoon-drill-down {
            position: relative;
            left: 3px;
        }

        .icomoon-workflow-run {
            position: relative;
            left: 2px;
        }
    }

    &__drill {
        position: absolute;
        right: 0;
        top: 0;
        width: 50px;
        background: #f3f3f3;
        bottom: 0;
        box-shadow: 0 0 30px rgba(0, 0, 0, 0.05) inset;
        transition: 0.2s ease all;

        &:after {
            content: '';
            width: 14px;
            height: 14px;
            border: 2px solid #ccc;
            position: absolute;
            left: 50%;
            top: 50%;
            -webkit-transform: rotate(45deg);
            transform: translate(-66%, -50%) rotate(45deg);
            border-bottom: 0;
            border-left: 0;
            transition: 0.2s ease all;
        }

        &:hover {
            transition: 0.2s ease all;
            background: #e8e8e8;

            &:after {
                border-color: #aaa;
                transition: 0.2s ease all;
            }
        }

        &.up {
            &:after {
                transform: rotate(-45deg);
            }
        }
    }

    &__downloads,
    &__share {
        width: 100%;

        & > div {
            width: 100%;
        }

        &-wrapper {
            & > div {
                margin-top: 10px;
                width: 100%;
                display: flex;
                border-top: 1px solid #ddd;
                justify-content: flex-end;
                padding: 10px 5px 0;

                > span {
                    @media (max-width: $breakpoint-sm) {
                        width: 100%;
                    }
                }
            }
        }
    }

    &__downloads {
        a,
        button {
            background: $green;
            border-radius: 16px;
            color: white;
            cursor: pointer;
            margin-left: 10px;
            min-height: 32px;
            max-height: 32px;
            width: 32px;
            display: flex;
            justify-content: space-around;
            border: 1px solid transparent;
            display: flex;
            align-items: center;

            .dab-result-details__file-icon {
                font-size: 21px;
                position: relative;
                top: -1px;
            }

            &.expandable {
                overflow: hidden;
                transition: width 450ms ease-in-out;

                .dab-result-details__file-icon {
                    border-radius: 50%;
                    height: 32px;
                    width: 32px;

                    &::before {
                        left: 5px;
                        position: absolute;
                        top: 6px;
                    }
                }

                &.expanded {
                    color: $green;
                    cursor: default;
                    background: white;
                    text-decoration: none;
                    width: auto;
                    border: none !important;

                    &.expandable-on-click {
                        border: 1px solid $green !important;
                        width: 340px;

                        @media (max-width: $breakpoint-sm) {
                            max-height: none;
                            flex-direction: column;
                            gap: 5px;
                            width: 100%;
                            padding: 5px;
                            margin: 0;
                        }
                    }

                    .dab-result-details__file-icon {
                        &::after {
                            border-right: 1px solid $green;
                            border-top: 1px solid $green;
                            content: ' ';
                            height: 7px;
                            position: absolute;
                            right: 0px;
                            top: 13px;
                            transform: rotate(45deg);
                            width: 7px;
                        }

                        @media (max-width: $breakpoint-sm) {
                            display: none !important;
                        }

                        &:hover {
                            &::after {
                                border-color: $green-dark;
                            }
                        }
                    }

                    .dab-result-details__file-icon,
                    .dab-result-details__direct-download-button,
                    .dab-result-details__bulk-download-button {
                        cursor: pointer;
                        display: flex;
                        white-space: nowrap;
                        margin-right: 10px;

                        &:hover {
                            color: $green-dark;

                            .plus-icon {
                                border: 2px solid $green-dark;

                                &::before,
                                &::after {
                                    background: $green-dark;
                                }
                            }

                            .arrow-circled {
                                color: $green;
                            }

                            .expandable-on-click {
                                .arrow-circled {
                                    border: 2px solid $green-dark;
                                }
                            }
                        }

                        &.disabled {
                            color: $grey-dark;
                            cursor: default;

                            .plus-icon {
                                border: 2px solid $grey-dark;

                                &::before,
                                &::after {
                                    background: $grey-dark;
                                }
                            }

                            .bulk-download__icon {
                                border: 2px solid $grey-dark;
                                background: url('/svg/bulk-download-2-disabled.svg')
                                    center center no-repeat;
                            }
                        }
                    }
                }
            }

            &:hover {
                color: $green;
                background: white;
                border: 1px solid $green;
                text-decoration: none;
            }
        }
    }

    &__direct-download-button,
    &__bulk-download-button {
        display: none;
        align-items: center;
    }

    .plus-icon {
        border: 2px solid $green;
        border-radius: 50%;
        display: inline-block;
        height: 18px;
        margin-right: 3px;
        position: relative;
        top: -1px;
        transform: scale(0.8);
        width: 18px;

        &::before,
        &::after {
            background: $green;
            content: '';
            height: 2px;
            left: 3px;
            position: absolute;
            top: 6px;
            width: 8px;
        }

        &::after {
            transform: rotate(90deg);
        }
    }

    .bulk-download__icon {
        background: url('/svg/bulk-download-2.svg') center center no-repeat;
        border: 2px solid $green;
        border-radius: 50%;
        display: inline-block;
        height: 24px;
        margin-right: 3px;
        position: relative;
        width: 24px;
        background-size: 16px;
    }

    .expandable-on-click {
        .bulk-download__icon {
            height: 18px;
            transform: scale(0.8);
            width: 18px;
            background-size: 12px;
        }
    }

    .arrow-circled {
        border: 2px solid $green;
        border-radius: 50%;
        display: inline-block;
        height: 24px;
        margin-right: 3px;
        position: relative;
        width: 24px;

        &::before {
            font-size: 14px;
            left: 3px;
            position: absolute;
            top: 4px;
        }
    }

    .expandable-on-click {
        .arrow-circled {
            height: 18px;
            transform: scale(0.8);
            width: 18px;

            &::before {
                font-size: 10px;
                left: 2px;
                top: 3px;
            }
        }
    }
}

.extended-view-mode {
    .dab-result-details {
        width: 100%;

        &__wrapper {
            > div {
                height: 330px;
                padding-top: 0;
            }
        }

        &__title {
            font-size: 18px;
        }
    }
}
</style>
