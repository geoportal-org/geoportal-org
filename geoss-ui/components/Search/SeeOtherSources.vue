<template>
    <div class="see-other-sources" v-if="alternateSources && alternateSources.length"
        data-tutorial-tag="search-container-other-sources">
        <span class="see-other-sources__label">{{ $tc('seeOtherSources.sources') }}:</span>
        <CustomSelect class="see-other-sources__select" v-model="selectedSource" :clearable="false" :appendToBody="false"
            :options="alternateSources" />
    </div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from 'nuxt-property-decorator';
import { SearchGetters } from '@/store/search/search-getters';
import { DataSourceGroup, AlternateSourcesMap } from '@/interfaces/DataSources';
import { SearchActions } from '@/store/search/search-actions';

@Component
export default class SeeOtherSourcesComponent extends Vue {

    public selectedSource = this.dataSource;

    get dataSource() {
        return this.$store.getters[SearchGetters.dataSource];
    }

    get currentResults() {
        return this.$store.getters[SearchGetters.currentResults];
    }

    get alternateSources() {
        const alternateSources = [];
        for (const sourceName in DataSourceGroup) {
            if (DataSourceGroup[sourceName] === DataSourceGroup[this.dataSource]) {
                const dataNameResults = `${sourceName}Results`;

                if (this.$store.getters[SearchGetters[dataNameResults as keyof typeof SearchGetters]]) {

                    const resultsObjest = this.$store.getters[SearchGetters[dataNameResults as keyof typeof SearchGetters]];
                    const totalResults = resultsObjest.totalResults || resultsObjest['opensearch:totalResults'] || resultsObjest['os:totalResults'];
                    alternateSources.push({ text: `${AlternateSourcesMap[sourceName]} ${this.totalResultsString(totalResults)}`, id: sourceName });
                }
            }
        }
        return alternateSources;
    }

    @Watch('selectedSource')
    private onSourceSelect() {
        this.$store.dispatch(SearchActions.setDataSource, { value: this.selectedSource });
    }

    @Watch('currentResults')
    private onResultsSelect(val: any, oldVal: any) {
        const oldThis = this;
        setTimeout(() => {
            if (!oldThis.$store.getters[SearchGetters.workflowInputId] && oldVal) {
                // Avoid this part when user selects input data for Information Producer or loads bookmark
                oldThis.$store.dispatch(SearchActions.setResultIdDetails, null);
            }
        }, 0);
    }

    @Watch('dataSource')
    private onSourceChange() {
        this.selectedSource = this.dataSource;
    }

    private totalResultsString(totalResults: number) {
        if (totalResults > 1000000) {
            return `(${Math.round(totalResults / 1000000)}M)`;
        } else if (totalResults > 1000) {
            return `(${Math.round(totalResults / 1000)}k)`;
        } else {
            return `(${totalResults})`;
        }
    }
}
</script>

<style lang="scss" scoped>
.see-other-sources {
    flex: 0 0 auto;
    flex-grow: 1;
    flex-basis: 0;
    background-color: rgba(black, 0.65);
    display: flex;
    color: white;
    align-items: center;
    justify-content: flex-start;
    font-size: 14px;
    padding: 10px 20px;
    margin-top: 5px;

    @media (max-width: $breakpoint-md) {
        padding-right: 10px;
    }

    @media (max-width: $breakpoint-sm) {
        padding-right: 20px;
        justify-content: center;
    }

    &__label {
        text-align: center;
        margin-right: 10px;
        font-weight: bold;
    }

    &__switch {
        font-weight: bold;
        padding: 5px 13px;
        color: white;
        background: $blue;
        border-radius: 10px;
        margin: 0 10px 0 0;
        font-style: italic;
        cursor: pointer;
        font-weight: normal;
        text-transform: capitalize;

        &:hover,
        &.active {
            color: white;
            background: $blue-dark;
        }

        &.active {
            display: none;
        }
    }

    // :deep(.custom-select) {
    //     &__trigger {
    //         background: $blue-dark;
    //         border-radius: 4px;
    //         color: white;
    //         padding: 2px 15px;
    //         white-space: nowrap;

    //         &:after {
    //             border-top: unset;
    //             border-bottom: 4px solid white;
    //         }
    //     }

    //     &__container {
    //         border: none;
    //         overflow: hidden;
    //         top: unset;
    //         bottom: calc(100% + 3px);
    //         max-width: none;
    //     }

    //     &__option {
    //         color: $blue-dark;
    //         min-width: 200px;

    //         &.selected {
    //             background: $blue-dark;
    //             color: white;
    //         }
    //     }
    // }

}
</style>
