<template>
	<div v-if="crRelationSrc && crRelationSrc.id && crRelationSrc !== ''" class="crRelationSrc">
		<div class="crRelationSrc__top">
			<div class="crRelationSrc__title">
				<button @click="cancel()" :title="$t('popupContent.cancel')"></button>
				<i :class="`icomoon-data-source--${crRelationSrcDataSource}`"></i>
				<span>{{ crRelationSrc.title }}</span>
			</div>
			<div class="d-flex flex--align-center flex--justify-end flex--1">
				<button class="crRelationSrc__action cancel" @click="cancel()">{{$t('popupContent.cancel')}}</button>
				<button class="crRelationSrc__action" @click="accept()">{{$t('popupContent.accept')}}</button>
			</div>
		</div>
	</div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';

import { SearchGetters } from '@/stores/search/search-getters';
import { SearchActions } from '@/stores/search/search-actions';
import { GeneralFiltersGetters } from '@/stores/general-filters/general-filters-getters';
import UtilsService from '@/services/utils.service';
import { AppVueObj } from '@/data/global';
import { DataOrigin, DataSourceGroup } from '@/interfaces/DataSources';
import NotificationService from '@/services/notification.service';
import { PopupActions } from '@/stores/popup/popup-actions';
import to from '@/utils/to';
import GeossSearchApiService from '@/services/geoss-search.api.service';
import ErrorPopup from '@/components/ErrorPopup.vue';
import GeneralPopup from '@/components/GeneralPopup.vue';

@Component
export default class CrRelationsComponent extends Vue {

	get crRelations() {
		return this.$store.getters[SearchGetters.crRelation];
	}

	get crRelationSrc() {
		return this.crRelations.srcEntry;
	}

	get crRelationDest() {
		return this.crRelations.destEntry;
	}

	get crRelationSrcDataSource() {
		return DataSourceGroup[this.crRelationSrc.dataSource];
	}

	public cancel() {
		const crRelations = JSON.parse(JSON.stringify(this.crRelations));
		crRelations.srcEntry = {
			id: null,
			dataSource: null
		};
		crRelations.destEntry = [];
		this.$store.dispatch(SearchActions.setCrRelation, crRelations);
	}

	public async accept() {
		if (this.crRelationDest.length) {
			const destEntries = [];
			for (const entry of this.crRelationDest) {
				destEntries.push({
					id: entry.id,
					dataSource: DataOrigin[entry.dataSource],
					type: `${DataSourceGroup[entry.dataSource] === 'services' ? 'service' : DataSourceGroup[entry.dataSource]}_resource`,
				});
			}
			const relationsData = {
				relationType: this.crRelations.relationType,
				srcEntry: {
					id: this.crRelations.srcEntry.id,
					dataSource: DataOrigin[this.crRelations.srcEntry.dataSource],
					type: `${DataSourceGroup[this.crRelations.srcEntry.dataSource] === 'services' ? 'service' : DataSourceGroup[this.crRelations.srcEntry.dataSource]}_resource`,
				},
				destEntry: destEntries
			};
			const [err, data] = await to(GeossSearchApiService.entryRelationsAPI(false, relationsData));
			if(err) {
				const props = {
					title: this.$t('general.error'),
					subtitle: err
				};
				this.$store.dispatch(PopupActions.openPopup, {contentId: 'error', component: ErrorPopup, props});
			} else if (data) {
				if (data.result === 'success') {
					const props = {
						title: this.$t('popupTitles.entryRelations'),
						subtitle: this.$t('popupContent.entryRelationsRedirecting'),
					};
					window.location = data.redirect;
					this.$store.dispatch(PopupActions.openPopup, {contentId: 'general', component: GeneralPopup, props});
				} else {
					const props = {
						title: this.$t('popupTitles.entryRelations'),
						subtitle: data.comment || data.result
					};
					this.$store.dispatch(PopupActions.openPopup, {contentId: 'error', component: ErrorPopup, props});
				}
			}
		} else {
			NotificationService.show(
				`${this.$t('popupTitles.entryRelations')}`,
				`${this.$t('popupContent.entryRelationsNoDest')}`,
				10000,
				null,
				9999,
				'error'
			);
		}
	}
}
</script>

<style lang="scss" scoped>
.crRelationSrc {
	background-color: $green-transparent;
	padding: 8px 20px;
	margin-top: 5px;

	&__top {
		display: flex;
		justify-content: space-between;
		align-items: center;

		@media (max-width: $breakpoint-sm) {
			flex-wrap: wrap;
		}
	}

	&__action {
		padding: 5px 20px;
		color: white;
		background-color: $green-light;
		border-radius: 15px;
		font-style: italic;

		&.cancel {
			background-color: white;
			color: $green-light;
			margin-right: 15px;
		}
	}

	&__title {
		color: white;
		font-size: 20px;
		display: flex;
		align-items: center;

		@media (max-width: $breakpoint-sm) {
			width: 100%;
			margin-bottom: 20px;
		}

		button {
			position: relative;
			height: 24px;
			width: 24px;
			border-radius: 50%;
			border: 2px solid white;
			margin-right: 10px;
			flex-shrink: 0;

			&:before, &:after {
				content: '';
				position: absolute;
				left: 4px;
				top: 9px;
				height: 2px;
				width: 12px;
				background: white;
				transform: rotate(45deg);
			}

			&:after {
				transform: rotate(-45deg);
			}
		}

		span {
			overflow: hidden;
			text-overflow: ellipsis;
			padding: 0 10px;
			background: none;
			border: none;
		}
	}
}
</style>