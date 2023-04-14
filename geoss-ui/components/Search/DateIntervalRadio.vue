<template>
    <div class="choice-options">
        <div v-for="option of options" :key="option.value" class="choice-option">
            <input v-model="value" type="radio" :value="option.value" :id="`dateInterval${option.title}${randomId}`" />
            <label :for="`dateInterval${option.title}${randomId}`">{{ option.title }}</label>
        </div>
    </div>
</template>

<script lang="ts">
// @ts-nocheck
import { Component, Vue, Emit, Prop } from 'nuxt-property-decorator';

import date from '@/filters/date';

@Component
export default class DateIntervalRadio extends Vue {
    public lastWeekValue: string = null;
    public lastMonthValue: string = null;
    public lastYearValue: string = null;
    public last10YearsValue: string = null;

    public randomId = Math.random();

    @Prop({ required: true, type: String }) private dateFrom!: string;
    @Prop({ required: true, type: String }) private dateTo!: string;
    @Prop({ required: false, type: String }) private datePeriod!: string;

    get options() {
        return [
            {
                title: this.$tc('generalFilters.last10Years'),
                value: this.last10YearsValue
            },
            {
                title: this.$tc('generalFilters.lastYear'),
                value: this.lastYearValue
            },
            {
                title: this.$tc('generalFilters.lastMonth'),
                value: this.lastMonthValue
            },
            {
                title: this.$tc('generalFilters.lastWeek'),
                value: this.lastWeekValue
            }
        ];
    }

    get value() {
        if (this.datePeriod) {
            return this.datePeriod;
        }

        const startDate = new Date(this.dateFrom);
        const today = new Date();

        startDate.setHours(0, 0, 0, 0);
        today.setHours(0, 0, 0, 0);

        const endDateLastWeek = new Date(today);
        endDateLastWeek.setDate(endDateLastWeek.getDate() - 7);
        endDateLastWeek.setHours(0, 0, 0, 0);

        const endDateLastMonth = new Date(today);
        endDateLastMonth.setMonth(endDateLastMonth.getMonth() - 1);
        endDateLastMonth.setHours(0, 0, 0, 0);

        const endDateLastYear = new Date(today);
        endDateLastYear.setFullYear(endDateLastYear.getFullYear() - 1);
        endDateLastYear.setHours(0, 0, 0, 0);

        const endDateLast10Years = new Date(today);
        endDateLast10Years.setFullYear(endDateLast10Years.getFullYear() - 10);
        endDateLast10Years.setHours(0, 0, 0, 0);

        if (endDateLastWeek.getTime() === startDate.getTime()) {
            return this.lastWeekValue;
        } else if (endDateLastMonth.getTime() === startDate.getTime()) {
            return this.lastMonthValue;
        } else if (endDateLastYear.getTime() === startDate.getTime()) {
            return this.lastYearValue;
        } else if (endDateLast10Years.getTime() === startDate.getTime()) {
            return this.last10YearsValue;
        }
    }

    @Emit()
    private onDatesChange(value) {
        return value;
    }

    private changeDates(dateFrom: string, dateTo: string, datePeriod: string) {
        if (this.dateFrom !== dateFrom || this.dateTo !== dateTo || this.datePeriod !== datePeriod) {
            this.onDatesChange({ dateFrom, dateTo, datePeriod });
        }
    }

    private setDatePeriod(value) {
        const startDate = new Date();
        startDate.setHours(0, 0, 0, 0);

        switch (value) {
            case this.lastWeekValue: {
                startDate.setDate(startDate.getDate() - 7);
                break;
            }
            case this.lastMonthValue: {
                startDate.setMonth(startDate.getMonth() - 1);
                break;
            }
            case this.lastYearValue: {
                startDate.setFullYear(startDate.getFullYear() - 1);
                break;
            }
            case this.last10YearsValue: {
                startDate.setFullYear(startDate.getFullYear() - 10);
                break;
            }
        }

        const endDate = new Date();
        endDate.setHours(0, 0, 0, 0);
        this.changeDates(date(startDate.toISOString(), 'YYYY-MM-DD'), '', value);
    }

    set value(value) {
        this.setDatePeriod(value);
    }

    private created() {
        this.lastWeekValue = 'last_week';
        this.lastMonthValue = 'last_month';
        this.lastYearValue = 'last_year';
        this.last10YearsValue = 'last_10_years';
        if (this.datePeriod && this.datePeriod !== '') {
            this.setDatePeriod(this.datePeriod);
        }
    }
}
</script>
