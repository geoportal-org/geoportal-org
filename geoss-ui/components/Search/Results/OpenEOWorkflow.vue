<template>
    <div class="service-workflow">
        <div class="service-workflow__title">
            <span>{{ title }}</span>
        </div>
        <div class="service-workflow__switcher">
            <button
                :class="{ active: showDetails }"
                @click="toggleShowDetails(true)"
                :title="$tc('popupContent.seeThisWorkflow')"
            >
                <img
                    :src="`/svg/see-doc.svg`"
                    :alt="$tc('popupContent.seeThisWorkflow')"
                />
                <span>{{ $tc('popupContent.seeThisWorkflow') }}</span>
            </button>
            <button
                :class="{ active: !showDetails, disabled: !isSignedIn }"
                @click="toggleShowDetails(false)"
                :title="$tc('popupContent.runs')"
                :disabled="!isSignedIn"
            >
                <img
                    :src="`/svg/run-doc.svg`"
                    :alt="$tc('popupContent.runs')"
                />
                <span>{{ $tc('popupContent.runs') }}</span>
            </button>
        </div>
        <div class="service-workflow__details" v-show="showDetails">
            <div class="service-workflow__run-title">
                <div class="service-workflow__title">
                    <span>{{ $tc('popupContent.runName') }}</span>
                </div>
                <div class="service-workflow__run-title-input">
                    <input
                        type="text"
                        id="run-title"
                        :placeholder="$tc('popupContent.runName')"
                        v-model="workflowRunName"
                    />
                </div>
                <div class="service-workflow__title">
                    <span>{{ $tc('popupContent.dateRange') }}</span>
                </div>
                <div class="wrapper">
                    <DatePicker
                        :config="datePickerConfig"
                        :placeholder="$tc('placeholders.from')"
                        value="dateFrom"
                        v-model="dateFrom"
                        :clearable="true"
                    ></DatePicker>
                    <DatePicker
                        :disabled="true"
                        :placeholder="$tc('placeholders.to')"
                        value="dateTo"
                        v-model="dateTo"
                        :clearable="false"
                    ></DatePicker>
                </div>
                <div class="service-workflow__title">
                    <span>{{ $tc('popupContent.bbox') }}</span>
                </div>
                <div v-if="isAreaTooBig" class="area_error">
                    {{ $tc('popupContent.areaTooBig') }}
                </div>
                <div v-if="areCoordsValid" class="wrapper">
                    <div class="coord_wrapper">
                        E:
                        <p class="coord">{{ coords.E }}</p>
                    </div>
                    <div class="coord_wrapper">
                        W:
                        <p class="coord">{{ coords.W }}</p>
                    </div>
                    <div class="coord_wrapper">
                        N:
                        <p class="coord">{{ coords.N }}</p>
                    </div>
                    <div class="coord_wrapper">
                        S:
                        <p class="coord">{{ coords.S }}</p>
                    </div>
                </div>
                <div class="buttons_wrapper">
                    <button class="draw_button" @click="setBoundingBox">
                        {{ $tc('popupContent.draw') }}
                    </button>
                    <button
                        v-if="areCoordsValid"
                        class="clear_button"
                        @click="clearCoords"
                    >
                        {{ $tc('popupContent.clear') }}
                    </button>
                </div>
            </div>
            <div class="d-flex flex--justify-end flex--wrap">
                <button
                    class="service-workflow__run"
                    :class="{ disabled: !allRequiredFilled }"
                    :disabled="!allRequiredFilled"
                    @click="run()"
                >
                    {{ $tc('popupContent.run') }}
                </button>
            </div>
        </div>

        <div v-if="!showDetails">
            <SavedRuns :isPopup="true" />
        </div>
    </div>
</template>

<script lang="ts">
// @ts-nocheck
import { Component, Vue, Prop, Watch } from 'nuxt-property-decorator'
import SavedRuns from '@/components/SavedRuns.vue'
import { MapActions } from '@/store/map/map-actions'
import { GeneralFiltersActions } from '@/store/generalFilters/general-filters-actions'
import { GeneralFiltersGetters } from '~/store/generalFilters/general-filters-getters'
import { UserGetters } from '~/store/user/user-getters'
import OpenEOService from '@/services/openeo.service'
import PopupCloseService from '@/services/popup-close.service'
import NotificationService from '@/services/notification.service'

@Component({
    components: {
        SavedRuns
    }
})
export default class OpenEOWorkflowComponent extends Vue {
    @Prop({ required: true, type: String }) public title!: any
    @Prop({ required: true, type: String }) public workflowUrl!: string
    @Prop({ required: false, type: String }) public urlToResource!: string

    public showDetails = true
    public workflowRunNameValue = ''
    public dateFromValue = ''
    public dateToValue = ''
    public isAreaTooBig = false
    public coords = {
        W: '',
        S: '',
        E: '',
        N: ''
    }
    public allRequiredFilled = false

    public datePickerConfig = {
        wrap: false,
        defaultDate: null,
        enable: [
            function (date) {
                return date.getDate() === 1
            }
        ],
        minDate: '2018-01-01', // Set the minimum date to January 1, 2018
        maxDate: '2023-12-31' // Set the maximum date to December 31, 2023
    }

    get isSignedIn() {
        return this.$nuxt.$auth.$state.loggedIn
    }

    get workflowRunName() {
        return this.workflowRunNameValue
    }
    get dateFrom() {
        return this.dateFromValue
    }
    get dateTo() {
        return this.dateToValue
    }

    get coordinates() {
        return this.$store.getters[GeneralFiltersGetters.openEoCoordinates]
    }

    get areCoordsValid() {
        if (
            this.coords.E !== '' &&
            this.coords.W !== '' &&
            this.coords.N !== '' &&
            this.coords.S !== ''
        )
            return true
        return false
    }

    set workflowRunName(value: string) {
        this.workflowRunNameValue = value
        this.checkRequiredFilled()
    }

    set dateFrom(value: any) {
        this.dateFromValue = value
    }

    set dateTo(value: any) {
        this.dateToValue = value
    }

    public checkRequiredFilled() {
        if (
            this.workflowRunName !== '' &&
            this.dateFromValue !== '' &&
            this.dateToValue !== '' &&
            this.areCoordsValid
        ) {
            this.allRequiredFilled = true
        } else {
            this.allRequiredFilled = false
        }
    }

    public setBoundingBox() {
        this.clearCoords()
        this.$store.dispatch(GeneralFiltersActions.setOpenEoPopupVisible, false)
        this.$store.dispatch(GeneralFiltersActions.setOpenEoMapDraw, true)
        this.$store.dispatch(MapActions.setHideSearching, true)
    }

    public getFutureDate(inputDate) {
        // Parse the input date
        let date = new Date(inputDate)

        // Add one year
        date.setFullYear(date.getFullYear() + 1)

        // Subtract one day (in milliseconds)
        date.setDate(date.getDate() - 1)

        // Format back to 'YYYY-MM-DD'
        let year = date.getFullYear()
        let month = ('0' + (date.getMonth() + 1)).slice(-2) // Months are zero-indexed
        let day = ('0' + date.getDate()).slice(-2)

        return `${year}-${month}-${day}`
    }

    public clearCoords() {
        this.coords = {
            W: '',
            S: '',
            E: '',
            N: ''
        }
    }

    public calculateBoundingBoxArea(bbox) {
        const R = 6371 // Earth's radius in kilometers

        // Convert degrees to radians
        const lat1 = (bbox.N * Math.PI) / 180
        const lat2 = (bbox.S * Math.PI) / 180
        const lonDiff = ((bbox.E - bbox.W) * Math.PI) / 180

        // Calculate area based on spherical approximation
        const area = Math.abs(
            R * R * lonDiff * (Math.sin(lat1) - Math.sin(lat2))
        )

        return area
    }

    public async run() {
        if (!this.isSignedIn) {
            const fullLoginUrl = `/c/portal/login?redirect=${this.urlToResource}`
            const message = `${this.$tc(
                'popupContent.mustBeLoggedIn1'
            )}<a href="${fullLoginUrl}">${this.$tc(
                'popupContent.mustBeLoggedIn2'
            )}</a> ${this.$tc('popupContent.mustBeLoggedIn3')}`

            return NotificationService.show(
                `${this.$tc('general.info')}`,
                message,
                100000,
                'must-be-logged-in',
                9999,
                'info'
            )
        }
        const token = this.$store.getters[UserGetters.openEOToken]
        const tokenExp = this.$store.getters[UserGetters.openEOTokenExpireDate]
        const currDateTime = Math.floor(Date.now() / 1000)
        if (tokenExp <= currDateTime) {
            await OpenEOService.authenticateOpenEO()
        }
        const body = {
            'title': this.workflowRunName,
            'process': {
                id: 'cropmap',
                process_graph: {
                    biopar1: {
                        process_id: 'worldcereal_inference',
                        namespace:
                            'https://github.com/ESA-APEx/apex_algorithms/raw/main/openeo_udp/worldcereal_inference.json',
                        arguments: {
                            spatial_extent: {
                                west: this.coords.W,
                                east: this.coords.E,
                                south: this.coords.S,
                                north: this.coords.N,
                                crs: 'EPSG:4326'
                            },
                            temporal_extent: [this.dateFrom, this.dateTo]
                        },
                        result: true
                    }
                }
            },
            'driver-memory': '4g',
            'executor-memory': '1g',
            'executor-memoryOverhead': '1g',
            'python-memory': '2g',
            'udf-dependency-archives': [
                'https://artifactory.vgt.vito.be/artifactory/auxdata-public/openeo/onnx_dependencies_1.16.3.zip#onnx_deps'
            ]
        }

        const response = await OpenEOService.createOpenEOJob(this.workflowUrl, token, body)
        await OpenEOService.runOpenEOJob(response.job_id, token)
        this.close()
    }

    public async toggleShowDetails(value: boolean) {
        if (this.showDetails !== value) {
            this.showDetails = value
        }
    }

    public close() {
        PopupCloseService.closePopup('openEOWorkflow')
    }

    @Watch('coordinates')
    private onCoordinatesChange(val) {
        if (val) {
            const area = this.calculateBoundingBoxArea(val)
            if (area > 2500) {
                this.isAreaTooBig = true
            } else {
                this.isAreaTooBig = false
                this.coords.N = val.N
                this.coords.E = val.E
                this.coords.W = val.W
                this.coords.S = val.S
                this.checkRequiredFilled()
            }
        }
    }

    @Watch('dateFrom')
    private onDateFromChange(val) {
        this.dateTo = this.getFutureDate(val)
        this.checkRequiredFilled()
    }

    private async mounted() {
        await this.$nextTick()
        this.timeStart = performance.now()
    }
}
</script>

<style lang="scss">
.date-picker {
    width: 50%;
}

.area_error {
    color: red;
    margin-bottom: 20px;
}

.draw_button {
    border: 1px solid;
    padding: 10px 20px 10px 20px;
    text-align: center;
    text-justify: center;
    color: #0661a9;
}

.clear_button {
    border: 1px solid;
    padding: 10px 20px 10px 20px;
    text-align: center;
    text-justify: center;
    color: #d31d1d;
}

.wrapper {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    gap: 1rem;
}

.coord {
    color: darkblue;
    font-size: 16px;
}

.coord_wrapper {
    display: flex;
    gap: 10px;
    font-size: 20px;
    align-items: center;
    margin-bottom: 10px;
}

.service-workflow {
    padding: 15px;
    padding-bottom: 45px;

    .saved-runs {
        margin: 0 -15px -45px;

        .service-workflow {
            &__saved-run {
                padding: 20px 5px;
            }
        }
    }

    &__title {
        margin: 10px 0 20px;
        position: relative;
        display: flex;
        align-items: center;

        span {
            font-size: 20px;
            font-weight: bold;
            color: $black;
            text-transform: uppercase;
        }

        .cross {
            position: absolute;
            right: 0px;
            top: 0;

            &:before,
            &:after {
                background-color: $blue;
            }
        }
    }

    // &__title:nth-child(n + 2) {
    //     span {
    //         font-size: 18px;
    //     }
    // }

    &__run-title {
        span {
            font-size: 16px;
            font-weight: normal;
            color: $black;
            text-transform: uppercase;
            margin-top: 2rem;
        }
        margin-bottom: 20px;
        input {
            width: 100%;
            height: 40px;
            padding: 10px;
            border: 1px solid #e0e0e0;
        }
    }

    &__switcher {
        display: flex;
        width: 95%;
        margin: 0 auto 20px;
        padding-top: 3px;

        button {
            width: 50%;
            padding: 15px 20px;
            width: 100%;
            text-align: center;
            background: white;
            border-bottom: 3px solid $blue;
            font-size: 16px;
            font-weight: bold;
            transition: font-weight cubic-bezier(0.215, 0.61, 0.355, 1) 0.3s;
            transition: border-bottom cubic-bezier(0.215, 0.61, 0.355, 1) 0.3s;

            &:not(.active) {
                font-weight: normal;
                border-bottom: 1px solid $grey;
            }

            img {
                width: 25px;
                margin-right: 10px;
            }
        }
    }

    &__required-fields {
        font-size: 0.85em;
        color: $grey-dark;
        margin-bottom: 30px;
    }

    &__resource {
        border-bottom: none;
        margin-bottom: 20px;

        &-header,
        &-data {
            display: flex;

            > div {
                width: 33.333%;
                padding: 10px 15px;
                border: 1px solid $grey-lighter;
                border-right: none;
                margin-bottom: -1px;
                display: flex;
                align-items: center;
                min-height: 55px;
                text-align: center;

                &:last-child {
                    border-right: 1px solid $grey-lighter;
                }

                div {
                    + div {
                        margin-top: 10px;
                    }

                    flex: 0 0 100%;
                    justify-content: center;
                }

                span {
                    word-break: break-all;
                    font-size: 0.91em;
                    line-height: 1.3em;
                    margin-top: -2px;
                }

                .default-value {
                    cursor: pointer;
                }

                i {
                    font-style: normal;
                    margin-left: -3px;

                    &.pencil {
                        transform: scaleX(-1);
                        display: inline-block;
                        margin-right: 7px;
                        margin-left: 0;
                    }

                    &.restore-default-value {
                        margin-left: 5px;
                        width: 15px;
                        display: inline-block;
                        cursor: pointer;
                    }
                }
            }

            button {
                margin-right: 5px;
                font-size: 14px;

                .cross {
                    &:before,
                    &:after {
                        background: black;
                        width: 15px;
                        left: 4px;
                    }
                }
            }

            input {
                width: calc(100% - 20px);
                padding: 5px;
                border: 1px solid $grey-light;
            }
        }

        &-header {
            div {
                font-weight: bold;
                color: white;
                background: $grey-blue-tint;
                justify-content: center;
                min-height: 40px;
            }
        }

        .field-required {
            font-style: italic;
            font-size: 0.85em;
            color: $red;
        }

        .expert-option-info {
            font-style: italic;
            font-size: 0.85em;
        }
    }

    &__expert-options {
        margin-right: 15px;
        color: $blue;
        font-size: 1em;

        &:hover {
            color: black;
        }
    }

    &__platforms {
        margin: 30px 0;

        &-choice {
            display: flex;

            label {
                width: calc(25% - 15px);
                border: 1px solid $grey-lighter;
                display: flex;
                flex-direction: column;
                align-items: center;
                justify-content: center;
                position: relative;
                padding: 15px 10px;
                cursor: pointer;
                margin-right: 15px;
                text-align: center;

                &:last-child {
                    margin-right: 0;
                }

                input {
                    margin-bottom: 7px;
                }

                small {
                    display: none;
                    padding: 0 3px;
                    background: white;
                    position: absolute;
                    top: -7px;
                    right: 5px;
                    font-size: 0.76em;
                    font-style: italic;
                    text-transform: capitalize;
                }

                span {
                    text-transform: uppercase;
                }

                &.active {
                    border: 1px solid $blue;

                    small,
                    span {
                        color: $blue;
                    }
                }

                &.optimal {
                    small {
                        display: block;
                    }
                }

                &.disabled {
                    opacity: 0.5;
                    pointer-events: none;
                }
            }
        }
    }

    &__select-resources,
    &__run,
    &__add-run-btn {
        height: 37px;
        color: $blue;
        font-size: 17px;
        padding: 0 25px;
        position: relative;
        transition: background-color 150ms ease-in-out;

        &:hover:not(:disabled),
        &:active:not(:disabled) {
            &:after {
                border-left-color: white;
            }
        }
    }

    &__select-resources,
    &__select-input .custom-select__trigger,
    &__run,
    &__add-run-btn {
        width: 200px;

        span {
            display: inline-block;
            vertical-align: middle;
        }

        .arrow {
            position: relative;
            width: 15px;
            height: 15px;
            border: 1px solid #0661a9;
            border-radius: 50%;
            display: inline-block;
            margin-left: 3px;

            &:before,
            &:after {
                content: '';
                width: 5px;
                height: 2px;
                background: #0661a9;
                position: absolute;
                left: 4px;
                top: 4px;
                -webkit-transform: rotate(45deg);
                transform: rotate(45deg);
            }

            &:after {
                top: 7px;
                -webkit-transform: rotate(-45deg);
                transform: rotate(-45deg);
            }
        }
    }

    &__select-input .custom-select__trigger,
    &__run,
    &__add-run-btn {
        background-color: $blue;
        color: white;
        font-size: 17px;
        text-transform: capitalize;

        &:after {
            display: block;
            width: 0;
            height: 0;
            border-top: 8px solid transparent;
            border-bottom: 8px solid transparent;
            border-left: 8px solid $white;
            content: '';
            margin-left: 10px;
            position: absolute;
            right: 7px;
            top: calc(50% - 8px);
            transition: border-left-color 150ms ease-in-out;
        }
    }

    &__select-input .custom-select__trigger {
        &:after {
            border-top: 8px solid white;
            border-right: 8px solid transparent;
            border-left: 8px solid transparent;
            top: 15px;
        }

        &.active:after {
            top: 6px;
        }

        span {
            white-space: nowrap;
            display: block !important;
        }
    }

    .bjs-powered-by {
        display: none;
    }

    .bpmn-legend {
        margin-top: 20px;
        padding-top: 15px;
        border-top: 1px solid $grey-lighter;
        font-size: 0.85em;

        > span {
            margin: 0 5px;

            &.required {
                color: $red;
            }

            &.optional {
                color: $yellow;
            }

            &.expert {
                color: $grey;
            }

            &:first-letter {
                text-transform: uppercase;
            }
        }
    }

    @keyframes loadingDots {
        0% {
            opacity: 0.1;
        }

        20% {
            opacity: 1;
        }

        100% {
            opacity: 0.1;
        }
    }

    .platform-loading {
        margin-left: 10px;
        font-size: 0.85em;
        line-height: 1.5em;

        span {
            color: black;
            animation-name: loadingDots;
            animation-duration: 1s;
            animation-iteration-count: infinite;
            animation-fill-mode: both;

            &:nth-child(2) {
                animation-delay: 0.2s;
            }

            &:nth-child(3) {
                animation-delay: 0.4s;
            }
        }
    }

    svg[data-element-id] {
        position: absolute;
    }

    svg[data-element-id="autogenerated-1559206811037-process"],
    // EODESM
    svg[data-element-id="autogenerated-1540301452629-process"] {
        // EO-SDM
        left: 70px;
    }

    svg[data-element-id="autogenerated-1523951879929-process"],
    // LandMetrics
    svg[data-element-id="autogenerated-1528478431956-process"] {
        // PHENOLOGYMETRICS
        left: 190px;
    }

    svg[data-element-id='autogenerated-1527757840088-process'] {
        // COINS
        left: 240px;
    }

    svg[data-element-id="autogenerated-1664543852740-process"],
    // Trends Earth v1
    svg[data-element-id="autogenerated-1528362962211-process"],
    // INSTAR
    svg[data-element-id="autogenerated-1543344870684-process"],
    // WIMMED
    svg[data-element-id="autogenerated-1523461201638-process"] {
        // HYDROMAP
        left: 270px;
    }
}
</style>
