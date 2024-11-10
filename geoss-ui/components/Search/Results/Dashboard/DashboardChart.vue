<template>
    <div class="dashboard-chart" ref="dashboardChartWrapper">
        <div class="dashboard-chart__config" v-if="mode === 'creator'">
            <div class="inputs_wrapper">
                <label>{{ $tc('popupContent.showAs') }}: </label>
                <select v-model="chartMode" @change="changeChartMode()">
                    <option value="values">
                        {{ $tc('popupContent.values') }}
                    </option>
                    <option value="%">
                        {{ $tc('popupContent.percentage') }}
                    </option>
                </select>
            </div>
            <div class="inputs_wrapper">
                <label class="labels_label"
                    >{{ $tc('popupContent.labels') }}:
                </label>
                <input
                    class="label_input"
                    v-for="(label, index) of labels"
                    :key="index"
                    v-model="labels[index]"
                />
            </div>
            <div class="inputs_wrapper">
                <label class="labels_label"
                    >{{ $tc('popupContent.datasetNames') }}:
                </label>
                <input
                    class="label_input"
                    v-for="(data, index) of inputData"
                    :key="index"
                    v-model="inputData[index].label"
                />
            </div>
            <div class="inputs_wrapper">
                <label class="labels_label"
                    >{{ $tc('popupContent.colors') }}:
                </label>
                <input
                    class="label_input"
                    v-for="(data, index) of inputData"
                    :key="index"
                    v-model="inputData[index].backgroundColor"
                />
            </div>
        </div>
        <canvas v-if="chartData" ref="dashboardChart"></canvas>
    </div>
</template>

<script lang="ts">
import { Prop, Component, Vue, Watch } from 'nuxt-property-decorator'
import { Chart, registerables } from 'chart.js'
Chart.register(...registerables)

interface ChartData {
    label: string
    title: string
    labels: string // comma separated values
    values: any[] // comma separated values
    mode: string // percentage or absolute values
}

@Component({})
export default class DashboardChartComponent extends Vue {
    @Prop({ default: null, type: Object }) public chartData!: ChartData
    @Prop({ default: '', type: String }) public mode!: string
    @Prop({ default: true, type: Boolean }) public layoutChange!: boolean

    public chartMode = 'values'
    public labels: any = []
    public inputData: any = []
    private chart: any = {}
    private chartOptions: any = {}
    private transformedData: any = []

    public changeChartMode() {
        this.$emit('change', this.chartMode)
        this.transformData()
        if (this.chartData.values.length === 1) {
            this.chart.config.data.datasets = [
                {
                    label: this.chartData.label + ' [' + this.chartMode + ']',
                    data: this.transformedData,
                    backgroundColor: [
                        '#0661a9',
                        '#0661a9',
                        '#0661a9',
                        '#0661a9'
                    ]
                }
            ]
        } else {
            this.chart.config.data.datasets = this.transformedData
        }

        this.chart.update()
    }

    private transformData() {
        if (this.chartData.values.length === 1) {
            const values: number[] = this.chartData.values[0]
                .split(',')
                .map((e: string) => Number(e))
            if (this.chartMode === 'values') {
                this.transformedData = values
            } else {
                const sum = values?.reduce((a, b) => a + b)
                this.transformedData = []
                for (const value of values) {
                    this.transformedData.push(
                        Math.round((value / sum) * 10000) / 100
                    )
                }
            }
        } else {
            const datasets = JSON.parse(JSON.stringify(this.chartData.values))

            if (this.chartMode === 'values') {
                this.transformedData = datasets
            } else {
                this.transformedData = []
                datasets.forEach((dataset: any) => {
                    const sum = dataset.data.reduce(
                        (a: number, b: number) => a + b
                    )
                    dataset.data = dataset.data.map((value: number) => {
                        return Math.round((value / sum) * 10000) / 100
                    })
                })
                this.transformedData = datasets
            }
        }
        this.inputData = this.transformedData
    }

    @Watch('labels')
    private onLabelsChange() {
        this.$emit('changeLabels', this.labels)
        this.chart.config.data.labels = this.labels
        this.chart.update()
    }

    @Watch('inputData', { deep: true })
    private onInputDataChange() {
        this.$emit('changeDatasets', this.inputData)
        this.chart.config.data.datasets = this.inputData
        this.chart.update()
    }

    @Watch('layoutChange')
    private async onLayoutChange() {
        await this.$nextTick()
        const aspectRatio =
            (this.$refs.dashboardChartWrapper as any).clientWidth < 600 ? 1 : 2
        this.chart.aspectRatio = aspectRatio
        this.chart.update('resize')
    }

    private async mounted() {
        if (!this.chartData) {
            return
        }
        await this.$nextTick()
        this.chartMode = this.chartData.mode || '%'
        this.transformData()
        this.labels = this.chartData.labels.split(',')
        const aspectRatio =
            (this.$refs.dashboardChartWrapper as any).clientWidth < 600 ? 1 : 2
        if (this.chartData.values.length === 1) {
            this.chartOptions = {
                type: 'bar',
                data: {
                    labels: this.chartData.labels.split(','),
                    datasets: [
                        {
                            label:
                                this.chartData.label +
                                ' [' +
                                this.chartMode +
                                ']',
                            data: this.transformedData,
                            backgroundColor: [
                                '#0661a9',
                                '#0661a9',
                                '#0661a9',
                                '#0661a9'
                            ]
                        }
                    ]
                },
                options: {
                    responsive: true,
                    aspectRatio,
                    title: {
                        display: true,
                        text: this.chartData.title
                    }
                }
            }
        } else {
            this.chartOptions = {
                type: 'bar',
                data: {
                    labels: this.chartData.labels.split(','),
                    datasets: this.transformedData
                },
                options: {
                    aspectRatio,
                    title: {
                        display: true,
                        text: this.chartData.title
                    },
                    plugins: {
                        legend: {
                            onClick: (e: any, legendItem: any, legend: any) => {
                                const index = legendItem.datasetIndex
                                const dataset =
                                    legend.chart.data.datasets[index]

                                // Count the currently visible datasets
                                const visibleDatasets =
                                    legend.chart.data.datasets.filter(
                                        (ds: any) => !ds.hidden
                                    ).length

                                // Check if we are trying to hide the last visible dataset
                                if (!dataset.hidden && visibleDatasets === 1) {
                                    // Do nothing, ensure at least one dataset remains visible
                                    return
                                }

                                // Toggle visibility if it will keep visible datasets <= 4
                                if (!dataset.hidden || visibleDatasets < 4) {
                                    dataset.hidden = !dataset.hidden
                                    legend.chart.update()
                                }
                                this.$emit(
                                    'changeDatasets',
                                    legend.chart.data.datasets
                                )
                            }
                        }
                    }
                }
            }
        }

        const ctx = this.$refs.dashboardChart as HTMLCanvasElement
        this.chart = new Chart(ctx, this.chartOptions)
    }
}
</script>

<style lang="scss">
.label_input {
    max-width: 70px;
    outline: none;
    border: none;
    border-bottom: 1px solid;
    &:focus {
        border: 1px solid;
        border-color: #0661a9;
    }
}

.inputs_wrapper {
    display: flex;
    width: 100%;
    gap: 10px;
    justify-items: flex-start;
    justify-content: flex-start;
    align-items: center;
}

.dashboard-chart {
    &__config {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-items: flex-start;
        justify-content: flex-start;
        margin: 5px;
        gap: 5px;

        label {
            font-size: 0.85em;
            margin-right: 5px;
        }
    }

    canvas {
        max-width: 100%;
    }
}
</style>
