<template>
    <div>
        <SearchFiltersTrigger
            :class="{
                'disable-container': currentResults && generalFiltersInChange,
            }"
            @on-section-change="activeSection = $event"
            :active-section="activeSection"
            @toggle="containerVisible = !containerVisible"
            :active="containerVisible || generalFiltersInChange"
            :only-advanced="!currentResults"
        />
        <CollapseTransition>
            <div
                class="filters-container transition-force-visible"
                v-show="containerVisible || generalFiltersInChange"
            >
                <SearchGeneralFilters
                    :only-advanced="!currentResults"
                    v-show="activeSection === 'advanced' || !currentResults"
                />
                <WorldCerealFiltersComponent v-show="inSituFiltersAvailable && activeSection !== 'advanced'" />
                <SearchFacetedFilters
                    v-show="
                        facetedFiltersAvailable && activeSection !== 'advanced'
                    "
                />
                <SearchGranulaFilters
                    v-show="
                        granulaFiltersAvailable && activeSection !== 'advanced'
                    "
                />
                <SearchIrisFilters
                    v-show="
                        irisFiltersAvailable && activeSection !== 'advanced'
                    "
                />
            </div>
        </CollapseTransition>
    </div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from 'nuxt-property-decorator'

import SearchFiltersTrigger from '@/components/Search/SearchFiltersTrigger.vue'
import SearchGeneralFilters from '@/components/Search/SearchGeneralFilters.vue'
import SearchFacetedFilters from '@/components/Search/SearchFacetedFilters.vue'
import SearchGranulaFilters from '@/components/Search/SearchGranulaFilters.vue'
import WorldCerealFiltersComponent from '@/components/Search/WorldCerealFilters.vue'
import SearchIrisFilters from '@/components/Search/SearchIrisFilters.vue'
import { FacetedFiltersGetters } from '@/store/facetedFilters/faceted-filters-getters'
import { GranulaFiltersGetters } from '@/store/granulaFilters/granula-filters-getters'
import { IrisFiltersGetters } from '@/store/irisFilters/iris-filters-getters'
import { GeneralFiltersGetters } from '@/store/generalFilters/general-filters-getters'
import { SearchGetters } from '@/store/search/search-getters'
import { GeneralFiltersActions } from '@/store/generalFilters/general-filters-actions'
import UtilsService from '@/services/utils.service'
import TutorialTagsService from '@/services/tutorial-tags.service'
import { Timers } from '@/data/timers'
import CollapseTransition from '@/plugins/CollapseTransition'
import { InSituFiltersGetters } from '~/store/inSituFilters/inSitu-filters.getters'

@Component({
    components: {
        SearchFiltersTrigger,
        SearchGeneralFilters,
        SearchFacetedFilters,
        SearchGranulaFilters,
        SearchIrisFilters,
        WorldCerealFiltersComponent,
        CollapseTransition,
    },
})
export default class SearchFiltersComponent extends Vue {
    public activeSection = 'advanced'

    get containerVisible() {
        return this.$store.getters[GeneralFiltersGetters.containerVisible]
    }

    set containerVisible(value: boolean) {
        TutorialTagsService.refreshTagsGroup(
            'filters-',
            value,
            Timers.collapseTransition
        )
        this.$store.dispatch(GeneralFiltersActions.setContainerVisible, value)
    }

    get facetedFiltersAvailable() {
        return this.$store.getters[
            FacetedFiltersGetters.facetedFiltersAvailable
        ]
    }

    get granulaFiltersAvailable() {
        return this.$store.getters[
            GranulaFiltersGetters.granulaFiltersAvailable
        ]
    }

    get irisFiltersAvailable() {
        return this.$store.getters[IrisFiltersGetters.irisFiltersAvailable]
    }

    get inSituFiltersAvailable() {
        return this.$store.getters[InSituFiltersGetters.inSituFiltersAvailable]
    }

    get currentResults() {
        return this.$store.getters[SearchGetters.currentResults]
    }

    get generalFiltersInChange() {
        return this.$store.getters[GeneralFiltersGetters.inChangeProcess]
    }

    @Watch('activeSection')
    public onActiveSectionChange() {
        TutorialTagsService.refreshTagsGroup('filters-', false)
        TutorialTagsService.refreshTagsGroup('filters-', true, 0)
    }

    @Watch('containerVisible')
    public async toggleContainerVisibility(value: boolean) {
        const heightLess700 = window.matchMedia('(max-height: 700px)')

        if (this.containerVisible && !heightLess700.matches) {
            const mainContainer = this.$parent!.$parent!.$refs[
                'main-container'
            ] as HTMLElement
            const scrollableContainer = mainContainer.querySelector(
                '.vb-content'
            ) as HTMLElement
            if (scrollableContainer.scrollTop !== 0) {
                await UtilsService.scrollY(scrollableContainer, 0, 1000)
            }
        }
    }

    @Watch('currentResults')
    private async onCurrentResultsChanged() {
        this.activeSection = 'nonadvanced'

        const heightLess700 = window.matchMedia('(max-height: 700px)')
        if (
            (this.$store.getters[
                FacetedFiltersGetters.facetedFiltersAvailable
            ] ||
                this.$store.getters[
                    GranulaFiltersGetters.granulaFiltersAvailable
                ] ||
                this.$store.getters[IrisFiltersGetters.irisFiltersAvailable] ||
                this.$store.getters[InSituFiltersGetters.inSituFiltersAvailable]) &&
            !heightLess700.matches &&
            this.currentResults
        ) {
            await this.$nextTick() // to remove empty space effect when some of the filters are hidden
            this.containerVisible = true
        } else {
            this.containerVisible = false
        }

        this.activeSection = 'nonadvanced'
    }

    @Watch('generalFiltersInChange')
    private onGeneralFiltersInChangeChange() {
        if (this.generalFiltersInChange && this.currentResults) {
            this.activeSection = 'advanced'
            this.containerVisible = true
        } else {
            // Going back in browser history case
            this.activeSection = 'nonadvanced'
            this.containerVisible = true
        }
    }
}
</script>

<style lang="scss" scoped></style>
