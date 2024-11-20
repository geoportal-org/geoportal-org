<template>
	<div>
		<div v-if="links.length" class="custom-downloads__bulk-title bulk-download-title">
			<span>{{$tc('fileDownloadsPopup.bulkDownloadPackageCreation')}}:</span>
		</div>
		<div class="bulk-download">
			<div v-if="links.length">
				<div v-for="(link, index) of links" :key="index" class="download-link">
					<i v-if="FileFormatsIcons.indexOf(link.type) !== -1" class="download-link__icon" :class="`icomoon-${link.type} ${link.scoreClass || 'av-no-info'}`"
							:title="`${link.scoreText || 'No resource availability information'}`"></i>
					<i v-else class="download-link__icon" :class="`icomoon-doc-file  ${link.scoreClass || 'av-no-info'}`"
							:title="`${link.scoreText || 'No resource availability information'}`"></i>
					<div class="download-link__description-wrapper">
						<p class="download-link__title">{{getLinkName(link)}}</p>
						<div class="download-link__description">{{link.desc}}</div>
					</div>
					<input type="checkbox" :id="`popup-result-${index}`" :value="link" class="download-link__input" v-model="selectedLinks" />
					<label :for="`popup-result-${index}`" class="download-link__checkbox">
						<i class="icomoon-checkbox--empty" v-show="selectedLinks.findIndex(item => item.url === link.url) === -1"></i>
						<i class="icomoon-checkbox--filled" v-show="selectedLinks.findIndex(item => item.url === link.url) !== -1"></i>
					</label>
					<button title="Remove" class="download-link__file-remove-btn" @click="removeLink(link.url)"></button>
				</div>
				<div class="bulk-download__buttons text-center">
					<button class="red-btn-contour" @click="clearAll()">{{$tc('fileDownloadsPopup.clearAll')}}</button>
					<button class="green-btn-default" @click="preparePackage()" :disabled="downloadButtonDisabled()">{{$tc('fileDownloadsPopup.preparePackage')}}</button>
				</div>
			</div>
			<div v-else class="bulk-download__no-links">
				<span>{{$tc('fileDownloadsPopup.listIsEmpty')}}</span>
			</div>
		</div>
		<FileDownloadPopup @download-file-id="removeAssignedLinks($event)" />
	</div>
</template>

<script lang="ts">
// @ts-nocheck
import { Component, Vue } from 'nuxt-property-decorator';

import FileDownloadPopup from '@/components/FileDownloadPopup.vue';
import { FileDownloadActions } from '@/store/fileDownload/file-download-actions';
import { FileDownloadGetters } from '@/store/fileDownload/file-download-getters';
import { BulkDownloadActions } from '@/store/bulkDownload/bulk-download-actions';
import { BulkDownloadGetters } from '@/store/bulkDownload/bulk-download-getters';
import LogService from '@/services/log.service';
import { FileFormatsIcons } from '@/data/file-formats-icons';
import PopupCloseService from '@/services/popup-close.service';
import GeossSearchApiService from '@/services/geoss-search.api.service';
import SearchEngineService from '@/services/search-engine.service';
import { DownloadFile, DownloadFileStatus } from '@/interfaces/DownloadFile';
import { BulkDownloadLink } from '@/interfaces/BulkDownloadLink';
import { GeneralGetters } from '@/store/general/general-getters';

@Component({
	components: {
		FileDownloadPopup
	}
})
export default class BulkDownloadPopupComponent extends Vue {
	public FileFormatsIcons = FileFormatsIcons;
	public selectedLinks = [];

	get links() {
		return this.$store.getters[BulkDownloadGetters.links];
	}

	get files() {
		return this.$store.getters[FileDownloadGetters.files];
	}

	get isBulkDownloadEnabled() {
		return this.$store.getters[GeneralGetters.isBulkDownloadEnabled];
	}

	public downloadButtonDisabled() {
		return this.selectedLinks.length === 0;
	}

	public updateSelectedLinks() {
		for (const link of this.links) {
			this.selectedLinks.push(link);
		}
	}

	public removeAssignedLinks(fileId: string) {
		for (const link of this.links) {
			if (link.assignedFileId === fileId) {
				this.removeLink(link.url);
			}
		}
	}

	public getLinkName(link: BulkDownloadLink) {
		if (link.format === 'other' && ((link.name === '') || (link.name === 'all')) && link.url.indexOf('MapServer') > -1) {
			return 'Map Server';
		} else {
			return link.name;
		}
	}

	public preparePackage() {
		const linksArray: any = [];
		const date = new Date();
		const timestamp = date.getTime();

		this.links.forEach((link: any) => {
			if (this.selectedLinks.findIndex((item: any) => item.url === link.url) !== -1) {
				this.$store.dispatch(BulkDownloadActions.assignFileId, {url: link.url, fileId: timestamp});
				linksArray.push({
					title: this.getLinkName(link),
					url: link.url,
				});
			}
		});

		const downloadFile: DownloadFile = {
			format: '',
			download: '',
			type: '',
			id: timestamp,
			status: DownloadFileStatus.inProgress,
			statusObject: null,
			checkStatusUrl: SearchEngineService.getBulkDownloadUrl(),
			checkStatusPayload: GeossSearchApiService.prepareBulkDownloadRequest(linksArray),
			url: '',
			name: `Geoss Download package - ${date.toLocaleDateString('en-GB')} ${date.getHours()}:${date.getMinutes()} - ${linksArray.length} file(s)`,
			progressData: null
		};

		this.$store.dispatch(FileDownloadActions.addFile, downloadFile);

		if (!this.isBulkDownloadEnabled) {
			PopupCloseService.closePopup('bulk-download');
			this.$store.dispatch(FileDownloadActions.openTrigger, true);
		}

		LogService.logElementClick(null, null, null, null, 'Bulk download', null, null, null);
	}

	public clearAll() {
		this.$store.dispatch(BulkDownloadActions.removeAllLinks);
		this.selectedLinks = [];
		if (!this.links.length && !this.files.length) {
			PopupCloseService.closePopup('bulk-download');
		}
	}

	public removeLink(url: any) {
		this.$store.dispatch(BulkDownloadActions.removeLink, url);
		this.selectedLinks = this.selectedLinks.filter((item: any) => item.url !== url);
		if (!this.selectedLinks.length && !this.selectedLinks.length) {
			PopupCloseService.closePopup('bulk-download');
		}
	}

	private mounted() {
		this.updateSelectedLinks();
	}
}
</script>

<style lang="scss" scoped>
.bulk-download {
	padding: 30px 25px;

	&__buttons {
		button {
			margin: 10px 10px 0;
		}
	}

	&__no-links {
		color: $grey;
		text-align: center;
		padding: 15px;
		font-size: 1.3em;
	}

	&-title {
		margin-top: 15px;
	}
}

.download-link {
	display: flex;
	margin-bottom: 30px;
	position: relative;

	&:last-child {
		margin-bottom: 0;
	}

	&__input {
		height: 0;
		opacity: 0;
		width: 0;
	}

	&__checkbox {
		position: relative;

		i {
			background: white;
			cursor: pointer;
			font-size: 20px;
			left: 0px;
			position: absolute;
			top: 9px;

			&::before {
				color: #777;
			}

			&.icomoon-checkbox--filled {
				background: transparent;
				font-size: 22px;
				top: 7px;

				&::before {
					color: black;
				}
			}

		}
	}

	&__icon {
		align-items: center;
		border-radius: 50%;
		color: white;
		display: flex;
		font-size: 25px;
		height: 40px;
		justify-content: center;
		margin-right: 20px;
		min-width: 40px;
		width: 40px;

		&.av-highest {
			background-color: #22b623;
		}

		&.av-high {
			background-color: #75c51f;
		}

		&.av-med {
			background-color: #e7be2e;
		}

		&.av-low {
			background-color: #ff7700;
		}

		&.av-lowest {
			background-color: #ff0000;
		}

		&.av-no-info {
			background-color: #a3a3a3;
		}
	}

	&__description-wrapper {
		margin-right: 30px;
		width: calc(100% - 170px);
		overflow-wrap: break-word;

		@media (max-width: $breakpoint-md) {
			width: calc(100% - 150px);
		}
	}

	&__title {
		color: #0000ee;
		margin-bottom: 5px;
	}

	&__file-remove-btn {
		border: 2px solid $red;
		border-radius: 50%;
		height: 20px;
		position: absolute;
		right: 0px;
		top: 10px;
		width: 20px;

		&:before,
		&:after {
			background: $red;
			content: '';
			height: 2px;
			left: 3px;
			position: absolute;
			top: 7px;
			transform: rotate(45deg);
			width: 10px;
		}

		&:after {
			transform: rotate(-45deg);
		}
	}

	a,
	button {
		display: block;
		margin-bottom: 5px;
	}
}
</style>
