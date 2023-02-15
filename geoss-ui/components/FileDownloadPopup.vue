<template>
	<div v-if="files && files.length">
		<div v-if="isBulkDownloadEnabled" class="custom-downloads__bulk-title">
			<span>{{$t('fileDownloadsPopup.packagesList')}}:</span>
		</div>
		<div class="custom-downloads">
			<div class="custom-downloads__files">
				<table>
					<tr>
						<th>{{$t('fileDownloadsPopup.fileName')}}</th>
						<th>{{$t('fileDownloadsPopup.status')}}</th>
						<th>{{$t('fileDownloadsPopup.action')}}</th>
					</tr>
					<tr class="custom-downloads__file" v-for="file in files" :key="file.id">
						<td class="custom-downloads__file-name">
							<div>{{file.name}}</div>
							<small v-if="shouldExludeFilesToContinue(file) && prepareExceptionsFilesNames(file) !== ''" class="custom-downloads__file-statusDescription">{{$t('fileDownloadsPopup.files')}} <b>{{prepareExceptionsFilesNames(file)}}</b> {{$t('fileDownloadsPopup.areCurrentlyUnavailableYouCan')}} <a @click="downloadPackageWithExceptions(file)">{{$t('fileDownloadsPopup.downloadPackageWithoutThisFiles')}}</a> {{$t('fileDownloadsPopup.or')}} <a @click="retryPackage(file.id)">{{$t('fileDownloadsPopup.retry')}}</a> {{$t('fileDownloadsPopup.packagePreparation')}}</small>
							<small v-else-if="prepareExceptionsFilesNames(file) !== ''" class="custom-downloads__file-statusDescription">{{$t('fileDownloadsPopup.files')}} <b>{{prepareExceptionsFilesNames(file)}}</b> {{$t('fileDownloadsPopup.areCurrentlyUnavailableYouCan')}} <a @click="removeFile(file.id)">{{$t('fileDownloadsPopup.abort')}}</a> {{$t('fileDownloadsPopup.or')}} <a @click="retryPackage(file.id)">{{$t('fileDownloadsPopup.retry')}}</a> {{$t('fileDownloadsPopup.packagePreparation')}}</small>
						</td>
						<td class="custom-downloads__file-status">{{file.status}}</td>
						<td class="custom-downloads__file-download-btn" v-if="file.status === statuses.ready">
							<a v-if="file.download !== ''"
								class="green-btn-default"
								target="_blank"
								:download="file.download"
								:href="file.url"
								@click="downloadFileId(file.id)">
								{{$t('fileDownloadsPopup.download')}}
							</a>
							<a v-else
								class="green-btn-default"
								target="_blank" 
								download
								:href="file.url"
								@click="downloadFileId(file.id)">
								{{$t('fileDownloadsPopup.download')}}>
							</a>
							<button title="Remove" class="custom-downloads__file-remove-btn" @click="removeFile(file.id)"></button>
						</td>
						<td class="custom-downloads__file-cancel-btn" v-else>
							<button class="red-btn-default" @click="removeFile(file.id)">{{$t('fileDownloadsPopup.cancel')}}</button>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</template>

<script lang="ts">
import { Component, Vue, Emit } from 'vue-property-decorator';

import { DownloadFileStatus } from '@/interfaces/DownloadFile';
import { FileDownloadGetters } from '@/stores/file-download/file-download-getters';
import { FileDownloadActions } from '@/stores/file-download/file-download-actions';
import { PopupGetters } from '@/stores/popup/popup-getters';
import PopupCloseService from '@/services/popup-close.service';
import { DownloadFile } from '../interfaces/DownloadFile';
import { GeneralGetters } from '@/stores/general/general-getters';
import { BulkDownloadGetters } from '@/stores/bulk-download/bulk-download-getters';
import { parseXMLToJSON } from '@/services/general.api.service';
import GeossSearchApiService from '@/services/geoss-search.api.service';

@Component
export default class FileDownloadPopupComponent extends Vue {
	public statuses = DownloadFileStatus;

	get links() {
		return this.$store.getters[BulkDownloadGetters.links];
	}

	get files() {
		return this.$store.getters[FileDownloadGetters.files];
	}

	get isBulkDownloadEnabled() {
		return this.$store.getters[GeneralGetters.isBulkDownloadEnabled];
	}

	public removeFile(fileId: number) {
		this.$store.dispatch(FileDownloadActions.removeFile, fileId);
		if(!this.files.length) {
			PopupCloseService.closePopup('custom-downloads');
		}
	}

	public getSavedFileData(file) {
		let downloadFile = null;
		let dataInputs = null;
		downloadFile = JSON.parse(sessionStorage.getItem('fileId_' + file.id));
		if (downloadFile && downloadFile.checkStatusPayload) {
			const jsonData = JSON.parse(parseXMLToJSON(downloadFile.checkStatusPayload));
			dataInputs = jsonData['wps:Execute']['wps:DataInputs']['wps:Input'];
			if (!Array.isArray(dataInputs)) {
				dataInputs = [dataInputs];
			}
		}
		return {
			downloadFile,
			dataInputs
		};
	}

	public getExceptionsDetails(file) {
		const exceptionFileUrls = [];
		const exceptionFileNames = [];
		if (this.links.length && file && file.statusObject) {
			let exceptionReports = file.statusObject['ows:ExceptionReport']['ows:Exception'];
			if (!Array.isArray(exceptionReports)) {
				exceptionReports = [exceptionReports];
			}
			for (const exception of exceptionReports) {
				const exceptionUrl = exception._attributes.locator;
				if (exceptionUrl !== 'null') {
					exceptionFileUrls.push(exceptionUrl);
					if (this.links.filter(link => link.url === exceptionUrl.length)) {
						const exceptionName = this.links.filter(link => link.url === exceptionUrl)[0].name;
						exceptionFileNames.push(exceptionName);
					}
				}
			}
		}
		return {
			exceptionFileUrls,
			exceptionFileNames
		};
	}

	public shouldExludeFilesToContinue(file) {
		const {downloadFile, dataInputs} = this.getSavedFileData(file);
		const {exceptionFileUrls, exceptionFileNames} = this.getExceptionsDetails(file);
		if (exceptionFileUrls.length) {
			if (dataInputs && exceptionFileUrls) {
				if (dataInputs.length === exceptionFileUrls.length) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public prepareExceptionsFilesNames(file) {
		let exceptionFiles = '';
		const {exceptionFileUrls, exceptionFileNames} = this.getExceptionsDetails(file);

		exceptionFiles = `${exceptionFileNames.join(', ')}`;
		return exceptionFiles;
	}

	public downloadPackageWithExceptions(file) {
		const linksArray = [];
		const date = new Date();
		const timestamp = date.getTime();
		const {downloadFile, dataInputs} = this.getSavedFileData(file);
		const {exceptionFileUrls, exceptionFileNames} = this.getExceptionsDetails(file);

		for (const link of dataInputs) {
			const title = link['ows:Title'];
			const url = link['wps:Reference']._attributes['xlink:href'];
			if (!exceptionFileUrls.includes(url)) {
				linksArray.push({
					title,
					url
				});
			}
		}

		downloadFile.checkStatusPayload = GeossSearchApiService.prepareBulkDownloadRequest(linksArray);
		downloadFile.id = timestamp;
		downloadFile.name = `Geoss Download package - ${date.toLocaleDateString('en-GB')} ${date.getHours()}:${date.getMinutes()} - ${linksArray.length} file(s)`;

		this.removeFile(file.id);
		this.$store.dispatch(FileDownloadActions.addFile, downloadFile);
	}

	public retryPackage(fileId) {
		const downloadFile = JSON.parse(sessionStorage.getItem('fileId_' + fileId));
		if (downloadFile) {
			this.removeFile(fileId);
			this.$store.dispatch(FileDownloadActions.addFile, downloadFile);
		}
	}

	@Emit()
	private downloadFileId(fileId: string) {
		return fileId;
	}
}
</script>

<style lang="scss">
.custom-downloads {
	padding: 0px 25px 30px;

	&__bulk-title {
		width: 100%;
		color: black;
		padding: 10px 25px;
		font-size: 22px;
		position: relative;
	}

	&__files {
		table {
			width: 100%;
		}

		tr {
			border-top: 1px solid #ddd;
			border-bottom: 1px solid #ddd;
		}
		
		td, th {
			padding: 15px;
		}
	}

	&__file {
		&-name {
			white-space: pre-wrap;
		}
		&-statusDescription {
			display: block;
			margin-top: 10px;
			line-height: 1.33em;
			font-size: 0.85em;
			color: $red;

			a {
				cursor: pointer;
				text-decoration: underline;
				&:hover {
					color: $red-dark;
				}
			}
		}
		&-status,
		&-download-btn,
		&-cancel-btn {
			text-align: center;
			vertical-align: middle;
		}

		&-status {
			white-space: nowrap;
		}

		&-download-btn {
			position: relative;
			padding: 15px 30px !important;
			a {
				text-decoration: none !important;
			}
		}

		&-remove-btn {
			position: absolute;
			right: 0px;
			width: 20px;
			height: 20px;
			border-radius: 50%;
			border: 1px solid #aaa;

			&:before,
			&:after {
				position: absolute;
				content: '';
				left: 0;
				width: 19px;
				height: 1px;
				background: #aaa;
				top: 8px;
				transform: rotate(45deg);
			}

			&:after {
				transform: rotate(-45deg);
			}
		}
	}
}
</style>