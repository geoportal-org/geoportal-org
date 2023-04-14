<template>
    <div v-if="showExtendedView">
        <div class="extended-view">
            <div class="extended-view__top">
                <div class="extended-view__main-result">
                    <SearchResultDabDetails :result="mainResult" :key="mainResultId" :index="0"
                        :image="getImage(mainResult.logo)" :extendedViewMode="true" />
                </div>
                <div class="extended-view__wikipedia-result">
                    <ExtendedViewWikipedia :result="wikipediaResult" />
                </div>
            </div>
            <div class="extended-view__bottom">
                <div class="extended-view__related-result">
                    <ExtendedViewRelatedResults :chosenResult="mainResult" :results="relatedResults" />
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from 'nuxt-property-decorator';
import { SearchGetters } from '@/store/search/search-getters';
import { ExtendedViewGetters } from '@/store/extendedView/extended-view-getters';
import SearchResultDabDetails from '@/components/Search/Results/ResultDetails.vue';
import ExtendedViewWikipedia from '@/components/ExtendedView/ExtendedViewWikipedia.vue';
import ExtendedViewRelatedResults from '@/components/ExtendedView/ExtendedViewRelatedResults.vue';

@Component({
    components: {
        SearchResultDabDetails,
        ExtendedViewWikipedia,
        ExtendedViewRelatedResults
    }
})
export default class ExtendedViewComponent extends Vue {
    [x: string]: any;
    public showExtendedView = false;

    get mainResult() {
        return this.$store.getters[ExtendedViewGetters.result];
    }

    get mainResultId() {
        // Used to change component key to force update of SearchResultDabDetails
        return this.$store.getters[ExtendedViewGetters.result].id;
    }

    get wikipediaResult() {
        const wikipediaEntries = this.$store.getters[SearchGetters.wikipediaResults];
        if (wikipediaEntries && wikipediaEntries.entry && wikipediaEntries.entry[0]) {
            return wikipediaEntries.entry[0];
        } else {
            return null;
        }
    }

    get relatedResults() {
        const results = {} as any;
        results.data = JSON.parse(JSON.stringify(this.$store.getters[SearchGetters.dabResults])) || { entry: [] };
        results.information = JSON.parse(JSON.stringify(this.$store.getters[SearchGetters.zenodoResults])) || { entry: [] };
        results.services = JSON.parse(JSON.stringify(this.$store.getters[SearchGetters.servicesResults])) || { entry: [] };
        return results;
    }

    get isExtendedViewActive() {
        return this.$store.getters[ExtendedViewGetters.isExtendedViewActive];
    }

    @Watch('isExtendedViewActive')
    private onExtendedViewEnabling(val: any) {
        // To achieve smooth transition, the showExtendedView must be handled manually
        if (val === true) {
            this.showExtendedView = true;
            this.$nextTick(() => {
                document.getElementsByClassName('extended-view')[0].classList.add('active');
            });
        } else {
            document.getElementsByClassName('extended-view')[0].classList.remove('active');
            setTimeout(() => {
                this.showExtendedView = false;
            }, 450);
        }
    }
}
</script>

<style lang="scss" scoped>
.extended-view {
    font-size: 24px;
    left: -50vw;
    position: fixed;
    width: calc(100vw - 200px);
    max-width: 1300px;
    top: 50%;
    transform: translate(-50%, -50%);
    transition: left 450ms ease-in-out;
    z-index: 4;

    &.active {
        left: 50%;
    }

    &__top {
        display: flex;
        margin-bottom: 14px;
    }

    &__bottom {
        height: 100%;
    }

    &__main-result {
        flex: 1 1 45%;
        margin-right: 7px;
    }

    &__wikipedia-result {
        flex: 1 1 55%;
        margin-left: 7px;
    }

    &__related-result {
        height: 100%;
    }
}
</style>
