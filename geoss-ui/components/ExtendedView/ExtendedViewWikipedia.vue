<template>
	<div class="ev-wikipedia">
		<div v-if="result" class="ev-wikipedia__wrapper">
			<div class="ev-wikipedia__image" :class="{'ev-wikipedia__image--default': (getImage(result.logo) !== result.logo)}">
				<img :src="getImage(result.logo)" @error="imageLoadError(result.logo)" :alt="result.title" v-image-preview />
			</div>
			<div class="d-flex flex--column flex--1 padding-right-25">
				<div class="d-flex flex--wrap flex--justify-between flex--align-start flex--no-shrink">
					<div class="ev-wikipedia__text">
						<div class="ev-wikipedia__title">{{ result.title }}</div>
					</div>
					<ViewsAndRatings :result="result" :extendedViewMode="true"/>
				</div>
				<div v-if="result.summary && typeof result.summary === 'string'" class="ev-wikipedia__summary">
					<div v-html-to-text="result.summary"></div>
				</div>
				<div class="d-flex">
					<button @click="showDetails()" class="ev-wikipedia__more">
						<span>{{$t('dabResult.seeOnWikipedia')}}</span>
						<span class="arrow"></span>
					</button>
				</div>
			</div>
		</div>
		<div v-else class="ev-wikipedia__placeholder">
			<div class="ev-wikipedia__placeholder-text">
				<p>No additional</p>
				<p>information found</p>
			</div>
		</div>
	</div>
</template>

<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator';
import UtilsService from '@/services/utils.service';
import ViewsAndRatings from '@/components/ViewsAndRatings.vue';

@Component({
	components: {
		ViewsAndRatings
	}
})
export default class ExtendedViewWikipediaComponent extends Vue {
	@Prop({ default: null, type: Object}) public result!: any;

	public showDetails() {
		const data = UtilsService.getArrayByString(
				this.result,
				'gmd:distributionInfo.gmd:MD_Distribution.gmd:transferOptions.gmd:MD_DigitalTransferOptions.gmd:onLine'
			);
		const url = UtilsService.getPropByString(data[0], 'gmd:CI_OnlineResource.gmd:linkage.gmd:URL');
		window.open(url, '_blank');
	}
}
</script>

<style lang="scss" scoped>
	.ev-wikipedia {
		background-color: rgba(255, 255, 255, 0.75);
		height: 100%;

		&__placeholder {
			background: url('#{$staticPath}/svg/wikipedia-placeholder.svg') center center / cover no-repeat;
			display: flex;
			height: 100%;
		}

		&__placeholder-text {
			margin: auto;
			padding-top: 15px;

			p {
				color: #888;
				font-size: 20px;
				margin: auto;
				text-align: center;
			}
		}

		&__wrapper {
			background-color: white;
			display: flex;

			> div {
				background: rgba(255, 255, 255, 0.9);
				background-clip: content-box;
				display: flex;
				height: 330px;
				padding: 25px 10px;

				@media (max-width: $breakpoint-sm) {
					height: auto;
					min-height: 340px;
					max-height: 400px;
				}
			}
		}

		&__image {
			width: 33.333%;
			height: 100%;
			background: $grey;
			display: flex;
			flex-direction: column;
			justify-content: center;
			align-items: center;
			flex: 0 0 auto;

			@media (max-width: $breakpoint-sm) {
				width: 100%;
			}

			&--default {
				padding: 30px;

				@media (max-width: $breakpoint-sm) {
					height: 150px;
					padding: 10px;
				}
			}

			img {
				width: auto;
				height: auto;
				display: block;
				max-width: 100%;
				max-height: 100%;
			}
		}

		&__text {
			flex: 1 0 0;
			display: flex;
			flex-direction: column;
			min-height: 0;
			margin: 5px 15px 5px 0;
		}

		&__title {
			color: $blue;
			line-height: 20px;
			font-size: 18px;
			font-weight: 700;
			word-break: break-word;
			max-height: 42px;
			overflow: hidden;
			text-overflow: ellipsis;
			-webkit-line-clamp: 2;
			display: -webkit-box;
			-webkit-box-orient: vertical;

			.is-parent-ref & {
				color: white;
			}
		}

		&__summary {
			font-size: 14px;
			line-height: 14px;
			color: black;
			flex: 1 1 auto;
			overflow: hidden;
			margin-top: 10px;
			position: relative;
			width: 100%;

			@media (max-width: $breakpoint-sm) {
				word-break: break-word;
			}

			&:after {
				position: absolute;
				bottom: 0;
				left: 0;
				content: '';
				background: linear-gradient(rgba(white, 0.001), rgba(white, 0.75) 50%, white 100%);
				z-index: 1;
				height: 30px;
				width: 100%;
			}
		}

		&__more {
			color: $blue;
			font-weight: 700;
			margin: 5px 5px 5px auto;
			display: flex;
			align-items: center;

			&:hover {
				span {
					text-decoration: underline;
				}
			}

			.arrow {
				position: relative;
				width: 15px;
				height: 15px;
				border: 1px solid $blue;
				border-radius: 50%;
				display: inline-block;
				margin-left: 3px;

				.is-parent-ref & {
					border-color: white;
				}

				&:before,
				&:after {
					content: '';
					width: 5px;
					height: 2px;
					background: $blue;
					position: absolute;
					left: 4px;
					top: 4px;
					transform: rotate(45deg);

					.is-parent-ref & {
						background: white;
					}
				}

				&:after {
					top: 7px;
					transform: rotate(-45deg);
				}
			}
		}
	}
</style>