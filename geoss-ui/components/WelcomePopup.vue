<template>
    <div class="welcome-popup popup-default">
        <button class="cancel-button" title="Cancel request" @click="close()"></button>
        <p class="earth-icon">
            <i class="icomoon-earth"></i>
        </p>
        <h2>
            {{ $tc('welcomePopup.newVersionTitle') }}
        </h2>
        <p>
            {{ $tc('welcomePopup.newVersionPart1') }}
        </p>
        <p>
            {{ $tc('welcomePopup.newVersionPart2') }} <a target="_blank" :href="tourUrl"> <i class="icomoon-tour"></i> {{ $tc('welcomePopup.newVersionPart3') }}</a>. <br />
            {{ $tc('welcomePopup.newVersionPart4') }} <NuxtLink to="/release-notes"><i class="icomoon-info"></i> {{ $tc('welcomePopup.newVersionPart5') }}</NuxtLink> {{ $tc('welcomePopup.newVersionPart6') }}
        </p>
    </div>
</template>

<script lang="ts">
import { Component, Vue, Prop } from 'nuxt-property-decorator';
import PopupCloseService from '@/services/popup-close.service';
import { SearchEngineGetters } from '~/store/searchEngine/search-engine-getters';
import TutorialTagsService from '@/services/tutorial-tags.service';

@Component
export default class WelcomePopupComponent extends Vue {
    get tourUrl() {
        return this.$store.getters[SearchEngineGetters.tourUrl];
    }
    public close(event?: string) {
        PopupCloseService.closePopup('welcome', event);
    }
    public mounted() {
        this.$cookies.remove('TUTORIAL_TAGS_BLACKLIST');
        TutorialTagsService.eventBus.$emit('blacklist-refresh');
    }
}
</script>

<style lang="scss">
.welcome-popup {
    .popup__content {
        background: none;
        font-size: 1.1em;
        color: white;
        line-height: 1.5em;
        text-align: center;

        .welcome-popup {
            background: none;
            color: white;
        }

        h2 {
            font-size: 1.33em;
            margin-bottom: 15px;
            font-weight: normal;
        }

        p {
            font-size: 1.1em;
            margin-bottom: 15px;

            a {
                color: $blue-light;
                font-weight: bold;

                &:hover {
                    color: $green;
                    text-decoration: none;
                }

                .icomoon-tour {
                    font-size: 1.3em;
                    top: 4px;
                    position: relative;
                    margin-right: -3px;
                }

                .icomoon-info {
                    font-size: 0.6em;
                    border: 1px solid;
                    border-radius: 50%;
                    padding: 3px;
                    position: relative;
                    top: -1px;
                    margin-right: -2px;
                }
            }

            &.earth-icon {
                font-size: 1.8em;
                margin-bottom: 25px;
                margin-top: 65px;

                i {
                    opacity: 0.06;
                    font-size: 10em;
                    position: absolute;
                    left: 50%;
                    top: 0;
                    transform: translateX(-50%);
                    z-index: -1;
                }
            }
        }

        .cancel-button {
            background: $red;
            position: absolute;
            height: 30px;
            width: 30px;
            border-radius: 50%;
            right: 62px;
            top: 33px;

            &:before, &:after {
                content: '';
                position: absolute;
                left: 5px;
                top: 13px;
                height: 3px;
                width: 20px;
                background: white;
                transform: rotate(45deg);
            }

            &:after {
                transform: rotate(-45deg);
            }
        }
    }
}
</style>
