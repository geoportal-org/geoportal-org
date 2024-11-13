<template>
    <button v-if="isBulkDownloadEnabled" class="disabled-transparent" :class="{pulsate}" @click="openBulkDownloadPopup()" :disabled="disabled || !isSignedIn">
        <i class="bulk-download__icon">
            <span v-if="links.length" class="counter">{{links.length}}</span>
        </i>
        <span class="button-caption">{{ $tc('popupTitles.downloadList') }}</span>
    </button>
</template>

<script lang="ts">
// @ts-nocheck
import { Component, Vue, Prop, Watch } from 'nuxt-property-decorator';

import BulkDownloadPopup from '@/components/BulkDownloadPopup.vue';

import { BulkDownloadGetters } from '@/store/bulkDownload/bulk-download-getters';
import { BulkDownloadActions } from '@/store/bulkDownload/bulk-download-actions';
import { DownloadFile } from '@/interfaces/DownloadFile';
import { FileDownloadGetters } from '@/store/fileDownload/file-download-getters';
import { PopupActions } from '@/store/popup/popup-actions';
import { AppVueObj } from '@/data/global';
import { GeneralGetters } from '@/store/general/general-getters';

@Component
export default class BulkDownloadNotifierComponent extends Vue {
    @Prop({ default: false, type: Boolean}) public disabled!: boolean;
    public pulsate = false;

    private popupOpened = false;

    public async openBulkDownloadPopup() {
        this.pulsate = false;
        this.popupOpened = true;

        const title = this.$tc('popupTitles.downloadsList');
        await this.$store.dispatch(PopupActions.openPopup, {contentId: 'bulk-download', title, component: BulkDownloadPopup});
        this.popupOpened = false;
    }

    get isSignedIn() {
        return this.$nuxt.$auth.loggedIn;
    }

    get links() {
        return this.$store.getters[BulkDownloadGetters.links];
    }

    get openTrigger() {
        return this.$store.getters[BulkDownloadGetters.openTrigger];
    }

    get files() {
        return this.$store.getters[FileDownloadGetters.files];
    }

    get isBulkDownloadEnabled() {
        return this.$store.getters[GeneralGetters.isBulkDownloadEnabled];
    }

    @Watch('files')
    private onFilesChanged(val: DownloadFile[], oldVal: DownloadFile[]) {
        if(val.length === oldVal.length && !this.popupOpened) {
            for(const file of val) {
                const oldFile = oldVal.find(item => item.id === file.id);
                if(oldFile && oldFile.status !== file.status) {
                    this.pulsate = true;
                    break;
                }
            }
        } else if(val.length > oldVal.length && !this.popupOpened) {
            this.pulsate = true;
        }
    }

    @Watch('openTrigger')
    private onOpenChanged(val: any, oldVal: any) {
        if (val) {
            this.openBulkDownloadPopup();
            this.$store.dispatch(BulkDownloadActions.openTrigger, false);
        }
    }
}
</script>

<style lang="scss">
.bulk-download-notifier {
    background-repeat: no-repeat;
    background-size: 100% 100%;
    color: white;
    height: 32px;
    margin-bottom: 7px;
    position: relative;
    top: -3px;
    width: 32px;

    &[disabled] {
        opacity: 0.75
    }

    @media (max-width: $breakpoint-lg) {
        height: 29px;
        margin-bottom: 0;
        margin-right: 30px;
        width: 29px;
        left: -10px;
    }

    .button-caption {
        font-size: 11px;
        display: block;
        width: auto;
        white-space: nowrap;
        transform: translateX(-50%);
        @media (max-width: $breakpoint-lg) {
            display: none;
        }
    }

    .bulk-download__icon {
        background: url('/svg/bulk-download.svg') center center no-repeat;
        width: 32px;
        height: 32px;
        display: inline-block;
        -webkit-filter: drop-shadow( 0px 0px 2px rgba(0, 0, 0, .2));
          filter: drop-shadow( 0px 0px 2px rgba(0, 0, 0, .2));
    }

    &.pulsate {
        animation: pulsate 1s linear infinite;
        background: $yellow;
        border-radius: 50%;
    }

    i {
        font-size: 33px;

        @media (max-width: $breakpoint-lg) {
            font-size: 33px;
        }
    }

    .counter {
        background-color: $green;
        border-radius: 50%;
        display: inline-block;
        font-family: NotesEsaReg;
        font-size: 14px;
        height: 18px;
        line-height: 1.4;
        position: absolute;
        right: -10px;
        top: -5px;
        width: 18px;

        @media (max-width: $breakpoint-lg) {
            right: -12px;
            top: 0;
        }
    }

    @keyframes pulsate {
        0% {
            transform: scale(1);
        }
        50% {
            transform: scale(1.3);
        }
        100% {
            transform: scale(1);
        }
    }
}
</style>
