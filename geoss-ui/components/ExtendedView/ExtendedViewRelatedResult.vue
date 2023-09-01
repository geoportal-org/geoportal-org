<template>
    <div class="ev-related-result">
        <div v-if="result.id === 'placeholder'" class="ev-related-result__placeholder">
            <img :src="`/svg/${result.type}-gray.svg`" :alt="result.type" />
        </div>
        <div v-else class="ev-related-result__wrapper">
            <div class="ev-related-result__image"
                :class="{ 'ev-related-result__image--default': (getImage(result.logo) !== result.logo) }">
                <img :src="getImage(result.logo)" @error="imageLoadError(result.logo)" :alt="title" v-image-preview />
            </div>
            <div class="ev-related-result__text-data" @click="changeMainResult()">
                <div v-if="title" class="ev-related-result__title line-clamp--2">{{ title }}</div>
                <div v-if="summary && typeof summary === 'string'" class="ev-related-result__summary line-clamp--2"
                    v-html-to-text="summary"></div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
// @ts-nocheck
import { Component, Vue, Prop } from 'nuxt-property-decorator';
import { DataSourcesMask } from '@/interfaces/DataSources';
import { SearchActions } from '@/store/search/search-actions';
import { ExtendedViewActions } from '@/store/extendedView/extended-view-actions';

@Component
export default class ExtendedViewRelatedResultComponent extends Vue {
    [x: string]: any;
    @Prop({ default: null, type: Object }) public result!: any;

    public changeMainResult() {
        // If user changes tab in EV mode, change tab in normal mode too.
        // Otherwise the code will not interpret e.g. Zenodo titles and descriptions correctly.
        this.$store.dispatch(SearchActions.setDataSource, { value: DataSourcesMask[this.result.dataSource] });
        this.$store.dispatch(ExtendedViewActions.setResult, this.result);
    }

    get title() {
        if (this.result.metadata && this.result.metadata.title) {
            return this.result.metadata.title;
        } else {
            return this.result.title;
        }
    }

    get summary() {
        if (this.result.metadata && this.result.metadata.description) {
            return this.result.metadata.description;
        } else {
            return this.result.summary;
        }
    }
}
</script>

<style lang="scss" scoped>
.ev-related-result {
    flex: 0 1 auto;
    margin: 10px;
    width: calc(100% / 3 - 20px);

    &__wrapper {
        height: 95px;
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

        &:hover {
            background-color: white;

            &:after {
                border-top-color: white;
            }
        }
    }

    &__image {
        width: 95px;
        height: 95px;
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
        background: rgba(255, 255, 255, 0.9);
        padding: 5px;
        flex: 1 1 auto;
        overflow: hidden;
        cursor: pointer;
    }

    &__title {
        color: $blue;
        line-height: 20px;
        font-size: 17px;
        font-weight: bold;
        margin-bottom: 10px;
    }

    &__summary {
        font-size: 13px;
        line-height: 15px;
    }

    &__placeholder {
        align-items: center;
        background: $white-transparent;
        display: flex;
        height: 95px;
        justify-content: center;
        padding: 20px;

        @media(max-width: $breakpoint_sm) {
            display: none;
        }

        img {
            height: auto;
            margin: 0 auto;
            max-height: 100%;
            width: 60%;
        }
    }
}
</style>
