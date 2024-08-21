<template>
    <div class="filters general-filters" :class="{ 'only-advanced': onlyAdvanced }">
        <div class="general-filters__absolute-box" :class="{ stretched: generalFiltersInChange }">
            <div class="general-filters__filter--geoss-submit d-flex margin-right-15" v-if="generalFiltersInChange">
                <button @click="cancelChanges()" class="general-filters__action cancel">
                    {{ $tc('generalFilters.cancel') }}
                </button>
                <button :disabled="!isNonEmpty" @click="acceptChanges()" class="general-filters__action accept">
                    {{ $tc('generalFilters.accept') }}
                </button>
            </div>
        </div>
        <div v-if="googlePlacesApiError === 'OVER_QUERY_LIMIT'" class="general-filters__google-api-error">
            {{ getGooglePlacesApiError() }}
        </div>
        <div class="general-filters__wrapper">
            <div class="general-filters__column">
                <div class="general-filters__column__header">
                    Choose location:
                </div>
                <CustomSelect class="general-filters__filter location-type" @input="onLocationTypeChanged($event)"
                    :value="locationType" :options="availableLocationTypeOptions" :appendToBody="appendToBody"
                    :clearable="false" data-tutorial-tag="filters-general-location-type">
                    <i slot="icon" class="icomoon-earth"></i>
                </CustomSelect>
                <GooglePlacesSelect v-show="generalFilters.locationType === 'geolocation'"
                    @input="onGooglePlacesSelect($event)" class="general-filters__filter geolocation"
                    data-tutorial-tag="filters-general-geolocation" />
                <CustomSelect v-show="generalFilters.locationType === 'continent_country'"
                    class="general-filters__filter location" @input="onContryOrContinentChanged($event)"
                    :value="selectedAreaCoordsConcat" :options="countriesAndContinents" textProp="title"
                    idProp="coordinates" :appendToBody="appendToBody" :filterable="true"
                    :placeholder="$t('placeholders.selectContinentOrCountry')"
                    data-tutorial-tag="filters-general-continent-country">
                    <i slot="icon" class="icomoon-all-directions-arrows"></i>
                </CustomSelect>
                <div class="general-filters__filter general-filters__filter--coordinates location"
                    v-show="generalFilters.locationType === 'coordinates'"
                    data-tutorial-tag="filters-general-coordinates">
                    <div class="coordinates-inputs">
                        <input type="number" v-model="coordinates.W" @change="onCoordinatesChange('W', $event)"
                            min="-180" max="180" step="any" placeholder="First longitude" />
                        <input type="number" v-model="coordinates.S" @change="onCoordinatesChange('S', $event)"
                            min="-90" max="90" step="any" placeholder="First latitude" />
                        <input type="number" v-model="coordinates.E" @change="onCoordinatesChange('E', $event)"
                            min="-180" max="180" step="any" placeholder="Second longitude" />
                        <input type="number" v-model="coordinates.N" @change="onCoordinatesChange('N', $event)"
                            min="-90" max="90" step="any" placeholder="Second latitude" />
                    </div>
                    <div class="coordinates-buttons">
                        <button @click="clearCoordinates()" :class="{ active: isCoordinateFilled() }">
                            ✕ {{ $tc('generalFilters.cancel') }}
                        </button>
                    </div>
                </div>
            </div>
            <div class="general-filters__column">
                <div class="general-filters__column__header">
                    Choose data catalogues and Thematic areas:
                </div>
                <CustomSelect class="general-filters__filter sources" @input="onSourcesChanged($event)"
                    :value="generalFilters.sources" :options="sourceOptions" :multiple="true" :filterable="true"
                    textProp="label" idProp="value" :placeholder="$t('generalFilters.earthObservationCatalogs')"
                    :appendToBody="appendToBody" :buttonDisabled="dataSource !== DataSources.DAB"
                    data-tutorial-tag="filters-general-sources" />

                <ExtendedCustomSelect class="general-filters__filter views" @input="onViewChanged($event)"
                    :value="generalFilters.viewId" :options="viewOptions" nestedOptionsFieldName="subOptions"
                    :filterable="true" textProp="label" idProp="value" :placeholder="$t('generalFilters.thematicAreas')"
                    :appendToBody="appendToBody" :buttonDisabled="dataSource !== DataSources.DAB ||
                        onlyDefaultViewAvailable()
                        " :clearable="!onlyDefaultViewAvailable()" data-tutorial-tag="filters-general-views" />
            </div>
            <div class="general-filters__column narrow">
                <div class="general-filters__column__header"></div>
                <GeossDataCoreCheckbox class="general-filters__filter general-filters__filter--geoss-data-core" @input="
                    generalFiltersGeossDataCore =
                    $event.target.value != 'true'
                    " :buttonDisabled="dataSource !== DataSources.DAB" data-tutorial-tag="filters-general-data-core" />
            </div>
        </div>
        <div class="general-filters__wrapper">
            <div class="general-filters__column narrow">
                <BoundingBoxRelationRadio @input="setBoundingBoxRelation($event)"
                    class="margin-top-5 general-filters__filter location" />
            </div>
            <div class="general-filters__column wide">
                <DateSlider class="general-filters__filter full-width" :min-year="minYear" :max-year="maxYear"
                    :date-from="dateFrom" :date-to="dateTo" :date-period="datePeriod"
                    @on-change-dates="changeDates($event)" data-tutorial-tag="filters-general-date-range" />
            </div>
        </div>
    </div>
</template>

<script lang="ts">
// @ts-nocheck
import { Component, Prop, Vue, Watch } from 'nuxt-property-decorator'

import GeossDataCoreCheckbox from '@/components/Search/GeneralFilters/GeossDataCoreCheckbox.vue'
import GooglePlacesSelect from '@/components/Search/GeneralFilters/GooglePlacesSelect.vue'
import BoundingBoxRelationRadio from '@/components/Search/GeneralFilters/BoundingBoxRelationRadio.vue'
import SearchFiltersTrigger from '@/components/Search/SearchFiltersTrigger.vue'
import DateSlider from '@/components/Search/DateSlider.vue'
import ExtendedCustomSelect from '@/components/ExtendedCustomSelect.vue'

import { GeneralFiltersActions } from '@/store/generalFilters/general-filters-actions'
import GeossSearchApiService from '@/services/geoss-search.api.service'
import { Source, View } from '@/interfaces/GeneralFilters'
import { CountriesAndContinents } from '@/data/general-filters'
import { GeneralFiltersGetters } from '@/store/generalFilters/general-filters-getters'
import UtilsService from '@/services/utils.service'
import { SearchGetters } from '@/store/search/search-getters'
import { GranulaFiltersActions } from '@/store/granulaFilters/granula-filters-actions'
import { SearchActions } from '@/store/search/search-actions'
import { MapCoordinate } from '@/interfaces/MapCoordinate'
import { DataSources } from '@/interfaces/DataSources'
import to from '@/utils/to'
import { AppVueObj } from '@/data/global'
import { SearchEngineGetters } from '@/store/searchEngine/search-engine-getters'

declare const google: any

@Component({
    components: {
        GeossDataCoreCheckbox,
        GooglePlacesSelect,
        BoundingBoxRelationRadio,
        SearchFiltersTrigger,
        DateSlider,
        ExtendedCustomSelect
    }
})
export default class SearchGeneralFiltersComponent extends Vue {
    @Prop({ type: Boolean, required: true }) public onlyAdvanced!: boolean

    public DataSources = DataSources
    public sourceOptions: Source[] = []
    public viewOptions: View[] = []
    public countriesAndContinents = CountriesAndContinents
    public lastState: any = null

    public minYear = new Date().getFullYear() - 20
    public maxYear = new Date().getFullYear()

    get appendToBody() {
        return this.containerVisible || this.generalFiltersInChange
    }

    get parentRef() {
        return this.$store.getters[SearchGetters.parentRef]
    }

    get isNonEmpty() {
        return this.$store.getters[GeneralFiltersGetters.isNonEmpty]
    }

    get dataSource(): DataSources {
        return this.$store.getters[SearchGetters.dataSource]
    }

    get availableLocationTypeOptions() {
        const locationTypeOptions =
            this.$store.getters[GeneralFiltersGetters.locationTypeOptions]
        const options = []
        for (const locationType of locationTypeOptions) {
            const textPath =
                locationType === 'continent_country'
                    ? 'generalFilters.continentCountry'
                    : 'generalFilters.' + locationType
            options.push({
                id: locationType,
                text: this.$tc(textPath)
            })
        }
        return options
    }

    get generalFiltersGeossDataCore() {
        return this.$store.getters[GeneralFiltersGetters.state].geossDataCore
    }

    set generalFiltersGeossDataCore(value: boolean) {
        this.rememberState()
        this.$store.dispatch(GeneralFiltersActions.setGeossDataCore, value)
    }

    get generalFilters() {
        return this.$store.getters[GeneralFiltersGetters.state]
    }

    get locationType() {
        return this.generalFilters.locationType
    }

    get containerVisible() {
        return this.$store.getters[GeneralFiltersGetters.containerVisible]
    }

    get siteId() {
        return this.$store.getters[SearchEngineGetters.siteId]
    }

    get coordinates() {
        return this.generalFilters.selectedAreaCoordinates
    }

    get selectedAreaCoordsConcat() {
        if (
            this.coordinates.W &&
            this.coordinates.S &&
            this.coordinates.E &&
            this.coordinates.N
        ) {
            return `${this.coordinates.W},${this.coordinates.S},${this.coordinates.E},${this.coordinates.N}`
        }

        return ''
    }

    get dateFrom() {
        return this.generalFilters.dateFrom
    }

    set dateFrom(value) {
        this.rememberState()
        this.$store.dispatch(GeneralFiltersActions.setDateFrom, value)
    }

    get dateTo() {
        return this.generalFilters.dateTo
    }

    set dateTo(value) {
        this.rememberState()
        this.$store.dispatch(GeneralFiltersActions.setDateTo, value)
    }

    get datePeriod() {
        return this.generalFilters.datePeriod
    }

    set datePeriod(value) {
        this.rememberState()
        this.$store.dispatch(GeneralFiltersActions.setDatePeriod, value)
    }

    get generalFiltersInChange() {
        return this.$store.getters[GeneralFiltersGetters.inChangeProcess]
    }

    get workflow() {
        return this.$store.getters[SearchGetters.workflow]
    }

    get workflowMapDraw() {
        return this.$store.getters[GeneralFiltersGetters.state].workflowMapDraw
    }

    get googlePlacesApiError() {
        return this.$store.getters[GeneralFiltersGetters.state]
            .googlePlacesApiError
    }

    get viewId() {
        return this.$store.getters[GeneralFiltersGetters.state].viewId
    }

    public changeDates(value: {
        dateFrom: string
        dateTo: string
        datePeriod: string
    }) {
        this.rememberState()
        this.dateFrom = value.dateFrom
        this.dateTo = value.dateTo
        this.datePeriod = value.datePeriod
    }

    public onSourcesChanged(value: string[]) {
        this.rememberState()
        this.$store.dispatch(GeneralFiltersActions.setSources, value)
    }

    public onViewChanged(value: string) {
        this.rememberState()
        this.$store.dispatch(GeneralFiltersActions.setViewId, value)
    }

    public onLocationTypeChanged(value: string) {
        this.rememberState()
        this.$store.dispatch(
            GeneralFiltersActions.setBoundingBoxRelation,
            'OVERLAPS'
        )
        this.$store.dispatch(GeneralFiltersActions.setLocationType, value)
    }

    public onCoordinatesChange(coordinate: string, event: Event) {
        this.rememberState()
        const coordinates = { ...this.coordinates }
        coordinates[coordinate] = this.getValidCoordinate(coordinate, event)
        this.$store.dispatch(
            GeneralFiltersActions.setSelectedAreaCoordinates,
            coordinates
        )
        this.$store.dispatch(GeneralFiltersActions.setGooglePlacesInput, '')
    }

    public clearCoordinates() {
        this.$store.dispatch(GeneralFiltersActions.setSelectedAreaCoordinates, {
            W: null,
            S: null,
            E: null,
            N: null
        })
    }

    public isCoordinateFilled() {
        return (
            this.coordinates.W ||
            this.coordinates.S ||
            this.coordinates.E ||
            this.coordinates.N
        )
    }

    public validCoordinates({
        W,
        S,
        E,
        N
    }: {
        W: number | null
        S: number | null
        E: number | null
        N: number | null
    }) {
        return W !== null && S !== null && E !== null && N !== null
    }

    public validateCoordinates(coordinate: string, event: Event) {
        const input = event.target as HTMLInputElement
        input.value = this.getValidCoordinate(coordinate, event)
    }

    public onContryOrContinentChanged(coordiantes: string) {
        this.rememberState()
        this.$store.dispatch(
            GeneralFiltersActions.setBoundingBoxRelation,
            'OVERLAPS'
        )
        if (
            typeof coordiantes === 'string' &&
            coordiantes.split(',').length === 4
        ) {
            const coordiantesSplitted = coordiantes.split(',')
            this.$store.dispatch(
                GeneralFiltersActions.setSelectedAreaCoordinates,
                {
                    W: parseFloat(coordiantesSplitted[0]),
                    S: parseFloat(coordiantesSplitted[1]),
                    E: parseFloat(coordiantesSplitted[2]),
                    N: parseFloat(coordiantesSplitted[3])
                }
            )
        } else {
            this.$store.dispatch(
                GeneralFiltersActions.setSelectedAreaCoordinates,
                null
            )
        }
        this.$store.dispatch(GeneralFiltersActions.setGooglePlacesInput, '')
    }

    public async cancelChanges() {
        this.$store.dispatch(GeneralFiltersActions.parseValues, this.lastState)
        await this.$nextTick()
        this.$store.dispatch(GeneralFiltersActions.setInChangeProcess, false)

        if (this.workflow) {
            this.$store.dispatch(SearchActions.setWorkflowCoordinates, null)
            this.$store.dispatch(
                GeneralFiltersActions.setWorkflowMapDraw,
                false
            )
            const previousState = JSON.parse(AppVueObj.storeStateBackup)
            previousState.search.workflow = this.workflow
            previousState.search.workflowCoordinates = null
            previousState.popup.queue = []
            previousState.popup.promises = []
            UtilsService.popFromHistory(JSON.stringify(previousState))
        }
    }

    public acceptChanges() {
        const previousState = JSON.parse(AppVueObj.storeStateBackup)
        if (this.workflow) {
            previousState.search.workflow = this.workflow
            if (this.validCoordinates(this.coordinates)) {
                previousState.search.workflowCoordinates = this.coordinates
            }
        }
        if (this.workflowMapDraw) {
            previousState.popup.queue = []
            previousState.popup.promises = []
            UtilsService.popFromHistory(JSON.stringify(previousState))
        } else {
            this.$store.dispatch(
                GeneralFiltersActions.setInChangeProcess,
                false
            )
            const currentState = UtilsService.getCurrentState()
            for (const prop in this.lastState) {
                if (currentState.generalFilters.hasOwnProperty(prop)) {
                    currentState.generalFilters[prop] = this.lastState[prop]
                }
            }
            if (!this.parentRef) {
                this.$store.dispatch(GranulaFiltersActions.reset)
            }
            this.$store
                .dispatch(SearchActions.getResults, {
                    noPushToHistory: !!this.workflow
                })
                .catch(() => {
                    if (this.workflow) {
                        UtilsService.setCurrentState(currentState)
                    }
                })
        }
    }

    public setBoundingBoxRelation(value: any) {
        this.rememberState()
        this.$store.dispatch(
            GeneralFiltersActions.setBoundingBoxRelation,
            value
        )
    }

    public onGooglePlacesSelect(coordinate: MapCoordinate) {
        this.rememberState()
        this.$store.dispatch(
            GeneralFiltersActions.setBoundingBoxRelation,
            'OVERLAPS'
        )
        this.$store.dispatch(
            GeneralFiltersActions.setSelectedAreaCoordinates,
            coordinate
        )
    }

    public getGooglePlacesApiError() {
        if (this.googlePlacesApiError) {
            const errorTextPath = 'errors.google.OVER_QUERY_LIMIT'
            return this.$t(errorTextPath)
        } else {
            return null
        }
    }

    public onlyDefaultViewAvailable() {
        return (
            this.viewOptions.length === 1 &&
            this.viewOptions[0].defaultOption === true
        )
    }

    private rememberState(lastState?: any) {
        if (!this.lastState) {
            this.lastState = lastState
                ? lastState
                : JSON.parse(
                    JSON.stringify(
                        this.$store.getters[GeneralFiltersGetters.values]
                    )
                )
            this.$store.dispatch(GeneralFiltersActions.setInChangeProcess, true)
        } else {
            /**
             * we need to make check after value of the filter
             * has changed and this is what timeout is for
             */
            setTimeout(() => {
                if (this.lastState) {
                    const currentGeneralFilters = JSON.stringify(
                        this.$store.getters[GeneralFiltersGetters.values]
                    )
                    /* if datePeriod is set, currentGeneralFilters.dateTo will never match this.lastState.dateTo
                     * datePeriod sets relative period to users current day
                     * it causes always returning inChangeProcess = true
                     * therefore we should always return inChangeProcess = false in that case
                     */
                    const inChangeProcess =
                        this.lastState.datePeriod === ''
                            ? currentGeneralFilters !==
                            JSON.stringify(this.lastState)
                            : false
                    this.$store.dispatch(
                        GeneralFiltersActions.setInChangeProcess,
                        inChangeProcess
                    )
                }
            }, 0)
        }
    }

    private getValidCoordinate(coordinate: string, event: Event) {
        const coordinates = { ...this.coordinates }
        const input = event.target as HTMLInputElement
        const value = input.value
        coordinates[coordinate] = value ? parseFloat(input.value) : null
        if (coordinates[coordinate] !== null) {
            if (coordinate === 'W' || coordinate === 'E') {
                if (coordinates[coordinate] > 180) {
                    coordinates[coordinate] = 180
                } else if (coordinates[coordinate] < -180) {
                    coordinates[coordinate] = -180
                }
            } else {
                if (coordinates[coordinate] > 90) {
                    coordinates[coordinate] = 90
                } else if (coordinates[coordinate] < -90) {
                    coordinates[coordinate] = -90
                }
            }
        }
        return coordinates[coordinate]
    }

    private setDefaultViewIdIfAny() {
        const defaultViewOption = this.viewOptions.find(
            (option) => option.defaultOption
        )
        if (defaultViewOption) {
            this.$store.dispatch(
                GeneralFiltersActions.setViewId,
                defaultViewOption.value
            )
        }
    }

    @Watch('locationType')
    private onLocationTypeChange(newVal: any, oldVal: any) {
        if (
            oldVal === 'geolocation' &&
            !this.availableLocationTypeOptions.includes('geolocation')
        ) {
            return
        }
        const lastState = JSON.parse(
            JSON.stringify(this.$store.getters[GeneralFiltersGetters.values])
        )
        lastState.locationType = oldVal
        this.rememberState(lastState)
    }

    @Watch('coordinates')
    private onCoordinatesValueChange(newVal: any, oldVal: any) {
        const lastState = JSON.parse(
            JSON.stringify(this.$store.getters[GeneralFiltersGetters.values])
        )
        lastState.selectedAreaCoordinates = oldVal
        this.rememberState(lastState)
    }

    @Watch('generalFiltersInChange')
    private onGeneralFiltersInChangeChange() {
        if (!this.generalFiltersInChange) {
            this.lastState = null
        }
    }

    @Watch('viewId')
    private onVIewIdChange(newVal: any) {
        if (newVal === '' && this.onlyDefaultViewAvailable()) {
            this.setDefaultViewIdIfAny()
        }
    }

    private async created() {
        const [, sourceOptions] = await to(
            GeossSearchApiService.getSourcesOptions('')
        ) // TODO: add url
        if (sourceOptions) {
            this.sourceOptions = sourceOptions
        }

        const [, viewOptions] = await to(
            GeossSearchApiService.getViewsOptions(this.siteId)
        )
        if (viewOptions) {
            this.viewOptions = viewOptions
            this.setDefaultViewIdIfAny()
        }

        if (sessionStorage.getItem('googlePlacesApiError')) {
            this.$store.dispatch(
                GeneralFiltersActions.setGooglePlacesApiError,
                sessionStorage.getItem('googlePlacesApiError')
            )
        }
    }
}
</script>

<style lang="scss">
.general-filters {
    padding-bottom: 10px;
    background: $blue-transparent;
    position: relative;

    &:not(.only-advanced) {
        margin-top: 2px;
    }

    .general-filters__wrapper {
        display: flex;
        flex-wrap: nowrap;
        gap: 20px;
        width: 100%;

        @media (max-width: $breakpoint-lg) {
            flex-wrap: wrap;
            gap: 0;
        }
    }

    &.only-advanced {
        background: $blue;

        .general-filters__wrapper {
            padding-top: 0;
            border-top: 1px solid #fff;
        }

        .general-filters__column {
            padding: 20px 0;
        }

        .general-filters__absolute-box {
            position: absolute;
            width: 170px;
            top: -34px;
            right: 0;
            padding: 0;

            @media (max-width: $breakpoint-sm) {
                &.stretched {
                    padding: 0 20px;
                    right: 0;
                    width: 100%;
                }
            }
        }
    }

    .general-filters__absolute-box {
        padding: 10px 0;
        justify-content: flex-end;
    }

    &__google-api-error {
        width: 100%;
        margin: 0 0 10px;
        font-size: 0.85em;
        position: relative;
        color: $yellow;
    }

    &__absolute-box {
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: 100%;
    }

    &__action {
        padding: 5px 20px;
        color: white;
        background-color: $blue-light;
        border-radius: 15px;

        &.cancel {
            background-color: white;
            color: $blue-light;
            margin-right: 15px;
        }
    }

    &__wrapper {
        background-color: transparent !important;
        padding-left: 0 !important;
        padding-right: 0 !important;

        &:after {
            display: none !important;
        }

        @media (max-width: $breakpoint-sm) {
            &:after {
                margin-bottom: 0 !important;
            }
        }
    }

    &__column {
        width: 37.5%;
        padding: 5px 0;

        @media (max-width: $breakpoint-lg) {
            width: 100% !important;
            padding: 10px 0;
        }

        &.full {
            width: 100%;
        }

        &.narrow {
            width: 20%;
        }

        &.wide {
            width: 80%;
        }

        &__header {
            color: #fff;
            padding-bottom: 10px;
            white-space: nowrap;

            @media (max-width: $breakpoint-sm) {
                white-space: normal;
            }
        }
    }

    &__filter {
        // width: calc(50% - 13px);
        margin-bottom: 5px;

        @media (max-width: $breakpoint-sm) {
            width: 100%;
            order: 0;

            &.sources,
            &.views {
                order: 0;
            }

            &.location-type,
            &.location {
                order: 1;
            }

            &.dates {
                order: 1;
            }
        }

        &--geoss-data-core {
            width: auto;
            margin-top: 12px;

            @media (max-width: $breakpoint-sm) {
                margin-bottom: 0 !important;
                margin-top: 2px;
                order: 0;
                width: 100%;
            }

            img {
                height: 28px;
            }
        }

        &--geoss-submit {
            @media (max-width: $breakpoint-sm) {
                margin-right: 0 !important;
                margin-top: 5px;
            }
        }

        &--coordinates {
            .coordinates {
                &-inputs {
                    display: flex;
                    justify-content: space-between;
                    flex-wrap: wrap;
                }

                &-buttons {
                    position: relative;

                    button {
                        color: $white;
                        position: absolute;
                        top: 3px;
                        right: 0;
                        opacity: 0;
                        transition: 0.1s ease all;
                        pointer-events: none;

                        &.active {
                            opacity: 1;
                            pointer-events: all;
                            top: 8px;
                        }
                    }
                }
            }

            input {
                width: calc(25% - 5px);
                border: none;
                height: 37px;
                padding: 4px 6px;
                font-size: 12px;
                -moz-appearance: textfield;

                &:hover {
                    -moz-appearance: initial;
                }

                @media (max-width: $breakpoint-xxl) {
                    width: calc(50% - 2.5px);

                    &:nth-child(1),
                    &:nth-child(2) {
                        margin-bottom: 5px;
                    }
                }
            }
        }
    }

    .custom-select__container {
        max-height: 170px;
    }
}
</style>
