<template>
    <div>
        <div class="search-container" :class="{ 'active': containerActive, 'container-hidden': containerFullyHidden, 'in-transition': containerInTransition }">
            <DraggableResizable :handles="['br']" :disabled="dragResizeDisabled" :min-height="300" :min-width="500" :resizable="searchResultsActive"
                className="search-container__wrapper" parent="body" :offset-top="90" :offset-right="searchResultsActive ? 90 : 0"
                :class="{'with-results': searchResultsActive}"
                drag-handle=".drag-handle">
                <button
                    class="search-container-toggler"
                    :class="{ 'container-fully-hidden': containerFullyHidden }"
                    @click="toggleSearchContainer()"
                    :data-tutorial-tag="containerFullyHidden ? 'search-container-show' : 'search-container-hide'">
                    <span v-if="containerFullyHidden && searchResultsActive">{{ $tc('general.searchResults') }}</span>
                    <span v-if="containerFullyHidden && !searchResultsActive">{{ $tc('searchBar.search') }}</span>
                    <span v-if="!containerFullyHidden" class="search-container-toggler__arrow"></span>
                </button>
                <div slot="handle" class="resize-icon">
                    <span></span>
                </div>
                <div :class="{'d-flex flex--wrap': !searchResultsActive}">
                    <DataSourcesComponent v-show="searchResultsActive" :class="{'disable-container': generalFiltersInChange}" />
                    <SearchBar :class="{'disable-container': generalFiltersInChange}" />
                </div>
                <div ref="main-container" v-bar>
                    <div>
                        <div :class="{'filters-sticky-margin': filtersSticky}">
                            <SearchFilters :class="{'filters-sticky': filtersSticky}" />
                            <SearchResultDabDetails :class="{'disable-container': generalFiltersInChange}" :separated="true" v-if="parentRef" :result="parentRef.entry" :index="0" :image="getImage(parentRef.entry.logo)" />
                            <Workflow v-if="workflow && !workflowMapDraw" :workflow="workflow"/>
                            <CrRelations v-if="crRelationSrc"/>
                            <div v-if="!workflowMapDraw" :class="{'disable-container': generalFiltersInChange}" v-show="searchResultsActive" class="search-container__results">
                                <component :is="currentResultsContainer"></component>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="search-container__footer">
                    <SeeOtherSources :class="{'disable-container': generalFiltersInChange}" />
                    <PoweredBy v-if="isWidget" :class="{'disable-container': generalFiltersInChange}" v-show="searchResultsActive" />
                    <SearchResultsPagination :class="{'disable-container': generalFiltersInChange}" v-show="searchResultsActive" />
                </div>
                <SeeAlso v-if="windowWidth < 1200" />
            </DraggableResizable>
        </div>
        <SeeAlso v-if="windowWidth >= 1200" />
    </div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from 'nuxt-property-decorator';

import SearchBar from '@/components/Search/SearchBar.vue';
import SearchFilters from '@/components/Search/Filters.vue';
import SearchResultsDab from '@/components/Search/Results/Dab.vue';
import SearchResultsPagination from '@/components/Search/SearchResultsPagination.vue';
import DataSourcesComponent from '@/components/Search/DataSources.vue';
import DraggableResizable from '@/components/DraggableResizable.vue';
import SearchResultDabDetails from '@/components/Search/Results/ResultDetails.vue';
import SearchResultsData from '@/components/Search/Results/Data.vue';
import SearchResultsAmerigeoss from '@/components/Search/Results/Amerigeoss.vue';
import SearchResultsNextgeoss from '@/components/Search/Results/Nextgeoss.vue';
import SearchResultsInformation from '@/components/Search/Results/Information.vue';
import SearchResultsZenodo from '@/components/Search/Results/Zenodo.vue';
import SearchResultsWikipedia from '@/components/Search/Results/Wikipedia.vue';
import SearchResultsServices from '@/components/Search/Results/Services.vue';
import Workflow from '@/components/Search/Workflow.vue';
import CrRelations from '@/components/Search/CrRelations.vue';
import PoweredBy from '@/components/Search/PoweredBy.vue';
import SeeOtherSources from '@/components/Search/SeeOtherSources.vue';
import SeeAlso from '@/components/Search/SeeAlso.vue';

import { SearchGetters } from '@/store/search/search-getters';
import { FacetedFiltersGetters } from '@/store/facetedFilters/faceted-filters-getters';
import { GranulaFiltersGetters } from '@/store/granulaFilters/granula-filters-getters';
import { IrisFiltersGetters } from '@/store/irisFilters/iris-filters-getters';
import { SearchActions } from '@/store/search/search-actions';
import { GeneralFiltersGetters } from '@/store/generalFilters/general-filters-getters';
import UtilsService from '@/services/utils.service';
import { MapGetters } from '@/store/map/map-getters';
import { DataSources } from '@/interfaces/DataSources';
import { MapActions } from '@/store/map/map-actions';
import MapCoordinatesUtils from '@/services/map/coordinates-utils';
import { ResultService } from '@/services/result.service';
import { LayerData } from '@/interfaces/LayerData';
import { GeneralFiltersActions } from '@/store/generalFilters/general-filters-actions';
import { GeneralGetters } from '@/store/general/general-getters';
import TutorialTagsService from '@/services/tutorial-tags.service';

@Component({
    components: {
        SearchBar,
        SearchFilters,
        SearchResultsPagination,
        DataSourcesComponent,
        DraggableResizable,
        SearchResultDabDetails,
        SearchResultsDab,
        SearchResultsData,
        SearchResultsAmerigeoss,
        SearchResultsNextgeoss,
        SearchResultsInformation,
        SearchResultsZenodo,
        SearchResultsWikipedia,
        SearchResultsServices,
        Workflow,
        CrRelations,
        SeeOtherSources,
        PoweredBy
    }
})
export default class SearchContainerComponent extends Vue {
    [x: string]: any;
    public hideAnimationTime = 450;
    public dataSources = DataSources;
    public windowWidth = 0;
    public containerActive = true;
    public containerFullyHidden = false;
    public containerInTransition = false;

    public dragResizeDisabled = true;

    public filtersSticky = false;

    private scrollableContainer: any = null;

    get isWidget() {
        return this.$store.getters[GeneralGetters.isWidget];
    }

    get currentResultsContainer() {
        return this.dataSource ? `search-results-${this.dataSource}` : 'search-results-dab';
    }

    get workflow() {
        return this.$store.getters[SearchGetters.workflow];
    }

    get workflowMapDraw() {
        return this.$store.getters[GeneralFiltersGetters.state].workflowMapDraw;
    }

    get crRelationSrc() {
        return this.$store.getters[SearchGetters.crRelation] && this.$store.getters[SearchGetters.crRelation].srcEntry;
    }

    get dataSource(): DataSources {
        return this.$store.getters[SearchGetters.dataSource];
    }

    get showFullMap() {
        return this.$store.getters[MapGetters.showFull];
    }

    get hideSearching() {
        return this.$store.getters[MapGetters.hideSearching];
    }

    get searchResultsActive() {
        return this.$store.getters[SearchGetters.resultsActive];
    }

    get parentRef() {
        return this.$store.getters[SearchGetters.parentRef];
    }

    get facetedFiltersAvailable() {
        return this.$store.getters[FacetedFiltersGetters.facetedFiltersAvailable];
    }

    get granulaFiltersAvailable() {
        return this.$store.getters[GranulaFiltersGetters.granulaFiltersAvailable];
    }

    get irisFiltersAvailable() {
        return this.$store.getters[IrisFiltersGetters.irisFiltersAvailable];
    }

    get resultIdDetails() {
        return this.$store.getters[SearchGetters.resultIdDetails];
    }

    get clickedLayerId() {
        return this.$store.getters[MapGetters.clickedLayerId];
    }

    get generalFiltersInChange() {
        return this.$store.getters[GeneralFiltersGetters.inChangeProcess];
    }

    get currentResults() {
        return this.$store.getters[SearchGetters.currentResults];
    }

    public toggleSearchContainer() {
        this.$store.dispatch(MapActions.setHideSearching, !this.hideSearching);
    }

    @Watch('hideSearching')
    private async onHideSearchingChange() {
        this.containerInTransition = true;
        await this.$nextTick();
        this.containerActive = !this.hideSearching;
        if (!this.containerActive) {
            setTimeout(() => {
                this.containerFullyHidden = true;
            }, this.hideAnimationTime);
        } else {
            this.containerFullyHidden = false;
            setTimeout(() => {
                this.containerInTransition = false;
            }, this.hideAnimationTime);
        }

        TutorialTagsService.refreshTagsGroup('search-container', false);
        TutorialTagsService.refreshTagsGroup('searchbar', false);
        TutorialTagsService.refreshTagsGroup('filters', false);
        TutorialTagsService.refreshTagsGroup('result', false);

        TutorialTagsService.refreshTagsAll();
    }

    // @Watch('resultIdDetails')
    // private onResultIdDetails() {
    // 	if (this.resultIdDetails) {
    // 		const targetElem = document.querySelector(`[data-id="${this.resultIdDetails}"]`) as HTMLElement;
    // 		if (targetElem) {
    // 			if (this.$store.getters[GeneralFiltersGetters.containerVisible]) {
    // 				this.$store.dispatch(GeneralFiltersActions.setContainerVisible, false);
    // 			}
    // 			setTimeout(() => {
    // 				if (targetElem.offsetHeight + targetElem.offsetTop > this.scrollableContainer.offsetHeight + this.scrollableContainer.scrollTop) {
    // 					UtilsService.scrollY(this.scrollableContainer, targetElem.offsetHeight + targetElem.offsetTop - this.scrollableContainer.offsetHeight, 40000);
    // 				}
    // 			}, Timers.collapseTransition + 100);
    // 		}
    // 	}
    // }

    @Watch('clickedLayerId')
    private onClickedLayerId() {
        if (this.clickedLayerId) {
            const targetElem = document.querySelector(`[data-layer-id="${this.clickedLayerId}"]`) as HTMLElement;
            if (targetElem) {
                if (this.$store.getters[GeneralFiltersGetters.containerVisible]) {
                    this.$store.dispatch(GeneralFiltersActions.setContainerVisible, false);
                }
                if (targetElem.offsetHeight + targetElem.offsetTop > this.scrollableContainer.offsetHeight + this.scrollableContainer.scrollTop) {
                    UtilsService.scrollY(this.scrollableContainer, targetElem.offsetHeight + targetElem.offsetTop - this.scrollableContainer.offsetHeight, 40000);
                } else if (targetElem.offsetTop < this.scrollableContainer.scrollTop) {
                    UtilsService.scrollY(this.scrollableContainer, targetElem.offsetTop, 40000);
                }
            }
        }
    }

    get aoiLayer() {
        return this.$store.getters[MapGetters.layers].filter((layerData: LayerData) =>
            layerData.id === 'area-selected'
        );
    }

    @Watch('searchResultsActive')
    private onResultsActiveChange() {
        if (this.aoiLayer[0]) {
            setTimeout(() => {
                this.$store.dispatch(MapActions.centerMap, this.aoiLayer[0].coordinate);
            }, 0);
        }
    }

    private checkDragResizeEnabled() {
        const media = window.matchMedia('(max-width: 992px)');
        this.dragResizeDisabled = media.matches;

        window.addEventListener('resize', () => {
            this.dragResizeDisabled = media.matches;
        });
    }

    private getWindowWidth() {
        this.windowWidth = document.documentElement.clientWidth;
    }

    private mounted() {
        this.checkDragResizeEnabled();

        const generalFiltersNonEmpty = this.$store.getters[GeneralFiltersGetters.isNonEmpty];
        const phraseExists = !!(this.$store.getters[GeneralFiltersGetters.state].phrase);
        const folderToLoadExists = !!(this.$store.getters[SearchGetters.parentRefs].length);
        const bookmarkToLoadExists = UtilsService.getUrlParam('targetId');

        if ((generalFiltersNonEmpty || phraseExists || folderToLoadExists || bookmarkToLoadExists) && !this.$store.getters[SearchGetters.dabResults]) {
            this.$store.dispatch(SearchActions.getResults, { replaceHistory: false });
        }

        const mainContainer = this.$refs['main-container'] as HTMLElement;
        this.scrollableContainer = mainContainer.querySelector('.vb-content') as HTMLElement;

        const heightLess1000 = window.matchMedia('(max-height: 1000px)');

        this.scrollableContainer.addEventListener('scroll', () => {
            if (heightLess1000.matches) {
                this.filtersSticky = false;
            } else {
                if (this.scrollableContainer.scrollTop !== 0) {
                    this.filtersSticky = true;
                    this.$store.dispatch(GeneralFiltersActions.setContainerVisible, false);
                } else {
                    this.filtersSticky = false;
                }
            }
            TutorialTagsService.refreshTagsGroup('result', true, 0);
        });

        this.onHideSearchingChange();

        this.$nextTick(() => {
            window.addEventListener('resize', this.getWindowWidth);
            this.getWindowWidth();
        });
    }

    private beforeDestroy() {
        window.removeEventListener('resize', this.getWindowWidth);
    }

    @Watch('currentResults')
    private onDabResultsChanged() {
        this.scrollableContainer.scrollTop = 0;

        this.$store.dispatch(MapActions.removeBoundingLayers);

        if (this.currentResults && this.currentResults.entry) {
            this.currentResults.entry.forEach((item: any, index: number) => {
                const layer = ResultService.getFeature(item, index);

                const { boxes, pins } = ResultService.getBoundingBoxesAndPins(item);
                const outerBox = MapCoordinatesUtils.mergeBoxes(boxes);
                const outerPin = MapCoordinatesUtils.mergeBoxes(pins);
                let coordinate = null;
                if (outerBox) {
                    coordinate = {
                        W: outerBox[0],
                        S: outerBox[1],
                        E: outerBox[2],
                        N: outerBox[3]
                    };
                } else if (outerPin) {
                    coordinate = {
                        W: outerPin[0],
                        S: outerPin[1],
                        E: outerPin[2],
                        N: outerPin[3]
                    };
                }

                if (layer) {
                    const type = layer.boundingType;
                    this.$store.dispatch(MapActions.addLayer, {
                        coordinate,
                        layer,
                        id: item.id,
                        index,
                        type
                    });
                }
            });

            this.$store.dispatch(MapActions.repaintBoudingLayers);

            this.$nextTick(() => {
                if (UtilsService.getUrlParam('targetId') && this.currentResults.entry.length === 1) {
                    const layerData = this.$store.getters[MapGetters.layers].find((layerData: LayerData) => layerData.id === this.currentResults.entry[0].id);
                    if (layerData && layerData.coordinate) {
                        this.$store.dispatch(MapActions.centerMap, layerData.coordinate);
                        this.$store.dispatch(MapActions.setClickedLayerId, this.currentResults.entry[0].id);
                    }
                }
            });
        }
    }
}
</script>

<style lang="scss" scoped>
$hideAnimationTime: 450ms;

.search-container {
    left: 0;
    top: 0;
    z-index: 5;
    width: 100vw;
    height: 100%;
    transition: left $hideAnimationTime ease-in-out;
    left: -110vw;

    &.container-hidden {
        .search-container__wrapper {
            left: 0;
            transform: none;
        }
    }

    &.in-transition {
        position: absolute;
    }

    &.active {
        left: 0;
    }

    &-toggler {
        position: absolute;
        left: -44px;
        top: 44px;
        transform: rotate(270deg);
        height: 20px;
        background: $green-dark;
        overflow: hidden;
        padding: 0 5px;
        border-radius: 10px 10px 0 0;
        width: 109px;
        text-align: center;
        color: $white;
        text-transform: uppercase;
        font-size: 0.76em;
        white-space: nowrap;
        line-height: 20px;
        cursor: pointer;

        @media (max-width: $breakpoint-lg) {
            display: none;
        }

        &:hover {
            background: $green-darker;
        }

        &.container-fully-hidden {
            border-radius: 0 0 10px 10px;
            position: fixed;
            left: -45px;
            top: auto;
            margin-top: -14px;
        }

        &__arrow {
            &:after {
                content: '';
                width: 8px;
                height: 8px;
                border: 2px solid $white;
                position: absolute;
                left: 50%;
                top: 50%;
                transform: translate(-66%, -33%) rotate(45deg);
                border-bottom: 0;
                border-right: 0;
                transition: 0.2s ease all;
            }
        }
    }

    .filters-sticky {
        position: absolute;
        left: 0;
        top: 0;
        width: 100%;
        z-index: 1;
    }

    .filters-sticky-margin {
        margin-top: 49px;
    }

    &__wrapper {
        position: absolute;
        left: 50%;
        transform: translate(-50%, -50%);
        top: 50%;
        z-index: 2 !important;
        width: 70%;
        max-width: 1000px;
        max-height: calc(100% - 200px);
        display: flex;
        flex-direction: column;
        padding-left: 24px;

        @media (max-width: $breakpoint-lg) {
            padding-left: 0;
            top: 94px;
            left: 10px;
            width: calc(100% - 20px);
            max-height: calc(100% - 200px);
            transform: none;
        }

        &.resized {
            max-width: none;
            max-height: none;
        }

        &.with-results,
        &.dragged {
            transform: none;

            .search-container-toggler {
                &.container-fully-hidden {
                    margin-top: 44px;
                }
            }
        }

        &.with-results {
            &:not(.dragged) {
                left: 30px;
                top: 100px;

                @media (max-width: $breakpoint-lg) {
                    left: 30px;
                    top: 100px;
                }

                @media (max-width: $breakpoint-md) {
                    left: 15px;
                }

                @media(max-width: $breakpoint-sm) {
                    left: 5px;
                }
            }

            &:not(.resized) {
                max-width: 800px;

                @media (max-width: $breakpoint-lg) {
                    width: calc(100% - 155px);
                    max-width: none;
                }

                @media (max-width: $breakpoint-md) {
                    width: calc(100% - 110px);
                }

                @media(max-width: $breakpoint-sm) {
                    width: calc(100% - 65px);
                }
            }
        }
    }

    &__results {
        margin-top: 5px;
    }
}
</style>

<style lang="scss">
.search-container {
    &__footer {
        display: flex;
        background-color: $green-transparent;
        margin-top: 5px;
        flex: 0 0 auto;
        flex-wrap: wrap;
        justify-content: space-between;

        @media (max-width: $breakpoint-sm) {
            display: block;
        }

        .see-other-sources {
            background-color: transparent;
            margin-top: 0;
        }

        .pagination {
            background-color: transparent;
            margin-top: 0;
        }
    }
}

.resize-icon {
    width: 20px;
    height: 20px;
    position: relative;
    display: block;

    &:before,
    &:after,
    span {
        content: '';
        background-color: white;
        width: 20px;
        height: 1px;
        transform: rotate(-45deg);
        top: 8px;
        left: 0;
        display: block;
        position: absolute;
    }

    &:after {
        width: 13px;
        top: 11px;
        left: 6px;
    }

    span {
        width: 6px;
        top: 14px;
        left: 12px;
    }
}
</style>
