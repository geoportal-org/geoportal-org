<template>
    <div>
        <div class="amerigeoss-results" :class="{ 'with-placeholder': amerigeossResultsPlaceholders }">
            <div v-for="(result, index) of amerigeossResults" :key="result.id" class="amerigeoss-result">
                <div class="amerigeoss-result__wrapper" v-show="resultIdDetails !== result.id"
                    :class="{ 'details-shown': resultIdDetails === result.id, 'amerigeoss-result__wrapper--underemphasize': (resultIdDetails && resultIdDetails !== result.id) }">
                    <div class="amerigeoss-result__text-data" @click="showResultDetails(result.id)">
                        <div v-if="result.title" v-line-clamp:20="2" class="amerigeoss-result__title">{{ result.title }}</div>
                        <div v-if="result.contributor && result.contributor.orgName" class="amerigeoss-result__contributor" v-line-clamp:20="1">{{ $tc('dabResult.organisation')}}: {{result.contributor.orgName }}</div>
                    </div>
                    <CrRelationsCheckbox :result="result" />
                </div>
                <SearchResultDabDetails :result="result" :index="index" :image="getImage(result.logo)" />
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
export default class SearchResultsAmerigeossComponent extends Vue {
    [x: string]: any;

    get amerigeossResults() {
        return (this.$store.getters[SearchGetters.amerigeossResults] ? this.$store.getters[SearchGetters.amerigeossResults].entry : []);
    }

    get resultIdDetails() {
        return this.$store.getters[SearchGetters.resultIdDetails];
    }

    get amerigeossResultsPlaceholders() {
        return (this.amerigeossResults.length % 2);
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
.amerigeoss-results {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
    align-content: center;

    &.with-placeholder {
        @media(max-width: $breakpoint-sm) {
            .amerigeoss-result.placeholder {
                display: none;
            }
        }
    }
}

.amerigeoss-result {
    width: 100%;
    margin: 0 2px 5px;


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
            max-width: 60%;
            max-height: 100%;
            margin: 0 auto;
            height: auto;
        }
    }

    &__wrapper {
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
            height: 0;
        }

        &:hover, &--highlighted {
            outline: 2px solid $blue;
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

        &--default {
            padding: 10px;
        }

        img {
            max-width: 100%;
            max-height: 100%;
        }
    }

    &__text-data {
        background: $white;
        padding: 15px;
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
