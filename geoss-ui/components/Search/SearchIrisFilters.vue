<template>
    <div class="iris-filters filters">
        <div class="iris-filters__wrapper" v-show="activeSection !== 'advanced'">
            <CustomSelect class="iris-filters__filter" v-model="magnitudeType" :options="magnitudeTypeOptions"
                :appendToBody="appendToBody" :placeholder="$tc('irisFilters.magnitudeType')"
                data-tutorial-tag="filters-iris-magnitude-type" />
            <CustomSelect class="iris-filters__filter" v-model="sort" :options="sortOptions" :appendToBody="appendToBody"
                :placeholder="$tc('irisFilters.sortBy')" data-tutorial-tag="filters-iris-sort" />
            <div class="iris-filters__filter slider" data-tutorial-tag="filters-iris-magnitude-range">
                <label>Magnitude limit: {{ magnitudeFormatted }}</label>
                <!-- <vue-slider ref="magnitudeSlider" :min="0" :max="10" :value="magnitude" tooltip="focus"
                    @drag-start="dragStart()" @drag-end="dragStop('magnitude', $event)"
                    @change="sliderChange('magnitude', $event)" /> -->
            </div>
            <div class="iris-filters__filter slider" data-tutorial-tag="filters-iris-depth-range">
                <label>Depth: {{ depthFormatted }}</label>
                <!-- <vue-slider ref="depthSlider" :min="0" :max="100" :value="depth" tooltip="focus" @drag-start="dragStart()"
                    @drag-end="dragStop('depth', $event)" @change="sliderChange('depth', $event)" /> -->
            </div>
            <DateSlider class="iris-filters__filter full-width" :min-year="minYear" :max-year="maxYear"
                :date-from="dateFrom" :date-to="dateTo" :listenDragEnd="true" @on-change-dates="changeDates($event)"
                data-tutorial-tag="filters-iris-date-range" />
        </div>
    </div>
</template>

<script lang="ts">
// @ts-nocheck
import { Component, Vue, Watch } from 'nuxt-property-decorator';

// import VueSlider from '@/components/Slider/index';
import DateSlider from '@/components/Search/DateSlider.vue';
import { IrisFiltersGetters } from '@/store/irisFilters/iris-filters-getters';
import { IrisFiltersActions } from '@/store/irisFilters/iris-filters-actions';
import { SearchActions } from '@/store/search/search-actions';
import { GeneralFiltersGetters } from '@/store/generalFilters/general-filters-getters';

@Component({
    components: {
        // VueSlider,
        DateSlider
    }
})
export default class SearchIrisFiltersComponent extends Vue {

    public minYear = new Date().getFullYear() - 20;
    public maxYear = new Date().getFullYear();
    public dragAction = false;

    public activeSection = 'iris';

    get appendToBody() {
        return (this.containerVisible || this.generalFiltersInChange);
    }

    get containerVisible() {
        return this.$store.getters[GeneralFiltersGetters.containerVisible];
    }

    get generalFiltersInChange() {
        return this.$store.getters[GeneralFiltersGetters.inChangeProcess];
    }

    get magnitudeType() {
        return this.$store.getters[IrisFiltersGetters.magnitudeType];
    }

    set magnitudeType(value: string) {
        this.$store.dispatch(IrisFiltersActions.setMagnitudeType, value);
        this.$store.dispatch(SearchActions.getResults);
    }

    get magnitudeTypeOptions() {
        return this.$store.getters[IrisFiltersGetters.magnitudeTypeOptions];
    }

    get sort() {
        return this.$store.getters[IrisFiltersGetters.sort];
    }

    set sort(value: string) {
        this.$store.dispatch(IrisFiltersActions.setSort, value);
        this.$store.dispatch(SearchActions.getResults);
    }

    get sortOptions() {
        return this.$store.getters[IrisFiltersGetters.sortOptions];
    }

    get magnitude() {
        return [
            this.$store.getters[IrisFiltersGetters.minMagnitude],
            this.$store.getters[IrisFiltersGetters.maxMagnitude]
        ] as any;
    }

    get magnitudeFormatted() {
        return `${this.magnitude[0]}-${this.magnitude[1]}`;
    }

    set magnitude(slider: VueSlider) {
        const value = (this.$refs.magnitudeSlider as VueSlider).getValue();
        this.$store.dispatch(IrisFiltersActions.setMinMagnitude, value[0]);
        this.$store.dispatch(IrisFiltersActions.setMaxMagnitude, value[1]);
        this.$store.dispatch(SearchActions.getResults);
    }

    get depth() {
        return [
            this.$store.getters[IrisFiltersGetters.minDepth],
            this.$store.getters[IrisFiltersGetters.maxDepth]
        ];
    }

    get depthFormatted() {
        return `${this.depth[0]}-${this.depth[1]}`;
    }

    set depth(slider: any) {
        const value = (this.$refs.depthSlider as VueSlider).getValue();
        this.$store.dispatch(IrisFiltersActions.setMinDepth, value[0]);
        this.$store.dispatch(IrisFiltersActions.setMaxDepth, value[1]);
        this.$store.dispatch(SearchActions.getResults);
    }

    get dateFrom() {
        return this.$store.getters[IrisFiltersGetters.dateFrom];
    }

    set dateFrom(value: string) {
        this.$store.dispatch(IrisFiltersActions.setDateFrom, value);
        this.$store.dispatch(SearchActions.getResults);
    }

    get dateTo() {
        return this.$store.getters[IrisFiltersGetters.dateTo];
    }

    set dateTo(value: string) {
        this.$store.dispatch(IrisFiltersActions.setDateTo, value);
        this.$store.dispatch(SearchActions.getResults);
    }

    get isAvailable() {
        return this.$store.getters[IrisFiltersGetters.irisFiltersAvailable];
    }

    public changeDates(value: { dateFrom: string, dateTo: string }) {
        this.$store.dispatch(IrisFiltersActions.setDateFrom, value.dateFrom);
        this.$store.dispatch(IrisFiltersActions.setDateTo, value.dateTo);
        this.$store.dispatch(SearchActions.getResults);
    }

    public dragStart() {
        this.dragAction = true;
    }

    public dragStop(target, value) {
        this.dragAction = false;
        this[target] = value;
    }

    public sliderChange(target, value) {
        if (!this.dragAction) {
            this[target] = value;
        }
    }

    @Watch('isAvailable')
    private onIsAvailableChange(val: boolean, oldVal: boolean) {
        if (!oldVal && val) {
            this.$store.dispatch(IrisFiltersActions.setMinMagnitude, this.magnitude[0] || 0);
            this.$store.dispatch(IrisFiltersActions.setMaxMagnitude, this.magnitude[1] || 10);
            this.$store.dispatch(IrisFiltersActions.setMinDepth, this.depth[0] || 0);
            this.$store.dispatch(IrisFiltersActions.setMaxDepth, this.depth[1] || 100);
        }
    }
}
</script>

<style lang="scss">
.iris-filters {
    padding: 0;

    &__wrapper {
        display: flex;
        flex-wrap: wrap;
        justify-content: space-between;
        align-content: flex-start;
        align-items: flex-start;
        padding-top: 10px;
    }

    &__filter {
        width: calc(50% - 13px);
        margin-bottom: 5px;

        @media (max-width: $breakpoint-sm) {
            width: 100%;
        }

        &.slider {
            width: 100% !important;
            background: rgba(11, 35, 82, 0.31);
            padding: 5px 0 15px;

            label {
                font-size: 15px;
                font-weight: 400;
                color: white;
                padding-left: 10px;
                margin-top: 5px;
                margin-bottom: 5px;
                display: block;
            }

            .vue-slider {
                margin-left: 0;
                width: calc(100% - 20px) !important;
            }
        }

        &.dates {
            display: flex;
            justify-content: space-between;
            width: 100%;

            &>* {
                width: calc(50% - 13px);
            }
        }
    }

    .vue-slider {
        padding-left: 10px !important;
        padding-right: 10px !important;
    }
}
</style>
