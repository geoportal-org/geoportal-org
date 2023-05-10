<template>
    <div>
        <div class="date-slider">
            <div class="date-slider__title">{{ $tc('generalFilters.dateRanges') }}:</div>
            <div class="date-slider__min">{{ minYear }}</div>
            <div class="date-slider__max">{{ maxYear }}</div>
            <!-- <vue-slider :tooltip-placement="['left', 'right']" :min="minYear" :max="maxYear"
                :tooltip-formatter="dateFormatter" @change="sliderChange($event)" @drag-start="dragStart()"
                @drag-end="dragStop($event)" :value="dateYears" :tooltip="'always'" /> -->
        </div>
        <DateIntervalRadio :date-from="dateFrom" :date-to="dateTo" :date-period="datePeriod"
            @on-dates-change="setDates($event)" class="margin-top-5 full-width" />
    </div>
</template>

<script lang="ts">
// @ts-nocheck

import { Component, Vue, Prop, Emit } from 'nuxt-property-decorator';

// import VueSlider from '~/components/Slider/vue-slider';
import DatepickerComponent from '@/components/DatePicker/DatePicker.vue';
import DateIntervalRadio from '@/components/Search/DateIntervalRadio.vue';

import date from '@/filters/date';

@Component({
    components: {
        //  VueSlider,
        DateIntervalRadio
    }
})
export default class DateSliderComponent extends Vue {
    @Prop({ required: true, type: Number }) public minYear!: number;
    @Prop({ required: true, type: Number }) public maxYear!: number;
    @Prop({ required: true, type: String }) public dateFrom!: string;
    @Prop({ required: true, type: String }) public dateTo!: string;
    @Prop({ required: false, type: String }) public datePeriod!: string;

    private currentDay = new Date().getDate();
    private currentMonth = new Date().getMonth();
    private calendars = [];
    private sliderDateFrom = null;
    private sliderDateTo = null;
    private dragAction = false;

    get dateYears() {
        const startYear = this.minYear;
        const endYear = this.maxYear;
        return [startYear, endYear];
    }

    set dateYears([startYear, endYear]) {
        this.calendars[0].value = this.getCaledarDateFrom(startYear);
        this.calendars[1].value = this.getCaledarDateTo(endYear);
    }

    public dragStart() {
        this.dragAction = true;
    }

    public dragStop($event) {
        this.dragAction = false;
        this.dateYears = $event;
    }

    public sliderChange($event) {
        if (!this.dragAction) {
            this.dateYears = $event;
        }
        this.sliderDateFrom = $event[0].toString();
        this.sliderDateTo = $event[1].toString();
    }

    public setDates(value: { dateFrom: string, dateTo: string, datePeriod: string }) {
        this.changeDates(value.dateFrom, value.dateTo, value.datePeriod);
    }

    public getCaledarDateFrom(startYear) {
        const startDate = new Date(startYear, this.currentMonth, this.currentDay);
        const endDate = new Date(this.sliderDateTo, this.currentMonth, this.currentDay);
        this.changeDates(date(startDate.toISOString(), 'YYYY-MM-DD'), date(endDate.toISOString(), 'YYYY-MM-DD'), '');
        return date(startDate.toISOString(), 'YYYY-MM-DD');
    }

    public getCaledarDateTo(endYear) {
        const startDate = new Date(this.sliderDateFrom, this.currentMonth, this.currentDay);
        const endDate = new Date(endYear, this.currentMonth, this.currentDay);
        this.changeDates(date(startDate.toISOString(), 'YYYY-MM-DD'), date(endDate.toISOString(), 'YYYY-MM-DD'), '');
        return date(endDate.toISOString(), 'YYYY-MM-DD');
    }

    public dateFormatter(year: number, index: number) {
        const startDate = new Date(this.minYear, this.currentMonth, this.currentDay);
        const endDate = new Date(this.maxYear, this.currentMonth, this.currentDay);

        if (this.sliderDateFrom) {
            startDate.setFullYear(this.sliderDateFrom);
        }
        if (this.sliderDateTo) {
            endDate.setFullYear(this.sliderDateTo);
        }

        let dateToShow = null;

        if (index === 0) {
            dateToShow = startDate;
        } else {
            dateToShow = endDate;
        }

        return date(dateToShow.toISOString(), 'DD.MM.YYYY');
    }

    @Emit()
    private onChangeDates(value) {
        return value;
    }

    private changeDates(dateFrom: string, dateTo: string, datePeriod: string) {
        if (this.dateFrom !== dateFrom || this.dateTo !== dateTo || this.datePeriod !== datePeriod) {
            this.onChangeDates({ dateFrom, dateTo, datePeriod });
        }
    }

    private mounted() {

        this.sliderDateFrom = this.dateFrom;
        this.sliderDateTo = this.dateTo;

        const calendarTriggers = Array.from((this.$el as HTMLElement).querySelectorAll('.vue-slider-dot-tooltip'));
        for (let i = 0; i < calendarTriggers.length; i++) {
            const trigger = calendarTriggers[i] as HTMLElement;
            const ComponentClass = Vue.extend(DatepickerComponent);
            const tomorrow = new Date();
            tomorrow.setDate(tomorrow.getDate() + 1);
            const disabledDates = [
                {
                    from: date(tomorrow, 'YYYY-MM-DD'),
                    to: '3000-01-01'
                }
            ];
            let value = date(i === 0 ? new Date(this.minYear, this.currentMonth, this.currentDay) : new Date(this.maxYear, this.currentMonth, this.currentDay), 'YYYY-MM-DD');
            if (i === 0 && this.dateFrom) {
                value = date(new Date(this.dateFrom).toISOString(), 'YYYY-MM-DD');
            } else if (this.dateTo) {
                value = date(new Date(this.dateTo).toISOString(), 'YYYY-MM-DD');
            }
            const calendarInstance = new ComponentClass({
                propsData: {
                    value,
                    clearable: false,
                    config: {
                        clickOpens: false,
                        disable: disabledDates,
                        appendTo: document.querySelector('.geoss-data-pickers') || document.body
                    }
                }
            });

            calendarInstance.$on('input', val => {
                const date = new Date(val);
                if (i === 0) {
                    this.changeDates(val, this.dateTo, '');
                } else if (i === 1) {
                    this.changeDates(this.dateFrom, val, '');
                }
            });

            calendarInstance.$mount();
            this.calendars.push(calendarInstance);
            trigger.appendChild(calendarInstance.$el);
            trigger.addEventListener('mousedown', (event: Event) => {
                event.stopPropagation();
                (calendarInstance as any).fp.toggle();
            });
            trigger.addEventListener('click', (event: Event) => {
                event.stopPropagation();
            });
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

    &__title {
        margin-bottom: 20px;
        font-size: 14px;
        color: white;
    }

    &__min,
    &__max {
        position: absolute;
        bottom: -3px;
        color: white;
        font-size: 18px;
        font-weight: bold;
        left: 30px;
    }

    &__max {
        left: auto;
        right: 30px;
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
}

.vue-slider-process,
.vue-slider-rail {
    background-color: #E0B318;
    border-radius: 5px;
}
</style>
