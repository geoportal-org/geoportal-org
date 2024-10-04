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
                <label :title="$tc('customDownloadOptionsPopup.imageResolutionInPixels')" class="custom-download__param-title">{{ $tc('customDownloadOptionsPopup.outputSize') }} (height,width):</label>
                <input class="custom-download__param-input" type="text" v-model="outputSize" :class="{invalid: !outputSizeValid}" @input="onOutputSizeChange()" />
            </div>
            <div class="custom-download__param">
                <label :title="$tc('customDownloadOptionsPopup.coordinateReferenceSystemOfSubset')" class="custom-download__param-title">{{ $tc('customDownloadOptionsPopup.subsetCRS') }}:</label>
                <CustomSelect
                    class="custom-download__param-select"
                    v-model="subsetCRS"
                    @input="onSubsetCRSChange()"
                    :clearable="false"
                    :options="options.subsetCRSOptions"
                    :placeholder="$tc('customDownloadOptionsPopup.subsetCRS')" />
            </div>
            <div class="custom-download__param" v-show="subsetCRS">
                <label :title="$tc('customDownloadOptionsPopup.subsetLowerCoordinateInSpecifiedCRSFormat')" class="custom-download__param-title">{{ $tc('customDownloadOptionsPopup.subsetLowerCoordinate') }} ({{subsetCoordinatesTitle}}):</label>
                <input class="custom-download__param-input" type="text" v-model="subsetLowerCoordinate" :class="{invalid: !subsetCoordinatesValid(subsetLowerCoordinate)}"
                    :disabled="!subsetCRS" @input="onSubsetCoordinates(subsetLowerCoordinate)" :title="subsetCoordinatesError" />
            </div>
            <div class="custom-download__param" v-show="subsetCRS">
                <label :title="$tc('customDownloadOptionsPopup.subsetUpperCoordinateInSpecifiedCRSFormat')" class="custom-download__param-title">{{ $tc('customDownloadOptionsPopup.subsetUpperCoordinate') }} ({{subsetCoordinatesTitle}}):</label>
                <input class="custom-download__param-input" type="text" v-model="subsetUpperCoordinate" :class="{invalid: !subsetCoordinatesValid(subsetUpperCoordinate)}"
                    :disabled="!subsetCRS" @input="onSubsetCoordinates(subsetUpperCoordinate)" :title="subsetCoordinatesError" />
            </div>
        </div>
        <div class="text-center margin-top-30">
            <button class="green-btn-default" :disabled="!outputSizeValid || !subsetCoordinatesValid" @click="download()">{{ $tc('customDownloadOptionsPopup.download') }}</button>
            <button v-if="isBulkDownloadEnabled && !bookmarksMode" class="green-btn-default custom-download__add-to-bulk-download" @click="addToBulkDownload()"
                    :disabled="!outputSizeValid || !subsetCoordinatesValid || !isSignedIn" :title="isSignedIn ? $tc('customDownloadOptionsPopup.addToDownloads') : $tc('dabResult.thisOptionAvailableForSignedIn')">
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
import { BulkDownloadActions } from '@/store/bulkDownload/bulk-download-actions';
import { UserGetters } from '@/store/user/user-getters';
import { GeneralGetters } from '@/store/general/general-getters';
import PopupCloseService from '@/services/popup-close.service';
import { BulkDownloadLink } from '@/interfaces/BulkDownloadLink';
import LogService from '@/services/log.service';
import NotificationService from '@/services/notification.service';
import BulkDownloadPopup from '@/components/BulkDownloadPopup.vue';
import { PopupActions } from '@/store/popup/popup-actions';

@Component
export default class DabResultCustomDownloadComponent extends Vue {
    @Prop({ default: null, type: Object}) public options!: any;
    @Prop({ default: null, type: String}) public resultId!: string;
    @Prop({ default: null, type: String}) public resultOrgName!: string;
    @Prop({ default: null, type: String}) public resultTitle!: string;
    @Prop({ default: false, type: Boolean}) public bookmarksMode!: boolean;

    public format: any = null;
    public outputSize: string = '128,256';
    public subsetCRS: any = null;
    public subsetLowerCoordinate: string = '';
    public subsetUpperCoordinate: string = '';

    public outputSizeEdited = false;
    public subsetLowerCoordinateEdited = false;
    public subsetUpperCoordinateEdited = false;

    public subsetCoordinatesTitle = '';
    public subsetCoordinatesError: any = null;

    get isBulkDownloadEnabled() {
        return this.$store.getters[GeneralGetters.isBulkDownloadEnabled];
    }

    public subsetCoordinatesValid(coordinate: any) {
        const values = coordinate.split(',');
        let valid = (!this.subsetCRS || (coordinate && values.length === 2 && !isNaN(parseInt(values[0], 10)) && !isNaN(parseInt(values[1], 10))));
        if (valid && this.subsetCRS) {
            if (this.subsetCRS === 'EPSG:4326') {
                const latitude = parseInt(values[0], 10);
                const longitude = parseInt(values[1], 10);
                if (latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180) {
                    valid = false;
                }
            } else if (this.subsetCRS === 'EPSG:3857') {
                const x = parseFloat(values[0]);
                const y = parseFloat(values[1]);
                if (x < -20037508.3427892 || x > 20037508.3427892 || y < -20037508.3427892 || y > 20037508.3427892) {
                    valid = false;
                }
            }
        }
        return valid;
    }

    public onOutputSizeChange() {
        this.outputSizeEdited = true;
    }

    public onSubsetCoordinates(coordinate: any) {
        if (coordinate === this.subsetLowerCoordinate) {
            this.subsetLowerCoordinateEdited = true;
        } else if (coordinate === this.subsetUpperCoordinate) {
            this.subsetUpperCoordinateEdited = true;
        }
    }

    public onSubsetCRSChange() {
        const index = this.options.subsetCRSOptions.findIndex((option: any) => option.id === this.subsetCRS);
        if (index !== 0) {
            const subsetCRSOption = this.options.subsetCRSOptions[index];

            if (subsetCRSOption.id === 'EPSG:4326') {
                this.subsetCoordinatesTitle = 'lat, long';
                this.subsetCoordinatesError = this.$tc('customDownloadOptionsPopup.latLongRule');
            } else if (subsetCRSOption.id === 'EPSG:3857') {
                this.subsetCoordinatesTitle = 'x, y';
                this.subsetCoordinatesError = this.$tc('customDownloadOptionsPopup.xYRule');
            }

            if (!this.outputSizeEdited && this.options.outputSize[index - 1]) {
                this.outputSize = this.options.outputSize[index - 1];
            }

            if (!this.subsetLowerCoordinateEdited) {
                this.subsetLowerCoordinate = this.options.subsetLowerCoordinates[index - 1];
            }

            if (!this.subsetUpperCoordinateEdited) {
                this.subsetUpperCoordinate = this.options.subsetUpperCoordinates[index - 1];
            }
        }
    }

    public async openBulkDownloadPopup(e: any) {
        e.preventDefault();
        const title = this.$tc('popupTitles.downloadsList');
        await this.$store.dispatch(PopupActions.openPopup, {contentId: 'bulk-download', title, component: BulkDownloadPopup});
        return false;
    }

    public download() {
        const downloadFile: DownloadFile = {
            format: '',
            download: '',
            type: '',
            id: new Date().getTime(),
            status: DownloadFileStatus.inProgress,
            statusObject: null,
            checkStatusUrl: this.prepareDownloadLink(),
            url: '',
            name: `${this.resultTitle}\n${this.format} | ${this.outputSize} | ${this.subsetCRS || 'Default subset'}`,
            progressData: null
        };

        this.$store.dispatch(FileDownloadActions.addFile, downloadFile);
        PopupCloseService.closePopup('custom-download');

        NotificationService.show(
            `${this.$tc('popupTitles.downloadList')}`,
            `${this.$tc('popupContent.addedCustomToDownloadList')}`,
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
            desc: `${this.format} | ${this.outputSize} | ${this.subsetCRS || 'Default subset'}`,
            url: this.prepareDownloadLink()
        };

        this.$store.dispatch(BulkDownloadActions.addLink, link);
        PopupCloseService.closePopup('custom-download');


        NotificationService.show(
            `${this.$tc('popupTitles.downloadList')}`,
            `${this.$tc('popupContent.addedToDownloadList')}`,
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

    get outputSizeValid() {
        const values = this.outputSize.split(',');
        return (values.length === 2 && !isNaN(parseInt(values[0], 10)) && !isNaN(parseInt(values[1], 10)));
    }

    get isSignedIn() {
        return this.$auth.loggedIn;
    }

    private prepareDownloadLink() {
        let params = `outputFormat=${encodeURIComponent(this.format)};outputSize=${encodeURIComponent(this.outputSize)}`;
        if (this.options.crs) {
            params += `;outputCRS=${encodeURIComponent(this.options.crs)}`;
        }
        if (this.subsetCRS) {
            params += `;outputSubsetCRS=${encodeURIComponent(this.subsetCRS)};outputSubset=${encodeURIComponent(this.subsetLowerCoordinate + ',' + this.subsetUpperCoordinate)}`;
        }

        const transformationUrl = this.options.baseUrl +
            '?service=WPS' +
            '&request=execute' +
            '&identifier=gi-axe-transform' +
            '&storeexecuteresponse=true' +
            '&DataInputs=' + encodeURIComponent(params);

        return transformationUrl;
    }

    private mounted() {
        if (this.options.outputSize[0]) {
            this.outputSize = this.options.outputSize[0];
        }
        this.subsetLowerCoordinate = this.options.subsetLowerCoordinates[0] ? this.options.subsetLowerCoordinates[0] : '';
        this.subsetUpperCoordinate = this.options.subsetUpperCoordinates[0] ? this.options.subsetUpperCoordinates[0] : '';
        this.format = this.options.formatOptions[0] ? this.options.formatOptions[0].id : '';
    }
}
</script>

<style lang="scss">
.custom-download {
    padding: 30px 25px;

    &__title {
        font-size: 20px;
        margin-bottom: 25px;
    }

    &__param {
        display: flex;
        align-items: center;
        justify-content: center;
        margin: 15px 0;

        &-title {
            width: 250px;
            font-weight: bold;
            margin-right: 10px;
            text-align: right;
        }

        &-select,
        &-input {
            width: 250px;
            &[type="range"] {
                -webkit-appearance: slider-horizontal;
            }
        }

        &-select {
            .custom-select__trigger {
                border: 1px solid #ddd;
                border-radius: 5px;
                padding: 5px;
                height: 37px;

                &:after {
                    position: absolute;
                    right: 15px;
                }
            }
        }

        &-input {
            border: 1px solid #ddd;
            border-radius: 5px;
            padding: 10px 5px;
        }

        &-sub {
            display: flex;
            justify-content: space-between;
            position: relative;
            width: 250px;

            [type="number"] {
                text-align: center;
                width: 25%;
            }

            [type="range"] {
                width: 70%;
            }

            &.coordinates {
                [type="number"] {
                    text-align: left;
                    width: 47.5%;
                }
            }
        }
    }

    &__add-to-bulk-download {
        margin-left: 20px;

        @media (max-width: $breakpoint-lg) {
            margin-left: 0;
            margin-top: 10px;
        }
    }

    .plus-icon {
        border: 2px solid #fff;
        border-radius: 50%;
        display: inline-block;
        height: 18px;
        position: relative;
        top: 2px;
        width: 18px;

        &::before, &::after {
            background: #fff;
            content: "";
            height: 2px;
            left: 3px;
            position: absolute;
            top: 6px;
            width: 8px;
        }

        &::after {
            transform: rotate(90deg);
        }
    }
}
</style>
