<template>
    <div
        class="popup"
        :class="{
            'visible': visible,
            'on-top':
                currentQueueItem &&
                currentQueueItem.contentId === 'dab-request-too-long'
        }"
    >
        <div
            class="popup__wrapper"
            v-click-outside="{
                fn: closePopupOnClickOutside,
                excludeSelectors:
                    '.custom-select__container, .spinner, .tutorial-tag, .tutorial-mode, .tutorial-off'
            }"
            :class="`${
                currentQueueItem ? currentQueueItem.contentId + '-popup' : ''
            }`"
        >
            <div
                class="popup__header"
                v-if="currentQueueItem && currentQueueItem.title"
            >
                <span v-html="currentQueueItem.title"></span>
                <button
                    @click="closePopup()"
                    class="cross"
                    data-tutorial-tag="popup-close-button"
                ></button>
            </div>
            <div class="popup__content" v-bar>
                <div ref="container">
                    <!-- <portal-target name="popup-content" v-if="currentQueueItem"></portal-target> -->
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
// @ts-nocheck
import { Component, Vue, Watch } from 'nuxt-property-decorator'

import { PopupGetters } from '@/store/popup/popup-getters'
import { PopupActions } from '@/store/popup/popup-actions'
import { Timers } from '@/data/timers'
import PopupCloseService from '@/services/popup-close.service'
import TutorialTagsService from '@/services/tutorial-tags.service'
import { GeneralFiltersGetters } from '~/store/generalFilters/general-filters-getters';

@Component
export default class PopupComponent extends Vue {
    public visible = false
    public currentQueueItem: {
        contentId: string
        title: string
        errorInfo?: any
        props?: any
        noCloseOutside?: boolean
    } | null = null

    public closePopupOnClickOutside() {
        if (this.currentQueueItem && !this.currentQueueItem.errorInfo && !this.currentQueueItem.noCloseOutside) {
            this.closePopup()
        }
    }

    public closePopup(response?: any) {
        if (this.visible) {
            this.visible = false
            TutorialTagsService.refreshTagsGroup('popup-', false)
            setTimeout(() => {
                if (response) {
                    this.$store.dispatch(PopupActions.closePopupWithResponse, {
                        contentId: this.currentQueueItem.contentId,
                        response
                    })
                } else {
                    this.$store.dispatch(
                        PopupActions.closePopup,
                        this.currentQueueItem.contentId
                    )
                }
            }, Timers.closePopup)
        }
    }

    private get queue() {
        return this.$store.getters[PopupGetters.queue]
    }

    private get openEoVisible() {
        return this.$store.getters[GeneralFiltersGetters.openEoPopupVisible]
    }


    private escEventListener(event: KeyboardEvent) {
        const key = event.which || event.keyCode
        if (key === 27) {
            this.closePopup()
        }
    }

    @Watch('openEoVisible')
    private onOpenEoVisibleChange(val: boolean) {
        this.visible = val
    }


    @Watch('queue')
    onQueueChanged() {
        if (
            this.queue.length &&
            JSON.stringify(this.queue[0]) !==
                JSON.stringify(this.currentQueueItem)
        ) {
            const item = this.queue[0]
            const instance = new item.component({
                propsData: item.props,
                store: this.$store,
                i18n: this.$i18n
            })

            instance.$mount()
            ;(this.$refs.container as HTMLElement).innerHTML = ''
            ;(this.$refs.container as HTMLElement).appendChild(instance.$el)

            this.currentQueueItem = item
            this.visible = true
            document.addEventListener('keyup', this.escEventListener)

            // to update vbar scrollbar size
            setTimeout(() => {
                window.dispatchEvent(new Event('resize'))
            }, Timers.openPopup)
            TutorialTagsService.refreshTagsGroup(
                'popup-',
                true,
                Timers.openPopup
            )
        } else if (!this.queue.length) {
            this.currentQueueItem = null
            document.removeEventListener('keyup', this.escEventListener)
        }
    }

    private mounted() {
        PopupCloseService.eventBus.$on(
            'close',
            ({
                contentId,
                response
            }: {
                contentId: string
                response?: any
            }) => {
                if (
                    this.currentQueueItem &&
                    this.currentQueueItem.contentId === contentId
                ) {
                    this.closePopup(response)
                } else {
                    if (response) {
                        this.$store.dispatch(
                            PopupActions.closePopupWithResponse,
                            { contentId, response }
                        )
                    } else {
                        this.$store.dispatch(PopupActions.closePopup, contentId)
                    }
                }
            }
        )
    }
}
</script>

<style lang="scss">
.popup {
    width: 0;
    height: 0;
    top: 50%;
    left: 50%;
    position: fixed;
    z-index: 9997;
    display: flex;
    padding: 0;
    justify-content: center;
    flex-direction: column;
    align-items: center;
    background-color: rgba(0, 0, 0, 0);
    transition: background-color 400ms ease-in-out, opacity 400ms ease-in-out,
        width 0ms 400ms ease-in-out, height 0ms 400ms ease-in-out,
        top 0ms 400ms ease-in-out, left 0ms 400ms ease-in-out;

    &.visible {
        width: 100%;
        height: 100%;
        top: 0%;
        left: 0%;
        padding: 15px 0;
        transition: opacity 300ms ease-in-out,
            background-color 300ms ease-in-out;
        background-color: rgba(0, 0, 0, 0.8);

        .popup__wrapper {
            transform: scale(1);
            opacity: 1;
        }
    }

    &.on-top {
        z-index: 9999;
    }

    &__wrapper {
        min-width: 30%;
        min-height: 150px;
        max-width: 800px;
        max-height: 650px;
        width: 100%;
        display: flex;
        flex-direction: column;
        opacity: 0;
        transform: scale(0.8);
        transition: transform 200ms ease-in-out, opacity 200ms ease-in-out;

        &.dashboard-creator-popup {
            max-width: 80%;
        }

        @media (max-width: $breakpoint-lg) {
            width: 90%;
            max-width: 370px;
        }
    }

    &__header {
        background: $blue;
        width: 100%;
        color: white;
        padding: 18px 25px;
        font-size: 20px;
        font-weight: bold;
        position: relative;

        button {
            position: absolute;
            top: 50%;
            right: 10px;
            transform: translateY(-50%);
        }
    }

    &__content {
        border-bottom-left-radius: 5px;
        border-bottom-right-radius: 5px;
        background: white;
    }
}
</style>
