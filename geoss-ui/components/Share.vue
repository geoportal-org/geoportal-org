<template>
    <button :title="$tc('general.copyLink')" class="copy-link" :disabled="disabled" @click="shareLog('direct link')"
        ref="copyButton">
        <i class="copy-link__success-icon"></i>
        <i class="icomoon-share"></i>
    </button>
</template>

<script lang="ts">
// @ts-nocheck
import { Component, Vue, Prop } from 'nuxt-property-decorator'
import ClipboardJS from 'clipboard'
import MouseLeaveService from '@/services/mouse-leave.service'
import LogService from '@/services/log.service'

@Component
export default class ShareComponent extends Vue {
    @Prop(String) public url: string
    @Prop(Boolean) public disabled: boolean

    public initSurveyOnLeave() {
        MouseLeaveService.initSurvey()
    }

    public shareLog(network: string) {
        LogService.logRecommendationData('Share - ' + network, 'url', this.url)
    }

    private mounted() {
        const button = this.$refs.copyButton as HTMLElement
        const copyButton = new ClipboardJS(button, {
            text: () => {
                this.initSurveyOnLeave()
                return this.url
            }
        })
    }
}
</script>

<style lang="scss" scoped>
.copy-link {
    position: relative;

    &:disabled {
        pointer-events: none;
    }

    &:active {
        i {
            transition: none;
        }

        .copy-link__success-icon {
            opacity: 1;
        }

        .icomoon-share {
            opacity: 0;
        }
    }

    .icomoon-share {
        position: relative;
        transition: opacity 0s 1s;
        left: -1px;
    }

    &__success-icon {
        transition: opacity 0s 1s;
        opacity: 0;
        position: absolute;
        left: calc(50% - 1px);
        top: 50%;
        transform: translate(-50%, -50%);

        &:before {
            content: '\2714';
        }
    }
}
</style>
