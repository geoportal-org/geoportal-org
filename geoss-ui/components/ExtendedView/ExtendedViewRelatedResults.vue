<template>
    <div class="ev-related">
        <div class="ev-related__placeholder">
            <p>Related results not found</p>
        </div>
        <div class="ev-related__tabs">
            <button @click="sectionChange('data')" class="ev-related__tab data disabled-transparent"
                :class="{ active: activeSection === 'data' }" :disabled="!getResultsLength(dataResults)">{{
                    $tc('dataSources.dab') }}</button>
            <button @click="sectionChange('information')" class="ev-related__tab information disabled-transparent"
                :class="{ active: activeSection === 'information' }" :disabled="!getResultsLength(informationResults)">{{
                    $tc('dataSources.information') }}</button>
            <button @click="sectionChange('services')" class="ev-related__tab services disabled-transparent"
                :class="{ active: activeSection === 'services' }" :disabled="!getResultsLength(servicesResults)">{{
                    $tc('dataSources.services') }}</button>
        </div>
        <div class="ev-related__data">
            <div class="ev-related__left">
                <button class="disabled-transparent" :disabled="!leftArrowActive()" @click="changePage(false)"></button>
            </div>
            <div class="ev-related__results data" :class="{ active: activeSection === 'data' }">
                <ExtendedViewRelatedResult :result="dataResults[index]" v-for="(item, index) in dataResults" :key="index"
                    v-show="index >= startIndex && index < startIndex + resultsPerPage" />
            </div>
            <div class="ev-related__results information" :class="{ active: activeSection === 'information' }">
                <ExtendedViewRelatedResult :result="informationResults[index]" v-for="(item, index) in informationResults"
                    :key="index" v-show="index >= startIndex && index < startIndex + resultsPerPage" />
            </div>
            <div class="ev-related__results services" :class="{ active: activeSection === 'services' }">
                <ExtendedViewRelatedResult :result="servicesResults[index]" v-for="(item, index) in servicesResults"
                    :key="index" v-show="index >= startIndex && index < startIndex + resultsPerPage" />
            </div>
            <div class="ev-related__right">
                <button class="disabled-transparent" :disabled="!rightArrowActive()" @click="changePage(true)"></button>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { Component, Vue, Prop, Watch } from 'nuxt-property-decorator';
import ExtendedViewRelatedResult from '@/components/ExtendedView/ExtendedViewRelatedResult.vue';

@Component({
    components: {
        ExtendedViewRelatedResult
    }
})
export default class ExtendedViewRelatedResultsComponent extends Vue {
    @Prop({ default: null, type: Object }) public chosenResult!: any;
    @Prop({ default: null, type: Object }) public results!: any;

    public activeSection = 'data';
    public resultsPerPage = 6;
    public startIndex = 0;

    get dataResults() {
        return this.prepareResults('data');
    }

    get informationResults() {
        return this.prepareResults('information');
    }

    get servicesResults() {
        return this.prepareResults('services');
    }

    public getResultsLength(source: any[]) {
        const totalResults = source.reduce((total, curValue) => total += curValue.id !== 'placeholder' ? 1 : 0, 0);
        return totalResults;
    }

    public sectionChange(section: string) {
        this.startIndex = 0;
        this.activeSection = section;
    }

    public leftArrowActive() {
        if (this.startIndex > 0) {
            return true;
        }
        return false;
    }

    public rightArrowActive() {
        if (this.startIndex + this.resultsPerPage < this.results[this.activeSection].entry.length) {
            return true;
        }
        return false;
    }

    public changePage(forward: boolean) {
        if (forward) {
            this.startIndex += this.resultsPerPage;
        } else {
            this.startIndex -= this.resultsPerPage;
        }
    }

    @Watch('chosenResult')
    private onResourceChange() {
        const activeSectionResults = this.results[this.activeSection].entry;
        if (activeSectionResults.length) {
            return;
        } else if (this.getResultsLength(this.dataResults)) {
            this.activeSection = 'data';
        } else if (this.getResultsLength(this.informationResults)) {
            this.activeSection = 'information';
        } else {
            this.activeSection = 'services';
        }
    }

    // Exclude already selected resource from related list;
    // If needed, fill data with placeholders;
    private prepareResults(type: string) {
        const entries = this.results[type].entry.filter((el: any) => el.id !== this.chosenResult.id);
        if (entries.length % this.resultsPerPage < this.resultsPerPage) {
            for (let i = entries.length % this.resultsPerPage; i < this.resultsPerPage; i++) {
                entries.push({ id: 'placeholder', type });
            }
        }
        return entries;
    }

    private mounted() {
        this.onResourceChange();
    }

}
</script>

<style lang="scss" scoped>
.ev-related {
    height: 100%;

    &__placeholder {
        display: none;
        height: 100%;

        &.active {
            display: flex;
        }

        p {
            color: #888;
            margin: auto;
        }
    }

    &__tabs {
        color: white;
        font-size: 20px;
        text-transform: uppercase;
        font-weight: bold;
        white-space: nowrap;
        display: flex;
        align-items: center;
        justify-content: flex-end;
        cursor: pointer;
        width: 100%;
        background-color: transparent;
        padding: 0;
        height: 44px;

        @media (max-width: $breakpoint-sm) {
            font-size: 17px;
        }
    }

    &__tab {
        background: rgba($green, 0.85);
        width: 33.34%;
        color: white;
        text-transform: uppercase;
        font-size: 20px;
        padding: 10px 0;
        position: relative;
        min-height: 100%;

        @media (max-width: $breakpoint-sm) {
            font-size: 17px;
        }

        &.data {
            border-top-right-radius: 17px;
            margin-right: 2px;

            &:before {
                content: '';
                height: 30px;
                position: absolute;
                left: auto;
                right: -30px;
                bottom: -15px;
                width: 30px;
                background: radial-gradient(circle at 100% 0%, rgba(0, 0, 0, 0) 14px, rgba($green, 0.85) 15px) no-repeat;
                background-size: 50% 50%;
            }
        }

        &.information {
            border-bottom-right-radius: 0;
            border-bottom-left-radius: 15px;
            border-top-right-radius: 17px;
            margin-right: 2px;

            &:after {
                content: '';
                height: 30px;
                position: absolute;
                top: 0;
                width: 30px;
                background: radial-gradient(circle at 0 100%, rgba(0, 0, 0, 0) 14px, rgba($green, 0.85) 15px) no-repeat;
                background-size: 50% 50%;
                left: -15px;
                right: auto;
            }

            &:before {
                content: '';
                height: 30px;
                position: absolute;
                left: auto;
                right: -30px;
                bottom: -15px;
                width: 30px;
                background: radial-gradient(circle at 100% 0%, rgba(0, 0, 0, 0) 14px, rgba($green, 0.85) 15px) no-repeat;
                background-size: 50% 50%;
            }
        }

        &.services {
            border-bottom-right-radius: 0;
            border-bottom-left-radius: 15px;

            &:after {
                content: '';
                height: 30px;
                position: absolute;
                top: 0;
                width: 30px;
                background: radial-gradient(circle at 0 100%, rgba(0, 0, 0, 0) 14px, rgba($green, 0.85) 15px) no-repeat;
                background-size: 50% 50%;
                left: -15px;
                right: auto;
            }
        }

        &.active {
            background: rgba($blue, 0.85);

            &.data {
                &:before {
                    background: radial-gradient(circle at 100% 0%, rgba(0, 0, 0, 0) 14px, rgba($blue, 0.85) 15px) no-repeat;
                    background-size: 50% 50%;
                }
            }

            &.information {
                &:after {
                    background: radial-gradient(circle at 0 100%, rgba(0, 0, 0, 0) 14px, rgba($blue, 0.85) 15px) no-repeat;
                    background-size: 50% 50%;
                }

                &:before {
                    background: radial-gradient(circle at 100% 0%, rgba(0, 0, 0, 0) 14px, rgba($blue, 0.85) 15px) no-repeat;
                    background-size: 50% 50%;
                }
            }

            &.services {
                &:after {
                    background: radial-gradient(circle at 0 100%, rgba(0, 0, 0, 0) 14px, rgba($blue, 0.85) 15px) no-repeat;
                    background-size: 50% 50%;
                }
            }
        }

        &[disabled] {
            background: rgba($grey-dark, 0.85);

            &.data {
                &:before {
                    background: radial-gradient(circle at 100% 0%, rgba(0, 0, 0, 0) 14px, rgba($grey-dark, 0.85) 15px) no-repeat;
                    background-size: 50% 50%;
                }
            }

            &.information {
                &:after {
                    background: radial-gradient(circle at 0 100%, rgba(0, 0, 0, 0) 14px, rgba($grey-dark, 0.85) 15px) no-repeat;
                    background-size: 50% 50%;
                }

                &:before {
                    background: radial-gradient(circle at 100% 0%, rgba(0, 0, 0, 0) 14px, rgba($grey-dark, 0.85) 15px) no-repeat;
                    background-size: 50% 50%;
                }
            }

            &.services {
                &:after {
                    background: radial-gradient(circle at 0 100%, rgba(0, 0, 0, 0) 14px, rgba($grey-dark, 0.85) 15px) no-repeat;
                    background-size: 50% 50%;
                }
            }
        }
    }

    &__data {
        background-color: rgba($blue, 0.85);
        display: flex;
        height: 250px;
    }

    &__left,
    &__right {
        display: flex;
        flex: 0 0 auto;

        button {
            height: 35px;
            margin: auto 25px;
            position: relative;
            width: 25px;

            &:after,
            &:before {
                content: "";
                background: white;
                width: 20px;
                height: 3px;
                border-radius: 5px;
                position: absolute;
                top: 10px;
                transform: rotate(-40deg);
                right: 3px;
            }

            &:after {
                top: 22px;
                transform: rotate(40deg);
            }

            &[disabled] {

                &:after,
                &:before {
                    background: #bbb;
                }
            }
        }
    }

    &__right {
        button {
            &:before {
                transform: rotate(40deg);
            }

            &:after {
                transform: rotate(-40deg);
            }
        }
    }

    &__results {
        align-content: center;
        display: none;
        flex: 1 1 auto;
        flex-wrap: wrap;
        justify-content: flex-start;
        overflow: hidden;
        margin: 0 auto;
        max-width: 1110px;

        &.active {
            display: flex;
        }
    }
}
</style>
