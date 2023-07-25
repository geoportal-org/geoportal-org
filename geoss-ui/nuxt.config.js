export default {
    server: {
        port: 3000, // default : 3000
        host: '0.0.0.0', // do not put localhost (only accessible from the host machine)
    },

    // Global page headers: https://go.nuxtjs.dev/config-head
    head: {
        title: 'GEOSS Portal',
        htmlAttrs: {
            lang: 'en',
        },
        meta: [
            { charset: 'utf-8' },
            {
                name: 'viewport',
                content: 'width=device-width, initial-scale=1',
            },
            { hid: 'description', name: 'description', content: '' },
            { name: 'format-detection', content: 'telephone=no' },
        ],
        link: [{ rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' }],
    },

    // Global CSS: https://go.nuxtjs.dev/config-css
    css: [
        '@/assets/scss/reset',
        '@/assets/scss/icons',
        '@/assets/scss/general',
        '@/assets/scss/animations',
    ],

    // Plugins to run before rendering page: https://go.nuxtjs.dev/config-plugins
    plugins: [
        { src: '~plugins/OpenLayers.ts', mode: 'client' },
        '~/plugins/AppVueObj.ts',
        '~/plugins/CollapseTransition.ts',
        '~/plugins/AxiosPort.ts',
        { src: '~/plugins/MatomoPlugin.js', ssr: false }
    ],

    // Auto import components: https://go.nuxtjs.dev/config-components
    components: [
        '@/components',
        '@/components/Bookmarks',
        '@/components/DatePicker',
        '@/components/ExtendedView',
        '@/components/Feedback',
        '@/components/Map',
        '@/components/Search',
        '@/components/Search/GeneralFilters',
        '@/components/Search/Results',
        '@/components/Slider',
        '@/components/YellowPages',
    ],

    // Modules for dev and build (recommended): https://go.nuxtjs.dev/config-modules
    buildModules: [
        // https://go.nuxtjs.dev/typescript
        '@nuxt/typescript-build',
        '@nuxtjs/style-resources',
    ],

    // Modules: https://go.nuxtjs.dev/config-modules
    modules: [
        // https://go.nuxtjs.dev/axios
        '@nuxtjs/axios',
        '@nuxtjs/auth',
        '@nuxtjs/i18n',
        'cookie-universal-nuxt',
    ],

    axios: {
        proxy: true
    },

    auth: {
        strategies: {
            local: false,
            keycloak: {
                _scheme: '~/scheme/runtimeOauth2',
            },
            oauth2: {
                _scheme: 'oauth2'
            },
        }
    },

    i18n: {
        locales: [
            {
                code: 'en',
                file: 'en.ts',
            },
            // {
            //     code: 'es',
            //     file: 'es.ts',
            // },
            // {
            //     code: 'fr',
            //     file: 'fr.ts',
            // },
            // {
            //     code: 'pl',
            //     file: 'pl.ts',
            // },
            // {
            //     code: 'ru',
            //     file: 'ru.ts',
            // },
            // {
            //     code: 'zh',
            //     file: 'zh.ts',
            // },
        ],
        lazy: false,
        langDir: 'translations/',
        defaultLocale: 'en',
        vueI18n: {
            fallbackLocale: 'en',
        },
    },

    styleResources: {
        scss: ['@/assets/scss/variables.scss'],
    },

    // Axios module configuration: https://go.nuxtjs.dev/config-axios
    axios: {
        // Workaround to avoid enforcing hard-coded localhost:3000: https://github.com/nuxt-community/axios-module/issues/308
        baseURL: '/',
    },

    // Build Configuration: https://go.nuxtjs.dev/config-build
    build: {
        vendor: ['ol'],
    },

    publicRuntimeConfig: {
        baseUrl: process.env.BASE_URL,
        adminUrl: process.env.ADMIN_URL,
        keycloakBaseUrl: process.env.KEYCLOAK_BASE_URL,
        keycloakClientId: process.env.KEYCLOAK_CLIENT_ID,
        matomoSiteId: process.env.MATOMO_SITE_ID,
        matomoToken: process.env.MATOMO_TOKEN,
        auth: {
            defaultStrategy: 'keycloak',
            strategies: {
                keycloak: {
                    authorization_endpoint: process.env.KEYCLOAK_BASE_URL + '/protocol/openid-connect/auth',
                    access_token_endpoint: process.env.KEYCLOAK_BASE_URL + '/protocol/openid-connect/token',
                    userinfo_endpoint: process.env.KEYCLOAK_BASE_URL + '/protocol/openid-connect/userinfo',
                    redirect_uri: undefined,
                    scope: ['openid', 'profile', 'email', 'roles'],
                    grant_type: 'authorization_code',
                    response_type: 'code',
                    client_id: process.env.KEYCLOAK_CLIENT_ID,
                }
            }
        }
    },

    // ssr: true,
    // target: 'server',
}
