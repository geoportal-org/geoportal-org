<template>
    <button class="map-control disabled-transparent" :class="{ active: drawActive }"
        :title="$tc('mapControls.areaOfInterest')" :disabled="disabled" @click="toggleDrawInteraction()">
        <i class="icomoon-select"></i>
    </button>
</template>

<script lang="ts">
import { Component, Vue, Watch, Prop } from 'nuxt-property-decorator';

import MapCoordinatesUtils from '@/services/map/coordinates-utils';
import { MapActions } from '@/store/map/map-actions';
import { GeneralFiltersActions } from '@/store/generalFilters/general-filters-actions';
import { GeneralFiltersGetters } from '@/store/generalFilters/general-filters-getters';
import { LayerTypes } from '@/interfaces/LayerTypes';
import { MapGetters } from '@/store/map/map-getters';
import { MapCoordinate } from '@/interfaces/MapCoordinate';
import { SearchGetters } from '@/store/search/search-getters';
import { AppVueObj } from '~/data/global';

@Component
export default class MapControlDrawComponent extends Vue {
    @Prop({ default: false, type: Boolean }) public disabled!: boolean;
    private draw: any = null;
    public drawActive = false;

    get showFullMap() {
        return this.$store.getters[MapGetters.showFull];
    }

    get map() {
        return window['geossMap']; // this.$store.getters[MapGetters.map];
    }

    get selectedAreaCoordinates() {
        return this.$store.getters[GeneralFiltersGetters.state].selectedAreaCoordinates;
    }

    get workflow() {
        return this.$store.getters[SearchGetters.workflow];
    }

    get workflowCoordinates() {
        return this.$store.getters[SearchGetters.workflowCoordinates];
    }

    get workflowMapDraw() {
        return this.$store.getters[GeneralFiltersGetters.state].workflowMapDraw;
    }

    /**
     * Handler for selecting the area of interest.
     */
    public addDrawInteraction() {
        this.drawActive = true;
        this.$store.dispatch(GeneralFiltersActions.setLocationType, 'coordinates');
        this.$store.dispatch(MapActions.setShowFull, true);

        if (this.workflow && this.workflowCoordinates) {
            this.$store.dispatch(GeneralFiltersActions.setSelectedAreaCoordinates, this.workflowCoordinates);
        } else {
            this.$store.dispatch(GeneralFiltersActions.setSelectedAreaCoordinates, null);
        }

        this.draw = new AppVueObj.ol.interaction.Draw({
            source: AppVueObj.ol.MapSource,
            type: 'Circle',
            geometryFunction: AppVueObj.ol.interaction.Draw.createBox(),
            maxPoints: 2
        });

        this.draw.on('drawend', () => {
            this.removeDrawInteraction();
            AppVueObj.ol.MapSource.once('addfeature', (evt: any) => {
                const feature = evt.feature;
                const coords = feature.getGeometry().getCoordinates();

                const coordsWSEN = MapCoordinatesUtils.parseCoordinates(coords);
                let W = coordsWSEN[0];
                const S = coordsWSEN[1];
                let E = coordsWSEN[2];
                const N = coordsWSEN[3];

                const normalizedLongitude = MapCoordinatesUtils.normalizeLongitude(W, E);
                W = normalizedLongitude[0];
                E = normalizedLongitude[1];

                this.$store.dispatch(GeneralFiltersActions.setBoundingBoxRelation, 'OVERLAPS');
                this.$store.dispatch(GeneralFiltersActions.setSelectedAreaCoordinates, { W, S, E, N });

                AppVueObj.ol.MapSource.clear();
            });
            this.$store.dispatch(MapActions.setShowFull, false);
        });

        this.map.addInteraction(this.draw);
    }

    public removeDrawInteraction() {
        this.drawActive = false;
        this.map.removeInteraction(this.draw);
    }

    public toggleDrawInteraction() {
        this.$store.dispatch(GeneralFiltersActions.setContainerVisible, true);
        this.$store.dispatch(MapActions.setActiveCompareLayer, null);
        if (this.drawActive) {
            this.removeDrawInteraction();
        } else {
            this.addDrawInteraction();
        }
    }

    public clearDrawnLayers() {
        this.$store.dispatch(MapActions.removeLayer, 'area-selected');
    }

    public drawLayer({ W, S, E, N }: MapCoordinate) {
        const normalizedLongitude = MapCoordinatesUtils.normalizeLongitude(W, E);
        const normalizedW = normalizedLongitude[0];
        const normalizedE = normalizedLongitude[1];

        const longitudes = MapCoordinatesUtils.denormalizeLongitude(W, E);
        W = longitudes[0];
        E = longitudes[1];

        const rectangle = new AppVueObj.ol.Feature({
            geometry: new AppVueObj.ol.geom.Polygon(MapCoordinatesUtils.coordinatesForDrawing([W, S, E, N]))
        });
        rectangle.getGeometry().transform('EPSG:4326', 'EPSG:3857');

        this.$store.dispatch(MapActions.addLayer, {
            id: 'area-selected',
            coordinate: { W, S, E, N },
            layer: AppVueObj.ol.getMapDrawnLayer(rectangle),
            type: LayerTypes.SELECTEDAREA
        });
    }

    /**
     * Area of interest can be selected by various methods,
     * including: Google Places, W3W, manual coordinates,
     * seleting continent or country. Each of those methods
     * specify the coordinates for the area of interest and
     * this method is responsible for drawing the area every
     * time when it changes
     */
    @Watch('selectedAreaCoordinates')
    private onSelectedAreaCoordinatesChanged(val: any) {
        this.clearDrawnLayers();
        if (this.validateCoordinates(val)) {
            this.drawLayer(val);
            this.$store.dispatch(MapActions.centerMap, val);
        }
    }

    @Watch('workflowMapDraw')
    private onWorkflowMapDraw(showDrawTool: boolean) {
        if (showDrawTool) {
            this.addDrawInteraction();
        } else {
            this.removeDrawInteraction();
        }
    }

    private validateCoordinates({ W, S, E, N }: { W: number | null, S: number | null, E: number | null, N: number | null }) {
        return (W !== null && S !== null && E !== null && N !== null);
    }

    private mounted() {
        this.onSelectedAreaCoordinatesChanged(this.selectedAreaCoordinates);
    }
}
</script>

<style scoped lang="scss">
.map-control {
    color: white;

    .button-caption {
        font-size: 11px;
        display: block;
        width: auto;
        white-space: nowrap;
        transform: translateX(-50%);
        @media (max-width: $breakpoint-lg) {
            display: none;
        }
    }

    i {
        font-size: 32px;

        @media (max-width: $breakpoint-lg) {
            font-size: 29px;
        }
    }

    &.active {
        color: $red-light;
    }
}
</style>
