<template>
    <div class="bookmarks-header">
        <div class="my-workspace-header">
            <h1>{{ $tc('bookmarks.title') }}</h1>
            <NuxtLink to="/" class="close-window">
                <div class="line-1"></div>
                <div class="line-2"></div>
            </NuxtLink>
        </div>
        <div v-if="results.length" class="tools">
            <div class="check-all">
                <input
                    v-model="checkAll"
                    type="checkbox"
                    id="all-bookmarks"
                    autocomplete="off"
                />
                <label for="all-bookmarks">
                    {{ $tc('bookmarks.selectAll') }}
                </label>
                <small>
                    {{ `- ${numberOfChecked()} ${$tc('bookmarks.checked')}` }}
                </small>
            </div>
            <div class="actions">
                <span>
                    <FileDownloadNotifier class="file-download-notifier" />
                    <b v-show="files.length" @click="openFilesDownload()">
                        {{ $tc('bookmarks.download') }}
                    </b>
                </span>
                <span class="show-on-map" @click="showOnMapChecked()">
                    <i class="icomoon-show-on-map"></i>
                    <b>{{ $tc('bookmarks.showOnMap') }}</b>
                </span>
                <span class="delete" @click="deleteChecked()">
                    <i>✖</i>
                    <b>{{ $tc('bookmarks.delete') }}</b>
                </span>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { Component, Vue } from 'nuxt-property-decorator'
import { BookmarksActions } from '@/store/bookmarks/bookmarks-actions'
import { BookmarksGetters } from '@/store/bookmarks/bookmarks-getters'
import { PopupActions } from '@/store/popup/popup-actions'
import PopupCloseService from '@/services/popup-close.service'
import ErrorPopup from '@/components/ErrorPopup.vue'
import { DataOrigin, DataOriginMask } from '@/interfaces/DataSources'
import GeossSearchApiService from '@/services/geoss-search.api.service'
import { FileDownloadGetters } from '@/store/fileDownload/file-download-getters'
import { FileDownloadActions } from '@/store/fileDownload/file-download-actions'
import BookmarksService from '~/services/bookmarks.service'

@Component
export default class BookmarksHeader extends Vue {
    get files() {
        return this.$store.getters[FileDownloadGetters.files]
    }

    get mode() {
        return this.$store.getters[BookmarksGetters.mode]
    }

    get results() {
        return this.$store.getters[BookmarksGetters.results]
    }

    get checkAll() {
        return this.$store.getters[BookmarksGetters.checkAll]
    }

    get resultsOrigin() {
        return this.$store.getters[BookmarksGetters.resultsOrigin]
    }

    set checkAll(checkAll) {
        this.$store.dispatch(BookmarksActions.setCheckAll, checkAll)
    }

    get checkedResults() {
        return this.$store.getters[BookmarksGetters.checkedResults]
    }

    get checkedOrigins() {
        return this.$store.getters[BookmarksGetters.checkedOrigins]
    }

    public numberOfChecked() {
        return this.checkedResults.length
    }

    public openFilesDownload() {
        this.$store.dispatch(FileDownloadActions.openTrigger, true)
    }

    public popupNoItemsSelected() {
        const props = {
            title: this.$t('bookmarks.noItemsSelected'),
            subtitle: this.$t('bookmarks.selectItems'),
            actions: [{ event: 'accept', label: this.$t('bookmarks.accept') }],
        }
        this.$store.dispatch(PopupActions.openPopup, {
            contentId: 'error',
            component: ErrorPopup,
            props,
        })
    }

    public popupIncompatibleDataSources() {
        const actions: any = []
        for (const origin of this.checkedOrigins) {
            actions.push({
                event: origin,
                label: DataOriginMask[origin],
            })
        }
        const props = {
            title: this.$t('bookmarks.incompatibleDataSources'),
            subtitle: this.$t('bookmarks.desiredSource'),
            actions,
        }
        this.$store.dispatch(PopupActions.openPopup, {
            contentId: 'error',
            component: ErrorPopup,
            props,
        })
    }

    public dataSourcesCompatible() {
        const checkedOrigins: string[] = []
        let compatible = true
        for (const id of this.checkedResults) {
            for (const origin in this.resultsOrigin.byType) {
                if (
                    this.resultsOrigin.byType[origin].includes(id.toString()) &&
                    !checkedOrigins.includes(origin)
                ) {
                    checkedOrigins.push(origin)
                }
            }
            if (checkedOrigins.length > 1) {
                compatible = false
            }
        }
        this.$store.dispatch(BookmarksActions.setCheckedOrigins, checkedOrigins)
        return compatible
    }

    public popupChooseDataOrign(origin: string | number) {
        let checkedResults = this.checkedResults
        for (const id of checkedResults) {
            if (!this.resultsOrigin.byType[origin].includes(id)) {
                checkedResults = checkedResults.filter(
                    (item: any) => item !== id
                )
            }
        }
        this.$store.dispatch(BookmarksActions.setCheckAll, false)
        this.$store.dispatch(BookmarksActions.setCheckedResults, checkedResults)
        this.$store.dispatch(BookmarksActions.setCheckedOrigins, [origin])

        this.showResultsOnMap()
    }

    public getResultsDataSource(id: any) {
        return this.results.find((result: any) => result.targetId === id)
            .dataSource
        // const safeId = GeossSearchApiService.safeString(id)
        // return this.resultsOrigin.byId[safeId].dataSource
    }

    public showResultsOnMap() {
        const targetIds = this.checkedResults
        const dataSources: any[] = []
        for (const id of targetIds) {
            const source = this.getResultsDataSource(id)
            if (!dataSources.includes(source)) {
                dataSources.push(source)
            }
        }
        window.open(
            `/?f:dataSource=${dataSources.join()}&targetId=${targetIds}`
        )
    }

    public showOnMapChecked() {
        if (this.checkedResults.length) {
            if (this.dataSourcesCompatible()) {
                this.showResultsOnMap()
            } else {
                this.popupIncompatibleDataSources()
            }
        } else {
            this.popupNoItemsSelected()
        }
    }

    public async removeBookmarks() {
        const promises = []
        for (const id of this.checkedResults) {
            // const dataOrigin = DataOrigin[this.getResultsDataSource(id)]
            const dataSource = this.getResultsDataSource(id)
            //@ts-ignore
            const token = this.$nuxt.$auth.getToken('keycloak')

            promises.push(
                BookmarksService.removeBookmark(token, id, dataSource)
            )
        }
        Promise.all(promises).then(() =>
            this.$store.dispatch(BookmarksActions.getResults)
        )
    }

    public async removeGeoLikes() {
        const promises = []
        for (const id of this.checkedResults) {
            const dataOrigin = DataOrigin[this.getResultsDataSource(id)]
            promises.push(GeossSearchApiService.removeGeoLike(id, dataOrigin))
        }
        Promise.all(promises).then(() =>
            this.$store.dispatch(BookmarksActions.getResults)
        )
    }

    public deleteChecked() {
        if (!this.checkedResults.length) {
            this.popupNoItemsSelected()
        } else {
            if (this.mode === 'geo-likes') {
                this.removeGeoLikes()
            } else {
                this.removeBookmarks()
            }
        }
    }

    public closeWindowAndredirectToSearch() {
        if (sessionStorage.getItem('RECENT_SEARCH_PARAMS')) {
            window.location.href =
                window.location.origin +
                sessionStorage.getItem('RECENT_SEARCH_PARAMS')
        } else {
            window.location.href = window.location.origin
        }
    }

    private mounted() {
        PopupCloseService.eventBus.$on(
            'close',
            ({
                contentId,
                response,
            }: {
                contentId: string
                response?: any
            }) => {
                if (this.checkedOrigins.includes(response)) {
                    // make sure if proper error popup is closed
                    this.popupChooseDataOrign(response)
                }
            }
        )
    }
}
</script>

<style lang="scss" scoped>
.bookmarks-header {
    // .title {
    //     background-color: $blue-transparent;
    //     color: white;
    //     padding: 15px 25px;
    //     color: white;
    //     font-size: 18px;
    //     font-weight: bold;
    //     display: flex;
    //     justify-content: space-between;
    // }
    .tools {
        flex: 0 0 auto;
        background-color: $green-transparent;
        display: flex;
        color: white;
        align-items: center;
        justify-content: space-between;
        font-size: 14px;
        padding: 15px 25px;
        margin: 5px 0 0;

        .actions {
            display: flex;
            align-items: center;

            span {
                cursor: pointer;
                display: flex;
                align-items: center;
                margin-left: 20px;
                font-size: 1.2em;
                line-height: 1.5em;
                transition: 0.1s ease all;
                position: relative;

                &:hover {
                    &.show-on-map i {
                        color: $yellow;
                        transition: 0.1s ease all;
                    }

                    &.delete i {
                        color: $red;
                        transition: 0.1s ease all;
                    }
                }

                b {
                    font-weight: normal;
                }

                i {
                    transition: 0.1s ease all;
                    font-style: normal;
                    display: inline-block;
                    margin-right: 5px;
                }

                .file-download-notifier {
                    width: 30px;
                    height: 30px;
                    margin: 0 7px -5px 0;
                    top: -3px;

                    v-deep i {
                        font-size: 24px;
                    }
                }

                @media (max-width: $breakpoint-xs) {
                    b {
                        display: none;
                    }
                }
            }
        }

        .check-all {
            display: flex;
            flex-wrap: nowrap;
            align-items: baseline;

            input {
                display: none;

                &:checked + label:before {
                    content: '✖';
                }
            }

            label {
                position: relative;
                cursor: pointer;
                font-size: 1.2em;
                line-height: 1.5em;
                transition: 0.1s ease all;

                &:before {
                    content: '';
                    display: inline-block;
                    width: 13px;
                    height: 13px;
                    margin-right: 7px;
                    border: 1px solid white;
                    vertical-align: middle;
                    margin-bottom: 4px;
                    font-size: 16px;
                    line-height: 13px;
                }
            }

            small {
                margin-left: 5px;
            }
        }
    }
    .close-window {
        width: 25px;
        height: 25px;
        position: relative;

        &:hover {
            animation: rotateCloseIcon 300ms
                cubic-bezier(0.645, 0.045, 0.355, 1) both;

            & > div {
                background-color: #e31e24;
            }
        }

        & > div {
            width: 25px;
            height: 2px;
            background-color: white;
            transform-origin: center;
            position: absolute;
            transition: background-color 300ms
                cubic-bezier(0.645, 0.045, 0.355, 1);
        }

        .line-1 {
            transform: rotate(45deg);
        }

        .line-2 {
            transform: rotate(-45deg);
        }

        @keyframes rotateCloseIcon {
            0% {
                transform: rotate(0deg);
            }

            50% {
                transform: rotate(180deg);
            }

            100% {
                transform: rotate(360deg);
            }
        }
    }
}
</style>
