<template>
    <div
        class="user-contributions"
        v-if="userExtensions && userExtensions.length && showSection()"
    >
        <div class="user-contributions__label">
            {{
                $tc(
                    `popupContent.userContributedTitle${
                        model.charAt(0).toUpperCase() + model.slice(1)
                    }`
                )
            }}
        </div>
        <div
            class="user-contributions__item"
            v-for="(extension, index) in userExtensions"
            :key="index"
            v-show="showItem(extension)"
        >
            <div class="user-contributions__item-metadata">
                <div>
                    <i class="icomoon-editor--user"></i>
                </div>
                <div>
                    <b>{{ extension.userName }}</b>
                    <span>{{
                        formatDate(
                            extension.modifiedDate || extension.createdDate
                        )
                    }}</span>
                </div>
                <div
                    v-if="
                        userExtensions.userId &&
                        extension.userId.toString() === userId &&
                        showDeleteIcon &&
                        model !== 'transferOptions'
                    "
                    :title="
                        $tc('popupContent.userContributedRemoveEntryExtension')
                    "
                    class="delete-extension"
                    @click="removeExtension(extension)"
                >
                    <i class="icomoon-editor--trash"></i>
                </div>
            </div>
            <div
                v-if="model === 'summary'"
                class="user-contributions__item-content"
            >
                {{ extension[model] }}
            </div>
            <div
                v-if="model === 'keywords'"
                class="user-contributions__item-content"
            >
                <div v-for="(keyword, index) of extension[model]" :key="index">
                    <a
                        class="metadata__keyword"
                        @click="keywordSearch(keyword)"
                        >{{ keyword }}</a
                    >
                </div>
            </div>
            <div
                v-if="model === 'transferOptions'"
                class="user-contributions__item-content"
            >
                <div v-for="(link, index) in extension[model]" :key="index">
                    <div class="metadata__link" :title="link.displayTitle">
                        <div>
                            <a target="_blank" :href="link.url" class="link">{{
                                link.name ? link.name : link.url
                            }}</a>
                            <div
                                v-if="
                                    link.description && link.description !== ''
                                "
                            >
                                {{ link.description }}
                            </div>
                        </div>
                        <div
                            v-if="
                                extension.userId.toString() === userId &&
                                showDeleteIcon &&
                                model === 'transferOptions'
                            "
                            class="delete-extension"
                            @click="removeExtension(extension, index)"
                        >
                            <i class="icomoon-editor--trash"></i>
                        </div>
                    </div>
                </div>
            </div>
            <div
                v-if="model === 'comment'"
                class="user-contributions__item-content"
            >
                {{ extension[model] }}
            </div>
        </div>
    </div>
</template>

<script lang="ts">
// @ts-nocheck
import { Component, Vue, Prop, Emit } from 'nuxt-property-decorator'
import _ from 'lodash'
import { Liferay } from '@/data/global'
import UtilsService from '@/services/utils.service'
import { GeneralGetters } from '@/store/general/general-getters'
import PopupCloseService from '@/services/popup-close.service'
import { PopupActions } from '@/store/popup/popup-actions'
import ExtensionRemovePopup from './ExtensionRemovePopup.vue'
import ErrorPopup from '@/components/ErrorPopup.vue'
import to from '@/utils/to'
import GeossSearchApiService from '@/services/geoss-search.api.service'
import GeneralPopup from '@/components/GeneralPopup.vue'

@Component
export default class UserContributionsMetadata extends Vue {
    @Prop(String) public model!: string
    @Prop(Object) public data!: any
    public showDeleteIcon = true

    get userExtensions() {
        if (this.model && this.model === 'comment') {
            return this.userContributionsComments
        } else {
            return this.userContributionsExtensions
        }
    }

    get userId() {
        return !UtilsService.isWidget() && typeof Liferay !== 'undefined'
            ? Liferay.ThemeDisplay.getUserId()
            : null
    }

    get userContributionsExtensions() {
        let data = null
        data = UtilsService.getArrayByString(
            this.data.data,
            'userContributions.extensions'
        )
        return data
    }

    get userContributionsComments() {
        let data = null
        data = UtilsService.getArrayByString(
            this.data.data,
            'userContributions.comments'
        )
        return this.data.comments
    }

    get langLocale() {
        return this.$store.getters[GeneralGetters.langLocale]
    }

    public formatDate(date: string) {
        const dateObj = new Date(date)
        let displayDate = `${dateObj.toLocaleString(
            this.langLocale.replace(/_/g, '-'),
            { month: 'long' }
        )} ${dateObj.getDate()}, ${dateObj.getFullYear()}`
        if (this.langLocale === 'pl_PL') {
            displayDate = `${dateObj.getDate()} ${dateObj.toLocaleString(
                this.langLocale.replace(/_/g, '-'),
                { month: 'long' }
            )} ${dateObj.getFullYear()}`
        }
        return displayDate
    }

    public showSection() {
        return this.userExtensions.find(
            (el) =>
                (typeof el[this.model] === 'string' && el[this.model] !== '') ||
                (typeof el[this.model] === 'object' &&
                    Array.isArray(el[this.model]) &&
                    el[this.model].length)
        )
    }

    public showItem(extension) {
        return (
            (typeof extension[this.model] === 'string' &&
                extension[this.model] !== '') ||
            (typeof extension[this.model] === 'object' &&
                Array.isArray(extension[this.model]) &&
                extension[this.model].length)
        )
    }

    public checkTransferOptionsAvailable(
        model,
        extensionLinkToDelete,
        availableLinks
    ) {
        let linkBlocked = true
        if (!availableLinks || !availableLinks.length) {
            return linkBlocked
        } else {
            for (const link of availableLinks) {
                if (!link.description) {
                    link.description = null
                }
                if (_.isEqual(extensionLinkToDelete, link)) {
                    linkBlocked = false
                    break
                }
            }
            return linkBlocked
        }
    }

    public async removeExtension(extension: any, index = 0) {
        let err
        let data
        if (this.model === 'comment') {
            data = {
                result: 'success'
            }
        } else {
            ;[err, data] = await to(
                GeossSearchApiService.entryExtensionRemoveAvailable(
                    extension.entryExtensionId
                )
            )
        }
        PopupCloseService.closePopup('metadata')
        if (err || (data && data.result === 'error')) {
            const props = {
                title: this.$tc('general.error'),
                subtitle: err || data.comment,
                actions: [
                    {
                        event: 'extension-remove-error',
                        label: 'OK'
                    }
                ]
            }
            this.$store.dispatch(PopupActions.openPopup, {
                contentId: 'error',
                component: ErrorPopup,
                props
            })
        } else if (data && data.result === 'success') {
            if (
                (this.model === 'summary' &&
                    data.canDeleteDescription === 'false') ||
                (this.model === 'keywords' &&
                    data.canDeleteKeywords === 'false') ||
                (this.model === 'transferOptions' &&
                    this.checkTransferOptionsAvailable(
                        this.model,
                        extension[this.model][index],
                        data.transferOptionsExtensionsToDelete
                    ))
            ) {
                const props = {
                    title: this.$tc(
                        'popupContent.userContributedRemoveEntryExtension'
                    ),
                    subtitle: this.$tc(
                        'popupContent.userContributedRemoveUnavailable'
                    ),
                    actions: [
                        {
                            event: 'extension-remove-unavailable',
                            label: 'OK'
                        }
                    ]
                }
                this.$store.dispatch(PopupActions.openPopup, {
                    contentId: 'general',
                    component: GeneralPopup,
                    props
                })
            } else {
                const extensionDetails = this.data.data.userContributions
                const props = {
                    metadata: this.data,
                    extension,
                    model: this.model,
                    arrayIndex: index
                }
                this.$store.dispatch(PopupActions.openPopup, {
                    contentId: 'extension-remove',
                    title: 'Remove entry extension',
                    component: ExtensionRemovePopup,
                    props
                })
            }
        }
    }

    @Emit()
    public keywordSearch(keyword) {
        return keyword
    }
}
</script>

<style lang="scss">
.user-contributions {
    clear: both;
    margin: 30px 0 0;

    &__label {
        color: $black;
        font-weight: bold;
        margin-bottom: 10px;
    }

    &__item {
        border-top: 1px solid $grey-lighter;
        padding: 10px 0;
        line-height: 1.2em;

        &-metadata {
            display: flex;
            align-items: center;

            > div {
                flex-grow: 1;
                margin-bottom: 3px;

                &.delete-extension {
                    flex-grow: 0;
                }

                &:first-child {
                    width: 40px;
                    color: $green;
                    flex-grow: 0;

                    i {
                        font-size: 1.7em;
                    }
                }

                * {
                    display: block;
                }

                span {
                    font-size: 0.85em;
                }
            }
        }

        &-content {
            padding-left: 40px;
            display: flex;
            flex-wrap: wrap;
            flex-direction: row;
            word-break: break-word;

            .metadata {
                &__link {
                    margin: 5px 0;
                    display: flex;
                    align-items: center;

                    > div {
                        flex-grow: 1;

                        &.delete-extension {
                            flex-grow: 0;
                        }
                    }

                    &:before {
                        top: 4px;
                    }

                    a {
                        margin-bottom: 0;
                    }
                }
            }
        }

        .delete-extension {
            color: $blue;

            i {
                font-size: 1.4em;
                cursor: pointer;
                transition: 0.25s ease color;

                &:hover {
                    transition: 0.5s ease color;
                    color: $red;
                    animation: wobble 0.5s both;
                }
            }
        }
    }
}
</style>
