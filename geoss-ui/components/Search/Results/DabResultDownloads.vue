<template>
    <div class="download-links">
        <div v-for="(link, index) of sortedLinks" :key="index" class="download-link">
            <template v-if="link.type !== 'html'">
                <input v-if="isBulkDownloadEnabled" type="checkbox" :id="`popup-result-${index}`" :value="link" class="download-link__input" v-model="selectedLinks" />
                <label v-if="isBulkDownloadEnabled" :for="`popup-result-${index}`" class="download-link__label">
                    <i class="icomoon-checkbox--empty" v-show="selectedLinks.findIndex(item => item.url === link.url) === -1"></i>
                    <i class="icomoon-checkbox--filled" v-show="selectedLinks.findIndex(item => item.url === link.url) !== -1"></i>
                </label>
            </template>
            <i v-if="FileFormatsIcons.indexOf(link.type) !== -1" :class="`download-link__icon icomoon-doc-${link.type} ${link.scoreClass}`" :title="link.scoreText"></i>
            <i v-else :class="`download-link__icon icomoon-doc-file ${link.scoreClass}`" :title="link.scoreText"></i>
            <div>
                <button :title="link.scoreText" v-if="link.url.indexOf('/dhus/odata/') !== -1" @click="openSentinelLoginPopup(link.url)">{{ getLinkName(link) }}</button>
                <button :title="link.type.toUpperCase()" v-else-if="link.url.indexOf('/sdg/Series/DataCSV') !== -1" @click="getUnepFile(link.postData)">{{ getLinkName(link) }}</button>
                <a :title="link.scoreText" v-else :href="link.url" target="_blank" @click="downloadLinkClicked()">{{ getLinkName(link) }}</a>
                <div class="download-link__description">{{link.desc}}</div>
            </div>
        </div>
        <div v-if="isBulkDownloadEnabled" class="text-center margin-top-30">
            <button class="green-btn-default download-links__add-to-bulk-download" @click="addToBulkDownload()" :disabled="!selectedLinks.length || !isSignedIn" :title="isSignedIn ? $tc('customDownloadOptionsPopup.addToDownloads') : $tc('dabResult.thisOptionAvailableForSignedIn')">
                <i class="plus-icon"></i>
                {{ $tc('customDownloadOptionsPopup.addToDownloads') }}
            </button>
        </div>
    </div>
</template>

<script lang="ts">
// @ts-nocheck
import { Component, Vue, Prop } from 'nuxt-property-decorator';

import LogService from '@/services/log.service';
import MouseLeaveService from '@/services/mouse-leave.service';
import { FileFormatsIcons } from '@/data/file-formats-icons';
import PopupCloseService from '@/services/popup-close.service';
import GeossSearchApiService from '@/services/geoss-search.api.service';
import UtilsService from '@/services/utils.service';
import { BulkDownloadActions } from '@/store/bulkDownload/bulk-download-actions';
import { BulkDownloadLink } from '@/interfaces/BulkDownloadLink';
import { UserGetters } from '@/store/user/user-getters';
import to from '@/utils/to';
import { GeneralGetters } from '@/store/general/general-getters';
import { MyWorkspaceGetters } from '@/store/myWorkspace/my-workspace-getters';
import { PopupActions } from '@/store/popup/popup-actions';
import SentinelLogin from '@/components/Search/SentinelLogin.vue';
import SearchEngineService from '@/services/search-engine.service';

@Component
export default class DabResultDownloadsComponent extends Vue {
    public FileFormatsIcons = FileFormatsIcons;
    public sortedLinks = this.sortLinks;
    public selectedLinks = [];

    @Prop({required: true, type: Array}) public links: any;
    @Prop({required: true, type: Object}) private result: any;

    get isWidget() {
        return this.$store.getters[GeneralGetters.isWidget];
    }

    get sortLinks() {
        return this.links.sort((a: any, b: any) => {
            if (!a.score && !b.score) {
                return 0;
            } else if (!a.score && b.score) {
                return 1;
            } else if (a.score && !b.score) {
                return -1;
            } else if (a.score > b.score) {
                return -1;
            } else if (a.score < b.score) {
                return 1;
            } else {
                return 0;
            }
        });
    }

    get isSignedIn() {
        return this.$auth.loggedIn;
    }

    get isBulkDownloadEnabled() {
        return this.$store.getters[GeneralGetters.isBulkDownloadEnabled];
    }

    public getLinkName(link: any) {
        if (link.format === 'other' && ((link.name === '') || (link.name === 'all')) && link.url.indexOf('MapServer') > -1) {
            return 'Map Server';
        } else {
            return link.name;
        }
    }

    public downloadLinkClicked() {
        LogService.logElementClick(null, null, this.result.id, null, 'Direct download', null, this.result.contributor.orgName, this.result.title);
        MouseLeaveService.initSurvey();
    }

    public openSentinelLoginPopup(url: string) {
        PopupCloseService.closePopup('download-links', url);
        if (this.isWidget) {
            window.open(url);
        } else if (!this.isSignedIn || !this.$store.getters[MyWorkspaceGetters.settings].dhusUsername) {
            const props = {
                url,
                result: this.result
            };
            this.$store.dispatch(PopupActions.openPopup, { contentId: 'sentinel-login', title: this.$tc('popupTitles.sentinelDataAccess'), component: SentinelLogin, props });
        } else {
            window.open(SearchEngineService.getDhusProxyUrl(url));
        }
    }

    public addToBulkDownload() {
        this.selectedLinks.forEach((item: any) => {
            const link: BulkDownloadLink = {
                name: `${item.name}`,
                desc: `Format: ${item.type || 'other'}`,
                url: item.url
            };
            this.$store.dispatch(BulkDownloadActions.addLink, link);
        });

        PopupCloseService.closePopup('download-links');

        LogService.logElementClick(null, null, this.result.id, null, 'Direct to bulk download', null, this.result.contributor.orgName, this.result.title);
    }

    public async getUnepFile(params: any) {
        const [, data] = await to(GeossSearchApiService.getUnepFileUrl(params.series));
        if (data) {
            UtilsService.createAndOpenFile(data, `SDG-${params.indicator}-${params.series[0]}.csv`, 'text/csv');
        }

        LogService.logElementClick(null, null, this.result.id, null, 'Direct download', null, this.result.contributor.orgName, this.result.title);
    }
}
</script>

<style lang="scss" scoped>
.download-links {
    padding: 30px 25px;

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

.download-link {
    display: flex;
    margin-bottom: 30px;

    &:last-child {
        margin-bottom: 0;
    }

    &__input {
        height: 0;
        opacity: 0;
        width: 0;
    }

    &__label {
        position: relative;

        i {
            background: white;
            cursor: pointer;
            font-size: 20px;
            left: -8px;
            position: absolute;
            top: 9px;

            &::before {
                color: #777;
            }

            &.icomoon-checkbox--filled {
                background: transparent;

                &::before {
                    color: black;
                }
            }

        }
    }

    &__icon {
        font-size: 25px;
        margin: 0 20px;
        color: white;
        display: flex;
        justify-content: center;
        align-items: center;
        border-radius: 50%;
        min-width: 40px;
        width: 40px;
        height: 40px;

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

    a,
    button {
        display: block;
        margin-bottom: 5px;
    }
}
</style>
