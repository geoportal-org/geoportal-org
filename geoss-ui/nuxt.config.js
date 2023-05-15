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
        '@nuxtjs/i18n',
        'cookie-universal-nuxt',
    ],

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

    env: {
        baseUrl: process.env.NUXT_ENV_BASE_URL || 'https://gpp.uat.esaportal.eu',
        adminUrl: process.env.NUXT_ENV_ADMIN_URL || 'https://gpp-admin.uat.esaportal.eu'
    },

    // ssr: true,
    // target: 'server',
}
