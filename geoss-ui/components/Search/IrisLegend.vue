<template>
	<div class="iris-legend" :class="{collapsed: collapsed}" v-show="irisFiltersAvailable">
		<button class="iris-legend__hide-btn" @click="toggleCollapsed()"></button>
		<div>
			<div class="iris-legend__title">{{$t('irisFilters.legend.depth')}} [{{$t('irisFilters.legend.km')}}]:</div>
			<div class="iris-legend__depth">
				<div class="iris-legend__depth-item">0-20</div>
				<div class="iris-legend__depth-item">20-40</div>
				<div class="iris-legend__depth-item">40-60</div>
				<div class="iris-legend__depth-item">60-80</div>
				<div class="iris-legend__depth-item">80-100</div>
			</div>
		</div>
		<div class="d-flex flex--column">
			<div class="iris-legend__title">{{$t('irisFilters.legend.magnitude')}}:</div>
			<div class="iris-legend__magnitude">
				<div class="iris-legend__magnitude-item">9</div>
				<div class="iris-legend__magnitude-item">5</div>
				<div class="iris-legend__magnitude-item">1</div>
			</div>
		</div>
	</div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';

import { IrisFiltersGetters } from '@/stores/iris-filters/iris-filters-getters';

@Component
export default class IrisLegendComponent extends Vue {
	public collapsed = false;

	get irisFiltersAvailable() {
		return this.$store.getters[IrisFiltersGetters.irisFiltersAvailable];
	}

	public toggleCollapsed() {
		this.collapsed = !this.collapsed;
	}
}
</script>

<style lang="scss" scoped>
.iris-legend {
	position: fixed;
	bottom: 0;
	right: 0;
	z-index: 11;
	background: $white-transparent;
	padding: 10px 5px 5px 30px;
	display: flex;
	border-left: 1px solid black;
	border-top: 1px solid black;
	border-top-left-radius: 15px;
	transition: transform 350ms ease-in-out;

	&.collapsed {
		transform: translateX(calc(100% - 30px));
	}

	&__hide-btn {
		position: absolute;
		left: 5px;
		top: calc(50% - 15px);
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

		.iris-legend.collapsed & {
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
		padding-bottom: 5px;
	}

	&__depth-item {
		padding-left: 35px;
		position: relative;
		height: 26px;
		display: flex;
		align-items: center;
		white-space: nowrap;

		&:before {
			content: '';
			position: absolute;
			left: 0;
			top: 0;
			width: 25px;
			height: 25px;
			border: 1px solid black;
			border-top: none;
		}

		&:nth-child(1) {
			height: 27px;
			&:before {
				border-top: 1px solid black;
				background-color: #BABA0B;
			}
		}

		&:nth-child(2):before {
   			background-color: #EBBF02;
		}

		&:nth-child(3):before {
   			background-color: #EB9602;
		}

		&:nth-child(4):before {
   			background-color: #EB5302;
		}

		&:nth-child(5):before {
   			background-color: #D20140;
		}
	}

	&__magnitude {
		width: 160px;
		min-height: 120px;
		position: relative;
		height: 100%;

		&-item {
			position: absolute;
			left: 50%;
			bottom: 0;
			transform: translateX(-50%);
			border-radius: 50%;
			display: flex;
			justify-content: center;
			border: 1px solid black;

			&:nth-child(3) {
				width: 40px;
				height: 40px;
				align-items: center;
				background-color: rgba(#EDA11D, 0.8);
			}

			&:nth-child(2) {
				width: 80px;
				height: 80px;
				align-items: center;
				padding-bottom: 30px;
				background-color: rgba(#EFAC39, 0.8);
			}

			&:nth-child(1) {
				width: 120px;
				height: 120px;
				align-items: flex-start;
				padding-top: 15px;
				background-color: rgba(#F3C371, 0.8);
			}
		}
	}
}
</style>