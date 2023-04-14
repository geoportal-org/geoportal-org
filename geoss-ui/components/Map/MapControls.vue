<template>
    <div class="map-controls">
        <MapControlDraw data-tutorial-tag="map-control-aoi" class="map-control map-control__area-selection"
            :disabled="isExtendedViewActive" />
        <MapControlLayers data-tutorial-tag="map-control-layers" class="map-control map-control__layers"
            :class="{ 'disable-container': generalFiltersInChange }" :disabled="isExtendedViewActive" />
        <!-- <MapControlType data-tutorial-tag="map-control-basemap" v-if="!isWidget" class="map-control map-control__type" :class="{'disable-container': generalFiltersInChange}"
				:disabled="isExtendedViewActive" /> -->
        <button data-tutorial-tag="map-control-toggle-search"
            class="map-control map-control__toggle-search-container disabled-transparent" @click="toggleSearchContainer()"
            :title="$tc('searchBar.showHideAllMap')" :disabled="isExtendedViewActive">
            <i class="icomoon-minimize" v-show="!showFullMap"></i>
            <i class="icomoon-fullscreen" v-show="showFullMap"></i>
        </button>
        <FileDownloadNotifier data-tutorial-tag="map-control-download-file" class="file-download-notifier"
            :class="{ 'disable-container': generalFiltersInChange }" :disabled="isExtendedViewActive" />
        <BulkDownloadNotifier data-tutorial-tag="map-control-download-bulk" class="bulk-download-notifier"
            :class="{ 'disable-container': generalFiltersInChange }" :disabled="isExtendedViewActive" />
    </div>
</template>

<script lang="ts">
import { Component, Vue } from 'nuxt-property-decorator';

import MapControlDraw from '@/components/Map/MapControlDraw.vue';
import MapControlLayers from '@/components/Map/MapControlLayers.vue';
import MapControlType from '@/components/Map/MapControlType.vue';
import FileDownloadNotifier from '@/components/FileDownloadNotifier.vue';
import BulkDownloadNotifier from '@/components/BulkDownloadNotifier.vue';

import { MapGetters } from '@/store/map/map-getters';
import { Timers } from '@/data/timers';
import { MapActions } from '@/store/map/map-actions';
import { GeneralFiltersGetters } from '@/store/generalFilters/general-filters-getters';
import { GeneralGetters } from '@/store/general/general-getters';
import { ExtendedViewGetters } from '@/store/extendedView/extended-view-getters';

@Component({
    components: {
        MapControlDraw,
        MapControlLayers,
        MapControlType,
        FileDownloadNotifier,
        BulkDownloadNotifier
    }
})
export default class MapControlsComponent extends Vue {

    get isWidget() {
        return this.$store.getters[GeneralGetters.isWidget];
    }

    get showFullMap() {
        return this.$store.getters[MapGetters.showFull];
    }

    get isExtendedViewActive() {
        return this.$store.getters[ExtendedViewGetters.isExtendedViewActive];
    }

    get generalFiltersInChange() {
        return this.$store.getters[GeneralFiltersGetters.inChangeProcess];
    }

    public toggleSearchContainer() {
        this.$store.dispatch(MapActions.setShowFull, !this.showFullMap);
    }
}
</script>

<style lang="scss">
.map-controls {
    position: absolute;
    right: 45px;
    top: 170px;
    z-index: 3;
    display: flex;
    flex-direction: column;
    align-items: center;
    filter: drop-shadow(0px 0px 1px #222);

    @media (max-width: $breakpoint-lg) {
        top: auto;
        bottom: 5px;
        left: 10px;
        right: auto;
        flex-direction: row;
    }

    @media (max-width: $breakpoint-sm) {
        bottom: 40px;
    }

    @media (max-width: $breakpoint-xs) {
        bottom: 50px;
    }
}

.map-control {
    margin-bottom: 25px;
    width: 32px;
    height: 32px;
    background-repeat: no-repeat;
    background-size: 100% 100%;
    color: white;
    font-size: 29px;

    @media (max-width: $breakpoint-lg) {
        margin-bottom: 0;
        margin-right: 30px;

        width: 29px;
        height: 29px;
    }

    @media (max-width: $breakpoint-sm) {
        margin-right: 20px;
    }

    &__toggle-search-container {
        margin-bottom: 15px;
        display: none;

        @media (max-width: $breakpoint-lg) {
            display: block;
            margin-bottom: 0;
        }
    }

    i {
        text-shadow: $text-shadow-black;
    }

    .icomoon-map-search-switcher {
        font-size: 35px;
    }

    .icomoon-fullscreen {
        font-size: 28px;
        position: relative;
        top: 1px;
        left: 1px;

        @media (max-width: $breakpoint-sm) {
            left: -8px;
        }
    }

    .icomoon-minimize {
        @media (max-width: $breakpoint-sm) {
            position: relative;
            left: -10px;
        }
    }
}
</style>
