<template>
    <div class="what3words">
        <input type="text" @focus="openContainer()" v-model="searchPhrase" @input="updateOptions()"
            :placeholder="$tc('placeholders.w3w')" @blur="closeContainer()" />
        <div class="what3words__options" v-show="showOptions">
            <div class="what3words__option" @mousedown="selectOption(option)" v-for="option of options" :key="option.words">
                {{ option.words }}</div>
        </div>
    </div>
</template>

<script lang="ts">
// @ts-nocheck
import { Component, Vue, Watch } from 'nuxt-property-decorator';

import GeossSearchApiService from '@/services/geoss-search.api.service';
import { GeneralFiltersActions } from '@/store/generalFilters/general-filters-actions';
import { MapActions } from '@/store/map/map-actions';
import { LayerTypes } from '@/interfaces/LayerTypes';
import { GeneralFiltersGetters } from '@/store/generalFilters/general-filters-getters';
import to from '@/utils/to';
import { AppVueObj } from '~/data/global';

interface Option {
    words: string;
    geometry: {
        lat: number;
        lng: number;
    };
}

@Component
export default class What3WordsSelectComponent extends Vue {
    public searchPhrase = '';
    public options: Option[] = [];
    public showOptions = false;

    get locationType() {
        return this.$store.getters[GeneralFiltersGetters.state].locationType;
    }

    public closeContainer() {
        this.showOptions = false;
    }

    public async updateOptions() {
        this.parseSearchPhrase();
        const [, options] = await to(GeossSearchApiService.getWhat3WordsOptions(this.searchPhrase));
        this.options = options;
        this.openContainer();
    }

    public selectOption(option: Option) {
        this.searchPhrase = option.words;
        const coordinates = {
            S: option.geometry.lat,
            W: option.geometry.lng,
            N: option.geometry.lat,
            E: option.geometry.lng
        };
        this.$store.dispatch(GeneralFiltersActions.setSelectedAreaCoordinates, coordinates);

        this.$store.dispatch(MapActions.addLayer, {
            layer: AppVueObj.ol.getWhat3wordsLayer(coordinates),
            id: 'what3wordsPin',
            type: LayerTypes.SELECTEDAREA
        });
    }

    @Watch('locationType')
    private onLocationTypeChanged() {
        this.searchPhrase = '';
        this.$store.dispatch(MapActions.removeLayer, 'what3wordsPin');
    }

    private parseSearchPhrase() {
        const parsed = this.searchPhrase!.replace(/\s/g, '.')!.match(/^[a-z][a-z\.]*/gi)[0];
        this.searchPhrase = parsed;
    }

    public openContainer() {
        if (this.options.length) {
            this.showOptions = true;
        }
    }
}
</script>

<style lang="scss" scoped>
.what3words {
    position: relative;

    input {
        width: 100%;
        border: none;
        height: 37px;
        padding: 6px 12px 6px 30px;
        font-size: 14px;
    }

    &__options {
        position: absolute;
        top: calc(100% + 2px);
        left: 0;
        width: 100%;
        max-height: 340px;
        background: white;
        overflow-y: auto;
        overflow-x: hidden;
        border-radius: 6px;
        z-index: 1;
        border: 1px solid rgba(0, 0, 0, 0.2);
        font-size: 14px;
    }

    &__option {
        padding: 10px 20px 10px 20px;
        cursor: pointer;

        &:hover {
            background-color: #eee;
        }
    }
}
</style>
