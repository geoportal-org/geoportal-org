<template>
	<div class="layer-legend" :class="{collapsed: collapsed}" v-if="activeLayerLegend && supportedLegendTypes.includes(activeLayerLegend.legend.type)">
		<button class="layer-legend__hide-btn" @click="toggleCollapsed()"></button>
		<div>
			<div class="layer-legend__title">{{$t('map.legend')}}:</div>
			<div class="layer-legend__content">
				<div v-if="activeLayerLegend.legend.type == supportedLegendTypes[0]">
					<a :href="activeLayerLegend.legend.data" target="_blank" title="Open legend in original size">
						<img :src="activeLayerLegend.legend.data" :alt="activeLayerLegend.title" />
					</a>
				</div>
				<div v-else-if="activeLayerLegend.legend.type == supportedLegendTypes[1]">
					<div class="layer-legend__color-scale single-color">
						<div class='value'>{{activeLayerLegend.legend.data.value}} {{activeLayerLegend.legend.data.unit}}</div>
					</div>
				</div>
				<div v-else-if="activeLayerLegend.legend.type == supportedLegendTypes[2]" class="layer-legend__two-value-scale">
					<div class="layer-legend__color-scale">
						<div class='max'>{{activeLayerLegend.legend.data.max}} {{activeLayerLegend.legend.data.unit}}</div>
						<div class='min'>{{activeLayerLegend.legend.data.min}} {{activeLayerLegend.legend.data.unit}}</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from 'vue-property-decorator';

import { MapGetters } from '@/stores/map/map-getters';
import { LayerData } from '@/interfaces/LayerData';

@Component
export default class LayerLegendComponent extends Vue {
	public collapsed = false;

	get activeLayerLegend(): LayerData {
		return this.$store.getters[MapGetters.activeLayerLegend];
	}

	get supportedLegendTypes() {
		return this.$store.getters[MapGetters.supportedLegendTypes];
	}

	public toggleCollapsed() {
		this.collapsed = !this.collapsed;
	}

	@Watch('activeLayerLegend')
	private onActiveLayerLegend() {
		if(this.activeLayerLegend) {
			this.collapsed = false;
		}
	}
}
</script>

<style lang="scss" scoped>
.layer-legend {
	position: fixed;
	bottom: 0;
	right: 0;
	z-index: 3;
	background: $white-transparent;
	padding: 10px 0 30px 30px;
	display: flex;
	border-left: 1px solid black;
	border-top: 1px solid black;
	border-top-left-radius: 15px;
	transition: transform 350ms ease-in-out;
	max-width: 35%;
    max-height: 50%;

	&.collapsed {
		transform: translateX(calc(100% - 30px));
	}

	&__hide-btn {
		position: absolute;
		left: 5px;
		top: auto;
		height: 30px;
		width: 20px;

		&:before,
		&:after {
			content: "";
			background: $blue;
			width: 20px;
			height: 3px;
			border-radius: 5px;
			position: absolute;
			top: 7px;
			left: 0;
       		transform: rotate(40deg);
		}

		&:after {
			top: 18px;
			transform: rotate(-40deg);
		}

		.layer-legend.collapsed & {
			&:before {
				transform: rotate(-40deg);
			}
			&:after {
				transform: rotate(40deg);
			}
		}
	}

	&__title {
		text-align: center;
    	font-weight: 700;
		width: 100%;
		padding: 6px 20px 10px 0;
	}

	&__two-value-scale {
		display: flex;
		padding-right: 30px;
	}

	&__color-scale {
		background-image: linear-gradient(#00ff00, yellow, red);
		font-size: 12px;
		height: 200px;
		position: relative;
		width: 50px;
		&.single-color {
			align-items: center;
			background-color: #00ff00;
			background-image: none;
			display: flex;
			height: 50px;
			justify-content: center;
		}
		.min {
			bottom: 5px;
			position: absolute;
			right: 5px;
		}
		.max {
			position: absolute;
			right: 5px;
			top: 5px;
		}
	}

	&__content {
		height: 100%;
		img {
			max-height: 100%;
			width: 100%;
		}
	}
}
</style>