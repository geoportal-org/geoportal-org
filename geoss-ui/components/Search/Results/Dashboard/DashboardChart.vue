<template>
	<div class="dashboard-chart" ref="dashboardChartWrapper">
		<div class="dashboard-chart__config" v-if="mode === 'creator'">
			<label>{{ $tc('popupContent.showAs') }}: </label>
			<select v-model="chartMode" @change="changeChartMode()">
				<option value="values">{{ $tc('popupContent.values') }}</option>
				<option value="%">{{ $tc('popupContent.percentage') }}</option>
			</select>
		</div>
		<canvas v-if="chartData" ref="dashboardChart"></canvas>
	</div>
</template>

<script lang="ts">
import { Prop, Component, Vue, Watch } from 'nuxt-property-decorator';
import { Chart } from 'chart.js';

interface ChartData {
	label: string;
	title: string;
	labels: string; // comma separated values
	values: string; // comma separated values
	mode: string; // percentage or absolute values
}

@Component({})
export default class DashboardChartComponent extends Vue {
	@Prop({default: null, type: Object}) public chartData!: ChartData;
	@Prop({default: '', type: String}) public mode!: string;
	@Prop({default: true, type: Boolean}) public layoutChange!: boolean;

	public chartMode = '%';
	private chart: any = {};
	private chartOptions: any = {};
	private transformedData: any = [];

	public changeChartMode() {
		this.$emit('change', this.chartMode);
		this.transformData();
		this.chart.config.data.datasets = [{
			label: this.chartData.label + ' [' + this.chartMode + ']',
			data: this.transformedData,
			backgroundColor: ['#0661a9', '#0661a9', '#0661a9', '#0661a9']
		}];
		this.chart.update();
	}

	private transformData() {
		const values: number[] = this.chartData.values.split(',').map((e: string) => Number(e));
		if (this.chartMode === 'values') {
			this.transformedData = values;
		} else {
			const sum = values.reduce((a, b) => a + b);
			this.transformedData = [];
			for (const value of values) {
				this.transformedData.push(Math.round((value / sum) * 10000) / 100);
			}
		}
	}

	@Watch('layoutChange')
	private async onLayoutChange() {
		await this.$nextTick();
		const aspectRatio = (this.$refs.dashboardChartWrapper as any).clientWidth < 600 ? 1 : 2;
		this.chart.aspectRatio = aspectRatio;
		this.chart.update('resize');
	}

	private async mounted() {
		if (!this.chartData) {
			return;
		}
		await this.$nextTick();
		this.chartMode = this.chartData.mode || '%';
		this.transformData();
		const aspectRatio = (this.$refs.dashboardChartWrapper as any).clientWidth < 600 ? 1 : 2;
		this.chartOptions = {
			type: 'bar',
			data: {
				labels: this.chartData.labels.split(','),
				datasets: [{
					label: this.chartData.label + ' [' + this.chartMode + ']',
					data: this.transformedData,
					backgroundColor: ['#0661a9', '#0661a9', '#0661a9', '#0661a9']
				}]
			},
			options: {
				aspectRatio,
				title: {
					display: true,
					text: this.chartData.title
				},
			}
		};
		const ctx = this.$refs.dashboardChart as HTMLCanvasElement;
		this.chart = new Chart(ctx, this.chartOptions);
	}
}
</script>

<style lang="scss">
.dashboard-chart {
	&__config {
		margin: 5px;

		label {
			font-size: 0.85em;
		}
	}

	canvas {
		max-width: 100%;
	}
}
</style>
