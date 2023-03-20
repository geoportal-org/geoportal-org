<template>
    <header class="header" :class="{ active: menuOpened }">
        <div class="header__left">
            <button class="header__menu-trigger" :aria-label="$tc('menu.toggle')" @click="toggleMenu()"
                :class="{ active: menuOpened }" data-tutorial-tag="header-menu-trigger">
                <span class="top"></span>
                <span class="middle"></span>
                <span class="bottom"></span>
            </button>
            <a title="GEOSec" href="http://www.earthobservations.org/index.php" target="_blank"
                data-tutorial-tag="header-logo-geosec">
                <img :src="`/img/geoss-logo-white.png`" alt="GEO Secretary logo" />
            </a>
        </div>
        <div class="header__middle">
            <a :title="siteName" :href="siteUrl" data-tutorial-tag="header-logo-main">
                <img :src="siteLogo" :alt="siteName" />
            </a>
        </div>
        <div class="header__right">
            <a title="CNR_IIA" href="https://en.iia.cnr.it/" target="_blank" data-tutorial-tag="header-logo-cnr">
                <img :src="`/img/CNR_logo_IIA-1-white.png`" alt="CNR IIA logo" />
            </a>
            <a title="ESA" href="http://www.esa.int" target="_blank" data-tutorial-tag="header-logo-esa">
                <img :src="`/img/ESA-logo-51-white.png`" alt="ESA logo" />
            </a>
            <div class="header__language-switcher" v-click-outside="closeLangContainer"
                data-tutorial-tag="header-language-switch">
                <button @click="toggleLangContainer()" :title="$tc('menu.languageToggle')"
                    :aria-label="$tc('menu.languageToggle')">
                    <i class="icomoon-language"></i>
                    <span>{{ languageMap[langLocale] }}</span>
                </button>
                <button @click="toggleLangContainer()" :title="$tc('menu.languageToggle')"
                    :aria-label="$tc('menu.languageToggle')">
                    <span></span>
                </button>
                <div class="options">
                    <CollapseTransition>
                        <div v-show="langContainerActive" class="options__wrapper">
                            <span :class="{ active: (langLocale === 'en_US') }"
                                @click="changeLangLocale('en_US')">English</span>
                            <span :class="{ active: (langLocale === 'pl_PL') }"
                                @click="changeLangLocale('pl_PL')">Polish</span>
                            <span :class="{ active: (langLocale === 'es_ES') }"
                                @click="changeLangLocale('es_ES')">Spanish</span>
                            <span :class="{ active: (langLocale === 'zh_CN') }"
                                @click="changeLangLocale('zh_CN')">Chinese</span>
                            <span :class="{ active: (langLocale === 'fr_FR') }"
                                @click="changeLangLocale('fr_FR')">French</span>
                            <span :class="{ active: (langLocale === 'ru_RU') }"
                                @click="changeLangLocale('ru_RU')">Russian</span>
                        </div>
                    </CollapseTransition>
                </div>
            </div>
        </div>
    </header>
</template>

<script lang="ts">
import { Component, Vue } from 'nuxt-property-decorator';
import { MenuActions } from '@/store/menu/menu-actions';
import { MenuGetters } from '@/store/menu/menu-getters';
import { GeneralActions } from '@/store/general/general-actions';
import { GeneralGetters } from '@/store/general/general-getters';
import { SearchEngineGetters } from '@/store/search-engine/search-engine-getters';
import SpinnerService from '@/services/spinner.service';
import axios from 'axios';
import CollapseTransition from '@/plugins/CollapseTransition';

@Component({
    components: {
        CollapseTransition
    }
})
export default class HeaderComponent extends Vue {

    public languageMap = {
        en_US: 'English',
        pl_PL: 'Polish',
        es_ES: 'Spanish',
        fr_FR: 'French',
        zh_CN: 'Chinese',
        ru_RU: 'Russian'
    };

    get menuOpened() {
        return this.$store.getters[MenuGetters.opened];
    }

    get langContainerActive() {
        return this.$store.getters[MenuGetters.langContainerActive];
    }

    get langLocale() {
        return this.$store.getters[GeneralGetters.langLocale];
    }

    get siteName() {
        return this.$store.getters[SearchEngineGetters.siteName];
    }

    get siteLogo() {
        return this.$store.getters[SearchEngineGetters.siteLogo];
    }

    get siteUrl() {
        return this.$store.getters[SearchEngineGetters.siteUrl];
    }

    get isWidget() {
        return this.$store.getters[GeneralGetters.isWidget];
    }

    public toggleMenu() {
        console.log(this.$store)
        this.$store.dispatch(MenuActions.toggleOpened);
    }

    public toggleLangContainer() {
        this.$store.dispatch(MenuActions.setLangContainerActive, !this.langContainerActive);
    }

    public closeLangContainer() {
        if (this.langContainerActive) {
            this.$store.dispatch(MenuActions.setLangContainerActive, false);
        }
    }

    public changeLangLocale(locale: string) {
        if (this.langLocale !== locale) {
            if (!this.isWidget) {
                const requestUrl = `${location.origin}${location.pathname}?p_p_id=82&p_p_lifecycle=1&p_p_state=normal&p_p_mode=view&p_p_col_id=column-1&p_p_col_count=1&_82_struts_action=%2Flanguage%2Fview&_82_redirect=${location.pathname}`;
                const requestHeaders = { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' };
                const requestData = new FormData();
                requestData.set('_82_formDate', `${new Date().getTime()}`);
                requestData.set('_82_languageId', locale);
                axios({
                    method: 'post',
                    url: requestUrl,
                    headers: requestHeaders,
                    data: requestData
                }).then(() => {
                    if (!document.querySelector('.liferay-essentials') || document.querySelector('.dockbar-ready')) {
                        SpinnerService.showSpinner(null, false);
                        location.reload();
                    }
                });
            }
            this.$store.dispatch(GeneralActions.setLangLocale, locale);
        }
        this.toggleLangContainer();
    }

    private mounted() {
        if (this.$cookies.get('GUEST_LANGUAGE_ID')) {
            this.$store.dispatch(GeneralActions.setLangLocale, this.$cookies.get('GUEST_LANGUAGE_ID'));
        } else {
            this.$store.dispatch(GeneralActions.setLangLocale, 'en_US');
        }
    }
}
</script>

<style lang="scss" scoped>
.header {
    position: absolute;
    left: 0;
    top: 0;
    width: 100%;
    z-index: 302;
    display: flex;
    justify-content: space-between;
    align-items: stretch;
    padding: 30px;
    background: linear-gradient(black -30%, transparent 75%);

    &:after {
        content: '';
        position: absolute;
        left: 0;
        right: 0;
        top: 0;
        background-color: $blue;
        z-index: -1;
        transform: translateX(-100%);
        width: auto;
        max-width: 320px;
        padding-top: 80px !important;
        transition: transform 450ms ease-in-out;
    }

    &.active:after {
        @media (max-width: $breakpoint-sm) {
            transform: translateX(0);
        }

        @media(max-width: $breakpoint-xxs) {
            max-width: 100vw;
        }
    }

    .header__left,
    .header__middle,
    .header__right {
        display: flex;
        align-items: center;

        &>* {
            margin-right: 60px;

            &:last-child {
                margin-right: 0;
            }

            @media (max-width: $breakpoint-lg) {
                margin-right: 40px;
            }

            @media (max-width: $breakpoint-sm) {
                margin-right: 20px;
            }
        }
    }

    .header__middle,
    .header__right a:first-child {
        @media (max-width: $breakpoint-xs) {
            display: none;
        }
    }

    &__menu-trigger {
        height: 25px;
        width: 30px;
        position: relative;

        .top,
        .middle,
        .bottom {
            position: absolute;
            width: 100%;
            height: 3px;
            top: 0;
            left: 0;
            background: white;
            transition: transform 150ms ease-in-out;
        }

        .middle {
            top: 50%;
            transform: translateY(-50%);
            transition: transform 150ms ease-in-out, opacity 150ms ease-in-out;
        }

        .bottom {
            top: calc(100% - 3px);
        }

        &.active {
            .top {
                transform: translate3d(0, 12px, 0) rotate(45deg);
            }

            .middle {
                transform: translate(-50%, -50%);
                opacity: 0;
            }

            .bottom {
                transform: translate3d(0, -15px, 0) rotate(-45deg);
                top: calc(100% + 2px);
            }
        }
    }

    img {
        height: 30px;

        @media (max-width: $breakpoint-lg) {
            height: 22px;
        }
    }

    $size: 5px;

    &__language-switcher {
        position: relative;

        button:first-child {
            color: white;
            display: flex;
            align-items: center;

            @media (max-width: $breakpoint-md) {
                display: none;
            }

            i {
                margin-right: 10px;
                font-size: 17px;
            }

            span {
                font-size: 17px;
            }
        }

        button:not(:first-child) {
            display: none;
            width: 10px;
            height: calc(#{5px} * 5);
            position: relative;

            @media (max-width: $breakpoint-md) {
                display: block;
            }

            span {
                width: 5px;
                height: 5px;
                background: white;
                border-radius: 50%;
                display: block;
                position: absolute;
                top: calc(50% - #{5px / 2});
                left: calc(50% - #{5px / 2});
            }

            &:before,
            &:after {
                content: '';
                position: absolute;
                width: 5px;
                height: 5px;
                background: white;
                border-radius: 50%;
                top: 0;
                left: calc(50% - #{5px / 2});
            }

            &:after {
                top: auto;
                bottom: 0;
            }
        }

        .options {
            position: absolute;
            top: calc(100% + #{5px});
            right: 0;
            background: white;
            border-radius: 5px;
            overflow: hidden;

            &__wrapper {
                span {
                    display: block;
                    width: 100px;
                    text-align: center;
                    padding: 5px 0;
                    border-bottom: 1px solid #ccc;

                    &:hover,
                    &.active {
                        background: $blue;
                        color: white;
                        cursor: pointer;
                    }

                    &:last-child {
                        border-bottom: none;
                    }
                }
            }
        }
    }
}
</style>
