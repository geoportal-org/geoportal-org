<template>
	<div v-if="files && files.length">
		<div v-if="isBulkDownloadEnabled" class="custom-downloads__bulk-title">
			<span>{{$tc('popupTitles.downloadList')}}:</span>
		</div>
		<div class="custom-downloads">
			<div class="custom-downloads__files">
				<table>
					<tr>
						<th>{{$tc('fileDownloadsPopup.fileName')}}</th>
						<th>{{$tc('fileDownloadsPopup.status')}}</th>
						<th>{{$tc('fileDownloadsPopup.action')}}</th>
					</tr>
					<tr class="custom-downloads__file" v-for="file in files" :key="file.id">
						<td class="custom-downloads__file-name">
							<div>{{file.name}}</div>
							<small v-if="shouldExludeFilesToContinue(file) && prepareExceptionsFilesNames(file) !== ''" class="custom-downloads__file-statusDescription">{{$tc('fileDownloadsPopup.files')}} <b>{{prepareExceptionsFilesNames(file)}}</b> {{$tc('fileDownloadsPopup.areCurrentlyUnavailableYouCan')}} <a @click="downloadPackageWithExceptions(file)">{{$tc('fileDownloadsPopup.downloadPackageWithoutThisFiles')}}</a> {{$tc('fileDownloadsPopup.or')}} <a @click="retryPackage(file.id)">{{$tc('fileDownloadsPopup.retry')}}</a> {{$tc('fileDownloadsPopup.packagePreparation')}}</small>
							<small v-else-if="prepareExceptionsFilesNames(file) !== ''" class="custom-downloads__file-statusDescription">{{$tc('fileDownloadsPopup.files')}} <b>{{prepareExceptionsFilesNames(file)}}</b> {{$tc('fileDownloadsPopup.areCurrentlyUnavailableYouCan')}} <a @click="removeFile(file.id)">{{$tc('fileDownloadsPopup.abort')}}</a> {{$tc('fileDownloadsPopup.or')}} <a @click="retryPackage(file.id)">{{$tc('fileDownloadsPopup.retry')}}</a> {{$tc('fileDownloadsPopup.packagePreparation')}}</small>
						</td>
						<td class="custom-downloads__file-status">{{file.status}}</td>
						<td class="custom-downloads__file-download-btn" v-if="file.status === statuses.ready">
							<a v-if="file.download !== ''"
								class="green-btn-default"
								target="_blank"
								:download="file.download"
								:href="file.url"
								@click="downloadFileId(file.id)">
								{{$tc('fileDownloadsPopup.download')}}
							</a>
							<a v-else
								class="green-btn-default"
								target="_blank"
								download
								:href="file.url"
								@click="downloadFileId(file.id)">
								{{$tc('fileDownloadsPopup.download')}}>
							</a>
							<button title="Remove" class="custom-downloads__file-remove-btn" @click="removeFile(file.id)"></button>
						</td>
						<td class="custom-downloads__file-cancel-btn" v-else>
							<button class="red-btn-default" @click="removeFile(file.id)">{{$tc('fileDownloadsPopup.cancel')}}</button>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</template>

<script lang="ts">
import { Component, Vue, Emit } from 'nuxt-property-decorator';
import { DownloadFileStatus } from '@/interfaces/DownloadFile';
import { FileDownloadGetters } from '@/store/fileDownload/file-download-getters';
import { FileDownloadActions } from '@/store/fileDownload/file-download-actions';
import PopupCloseService from '@/services/popup-close.service';
import { GeneralGetters } from '@/store/general/general-getters';
import { BulkDownloadGetters } from '@/store/bulkDownload/bulk-download-getters';
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

	public getSavedFileData(file: any) {
		let downloadFile = null;
		let dataInputs = null;
		downloadFile = JSON.parse(sessionStorage.getItem('fileId_' + file.id) as any);
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

	public getExceptionsDetails(file: any) {
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
					if (this.links.filter((link: any) => link.url === exceptionUrl.length)) {
						const exceptionName = this.links.filter((link: any) => link.url === exceptionUrl)[0].name;
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

	public shouldExludeFilesToContinue(file: any) {
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

	public prepareExceptionsFilesNames(file: any) {
		let exceptionFiles = '';
		const {exceptionFileUrls, exceptionFileNames} = this.getExceptionsDetails(file);

		exceptionFiles = `${exceptionFileNames.join(', ')}`;
		return exceptionFiles;
	}

	public downloadPackageWithExceptions(file: any) {
		const linksArray = [];
		const date = new Date();
		const timestamp = date.getTime();
		const {downloadFile, dataInputs}: any = this.getSavedFileData(file);
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

	public retryPackage(fileId: any) {
		const downloadFile = JSON.parse(sessionStorage.getItem('fileId_' + fileId) as any);
		if (downloadFile) {
			this.removeFile(fileId);
			this.$store.dispatch(FileDownloadActions.addFile, downloadFile);
		}
	}

	@Emit()
	public downloadFileId(fileId: string) {
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
