<template>
    <div class="saved-runs">
        <div class="my-workspace-header">
            <h1>{{ $tc('savedRuns.savedRuns') }}</h1>
            <button class="openeo_button" @click="toggleOpenEoJobs">
                OpenEO Jobs
            </button>
        </div>
        <!-- <div
            v-if="workflow && workflowRunName"
            class="d-flex flex--justify-between flex--align-center service-workflow__add-run"
        >
            <input
                placeholder="Name"
                class="service-workflow__add-run-name"
                type="text"
                v-model="addRunName"
            />
            <input
                placeholder="Run ID"
                class="service-workflow__add-run-id"
                type="text"
                v-model="addRunId"
            />
            <button
                class="service-workflow__add-run-btn"
                :disabled="!addRunId || !addRunName"
                @click="addRun()"
            >
                {{ $tc('popupContent.add') }}
            </button>
        </div> -->
        <div
            v-show="loading"
            :class="{ 'separate-popup': !workflow && !workflowRunName }"
        >
            {{ $tc('popupContent.loadingData') }}...
        </div>
        <div
            v-show="!loading && (!displayRuns || !displayRuns.length)"
            :class="{ 'separate-popup': !workflow && !workflowRunName }"
        >
            {{ $tc('popupContent.noRunsAvailable') }}
        </div>
        <div class="service-workflow__saved-runs" v-if="displayRuns">
            <div :class="{ 'separate-popup': !workflow && !workflowRunName }">
                <div
                    class="service-workflow__saved-run"
                    v-for="displayRun of displayRuns"
                    :key="displayRun.id"
                >
                    <div class="d-flex">
                        <div class="service-workflow__saved-run-info">
                            <div class="service-workflow__saved-run-name">
                                {{ displayRun.name }}
                            </div>
                            <div class="service-workflow__saved-run-id">
                                {{ $tc('popupContent.id') }}:
                                {{ displayRun.id }}
                            </div>
                            <div
                                v-if="
                                    displayRun.outputs &&
                                    (displayRun.status === 'COMPLETED' ||
                                        displayRun.status === 'FINISHED') &&
                                    displayRun.result !== 'FAIL'
                                "
                                class="service-workflow__saved-outputs"
                            >
                                <div
                                    class="service-workflow__saved-outputs__label"
                                >
                                    {{ $tc('popupContent.outputs') }}:
                                </div>
                                <div
                                    v-for="output of displayRun.outputs"
                                    :key="output.id"
                                    class="service-workflow__saved-output"
                                >
                                    <div
                                        v-if="
                                            output.value &&
                                            output.valueSchema === 'url'
                                        "
                                        class="d-flex flex--align-center"
                                    >
                                        <button
                                            @click="
                                                downloadOutput(output.value)
                                            "
                                            :title="
                                                $tc('dabResult.downloadNow')
                                            "
                                        >
                                            <i class="icomoon-download"></i>
                                        </button>
                                        <span>{{ output.name }}</span>
                                        <div
                                            v-if="output.description"
                                            class="service-workflow__saved-output-link-description"
                                        >
                                            {{ output.description }}
                                        </div>
                                    </div>
                                    <div
                                        v-else-if="
                                            output.value && output.value.url
                                        "
                                        class="d-flex flex--align-center"
                                    >
                                        <button
                                            @click="
                                                showOnMap(
                                                    displayRun.runId,
                                                    output
                                                )
                                            "
                                            :title="$tc('dabResult.showOnMap')"
                                        >
                                            <i class="icomoon-show-on-map"></i>
                                        </button>
                                        <span>{{ output.name }}</span>
                                    </div>
                                    <div
                                        v-else
                                        class="d-flex flex--align-center"
                                    >
                                        <button
                                            disabled="true"
                                            :title="$tc('dabResult.showOnMap')"
                                        >
                                            <i class="icomoon-show-on-map"></i>
                                        </button>
                                        <span>{{ output.name }}</span>
                                    </div>
                                </div>
                            </div>
                            <div
                                class="service-workflow__saved-run-button-holder d-flex"
                            >
                                <button
                                    class="service-workflow__saved-run-btn"
                                    :class="{ active: displayRun.showLogs }"
                                    @click="toggleLogs(displayRun)"
                                    v-if="
                                        (displayRun.messageList &&
                                            displayRun.messageList.length) ||
                                        (showOpenEOJobs &&
                                            displayRun.status === 'ERROR')
                                    "
                                >
                                    {{ $tc('popupContent.messageLog') }}
                                </button>
                                <button
                                    class="service-workflow__saved-run-btn"
                                    :class="
                                        displayRun.result === 'FINISHED'
                                            ? 'solo_link'
                                            : 'link'
                                    "
                                    @click="createDashboard(displayRun)"
                                    v-if="
                                        displayRun.outputs &&
                                        displayRun.outputs.length &&
                                        displayRun.result !== 'FAIL'
                                    "
                                >
                                    {{ $tc('popupContent.createDashboard') }}
                                </button>
                            </div>
                        </div>
                        <div class="service-workflow__saved-run-status">
                            <span
                                class="service-workflow__saved-run-status__badge"
                                :class="{
                                    success:
                                        displayRun.status === 'COMPLETED' ||
                                        (displayRun.status === 'FINISHED' &&
                                            displayRun.result === 'SUCCESS') ||
                                        displayRun.result === 'FINISHED',
                                    error:
                                        displayRun.status === 'ERROR' &&
                                        displayRun.result === 'ERROR',
                                    running:
                                        displayRun.status === 'RUNNING' &&
                                        displayRun.result === 'RUNNING'
                                }"
                            >
                                {{ displayRun.status
                                }}<span v-if="displayRun.result && displayRun.result !== displayRun.status">
                                    - {{ displayRun.result }}</span
                                >
                            </span>
                        </div>
                    </div>
                    <div>
                        <CollapseTransition
                            v-if="
                                displayRun.messageList &&
                                displayRun.messageList.length
                            "
                        >
                            <div
                                v-show="displayRun.showLogs"
                                class="service-workflow__saved-logs"
                            >
                                <div
                                    v-for="log of displayRun.messageList"
                                    :key="log"
                                >
                                    {{ log }}
                                </div>
                            </div>
                        </CollapseTransition>
                    </div>
                </div>
            </div>
            <Pagination
                v-if="displayRuns && displayRuns.length && !showOpenEOJobs"
                :start-index="runsStartIndex"
                :results-per-page="runsResultsPerPage"
                :total="runsTotal"
                @on-start-index-change="onRunsStartIndexChange($event)"
            />
        </div>
    </div>
</template>

<script lang="ts">
// @ts-nocheck
import { runsTest } from '@/data/saved-runs-test'
import { Component, Vue, Prop, Watch } from 'nuxt-property-decorator'
import GeossSearchApiService from '@/services/geoss-search.api.service'
import to from '@/utils/to'
import NotificationService from '@/services/notification.service'
import PopupCloseService from '@/services/popup-close.service'
import LayersUtils from '@/services/map/layer-utils'
import { MapActions } from '@/store/map/map-actions'
import { LayerTypes } from '@/interfaces/LayerTypes'
import { Timers } from '@/data/timers'
import { UserGetters } from '@/store/user/user-getters'
import Pagination from '@/components/Pagination.vue'
import DashboardCreator from '@/components/Search/Results/Dashboard/DashboardCreator.vue'
import { PopupActions } from '@/store/popup/popup-actions'
import CollapseTransition from '@/plugins/CollapseTransition'
import OpenEOService from '@/services/openeo.service'
import SpinnerService from '@/services/spinner.service'

@Component({
    components: {
        Pagination,
        CollapseTransition
    }
})
export default class SavedRunsComponent extends Vue {
    @Prop({ required: false, type: String }) public workflowRunName!: string
    @Prop({ required: false, type: Object }) public workflow!: any
    @Prop({ required: false, type: Boolean, default: false })
    public isPopup!: any

    public displayRuns: any = null
    public savedRuns: any = null
    public openEOJobs: any = null
    public showOpenEOJobs: boolean = false
    public runsResultsPerPage = 10
    public runsStartIndex = 0
    public runsTotal = 0
    // public addRunId = ''
    // public addRunName = ''
    public loading = false

    get isSignedIn() {
        return this.$nuxt.$auth.loggedIn
    }

    // public async addRun() {
    //     const run: any = {}
    //     const [err, { status, messageList, result }] = await to(
    //         GeossSearchApiService.getRunStatus(this.addRunId)
    //     )
    //     if (!err) {
    //         GeossSearchApiService.addSavedRun(
    //             this.workflowRunName,
    //             this.workflow,
    //             this.addRunId
    //         ).then(() => {
    //             run.status = status
    //             run.messageList = messageList
    //             run.result = result
    //             run.showLogs = false
    //             if (status === 'COMPLETED' || status === 'SUCCESS') {
    //                 return GeossSearchApiService.getRunStatus(
    //                     this.addRunId
    //                 ).then(({ outputs }: any) => {
    //                     run.outputs = outputs
    //                     run.showOutputs = false
    //                 })
    //             }
    //         })
    //     } else {
    //         NotificationService.show(
    //             `${this.$tc('popupTitles.savedRuns')}`,
    //             `Analysis with run ID ${this.addRunId} does not exist.`,
    //             10000,
    //             undefined,
    //             9999,
    //             'error'
    //         )
    //     }
    // }

    public async showOnMap(runId: string, output: any) {
        const { url, name, protocol, legend } = output.value
        let coordinate
        if (
            output.aoi &&
            output.aoi.geometry &&
            output.aoi.geometry.bbox &&
            output.aoi.geometry.bbox.length
        ) {
            const bbox = output.aoi.geometry.bbox
            coordinate = {
                W: bbox[0] * 1,
                S: bbox[1] * 1,
                E: bbox[2] * 1,
                N: bbox[3] * 1
            }
        } else {
            ;[, coordinate] = await to(
                GeossSearchApiService.getRunCoordinates(runId)
            )
        }
        if (this.isPopup) {
            this.close()
        } else {
            this.$router.push('/')
        }

        let version = '1.1.1'
        if (protocol) {
            if (protocol.indexOf('WebMapService') > -1) {
                const match = /((\d)+\.(\d)+(\.(\d)+)?)/g.exec(protocol)
                if (match) {
                    version = match[0]
                }
            }
        }

        const layerUrl = `${url}VERSION=${version}&LAYERS=${name}&TILED=true&`
        const layer = LayersUtils.createWMS(name, layerUrl)

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
        })

        this.$store.dispatch(MapActions.changeLayerVisibility, {
            id: LayerTypes.BOUNDING,
            value: false
        })
        this.$store.dispatch(MapActions.setShowFull, true)

        setTimeout(
            () => {
                this.$store.dispatch(MapActions.zoomInLayer, name)
            },
            Timers.hideSearchContainer > Timers.closePopup
                ? Timers.hideSearchContainer
                : Timers.closePopup
        )
    }

    public async toggleLogs(run: any) {
        if (run.status === 'ERROR' && this.showOpenEOJobs) {
            if (run.messageList.length === 0) {
                const logs = await OpenEOService.getJobLogs(
                    run.id,
                    this.$store.getters[UserGetters.openEOToken]
                )
                run.messageList = logs.logs.map((log) => {
                    return (
                        `[${log.time.toString()}]` +
                        ' ' +
                        log.message.toString()
                    )
                })
            }
            run.showLogs = !run.showLogs
            run.showOutputs = false
        } else {
            run.showLogs = !run.showLogs
            run.showOutputs = false
        }
    }

    public toggleOutputs(run: any) {
        run.showOutputs = !run.showOutputs
        run.showLogs = false
    }

    public async onRunsStartIndexChange(startIndex: number) {
        this.runsStartIndex = startIndex
        this.getSavedRuns()
    }

    public async getSavedRuns() {
        const pageNum = this.runsStartIndex / this.runsResultsPerPage
        const { data, pageInfo } = await GeossSearchApiService.getSavedRuns(
            this.runsResultsPerPage,
            pageNum
        )
        if (data) {
            this.savedRuns = data
            this.displayRuns = data
        }
        return { data, pageInfo }
    }

    public async toggleOpenEoJobs() {
        if (this.openEOJobs === null) {
            const jobs = await this.getOpenEOJobs()
            if (jobs) {
                this.displayRuns = jobs
                this.openEOJobs = jobs
                this.showOpenEOJobs = true
            }
        } else {
            if (this.showOpenEOJobs) {
                this.displayRuns = this.savedRuns
            } else {
                const jobs = await this.getOpenEOJobs()
                this.openEOJobs = jobs
                this.displayRuns = jobs
            }
            this.showOpenEOJobs = !this.showOpenEOJobs
        }
    }

    public async getOpenEOJobs() {
        let longRequestInfo: string | number | NodeJS.Timeout | undefined
        SpinnerService.showSpinner(null, false)
        longRequestInfo = setTimeout(() => {
            SpinnerService.setLongRequestInfo(true)
        }, 10000)
        let token = this.$store.getters[UserGetters.openEOToken]
        const tokenExp = this.$store.getters[UserGetters.openEOTokenExpireDate]
        const currDateTime = Math.floor(Date.now() / 1000)
        if (!token || tokenExp <= currDateTime) {
            token = await OpenEOService.authenticateOpenEO()
        }
        const jobs = await OpenEOService.getOpenEOJobs(token)
        jobs.sort((a, b) => new Date(b.createdOn) - new Date(a.createdOn))
        //spinner
        SpinnerService.hideSpinner()
        clearTimeout(longRequestInfo)
        SpinnerService.setLongRequestInfo(false)
        return jobs
    }

    public close() {
        if (this.workflow) {
            PopupCloseService.closePopup('workflow')
        } else {
            PopupCloseService.closePopup('openEOWorkflow')
        }
    }

    public downloadOutput(url: string) {
        if (url && url !== '') {
            window.open(url, '_blank')
        }
    }

    public async createDashboard(savedRun: any) {
        // PopupCloseService.closePopup('saved-runs')
        const props = {
            savedRun: savedRun,
            protected: {
                message: this.$tc('popupContent.creatorCloseConfirmation')
            }
        }
        this.$store.dispatch(PopupActions.openPopup, {
            contentId: 'dashboard-creator',
            title: this.$tc('popupTitles.dashboards'),
            noCloseOutside: true,
            component: DashboardCreator,
            props
        })
    }

    private async mounted() {
        await this.$nextTick()

        if (!this.displayRuns) {
            this.loading = true
            const { data, pageInfo } = await this.getSavedRuns()
            if (data) {
                this.displayRuns = data
                this.runsTotal = pageInfo.totalElements
            }
            this.loading = false
        }
    }
}
</script>

<style lang="scss">
.saved-runs {
    .openeo_button {
        color: white;
        border: 1px solid;
        padding: 7px 20px 7px 20px;
    }
    .separate-popup {
        padding: 20px 30px;
    }

    .service-workflow {
        &__saved-run {
            border-bottom: 1px solid $grey-lighter;
            padding: 20px 0px;

            @media (max-width: $breakpoint-lg) {
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

                &.solo_link {
                    font-weight: bold;
                    color: $blue;
                    background: none;
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

                @media (max-width: $breakpoint-sm) {
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

            @media (max-width: $breakpoint-lg) {
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

                &.running {
                    background: lightskyblue;
                }

                &.error {
                    background: red;
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
