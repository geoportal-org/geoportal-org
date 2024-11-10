<template>
	<div v-if="mapData" class="dashboard-map">
		<div :id="`map-${uuid}`" />
		<p class="dashboard-map__title">{{ mapData.outputName }}</p>
		<img v-if="mapData.legend && mapData.legend !== ''" class="dashboard-map__legend" :src="mapData.legend" />
	</div>
</template>

<script lang="ts">
// @ts-nocheck
import { Prop, Component, Vue } from 'nuxt-property-decorator';
import LayerTilesService from '@/services/map/layer-tiles.service';
import LayersUtils from '@/services/map/layer-utils';
import MapCoordinatesUtils from '@/services/map/coordinates-utils';
import { AppVueObj } from '~/data/global'
import { v4 as uuidv4 } from "uuid";

interface MapData {
	id: string;
	legend: string;
	name: string;
	outputName: string;
	protocol: string;
	runId: string;
	url: string;
	bbox: object;
}

@Component({})
export default class DashboardChartComponent extends Vue {
	@Prop({default: null, type: Object}) public mapData!: MapData;

	get uuid() {
		return uuidv4();
	}

	private map: any;

	private async mounted() {
		if (!this.mapData) {
			return;
		}
		await this.$nextTick();

		const {outputName, url, name, protocol, id, runId, legend} = this.mapData;
		let version = '1.1.1';
		if(protocol) {
			if(protocol.indexOf('WebMapService') > -1) {
				const match = /((\d)+\.(\d)+(\.(\d)+)?)/g.exec(protocol);
				if (match) {
					version = match[0];
				}
			}
		}

		const layerUrl = `${url}VERSION=${version}&LAYERS=${name}&TILED=true&`;
		const layer = LayersUtils.createWMS(name, layerUrl);
		const currMap = 'osm';
		const layers = [
			layer,
			LayerTilesService[currMap].getLayerTile()
		];

		this.map = new AppVueObj.ol.Map({
			layers,
			target: `map-${this.uuid}`,
			view: new AppVueObj.ol.View({
				center: [0, 0],
				zoom: 3
			}),
			controls: []
		});

		if (this.mapData.bbox) {
			const coordinates = {
				W: this.mapData.bbox[0] * 1,
				S: this.mapData.bbox[1] * 1,
				E: this.mapData.bbox[2] * 1,
				N: this.mapData.bbox[3] * 1
			};
			const {W, E} = coordinates;
			let {S, N} = coordinates;
			const longitudes = MapCoordinatesUtils.denormalizeLongitude(W, E);
			let denormalizedW: number = longitudes[0];
			let denormalizedE: number = longitudes[1];

			const minSize = 0.02;
			if (denormalizedE - denormalizedW < minSize || N - S < minSize) {
				denormalizedW = denormalizedW - minSize / 2;
				denormalizedE = denormalizedE + minSize / 2;
				S = S - minSize / 2;
				N = N + minSize / 2;
			}

			let extent: [number, number, number, number] = [denormalizedW, S, denormalizedE, N];
			extent = AppVueObj.ol.extent.applyTransform(extent, AppVueObj.ol.proj.getTransform('EPSG:4326', 'EPSG:3857'));
			this.map.getView().fit(extent);
		}
	}
}
</script>

<style lang="scss" scoped>
.dashboard-map {
	position: relative;
	width: 100%;
	height: 100%;
	min-height: 400px;

	> div {
		width: 100%;
		height: calc(100% - 32px);
		position: absolute;
		left: 0;
		top: 0;
	}

	> p {
		position: absolute;
		bottom: 0;
		width: 100%;
		text-overflow: ellipsis;
		overflow: hidden;
		white-space: nowrap;
	}

	&__title {
		text-align: center;
		margin: 10px 0;
		font-size: 0.85em;
		color: $grey-dark;
	}

	&__legend {
		position: absolute;
		bottom: 32px;
		right: 0;
		transform: scale(0.5) translate(50%, 50%);
	}
}
</style>
