<template>
        <div class="filters insitu-filters" :class="{'only-advanced': onlyAdvanced}">
        <div class="insitu-filters__wrapper">
            <CustomSelect
                class="insitu-filters__filter cropType"
                @input="oncropTypesChange($event)"
                :value="cropTypes"
                :options="cropTypesOptions"
                :multiple="true"
                :filterable="true"
                textProp="text"
                idProp="id"
                :placeholder="$tc('inSituFilters.cropTypes')"
                :appendToBody="appendToBody"
            />
            <CustomSelect
                class="insitu-filters__filter landCoverType"
                @input="onLandCoverChange($event)"
                :value="landCoverTypes"
                :options="landCoverTypesOptions"
                :multiple="true"
                :filterable="true"
                textProp="text"
                idProp="id"
                :placeholder="$tc('inSituFilters.landCoverTypes')"
                :appendToBody="appendToBody"
            />
            <CustomSelect
                class="insitu-filters__filter irrigationType"
                @input="onirrigationTypesChange($event)"
                :value="irrigationTypes"
                :options="irrigationTypesOptions"
                :multiple="true"
                :filterable="true"
                textProp="text"
                idProp="id"
                :placeholder="$tc('inSituFilters.irrigationTypes')"
                :appendToBody="appendToBody"
            />
            <div class="slider-section-container">
                <div class="slider-container cropConfidence">
                    <div class="insitu-filters__title">
                        {{ $tc('inSituFilters.cropConfidence') }}:
                    </div>
                    <vue-slider
                        v-model="cropConfidence"
                        :enable-cross="false"
                        @drag-end="getResults()"
                    ></vue-slider>
                </div>
                <div class="slider-container landCoverConfidence">
                    <div class="insitu-filters__title">
                        {{ $tc('inSituFilters.landCoverConfidence') }}:
                    </div>
                    <vue-slider
                        v-model="landCoverConfidence"
                        :enable-cross="false"
                        @drag-end="getResults()"
                    ></vue-slider>
                </div>
                <div class="slider-container irrigationConfidence">
                    <div class="insitu-filters__title">
                        {{ $tc('inSituFilters.irrigationConfidence') }}:
                    </div>
                    <vue-slider
                        v-model="irrigationConfidence"
                        :enable-cross="false"
                        @drag-end="getResults()"
                    ></vue-slider>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
// @ts-nocheck
import { Component, Vue, Watch, Prop } from 'nuxt-property-decorator'

import GeossDataCoreCheckbox from '@/components/Search/GeneralFilters/GeossDataCoreCheckbox.vue'
import GooglePlacesSelect from '@/components/Search/GeneralFilters/GooglePlacesSelect.vue'
import BoundingBoxRelationRadio from '@/components/Search/GeneralFilters/BoundingBoxRelationRadio.vue'
import SearchFiltersTrigger from '@/components/Search/SearchFiltersTrigger.vue'
import DateSlider from '@/components/Search/DateSlider.vue'
import ExtendedCustomSelect from '@/components/ExtendedCustomSelect.vue'
import { Source, View } from '@/interfaces/GeneralFilters'
import { CountriesAndContinents } from '@/data/general-filters'
import { SearchGetters } from '@/store/search/search-getters'
import { DataSources } from '@/interfaces/DataSources'
import { InSituFiltersActions } from '@/store/inSituFilters/inSitu-filters.actions'
import { InSituFiltersGetters } from '@/store/inSituFilters/inSitu-filters.getters'
import VueSlider from 'vue-slider-component'
import { SearchActions } from '@/store/search/search-actions'
import { GeneralFiltersGetters } from '@/store/generalFilters/general-filters-getters'

@Component({
    components: {
        GeossDataCoreCheckbox,
        GooglePlacesSelect,
        BoundingBoxRelationRadio,
        SearchFiltersTrigger,
        DateSlider,
        ExtendedCustomSelect,
        VueSlider,
    },
})
export default class InSituFiltersComponent extends Vue {
    @Prop({type: Boolean, required: true}) public onlyAdvanced!: boolean;

    public DataSources = DataSources
    public sourceOptions: Source[] = []
    public viewOptions: View[] = []
    public countriesAndContinents = CountriesAndContinents
    public lastState = null

    public minYear = new Date().getFullYear() - 20
    public maxYear = new Date().getFullYear()

    get cropConfidence() {
        return this.$store.getters[InSituFiltersGetters.cropConfidence]
    }

    set cropConfidence([start, end]) {
        this.$store.dispatch(InSituFiltersActions.setCropConfidence, [
            start,
            end,
        ])
    }

    get landCoverConfidence() {
        return this.$store.getters[InSituFiltersGetters.landCoverConfidence]
    }

    set landCoverConfidence([start, end]) {
        this.$store.dispatch(InSituFiltersActions.setLandCoverConfidence, [
            start,
            end,
        ])
    }

    get irrigationConfidence() {
        return this.$store.getters[InSituFiltersGetters.irrigationConfidence]
    }

    set irrigationConfidence([start, end]) {
        this.$store.dispatch(InSituFiltersActions.setIrrigationConfidence, [
            start,
            end,
        ])
    }

    get cropTypes() {
        return this.$store.getters[InSituFiltersGetters.cropTypes]
    }

    set cropTypes(value: string) {
        this.$store.dispatch(InSituFiltersActions.setCropTypes, value)
    }

    get cropTypesOptions() {
        return this.prepareLabels(this.$store.getters[InSituFiltersGetters.cropTypesOptions])
    }

    get landCoverTypes() {
        return this.$store.getters[InSituFiltersGetters.landCoverTypes]
    }

    set landCoverTypes(value: string) {
        this.$store.dispatch(InSituFiltersActions.setLandCoverTypes, value)
    }

    get landCoverTypesOptions() {
        return this.prepareLabels(this.$store.getters[InSituFiltersGetters.landCoverTypesOptions])
    }

    get irrigationTypes() {
        return this.$store.getters[InSituFiltersGetters.irrigationTypes]
    }

    set irrigationTypes(value: string) {
        this.$store.dispatch(InSituFiltersActions.setIrrigationTypes, value)
    }

    get irrigationTypesOptions() {
        return this.prepareLabels(this.$store.getters[InSituFiltersGetters.irrigationTypesOptions])
    }

    get appendToBody() {
        return this.containerVisible || this.generalFiltersInChange
    }

    get currentResults() {
        return this.$store.getters[SearchGetters.currentResults]
    }

    get generalFiltersInChange() {
		return this.$store.getters[GeneralFiltersGetters.inChangeProcess];
	}

    public prepareLabels(array: any[]) {
        return array.map((element: { id: string; text: string }) => {
            return {
                ...element,
                text: element.text.replaceAll('_', ' ')
            }
        })
    }

    public oncropTypesChange(value: string[]) {
        this.$store.dispatch(InSituFiltersActions.setCropTypes, value)
    }

    public onLandCoverChange(value: string[]) {
        this.$store.dispatch(InSituFiltersActions.setLandCoverTypes, value)
    }

    public onirrigationTypesChange(value: string[]) {
        this.$store.dispatch(InSituFiltersActions.setIrrigationTypes, value)
    }

    public getResults() {
        if (!this.generalFiltersInChange) {
			this.$store.dispatch(SearchActions.getResults);
		}
    }

    private updateFilterOptions(storeAction: string, filter: string) {
        const options = []

        const entries = this.currentResults['entry'] || []
        entries.forEach((entry: any) => {
            if (entry[`${filter}`] !== undefined) {
                if (entry[`${filter}`]?.label instanceof Array) {
                    entry[`${filter}`].label.forEach((label: any) => {
                        if (
                            !options.some(
                                (option) => option.id === label._attributes.code
                            )
                        ) {
                            options.push({
                                text: label._text,
                                id: label._attributes.code,
                            })
                        }
                    })
                } else {
                    if (
                        !options.some(
                            (option) =>
                                option.id ===
                                entry[`${filter}`].label._attributes.code
                        )
                    ) {
                        options.push({
                            text: entry[`${filter}`].label._text,
                            id: entry[`${filter}`].label._attributes.code,
                        })
                    }
                }
            }
        })
        options.sort((a, b) => {
            if (a.text > b.text) return 1
            if (a.text < b.text) return -1
            return 0
        })
        this.$store.dispatch(storeAction, options)
    }

    @Watch('currentResults')
    private async onCurrentResultsChanged() {
        if (this.currentResults && this.currentResults['entry'].length) {
            this.updateFilterOptions(
                InSituFiltersActions.setCropTypesOptions,
                'cropTypes'
            )
            this.updateFilterOptions(
                InSituFiltersActions.setLandCoverTypesOptions,
                'landCoverTypes'
            )
            this.updateFilterOptions(
                InSituFiltersActions.setIrrigationTypesOptions,
                'irrigationTypes'
            )
        }
    }

    @Watch('cropTypes')
    private async onCropTypesChange() {
        this.getResults();
    }

    @Watch('landCoverTypes')
    private async onLandCoversChange() {
        this.getResults();
    }

    @Watch('irrigationTypes')
    private async onIrrigationChange() {
        this.getResults();
    }
}
</script>

<style lang="scss">
.insitu-filters {
    background: $blue-transparent;
    position: relative;

    &.only-advanced {
        display: none;
        background: $blue;
    }

    .slider-section-container {
        display: flex;
        flex-direction: row;
        width: 100%;
        gap: 15px;
        order: 3;

        @media (max-width: $breakpoint-sm) {
            flex-direction: column;
            gap: 0px;
        }
    }

    .slider-container {
        width: calc(33% - 20px);
        margin: 15px auto;

        @media (max-width: $breakpoint-sm) {
            width: 100%;
        }
    }

    /* Slider styling */
    .vue-slider {
        margin-left: 0;
        width: 100% !important;
    }

    /* Handle (dot) styling */
    .vue-slider-dot {
        width: 20px;
        height: 20px;
        background-color: white;
        border: 2px solid rgb(228, 228, 228);
        border-radius: 50%;
    }

    .vue-slider-dot-tooltip-inner {
        border: 2px solid white;
        border-radius: 15px;
        color: white;
        background: $blue;
        padding: 5px 10px;
    }

    .vue-slider-rail {
        background-color: #e0b318;
    }

    .vue-slider-process {
        background-color: white;
    }

    &__title {
        margin-bottom: 8px;
        font-size: 14px;
        color: white;
    }

    &__wrapper {
        background-color: transparent !important;
        padding-left: 0 !important;
        padding-right: 0 !important;
    }

    &__filter {
        width: calc(33% - 13px);
        margin-bottom: 5px;

        @media (max-width: $breakpoint-sm) {
            width: 100%;
            order: 0;

            &.cropType {
                order: 0;
            }

            &.landCoverType {
                order: 1;
            }

            &.irrigationType {
                order: 2;
            }
        }
    }

    .custom-select__container {
        max-height: 170px;
    }
}
</style>
