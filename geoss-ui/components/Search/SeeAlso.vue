<template>
    <div v-show="(recentSeeAlsoPhrases.length || recentSeeAlsoRecommendations.length) && currentResults" class="see-also" :class="{'active': containerActive, 'container-hidden': containerFullyHidden, 'in-transition': containerInTransition}">
        <DraggableResizable
            :handles="['br']"
            :disabled="false"
            :min-height="getSeeAlsoBoxHeight()"
            :min-width="250"
            :resizable="true"
            :z="1"
            :className="getSeeAlsoBoxClass()"
            parent="body"
            :offset-top="90"
            :offset-right="0"
            drag-handle=".see-also__drag-handle"
            >
            <button class="see-also-toggler"
                :class="{'container-fully-hidden': containerFullyHidden}"
                @click="toggleSeeAlso()"
                :data-tutorial-tag="containerFullyHidden ? 'see-also-show' : 'see-also-hide'"
                :style="containerFullyHidden ? '' : getTogglerStyles()">
                <span v-if="containerFullyHidden">{{ $tc('searchBar.seeAlso') }}</span>
                <span v-if="!containerFullyHidden" class="see-also-toggler__arrow"></span>
            </button>
            <div slot="handle" class="resize-icon">
                <span></span>
            </div>
            <div class="see-also__drag-handle" :title="$tc('searchBar.moveAround')"></div>
            <div class="see-also__scrollable-wrapper">
                <button class="see-also__scroll-button scroll-left"
                    :class="{disabled: disableScrollLeft}"
                    @mousedown="seeAlsoScroll('left')"
                    @mouseleave="stopScroll()"
                    @mouseup="stopScroll()"
                    @touchstart="seeAlsoScroll('left')"
                    @touchend="stopScroll()"
                    @touchcancel="stopScroll()"
                    ></button>
                <div v-if="recentSeeAlsoPhrases.length" class="see-also__scrollable-track">
                    <b class="see-also__title" data-tutorial-tag="see-also">{{ $tc('searchBar.seeAlso') }}</b>
                    <div v-for="(item, index) in recentSeeAlsoPhrases" :key="index" @click="selectSuggestion(item)" class="see-also__item">
                        <i>{{item}}</i>
                    </div>
                </div>
                <div v-if="recentSeeAlsoRecommendations.length" class="see-also__scrollable-track recommendations">
                    <b class="see-also__title recommendations" data-tutorial-tag="see-also-recommendations">{{ $tc('searchBar.recommendationsForYou') }}</b>
                    <div class="see-also__slider-wrapper">
                        <!-- <Carousel class="see-also__slider"
                            :class="{'no-arrows': (recentSeeAlsoRecommendations.length < 4)}"
                            :scrollPerPage="false"
                            :mouse-drag="true"
                            :paginationEnabled="false"
                            :per-page="3"
                            :navigationNextLabel="''"
                            :navigationPrevLabel="''"
                            :navigationEnabled="true">
                            <Slide v-for="(item, index) in recentSeeAlsoRecommendations" :key="index" class="see-also__slide">
                                <div @click="selectRecommendation(item)">
                                    <b class="see-also__slide--title">{{item.title}}</b>
                                    <p class="see-also__slide--description">{{item.description}}</p>
                                </div>
                            </Slide>
                        </Carousel> -->
                    </div>
                </div>
                <button class="see-also__scroll-button scroll-right"
                    :class="{disabled: disableScrollRight}"
                    @mousedown="seeAlsoScroll('right')"
                    @mouseleave="stopScroll()"
                    @mouseup="stopScroll()"
                    @touchstart="seeAlsoScroll('right')"
                    @touchend="stopScroll()"
                    @touchcancel="stopScroll()"></button>
            </div>
        </DraggableResizable>
    </div>
</template>

<script lang="ts">
import { Component, Vue, Prop, Watch } from 'nuxt-property-decorator';
import { SearchActions } from '@/store/search/search-actions';
import { SearchGetters } from '@/store/search/search-getters';
import { MapGetters } from '@/store/map/map-getters';
import { GeneralFiltersActions } from '@/store/generalFilters/general-filters-actions';
import DraggableResizable from '@/components/DraggableResizable.vue';
import { MapActions } from '@/store/map/map-actions';
import TutorialTagsService from '@/services/tutorial-tags.service';
// import { Carousel, Slide } from 'vue-carousel';
import LogService from '@/services/log.service';

@Component({
    components: {
        DraggableResizable,
        // Carousel,
        // Slide
    }
})
export default class SeeAlsoComponent extends Vue {
    public hideAnimationTime = 450;
    public containerActive = true;
    public containerFullyHidden = false;
    public containerInTransition = false;
    public scrollInterval: any = null;
    public disableScrollLeft = true;
    public disableScrollRight = true;
    public scrollTarget: any = null;

    get recentSeeAlsoPhrases() {
        return this.$store.getters[SearchGetters.recentSeeAlsoPhrases];
    }

    set recentSeeAlsoPhrases(value: string[]) {
        this.$store.dispatch(SearchActions.setRecentSeeAlsoPhrases, value);
    }

    get recentSeeAlsoRecommendations() {
        return this.$store.getters[SearchGetters.recentSeeAlsoRecommendations];
    }

    set recentSeeAlsoRecommendations(value: string[]) {
        this.$store.dispatch(SearchActions.setRecentSeeAlsoRecommendations, value);
    }

    get currentResults() {
        return this.$store.getters[SearchGetters.currentResults];
    }

    get hideSeeAlso() {
        return this.$store.getters[MapGetters.hideSeeAlso];
    }

    public getSeeAlsoBoxHeight() {
        let boxHeight = 109;
        if (this.recentSeeAlsoPhrases.length && this.recentSeeAlsoRecommendations.length) {
            boxHeight = 240;
        }
        return boxHeight;
    }

    public getSeeAlsoBoxClass() {
        let boxClass = 'see-also__drag-resize-wrapper';
        if (this.recentSeeAlsoPhrases.length && this.recentSeeAlsoRecommendations.length) {
            boxClass += ' with-recommendations';
        }
        return boxClass;
    }

    public getTogglerStyles() {
        const boxHeight = this.getSeeAlsoBoxHeight();
        let togglerStyles = '';
        if (boxHeight === 240) {
            togglerStyles = `width: ${boxHeight}px; right: -110px; top: calc(50% - 10px);`;
        }
        return togglerStyles;
    }

    public selectRecommendation(item: any) {
        this.$store.dispatch(SearchActions.setTargetIds, item.code);
        this.$store.dispatch(SearchActions.setDataSource, { value: item.dataSource });
        this.$store.dispatch(SearchActions.getResults);
        LogService.logRecommendationData('Select recommendation', 'recommended', item.title);
    }

    public selectSuggestion(item: string) {
        this.$store.dispatch(GeneralFiltersActions.setAdditionalSearchFields, 'abstract');
        this.$store.dispatch(GeneralFiltersActions.setPhrase, item);
        this.resetSeeAlso();
        this.$store.dispatch(SearchActions.getResults);
        LogService.logRecommendationData('Select suggestion', 'phrase', item);
    }

    public resetSeeAlso() {
        this.recentSeeAlsoPhrases = [];
        this.scrollTarget.scrollLeft = 0;
    }

    public seeAlsoScroll(direction: any) {
        if (!this.scrollInterval) {
            this.scrollInterval = setInterval(() => {
                if (direction === 'left') {
                    this.scrollTarget.scrollLeft -= 10;
                } else {
                    this.scrollTarget.scrollLeft += 10;
                }
                this.verifyScrollArrows();
            }, 20);
        }
    }

    public stopScroll() {
        clearInterval(this.scrollInterval);
        this.scrollInterval = null;
    }

    public verifyScrollArrows() {
        this.scrollTarget = document.querySelector('.see-also__scrollable-track') as HTMLElement;
        if (this.scrollTarget && this.scrollTarget.offsetWidth && this.scrollTarget.scrollWidth) {
            if (this.scrollTarget.offsetWidth < this.scrollTarget.scrollWidth) {
                if (this.scrollTarget.scrollLeft === 0) {
                    this.disableScrollLeft = true;
                } else {
                    this.disableScrollLeft = false;
                }
                if (this.scrollTarget.scrollLeft + this.scrollTarget.offsetWidth === this.scrollTarget.scrollWidth) {
                    this.disableScrollRight = true;
                } else {
                    this.disableScrollRight = false;
                }
            }
        }
    }

    public toggleSeeAlso() {
        this.$store.dispatch(MapActions.setHideSeeAlso, !this.hideSeeAlso);
    }

    private mounted() {
        this.$nextTick(() => {
            window.addEventListener('resize', this.verifyScrollArrows);
        });
    }

    private beforeDestroy() {
        window.removeEventListener('resize', this.verifyScrollArrows);
    }

    @Watch('currentResults')
    private onCurrentResultsChange() {
        if (this.currentResults) {
            this.$nextTick(() => this.verifyScrollArrows());
        }
    }

    @Watch('hideSeeAlso')
    private async onHideSeeAlsoChange() {
        this.containerInTransition = true;
        await this.$nextTick();
        this.containerActive = !this.hideSeeAlso;
        if(!this.containerActive) {
            setTimeout(() => {
                this.containerFullyHidden = true;
            }, this.hideAnimationTime);
        } else {
            this.containerFullyHidden = false;
            setTimeout(() => {
                this.containerInTransition = false;
            }, this.hideAnimationTime);
        }
        TutorialTagsService.refreshTagsGroup('see-also', false);
        TutorialTagsService.refreshTagsGroup('see-also', true, this.hideAnimationTime);
    }
}
</script>

<style lang="scss">
.see-also {
    .handle-br {
        right: 22px;

        @media (max-width: $breakpoint-xl) {
            right: 2px;
        }
    }
}
</style>

<style lang="scss" scoped>
.see-also {
    --offset-left: 925px;
    --offset-bottom: 207px;
    --offset-bottom-with-recommendations: 340px;
    --offset-right: 45px;
    @media (max-width: $breakpoint-xxl) {
        --offset-left: 860px;
        --offset-right: 30px;
    }

    height: 100%;
    left: 0;
    top: 0;
    width: 100vw;
    z-index: 6;

    @media (min-width: $breakpoint-lg) {
        &:not(.active) {
            .see-also__drag-resize-wrapper {
                left: 110vw !important;
                transform: none;
            }
        }

        &.in-transition {
            .see-also__drag-resize-wrapper {
                transition: right 450ms ease-in-out, left 450ms ease-in-out;
                position: absolute;
            }
        }
    }

    &.active {
        .see-also__drag-resize-wrapper {
            left: var(--offset-left);
        }
    }

    &-toggler {
        position: absolute;
        right: -45px;
        top: 44px;
        transform: rotate(270deg);
        height: 20px;
        background: $green-dark;
        overflow: hidden;
        padding: 0 5px;
        border-radius: 0 0 10px 10px;
        width: 109px;
        text-align: center;
        color: $white;
        text-transform: uppercase;
        font-size: 0.76em;
        white-space: nowrap;
        line-height: 20px;
        cursor: pointer;

        @media (max-width: $breakpoint-xl) {
            display: none;
        }

        &:hover {
            background: $green-darker;
        }

        &.container-fully-hidden {
            border-radius: 10px 10px 0 0;
            position: fixed;
            right: -45px;
            top: auto;
            margin-top: 44px;
        }

        &__arrow {
            &:after {
                content: '';
                width: 8px;
                height: 8px;
                border: 2px solid $white;
                position: absolute;
                left: 50%;
                top: 50%;
                transform: translate(-66%, -66%) rotate(45deg);
                border-top: 0;
                border-left: 0;
                transition: 0.2s ease all;
            }
        }
    }

    &__drag-resize-wrapper {
        overflow: hidden;
        padding-left: 10px;
        position: fixed;
        transition: right 450ms ease-in-out;
        z-index: 1;
        width: calc(100% - var(--offset-left) - var(--offset-right));
        top: calc(100% - var(--offset-bottom));
        height: 109px;
        -ms-overflow-style: none;
        scrollbar-width: none;

        &.with-recommendations {
            height: 240px;
            top: calc(100% - var(--offset-bottom-with-recommendations));

            @media (max-width: $breakpoint-xl) {
                height: auto;
            }
        }

        &::-webkit-scrollbar {
            display: none;
        }
    }

    &__slider-wrapper {
        width: 100%;
        @media (max-width: $breakpoint-xl) {
            padding: 0 4px;
        }
    }

    &__slider {
        width: 100%;
        padding: 10px 50px;
        display: flex;
        align-items: center;

        &.no-arrows {
            padding-left: 5px;
            padding-right: 5px;
        }

        @media (max-width: $breakpoint-xxl) {
            padding: 10px 35px;
        }

        @media (max-width: $breakpoint-xl) {
            padding: 0px 25px 5px;
        }
    }

    :v-deep(.see-also__slide) {
        // display: flex;
        // flex-direction: column;
        // color: white;
        // opacity: 0.8;
        // align-self: center;

        // &:hover {
        //     opacity: 1;
        //     cursor: pointer;
        // }

        // &--title,
        // &--description {
        //     display: -webkit-box;
        //     -webkit-box-orient: vertical;
        //     overflow: hidden;
        //     text-overflow: ellipsis;
        //     white-space: normal;
        // }

        // &--title {
        //     -webkit-line-clamp: 1;
        //     margin-bottom: 5px;
        // }

        // &--description {
        //     -webkit-line-clamp: 2;

        //     @media (max-width: $breakpoint-xl) {
        //         -webkit-line-clamp: 1;
        //     }
        // }
    }

    @media (max-width: $breakpoint-xl) {
        ::v-deep(.VueCarousel-navigation-button) {
            &:before,
            &:after {
                width: 11px;
                height: 2px;
            }

            &:before {
                top: 19px;
            }

            &:after {
                top: 25px;
            }
        }
    }

    &__scrollable-wrapper {
        background-color: rgba($blue, 0.75);
        position: absolute;
        left: 0;
        top: 0;
        right: 23px;
        bottom: 0;
        overflow: scroll;
        -ms-overflow-style: none;
        scrollbar-width: none;
        padding-left: 10px;

        @media (max-width: $breakpoint-xl) {
            right: 5px;
            padding-left: 0;
        }

        &::-webkit-scrollbar {
            display: none;
        }
    }

    &__scrollable-track {
        display: flex;
        flex-wrap: wrap;
        padding: 10px;
        white-space: nowrap;
        -ms-overflow-style: none;
        scrollbar-width: none;
        &::-webkit-scrollbar {
            display: none;
        }

        & + .see-also__scrollable-track {
            .see-also__title {
                border-top: 1px solid rgba(255,255,255,0.8);
                padding-top: 15px;
            }
        }
    }

    &__title {
        color: white;
        display: block;
        font-size: 16px;
        padding: 5px;
        text-transform: uppercase;
        width: 100%;

        @media (max-width: $breakpoint-xl) {
            &.recommendations {
                display: none;
            }
        }
    }

    &__item {
        background-color: white;
        border-radius: 13px;
        color: #0661a9;
        cursor: pointer;
        font-size: 14px;
        margin: 8px 8px 0 0;
        padding: 5px 10px;
        user-select: none;

        &:hover {
            background-color: rgba($grey-lighter, 0.95);
        }
    }

    &__drag-handle {
        background: rgba(white, 0.15);
        border-right: 1px solid rgba(white, 0.35);
        height: 100%;
        left: 0;
        margin: 0;
        position: absolute;
        top: 0;
        user-select: none;
        width: 10px;
        z-index: 1;

        &:hover {
            background: rgba(white, 0.35);
            cursor: move;
        }
    }

    .resize-icon {
        bottom: 0;
        cursor: se-resize;
        display: block;
        height: 20px;
        position: absolute;
        right: 2px;
        width: 20px;

        &:before,
        &:after,
        span {
            background-color: white;
            content: '';
            display: block;
            height: 1px;
            left: 0;
            position: absolute;
            top: 8px;
            transform: rotate(-45deg);
            width: 20px;
        }

        &:after {
            left: 6px;
            top: 11px;
            width: 13px;
        }

        span {
            left: 12px;
            top: 14px;
            width: 6px;
        }
    }

    &__scroll-button {
        display: none;
        position: absolute;
        top: 22px;
        border: solid white;
        border-width: 0 2px 2px 0;
        padding: 4px;

        &.disabled {
            opacity: 0.25;
            // pointer-events: none;
        }

        &.scroll {
            &-left {
                left: 12px;
                transform: rotate(135deg);
                -webkit-transform: rotate(135deg);
            }

            &-right {
                right: 12px;
                transform: rotate(-45deg);
                -webkit-transform: rotate(-45deg);
            }
        }
    }

    @media (max-width: $breakpoint-xl) {
        position: relative;
        width: auto;
        height: auto;
        margin-top: 5px;

        &__drag-resize-wrapper {
            position: relative;
            width: auto;
            height: auto;
            left: auto !important;
            padding-left: 0;
            top: auto;

            &:not(.active) {
                left: auto !important;
            }
        }

        &__scrollable-wrapper {
            position: relative;
            overflow: hidden;
            left: auto;
            right: auto;
        }

        &__scrollable-track {
            flex-wrap: nowrap;
            align-items: center;
            padding: 10px 0;
            margin: 0 30px;
            overflow: scroll;

            @media (max-width: $breakpoint-xl) {
                &.recommendations {
                    display: none;
                }
            }
        }

        &__drag-handle {
            display: none;
        }

        &__item {
            margin: 5px;
        }

        &__scroll-button {
            display: block;
        }

        &__title {
            padding-left: 0;
            width: auto;
        }
    }

    @media (max-width: $breakpoint-md) {
        &__title {
            display: none;
        }
    }
}
</style>
