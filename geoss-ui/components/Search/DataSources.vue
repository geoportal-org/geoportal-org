<template>
	<div v-if="showComponent" class="data-sources" :class="{side: searchResultsActive}">
		<button
			data-tutorial-tag="search-container-datasource-data"
			class="data-sources__item disabled-transparent" 
			:disabled="workflow || !anyResultFromDataTabAvailable" 
			v-show="searchResultsActive && !dataTabHidden" 
			@click="selectDataSource(DataSources.DAB)" 
			:class="{
				active: activeTab === 'data',
				'loading-source': loading
			}">
			<i class="icomoon-data-source--data"></i>
			<span>{{$t('dataSources.dab')}}</span>
			<img class="tab_spinner" :src="`${staticPath()}/img/spinner.png`" alt="Spinner" />
		</button>
		<button
			data-tutorial-tag="search-container-datasource-information"
			class="data-sources__item disabled-transparent" 
			v-show="searchResultsActive && !informationTabHidden" 
			:disabled="workflow || !anyResultFromInformationTabAvailable" 
			@click="selectDataSource(DataSources.INFORMATION)" 
			:class="{
				active: activeTab === 'information',
				'loading-source': loading
			}">
			<i class="icomoon-data-source--information"></i>
			<span>{{$t('dataSources.information')}}</span>
			<img class="tab_spinner" :src="`${staticPath()}/img/spinner.png`" alt="Spinner" />
		</button>
		<button
			data-tutorial-tag="search-container-datasource-services"
			class="data-sources__item disabled-transparent" 
			v-show="searchResultsActive && !servicesTabHidden" 
			:disabled="workflow || !anyResultFromServicesTabAvailable" 
			@click="selectDataSource(DataSources.SERVICES)" 
			:class="{
				active: activeTab === 'services',
				'loading-source': loading
			}">
			<i class="icomoon-data-source--services"></i>
			<span>{{$t('dataSources.services')}}</span>
			<img class="tab_spinner" :src="`${staticPath()}/img/spinner.png`" alt="Spinner" />
		</button>
	</div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from 'vue-property-decorator';
import { SearchActions } from '@/stores/search/search-actions';
import { DataSource, DataSources, DataSourceGroup } from '@/interfaces/DataSources';
import { SearchGetters } from '@/stores/search/search-getters';
import UtilsService from '@/services/utils.service';

@Component
export default class DataSourcesComponent extends Vue {
	public DataSources = DataSources;
	public loading = this.otherSourcesLoading;

	get workflow() {
		return this.$store.getters[SearchGetters.workflow];
	}

	get dataSource(): DataSource {
		return this.$store.getters[SearchGetters.dataSource];
	}

	get searchResultsActive() {
		return this.$store.getters[SearchGetters.resultsActive];
	}

	get dabResults() {
		return !!this.$store.getters[SearchGetters.dabResults];
	}

	get dataResults() {
		return !!this.$store.getters[SearchGetters.dataResults];
	}

	get amerigeossResults() {
		return !!this.$store.getters[SearchGetters.amerigeossResults];
	}

	get nextgeossResults() {
		return !!this.$store.getters[SearchGetters.nextgeossResults];
	}

	get servicesResults() {
		return !!this.$store.getters[SearchGetters.servicesResults];
	}

	get informationResults() {
		return !!this.$store.getters[SearchGetters.informationResults];
	}

	get zenodoResults() {
		return !!this.$store.getters[SearchGetters.zenodoResults];
	}

	get wikipediaResults() {
		return !!this.$store.getters[SearchGetters.wikipediaResults];
	}

	get anyResultFromDataTabAvailable() {
		return this.dabResults || this.dataResults || this.amerigeossResults || this.nextgeossResults;
	}

	get anyResultFromInformationTabAvailable() {
		return this.informationResults || this.zenodoResults || this.wikipediaResults;
	}

	get anyResultFromServicesTabAvailable() {
		return this.servicesResults;
	}

	get dataTabHidden() {
		return !!(this.$store.getters[SearchGetters.hiddenDataSources].includes(DataSources.DAB)
			&& this.$store.getters[SearchGetters.hiddenDataSources].includes(DataSources.DATA)
			&& this.$store.getters[SearchGetters.hiddenDataSources].includes(DataSources.AMERIGEOSS)
			&& this.$store.getters[SearchGetters.hiddenDataSources].includes(DataSources.NEXTGEOSS));
	}

	get informationTabHidden() {
		return !!(this.$store.getters[SearchGetters.hiddenDataSources].includes(DataSources.INFORMATION)
			&& this.$store.getters[SearchGetters.hiddenDataSources].includes(DataSources.ZENODO)
			&& this.$store.getters[SearchGetters.hiddenDataSources].includes(DataSources.WIKIPEDIA));
	}

	get servicesTabHidden() {
		return !!this.$store.getters[SearchGetters.hiddenDataSources].includes(DataSources.SERVICES);
	}

	get showComponent() {
		const noOfTabs = Number(!this.dataTabHidden) + Number(!this.informationTabHidden) + Number(!this.servicesTabHidden);
		return noOfTabs > 1 ? true : false;
	}

	get otherSourcesLoading() {
		return this.$store.getters[SearchGetters.otherSourcesLoading];
	}

	get activeTab() {
		return DataSourceGroup[this.dataSource];
	}

	public selectDataSource(dataSource: DataSource) {
		if(this.dataSource !== dataSource) {
			if(dataSource === DataSources.DAB) {
				if (!this.dabResults) {
					if (this.dataResults) {
						dataSource = DataSources.DATA;
					} else if (this.amerigeossResults) {
						dataSource = DataSources.AMERIGEOSS;
					} else {
						dataSource = DataSources.NEXTGEOSS;
					}
				}
			}
			if(dataSource === DataSources.INFORMATION && !this.informationResults) {
				if (this.zenodoResults) {
					dataSource = DataSources.ZENODO;
				} else {
					dataSource = DataSources.WIKIPEDIA;
				}
			}
			this.$store.dispatch(SearchActions.setResultIdDetails, null);
			this.$store.dispatch(SearchActions.setDataSource, { value: dataSource });
			UtilsService.pushToHistory();
		}
	}

	@Watch('otherSourcesLoading')
	private onLoadingChange() {
		this.loading = this.otherSourcesLoading;
	}
}
</script>

<style lang="scss">
.data-sources {
	display: flex;
	align-items: stretch;
	flex: 0 0 auto;
	margin-right: 5px;

	@media(max-width: $breakpoint-md) {
		width: 100%;
		margin-right: 0;
		margin-bottom: 5px;
	}
	
	&.side {
		position: absolute;
		z-index: 1;
		right: 0;
		top: 0;
		transform: translateX(calc(100% + 5px));
		flex-direction: column;
		justify-content: flex-start;
		margin-top: 0;
		width: auto;
	}

	.custom-select {
		width: 200px;

		@media(max-width: $breakpoint-md) {
			width: 100%;
		}

		&__trigger {
			height: 100%;
			font-size: 22px;
			background: rgba(170, 170, 170, 0.3);
			font-style: italic;
			color: white;
			padding: 6px 15px;
			box-shadow: 0 0 10px 0 rgba(0, 0, 0, 0.75);

			@media(max-width: $breakpoint-md) {
				padding: 15px;
			}

			@media(max-width: $breakpoint-sm) {
				padding: 10px 15px;
			}

			@media (max-width: $breakpoint-xs) {
				font-size: 18px;
			}

			&.active {
				background-color: white;

				i {
					color: $blue;
				}

				span {
					color: $grey-darker;
				}

				&:after {
					border-top-color: $grey-darker;
				}
			}

			&:after {
				border-top: 6px solid white;
				border-right: 6px solid transparent;
				border-left: 6px solid transparent;
			}
			
			i {
				font-size: 30px;
				margin-right: 25px;

				@media (max-width: $breakpoint-sm) {
					margin-right: 15px;
				}

				@media (max-width: $breakpoint-xs) {
					font-size: 25px;
				}
			}
		}

		&__container {
			top: 100%;
			border: none;
			border-radius: 0;
		}

		&__option {
			display: flex;
			align-items: center;
			padding-left: 15px;
			padding-right: 15px;

			@media(max-width: $breakpoint-md) {
				padding: 15px;
			}

			@media(max-width: $breakpoint-sm) {
				padding: 10px 15px;
			}

			&.disabled {
				i,
				span {
					color: $grey-dark;
				}
			}

			i {
				margin-right: 25px;
				font-size: 30px;
				color: $blue;

				@media (max-width: $breakpoint-xs) {
					font-size: 25px;
				}
			}

			span {
				font-size: 22px;
				color: $grey-darker;
				font-style: italic;

				@media (max-width: $breakpoint-xs) {
					font-size: 18px;
				}
			}

		}
	}

	&__item {
		margin-bottom: 5px;
		background-color: rgba($green-dark, 0.85);
		border-radius: 0;
		width: 90px;
		height: 90px;
		font-size: 55px;
		position: relative;
		color: white;

		@media(max-width: $breakpoint-md) {
			width: 75px;
			height: 75px;
			font-size: 40px;
		}

		@media(max-width: $breakpoint-sm) {
			width: 50px;
			height: 50px;
			font-size: 30px;
		}

		&:hover,
		&.active {
			background-color: white;
			color: $green-dark;
		}

		&:hover span {
			visibility: visible;
		}

		&[disabled] {
			background-color: rgba($grey-darker, 0.65);
			color: rgba(white, 0.5) !important;

			span {
				background-color: rgba($grey-darker, 0.65);
				color: rgba(white, 0.85) !important;
			}
		}

		span {
			font-size: 15px;
			position: absolute;
			top: 50%;
			left: calc(100% + 15px);
			transform: translateY(-50%);
			background: $grey-darker;
			color: white;
			padding: 3px 10px;
			border-radius: 15px;
			margin-top: 0;
			visibility: hidden;
			transition: opacity 250ms ease-in-out;
			text-transform: uppercase;

			@media(max-width: $breakpoint-lg) {
				display: none;
			}
		}
	}
	.tab_spinner {
    	animation: spin 2s linear infinite;
		left: 25%;
		position: absolute;
		top: 25%;
		width: 50%;
		opacity: 0;
		transition: opacity 300ms ease-in-out;
	}

	.loading-source {
		&.active {
			.tab_spinner {
				opacity: 0;
			}
		}
		.tab_spinner {
			opacity: 1;
		}
	}

	@keyframes spin { 
		0% { 
			transform: rotate(0deg); 
		}
		100% { 
			transform: rotate(360deg); 
		} 
	}
}
</style>