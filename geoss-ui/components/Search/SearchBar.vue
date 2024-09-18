<template>
    <div class="search-bar" data-tutorial-tag="searchbar">
        <div class="drag-handle" title="Move around"></div>
        <input
            data-tutorial-tag="searchbar-input"
            aria-label="phrase"
            class="search-bar__input"
            type="text"
            v-model="phrase"
            @keyup="onKeyup($event)"
            @keydown="onKeydown($event)"
            @focus="getSuggestions()"
            @blur="resetSuggestions()"
            :placeholder="$tc('placeholders.searchBar')"
        />
        <button
            data-tutorial-tag="searchbar-search"
            class="search-bar__search-trigger disabled-transparent"
            @click="search()"
            :disabled="
                (!generalFiltersNotEmpty && this.phrase === '') || !dataSource
            "
            :title="$tc('searchBar.search')"
        >
            <i class="icomoon-search"></i>
        </button>
        <span class="search-bar__separator"></span>
        <Share
            data-tutorial-tag="searchbar-share"
            class="search-bar__share-trigger disabled-transparent"
            :disabled="!searchResultsActive"
            :url="shareUrl"
        />
        <button
            data-tutorial-tag="searchbar-save"
            class="search-bar__save-search disabled-transparent"
            :disabled="!searchResultsActive || searchJustSaved"
            v-if="isSignedIn"
            @click="saveSearch()"
            :title="$tc('searchBar.saveSearch')"
        >
            <i class="icomoon-save"></i>
        </button>
        <button
            data-tutorial-tag="searchbar-clear"
            class="search-bar__clear-search-container disabled-transparent"
            :title="$tc('searchBar.clearSearch')"
            :disabled="
                !generalFiltersNotEmpty && !searchResultsActive && phrase === ''
            "
            @click="resetGeneralFiltersAndResults()"
        >
            <i class="icomoon-close"></i>
        </button>
        <div
            class="search-bar__autocomplete"
            v-show="suggestions.length || seeAlso.length"
        >
            <div class="suggestions" v-if="suggestions.length">
                <i class="icomoon-star-empty"></i>
                <b>{{ $tc('searchBar.popular') }}</b>
                <div
                    v-for="suggestion in suggestions"
                    :key="suggestion"
                    :class="{ selected: selectedSuggestion === suggestion }"
                    @mousedown="selectSuggestion(suggestion)"
                >
                    {{ suggestion }}
                </div>
            </div>
            <div class="see-also" v-if="seeAlso.length">
                <i class="icomoon-search-share"></i>
                <b>{{ $tc('searchBar.seeAlso') }}</b>
                <div
                    v-for="related in seeAlso"
                    :key="related"
                    :class="{ selected: selectedSuggestion === related }"
                    @mousedown="selectSuggestion(related)"
                >
                    {{ related }}
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
// @ts-nocheck
import { Component, Vue, Watch } from 'nuxt-property-decorator'

import ErrorPopup from '@/components/ErrorPopup.vue'
import GeneralPopup from '@/components/GeneralPopup.vue'

import { GeneralFiltersActions } from '@/store/generalFilters/general-filters-actions'
import { SearchActions } from '@/store/search/search-actions'
import GeossSearchApiService from '@/services/geoss-search.api.service'
import { GeneralFiltersGetters } from '@/store/generalFilters/general-filters-getters'
import { FacetedFiltersActions } from '@/store/facetedFilters/faceted-filters-actions'
import { GranulaFiltersActions } from '@/store/granulaFilters/granula-filters-actions'
import { IrisFiltersActions } from '@/store/irisFilters/iris-filters-actions'
import { UserGetters } from '@/store/user/user-getters'
import { SearchGetters } from '@/store/search/search-getters'
import { PopupActions } from '@/store/popup/popup-actions'
import SearchEngineService from '@/services/search-engine.service'
import UtilsService from '@/services/utils.service'
import { GeneralApiService } from '@/services/general.api.service'
import { DataSource } from '@/interfaces/DataSources'
import LogService from '@/services/log.service'
import to from '@/utils/to'
import { MyWorkspaceGetters } from '@/store/myWorkspace/my-workspace-getters'
import { FacetedFiltersGetters } from '@/store/facetedFilters/faceted-filters-getters'
import { GranulaFiltersGetters } from '@/store/granulaFilters/granula-filters-getters'
import { IrisFiltersGetters } from '@/store/irisFilters/iris-filters-getters'
import { MapGetters } from '@/store/map/map-getters'
import SpinnerService from '@/services/spinner.service'
import { Timers } from '@/data/timers'
import { InSituFiltersActions } from '~/store/inSituFilters/inSitu-filters.actions'
import { InSituFiltersGetters } from '~/store/inSituFilters/inSitu-filters.getters'

@Component
export default class SearchBarComponent extends Vue {
    public showShare = false
    public shareUrl = ''
    public suggestions = []
    public seeAlso = []
    public selectedSuggestion: any = null
    public lastPhrase = null
    public suggestionsActiveCalls = 0
    public cancelSuggestions = false

    private preventKeyup = false

    get dataSource(): DataSource {
        return this.$store.getters[SearchGetters.dataSource]
    }

    get isSignedIn() {
        return this.$auth.loggedIn
    }

    get searchResultsActive() {
        return this.$store.getters[SearchGetters.resultsActive]
    }

    get searchJustSaved() {
        return this.$store.getters[SearchGetters.searchJustSaved]
    }

    get phrase() {
        return this.$store.getters[GeneralFiltersGetters.state].phrase
    }

    set phrase(value: string) {
        this.$store.dispatch(GeneralFiltersActions.setPhrase, value)
    }

    get generalFiltersNotEmpty() {
        return this.$store.getters[GeneralFiltersGetters.isNonEmpty]
    }

    get workflow() {
        return this.$store.getters[SearchGetters.workflow]
    }

    get keyword() {
        return this.$store.getters[SearchGetters.keyword]
    }

    public replaceQuotes() {
        this.phrase = this.phrase
            .replace(/„/g, '"') // U+201E
            .replace(/”/g, '"') // U+201D
            .replace(/“/g, '"') // U+201C
            .replace(/‚/g, "'") // U+201A
            .replace(/’/g, "'") // U+2019
            .replace(/‘/g, "'") // U+2018
    }

    public async resetGeneralFiltersAndResults() {
        await this.$store.dispatch(GeneralFiltersActions.reset)
        await this.$store.dispatch(InSituFiltersActions.reset)
        await this.$store.dispatch(FacetedFiltersActions.reset)
        await this.$store.dispatch(GranulaFiltersActions.reset)
        await this.$store.dispatch(IrisFiltersActions.reset)
        await this.$store.dispatch(InSituFiltersActions.reset)

        await this.$store.dispatch(SearchActions.reset)
        await this.$nextTick()
        this.$store.dispatch(GeneralFiltersActions.setInChangeProcess, false)
        UtilsService.pushToHistory()
    }

    public onKeyup(event: KeyboardEvent) {
        if (
            this.phrase &&
            this.lastPhrase !== this.phrase &&
            event.keyCode !== 13 &&
            this.phrase.length > 1
        ) {
            this.getSuggestions()
        } else if (this.lastPhrase !== this.phrase) {
            this.suggestions = []
            this.seeAlso = []
        }

        if (this.lastPhrase !== this.phrase) {
            this.selectedSuggestion = null
        }

        this.lastPhrase = this.phrase

        if (!event || (event && event.keyCode === 13 && !this.preventKeyup)) {
            this.search(event)
        }

        this.preventKeyup = false
        this.replaceQuotes()
    }

    public search(event?: KeyboardEvent) {
        if ((this.generalFiltersNotEmpty || this.phrase) && this.dataSource) {
            this.cancelSuggestions = true
            this.replaceQuotes()
            if (event) {
                ;(event.currentTarget as HTMLInputElement).blur()
            }

            this.resetSuggestions()

            if (
                GeossSearchApiService.isIdenticalToCacheParams(
                    this.$store.getters[SearchGetters.filtersParams]
                )
            ) {
                SpinnerService.showSpinner()
                setTimeout(() => {
                    SpinnerService.hideSpinner()
                }, Timers.fakeDabRequest)
            } else {
                this.$store.dispatch(FacetedFiltersActions.reset)
                this.$store.dispatch(GranulaFiltersActions.reset)
                this.$store.dispatch(IrisFiltersActions.reset)

                this.$store.dispatch(SearchActions.setParentRefs, null)
                this.$store.dispatch(
                    GeneralFiltersActions.setInChangeProcess,
                    false
                )
                this.$store.dispatch(SearchActions.getResults, {
                    noPushToHistory: !!this.workflow
                })
            }
        }
        this.prepareShareUrl()
    }

    public async saveSearch() {
        let bbox = ''
        const { S, W, N, E } =
            this.$store.getters[GeneralFiltersGetters.state]
                .selectedAreaCoordinates
        if (S && W && N && E) {
            bbox = `${S.toFixed(13)}, ${W.toFixed(13)}, ${N.toFixed(
                13
            )}, ${E.toFixed(13)}`
        }

        const queryParams: any = {}
        const sources = this.$store.getters[GeneralFiltersGetters.state].sources
        const viewId = this.$store.getters[GeneralFiltersGetters.state].viewId
        if (sources.length) {
            queryParams.sources = sources.join(', ')
        }
        if (viewId.length) {
            queryParams.viewId = viewId
        }

        let targetId = ''
        if (
            this.$store.getters[SearchGetters.parentRef] &&
            this.$store.getters[SearchGetters.parentRef].id
        ) {
            targetId = this.$store.getters[SearchGetters.parentRef].id
        }

        let filterSet = ''
        if (
            Object.keys(this.$store.getters[FacetedFiltersGetters.saveParams])
                .length
        ) {
            filterSet = 'faceted-filters'
        } else if (
            Object.keys(this.$store.getters[GranulaFiltersGetters.saveParams])
                .length
        ) {
            filterSet = 'granula-filters'
        } else if (
            Object.keys(this.$store.getters[InSituFiltersGetters.saveParams])
                .length
        ) {
            filterSet = 'inSitu-filters'
        } else if (
            Object.keys(this.$store.getters[IrisFiltersGetters.saveParams])
                .length
        ) {
            filterSet = 'iris-filters'
        }

        const [, result] = await to(
            GeossSearchApiService.addSavedSearch({
                name: this.$store.getters[GeneralFiltersGetters.state].phrase,
                popularSearchId:
                    this.$store.getters[MyWorkspaceGetters.search]
                        .popularSearchId,
                query: this.$store.getters[GeneralFiltersGetters.state].phrase,
                fullAndOpenDataset:
                    this.$store.getters[GeneralFiltersGetters.state]
                        .geossDataCore,
                dateFrom: this.$store.getters[GeneralFiltersGetters.state]
                    .dateFrom
                    ? this.$store.getters[
                          GeneralFiltersGetters.state
                      ].dateFrom.toString()
                    : '',
                dateTo: this.$store.getters[GeneralFiltersGetters.state].dateTo
                    ? this.$store.getters[
                          GeneralFiltersGetters.state
                      ].dateTo.toString()
                    : '',
                datePeriod: this.$store.getters[GeneralFiltersGetters.state]
                    .datePeriod
                    ? this.$store.getters[
                          GeneralFiltersGetters.state
                      ].datePeriod.toString()
                    : '',
                aoiOption:
                    this.$store.getters[GeneralFiltersGetters.state]
                        .locationType,
                aoiBoundingBox: bbox,
                aoiGeolocation:
                    this.$store.getters[GeneralFiltersGetters.state]
                        .googlePlacesInput,
                aoiW3W: '', // Unused, but field still exists in DB
                aoiRelation:
                    this.$store.getters[GeneralFiltersGetters.state]
                        .boundingBoxRelation,
                cloudCoverageFrom: 0, // Unused, but field still exists in DB
                cloudCoverageTo: 0, // Unused, but field still exists in DB
                queryParams,
                folder: '', // Unused, but field still exists in DB
                targetId,
                filterParams: {
                    ...this.$store.getters[FacetedFiltersGetters.saveParams],
                    ...this.$store.getters[GranulaFiltersGetters.saveParams],
                    ...this.$store.getters[IrisFiltersGetters.saveParams],
                    ...this.$store.getters[InSituFiltersGetters.saveParams]
                },
                filterSet,
                currMap: this.$store.getters[MapGetters.activeLayerTileId],
                groupId: this.$store.getters[UserGetters.groupId],
                dataSource: this.$store.getters[SearchGetters.dataSource]
            })
        )

        if (result) {
            const props = {
                title: this.$tc('popupTitles.saveSearch'),
                subtitle: this.$tc('popupContent.saveSearchSuccess')
            }
            this.$store.dispatch(PopupActions.openPopup, {
                contentId: 'general',
                component: GeneralPopup,
                props
            })
            LogService.logSearchResult('Search saved in My Workspace')
            this.$store.dispatch(SearchActions.setSearchJustSaved, true)
        } else {
            const props = {
                title: this.$tc('popupTitles.saveSearch'),
                subtitle: this.$tc('popupContent.saveSearchFail')
            }
            this.$store.dispatch(PopupActions.openPopup, {
                contentId: 'error',
                component: ErrorPopup,
                props
            })
        }
    }

    public resetSuggestions() {
        this.selectedSuggestion = null
        this.suggestions = []
        this.seeAlso = []
        this.lastPhrase = null
    }

    public selectSuggestion(suggestion: string) {
        this.phrase = suggestion
        this.resetSuggestions()
        this.search()
    }

    public async getSuggestions() {
        if (this.phrase) {
            this.suggestionsActiveCalls++
            const [, [suggested, related]] = await to(
                LogService.getAllSuggestions(this.phrase, 4, 10)
            )
            this.suggestionsActiveCalls--
            if (!this.suggestionsActiveCalls) {
                if (!this.cancelSuggestions) {
                    if (suggested && Array.isArray(suggested)) {
                        const suggestions = suggested.map((item) => item.key)
                        if (
                            (suggestions.length !== 1 ||
                                suggestions[0] !== this.phrase) &&
                            this.$el.querySelector('input') ===
                                document.activeElement
                        ) {
                            this.suggestions = suggestions
                        }
                    }
                    if (related) {
                        this.seeAlso = related
                    }
                } else {
                    this.cancelSuggestions = false
                }
            }
        }
    }

    public onKeydown(event: KeyboardEvent) {
        let index = null
        if (event.keyCode === 38) {
            // up arrow
            index = this.selectedSuggestion
                ? this.suggestions.indexOf(this.selectedSuggestion)
                : this.suggestions.length
            index = index === 0 ? this.suggestions.length - 1 : index - 1
        } else if (event.keyCode === 40) {
            // down arrow
            index = this.selectedSuggestion
                ? this.suggestions.indexOf(this.selectedSuggestion)
                : -1
            index = index === this.suggestions.length - 1 ? 0 : index + 1
        } else if (event.keyCode === 27) {
            // esc
            this.selectedSuggestion = null
        } else if (event.keyCode === 13 && this.selectedSuggestion) {
            // enter
            this.phrase = this.selectedSuggestion
            this.preventKeyup = true
            this.search(event)
        }

        if (index !== null) {
            this.selectedSuggestion = this.suggestions[index]
            event.preventDefault()
            return false
        }
    }

    public async prepareShareUrl() {
        this.shareUrl = SearchEngineService.getShareUrl()
    }

    @Watch('keyword')
    public onKeywordChange() {
        this.phrase = this.keyword
        this.search()
    }
}
</script>

<style lang="scss" scoped>
.search-bar {
    background: rgb(255, 255, 255);
    display: flex;
    align-items: stretch;
    flex: 1 0 auto;
    position: relative;
    box-shadow: 0 0 10px 0 rgba(0, 0, 0, 0.75);
    padding-right: 10px;
    margin-bottom: 5px;

    @media (max-width: $breakpoint-lg) {
        padding-left: 0;
    }

    @media (max-width: $breakpoint-sm) {
        padding-left: 5px;
    }

    & > * {
        height: 40px;
        width: 40px;
        margin: 10px;
        transition: opacity 250ms 750ms ease-in-out;

        @media (max-width: $breakpoint-sm) {
            margin: 5px;
        }

        @media (max-width: $breakpoint-xs) {
            margin: 5px 0;
        }
    }

    .drag-handle {
        border-left: 1px solid rgba(white, 0.35);
        position: absolute;
        right: 0;
        top: 0;
        z-index: 1;
        height: 100%;
        width: 10px;
        margin: 0;
        background: rgba(white, 0.15);

        @media (max-width: $breakpoint-lg) {
            display: none;
        }

        &:hover {
            cursor: move;
            background: rgba(white, 0.35);
        }
    }

    ::v-deep(button) {
        color: #1c786f;
        font-size: 25px;
        display: flex;
        align-items: center;
        flex-direction: column;
        justify-content: center;
        &[disabled][disabled][disabled] {
            color: #636363;
        }

        i {
            margin: auto 0;
        }
    }

    &__save-search i {
        font-weight: bold;
    }

    &__input {
        flex: 1 1 auto;
        padding: 4px 6px 4px 0;
        border: none;
        background: transparent;
        display: block;
        font-size: 22px;
        outline: 0;
        color: #424242;
        font-style: italic;
        box-sizing: border-box;

        @media (max-width: $breakpoint-xs) {
            font-size: 18px;
            padding: 4px 6px 4px 10px;
        }

        &::placeholder {
            color: #636363;
        }
    }

    &__separator {
        width: 2px;
        height: 50px;
        background: #636363;
        margin: 5px 0;

        @media (max-width: $breakpoint-sm) {
            height: 40px;
        }
    }

    &__autocomplete {
        position: absolute;
        left: 0;
        top: 100%;
        width: 100%;
        background-color: white;
        z-index: 6;
        height: auto;
        margin: 0;

        @media (max-width: $breakpoint-sm) {
            padding-left: 52px;
        }

        @media (max-width: $breakpoint-xs) {
            padding-left: 38px;
        }

        > div {
            position: relative;
            padding: 10px 10px 5px 48px;
        }

        i {
            position: absolute;
            left: 18px;
            top: 15px;
            color: $yellow;
            font-size: 22px;

            @media (max-width: $breakpoint-sm) {
                left: 12px;
            }

            @media (max-width: $breakpoint-xs) {
                left: 8px;
            }
        }

        > div {
            div {
                color: '#424242';
                font-size: 19px;
                cursor: pointer;
                padding: 5px;

                &:hover,
                &.selected {
                    background-color: rgba($grey-lighter, 0.5);
                }

                @media (max-width: $breakpoint-xs) {
                    font-size: 16px;
                }
            }

            b {
                color: $green;
                text-transform: uppercase;
                width: 100%;
                padding: 10px 5px 10px;
                display: block;
                font-size: 16px;

                @media (max-width: $breakpoint-xs) {
                    font-size: 14px;
                }
            }

            &.see-also {
                display: flex;
                flex-wrap: wrap;

                div {
                    margin: 0 10px 0 0;
                }

                &:not(:only-child) {
                    padding-top: 10px;
                    margin-top: 10px;
                    border-top: 1px solid $grey-lighter;
                }
            }

            &:last-child {
                margin-bottom: 10px;
            }
        }
    }
}
</style>
