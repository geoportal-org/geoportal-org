<template>
    <div class="custom-download">
        <div class="custom-download__title">{{ $tc('customDownloadOptionsPopup.title') }}:</div>
        <div class="custom-download__params">
            <div class="custom-download__param">
                <label :title="$tc('customDownloadOptionsPopup.outputDownloadFormat')" class="custom-download__param-title">{{ $tc('customDownloadOptionsPopup.format') }}:</label>
                <CustomSelect
                    class="custom-download__param-select"
                    v-model="format"
                    :clearable="false"
                    :options="options.formatOptions"
                    :placeholder="$tc('customDownloadOptionsPopup.format')" />
            </div>
            <div class="custom-download__param">
                <label :title="$tc('customDownloadOptionsPopup.scaleFactor')" class="custom-download__param-title">{{ $tc('customDownloadOptionsPopup.scaleFactor') }}:</label>
                <div class="custom-download__param-sub">
                    <input class="custom-download__param-input" type="range" min="0.001" max="1" step="0.001" v-model="scaleFactor" />
                    <input class="custom-download__param-input" type="number" min="0.001" max="1" step="0.001" v-model="scaleFactor" />
                </div>
            </div>
            <div class="custom-download__param">
                <label :title="$tc('customDownloadOptionsPopup.rangeSubset')" class="custom-download__param-title">{{ $tc('customDownloadOptionsPopup.rangeSubset') }}:</label>
                <input class="custom-download__param-input" type="text" v-model="rangeSubset" readonly />
                <!-- Only RED_BAND actually works -->
                <!-- <CustomSelect
                    class="custom-download__param-select"
                    v-model="rangeSubset"
                    :clearable="true"
                    :options="options.rangeSubset"
                    :placeholder="$tc('customDownloadOptionsPopup.none')" /> -->
            </div>
            <div class="custom-download__param">
                <label :title="$tc('customDownloadOptionsPopup.crs')" class="custom-download__param-title">{{ $tc('customDownloadOptionsPopup.crs') }}:</label>
                <CustomSelect
                    class="custom-download__param-select"
                    v-model="outputCRS"
                    :clearable="false"
                    :options="options.outputCRS"
                    :placeholder="$tc('customDownloadOptionsPopup.default')" />
            </div>
            <div class="custom-download__param">
                <label :title="$tc('customDownloadOptionsPopup.coordinatesLowerCorner')" class="custom-download__param-title">{{ $tc('customDownloadOptionsPopup.coordinatesLowerCorner') }}:</label>
                <div class="custom-download__param-sub coordinates">
                    <input class="custom-download__param-input" ref="lc0" type="number" :min="lowerCornerDefaults[0]" :max="upperCornerDefaults[0]" step="0.0000000001" v-model="lowerCorner[0]" />
                    <input class="custom-download__param-input" ref="lc1" type="number" :min="lowerCornerDefaults[1]" :max="upperCornerDefaults[1]" step="0.0000000001" v-model="lowerCorner[1]" />
                </div>
            </div>
            <div class="custom-download__param">
                <label :title="$tc('customDownloadOptionsPopup.coordinatesUpperCorner')" class="custom-download__param-title">{{ $tc('customDownloadOptionsPopup.coordinatesUpperCorner') }}:</label>
                <div class="custom-download__param-sub coordinates">
                    <input class="custom-download__param-input" ref="uc0" type="number" :max="upperCornerDefaults[0]" :min="lowerCornerDefaults[0]" step="0.0000000001" v-model="upperCorner[0]" />
                    <input class="custom-download__param-input" ref="uc1" type="number" :max="upperCornerDefaults[1]" :min="lowerCornerDefaults[1]" step="0.0000000001" v-model="upperCorner[1]" />
                </div>
            </div>
        </div>
        <div class="text-center margin-top-30">
            <button class="green-btn-default" @click="download()">{{ $tc('customDownloadOptionsPopup.download') }}</button>
            <button v-if="isBulkDownloadEnabled && !bookmarksMode" class="green-btn-default custom-download__add-to-bulk-download" @click="addToBulkDownload()"
                    :disabled="!isSignedIn" :title="isSignedIn ?  $tc('customDownloadOptionsPopup.addToDownloads') :  $tc('dabResult.thisOptionAvailableForSignedIn')">
                <i class="plus-icon"></i>
                {{ $tc('customDownloadOptionsPopup.addToDownloads') }}
            </button>
        </div>
    </div>
</template>

<script lang="ts">
// @ts-nocheck
import { Component, Vue, Prop } from 'nuxt-property-decorator';

import { DownloadFile, DownloadFileStatus } from '@/interfaces/DownloadFile';
import { FileDownloadActions } from '@/store/fileDownload/file-download-actions';
import PopupCloseService from '@/services/popup-close.service';
import { UserGetters } from '@/store/user/user-getters';
import { GeneralGetters } from '@/store/general/general-getters';
import { BulkDownloadActions } from '@/store/bulkDownload/bulk-download-actions';
import { BulkDownloadLink } from '@/interfaces/BulkDownloadLink';
import LogService from '@/services/log.service';
import NotificationService from '@/services/notification.service';
import BulkDownloadPopup from '@/components/BulkDownloadPopup.vue';
import { PopupActions } from '@/store/popup/popup-actions';

@Component
export default class CustomDownloadWCSComponent extends Vue {
    @Prop({ default: null, type: Object}) public options!: any;
    @Prop({ default: null, type: String}) public resultId!: string;
    @Prop({ default: null, type: String}) public resultOrgName!: string;
    @Prop({ default: null, type: String}) public resultTitle!: string;

    public format: any = null;
    public scaleFactor: number | null = null;
    public lowerCorner: any = [];
    public upperCorner: any = [];
    public lowerCornerDefaults: any = [];
    public upperCornerDefaults: any = [];
    public rangeSubset: string | null = null;
    public outputCRS: string | null = null;

    get isSignedIn() {
        return this.$auth.loggedIn;
    }

    get isBulkDownloadEnabled() {
        return this.$store.getters[GeneralGetters.isBulkDownloadEnabled];
    }

    public async openBulkDownloadPopup(e: any) {
        e.preventDefault();
        const title = this. $tc('popupTitles.downloadsList');
        await this.$store.dispatch(PopupActions.openPopup, {contentId: 'bulk-download', title, component: BulkDownloadPopup});
        return false;
    }

    public download() {
        const downloadFile: DownloadFile = {
            format: this.format,
            download: `${new Date().getTime()}_${this.resultTitle.replace(/ /gi, '_')}_${this.format}_${this.scaleFactor}`,
            type: 'wcs',
            id: new Date().getTime(),
            status: DownloadFileStatus.inProgress,
            statusObject: null,
            checkStatusUrl: this.prepareDownloadLink(),
            url: '',
            name: `${this.resultTitle}\n${new Date().toLocaleString()} | ${this.format}`,
            progressData: null
        };
        this.$store.dispatch(FileDownloadActions.addFile, downloadFile);
        PopupCloseService.closePopup('custom-download');

        NotificationService.show(
            `${this. $tc('popupTitles.downloadList')}`,
            `${this. $tc('popupContent.addedCustomToDownloadList')}`,
            10000,
            null,
            9999,
            'success'
        );

        setTimeout(() => {
            const openBulkDownloadPopup = document.querySelectorAll('.openBulkDownloadPopup');
            if (openBulkDownloadPopup && openBulkDownloadPopup.length) {
                openBulkDownloadPopup.forEach(button => {
                    button.addEventListener('click', this.openBulkDownloadPopup);
                });
            }
        }, 200);

        LogService.logElementClick(null, null, this.resultId, null, 'Custom download', null, this.resultOrgName, this.resultTitle);
    }

    public addToBulkDownload() {
        const link: BulkDownloadLink = {
            name: `${this.resultTitle}`,
            desc: `${this.resultTitle}\n${new Date().toLocaleString()} | ${this.format}`,
            url: this.prepareDownloadLink()
        };

        this.$store.dispatch(BulkDownloadActions.addLink, link);
        PopupCloseService.closePopup('custom-download');

        NotificationService.show(
            `${this. $tc('popupTitles.downloadList')}`,
            `${this. $tc('popupContent.addedToDownloadList')}`,
            10000,
            null,
            9999,
            'success'
        );

        setTimeout(() => {
            const openBulkDownloadPopup = document.querySelectorAll('.openBulkDownloadPopup');
            if (openBulkDownloadPopup && openBulkDownloadPopup.length) {
                openBulkDownloadPopup.forEach(button => {
                    button.addEventListener('click', this.openBulkDownloadPopup);
                });
            }
        }, 200);

        LogService.logElementClick(null, null, this.resultId, null, 'Custom to bulk download', null, this.resultOrgName, this.resultTitle);
    }

    private prepareDownloadLink() {
        const downloadUrl = `${this.options.baseUrl}&request=GetCoverage&format=${encodeURIComponent(this.format)}&scalefactor=${this.scaleFactor}&subset=Lat(${this.lowerCorner[0]},${this.upperCorner[0]})&subset=Long(${this.lowerCorner[1]},${this.upperCorner[1]})&outputCRS=${this.outputCRS}&rangesubset=${this.rangeSubset}`;
        return downloadUrl;
    }

    private mounted() {
        this.format = this.options.nativeFormat;
        this.scaleFactor = this.options.scaleFactor;
        this.lowerCorner = this.options.lowerCorner.split(' ');
        this.upperCorner = this.options.upperCorner.split(' ');
        this.lowerCornerDefaults = this.options.lowerCorner.split(' ');
        this.upperCornerDefaults = this.options.upperCorner.split(' ');
        this.outputCRS = this.options.outputCRS[0].id;
        this.rangeSubset = 'RED_BAND';
    }
}
</script>

<style lang="scss" scoped>

</style>
