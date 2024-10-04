<template>
    <div class="date-slider__wrapper">
        <div class="date-options">
            <div class="date-slider__title">
                {{ $tc('generalFilters.dateRanges') }}:
            </div>
            <DateIntervalRadio :date-from="dateFrom" :date-to="dateTo" :date-period="datePeriod"
                @on-dates-change="setDates($event)" class="margin-top-5 full-width" />
        </div>
        <div class="date-slider">
            <div class="date-slider__min">{{ minYear }}</div>
            <div class="date-slider__max">{{ maxYear }}</div>
            <vue-slider ref="slider" :tooltip-placement="['bottom', 'bottom']" :min="minYear" :max="maxYear"
                :tooltip-formatter="dateFormatter" @change="sliderChange()" :value="dateYears" :tooltip="'always'" :enable-cross="false" />
        </div>
    </div>
</template>

<script lang="ts">
import { Component, Vue, Prop, Emit } from 'nuxt-property-decorator'
import VueSlider from 'vue-slider-component'
import DatepickerComponent from '@/components/DatePicker/DatePicker.vue'
import DateIntervalRadio from '@/components/Search/DateIntervalRadio.vue'

import date from '@/filters/date'

@Component({
    components: {
        VueSlider,
        DateIntervalRadio
    }
})
export default class DateSliderComponent extends Vue {
    @Prop({ required: true, type: Number }) public minYear!: number
    @Prop({ required: true, type: Number }) public maxYear!: number
    @Prop({ required: true, type: String }) public dateFrom!: string
    @Prop({ required: true, type: String }) public dateTo!: string
    @Prop({ required: false, type: String }) public datePeriod!: string

    private currentDay = new Date().getDate()
    private currentMonth = new Date().getMonth()+1
    private calendars: any[] = []
    private sliderYearFrom: any = ''
    private sliderYearTo: any = ''

    get dateYears() {
        const startYear = this.minYear
        const endYear = this.maxYear
        return [startYear, endYear]
    }

    set dateYears(dates) {
        this.calendars[0].value = this.getCaledarDateFrom(dates[0])
        this.calendars[1].value = this.getCaledarDateTo(dates[1])
    }

    public sliderChange() {
        const event = (this.$refs.slider as any).getValue()
        this.dateYears = event
        this.sliderYearFrom = event[0].toString()
        this.sliderYearTo = event[1].toString()
    }

    public setDates(value: {
        dateFrom: string
        dateTo: string
        datePeriod: string
    }) {
        this.changeDates(value.dateFrom, value.dateTo, value.datePeriod)
    }

    public getCaledarDateFrom(startYear: any) {
        const startDate = new Date(
            startYear,
            this.currentMonth,
            this.currentDay
        )
        const endDate = new Date(
            this.sliderYearTo,
            this.currentMonth,
            this.currentDay
        )
        this.changeDates(
            date(startDate.toISOString(), 'YYYY-MM-DD'),
            date(endDate.toISOString(), 'YYYY-MM-DD'),
            ''
        )
        return date(startDate.toISOString(), 'YYYY-MM-DD')
    }

    public getCaledarDateTo(endYear: number) {
        const startDate = new Date(
            this.sliderYearFrom,
            this.currentMonth,
            this.currentDay
        )
        const endDate = new Date(endYear, this.currentMonth, this.currentDay)
        this.changeDates(
            date(startDate.toISOString(), 'YYYY-MM-DD'),
            date(endDate.toISOString(), 'YYYY-MM-DD'),
            ''
        )
        return date(endDate.toISOString(), 'YYYY-MM-DD')
    }

    public dateFormatter(e: any) {
        if (this.dateFrom && this.dateTo) {
            const from = new Date(this.dateFrom)
            const to = new Date(this.dateTo)
            if (from && from.getFullYear() === e) {
                return `${from.getDate()}.${from.getMonth()+1}.${e}`
            }
            if (to && to.getFullYear() === e) {
                return `${to.getDate()}.${to.getMonth()+1}.${e}`
            }
        }
        return `${this.currentDay}.${this.currentMonth}.${e}`
    }

    @Emit()
    private onChangeDates(value: any) {
        return value
    }

    private changeDates(dateFrom: string, dateTo: string, datePeriod: string) {
        if (
            this.dateFrom !== dateFrom ||
            this.dateTo !== dateTo ||
            this.datePeriod !== datePeriod
        ) {
            this.onChangeDates({ dateFrom, dateTo, datePeriod })
        }
    }

    private mounted() {
        this.sliderYearFrom = this.dateFrom
        this.sliderYearTo = this.dateTo

        const calendarTriggers = Array.from(
            (this.$el as HTMLElement).querySelectorAll(
                '.vue-slider-dot-tooltip'
            )
        )
        for (let i = 0; i < calendarTriggers.length; i++) {
            const trigger = calendarTriggers[i] as HTMLElement
            // @ts-ignore
            const ComponentClass = Vue.extend(DatepickerComponent)
            const tomorrow = new Date()
            tomorrow.setDate(tomorrow.getDate() + 1)
            const disabledDates = [
                {
                    from: date(tomorrow, 'YYYY-MM-DD'),
                    to: '3000-01-01'
                }
            ]
            let value = date(
                i === 0
                    ? new Date(this.minYear, this.currentMonth, this.currentDay)
                    : new Date(
                        this.maxYear,
                        this.currentMonth,
                        this.currentDay
                    ),
                'YYYY-MM-DD'
            )
            if (i === 0 && this.dateFrom) {
                value = date(
                    new Date(this.dateFrom).toISOString(),
                    'YYYY-MM-DD'
                )
            } else if (this.dateTo) {
                value = date(new Date(this.dateTo).toISOString(), 'YYYY-MM-DD')
            }
            const calendarInstance = new ComponentClass({
                propsData: {
                    value,
                    clearable: false,
                    config: {
                        clickOpens: false,
                        disable: disabledDates,
                        appendTo:
                            document.querySelector('.geoss-data-pickers') ||
                            document.body
                    }
                }
            })

            calendarInstance.$on('input', (val: any) => {
                const date = new Date(val)
                if (i === 0) {
                    this.changeDates(val, this.dateTo, '')
                } else if (i === 1) {
                    this.changeDates(this.dateFrom, val, '')
                }
            })

            calendarInstance.$mount()
            this.calendars.push(calendarInstance)
            trigger.appendChild(calendarInstance.$el)
            trigger.addEventListener('mousedown', (event: Event) => {
                // TODO Datepicker support
                // event.stopPropagation()
                // (calendarInstance as any).fp.toggle()
            })
            trigger.addEventListener('click', (event: Event) => {
                event.stopPropagation()
            })
        }
    }
}
</script>

<style lang="scss">
.date-slider {
    margin-top: 15px;
    margin-bottom: 15px;
    position: relative;
    width: 100%;
    display: flex;
    align-items: center;

    &__title {
        font-size: 14px;
        color: white;
    }

    &__min,
    &__max {
        position: absolute;
        color: white;
        font-size: 18px;
        font-weight: bold;
        left: 30px;
    }

    &__max {
        left: auto;
        right: 30px;
    }

    &__wrapper {
        display: flex;

        @media (max-width: $breakpoint-sm) {
            flex-direction: column;
        }

        .date-options {
            width: 33%;
            padding-top: 6px;

            @media (max-width: $breakpoint-sm) {
                width: 100%;
            }
        }
    }
}

.vue-slider {
    height: 12px !important;
    padding: 0 !important;
    margin-left: 100px;
    width: calc(100% - 200px) !important;
}

.vue-slider-dot {
    width: 16px !important;
    height: 16px !important;
}

.vue-slider-dot-tooltip {
    .date-picker {
        top: 0;
        position: absolute;

        &>input {
            opacity: 0;
            height: 30px;
        }
    }
}

.vue-slider-dot-tooltip-inner {
    border: 2px solid white;
    border-radius: 15px;
    color: white;
    background: $blue;
    padding: 5px 10px;
    font-size: 12px;
}

.vue-slider-process,
.vue-slider-rail {
    background-color: #ffffff;
    border-radius: 5px;
}

.vue-slider-rail {
    background-color: #e0b318;
}
</style>
