<template>
    <header class="header" :class="{ active: menuOpened, atlantos: isAtlantOsCommunityPortal() }">
        <div class="header__left">
            <button class="header__menu-trigger" :aria-label="$tc('menu.toggle')" @click="toggleMenu()" :class="{ active: menuOpened }" data-tutorial-tag="header-menu-trigger">
                <span class="top"></span>
                <span class="middle"></span>
                <span class="bottom"></span>
            </button>
            <div class="header__left__slider">
                <a title="GEOSec" href="http://www.earthobservations.org/index.php" target="_blank" data-tutorial-tag="header-logo-geosec">
                    <img :src="`/img/geoss-logo-white.png`" alt="GEO Secretary logo" />
                </a>
                <a :title="siteName" :href="siteUrl" data-tutorial-tag="header-logo-main">
                    <img :src="siteLogo" :alt="siteName" />
                </a>
                <a v-if="isAtlantOsCommunityPortal()" title="iAtlantic" href="https://doi.org/10.3030/818123" target="_blank" data-tutorial-tag="header-logo-atlantos">
                    <img :src="`/img/iAtlantic-Logo-Rev.png`" alt="iAtlantic" />
                </a>
                <a title="CNR_IIA" href="https://en.iia.cnr.it/" target="_blank" data-tutorial-tag="header-logo-cnr">
                    <img :src="`/img/CNR_logo_IIA-1-white.png`" alt="CNR IIA logo" />
                </a>
                <a title="ESA" href="http://www.esa.int" target="_blank"  data-tutorial-tag="header-logo-esa">
                    <img :src="`/img/ESA-logo-51-white.png`" alt="ESA logo" />
                </a>
            </div>
        </div>
        <div class="header__middle">
            <NuxtLink :title="siteName" :to="siteRoot()" data-tutorial-tag="header-logo-main">
                <img :src="siteLogo" :alt="siteName" />
            </NuxtLink>
        </div>
        <div class="header__right">
            <a v-if="isAtlantOsCommunityPortal()" title="iAtlantic" href="https://doi.org/10.3030/818123" target="_blank" class="header-logo-atlantos" data-tutorial-tag="header-logo-atlantos">
                <img :src="`/img/iAtlantic-Logo-Rev.png`" alt="iAtlantic" />
            </a>
            <a title="CNR_IIA" href="https://en.iia.cnr.it/" target="_blank" data-tutorial-tag="header-logo-cnr">
                <img :src="`/img/CNR_logo_IIA-1-white.png`" alt="CNR IIA logo" />
            </a>
            <a title="ESA" href="http://www.esa.int" target="_blank" data-tutorial-tag="header-logo-esa">
                <img :src="`/img/ESA-logo-51-white.png`" alt="ESA logo" />
            </a>
            <div class="header__language-switcher" v-click-outside="closeLangContainer"  data-tutorial-tag="header-language-switch">
                <button @click="toggleLangContainer()" :title="$tc('menu.languageToggle')":aria-label="$tc('menu.languageToggle')">
                    <i class="icomoon-language"></i>
                    <span>{{ languageMap[langLocale] }}</span>
                </button>
                <button @click="toggleLangContainer()" :title="$tc('menu.languageToggle')":aria-label="$tc('menu.languageToggle')">
                    <span></span>
                </button>
                <div class="options">
                    <CollapseTransition>
                        <div v-show="langContainerActive" class="options__wrapper">
                            <span :class="{ active: (langLocale === 'en') }" @click="changeLangLocale('en')">English</span>
                            <span :class="{ active: (langLocale === 'pl') }" @click="changeLangLocale('pl')">Polish</span>
                            <span :class="{ active: (langLocale === 'es') }" @click="changeLangLocale('es')">Spanish</span>
                            <span :class="{ active: (langLocale === 'zh') }" @click="changeLangLocale('zh')">Chinese</span>
                            <span :class="{ active: (langLocale === 'fr') }" @click="changeLangLocale('fr')">French</span>
                            <span :class="{ active: (langLocale === 'ru') }" @click="changeLangLocale('ru')">Russian</span>
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
import { SearchEngineGetters } from '~/store/searchEngine/search-engine-getters';
import CollapseTransition from '@/plugins/CollapseTransition';

@Component({
    components: {
        CollapseTransition
    }
})
export default class HeaderComponent extends Vue {

    public mobileLogosAnimation: any = null;

    public languageMap: { [key: string]: string } = {
        en: 'English',
        pl: 'Polish',
        es: 'Spanish',
        fr: 'French',
        zh: 'Chinese',
        ru: 'Russian'
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
            this.$i18n.setLocale(locale)
            this.$i18n.setLocaleCookie(locale)
            this.$store.dispatch(GeneralActions.setLangLocale, locale);
        }
        this.toggleLangContainer();
    }

    public isAtlantOsCommunityPortal() {
        return window.location.pathname.indexOf('/community/aaod') > -1;
    }

    public runMobileLogosAnimation() {
        console.log(111)
        const mobileLogos: any = document.querySelector('.header__left__slider')?.querySelectorAll('a');
        if (this.mobileLogosAnimation) {
            clearInterval(this.mobileLogosAnimation);
        }
        if (window.innerWidth <= 480) {
            let i = 0;
            this.mobileLogosAnimation = setInterval(() => {
                for (const logo of mobileLogos) {
                    logo.style.display = 'none';
                }
                mobileLogos[i].style.display = 'block';
                i++;
                if (i >= mobileLogos.length) {
                    i = 0;
                }
            }, 2000);
        } else {
            for (const logo of mobileLogos) {
                logo.style.display = '';
            }
        }
    }

    public siteRoot() {
        if (!this.siteUrl || this.siteUrl === 'global') {
            return '/'
        }
        return '/community/' + this.siteUrl
    }

     private mounted() {
        if (this.$cookies.get('i18n_redirected')) {
            this.$store.dispatch(GeneralActions.setLangLocale, this.$cookies.get('i18n_redirected'));
        } else {
            this.$store.dispatch(GeneralActions.setLangLocale, 'en');
        }

        this.runMobileLogosAnimation();
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
    background: linear-gradient(black -30%, transparent 100%);

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

    &.atlantos {
        .header__left {
            width: 496px;

            @media (max-width: $breakpoint-xxl) {
                width: auto;
            }
        }

        .header__right {
            &>* {
                @media (max-width: $breakpoint-xxl) {
                    margin-right: 30px;
                }
            }

            .header-logo-atlantos {
                @media (max-width: $breakpoint-md) {
                    display: none;
                }
            }

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
    .header__right > a {
        @media (max-width: $breakpoint-xs) {
            display: none;
        }
    }

    @media (min-width: $breakpoint-lg) {
        .header__middle {
            img {
                height: auto !important;
                max-width: 200px;
                max-height: 60px;
                margin: -15px auto;
            }
        }

        .header__left {
            width: 314px;
        }
    }

    .header__left {
        &__slider {
            >* {
                display: none;
                &:first-child {
                    display: block;
                }

                @media (min-width: $breakpoint-xs) {
                    display: none !important;
                    &:first-child {
                        display: block !important;
                    }
                }
            }
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
