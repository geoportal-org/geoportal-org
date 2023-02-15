<template>
	<button class="map-control disabled-transparent" :class="{active: drawActive}" :title="$t('mapControls.areaOfInterest')"
			:disabled="disabled" @click="toggleDrawInteraction()">
		<i class="icomoon-select"></i>
	</button>
</template>

<script lang="ts">
import { Component, Vue, Watch, Prop } from 'vue-property-decorator';

import ol from '@/ol';

import MapCoordinatesUtils from '@/services/map/coordinates-utils';
import { MapSource, getMapDrawnLayer } from '@/data/map';
import { MapActions } from '@/stores/map/map-actions';
import { GeneralFiltersActions } from '@/stores/general-filters/general-filters-actions';
import { GeneralFiltersGetters } from '@/stores/general-filters/general-filters-getters';
import { LayerTypes } from '@/interfaces/LayerTypes';
import { MapGetters } from '@/stores/map/map-getters';
import { MapCoordinate } from '@/interfaces/MapCoordinate';
import { SearchGetters } from '@/stores/search/search-getters';

@Component
export default class MapControlDrawComponent extends Vue {
	@Prop({ default: false, type: Boolean}) public disabled!: boolean;
	private draw: ol.interaction.Draw | null = null;
	private drawActive = false;

	get map() {
		return this.$store.getters[MapGetters.map];
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

		if (this.workflow && this.workflowCoordinates) {
			this.$store.dispatch(GeneralFiltersActions.setSelectedAreaCoordinates, this.workflowCoordinates);
		} else {
			this.$store.dispatch(GeneralFiltersActions.setSelectedAreaCoordinates, null);
		}

		this.draw = new ol.interaction.Draw({
			source: MapSource,
			type: 'Circle',
			geometryFunction: ol.interaction.Draw.createBox(),
			maxPoints: 2
		});

		this.draw.on('drawend', () => {
			this.removeDrawInteraction();
			MapSource.once('addfeature', (evt: any) => {
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
				this.$store.dispatch(GeneralFiltersActions.setSelectedAreaCoordinates, {W, S, E, N});

				MapSource.clear();
			});
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
		if(this.drawActive) {
			this.removeDrawInteraction();
		} else {
			this.addDrawInteraction();
		}
	}

	public clearDrawnLayers() {
		this.$store.dispatch(MapActions.removeLayer, 'area-selected');
	}

	public drawLayer({W, S, E, N}: MapCoordinate) {
		const normalizedLongitude = MapCoordinatesUtils.normalizeLongitude(W, E);
		const normalizedW = normalizedLongitude[0];
		const normalizedE = normalizedLongitude[1];

		const longitudes = MapCoordinatesUtils.denormalizeLongitude(W, E);
		W = longitudes[0];
		E = longitudes[1];

		const rectangle = new ol.Feature({
			geometry: new ol.geom.Polygon(MapCoordinatesUtils.coordinatesForDrawing([W, S, E, N]))
		});
		rectangle.getGeometry().transform('EPSG:4326', 'EPSG:3857');

		this.$store.dispatch(MapActions.addLayer, {
			id: 'area-selected',
			coordinate: {W, S, E, N},
			layer: getMapDrawnLayer(rectangle),
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
	private onSelectedAreaCoordinatesChanged(val: MapCoordinate | null) {
		this.clearDrawnLayers();
		if(this.validateCoordinates(val)) {
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

	private validateCoordinates({W, S, E, N}: {W: number | null, S: number | null, E: number | null, N: number | null}) {
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
