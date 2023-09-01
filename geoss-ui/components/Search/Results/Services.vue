<template>
    <div>
        <div class="services-results" :class="{ 'with-placeholder': servicesResultsPlaceholders }">
            <div v-for="(result, index) of serviceResults" :key="result.id" class="services-result">
                <div class="services-result__wrapper"
                    :class="{ 'details-shown': resultIdDetails === result.id, 'services-result__wrapper--underemphasize': (resultIdDetails && resultIdDetails !== result.id) }">
                    <div class="services-result__image"
                        :class="{ 'services-result__image--default': (getImage(result.logo) !== result.logo) }">
                        <img :src="getImage(result.logo)" @error="imageLoadError(result.logo)" :alt="result.title"
                            v-image-preview />
                    </div>
                    <div class="services-result__text-data" @click="showResultDetails(result.id)">
                        <div v-if="result.title" class="services-result__title line-clamp--2">{{ result.title }}</div>
                        <div v-if="result.contributor && result.contributor.orgName"
                            class="services-result__contributor line-clamp--1">
                            ({{ $tc('dabResult.organisation') }}: {{ result.contributor.orgName }})
                        </div>
                        <div v-if="result.summary && typeof result.summary === 'string'"
                            class="services-result__summary line-clamp--3" :class="{ 'checkbox-active': checkboxActive }"
                            v-html-to-text="result.summary"></div>
                    </div>
                    <CrRelationsCheckbox :result="result" />
                </div>
                <SearchResultDabDetails :result="result" :index="index" :image="getImage(result.logo)" />
            </div>
            <div v-if="servicesResultsPlaceholders" class="services-result placeholder">
                <img :src="`/svg/services-gray.svg`" alt="SERVICES" />
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { Component, Vue } from 'nuxt-property-decorator';

import SearchResultDabDetails from '@/components/Search/Results/ResultDetails.vue';
import CrRelationsCheckbox from '@/components/Search/CrRelationsCheckbox.vue';

import { SearchGetters } from '@/store/search/search-getters';
import { SearchActions } from '@/store/search/search-actions';

@Component({
    components: {
        SearchResultDabDetails,
        CrRelationsCheckbox
    }
})
export default class SearchResultsServicesComponent extends Vue {
    [x: string]: any;
    get serviceResults() {
        return (this.$store.getters[SearchGetters.servicesResults] ? this.$store.getters[SearchGetters.servicesResults].entry : []);
    }

    get resultIdDetails() {
        return this.$store.getters[SearchGetters.resultIdDetails];
    }

    get servicesResultsPlaceholders() {
        return (this.serviceResults.length % 2);
    }

    get checkboxActive() {
        return this.$store.getters[SearchGetters.crRelation];
    }

    public showResultDetails(id: string) {
        if (this.resultIdDetails === id) {
            this.$store.dispatch(SearchActions.setResultIdDetails, null);
        } else {
            this.$store.dispatch(SearchActions.setResultIdDetails, id);
        }
    }
}
</script>

<style lang="scss" scoped>
.services-results {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
    align-content: center;

    &.with-placeholder {
        @media(max-width: $breakpoint-sm) {
            .services-result.placeholder {
                display: none;
            }
        }
    }
}

.services-result {
    width: calc(50% - 2.5px);
    margin-bottom: 5px;

    &:last-child,
    &:nth-last-of-type(1),
    &:nth-last-of-type(2) {
        margin-bottom: 0;
    }


    @media (max-width: $breakpoint-sm) {
        width: 100%;
    }

    &.placeholder {
        padding: 20px;
        background: $white-transparent;
        display: flex;
        align-items: center;
        justify-content: center;
        height: 125px;

        @media(max-width: $breakpoint_sm) {
            display: none;
        }

        img {
            width: 60%;
            max-height: 100%;
            margin: 0 auto;
            height: auto;
        }
    }

    &__wrapper {
        height: 125px;
        display: flex;
        position: relative;

        &:after {
            content: '';
            position: absolute;
            left: calc(50% - 5px);
            top: 100%;
            border-top: 10px solid $white-transparent;
            border-left: 10px solid transparent;
            border-right: 10px solid transparent;
            display: none;
        }

        &.details-shown {
            &:after {
                display: block;
            }
        }

        &:hover,
        &--highlighted {
            box-shadow: 0 0 20px white;
            background-color: white;

            &:after {
                border-top-color: white;
            }
        }

        &--underemphasize {
            opacity: 0.85;
        }
    }

    &__image {
        width: 125px;
        height: 125px;
        background: $grey;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        flex: 0 0 auto;
        position: relative;

        &--default {
            padding: 10px;
        }

        img {
            max-width: 100%;
            max-height: 100%;
        }
    }

    &__text-data {
        background: $white-transparent;
        padding: 10px;
        flex: 1 1 auto;
        overflow: hidden;
        cursor: pointer;
    }

    &__title {
        color: $blue;
        line-height: 20px;
        font-size: 17px;
        font-weight: bold;
        margin-bottom: 3px;
    }

    &__contributor {
        font-size: 12px;
        margin-bottom: 10px;
        width: auto;
        display: block;
        color: #777;
        font-style: italic;
    }

    &__summary {
        font-size: 13px;
        line-height: 15px;

        &.checkbox-active {
            padding-right: 30px;
        }
    }
}
</style>
