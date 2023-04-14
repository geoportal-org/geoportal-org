<template>
    <button v-if="!isBulkDownloadEnabled" class="disabled-transparent" :class="{ pulsate }" @click="openFileDownloadPopup()"
        :disabled="disabled" v-show="files.length">
        <i class="icomoon-custom-download"></i>
    </button>
</template>

<script lang="ts">
import { Component, Vue, Prop, Watch } from 'nuxt-property-decorator';

// import FileDownloadPopup from '@/components/FileDownloadPopup.vue';

import { DownloadFile } from '@/interfaces/DownloadFile';
import { FileDownloadGetters } from '@/store/fileDownload/file-download-getters';
import { FileDownloadActions } from '@/store/fileDownload/file-download-actions';
import { PopupActions } from '@/store/popup/popup-actions';
import { AppVueObj } from '~/data/global';
import { GeneralGetters } from '@/store/general/general-getters';

@Component
export default class FileDownloadNotifierComponent extends Vue {
    @Prop({ default: false, type: Boolean }) public disabled!: boolean;
    public pulsate = false;

    private popupOpened = false;

    public async openFileDownloadPopup() {
        this.pulsate = false;
        this.popupOpened = true;

        const title = AppVueObj.app.$tc('popupTitles.yourDownloads');
        // await this.$store.dispatch(PopupActions.openPopup, {contentId: 'custom-downloads', title, component: FileDownloadPopup});
        this.popupOpened = false;
    }

    get files() {
        return this.$store.getters[FileDownloadGetters.files];
    }

    get openTrigger() {
        return this.$store.getters[FileDownloadGetters.openTrigger];
    }

    get isBulkDownloadEnabled() {
        return this.$store.getters[GeneralGetters.isBulkDownloadEnabled];
    }

    @Watch('files')
    private onFilesChanged(val: DownloadFile[], oldVal: DownloadFile[]) {
        if (val.length === oldVal.length && !this.popupOpened) {
            for (const file of val) {
                const oldFile = oldVal.find(item => item.id === file.id);
                if (oldFile && oldFile.status !== file.status) {
                    this.pulsate = true;
                    break;
                }
            }
        } else if (val.length > oldVal.length && !this.popupOpened) {
            this.pulsate = true;
        }
    }

    @Watch('openTrigger')
    private onOpenChanged(val: any, oldVal: any) {
        if (val) {
            this.openFileDownloadPopup();
            this.$store.dispatch(FileDownloadActions.openTrigger, false);
        }
    }
}
</script>

<style lang="scss">
.file-download-notifier {
    margin-bottom: 7px;
    width: 48px;
    height: 48px;
    background-repeat: no-repeat;
    background-size: 100% 100%;
    color: white;
    top: -3px;
    position: relative;

    @media (max-width: $breakpoint-lg) {
        margin-bottom: 0;
        margin-right: 30px;

        width: 29px;
        height: 29px;
    }

    &.pulsate {
        background: $yellow;
        border-radius: 50%;
        animation: pulsate 1s linear infinite;
    }

    i {
        font-size: 33px;

        @media (max-width: $breakpoint-lg) {
            font-size: 33px;
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

