<template>
	<div class="extension-remove">
		<h1>{{ $t('popupContent.userContributedAreYouSure') }}</h1>
		<UserContributionRemoveElement :model="model" :data="metadata" :extensionId="extension.entryExtensionId" :arrayIndex="arrayIndex" />
		<div v-if="model != 'comment'" class="extension-remove__reason">
			<label for="remove-comment">{{ $t('popupContent.userContributedCommentOnYourDecision') }}:</label>
			<textarea v-model="comment" name="comment" id="remove-comment" />
		</div>
		<div class="popup-default__actions">
			<button class="blue-btn-default" @click="cancel('cancel')">{{ $t('popupContent.userContributedCancel') }}</button>
			<button class="green-btn-default" @click="submit()">{{ $t('popupContent.userContributedDelete') }}</button>
		</div>
	</div>
</template>

<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator';
import PopupCloseService from '@/services/popup-close.service';
import { PopupActions } from '@/stores/popup/popup-actions';
import DabResultMetadata from './DabResultMetadata.vue';
import UserContributionRemoveElement from './UserContributionRemoveElement.vue';
import to from '@/utils/to';
import GeossSearchApiService from '@/services/geoss-search.api.service';
import ErrorPopup from '@/components/ErrorPopup.vue';
import GeneralPopup from '@/components/GeneralPopup.vue';
import { SearchActions } from '@/stores/search/search-actions';

@Component({
	components: {
		UserContributionRemoveElement
	}
})

export default class ExtensionRemovePopupComponent extends Vue {
	@Prop() public actions!: any;
	@Prop({type: String}) public title!: string;
	@Prop(Object) private metadata!: any;
	@Prop(Object) private extension!: any;
	@Prop({type: String}) private model!: string;
	@Prop(Number) private arrayIndex!: number;

	private comment = '';

	public cancel(event) {
		PopupCloseService.closePopup('extension-remove', event);
	}

	public async submit() {
		let err;
		let data;
		if (this.model === 'summary' || this.model === 'keywords') {
			const extensionData = {
				code: this.metadata.data.userContributions.code,
				dataSourceType: this.metadata.data.userContributions.dataSourceType,
				entryExtensionId: this.extension.entryExtensionId,
				deleteField: (this.model === 'summary' ? 'description' : this.model),
				comment: this.comment
			};
			[err, data] = await to(GeossSearchApiService.entryExtensionRemove('REMOVE_ENTRY_EXTENSION_SPECIFIC_DATA', extensionData));
		} else if (this.model === 'transferOptions') {
			const extensionData = {
				...this.extension[this.model][this.arrayIndex],
				...{
					comment: this.comment
				}
			};
			[err, data] = await to(GeossSearchApiService.entryExtensionRemove('REMOVE_TRANSFER_OPTIONS_EXTENSION_SPECIFIC_DATA', extensionData));
		} else if (this.model === 'comment') {
			[, data] = await to(GeossSearchApiService.rateResource(
				this.metadata.data.title,
				this.metadata.data.id,
				this.extension.score,
				'',
				this.metadata.data.userContributions.dataSourceType
			));
		}
		PopupCloseService.closePopup('extension-remove');
		if(err || (data && data.result === 'error')) {
			const props = {
				title: this.$t('general.error'),
				subtitle: err || data.comment,
				actions: [{
					event: 'extension-remove-error',
					label: 'OK'
				}]
			};
			this.$store.dispatch(PopupActions.openPopup, {contentId: 'error', component: ErrorPopup, props});
		} else if (data && data.result !== 'error') {
			if (this.model === 'comment') {
				this.$store.dispatch(SearchActions.updateDabResultRating, {id: this.metadata.data.id, rating: data});
			}
			const props = {
				title: this.$t('popupContent.userContributedRemoveEntryExtension'),
				subtitle: this.$t('popupContent.userContributedRemoveEntryExtensionSuccess'),
				actions: [{
					event: {
						id: 'extension-remove-success',
						data: {
							model: this.model,
							target: this.extension,
							arrayIndex: this.arrayIndex
						}
					},
					label: 'OK'
				}]
			};
			this.$store.dispatch(PopupActions.openPopup, {contentId: 'general', component: GeneralPopup, props});
		}
	}
}

</script>

<style lang="scss" scoped> 
.extension-remove {
	padding: 30px 25px;

	&__reason {
		margin: 30px 0;

		label {
			display: block;
			margin-bottom: 10px;
		}

		textarea {
			width: 100%;
			height: 100px;
			padding: 10px;
		}
	}
}
</style>