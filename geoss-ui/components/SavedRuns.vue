<template>
	<div class="saved-runs">
		<div v-if="workflow && workflowRunName" class="d-flex flex--justify-between flex--align-center service-workflow__add-run">
			<input placeholder="Name" class="service-workflow__add-run-name" type="text" v-model="addRunName" />
			<input placeholder="Run ID" class="service-workflow__add-run-id" type="text" v-model="addRunId" />
			<button class="service-workflow__add-run-btn" :disabled="!addRunId || !addRunName" @click="addRun()">{{$tc('popupContent.add')}}</button>
		</div>
		<div v-show="loading" :class="{'separate-popup': !workflow && !workflowRunName}">{{$tc('popupContent.loadingData')}}...</div>
		<div v-show="!loading && (!savedRuns || !savedRuns.length)" :class="{'separate-popup': !workflow && !workflowRunName}">{{$tc('popupContent.noRunsAvailable')}}</div>
		<div class="service-workflow__saved-runs" v-if="savedRuns">
			<div :class="{'separate-popup': !workflow && !workflowRunName}">
				<div class="service-workflow__saved-run" v-for="savedRun of savedRuns" :key="savedRun.id">
					<div class="d-flex">
						<div class="service-workflow__saved-run-info">
							<div class="service-workflow__saved-run-name">{{savedRun.name}}</div>
							<div class="service-workflow__saved-run-id">{{$tc('popupContent.id')}}: {{savedRun.id}}</div>
							<div v-if="savedRun.outputs && savedRun.status === 'COMPLETED' && savedRun.result !== 'FAIL'" class="service-workflow__saved-outputs">
								<div class="service-workflow__saved-outputs__label">{{$tc('popupContent.outputs')}}:</div>
								<div v-for="output of savedRun.outputs" :key="output.id" class="service-workflow__saved-output">
									<div v-if="output.value && output.valueSchema === 'url'" class="d-flex flex--align-center">
										<button @click="downloadOutput(output.value)" :title="$tc('dabResult.downloadNow')">
											<i class="icomoon-download"></i>
										</button>
										<span>{{output.name}}</span>
										<div v-if="output.description" class="service-workflow__saved-output-link-description">{{output.description}}</div>
									</div>
									<div v-else-if="output.value && output.value.url" class="d-flex flex--align-center">
										<button @click="showOnMap(savedRun.runId, output)" :title="$tc('dabResult.showOnMap')">
											<i class="icomoon-show-on-map"></i>
										</button>
										<span>{{output.name}}</span>
									</div>
									<div v-else class="d-flex flex--align-center">
										<button disabled="true" :title="$tc('dabResult.showOnMap')">
											<i class="icomoon-show-on-map"></i>
										</button>
										<span>{{output.name}}</span>
									</div>
								</div>
							</div>
							<div class="service-workflow__saved-run-button-holder d-flex">
								<button class="service-workflow__saved-run-btn" :class="{active: savedRun.showLogs}" @click="toggleLogs(savedRun)" v-if="savedRun.messageList && savedRun.messageList.length">{{$tc('popupContent.messageLog')}}</button>
								<button class="service-workflow__saved-run-btn link" @click="createDashboard(savedRun)" v-if="savedRun.outputs && savedRun.outputs.length && savedRun.result !== 'FAIL'">{{$tc('popupContent.createDashboard')}}</button>
							</div>

						</div>
						<div class="service-workflow__saved-run-status">
							<span class="service-workflow__saved-run-status__badge" :class="{success: savedRun.status === 'COMPLETED' && savedRun.result === 'SUCCESS'}">
								{{savedRun.status}}<span v-if="savedRun.result"> - {{ savedRun.result }}</span>
							</span>
						</div>
					</div>
					<div>
						<CollapseTransition v-if="savedRun.messageList && savedRun.messageList.length">
							<div v-show="savedRun.showLogs" class="service-workflow__saved-logs">
								<div v-for="log of savedRun.messageList" :key="log">{{log}}</div>
							</div>
						</CollapseTransition>
					</div>
				</div>
			</div>
			<Pagination v-if="savedRuns && savedRuns.length"
				:start-index="runsStartIndex"
				:results-per-page="runsResultsPerPage"
				:total="runsTotal"
				@on-start-index-change="onRunsStartIndexChange($event)"/>
		</div>
	</div>
</template>

<script lang="ts">
import { runsTest } from '@/data/saved-runs-test';
import { Component, Vue, Prop } from 'nuxt-property-decorator';
import GeossSearchApiService from '@/services/geoss-search.api.service';
import to from '@/utils/to';
import NotificationService from '@/services/notification.service';
import PopupCloseService from '@/services/popup-close.service';
import LayersUtils from '@/services/map/layer-utils';
import { MapActions } from '@/store/map/map-actions';
import { LayerTypes } from '@/interfaces/LayerTypes';
import { Timers } from '@/data/timers';
import { UserGetters } from '@/store/user/user-getters';
import Pagination from '@/components/Pagination.vue';
import DashboardCreator from '@/components/Search/Results/Dashboard/DashboardCreator.vue';
import { PopupActions } from '@/store/popup/popup-actions';

@Component({
	components: {
		Pagination
	}
})
export default class SavedRunsComponent extends Vue {
	@Prop({required: false, type: String}) public workflowRunName!: string;
	@Prop({required: false, type: Object}) public workflow!: any;

	public savedRuns: any = null;
	public runsResultsPerPage = 5;
	public runsStartIndex = 0;
	public runsTotal = 0;
	public addRunId = '';
	public addRunName = '';
	public loading = false;

	get isSignedIn() {
		return this.$store.getters[UserGetters.isSignedIn];
	}

	public async addRun() {
		const run: any = {};
		const [err, {status, messageList, result}] = await to(GeossSearchApiService.getRunStatus(this.addRunId));
		if(!err) {
			GeossSearchApiService.addSavedRun(this.workflowRunName, this.workflow, this.addRunId).then(() => {
				run.status = status;
				run.messageList = messageList;
				run.result = result;
				run.showLogs = false;
				if(status === 'COMPLETED' || status === 'SUCCESS') {
					return GeossSearchApiService.getRunStatus(this.addRunId).then(({outputs}: any) => {
						run.outputs = outputs;
						run.showOutputs = false;
					});
				}
			});
		} else {
			NotificationService.show(
				`${this.$tc('popupTitles.savedRuns')}`,
				`Analysis with run ID ${this.addRunId} does not exist.`,
				10000,
				undefined,
				9999,
				'error'
			);
		}
	}

	public async showOnMap(runId: string, output: any) {
		const {url, name, protocol, legend} = output.value;
		let coordinate;
		if (output.aoi && output.aoi.geometry && output.aoi.geometry.bbox && output.aoi.geometry.bbox.length) {
			const bbox = output.aoi.geometry.bbox;
			coordinate = {
				W: bbox[0] * 1,
				S: bbox[1] * 1,
				E: bbox[2] * 1,
				N: bbox[3] * 1
			};
		} else {
			[, coordinate] = await to(GeossSearchApiService.getRunCoordinates(runId));
		}

		this.close();

		let version = '1.1.1';
		if(protocol) {
			if(protocol.indexOf('WebMapService') > -1) {
				const match = /((\d)+\.(\d)+(\.(\d)+)?)/g.exec(protocol);
				if (match) {
					version = match[0];
				}
			}
		}

		const layerUrl = `${url}VERSION=${version}&LAYERS=${name}&TILED=true&`;
		const layer = LayersUtils.createWMS(name, layerUrl);

		this.$store.dispatch(MapActions.addLayer, {
			layer,
			id: name,
			type: LayerTypes.CUSTOM,
			coordinate,
			title: output.name,
			legend: {
				type: 'img',
				data: legend
			}
		});

		this.$store.dispatch(MapActions.changeLayerVisibility, {id: LayerTypes.BOUNDING, value: false});
		this.$store.dispatch(MapActions.setShowFull, true);

		setTimeout(() => {
			this.$store.dispatch(MapActions.zoomInLayer, name);
		}, (Timers.hideSearchContainer > Timers.closePopup ? Timers.hideSearchContainer : Timers.closePopup));
	}

	public toggleLogs(run: any) {
		run.showLogs = !run.showLogs;
		run.showOutputs = false;
	}

	public toggleOutputs(run: any) {
		run.showOutputs = !run.showOutputs;
		run.showLogs = false;
	}

	public async onRunsStartIndexChange(startIndex: number) {
		this.runsStartIndex = startIndex;
		this.getSavedRuns();
	}

	public async getSavedRuns() {
		const [, data] = await to(GeossSearchApiService.getSavedRuns(this.isSignedIn, this.runsStartIndex, this.runsResultsPerPage));

		// test data
		// data = runsTest;

		if(data) {
			this.savedRuns = data.items;
		}
		return data;
	}

	public close() {
		if (this.workflow) {
			PopupCloseService.closePopup('workflow');
		} else {
			PopupCloseService.closePopup('saved-runs');
		}
	}

	public downloadOutput(url: string) {
		if (url && url !== '') {
			window.open(url, '_blank');
		}
	}

	public async createDashboard(savedRun: any) {
		PopupCloseService.closePopup('saved-runs');
		const props = {
			savedRun,
			protected: {
				message: this.$tc('popupContent.creatorCloseConfirmation')
			}
		};
		this.$store.dispatch(PopupActions.openPopup, {contentId: 'dashboard-creator', title: this.$tc('popupTitles.dashboards'), component: DashboardCreator, props });
	}

	private async mounted() {
		await this.$nextTick();

		if(!this.savedRuns) {
			this.loading = true;
			const data = await this.getSavedRuns();
			if(data) {
				this.runsTotal = data.totalCount;
			}
			this.loading = false;
		}
	}

}
</script>

<style lang="scss">
.saved-runs {
	.separate-popup {
		padding: 20px 30px;
	}

	.service-workflow {
		&__saved-run {
			border-bottom: 1px solid $grey-lighter;
			padding: 20px 0px;

			@media(max-width: $breakpoint-lg) {
				> .d-flex {
					flex-direction: column-reverse;
				}
			}

			&:last-child {
				border-bottom: none;
			}

			&-name,
			&-id,
			&-status {
				font-weight: bold;
				margin-bottom: 5px;
			}

			&-name {
				font-weight: bold;
				font-size: 1.2em;
			}

			&-button-holder {
				margin-top: 15px;
			}

			&-btn {
				font-size: 17px;
				padding: 7px 20px;
				border: none;
				margin-right: -1px;
				background: $blue;
				color: $white;

				&.active {
					color: $blue;
					background: #efefef;
				}

				&.link {
					margin-left: 20px;
					font-weight: bold;
					color: $blue;
					background: none;
					border-left: 1px solid #efefef;
				}

			}
		}

		&__saved-logs {
			padding: 15px;
			background: #efefef;

			div {
				padding-bottom: 5px;

				&:first-child {
					padding-top: 10px;
				}

				&:last-child {
					padding-bottom: 10px;
				}
			}
		}

		&__saved-outputs__label {
			margin: 10px 0 7px;
			font-weight: bold;
		}

		&__saved-output {
			padding-bottom: 5px;

			&:first-child {
				padding-top: 10px;
			}

			&:last-child {
				padding-bottom: 10px;
			}

			a {
				color: $blue;
			}

			button {
				background-color: $yellow;
				color: white;
				padding: 3px;
				border-radius: 50%;
				width: 25px;
				height: 25px;
				display: flex;
				align-items: center;
				justify-content: center;
				margin-right: 5px;
			}
		}

		&__add-run {
			margin-bottom: 15px;
			border-bottom: 1px solid $grey-lighter;
			padding: 15px 0;

			&-id,
			&-name,
			&-btn {
				width: 30%;

				@media(max-width: $breakpoint-sm) {
					width: 100%;
				}
			}

			&-id,
			&-name {
				border: 2px solid $blue;
				height: 37px;
				outline: 0;
				padding: 0 5px;
				font-size: 17px;
			}
		}

		&__saved-run-info {
			width: 100%;
		}

		&__saved-run-status {
			padding-left: 50px;

			@media(max-width: $breakpoint-lg) {
				padding: 0;
				margin-bottom: 15px;
			}

			&__badge {
				background: $grey-dark;
				color: $white;
				padding: 4px 8px;
				font-size: 0.71em;
				border-radius: 5px;
				white-space: nowrap;

				&.success {
					background: green;
				}
			}
		}

		.pagination {
			position: absolute;
			bottom: 0;
			left: 0;
			width: 100%;
			background-color: rgba($green, 0.95);
			margin-bottom: 0;
		}
	}
}
</style>
