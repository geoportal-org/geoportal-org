<template>
	<div class="yellow-pages-providers" ref="results-container" v-bar>
		<div>
			<div v-for="provider of results" :key="provider.id" class="provider" :id="`provider-${provider.id}`" :class="{active: activeProviders.indexOf(provider.id) !== -1}">
				<div class="provider__main">
					<div class="provider__left">
						<div class="provider__image">
							<img v-image-preview :src="getImage(provider.image_url)" @error="imageLoadError(provider.image_url)" :alt="provider.title" />
						</div>
							<div class="provider__side-info">
								<CollapseTransition>
									<div v-show="activeProviders.indexOf(provider.id) !== -1">
										<div class="provider__additional-info">
											<label>{{$t('yellowPages.geoAffiliation')}}  </label>
											<div>{{getProviderExtra(provider, 'GEO Affiliation')}}</div>
										</div>
										<div class="provider__additional-info">
											<label>{{$t('yellowPages.dataPolicy')}}  </label>
											<div>{{getProviderExtra(provider, 'Data Policy')}}</div>
										</div>
										<div class="provider__additional-info">
											<label>{{$t('yellowPages.geographicalCoverage')}}  </label>
											<div>{{getProviderExtra(provider, 'Geographical Coverage')}}</div>
										</div>
									</div>
								</CollapseTransition>
							</div>
					</div>

					<div class="provider__right">
						<div class="provider__main-info">
							<div class="provider__title" @click="toggleProvider(provider.id)">{{provider.title}}</div>
							<div class="provider__date" v-if="provider.date">
								<span class="provider__date__label">{{$t('yellowPages.registrationDate')}}:</span> <span> {{provider.date | date('DD-MM-YYYY')}}</span>
							</div>
							<div class="provider__website">
								<a :href="getProviderUrl(provider).url" target="_blank">{{getProviderUrl(provider).urlLabel}}</a>
							</div>
							<div class="provider__description">
								<div :class="{active: activeProviders.indexOf(provider.id) !== -1}">{{provider.description}}</div>
							</div>
							<div class="provider__principles">
								<img v-for="(principle, index) in availablePrinciples" 
									:key="index"
									:alt="principle"
									:class="{disabled: provider.principles.indexOf(principle) === -1}" 
									:src="`${staticPath()}/svg/dmp${index+1}.svg`" 
									:title="$t(`yellowPages.${principle}`)">
							</div>
						</div>
						<CollapseTransition>
							<div v-show="activeProviders.indexOf(provider.id) !== -1">
								<div class="provider__goals-title">{{$t('yellowPages.sustainableDevelopmentGoals')}}:</div>
								<div>
									<div class="provider__goals">
										<img v-for="goal in availableGoalsSDG" 
											:key="goal" 
											:alt="goal"
											:class="{disabled: provider.goalsSDG.indexOf(goal) === -1}" 
											:src="`${staticPath()}/img/sdg/${goal}.png`" 
											:title="$t(`yellowPages.${goal}`)">
									</div>
								</div>
								<div class="provider__goals-title">{{$t('yellowPages.societalBenefitAreas')}}:</div>
								<div>
									<div class="provider__goals-benefits">
										<img v-for="goal in availableGoalsSBA" 
											:key="goal" 
											:alt="goal"
											:class="{disabled: provider.goalsSBA.indexOf(goal) === -1}" 
											:src="`${staticPath()}/svg/sba/${goal}.svg`" 
											:title="$t(`yellowPages.${goal}`)">
									</div>
								</div>
							</div>
						</CollapseTransition>
						<Share class="provider__share-btn" :url="getProviderUrl(provider).url" />
						<!-- <button class="provider__share-btn" :class="{active: providerShareActive === provider.id}" @click="toggleProviderShare(provider.id)">
							<i class="icomoon-share"></i>
						</button>
						<CollapseTransition>
							<div class="provider__share__container" v-show="providerShareActive === provider.id">
								<div>
									<div class="provider__share">
										<Share :url="getProviderUrl(provider).url" />
									</div>
								</div>
							</div>
						</CollapseTransition> -->
					</div>
				</div>
				
			</div>
		</div>
	</div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from 'vue-property-decorator';

import { YellowPagesFiltersGetters } from '@/stores/yellow-pages-filters/yellow-pages-filters-getters';
import { AvailablePrinciples } from '@/data/principles';
import { SDGs } from '@/data/sdg';
import { SBAs } from '@/data/sba';
import { YellowPagesFiltersActions } from '@/stores/yellow-pages-filters/yellow-pages-filters-actions';
import Share from '@/components/Share.vue';

@Component({
	components: {
		Share
	}
})
export default class YellowPagesProvidersComponent extends Vue {
	public availablePrinciples = AvailablePrinciples;

	public availableGoalsSDG = SDGs;

	public availableGoalsSBA = SBAs;

	public activeProviders = [];

	public providerShareActive = null;

	private providers = [];

	get results(): any[] {
		return this.$store.getters[YellowPagesFiltersGetters.results];
	}

	public getProviderUrl(provider) {
		let url = '';
		let urlLabel = '';
		const urlExtra = provider.extras.find(extra => extra.key === 'Institution Web Site URL');
		if(urlExtra && urlExtra.value) {
			url = urlExtra.value;
			if (url.slice(0, 4) !== 'http') {
				url = 'http://' + url;
			}

			urlLabel = url;
			if (urlLabel.slice(0, 7) === 'http://') {
				urlLabel = urlLabel.slice(7);
			}
			if (urlLabel.slice(0, 8) === 'https://') {
				urlLabel = urlLabel.slice(8);
			}
		}
		return {url, urlLabel};
	}

	public getProviderExtra(provider, extraKey: string) {
		const extra = provider.extras.find(extra => extra.key === extraKey);
		if(extra && extra.value) {
			return extra.value;
		}
		return null;
	}

	public toggleProvider(providerId: string) {
		const index = this.activeProviders.indexOf(providerId);
		if(index !== -1) {
			this.activeProviders.splice(index, 1);
		} else {
			this.activeProviders.push(providerId);
		}
		this.$nextTick().then(() => {
			const descriptionEl = this.$el.querySelector(`#provider-${providerId} .provider__description`) as HTMLElement;
			if(index === -1) {
				descriptionEl.style.maxHeight = `${descriptionEl.scrollHeight}px`;
			} else {
				descriptionEl.style.maxHeight = '60px';
			}
		});
	}

	public toggleProviderShare(providerId: string) {
		if(this.providerShareActive === providerId) {
			this.providerShareActive = null;
		} else {
			this.providerShareActive = providerId;
		}
	}

	@Watch('results')
	private onResultsChanged() {
		this.$nextTick().then(() => {
			const resultsContainer = this.$refs['results-container'] as HTMLElement;
			const scrollableContainer = resultsContainer.querySelector('.vb-content') as HTMLElement;
			scrollableContainer.scrollTop = 0;
		});
	}

	private created() {
		this.$store.dispatch(YellowPagesFiltersActions.getResults);
	}
}
</script>

<style lang="scss">
.yellow-pages-providers {
	// background: $white-transparent;
	height: 100%;

	.provider {
		display: flex;
		align-items: stretch;
		flex-wrap: wrap;
		background-color: $white-transparent;
		transition: background-color 250ms ease-in-out;
		position: relative;
		margin-bottom: 5px;

		&:hover,
		&.active {
			background-color: white;
		}

		&.active {
			.provider__image{
				&::before{
					border-color:$grey;
				}
			}
		}

		&:last-child {
			border-bottom: none;
		}

		&__main {
			display: flex;
			flex-wrap: wrap;
			flex: 1 1 auto;
			max-width: 100%;
			flex-direction: row;

			@media(max-width: $breakpoint-sm) {
				max-width: 100%;
				flex-direction:column-reverse;
			}
		}

		&__left {
			width: 30%;
			@media(max-width: $breakpoint-sm) {
				width: 100%;
			}
		}

		&__right {
			width:70%;
			padding:15px 20px;
			@media(max-width: $breakpoint-sm) {
				padding-bottom: 0;
				width: 100%;
			}
		}

		&__image {
			flex: 0 0 225px;
			display: flex;
			flex-direction: column;
			align-items: center;
			justify-content: center;
			padding-right: 20px;
			padding: 15px 20px;
			height: 100%;
			background-color: $white-transparent;
			position:relative;
			max-height: 200px;

			&::before{
				content: '';
				position: absolute;
				top:15px;
				left:20px;
				width: calc(100% - 42px);
				height: calc(100% - 32px);
				border: 1px solid;
				border-color:transparent;
				transition: border-color ease-in-out 250ms 250ms;
			}

			@media(max-width: $breakpoint-xl) {
				flex: 0 0 175px;
			}

			@media(max-width: $breakpoint-md) {
				flex: 0 0 100%;
				padding: 20px 40px 20px 20px;
			}
			@media(max-width: $breakpoint-sm){
				display: none;
			}
			img {
				max-width: 100%;
				max-height: 100%;
				max-width: 63%;
    			max-height: 96px;
			}
		}

		&__main-info {
			flex: 1 1 auto;
			padding-right: 20px;

			@media(max-width: $breakpoint-md) {
				max-width: 100%;
			}
		}

		&__title {
			font-size: 16px;
			margin-bottom: 5px;
			font-weight: bold;
			cursor:pointer;
		}

		&__description {
			line-height: 20px;
			margin-bottom: 20px;
			overflow: hidden;
			transition: max-height 450ms ease-in-out;
			max-height: 60px;
			font-size: 13px;

			@keyframes white-space-animation {
				from {
					white-space: normal;
				}
				to {
					white-space: nowrap;
				}
			}

			div:not(.active) {
				overflow: hidden;
				text-overflow: ellipsis;
				animation: white-space-animation 0s;
				animation-delay: 450ms;
			}
		}

		&__principles {
			img {
				width: 25px;
				margin-right: 5px;
				margin-bottom: 5px;

				&.disabled {
					filter: grayscale(1);
				}
			}

			@media(max-width: $breakpoint-md) {
				margin-bottom: 15px;
			}
		}

		&__goals {
			margin-bottom: 20px;

			&-title {
				margin-top: 25px;
				margin-bottom: 10px;
				font-size: 14px;
				font-weight: bold;
				& + div:last-child>div {
					margin-bottom: 0;

					@media(max-width: $breakpoint-sm) {
						margin-bottom: 20px;
					}
				}
			}

			img {
				width: 54px;
				margin-right: 5px;
				margin-bottom: 5px;

				&.disabled {
					filter: grayscale(1);
				}
			}

			&-benefits {
				img{
					width:40px;
					margin-right: 5px;
					margin-bottom: 5px;

					&.disabled {
						filter: grayscale(1);
					}
				}
			}
		}

		&__side-info {
			padding-left: 20px;

			@media(max-width: $breakpoint-sm) {
				border-left: none;
				padding-left: 20px;
				max-width: none;
			}
		}

		&__date {
			font-size: 13px;
			margin-bottom: 5px;

			&__label{
				font-style: italic;
				color:$grey-darker;
			}
		}

		&__website {
			margin-bottom: 5px;
			white-space: nowrap;
			overflow: hidden;
			text-overflow: ellipsis;
			a {
				font-size: 13px;
				color: $blue-medium;
			}
		}

		&__additional-info {
			margin-bottom: 5px;
			font-size:13px;

			label,div {
				display:inline-block;
			}

			label {
				font-weight: bold;
				margin-bottom: 5px;
				margin-right:10px;
			}
			
		}

		&__more {
			text-align: right;

			button {
				font-size: 17px;
			}
		}

		&__share-btn {
			background: $yellow;
			border-radius: 50%;
			padding: 5px;
			color: white;
			margin-left: 10px;
			min-height: 32px;
			min-width: 32px;
			max-height: 32px;
			max-width: 32px;
			border: 1px solid transparent;
			position: absolute;
			top:15px;
			right:25px;

			&:hover,
			&.active {
				color: $yellow;
				background: white;
				border: 1px solid $yellow;
				text-decoration: none;
			}
		}

		&__share {
			padding-top: 10px;
			text-align: right;

			.social-sharing {
				padding: 10px;
				background: white;
				border:1px solid $grey-dark;
				justify-content: flex-end;
				display: inline-flex;
				.copy-link {
					margin-left:0;
				}
			}
		}

		&__share__container{

			position:absolute;
			top:40px;
			right: 25px;

		}
	}
}
</style>