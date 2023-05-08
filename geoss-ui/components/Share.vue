<template>
    <div class="social-sharing">
        <button :title="$tc('general.copyLink')" class="copy-link" @click="shareLog('Link')">
            <span>
                <i class="copy-link__success-icon"></i>
                <i class="icomoon-copy-link"></i>
            </span>
        </button>
        <!-- <social-sharing :url="url" :title="title" :description="description" :quote="quote" :hashtags="hashtags"
            :twitter-user="twitterUser" @open="initSurveyOnLeave()" inline-template>
            <div>
                <network network="facebook" title="Facebook" @click="shareLog('FB')">
                    <img alt="Facebook" :src="`/svg/social/facebook.svg`" />
                </network>
                <network network="linkedin" @click="shareLog('Linkedin')">
                    <img alt="LinkedIn" :src="`/svg/social/linkedin.svg`" />
                </network>
                <network network="skype" @click="shareLog('Skype')">
                    <img alt="Skype" :src="`/svg/social/skype.svg`" />
                </network>
                <network network="twitter" @click="shareLog('Twitter')">
                    <img alt="Twitter" :src="`/svg/social/twitter.svg`" />
                </network>
            </div>
        </social-sharing> -->
    </div>
</template>

<script lang="ts">
// @ts-nocheck
import { Component, Vue, Prop } from 'nuxt-property-decorator';
import ClipboardJS from 'clipboard';
import MouseLeaveService from '@/services/mouse-leave.service';
import LogService from '@/services/log.service';

@Component
export default class ShareComponent extends Vue {
    @Prop(String) public url: string;
    @Prop(String) public title: string;
    @Prop(String) public description: string;
    @Prop(String) public quote: string;
    @Prop(String) public hashtags: string;
    @Prop(String) public twitterUser: string;

    public initSurveyOnLeave() {
        MouseLeaveService.initSurvey();
    }

    public shareLog(network) {
        LogService.logRecommendationData('Search result', 'Share - ' + network);
    }

    private mounted() {
        const copyButton = new ClipboardJS(this.$el.querySelector('.copy-link'), {
            text: () => {
                this.initSurveyOnLeave();
                return this.url;
            }
        });
    }
}
</script>

<style lang="scss">
.social-sharing {
    display: flex;

    &>div {
        display: flex;
    }

    span,
    button {
        border-radius: 50%;
        margin-left: 10px;
        width: 32px;
        height: 32px;
        transition: background-color 250ms ease-in-out;
        display: flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;

        @media (max-width: $breakpoint-sm) {
            margin-left: 5px;
            width: 36px;
            height: 36px;
        }

        img {
            max-width: 20px;
            max-height: 20px;

            @media (max-width: $breakpoint-sm) {
                width: 20px;
            }
        }

        &[data-link="#share-facebook"] {
            background-color: rgba(#465192, 0.9);

            &:hover {
                background-color: rgba(#465192, 1);
                ;
            }
        }

        &[data-link="#share-linkedin"] {
            background-color: rgba(#468CC7, 0.9);

            &:hover {
                background-color: rgba(#468CC7, 1);
                ;
            }

            img {
                max-width: 18px;
                max-height: 18px;
            }
        }

        &[data-link="#share-skype"] {
            background-color: rgba(#44A3E7, 0.9);

            &:hover {
                background-color: rgba(#44A3E7, 1);
                ;
            }
        }

        &[data-link="#share-twitter"] {
            background-color: rgba(#4A94E8, 0.9);

            &:hover {
                background-color: rgba(#4A94E8, 1);
                ;
            }
        }
    }

    .copy-link {
        font-size: 19px;
        position: relative;
        color: white;
        background-color: rgba(#EFA310, 0.9);
        margin: 0;

        &:hover {
            background-color: rgba(#EFA310, 1);
            ;
        }

        @media (max-width: $breakpoint-sm) {
            font-size: 21px;
        }

        &:active {
            i {
                transition: none;
            }

            .copy-link__success-icon {
                opacity: 1;
            }

            .icomoon-copy-link {
                opacity: 0;
            }
        }

        .icomoon-copy-link {
            position: relative;
            left: -4px;
            transition: opacity 0s 1s;
        }

        &__success-icon {
            transition: opacity 0s 1s;
            opacity: 0;
            position: absolute;
            left: calc(50% - 2px);
            top: 50%;
            transform: translate(-50%, -50%);

            &:before {
                content: '\2714';
            }
        }
    }
}
</style>
