<template>
    <div>
        <div id="map"></div>
        <MapProgressBar />
        <div id="map-tooltip">
            <p v-html="tooltipMessage"></p>
        </div>
        <div id="comparison-bar" class="comparison-bar">
            <div class="vertical-line">
                <div class="ball">
                    <div class="nav-arrow nav-arrow--left">&lsaquo;</div>
                    <div class="nav-arrow nav-arrow--right">&rsaquo;</div>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from 'nuxt-property-decorator';
import { MapActions } from '@/store/map/map-actions';
import { MapGetters } from '@/store/map/map-getters';
import MapUtils from '@/services/map/map-utils';
import { AppVueObj } from '~/data/global'

@Component
export default class MapComponent extends Vue {

    get map() {
        return window['geossMap'] // this.$store.getters[MapGetters.map];
    }

    get tooltipMessage() {
        return this.$store.getters[MapGetters.mapTooltipMessage];
    }

    get activeCompareLayer() {
        return this.$store.getters[MapGetters.compareLayer];
    }

    get compareBarPosition() {
        return this.$store.getters[MapGetters.compareBarPosition];
    }

    get center() {
        return this.$store.getters[MapGetters.center];
    }

    get initialZoom() {
        return this.$store.getters[MapGetters.initialZoom];
    }

    @Watch('activeCompareLayer')
    private onActiveCompareLayerChange(newLayer: any) {
        const overlay = this.$store.getters[MapGetters.mapTooltip];
        if (overlay) {
            overlay.setPosition(undefined);
        }
    }

    @Watch('compareBarPosition')
    private onCompareBarPosition(position: any) {
        const overlay = this.$store.getters[MapGetters.mapTooltip];
        if (overlay) {
            overlay.setPosition(undefined);
        }
    }

    @Watch('center')
    private onCenterChange(newVal: Array<number>, oldVal: Array<number>) {
        if (newVal[0] !== oldVal[0] || newVal[1] !== oldVal[1]) {
            this.setMapPosition();
        }
    }

    @Watch('initialZoom')
    private onInitialZoomChange(newVal: number, oldVal: number) {
        if (newVal !== oldVal) {
            this.setMapPosition();
        }

    }

    private setMapPosition() {
        this.map.setView(new AppVueObj.ol.View({
            center: this.center,
            zoom: this.initialZoom
        }));
    }

    private mounted() {
        const mousePositionController = MapUtils.createMapPositionController();
        const scaleLineController = MapUtils.createScaleLineController();
        const mapDragAndDropInteraction = MapUtils.createDragAndDropInteraction();

        this.$store.dispatch(
            MapActions.setMap,
            new AppVueObj.ol.Map({
                interactions: (AppVueObj.ol.interaction as any).defaults().extend([mapDragAndDropInteraction]),
                layers: [AppVueObj.ol.MapVector],
                target: 'map',
                view: new AppVueObj.ol.View({
                    center: this.$store.getters[MapGetters.center],
                    zoom: this.$store.getters[MapGetters.initialZoom],
                    minZoom: 2
                }),
                controls: [
                    scaleLineController,
                    mousePositionController
                ]
            })
        );

        MapUtils.addBoundingBoxInteractions(this.map);
        MapUtils.addUNSDStatisticsInteractions(this.map);
        MapUtils.addFeatureSelectionAsAOI(this.map);
        MapUtils.addOnErrorMapSwitcher();
        MapUtils.loadQueuedLayers(this.map);

        this.$store.dispatch(MapActions.setActiveLayerTileId, this.$store.getters[MapGetters.activeLayerTileId]);
    }
}
</script>

<style lang="scss">
    .comparison-bar {
        display: none;
        height: 100%;
        left: 50%;
        padding: 0 8px;
        position: absolute;
        top: 0;
        touch-action: none;
        z-index: 2;

        &:hover {
            cursor: pointer;
        }

        .vertical-line {
            background-color: white;
            height: inherit;
            position: relative;
            width: 2px;

            .ball {
                align-items: center;
                background: white;
                border-radius: 50%;
                display: flex;
                flex: 1;
                font-size: 36px;
                height: 40px;
                justify-content: space-between;
                left: 50%;
                padding: 5px;
                position: absolute;
                top: 50%;
                transform: translate(-50%, -50%);
                width: 40px;
            }
        }
    }

    .ol-scale-line {
        background: rgba(0, 60, 136, 0.5);
        border-radius: 4px;
        bottom: 10px;
        right: 300px;
        padding: 2px;
        position: absolute;

        @media (max-width: $breakpoint-lg) {
            right: 210px;
            display: none;
        }

        @media (max-width: $breakpoint-sm) {
            display: none;
        }

        .ol-scale-line-inner {
            border: 1px solid #eee;
            border-top: none;
            color: #eee;
            font-size: 14px;
            text-align: center;
            margin: 1px;
            will-change: contents, width;
        }
    }

    .ol-mouse-position {
        position: absolute;
        bottom: 40px;
        white-space: nowrap;
        right: 15px;
        font-size: 12px;
        color: white;
        text-shadow: 0px 1px 5px black, 0px -2px 5px black;
    }

    #map-tooltip {
        background-color: rgba(0, 60, 136, 0.8);
        border-radius: 5px !important;
        border: 1px solid black !important;
        color: white;
        margin-left: 10px;
        padding: 5px 10px;

        .poi-param {
            &__name {
                width: 220px;
            }
        }

        a {
            color: white;
            text-decoration: underline;
        }
    }
</style>
<style scoped lang="scss">
    #map {
        margin: 0;
        width: 100%;
        height: 100%;
        position: absolute;
        left: 0;
        top: 0;
        z-index: 1;
        background: url('/img/geo.jpg');
    }
</style>
