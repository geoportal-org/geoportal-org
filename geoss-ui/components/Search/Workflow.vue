<template>
	<div class="workflow">
		<div class="workflow__top">
			<div class="workflow__title">
				<button @click="cancel()" :title="$t('popupContent.cancel')"></button>
				<i class="icomoon-data-source--services"></i>
				<span>{{workflow.name}}</span>
			</div>
			<div class="d-flex flex--align-center flex--justify-end flex--1">
				<button class="workflow__action cancel" @click="cancel()">{{$t('popupContent.cancel')}}</button>
				<button class="workflow__action" @click="accept()">{{$t('popupContent.accept')}}</button>
			</div>
		</div>
	</div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';

import { SearchGetters } from '@/stores/search/search-getters';
import { SearchActions } from '@/stores/search/search-actions';
import { GeneralFiltersGetters } from '@/stores/general-filters/general-filters-getters';
import UtilsService from '@/services/utils.service';
import { AppVueObj } from '@/data/global';

@Component
export default class WorkflowComponent extends Vue {
	get workflow() {
		return this.$store.getters[SearchGetters.workflow];
	}

	public cancel() {
		const selectedWorkflow = this.$store.getters[SearchGetters.workflow];
		const previousState = JSON.parse(AppVueObj.storeStateBackup);
		previousState.search.workflow = selectedWorkflow;
		previousState.popup.queue = [];
		previousState.popup.promises = [];
		UtilsService.popFromHistory(JSON.stringify(previousState));
	}

	public accept() {
		const selectedResource = this.$store.getters[SearchGetters.workflowResource];
		const selectedWorkflow = this.$store.getters[SearchGetters.workflow];

		const workflowPlatformData = this.$store.getters[SearchGetters.workflowPlatformData];
		const workflowPlatformDataLoading = this.$store.getters[SearchGetters.workflowPlatformDataLoading];
		const workflowCloudPlatforms = this.$store.getters[SearchGetters.workflowCloudPlatforms];
		const workflowSelectedPlatform = this.$store.getters[SearchGetters.workflowSelectedPlatform];
		const workflowOptimalPlatform = this.$store.getters[SearchGetters.workflowOptimalPlatform];
		const workflowCoordinates = this.$store.getters[GeneralFiltersGetters.state].selectedAreaCoordinates;

		const previousState = JSON.parse(AppVueObj.storeStateBackup);
		previousState.search.workflowResource = selectedResource;
		previousState.search.workflow = selectedWorkflow;
		previousState.search.workflowPlatformData = workflowPlatformData;
		previousState.search.workflowPlatformDataLoading = workflowPlatformDataLoading;
		previousState.search.workflowCloudPlatforms = workflowCloudPlatforms;
		previousState.search.workflowSelectedPlatform = workflowSelectedPlatform;
		previousState.search.workflowOptimalPlatform = workflowOptimalPlatform;
		previousState.search.workflowCoordinates = workflowCoordinates;
		previousState.generalFilters.workflowMapDraw = false;
		previousState.popup.queue = [];
		previousState.popup.promises = [];
		UtilsService.popFromHistory(JSON.stringify(previousState));
	}
}
</script>

<style lang="scss" scoped>
.workflow {
	background-color: $blue-transparent;
	padding: 8px 20px;
	margin-top: 5px;

	&__top {
		display: flex;
		justify-content: space-between;
		align-items: center;

		@media (max-width: $breakpoint-sm) {
			flex-wrap: wrap;
		}
	}

	&__action {
		padding: 5px 20px;
		color: white;
		background-color: $blue-light;
		border-radius: 15px;
		font-style: italic;

		&.cancel {
			background-color: white;
			color: $blue-light;
			margin-right: 15px;
		}
	}

	&__title {
		color: white;
		font-size: 20px;
		display: flex;
		align-items: center;

		@media (max-width: $breakpoint-sm) {
			width: 100%;
			margin-bottom: 20px;
		}

		button {
			min-width: 23px;
			min-height: 23px;
			border-radius: 50%;
			border: 2px solid white;
			position: relative;
			margin-right: 15px;
			flex-shrink: 0;

			&:before, &:after {
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

		span {
			overflow: hidden;
			text-overflow: ellipsis;
			padding: 0 10px;
			background: none;
			border: none;
		}
	}
}
</style>