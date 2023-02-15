<template>
	<div class="faceted-filters filters">
		<div class="faceted-filters__filters-hide" v-show="!anyFilterAvailable">{{$t('general.noFiltersAvailable')}}</div>
		<div class="faceted-filters__wrapper" v-show="anyFilterAvailable">
			<CustomSelect
				class="faceted-filters__filter" 
				v-model="keyword"
				:options="keywordOptions" 
				:filterable="true"
				:placeholder="$t('facetedFilters.keyword')"
				:appendToBody="appendToBody"
				data-tutorial-tag="filters-faceted-keyword" />
			<CustomSelect
				class="faceted-filters__filter" 
				v-model="format"
				:options="formatOptions" 
				:filterable="true"
				:placeholder="$t('facetedFilters.format')"
				:appendToBody="appendToBody"
				data-tutorial-tag="filters-faceted-format" />
			<CustomSelect
				class="faceted-filters__filter" 
				v-model="source"
				:options="sourceOptions" 
				:filterable="true"
				:placeholder="$t('facetedFilters.source')"
				:appendToBody="appendToBody"
				data-tutorial-tag="filters-faceted-source" />
			<CustomSelect
				class="faceted-filters__filter" 
				v-model="protocol"
				:options="protocolOptions" 
				:filterable="true"
				:placeholder="$t('facetedFilters.protocol')"
				:appendToBody="appendToBody"
				data-tutorial-tag="filters-faceted-protocol" />
			<CustomSelect
				class="faceted-filters__filter" 
				v-model="organisation"
				:options="organisationOptions" 
				:filterable="true"
				:placeholder="$t('facetedFilters.organisation')"
				:appendToBody="appendToBody"
				data-tutorial-tag="filters-faceted-organisation" />
			<CustomSelect
				class="faceted-filters__filter" 
				v-model="score"
				:options="scoreOptions" 
				:filterable="true"
				:placeholder="$t('facetedFilters.serviceHealth')"
				:appendToBody="appendToBody"
				data-tutorial-tag="filters-faceted-score" />
		</div>
	</div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from 'vue-property-decorator';

import { SearchGetters } from '@/stores/search/search-getters';
import { FacetedFiltersGetters } from '@/stores/faceted-filters/faceted-filters-getters';
import { FacetedFiltersActions } from '@/stores/faceted-filters/faceted-filters-actions';
import { SearchActions } from '@/stores/search/search-actions';
import UtilsService from '@/services/utils.service';
import { GeneralFiltersActions } from '@/stores/general-filters/general-filters-actions';
import { GeneralFiltersGetters } from '../../stores/general-filters/general-filters-getters';

@Component
export default class SearchFacetedFiltersComponent extends Vue {
	get anyFilterAvailable() {
		return (
			(this.keywordOptions && this.keywordOptions.length) ||
			(this.formatOptions && this.formatOptions.length) ||
			(this.sourceOptions && this.sourceOptions.length) ||
			(this.protocolOptions && this.protocolOptions.length) ||
			(this.organisationOptions && this.organisationOptions.length) ||
			(this.scoreOptions && this.scoreOptions.length)
		);
	}

	get appendToBody() {
		return (this.containerVisible || this.generalFiltersInChange);
	}

	get keyword() {
		return this.$store.getters[FacetedFiltersGetters.keyword];
	}

	set keyword(value: string) {
		this.$store.dispatch(FacetedFiltersActions.setKeyword, value);
		this.$store.dispatch(SearchActions.getResults, {theSameTab: true});
	}

	get keywordOptions() {
		return this.$store.getters[FacetedFiltersGetters.keywordOptions];
	}

	get format() {
		return this.$store.getters[FacetedFiltersGetters.format];
	}

	set format(value: string) {
		this.$store.dispatch(FacetedFiltersActions.setFormat, value);
		this.$store.dispatch(SearchActions.getResults, {theSameTab: true});
	}

	get formatOptions() {
		return this.$store.getters[FacetedFiltersGetters.formatOptions];
	}

	get source() {
		return this.$store.getters[FacetedFiltersGetters.source];
	}

	set source(value: string) {
		this.$store.dispatch(FacetedFiltersActions.setSource, value);
		this.$store.dispatch(SearchActions.getResults, {theSameTab: true});
	}

	get sourceOptions() {
		return this.$store.getters[FacetedFiltersGetters.sourceOptions];
	}

	get protocol() {
		return this.$store.getters[FacetedFiltersGetters.protocol];
	}

	set protocol(value: string) {
		this.$store.dispatch(FacetedFiltersActions.setProtocol, value);
		this.$store.dispatch(SearchActions.getResults, {theSameTab: true});
	}

	get protocolOptions() {
		return this.$store.getters[FacetedFiltersGetters.protocolOptions];
	}

	get organisation() {
		return this.$store.getters[FacetedFiltersGetters.organisation];
	}

	set organisation(value: string) {
		this.$store.dispatch(FacetedFiltersActions.setOrganisation, value);
		this.$store.dispatch(SearchActions.getResults, {theSameTab: true});
	}

	get organisationOptions() {
		return this.$store.getters[FacetedFiltersGetters.organisationOptions];
	}

	get score() {
		return this.$store.getters[FacetedFiltersGetters.score];
	}

	set score(value: string) {
		this.$store.dispatch(FacetedFiltersActions.setScore, value);
		this.$store.dispatch(SearchActions.getResults, {theSameTab: true});
	}

	get scoreOptions() {
		return this.$store.getters[FacetedFiltersGetters.scoreOptions];
	}

	get currentResults() {
		return this.$store.getters[SearchGetters.currentResults];
	}

	get containerVisible() {
		return this.$store.getters[GeneralFiltersGetters.containerVisible];
	}

	get generalFiltersInChange() {
		return this.$store.getters[GeneralFiltersGetters.inChangeProcess];
	}

	get dataSource() {
		return this.$store.getters[SearchGetters.dataSource];
	}

	private updateFilterValues(storeAction: string, filterName: string) {
		if (this.currentResults && this.currentResults.facetedFilters) {
			const facetedFilters = this.currentResults.facetedFilters;
			if (facetedFilters[filterName]) {
				this.$store.dispatch(storeAction, facetedFilters[filterName]);
			} else {
				this.$store.dispatch(storeAction, null);
			}
		}
	}

	private updateFilterOptions(storeAction: string, filter: string) {
		const options = [];
		if(this.currentResults && this.currentResults['dm:termFrequency']) {
			const filterOptionsRaw = this.currentResults['dm:termFrequency'][`dm:${filter}`];

			if(filterOptionsRaw && filterOptionsRaw[`dm:item`]) {

				if(filterOptionsRaw[`dm:item`].constructor !== Array) {
					filterOptionsRaw[`dm:item`] = [filterOptionsRaw[`dm:item`]];
				}

				for(const item of filterOptionsRaw[`dm:item`]) {
					let name = item['dm:decodedTerm'];
					if(filter === 'organisationName') {
						name = this.parseOrganizationName(name);
					} else if(filter === 'sscScore') {
						if (name === '0 - 20') {
							name = this.$t('fileDownloadsPopup.veryUnreliable');
						} else if (name === '21 - 40') {
							name = this.$t('fileDownloadsPopup.frequentlyUnavailable');
						} else if (name === '41 - 60') {
							name = this.$t('fileDownloadsPopup.sometimesUnavailable');
						} else if (name === '61 - 80') {
							name = this.$t('fileDownloadsPopup.mostlyAvailable');
						} else if (name === '81 - 100') {
							name = this.$t('fileDownloadsPopup.veryReliable');
						}
					}

					let id = item['dm:term'];
					const geossCrId = item['dm:id'];
					if (geossCrId) {
						id = geossCrId;
					} else if(filter === 'source') {
						id = item['dm:sourceId'];
					} else if(filter === 'sscScore') {
						id = `[${id.replace(' - ', ',')}]`;
					}

					options.push({
						text: `${name} (${item['dm:freq']})`,
						id
					});
				}
			}
		}

		this.$store.dispatch(storeAction, options);
	}

	private parseOrganizationName(name: string) {
		let orgName = name;
		let searchOrg = '';

		if (orgName.indexOf('u\'name\':') > -1) {
			const numberOrg = orgName.split('u\'name\':').length - 1;
			for (let i = 0; i < numberOrg; i++) {
				const start = orgName.indexOf('u\'name\':') + 11;
				const text = orgName.substring(start);
				const stop = text.indexOf('}') - 1;
				const fullName = text.substring(0, stop);
				orgName = text;
				searchOrg = searchOrg + fullName + ' ';
			}
			orgName = searchOrg;
		}

		return orgName;
	}

	@Watch('currentResults')
	private async onCurrentResultsChanged() {
		this.updateFilterOptions(FacetedFiltersActions.setKeywordOptions, 'keyword');
		this.updateFilterOptions(FacetedFiltersActions.setFormatOptions, 'format');
		this.updateFilterOptions(FacetedFiltersActions.setSourceOptions, 'source');
		this.updateFilterOptions(FacetedFiltersActions.setProtocolOptions, 'protocol');
		this.updateFilterOptions(FacetedFiltersActions.setOrganisationOptions, 'organisationName');
		this.updateFilterOptions(FacetedFiltersActions.setScoreOptions, 'sscScore');

		this.updateFilterValues(FacetedFiltersActions.setKeyword, 'kwd');
		this.updateFilterValues(FacetedFiltersActions.setFormat, 'frmt');
		this.updateFilterValues(FacetedFiltersActions.setSource, 'sources');
		this.updateFilterValues(FacetedFiltersActions.setProtocol, 'prot');
		this.updateFilterValues(FacetedFiltersActions.setOrganisation, 'organisationName');
		this.updateFilterValues(FacetedFiltersActions.setScore, 'sscScore');
	}
}
</script>

<style lang="scss">
.faceted-filters {
	padding: 0;

	&__filters-hide {
		color: white;
		font-size: 18px;
		font-weight: bold;
		white-space: nowrap;
		padding: 13px 20px;
		background-color: $blue-transparent;
		width: 100%;
		text-align: right;
		margin-top: 2px;
	}

	&__filter {
		width: calc(33.33% - 13px);
		margin-bottom: 5px;

		@media (max-width: $breakpoint-md) {
			width: calc(50% - 13px);
		}

		@media (max-width: $breakpoint-sm) {
			width: 100%;
		}
	}
}
</style>