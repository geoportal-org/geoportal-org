<template>
<div>
	<DashboardDisplay v-if="previewMode" :data="prepareDashboardData()" :preview="true" @closePreview="closePreview()" />
	<div v-else-if="!isMobile()" class="dashboard-creator">
		<div class="dashboard-creator__header">
			<div class="dashboard-creator__header__top">
				<div>
					<div class="dashboard-creator__based-on">
						{{ $tc('popupContent.dashboardBasedOn') }}:
					</div>
					<div class="dashboard-creator__title">
						{{ savedRun.name }}
					</div>
					<div class="dashboard-creator__run-id">
						<span>{{ savedRun.runId }}</span>
						<div class="dashboard-creator__saved-run-status">
							<span class="dashboard-creator__saved-run-status__badge" :class="{success: savedRun.status === 'COMPLETED' && savedRun.result === 'SUCCESS'}">
								{{savedRun.status}}<span v-if="savedRun.result"> - {{ savedRun.result }}</span>
							</span>
						</div>
					</div>
				</div>
				<div>
					<button class="blue-btn-link" @click="preview()">{{ $tc('popupContent.preview') }}</button>
					<!-- <button class="blue-btn-link" @click="downloadAsPdf()">{{ $tc('popupContent.downloadAsPdf') }}</button> -->
					<button class="blue-btn-default" @click="saveAsDraft()">{{ $tc('popupContent.saveAsDraft') }}</button>
				</div>
			</div>
			<div class="dashboard-creator__header__middle">
				<input class="dashboard-creator__name-input" type="text" :placeholder="$tc('popupContent.dashboardName') + '...'" v-model="title" />
				<textarea class="dashboard-creator__description" :placeholder="$tc('feedback.placeholder') + ' (max. 500 characters)'" v-model="summary" maxlength="500"></textarea>
			</div>
			<div class="dashboard-creator__header__bottom">
				<DashboardPagination :currentPage="currentPage" :pageCount="pages.length" @previous="previousPage()" @next="nextPage()" />
			</div>
		</div>

		<div v-if="pages.length" class="dashboard-creator__contents">
			<div class="dashboard-creator__panel">
				<div class="dashboard-creator__panel__label">
					{{ $tc('popupContent.tools') }}
				</div>
				<div class="dashboard-creator__tool">
					<div class="dashboard-creator__tool group">
						<LayoutIcon />
					</div>
					<div class="dashboard-creator__tool tool-label">{{ $tc('popupContent.layout') }}</div>
					<div class="dashboard-creator__tool sidebar">
						<div>
							<label v-for="layoutId of [1, 2, 3, 4, 5, 6]" :class="{active: currentPageLayout(layoutId)}" :title="$tc('popupContent.layout') + ' ' + layoutId">
								<component :is="`LayoutIcon`+layoutId"></component>
								<input type="radio" name="layout" @change="changeLayout(layoutId)" :checked="currentPageLayout(layoutId)" :value="layoutId" />
							</label>
						</div>
					</div>
				</div>
				<div class="dashboard-creator__tool">
					<div class="dashboard-creator__tool group">
						<DescriptionIcon />
					</div>
					<div class="dashboard-creator__tool tool-label">{{ $tc('popupContent.description') }}</div>
					<div class="dashboard-creator__tool sidebar">
						<div>
							<div draggable @dragstart="startDrag($event, 'richtext')" :title="$tc('popupContent.textEditor')"><DescriptionIcon /></div>
						</div>
					</div>
				</div>
				<div class="dashboard-creator__tool">
					<div class="dashboard-creator__tool group">
						<ChartIcon />
					</div>
					<div class="dashboard-creator__tool tool-label">{{ $tc('popupContent.chart') }}</div>
					<div class="dashboard-creator__tool sidebar">
						<div>
							<select v-model="selectedChart">
								<option v-for="chart of chartData" :value="chart.title">
									{{ chart.title }}
								</option>
							</select>
							<div v-show="selectedChart === chart.title" v-for="chart of chartData" draggable @dragstart="startDrag($event, 'chart', chart)" :title="chart.title"><ChartIcon /></div>
						</div>
					</div>
				</div>
				<div class="dashboard-creator__tool">
					<div class="dashboard-creator__tool group">
						<MapIcon />
					</div>
					<div class="dashboard-creator__tool tool-label">{{ $tc('popupContent.map') }}</div>
					<div class="dashboard-creator__tool sidebar">
						<div>
							<select v-model="selectedMap">
								<option v-for="map of mapData" :value="map.outputName">
									{{ map.outputName }}
								</option>
							</select>
							<div v-show="selectedMap === map.outputName" v-for="map of mapData" draggable @dragstart="startDrag($event, 'map', map)" :title="map.outputName"><MapIcon /></div>
						</div>
					</div>
				</div>
				<div class="dashboard-creator__tool">
					<div class="dashboard-creator__tool group page-order cursor-pointer" @click="togglePageOrder()">
						<PageOrderIcon />
					</div>
					<div class="dashboard-creator__tool tool-label">{{ $tc('popupContent.pageOrder') }}</div>
				</div>
				<div class="dashboard-creator__tool">
					<div class="dashboard-creator__tool group cursor-pointer" @click="addPage(true)" :title="$tc('popupContent.addPage')">
						<AddIcon />
					</div>
					<div class="dashboard-creator__tool tool-label">{{ $tc('popupContent.add') }}</div>
				</div>
				<div class="dashboard-creator__tool">
					<div class="dashboard-creator__tool group cursor-pointer" :disabled="pages.length === 1" @click="removePage(true)" :title="$tc('popupContent.removePage')">
						<RemoveIcon />
					</div>
					<div class="dashboard-creator__tool tool-label">{{ $tc('popupContent.remove') }}</div>
				</div>
			</div>
			<div class="dashboard-creator__layout-wrapper" ref="dashboardContents">
				<template v-for="(page, pageIndex) of pages">
					<div v-if="(pageIndex + 1 === Number(currentPage))" class="layout" :class="'layout-' + page.layoutClass" :key="pageIndex">
						<template v-for="(field, fieldIndex) of page.fields">
							<section v-if="generateField(page.layoutClass, fieldIndex)" :key="fieldIndex">
								<button v-if="field.type !== 'empty'" class="dashboard-creator__clear" @click="clearField(fieldIndex)" :title="$tc('popupContent.clearArea')">✕</button>
								<div class="empty-section" v-if="field.type === 'empty' && generateField(page.layoutClass, fieldIndex)" @drop="onDrop($event, fieldIndex)" @dragover.prevent @dragenter.prevent>
									<span v-html="$tc('popupContent.dragAndDropDescription')"></span>
								</div>
								<DashboardRichText v-if="field.type === 'richtext'" :textData="field.value" mode="creator" @change="updateRichText($event, field)"/>
								<DashboardMap v-if="field.type === 'map'" :mapData="field.value"/>
								<DashboardChart v-if="field.type === 'chart'" :chartData="field.value" mode="creator" @change="updateChartMode($event, field)" :layoutChange="layoutChangeTrigger"/>
							</section>
						</template>
					</div>
				</template>
			</div>
		</div>

		<div class="dashboard-creator__footer">
			<DashboardPagination :currentPage="currentPage" :pageCount="pages.length" @previous="previousPage()" @next="nextPage()" />
		</div>

		<div v-if="showPageOrder" class="dashboard-creator__page-order">
			<div class="header">
				{{ $tc('popupContent.changePageOrder') }}
				<button class="close" @click="togglePageOrder()">✕</button>
			</div>
			<div class="content">
				<p>{{ $tc('popupContent.dragAndDropPageIcons') }}</p>
				<div class="pages" :class="{dragging: pageOrderDrag}">
					<template v-for="page of pagesNewOrder">
						<div class="drop-slot" @drop="onDropOrder($event, pagesNewOrder.indexOf(page))" @dragover="onDragoverOrder($event)" @dragleave="onDragleaveOrder($event)" @dragover.prevent @dragenter.prevent></div>
						<div :key="pages.indexOf(page)" class="page" draggable @dragstart="startDragOrder($event, pagesNewOrder.indexOf(page))" @dragend="endDragOrder()">
							<div class="page__icon">
								<component :is="`LayoutIcon`+page.layoutClass"></component>
							</div>
							<div class="page__name">
								{{ $tc('popupContent.page') }} {{ pages.indexOf(page) + 1 }}
							</div>
						</div>
					</template>
					<div class="drop-slot" @drop="onDropOrder($event, pagesNewOrder.length)" @dragover="onDragoverOrder($event)" @dragleave="onDragleaveOrder($event)" @dragover.prevent @dragenter.prevent></div>
				</div>
			</div>
			<div class="footer">
				<button class="blue-btn-default" @click="saveNewOrder()">{{ $tc('popupContent.save') }}</button>
			</div>
		</div>
	</div>
	<div v-else-if="isMobile()" class="dashboard-creator">
		<p class="dashboard-creator__mobile-error">Please use <b>desktop version</b> to create dashboards.</p>
	</div>
</div>
</template>

<script lang="ts">
// @ts-nocheck
import { Prop, Component, Vue } from 'nuxt-property-decorator';
import DashboardRichText from './DashboardRichText.vue';
import DashboardMap from './DashboardMap.vue';
import DashboardChart from './DashboardChart.vue';
import DashboardPagination from './DashboardPagination.vue';
import DashboardDisplay from './DashboardDisplay.vue';
import DashboardService from '@/services/dashboard.service';
import * as XLSX from 'xlsx';

@Component({
	components: {
		DashboardRichText,
		DashboardMap,
		DashboardChart,
		DashboardPagination,
		DashboardDisplay,
	}
})
export default class DashboardCreatorComponent extends Vue {
	@Prop({type: Object}) public savedRun!: any = null;
	@Prop({type: Object}) public dashboardData!: any = null;

	public previewMode = false;
	public title = '';
	public summary = '';
	public mapData = [];
	public chartData = [];
	public selectedChart = '';
	public selectedMap = '';
	public layoutChangeTrigger = true;
	public showPageOrder = false;
	public pageOrderDrag = false;

	public emptyField = {
		empty: {
			type: 'empty',
			value: ''
		},
		richtext: {
			type: 'richtext',
			value: ''
		},
		chart: {
			type: 'chart',
			value: null
		},
		map: {
			type: 'map',
			value: null
		},
	};

	public currentPage = 1;
	public pages = [];
	public pagesNewOrder = [];
	public layout = 1;

	public isMobile() {
		let check = false;
		(a => {
			if (/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows ce|xda|xiino|android|ipad|playbook|silk/i.test(a) || /1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(a.substr(0, 4))) {
				check = true;
			}
		})(navigator.userAgent || navigator.vendor || (window as any).opera);
		return check;
	}

	public async previousPage() {
		await this.$nextTick();
		if (this.currentPage > 1) {
			this.currentPage--;
		}
	}

	public async nextPage() {
		await this.$nextTick();
		if (this.currentPage < this.pages.length) {
			this.currentPage++;
		}
	}

	public changeLayout(id) {
		const page = this.pages[this.currentPage - 1];
		page.layoutClass = id;
		this.layoutChangeTrigger = !this.layoutChangeTrigger;
	}

	public currentPageLayout(id) {
		return this.pages[this.currentPage - 1].layoutClass === id;
	}

	public generateField(layoutClass, index) {
		return ((layoutClass === 1 && index === 0) || (layoutClass === 2 && index < 2) || (layoutClass > 2 && index < 3));
	}

	public newPage() {
		return {
			layoutClass: 1,
			fields: [ this.newField('empty'), this.newField('empty'), this.newField('empty')]
		};
	}

	public newField(type) {
		return {
			...this.emptyField[type]
		};
	}

	public addPage(focusOnNewPage = false) {
		if (this.pages.length > 9) {
			return false;
		}
		this.pages.splice(this.currentPage, 0, this.newPage());
		if (focusOnNewPage) {
			++this.currentPage;
		}
	}

	public removePage(focusOnNewPage = false) {
		if (this.pages.length > 1) {
			this.pages.splice(this.currentPage - 1, 1);
			if (focusOnNewPage && this.currentPage > 1) {
				--this.currentPage;
			}
		}
	}

	public startDrag(event, type, eventData = null) {
		event.dataTransfer.dropEffect = 'move';
		event.dataTransfer.effectAllowed = 'move';
		event.dataTransfer.setData('fieldType', type);
		if (eventData) {
			const data = JSON.stringify(eventData);
			event.dataTransfer.setData('eventData', data);
		}
	}

	public onDrop(event, fieldIndex) {
		const type = event.dataTransfer.getData('fieldType');
		const eventData = event.dataTransfer.getData('eventData');
		const fields = this.pages[this.currentPage - 1].fields;
		const fieldToAdd = this.newField(type);
		if (eventData) {
			fieldToAdd.value = JSON.parse(eventData);
		}
		Vue.set(fields, fieldIndex, fieldToAdd);
	}

	public togglePageOrder() {
		this.showPageOrder = !this.showPageOrder;
		if (this.showPageOrder) {
			this.pagesNewOrder = [...this.pages];
		} else {
			this.pagesNewOrder = [];
		}
	}

	public startDragOrder(event, index) {
		event.dataTransfer.setData('recentIndex', index);
		this.pageOrderDrag = true;
	}

	public endDragOrder() {
		this.pageOrderDrag = false;
	}

	public onDragoverOrder(event) {
		event.target.classList.add('ready-to-drop');
	}

	public onDragleaveOrder(event) {
		event.target.classList.remove('ready-to-drop');
	}

	public onDropOrder(event, targetIndex) {
		event.target.classList.remove('ready-to-drop');
		const recentIndex = event.dataTransfer.getData('recentIndex');
		this.pagesNewOrder.splice(targetIndex, 0, this.pagesNewOrder.splice(recentIndex, 1)[0]);
	}

	public saveNewOrder() {
		this.pages = this.pagesNewOrder;
		this.togglePageOrder();
		this.currentPage = 1;
	}

	public clearField(fieldIndex) {
		const fields = this.pages[this.currentPage - 1].fields;
		Vue.set(fields, fieldIndex, this.newField('empty'));
	}

	public updateRichText(event, field) {
		field.value = event;
	}

	public updateChartMode(event, field) {
		field.value.mode = event;
	}

	public prepareMapData() {
		const outputWithAoi = this.savedRun.outputs.filter(e => e.aoi)[0];
		let bbox = null;
		if (outputWithAoi) {
			bbox = outputWithAoi.aoi.geometry.bbox;
		}
		for (const output of this.savedRun.outputs) {
			if (output.valueSchema === 'wms') {
				const map = {
					runId: this.savedRun.runId,
					id: output.id,
					outputName: output.name,
					legend: output.value.legend,
					name: output.value.name,
					protocol: output.value.protocol,
					url: output.value.url,
					bbox
				};
				this.mapData.push(map);
				if (this.selectedMap === '') {
					this.selectedMap = output.name;
				}
			}
		}
	}

	public async prepareChartData() {
		for (const output of this.savedRun.outputs) {
			if (output.valueSchema === 'url' && output.value.indexOf('.xlsx') > -1) {
				const url = output.value;
				const file = await fetch(url).then(r => r.blob()).then(blobFile => new Blob([blobFile], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;' }));
				const reader = new FileReader();

				reader.onload = (element: any) => {
					const data = element.target.result;
					const workbook = XLSX.read(data, {
						type: 'binary'
					});
					workbook.SheetNames.forEach((sheetName, index) => {
						if (index === workbook.SheetNames.length - 1) {
							return;
						}
						const jsonSheet = XLSX.utils.sheet_to_json(workbook.Sheets[sheetName]);
						const title = Object.values(jsonSheet[0])[0];
						const label = Object.values(jsonSheet[1])[0];
						let labels = [Object.values(jsonSheet[3])[0], Object.values(jsonSheet[4])[0], Object.values(jsonSheet[5])[0], Object.values(jsonSheet[6])[0]];
						const values = [Object.values(jsonSheet[3])[1], Object.values(jsonSheet[4])[1], Object.values(jsonSheet[5])[1], Object.values(jsonSheet[6])[1]];

						labels = labels.map(label => {
							const labelTransformed = label.slice(0, -1).replace('Land area with ', '').toLowerCase();
							return labelTransformed[0].toUpperCase() + labelTransformed.substring(1);
						});

						if (this.selectedChart === '') {
							this.selectedChart = title;
						}

						this.chartData.push({
							label,
							title,
							labels: labels.join(','),
							values: values.join(','),
							mode: '%',
						});
					});
				};

				reader.onerror = error => {
					console.warn(error);
				};

				reader.readAsBinaryString(file);
			}
		}
	}

	public prepareDashboardData() {
		return {
				title: this.title,
				summary: this.summary,
				pages: this.pages,
				outputs: this.savedRun.outputs,
				runId: this.savedRun.runId
			};
	}

	public preview() {
		this.previewMode = true;
	}

	public closePreview() {
		this.previewMode = false;
	}

	public downloadAsPdf() {
		console.log('PDF');
	}

	public saveAsDraft() {
		const dashboardData = this.prepareDashboardData();
		DashboardService.createDashboard(dashboardData);
	}

	private mounted() {
		this.addPage();
		this.prepareMapData();
		this.prepareChartData();
		this.title = 'Dashboard_' + new Date().toISOString().slice(0, 10);
	}
}
</script>

<style lang="scss" scoped>
.dashboard-creator {
	padding: 10px 25px;

	&__mobile-error {
		width: calc(100% + 50px);
		color: $white;
		padding: 30px 30px 30px 80px;
		margin: -10px -25px;
		background: url('/svg/info.svg') 25px center no-repeat $green;
		background-size: 40px;
	}

	&__page-order {
		position: absolute;
		left: 50%;
		top: 50%;
		transform: translate(-50%, -50%);
		z-index: 2;
		background: $white;
		border: 1px solid #ccc;
		border-radius: 5px;
		box-shadow: 0 0 20px rgba(0,0,0,0.1);
		width: 680px;
		max-width: 90%;
		min-height: 250px;
		display: flex;
		flex-direction: column;

		.header {
			padding: 18px 25px;
			background: $blue;
			color: $white;
			font-family: 'NotesEsaReg';
			font-size: 20px;
			.close {
				position: absolute;
				right: 15px;
				top: 15px;
				font-size: 1.2em;
				color: $white;
			}
		}

		.content {
			flex-grow: 1;

			p {
				padding: 18px 25px;
				text-align: center;
				border-bottom: 1px solid #ddd;
			}

			.pages {
				padding: 18px 25px;
				display: flex;
   				gap: 10px;
				flex-wrap: wrap;

				.drop-slot {
					width: 8px;
					height: 70px;
					transition: 0.2s ease all;
					outline: 1px dashed transparent;
				}

				&.dragging {
					.drop-slot {
						outline: 1px dashed $blue;

						&.ready-to-drop {
							width: 50px;
						}
					}
				}
				.page {
					cursor: pointer;
					margin-bottom: 5px;

					&__icon {
						width: 50px;
						height: 50px;
						background: $grey-light;
						margin-bottom: 5px;

						svg {
							color: $blue;
							padding: 5px;
						}
					}

					&__name {
						font-size: 0.85em;
						text-align: center;
					}
				}
			}
		}

		.footer {
			padding: 18px 25px;
			display: flex;
			justify-content: center;
			border-top: 1px solid #ddd;
		}
	}

	&__name-input {
		font-weight: bold;
		border: none;
		width: 100%;
		display: block;
		padding: 0 0 5px;
		margin-bottom: 5px;
		border-bottom: 1px solid $grey;
		font-size: 1.2em;
	}

	&__description {
		border: none;
		padding: 3px 0;
		width: 100%;
		min-height: 5em;
	}

	&__based-on {
		color: $grey-dark;
		font-size: 0.85em;
		margin-bottom: 4px;
	}

	&__title {
		font-weight: bold;
		margin-bottom: 1px;
	}

	&__run-id {
		color: $grey-dark;

		> span {
			display: inline-block;
			vertical-align: middle;
			margin-right: 5px;
			margin-top: 2px;
		}
	}

	&__header,
	&__footer {
		border-bottom: 1px solid $grey;
		padding: 10px 0;
		margin: 10px 0;
		display: flex;
		justify-content: space-between;
	}

	&__footer {
		border-top: 1px solid $grey;
		border-bottom: none;
		justify-content: flex-end;
	}

	&__contents {
		display: flex;
		justify-content: space-between;
	}

	&__layout-wrapper {
		width: calc(100% - 100px);
		margin: 20px 0;
	}

	&__clear {
		position: absolute;
		right: 0px;
		top: 0px;
		padding: 5px;
		background: white;
		border: 2px dashed #0661a9;
		line-height: 6px;
		z-index: 2;
		border-right: 0;
		border-top: 0;
		font-size: 0.6em;
	}

	&__saved-run-status {
		display: inline-block;
		vertical-align: middle;

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

	&__header {
		display: block;

		.blue-btn-default {
			font-size: 1.2em;
			white-space: nowrap;
			margin-left: 20px;
		}

		.blue-btn-link {
			white-space: nowrap;
			margin-left: 20px;
			color: $blue;
			font-size: 1em;
		}

		&__top {
			display: flex;
			align-items: center;
			justify-content: space-between;
			padding-bottom: 10px;
			margin-bottom: 10px;

			> div {
				line-height: 1.2em;

				@media(max-width: $breakpoint-lg) {
					display: flex;
					flex-direction: column;

					button {
						text-align: left;
					}

					> * + * {
						margin-top: 10px;
					}
				}
			}
		}

		&__middle {
			display: flex;
			flex-direction: column;
		}

		&__bottom {
			display: flex;
			justify-content: flex-end;
		}
	}

	&__panel {
		width: 60px;
		background: $grey-lighter;
		border-radius: 4px;
		padding: 10px 0;
		margin: 20px 20px 20px 0;
		position: sticky;
		top: 20px;
		z-index: 1;
		height: 479px;
		max-height: 100%;

		&__label {
			text-transform: uppercase;
			font-size: 0.85em;
			margin: 0 10px 10px;
			text-align: center;
		}

		.cursor-pointer {
			cursor: pointer;
		}

		[disabled] {
			pointer-events: none;
			opacity: 0.5;
		}
	}

	&__tool {
		position: relative;
		margin: 5px 0;
		padding: 0 10px;

		&.page-order {
			svg {
				transform: scale(1.1);
    			margin-top: 7px;
			}
		}

		svg {
			width: 26px;
			height: 26px;
			color: $black;
		}

		&:hover {
			.group {
				background: $black;

				svg,
				g {
					color: $white;
				}
			}

			.sidebar {
				display: block;

				input {
					display: none;
				}
			}
		}

		.group {
			display: block;
			width: 40px;
			height: 40px;
			border-radius: 50%;
			background: $white;
			text-align: center;
			display: flex;
			align-items: center;
			justify-content: center;
			padding: 0;
		}

		.tool-label {
			font-size: 0.6em;
		    padding: 0;
			margin: -3px 0 10px;
			display: block;
			text-align: center;
			white-space: nowrap;
		}

		.sidebar {
			display: none;
			left: 50px;
			top: 50%;
			transform: translateY(-90px);
			position: absolute;
			padding: 50px 0;
			padding-left: 25px;
			z-index: 3;

			> div {
				background: $grey-lighter;
				padding: 10px 10px 10px 0px;
				border-radius: 4px;
				display: flex;
				position: relative;

				&:before {
					width: 0;
					height: 0;
					border-style: solid;
					border-width: 8px 8px 8px 0;
					border-color: transparent $grey-lighter transparent transparent;
					display: block;
					content: '';
					position: absolute;
					left: -8px;
					top: 50%;
					transform: translateY(-50%);
				}

				> div,
				> label {
					display: block;
					width: 40px;
					height: 40px;
					border-radius: 50%;
					background: $white;
					text-align: center;
					display: flex;
					align-items: center;
					justify-content: center;
					color: $black;
					margin-left: 10px;
					cursor: pointer;

					&.active {
						background: $black;

						svg {
							color: $white;
						}
					}
				}

				> select {
					margin-left: 10px;
				}
			}
		}
	}
	.layout {
		display: grid;
		grid-template-columns: repeat(2, 1fr);
		grid-column-gap: 10px;
		grid-row-gap: 10px;

		section {
			border: 2px dashed $blue;
			position: relative;
			min-width: 0;
			min-height: 0;

			.empty-section {
				background: url('/svg/dragAndDropPlaceholder.svg') center center no-repeat;
				background-size: 75px;
				width: 100%;
				min-height: 100%;
				position: relative;

				> span {
					color: $blue;
					font-size: 0.85em;
					position: absolute;
					display: block;
					top: 50%;
					transform: translateY(45px);
					text-align: center;
					width: 100%;
				}
			}
		}

		&-1 {
			.empty-section {height: 474px;}
			section:nth-child(1) { grid-area: 1 / 1 / 3 / 3; }
		}
		&-2 {
			.empty-section {height: 474px;}
			section:nth-child(1) { grid-area: 1 / 1 / 3 / 2; }
			section:nth-child(2) { grid-area: 1 / 2 / 3 / 3; }
			@media(max-width: $breakpoint-lg) {
				section:nth-child(1) { grid-area: 1 / 1 / 2 / 3; }
				section:nth-child(2) { grid-area: 2 / 1 / 3 / 3; }
			}
		}
		&-3 {
			.empty-section {height: 230px;}
			section:nth-child(1) { grid-area: 1 / 1 / 3 / 2; display: grid; align-items: center; }
			section:nth-child(2) { grid-area: 1 / 2 / 2 / 3; }
			section:nth-child(3) { grid-area: 2 / 2 / 3 / 3; }
			@media(max-width: $breakpoint-lg) {
				section:nth-child(1) { grid-area: 1 / 1 / 2 / 3; }
				section:nth-child(2) { grid-area: 2 / 1 / 3 / 3; }
				section:nth-child(3) { grid-area: 3 / 1 / 4 / 3; }
			}
		}
		&-4 {
			.empty-section {height: 230px;}
			section:nth-child(1) { grid-area: 1 / 1 / 2 / 2; }
			section:nth-child(2) { grid-area: 2 / 1 / 3 / 2; }
			section:nth-child(3) { grid-area: 1 / 2 / 3 / 3; display: grid; align-items: center; }
			@media(max-width: $breakpoint-lg) {
				section:nth-child(1) { grid-area: 1 / 1 / 2 / 3; }
				section:nth-child(2) { grid-area: 2 / 1 / 3 / 3; }
				section:nth-child(3) { grid-area: 3 / 1 / 4 / 3; }
			}
		}
		&-5 {
			.empty-section {height: 230px;}
			section:nth-child(1) { grid-area: 1 / 1 / 2 / 2; }
			section:nth-child(2) { grid-area: 1 / 2 / 2 / 3; }
			section:nth-child(3) { grid-area: 2 / 1 / 3 / 3; }
			@media(max-width: $breakpoint-lg) {
				section:nth-child(1) { grid-area: 1 / 1 / 2 / 3; }
				section:nth-child(2) { grid-area: 2 / 1 / 3 / 3; }
				section:nth-child(3) { grid-area: 3 / 1 / 4 / 3; }
			}
		}
		&-6 {
			.empty-section {height: 230px;}
			section:nth-child(1) { grid-area: 1 / 1 / 2 / 3; }
			section:nth-child(2) { grid-area: 2 / 1 / 3 / 2; }
			section:nth-child(3) { grid-area: 2 / 2 / 3 / 3; }
			@media(max-width: $breakpoint-lg) {
				section:nth-child(1) { grid-area: 1 / 1 / 2 / 3; }
				section:nth-child(2) { grid-area: 2 / 1 / 3 / 3; }
				section:nth-child(3) { grid-area: 3 / 1 / 4 / 3; }
			}
		}
	}
}
</style>
