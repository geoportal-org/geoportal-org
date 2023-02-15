<template>
	<div class="granula-filters filters">
		<div class="granula-filters__wrapper">
			<CustomSelect
				v-show="productTypeOptions.length"
				class="granula-filters__filter" 
				v-model="productType"
				:options="productTypeOptions" 
				:filterable="true"
				:placeholder="$t('granulaFilters.productType')"
				:appendToBody="appendToBody"
				data-tutorial-tag="filters-granula-product-type" />
			<CustomSelect
				v-show="sensorPolarisationOptions.length"
				class="granula-filters__filter" 
				v-model="sensorPolarisation"
				:options="sensorPolarisationOptions" 
				:filterable="true"
				:placeholder="$t('granulaFilters.sensorPolarisation')"
				:appendToBody="appendToBody"
				data-tutorial-tag="filters-granula-sensor-polarisation"/>
			<CustomSelect
				v-show="sensorModeOptions.length"
				class="granula-filters__filter" 
				v-model="sensorMode"
				:options="sensorModeOptions" 
				:filterable="true"
				:placeholder="$t('granulaFilters.sensorMode')"
				:appendToBody="appendToBody"
				data-tutorial-tag="filters-granula-sensor-mode" />
			<CustomSelect
				v-show="sensorSwathOptions.length"
				class="granula-filters__filter" 
				v-model="sensorSwath"
				:options="sensorSwathOptions" 
				:filterable="true"
				:placeholder="$t('granulaFilters.sensorSwatch')"
				:appendToBody="appendToBody"
				data-tutorial-tag="filters-granula-sensor-swath" />
			<CustomSelect
				v-show="instrumentOptions.length"
				class="granula-filters__filter" 
				v-model="instrument"
				:options="instrumentOptions" 
				:filterable="true"
				:placeholder="$t('granulaFilters.instrument')"
				:appendToBody="appendToBody"
				data-tutorial-tag="filters-granula-instrument" />
			<CustomSelect
				v-show="productLevelOptions.length"
				class="granula-filters__filter" 
				v-model="productLevel"
				:options="productLevelOptions" 
				:filterable="true"
				:placeholder="$t('granulaFilters.productLevel')"
				:appendToBody="appendToBody"
				data-tutorial-tag="filters-granula-product-level" />
			<CustomSelect
				v-show="timelinessOptions.length"
				class="granula-filters__filter" 
				v-model="timeliness"
				:options="timelinessOptions" 
				:filterable="true"
				:placeholder="$t('granulaFilters.timeliness')"
				:appendToBody="appendToBody"
				data-tutorial-tag="filters-granula-timeliness" />
			<input class="granula-filters__filter input" type="number"
				:value="relativeOrbit" @blur="relativeOrbit = $event.target.value"
				v-show="relativeOrbitAvailable" :placeholder="$t('granulaFilters.relativeOrbit')"
				data-tutorial-tag="filters-granula-relative-orbit" />
			<input class="granula-filters__filter input" type="number" 
				:value="row" @blur="row = $event.target.value"
				v-show="rowAvailable" :placeholder="$t('granulaFilters.row')"
				data-tutorial-tag="filters-granula-row" />
			<input class="granula-filters__filter input" type="number" 
				:value="path" @blur="path = $event.target.value"
				v-show="pathAvailable" :placeholder="$t('granulaFilters.path')"
				data-tutorial-tag="filters-granula-path" />
			<DateSlider class="granula-filters__filter full-width" 
				:min-year="minYear"
				:max-year="maxYear"
				:date-from="fromDate"
				:date-to="toDate"
				@on-change-dates="changeDates($event)" 
				data-tutorial-tag="filters-date-range"/>
			<div class="iris-filters__filter slider" v-show="cloudCoverageAvailable" data-tutorial-tag="filters-granula-cloud-coverage">
				<label>Cloud coverage: {{cloudCoverage.join(' - ')}}%</label>
				<vue-slider :min="0" :max="100" :value="cloudCoverage"
					@drag-start="dragStart()"
					@drag-end="dragStop('cloudCoverage', $event)" 
					@change="sliderChange('cloudCoverage', $event)" />
			</div>
		</div>
	</div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from 'vue-property-decorator';

import VueSlider from '@/components/Slider/index';
import DateSlider from '@/components/Search/DateSlider.vue';

import { SearchGetters } from '@/stores/search/search-getters';
import { GranulaFiltersGetters } from '@/stores/granula-filters/granula-filters-getters';
import { GranulaFiltersActions } from '@/stores/granula-filters/granula-filters-actions';
import { SearchActions } from '@/stores/search/search-actions';
import UtilsService from '@/services/utils.service';
import { GeneralFiltersActions } from '@/stores/general-filters/general-filters-actions';
import { GeneralFiltersGetters } from '../../stores/general-filters/general-filters-getters';
import { DataSources } from '@/interfaces/DataSources';

@Component({
	components: {
		VueSlider,
		DateSlider
	}
})
export default class SearchGranulaFiltersComponent extends Vue {

	public minYear = new Date().getFullYear() - 20;
	public maxYear = new Date().getFullYear();
	public dragAction = false;

	get appendToBody() {
		return (this.containerVisible || this.generalFiltersInChange);
	}

	get containerVisible() {
		return this.$store.getters[GeneralFiltersGetters.containerVisible];
	}

	get generalFiltersInChange() {
		return this.$store.getters[GeneralFiltersGetters.inChangeProcess];
	}

	get productType() {
		return this.$store.getters[GranulaFiltersGetters.productType];
	}

	set productType(value: string) {
		this.$store.dispatch(GranulaFiltersActions.setProductType, value);
		this.$store.dispatch(SearchActions.getResults, {targetSource: DataSources.DAB});
	}

	get productTypeOptions() {
		return this.$store.getters[GranulaFiltersGetters.productTypeOptions];
	}

	get sensorPolarisation() {
		return this.$store.getters[GranulaFiltersGetters.sensorPolarisation];
	}

	set sensorPolarisation(value: string) {
		this.$store.dispatch(GranulaFiltersActions.setSensorPolarisation, value);
		this.$store.dispatch(SearchActions.getResults, {targetSource: DataSources.DAB});
	}

	get sensorPolarisationOptions() {
		return this.$store.getters[GranulaFiltersGetters.sensorPolarisationOptions];
	}

	get sensorMode() {
		return this.$store.getters[GranulaFiltersGetters.sensorMode];
	}

	set sensorMode(value: string) {
		this.$store.dispatch(GranulaFiltersActions.setSensorMode, value);
		this.$store.dispatch(SearchActions.getResults, {targetSource: DataSources.DAB});
	}

	get sensorModeOptions() {
		return this.$store.getters[GranulaFiltersGetters.sensorModeOptions];
	}

	get sensorSwath() {
		return this.$store.getters[GranulaFiltersGetters.sensorSwath];
	}

	set sensorSwath(value: string) {
		this.$store.dispatch(GranulaFiltersActions.setSensorSwath, value);
		this.$store.dispatch(SearchActions.getResults, {targetSource: DataSources.DAB});
	}

	get sensorSwathOptions() {
		return this.$store.getters[GranulaFiltersGetters.sensorSwathOptions];
	}

	get instrument() {
		return this.$store.getters[GranulaFiltersGetters.instrument];
	}

	set instrument(value: string) {
		this.$store.dispatch(GranulaFiltersActions.setInstrument, value);
		this.$store.dispatch(SearchActions.getResults, {targetSource: DataSources.DAB});
	}

	get instrumentOptions() {
		return this.$store.getters[GranulaFiltersGetters.instrumentOptions];
	}

	get productLevel() {
		return this.$store.getters[GranulaFiltersGetters.productLevel];
	}

	set productLevel(value: string) {
		this.$store.dispatch(GranulaFiltersActions.setProductLevel, value);
		this.$store.dispatch(SearchActions.getResults, {targetSource: DataSources.DAB});
	}

	get productLevelOptions() {
		return this.$store.getters[GranulaFiltersGetters.productLevelOptions];
	}

	get timeliness() {
		return this.$store.getters[GranulaFiltersGetters.timeliness];
	}

	set timeliness(value: string) {
		this.$store.dispatch(GranulaFiltersActions.setTimeliness, value);
		this.$store.dispatch(SearchActions.getResults, {targetSource: DataSources.DAB});
	}

	get timelinessOptions() {
		return this.$store.getters[GranulaFiltersGetters.timelinessOptions];
	}

	get cloudCoverageAvailable() {
		return this.$store.getters[GranulaFiltersGetters.cloudCoverageAvailable];
	}

	get cloudCoverage() {
		return this.$store.getters[GranulaFiltersGetters.cloudCoverage];
	}

	set cloudCoverage(value: number[]) {
		this.$store.dispatch(GranulaFiltersActions.setCloudCoverage, value);
		this.$store.dispatch(SearchActions.getResults, {targetSource: DataSources.DAB});
	}

	get relativeOrbit() {
		return this.$store.getters[GranulaFiltersGetters.relativeOrbit];
	}

	set relativeOrbit(value: string) {
		if(value !== this.relativeOrbit) {
			this.$store.dispatch(GranulaFiltersActions.setRelativeOrbit, value);
			this.$store.dispatch(SearchActions.getResults, {targetSource: DataSources.DAB});
		}
	}

	get relativeOrbitAvailable() {
		return this.$store.getters[GranulaFiltersGetters.relativeOrbitAvailable];
	}

	get row() {
		return this.$store.getters[GranulaFiltersGetters.row];
	}

	set row(value: string) {
		if(value !== this.row) {
			this.$store.dispatch(GranulaFiltersActions.setRow, value);
			this.$store.dispatch(SearchActions.getResults, {targetSource: DataSources.DAB});
		}
	}

	get rowAvailable() {
		return this.$store.getters[GranulaFiltersGetters.rowAvailable];
	}

	get path() {
		return this.$store.getters[GranulaFiltersGetters.path];
	}

	set path(value: string) {
		if(value !== this.path) {
			this.$store.dispatch(GranulaFiltersActions.setPath, value);
			this.$store.dispatch(SearchActions.getResults, {targetSource: DataSources.DAB});
		}
	}

	get pathAvailable() {
		return this.$store.getters[GranulaFiltersGetters.pathAvailable];
	}

	get fromDate() {
		return this.$store.getters[GranulaFiltersGetters.fromDate];
	}

	set fromDate(value: string) {
		this.$store.dispatch(GranulaFiltersActions.setFromDate, value);
		this.$store.dispatch(SearchActions.getResults, {targetSource: DataSources.DAB});
	}

	get toDate() {
		return this.$store.getters[GranulaFiltersGetters.toDate];
	}

	set toDate(value: string) {
		this.$store.dispatch(GranulaFiltersActions.setToDate, value);
		this.$store.dispatch(SearchActions.getResults, {targetSource: DataSources.DAB});
	}

	get currentResults() {
		return this.$store.getters[SearchGetters.currentResults];
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

	public changeDates(value: {dateFrom: string, dateTo: string}) {
		this.$store.dispatch(GranulaFiltersActions.setFromDate, value.dateFrom);
		this.$store.dispatch(GranulaFiltersActions.setToDate, value.dateTo);
		this.$store.dispatch(GeneralFiltersActions.setInChangeProcess, false);
		this.$store.dispatch(SearchActions.getResults, {theSameTab: true, targetSource: DataSources.DAB});
	}

	private updateFilterOptions(storeAction: string, filter: string) {
		const options = [];
		const filterOptionsRaw = this.currentResults['dm:termFrequency'][`dm:${filter}`];

		if(filterOptionsRaw && filterOptionsRaw[`dm:item`]) {

			if(filterOptionsRaw[`dm:item`].constructor !== Array) {
				filterOptionsRaw[`dm:item`] = [filterOptionsRaw[`dm:item`]];
			}

			for(const item of filterOptionsRaw[`dm:item`]) {
				const name = item['dm:decodedTerm'];

				options.push({
					text: `${name} (${item['dm:freq']})`,
					id: item['dm:decodedTerm']
				});
			}
		}

		this.$store.dispatch(storeAction, options);
	}

	@Watch('currentResults')
	private async onCurrentResultsChanged() {
		if(this.currentResults && this.currentResults['dm:termFrequency']) {
			this.updateFilterOptions(GranulaFiltersActions.setProductTypeOptions, 'prodType');
			this.updateFilterOptions(GranulaFiltersActions.setSensorPolarisationOptions, 'sarPolCh');
			this.updateFilterOptions(GranulaFiltersActions.setSensorModeOptions, 'sensorOpMode');
			this.updateFilterOptions(GranulaFiltersActions.setSensorSwathOptions, 'sensorSwath');
			this.updateFilterOptions(GranulaFiltersActions.setInstrumentOptions, 's3InstrumentIdx');
			this.updateFilterOptions(GranulaFiltersActions.setProductLevelOptions, 's3ProductLevel');
			this.updateFilterOptions(GranulaFiltersActions.setTimelinessOptions, 's3Timeliness');
		}
	}
}
</script>

<style lang="scss">
.granula-filters {
	padding: 0;
	
	&__filter {
		width: calc(50% - 13px);
		margin-bottom: 5px;

		@media (max-width: $breakpoint-sm) {
			width: 100%;
		}

		&.input {
			height: 37px;
			padding: 6px 12px 6px 30px;
			border: none;
			margin-bottom: 5px;

			&:disabled {
				opacity: 0.65;
			}
		}

		&.dates {
			display: flex;
			justify-content: space-between;
			flex-wrap: wrap;
			width: 100%;
			margin-bottom: 0;

			&>* {
				width: calc(50% - 13px);

				@media (max-width: $breakpoint-sm) {
					width: 100%;
				}
			}
		}
	}

	.vue-slider {
		padding-left: 10px !important;
		padding-right: 10px !important;
	}
}
</style>