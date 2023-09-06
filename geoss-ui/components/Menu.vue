<template>
    <nav class="menu" :class="{ alpha: alpha, active: menuOpened }" :style="{ 'padding-top': `${paddingTop}px` }"
        v-click-outside="{ fn: closeMenu, excludeSelectors: '.header, .tutorial-tag, .tutorial-mode, .tutorial-off' }">
        <div class="menu__inner-wrapper">
            <template v-for="(route, index) of routes">
                <div :key="route.title + '-separator'" v-if="index === 4" class="menu__separator"></div>
                <div class="menu__item" :class="{ active: route === activeLinksExpander }" :key="index"
                    :data-tutorial-tag="(index === 6 && isSignedIn) ? 'header-menu-item-8' : 'header-menu-item-' + (index + 1)">

                    <template v-if="!isMyWorkspace(route)">
                        <div v-if="route.links && route.links.length" class="menu__links-expander"
                            @click="toggleMenuSublinks(route)">
                            <img :src="route.imgURL" :alt="route.title" />
                            <span>{{ route.title.toUpperCase() }}</span>
                        </div>
                    </template>
                    <template v-else>
                        <div v-if="isSignedIn && route.links && route.links.length" class="menu__links-expander"
                            @click="toggleMenuSublinks(route)">
                            <img :src="route.imgURL" :alt="route.title" />
                            <span>{{ route.title.toUpperCase() }}</span>
                        </div>
                        <div v-else class="menu__link" @click="signIn()">
                            <img :src="route.imgURL" :alt="route.title" />
                            <span>{{ route.title.toUpperCase() }}</span>
                        </div>
                    </template>

                    <div v-if="route.links" :key="route.title" class="md-hidden-up">
                        <CollapseTransition>
                            <div v-show="route === activeLinksExpander">
                                <div class="menu__links-expanded">
                                    <template v-for="(subroute, subindex) of route.links">
                                        <a v-if="isExternal(subroute.link)" class="menu__sublink" :key="subindex"
                                            :to="subroute.link" target="_blank">
                                            {{ subroute.title }}
                                        </a>
                                        <NuxtLink v-else class="menu__sublink" :key="subroute.link"
                                            :to="subroute.link || ''">
                                            {{ subroute.title }}
                                        </NuxtLink>
                                    </template>
                                </div>
                            </div>
                        </CollapseTransition>
                    </div>
                    <a v-if="(!route.links || !route.links.length) && isExternal(route.link)" :href="route.link"
                        class="menu__link" target="_blank">
                        <img :src="route.imgURL" :alt="route.title" />
                        <span>{{ route.title }}</span>
                    </a>
                    <NuxtLink v-if="(!route.links || !route.links.length) && !isExternal(route.link)" :to="route.link || ''"
                        class="menu__link">
                        <img :src="route.imgURL" :alt="route.title" />
                        <span>{{ route.title }}</span>
                    </NuxtLink>
                </div>
            </template>
            <div class="menu__item">
                <a v-if="isSignedIn" class="menu__link" target="_blank" @click="signOff()">
                    <img src="/svg/sign-out.svg" :title="$tc('menu.signOff')" />
                    <span>{{ $tc('menu.signOff') }}</span>
                </a>
                <a v-else class="menu__link" target="_blank" @click="signIn()">
                    <img src="/svg/sign-in.svg" :title="$tc('menu.signIn')" />
                    <span>{{ $tc('menu.signIn') }}</span>
                </a>
            </div>
        </div>
        <div class="menu__inner-wrapper md-hidden-down">
            <div v-for="(route, index) of routes" :key="index" class="menu__item">
                <CollapseTransition v-if="route.links">
                    <div v-show="route === activeLinksExpander" class="menu__links-expanded">
                        <template v-for="(subroute, subindex) of route.links">
                            <a v-if="isExternal(subroute.link)" class="menu__sublink" :key="subindex" :href="subroute.link"
                                :data-tutorial-tag="`header-menu-subitem-${index + 1}-${subindex + 1}`" target="_blank">
                                {{ subroute.title }}
                            </a>
                            <NuxtLink v-else class="menu__sublink" :key="subroute.link" :to="subroute.link"
                                :data-tutorial-tag="`header-menu-subitem-${index + 1}-${subindex + 1}`">
                                {{ subroute.title }}
                            </NuxtLink>
                        </template>
                    </div>
                </CollapseTransition>
            </div>
            <!-- Sign-in sub-menu expander -->
            <div class="menu__item"></div>
        </div>
    </nav>
</template>

<script lang="ts">
// @ts-nocheck
import { Component, Vue, Prop, Watch } from 'nuxt-property-decorator';

import { MenuLinksWrapper } from '@/interfaces/Menu';
import { MenuGetters } from '@/store/menu/menu-getters';
import { MenuActions } from '@/store/menu/menu-actions';
import { GeneralGetters } from '@/store/general/general-getters';
import TutorialTagsService from '@/services/tutorial-tags.service';
import CollapseTransition from '@/plugins/CollapseTransition';

import MenuAPI from '@/api/menu'
import apiClient from '@/api/apiClient'

@Component({
    components: {
        CollapseTransition
    }
})
export default class MenuComponent extends Vue {
    @Prop({ default: true, type: Boolean }) public alpha!: boolean;
    @Prop({ type: Number }) public paddingTop!: number;

    public activeLinksExpander: MenuLinksWrapper | null = null;
    public routes: any = []; // : Array<MenuLink | MenuLinksWrapper> = [];

    get isSignedIn() {
        return this.$auth.loggedIn;
    }

    get menuOpened() {
        const menuOpened = this.$store.getters[MenuGetters.opened];
        TutorialTagsService.refreshTagsGroup('header-menu-item', menuOpened, 450);
        TutorialTagsService.refreshTagsGroup('header-menu-subitem', menuOpened, 450);
        return menuOpened;
    }

    set menuOpened(value) {
        if (this.menuOpened !== value) {
            this.$store.dispatch(MenuActions.setOpened, value);
        }
    }

    get langLocale() {
        return this.$store.getters[GeneralGetters.langLocale];
    }

    get route() {
        return this.$route;
    }

    public closeMenu() {
        this.menuOpened = false;
    }

    public toggleMenuSublinks(route: MenuLinksWrapper) {
        if (this.activeLinksExpander === route) {
            this.activeLinksExpander = null;
        } else {
            this.activeLinksExpander = route;
        }
        TutorialTagsService.refreshTagsGroup('header-menu-subitem', this.activeLinksExpander, 450);
    }

    public isExternal(link: string) {
        if (link && link.indexOf('/') !== 0 && link.indexOf('http') === 0) {
            return true;
        } else {
            return false;
        }
    }

    public async signOff() {
        const logoutUrl = new URL(this.$auth.strategies.keycloak.options.logout_endpoint);
        const refreshToken = this.$auth.getRefreshToken('keycloak').replace('Bearer ', '')
        const formData = new URLSearchParams();
        formData.append('refresh_token', refreshToken);
        formData.append('client_id', this.$config.keycloakClientId);
        await apiClient.$post(logoutUrl, formData.toString(), {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
            },
            json: true,
        })
        await this.$auth.logout();
        this.$cookies.remove('auth._token.keycloak');
        localStorage.removeItem('auth._token.keycloak');
    }

    public signIn() {
        this.$auth.loginWith('keycloak')
    }

    public isMyWorkspace(route) {
        return route.imgURL.indexOf('my-workspace') > 0
    }

    @Watch('langLocale')
    private async onLangLocaleChange() {
        this.routes = await MenuAPI.getMenu();
    }

    @Watch('route')
    private onRouteChange() {
        this.closeMenu();
    }

    private async mounted() {
        this.routes = await MenuAPI.getMenu();
        this.closeMenu();
    }
}
</script>

<style lang="scss" scoped>
$img_height: 50px;

.menu {
    background: $blue;
    position: fixed;
    top: 0;
    left: 0;
    width: 100vw;
    z-index: 301;
    padding-top: 130px;
    transform: translateY(-100%);
    opacity: 0;
    transition: transform 450ms ease-in-out, opacity 0ms 450ms ease-in-out;

    @media(max-width: $breakpoint-md) {
        transform: translateX(-100%);
        width: auto;
        height: 100vh;
        background: $blue !important;
        width: 320px;
        padding-top: 80px !important;
    }

    @media(max-width: $breakpoint-xxs) {
        width: 100vw;
    }

    &.alpha {
        background: rgba($blue, 0.9);
    }

    &.active {
        opacity: 1;
        transform: translateY(0);
        transition: transform 450ms ease-in-out;

        @media(max-width: $breakpoint-md) {
            overflow: auto;
            transform: translateX(0);
        }
    }

    &__inner-wrapper {
        display: flex;
        justify-content: space-around;
        position: relative;
        margin: 0 50px;

        @media(max-width: $breakpoint-xl) {
            margin: 0 30px;
        }

        @media(max-width: $breakpoint-lg) {
            margin: 0 15px;
        }

        @media(max-width: $breakpoint-md) {
            width: auto;
            flex-direction: column;
            margin: 0;
        }
    }

    &__separator {
        flex: .5;
        position: relative;

        @media(max-width: $breakpoint-xl) {
            flex: .1;
        }

        @media(max-width: $breakpoint-lg) {
            display: none;
        }

        @media(max-width: $breakpoint-md) {
            display: block;
            padding: 15px;
        }

        &:before {
            width: 1px;
            height: 100%;
            display: inline-block;
            background: #fff;
            content: '';
            position: absolute;
            left: 50%;
            top: 50%;
            transform: translate(-50%, -70%);

            @media(max-width: $breakpoint-md) {
                width: calc(100% - 30px);
                height: 1px;
                transform: translate(-50%, -50%);
            }
        }
    }

    &__item {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        color: white;
        flex: 1;

        @media(max-width: $breakpoint-md) {
            align-items: flex-start;
            padding-bottom: 20px;
            padding-left: 35px;
            padding-right: 35px;
            padding-top: 20px;
            width: 100%;
        }

        &.active {
            @media(max-width: $breakpoint-md) {
                background: radial-gradient(#205B8A, #163F5F);
            }

            .menu__links-expander {
                &:before {
                    transform: rotate(-45deg);
                }

                &:after {
                    transform: rotate(45deg);
                }

                @media(max-width: $breakpoint-md) {
                    &:before {
                        transform: rotate(45deg);
                    }

                    &:after {
                        transform: rotate(-45deg);
                    }
                }
            }
        }

        span {
            text-align: center;
        }

        img {
            height: $img_height;
            margin-bottom: 20px;

            @media(max-width: $breakpoint-md) {
                margin-bottom: 0;
                margin-right: 20px;
                max-height: 35px;
                max-width: 35px;
                position: absolute;
                left: 0;
                top: 50%;
                transform: translateY(-50%);
            }
        }
    }

    &__links-expander,
    &__link {
        position: relative;
        display: flex;
        flex-direction: column;
        align-items: center;
        cursor: pointer;
        padding-bottom: 40px;

        @media(max-width: $breakpoint-md) {
            padding-bottom: 0;
        }
    }

    &__links-expander {
        @media(max-width: $breakpoint-md) {
            padding-left: 70px;
            padding-right: 40px;
            width: 100%;
            align-items: flex-start;
        }

        &:before,
        &:after {
            content: '';
            display: block;
            position: absolute;
            bottom: 25px;
            top: auto;
            width: 7px;
            height: 2px;
            left: calc(50% - 5px);
            background: white;
            transform: rotate(45deg);

            @media(max-width: $breakpoint-md) {
                right: 0;
                left: auto;
                bottom: 50%;
                transform: rotate(-45deg);
            }
        }

        &:after {
            left: calc(50% - 1px);
            transform: rotate(-45deg);

            @media(max-width: $breakpoint-md) {
                left: auto;
                right: 5px;
                transform: rotate(45deg);
            }
        }

        span {
            font-size: 14px;
        }
    }

    &__sublink {
        display: block;
        color: white !important;
        position: relative;
        padding-bottom: 5px;

        &:before,
        &:after {
            content: '';
            position: absolute;
            left: -12px;
            top: 6px;
            width: 6px;
            height: 2px;
            background: white;
            transform: rotate(45deg);
        }

        &:after {
            top: 10px;
            transform: rotate(-45deg);
        }
    }

    &__link {
        text-transform: uppercase;
        font-size: 14px;
        color: white !important;
        display: flex;
        flex-direction: column;
        align-items: center;
        position: relative;

        @media(max-width: $breakpoint-md) {
            flex-direction: row;
            padding-left: 70px;
        }

        &:hover {
            text-decoration: none;

            span {
                text-decoration: underline;
            }
        }
    }

    &__links-expanded {
        @media(max-width: $breakpoint-md) {
            padding: 30px 0;
            margin-left: 15px;
        }

        &:after {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            height: 0;
            width: 100%;
            border-top: 1px solid white;

            @media(max-width: $breakpoint-md) {
                border-top: none;
            }
        }

        .menu__sublink:first-child {
            margin-top: 15px;

            @media(max-width: $breakpoint-md) {
                margin-top: 0;
            }
        }

        .menu__sublink:last-child {
            margin-bottom: 15px;

            @media(max-width: $breakpoint-md) {
                margin-bottom: 0;
            }
        }
    }
}
</style>
