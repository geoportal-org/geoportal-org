<template>
    <div class="statistics_main">
        <div class="my-workspace-header">
            <h1>{{ $tc('statistics.statistics') }}</h1>
            <NuxtLink to="/" class="close-window">
                <div class="line-1"></div>
                <div class="line-2"></div>
            </NuxtLink>
        </div>
        <form @submit.prevent="onSubmit">
            <div class="statistics_control" :class="{ closedStyle: !isOpen }">
                <div v-if="isOpen">
                    <h1>{{ $tc('statistics.chartOptions') }}</h1>
                    <div class="statistics_element statistics_element-start">
                        <p>{{ $tc('statistics.source') }}</p>

                        <input
                            v-model="form.source"
                            type="radio"
                            checked="true"
                            id="dataUsage"
                            :value="$tc('statistics.dataUsage')"
                            @change="
                                onSourceRadioChange($tc('statistics.dataUsage'))
                            "
                        />
                        <label for="dataUsage">
                            {{ $tc('statistics.dataUsage') }}
                        </label>
                        <input
                            v-model="form.source"
                            type="radio"
                            id="default"
                            :value="$tc('statistics.siteUsage')"
                            @change="
                                onSourceRadioChange($tc('statistics.siteUsage'))
                            "
                        />
                        <label for="default">
                            {{ $tc('statistics.siteUsage') }}
                        </label>
                    </div>
                    <div class="statistics_element">
                        <p>{{ $tc('statistics.dataset') }}</p>
                        <select
                            v-model="form.dataset"
                            class="statistics_select"
                            :disabled="isSiteUsage"
                        >
                            <option
                                v-for="(option, index) of datasetOptions"
                                :key="index"
                            >
                                {{ option }}
                            </option>
                        </select>
                    </div>
                    <div class="statistics_element">
                        <p>{{ $tc('statistics.type') }}</p>
                        <select
                            v-if="isSiteUsage && isAllCatalogsSelected"
                            v-model="form.type"
                            class="statistics_select"
                        >
                            <option
                                v-for="(option, index) of siteTypeOptions"
                                :key="index"
                            >
                                {{ option }}
                            </option>
                        </select>
                        <select
                            v-else-if="!isSiteUsage && isAllCatalogsSelected"
                            v-model="form.type"
                            class="statistics_select"
                        >
                            <option
                                v-for="(option, index) of dataTypeOptions"
                                :key="index"
                            >
                                {{ option }}
                            </option>
                        </select>
                        <select
                            v-else
                            v-model="form.type"
                            class="statistics_select"
                        >
                            <option
                                v-for="(option, index) of shortTypeOptions"
                                :key="index"
                            >
                                {{ option }}
                            </option>
                        </select>
                    </div>
                    <h1>{{ $tc('statistics.moreOptions') }}</h1>
                    <div v-if="isTypeNumeric" class="statistics_element">
                        <p>{{ $tc('statistics.interval') }}</p>
                        <input
                            v-if="form.source === $tc('statistics.dataUsage')"
                            type="number"
                            min="1"
                            v-model="form.interval"
                        />
                        <select v-model="form.unit" class="statistics_select">
                            <option
                                v-for="(option, index) of timeUnits"
                                :key="index"
                            >
                                {{ option }}
                            </option>
                        </select>
                    </div>
                    <div v-else class="statistics_element">
                        <p>{{ $tc('statistics.resultsPresented') }}</p>
                        <input
                            type="number"
                            min="1"
                            v-model="form.resultsNumber"
                            :disabled="form.type === 'Returning users'"
                        />
                    </div>
                </div>
                <div v-if="isOpen">
                    <h1>{{ $tc('statistics.analyzedPeriod') }}</h1>
                    <div class="statistics_datepicker_wrapper">
                        <DatePicker
                            :placeholder="$tc('placeholders.from')"
                            value="dateFrom"
                            v-model="form.dateFrom"
                            :clearable="false"
                        ></DatePicker>
                        <DatePicker
                            :placeholder="$tc('placeholders.to')"
                            value="dateTo"
                            v-model="form.dateTo"
                        ></DatePicker>
                    </div>

                    <div class="statistics_element statistics_element-start">
                        <input
                            v-model="form.period"
                            type="radio"
                            id="lastWeek"
                            :value="$tc('statistics.lastWeek')"
                            @change="onPeriodRadioChange('w')"
                            checked="true"
                        />
                        <label for="lastWeek">
                            {{ $tc('statistics.lastWeek') }}
                        </label>
                        <input
                            v-model="form.period"
                            type="radio"
                            id="lastMonth"
                            :value="$tc('statistics.lastMonth')"
                            @change="onPeriodRadioChange('m')"
                        />
                        <label for="lastMonth">
                            {{ $tc('statistics.lastMonth') }}
                        </label>
                        <input
                            v-model="form.period"
                            type="radio"
                            id="lastYear"
                            :value="$tc('statistics.lastYear')"
                            @change="onPeriodRadioChange('y')"
                        />
                        <label for="lastYear">
                            {{ $tc('statistics.lastYear') }}
                        </label>
                    </div>
                </div>
                <button
                    type="button"
                    v-if="isOpen"
                    class="statistics_menu_button"
                    @click="toggleIsOpen()"
                >
                    {{ $tc('statistics.hideOptions') }}
                </button>
                <button
                    type="button"
                    v-else
                    class="statistics_menu_button"
                    @click="toggleIsOpen()"
                >
                    {{ $tc('statistics.showOptions') }}
                </button>
                <div class="statistics_element statistics_element-end">
                    <div v-if="chartInstance" class="dropdown">
                        <button type="button" @click="toggleDropdown()" class="dropbtn">
                            {{ $tc('statistics.exportButton') }}
                        </button>
                        <div v-if="dropDownOpen" id="myDropdown" class="dropdown-content">
                            <a @click="downloadChartJsPDF()">PDF</a>
                            <a @click="downloadChartJsCSV()">CSV</a>
                        </div>
                    </div>
                    <!-- 
                    <div v-if="chartInstance" class="dropdown">
                        <button type="button" class="dropbtn">
                            {{ $tc('statistics.exportButton') }}
                            <i class="fa fa-caret-down"></i>
                        </button>
                        <div class="dropdown-content">
                            <a @click="downloadChartJsPDF()">PDF</a>
                            <a @click="downloadChartJsCSV()">CSV</a>
                        </div>
                    </div> -->
                    <button class="statistics_submit_button">
                        {{ $tc('statistics.showChart') }}
                    </button>
                </div>
            </div>
        </form>

        <div id="chart-container" class="chart_container">
            <canvas class="chart" id="chart"></canvas>
        </div>
        <div v-if="isCountires" class="map_container">
            <GChart
                class="gchart"
                type="GeoChart"
                :data="mapData"
                :options="mapOptions"
                :settings="mapSettings"
            />
        </div>
    </div>
</template>

<script type="module">
import MatomoDataService from '@/services/matomo-statistics.service.ts'
import Chart from 'chart.js/auto'
import { GChart } from 'vue-google-charts/legacy'
import { jsPDF } from 'jspdf'

export default {
    components: {
        GChart,
        Chart,
    },
    layout() {
        return 'static'
    },
    data() {
        let datasetOptions = [
            'All catalogs',
            'CORINE Land Cover',
            'European NUTS 2',
            'French Virtual Hub',
            'German Virtual Hub',
            'Italian Virtual Hub',
            'Noise Capture Data',
            'Polish Virtual Hub',
            'SOS SensorODP Alkante',
            'Sentinel',
            'Spanish Virtual Hub',
            'USGS Landsat 8',
        ]
        let dataTypeOptions = [
            this.$tc('statistics.dataTypeOptions.numberOfSearches'),
            this.$tc('statistics.dataTypeOptions.popularResources'),
            this.$tc('statistics.dataTypeOptions.popularKeywords'),
            this.$tc('statistics.dataTypeOptions.popularCatalogs'),
            this.$tc('statistics.dataTypeOptions.popularOrganizations'),
            this.$tc('statistics.dataTypeOptions.popularAreas'),
        ]
        let shortTypeOptions = [
            this.$tc('statistics.shortTypeOptions.numberOfSearches'),
            this.$tc('statistics.shortTypeOptions.popularResources'),
            this.$tc('statistics.shortTypeOptions.popularAreas'),
        ]
        let siteTypeOptions = [
            this.$tc('statistics.siteTypeOptions.numberOfUsers'),
            this.$tc('statistics.siteTypeOptions.numberOfSessions'),
            this.$tc('statistics.siteTypeOptions.bounceRate'),
            this.$tc('statistics.siteTypeOptions.returningUsers'),
            this.$tc('statistics.siteTypeOptions.usersCountries'),
            this.$tc('statistics.siteTypeOptions.popularBrowsers'),
        ]
        let timeUnits = [
            this.$tc('statistics.timeUnits.day'),
            this.$tc('statistics.timeUnits.week'),
            this.$tc('statistics.timeUnits.month'),
            this.$tc('statistics.timeUnits.year'),
        ]
        return {
            datasetOptions: datasetOptions,
            timeUnits: timeUnits,
            dataTypeOptions: dataTypeOptions,
            shortTypeOptions: shortTypeOptions,
            siteTypeOptions: siteTypeOptions,
            form: {
                source: this.$tc('statistics.dataUsage'),
                interval: 1,
                unit: timeUnits[0],
                resultsNumber: 10,
                dataset: datasetOptions[0],
                type: dataTypeOptions[0],
                dateFrom: null,
                dateTo: null,
                period: this.$tc('statistics.lastWeek'),
            },
            isOpen: true,
            chartInstance: null,
            chartData: null,
            isCountires: false,
            dropDownOpen: false,
            mapData: [],
            mapOptions: {
                responsive: true,
                defaultColor: '#f5f5f5',
                colorAxis: { colors: ['#a4a6de', '#0e1196'] },
                keepAspectRatio: true,
            },
            mapSettings: {
                packages: ['geochart'],
            },
        }
    },

    computed: {
        isSiteUsage() {
            return this.form.source === this.$tc('statistics.siteUsage')
                ? true
                : false
        },
        isTypeNumeric() {
            return (
                this.form.type ===
                    this.$tc('statistics.siteTypeOptions.numberOfUsers') ||
                this.form.type ===
                    this.$tc('statistics.siteTypeOptions.numberOfSessions') ||
                this.form.type ===
                    this.$tc('statistics.siteTypeOptions.bounceRate')
            )
        },
        isAllCatalogsSelected() {
            return this.form.dataset.includes('All catalogs')
        },
    },
    methods: {
        async onSubmit() {
            let result = await MatomoDataService.prepareChartData(
                this.form.type,
                this.isTypeNumeric ? this.form.unit : 'range',
                this.form.dateFrom,
                this.form.dateTo,
                this.form.resultsNumber.toString(),
                this.$config.matomoToken
            )
            if (this.chartInstance) {
                this.chartInstance.destroy()
            }

            //redraw chart
            const chrt = document.getElementById('chart')
            const chart = new Chart(chrt, {
                type: result.type,
                data: result.data,
                options: result.options,
            })
            this.chartInstance = chart
            this.chartData = result

            //draw map if countires
            if (result.isCountries) {
                this.isCountires = true
                this.mapData = result.mapData
            } else {
                this.isCountires = false
            }
        },
        toggleIsOpen() {
            this.isOpen = !this.isOpen
        },
        toggleDropdown() {
            this.dropDownOpen = !this.dropDownOpen
        },
        onSourceRadioChange(value) {
            this.form.dataset = this.datasetOptions[0]
            if (value === this.$tc('statistics.dataUsage')) {
                this.form.type = this.dataTypeOptions[0]
            } else {
                this.form.type = this.siteTypeOptions[0]
            }
        },
        onPeriodRadioChange(value) {
            let currentDate = new Date()
            let year = currentDate.getFullYear()
            let month = currentDate.getMonth()
            let day = currentDate.getDate()
            if (value === 'w') {
                this.form.dateFrom = new Date(year, month, day - 7)
            } else if (value === 'm') {
                this.form.dateFrom = new Date(year, month - 1, day)
            } else {
                this.form.dateFrom = new Date(year - 1, month, day)
            }
            this.form.dateTo = null
        },
        getCurrentDateAndTime() {
            const d = new Date()
            const date = d.toLocaleDateString('en-GB')
            const formattedDate = date.replaceAll('/', '-')
            const time = d.toLocaleTimeString('en-GB', {
                hour12: false,
            })
            return [formattedDate, time]
        },
        downloadChartJsPDF() {
            const [currentDate, currentTime] = this.getCurrentDateAndTime()
            const start = this.form.dateFrom
            const end = this.form.dateTo || currentDate
            const dateRange = start + ' — ' + end

            const canvas = document.getElementById('chart')
            const img = canvas.toDataURL()
            const doc = new jsPDF()
            const width = doc.internal.pageSize.getWidth()
            const height = doc.internal.pageSize.getHeight()

            doc.text('GEOSS Statistics', width / 2, 10, { align: 'center' })
            doc.addImage(img, 0, 15)
            doc.setFontSize(12)
            doc.text(
                this.$tc('statistics.period') + dateRange,
                width / 2,
                height / 2,
                {
                    align: 'center',
                }
            )
            doc.setFontSize(10)
            doc.text(
                this.$tc('statistics.generated') +
                    currentDate +
                    ' ' +
                    currentTime,
                width - 2,
                height / 2 + 5,
                { align: 'right' }
            )
            doc.save('GEOSS Statistics')
        },
        createCSV(array) {
            const keys = ['label', 'value']
            var result = ''
            array.forEach((item) => {
                keys.forEach((key, index) => {
                    result +=
                        index === 1
                            ? '"' + item[key] + '"'
                            : '"' + item[key] + '"' + ','
                })
                result += '\n'
            })

            return result
        },
        downloadChartJsCSV() {
            const [currentDate, currentTime] = this.getCurrentDateAndTime()

            const start = this.form.dateFrom
            const end = this.form.dateTo || currentDate
            const dateRange = start + ' — ' + end

            let data = []
            this.chartData.data.datasets[0].data.forEach((value, index) => {
                data.push({
                    label: this.chartData.data.labels[index],
                    value: value,
                })
            })

            const title =
                this.chartData?.options?.plugins?.title?.text || this.form.type
            const csv =
                'data:text/csv;charset=utf-8,' +
                '"' +
                title +
                '"' +
                '\n' +
                this.createCSV(data) +
                '\n' +
                this.$tc('statistics.period') +
                dateRange +
                '\n' +
                this.$tc('statistics.generated') +
                currentDate +
                ' ' +
                currentTime
            let excel = encodeURI(csv)

            let link = document.createElement('a')
            link.setAttribute('href', excel)
            link.setAttribute('download', 'GEOSS_Statistics.csv')
            link.click()
        },
    },
    mounted() {
        const now = new Date()
        this.form.dateFrom = new Date(
            now.getFullYear(),
            now.getMonth(),
            now.getDate() - 7
        )
    },
}
</script>

<style lang="scss" scoped>
.gchart {
    width: 90%;
}

.sub-page__content {
    height: 75vh;
    margin: 15vh auto 0;
    width: 50%;
    min-width: 400px;
}

.dropbtn {
    background-color: transparent;
    color: white;
    padding: 16px;
    font-size: 16px;
    border: none;
    cursor: pointer;
}

.dropdown {
    position: relative;
    display: inline-block;
}

.dropdown-content {
    display: block;
    position: absolute;
    background-color: #f1f1f1;
    min-width: 80px;
    box-shadow: 0px 8px 16px 0px rgba(0, 0, 0, 0.2);
    z-index: 1;
    text-align: center;
}

/* Links inside the dropdown */
.dropdown-content a {
    color: black;
    padding: 12px 16px;
    text-decoration: none;
    display: block;
}

.dropdown-content a:hover {
    background-color: #ddd;
}

.chart_container {
    width: 100%;
    padding: 1rem;
}

.map_container {
    width: 100%;
    padding: 1rem;
    display: flex;
    align-items: center;
    justify-content: center;
}

.chart {
    max-height: 30rem;
}

.cross {
    color: black;
}

.statistics_main {
    height: 100%;
    min-height: 500px;
    width: 100%;
}

.statistics_select {
    border-radius: 4px;
    min-width: 2rem;
    width: 80%;
    height: 2rem;
    min-height: 30px;
    border: none;
}

.statistics_control {
    // background-color: rgba(66,161,149,0.94);
    display: grid;
    grid-template-columns: 1fr 1fr;
    column-gap: 10%;
    border-top: 3px solid;
    border-color: rgba(66, 161, 149, 0.94);
    background-color: rgba(6, 97, 169, 0.75);
    height: 30vh;
    min-height: 30rem;
    padding: 0rem 2rem 1rem;
    color: white;

    h1 {
        font-size: 20px;
        margin: 2rem 0rem 2rem;
    }
}

.closedStyle {
    min-height: 6rem;
    height: 0px;
}

.statistics_element {
    display: flex;
    gap: 1rem;
    margin-top: 1rem;
    font-size: 14px;
    align-items: center;
    justify-content: space-between;
    width: 100%;

    label {
        font-style: italic;
    }

    input[type='number'] {
        border-radius: 4px;
        outline: none;
        padding: none;
        height: 2rem;
        min-height: 30px;
        width: 20%;
    }

    input[type='radio'] {
        accent-color: rgba(66, 161, 149);
    }

    label {
        font-size: 18px;
    }
}

.statistics_element-start {
    justify-content: flex-start;
}

.statistics_element-end {
    justify-content: flex-end;
}

.statistics_datepicker_wrapper {
    display: flex;
    flex-direction: row;
    gap: 1rem;
}

.statistics_submit_button {
    position: relative;
    background-color: rgba(66, 161, 149, 0.94);
    color: white;
    grid-column: 2;
    height: 2rem;
    width: 50%;
    align-self: center;
    justify-self: flex-end;
    font-size: 20px;
    margin: 1rem 0rem 1rem;
}

.statistics_menu_button {
    position: relative;
    background-color: transparent;
    color: white;
    grid-column: 1;
    height: 2rem;
    width: 50%;
    align-self: center;
    justify-self: flex-start;
    font-size: 16px;
    margin: 1rem 0rem 1rem;
}

.statistics_submit_button:hover {
    filter: brightness(90%);
}

.statistics_menu_button:hover {
    border: 1px solid;
    border-radius: 5px;
    border-color: rgba(66, 161, 149, 0.94);
}

@media (max-width: $breakpoint-md) {
    .statistics_control {
        grid-template-columns: 1fr;
        height: 100%;
    }

    .statistics_submit_button {
        grid-column: 1;
        width: 100%;
        align-self: center;
        justify-self: center;
    }
    .statistics_menu_button {
        grid-column: 1;
        width: 100%;
        align-self: center;
        justify-self: center;
    }
}
</style>
