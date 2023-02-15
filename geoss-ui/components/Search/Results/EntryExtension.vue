<template>
	<div class="entry-extension">
		<section class="pull-right" v-if="rootDataOrigin === 'geoss_cr'">
			<a class="btn" :href="advancedViewLink()" :title="$t('popupContent.advancedView')">
				{{ $t('popupContent.advancedView') }}
			</a>
		</section>
		<form @submit.prevent="passToModerator()">
			<section>
				<label for="keywords">
					{{ $t('popupContent.keywords') }}:
				</label>
				<vue-tags-input
					name="keywords"
					v-model="tag"
					:tags="tags"
					@tags-changed="newTags => tags = newTags"
					:placeholder="''" />
			</section>
			<section>
				<label for="definition">
					{{ $t('popupContent.definition') }}:
				</label>
				<textarea id="definition" name="definition" v-model="formData.entryExtensionForm.summary"/>
			</section>
			<section>
				<label for="link">
					{{ $t('popupContent.link') }}:
				</label>
				<div v-for="(link, index) of formData.transferOptionsExtensionForms" :key="index" class="link-wrapper">
					<div class="link-input" :class="{'with-protocol-type': showProtocolType}">
						<input id="link" type="text" name="link" v-model="link.endpointForm.url" />
						<div v-if="showProtocolType">
							<span>
								<i :class="`icomoon-editor--file-${link.endpointForm.urlType}`"></i>
								{{ $t(`popupContent.${link.endpointForm.urlType}`) }}
							</span>
							<ul>
								<li 
									v-for="(type, index) of urlTypes" :key="index" 
									v-show="type !== link.endpointForm.urlType"
									@click="link.endpointForm.urlType = type">
									<i :class="`icomoon-editor--file-${type}`"></i>
									{{ $t(`popupContent.${type}`) }}
								</li>
							</ul>
						</div>
					</div>
					<a class="link-add" :title="$t('popupContent.addLinkOption')" @click="addLinkOption()">
						<i class="icomoon-editor--add"></i>
					</a>
					<a class="link-remove" :title="$t('popupContent.removeLinkOption')" @click="removeLinkOption(link)">
						<i class="icomoon-editor--remove"></i>
					</a>
				</div>
			</section>
			<section>
				<label for="editorial-notes">
					{{ $t('popupContent.editorialNotes') }}:
				</label>
				<textarea id="editorial-notes" name="editorial-notes" v-model="formData.comment" />
			</section>
			<section class="pull-right">
				<button class="cta" :disabled="!formEnabled()" :class="{disabled: !formEnabled()}">
					{{ $t('popupContent.passToModerator') }}
				</button>
			</section>
		</form>
	</div>
</template>

<script lang="ts">
import { Component, Vue, Prop, Watch } from 'vue-property-decorator';
import PopupCloseService from '@/services/popup-close.service';
import to from '@/utils/to';
import LogService from '../../../services/log.service';
import { Liferay } from '@/data/global';
import { SearchGetters } from '@/stores/search/search-getters';
import { SearchActions } from '@/stores/search/search-actions';
import VueTagsInput from '@johmun/vue-tags-input';
import { DataSourceGroup } from '@/interfaces/DataSources';
import GeossSearchApiService from '@/services/geoss-search.api.service';
import { PopupActions } from '@/stores/popup/popup-actions';
import ErrorPopup from '@/components/ErrorPopup.vue';
import GeneralPopup from '@/components/GeneralPopup.vue';

@Component({
	components: {
		VueTagsInput
	}
})
export default class EntryExtensionComponent extends Vue {

	@Prop({ default: null, type: String}) private id!: string;
	@Prop({ default: null, type: String}) private title!: string;
	@Prop({ default: null, type: String}) private rootDataOrigin!: string;

	private showProtocolType = false;
	private tag = '';
	private tags = [];
	private dataSourceGroup = DataSourceGroup[this.dataSource] === 'services' ? 'service' : DataSourceGroup[this.dataSource];
	private formData = {
		entryExtensionForm: {
			summary: '',
			keywords: '',
			entryCode: this.id,
			entryTitle: this.title,
			dataSource: this.rootDataOrigin,
			userId: Liferay.ThemeDisplay.getUserId(),
			type: `${this.dataSourceGroup}_resource`
		},
		transferOptionsExtensionForms: [
			{
				protocol: 'information-html',
				endpointForm: {
					url: '',
					urlType: 'html'
				}
			}
		],
		comment: ''
	};

	private urlTypes = [
		'html',
		'pdf',
		'img',
		'file'
	];

	get dataSource() {
		return this.$store.getters[SearchGetters.dataSource];
	}

	get isSignedIn() {
		return Liferay.ThemeDisplay.isSignedIn;
	}

	get getTags() {
		return this.tags;
	}

	public formEnabled() {
		if (
			this.formData.entryExtensionForm.summary !== '' ||
			this.formData.entryExtensionForm.keywords !== '' ||
			this.formData.transferOptionsExtensionForms[0].endpointForm.url !== ''
		) {
			return true;
		} else {
			return false;
		}
	}

	public addLinkOption() {
		this.formData.transferOptionsExtensionForms.push(
			{
				protocol: 'information-html',
				endpointForm: {
					url: '',
					urlType: 'html'
				}
			}
		);
	}

	public removeLinkOption(link) {
		this.formData.transferOptionsExtensionForms = this.formData.transferOptionsExtensionForms.filter(e => {
			return e !== link;
		});
	}

	public advancedViewLink() {
		return GeossSearchApiService.entryExtensionAdvancedViewLink(this.id, this.rootDataOrigin);
	}

	public async passToModerator() {
		LogService.logElementClick(null, null, this.id, null, 'Pass to moderator', null, null, this.title);
		const formData = this.formData;
		for (const link of formData.transferOptionsExtensionForms) {
			link.endpointForm.url = encodeURIComponent(link.endpointForm.url);
		}
		const [err, result] = await to(GeossSearchApiService.entryExtensionPassToModerator(formData));
		PopupCloseService.closePopup('entry-extension');
		if(result && result.result === 'success' && !err) {
			const props = {
				title: this.$t('popupTitles.improveDefinition'),
				subtitle: this.$t('popupContent.improveDefinitionSuccess')
			};
			this.$store.dispatch(PopupActions.openPopup, {contentId: 'general', component: GeneralPopup, props});
			LogService.logElementClick(null, null, this.id, null, 'Entry extension sent successfuly', null, null, this.title);
		} else {
			const resultError = result && result.result === 'error' ? result.commment : null;
			const props = {
				title: this.$t('popupTitles.improveDefinition'),
				subtitle: !resultError ? this.$t('popupContent.improveDefinitionFail') : `${this.$t('popupContent.improveDefinitionFail')}: ${resultError}`
			};
			this.$store.dispatch(PopupActions.openPopup, {contentId: 'error', component: ErrorPopup, props});
			LogService.logElementClick(null, null, this.id, null, 'Failed to send entry extension', null, null, this.title);
		}
	}

	private mounted() {
		LogService.logElementClick(null, null, this.id, null, 'Entry extension start', null, null, this.title);
	}

	@Watch('getTags')
	private onTagsChange() {
		this.formData.entryExtensionForm.keywords = this.tags.map(e => e.text).join();
	}
}
</script>

<style lang="scss" scoped> 
.entry-extension {
	padding: 30px 25px;

	.link {
		&-wrapper {
			display: flex;
			align-items: center;

			&:last-child {
				.link-add {
					display: block;
				}
				.link-remove {
					display: none;
				}
			}

			+ .link-wrapper {
				margin-top: 10px;
			}
		}
		&-input {
			position: relative;
			width: calc(100% - 40px);

			&.with-protocol-type {
				input {
					padding-right: 110px;
				}
			}

			div {
				position: absolute;
				right: 0px;
				top: 50%;
				transform: translateY(-50%);
				font-size: 0.76em;
				padding: 8px 10px;
				text-transform: uppercase;

				&:before {
					content: '';
					width: 1px;
					height: 50%;
					background: $grey-lighter;
					left: 0;
					top: 50%;
					transform: translateY(-50%);
					display: block;
					position: absolute;
				}

				span {
					color: white;
					background: $green;
					border-radius: 15px;
					padding: 5px 15px;
					cursor: pointer;
					display: inline-block;
					width: 80px;
					position: relative;
					padding-left: 35px;

					i {
						left: 18px;
					}
				}

				&:hover {
					z-index: 1;

					span {
						background: $green-dark;
					}

					ul {
						display: block;
					}

					&:after {
						content: '';
						display: block;
						width: 8px;
						height: 8px;
						background: white;
						position: absolute;
						left: 50%;
						transform: translateX(-50%) rotate(45deg);
						bottom: -4px;
						box-shadow: 0 0 5px rgba(0,0,0,0.3);
					}
				}

				i {
					position: absolute;
					left: 13px;
					top: 50%;
					transform: translateY(-50%);

					&.icomoon-editor--file-html {
						font-size: 0.91em;
						transform: translateY(-50%) translateX(1px);
					}
				}

				ul {
					display: none;
					position: absolute;
					left: 50%;
					transform: translateX(-50%);
					background: white;
					width: calc(100% - 20px);
					box-shadow: 0 0 5px rgba(0,0,0,0.15);
					top: 99%;
					padding: 5px;
					color: $green;
					z-index: 1;

					&:after {
						content: '';
						display: block;
						width: 8px;
						height: 8px;
						background: white;
						position: absolute;
						left: 50%;
						transform: translateX(-50%) rotate(45deg);
						top: -4px;
						z-index: 1;
					}

					li {
						padding: 5px;
						cursor: pointer;
						position: relative;
						padding-left: 30px;

						&:hover {
							background: #f3f3f3;
							color: $green-dark;
							position: relative;
							z-index: 2;
						}
					}
				}
			}
		}

		&-add,
		&-remove {
			font-size: 1.8em;
			padding: 4px 0 0 10px;
			width: 40px;
			cursor: pointer;
			color: $green;

			&:hover {
				color: $green-dark;
				text-decoration: none;
			}
		}

		&-add {
			display: none;
		}

		&-remove {
			display: block;
		}
	}

	section {
		margin-bottom: 20px;
		display: flex;
		flex-direction: column;

		&.pull-right {
			flex-direction: row;
			justify-content: flex-end;
			float: none;
		}
	}

	button,
	a.btn {
		background: white;
		border: 1px solid $blue;
		padding: 7px 20px;
		color: $blue;
		font-size: 1.1em;

		&:hover {
			background: $blue;
			color: white;
			text-decoration: none;
		}

		&.cta {
			background: $blue;
			color: white;

			&:hover {
				background: white;
				color: $blue;
			}
		}
	}

	label {
		color: $green;
		margin-bottom: 5px;
	}

	input,
	textarea {
		height: 48px;
		padding: 10px;
		width: 100%;
		border: 1px solid $grey;

		&:focus {
			outline: none;
		}
	}
	
	textarea {
		height: 200px;
	}
}
</style>

<style lang="scss">
.entry-extension {
	.vue-tags-input {
		max-width: 100%;
	}
	.ti-input {
		height: 48px;
	}
	.ti-tag {
		background-color: #f3f3f3;
		color: black;
		padding: 5px 10px;

		.ti-icon-close {
			margin-top: 1px;
			margin-left: 5px;
			font-size: 0.8em;
			&:before {
				content: '✕';
			}
		}
	}
	.ti-deletion-mark {
		background-color: $red;
		color: white;
	}
}
</style>