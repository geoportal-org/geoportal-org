
<template>
    <div class="community-portal-configuration">
        <Notification />
        <div class="my-workspace-header">
            Community Portal Setup
        </div>
        <div class="community-portal-configuration__wrapper">
            <div class="my-workspace-tab my-workspace-content community-portal-configuration__sub">

                <template v-if="step === 0">
                    <img class="creator__logo" src="/img/geoss-logo-blue.png" />
                    <h1 class="creator__header">Community Portal Setup</h1>
                    <p>
                        Welcome to the Community portal configuration tool. Please follow the displayed steps to perform the
                        configuration correcty.
                    </p>
                    <p>
                        For a detailed explanation of the configuration process, see the instructions file included with the
                        installation files.
                    </p>
                    <button class="green-btn-default" @click="next()">Start Configuration</button>
                    <p>To get more information: <a :href="pdfManualUrl" target="_blank">View the manual</a></p>
                </template>

                <template v-if="step === 1">
                    <img class="creator__logo" src="/img/geoss-logo-blue.png" />
                    <h1 class="creator__header">Admin login</h1>
                    <p>In order to start a configuration, you need to log in as admin.</p>
                    <div class="creator__field required">
                        <label>Login: <span>*</span></label>
                        <input v-model="username" placeholder="Type here..." type="text" ref="login" />
                    </div>
                    <div class="creator__field required">
                        <label>Password: <span>*</span></label>
                        <input v-model="password" placeholder="Type here..." type="password" ref="password" />
                    </div>
                    <button class="green-btn-default" @click="signIn()">Log in</button>
                    <p>To get more information: <a :href="pdfManualUrl" target="_blank">View the manual</a></p>
                </template>

                <template v-if="step === 2">
                    <img class="creator__logo" src="/img/geoss-logo-blue.png" />
                    <h1 class="creator__header">Configuration</h1>
                    <div class="creator__field required">
                        <label>Name of the Community Portal: <span>*</span></label>
                        <input placeholder="Type here..." type="text" ref="site_name" />
                    </div>
                    <div class="creator__field required">
                        <label>Upload a logo: <span>*</span></label>
                        <input type="file" ref="site_logo" name="site_logo" accept="image/png, image/jpeg, image/svg" />
                    </div>
                    <p class="creator__hint">Additional frontend customization can be conducted via the source code.</p>
                    <div class="creator__buttons">
                        <button class="green-btn-default inverted" @click="prev()">Back</button>
                        <button class="green-btn-default" @click="saveSiteSettings()">Next</button>
                    </div>
                </template>

                <template v-if="step === 3">
                    <img class="creator__logo" src="/img/geoss-logo-blue.png" />
                    <h1 class="creator__header">Choose a view</h1>

                    <p>You can select a view (catalogue) to narrow down the search results of data sets on your portal by
                        default. The default view will be set if you don't indicate any of the catalogues.</p>
                    <div class="view-options">
                        <div class="creator__field radio">
                            <input type="radio" id="default" name="default-view" checked="true" ref="view" value=""
                                data-id="0" data-default="true">
                            <label for="default">Default</label>
                        </div>
                        <div class="creator__field radio" v-for="view of views" :key="view.value">
                            <input type="radio" :id="view.value" name="default-view" ref="view" :value="view.value"
                                 :data-id="view.id" :data-name="view.title"
                                :data-value="view.value">
                            <label :for="view.value">{{ view.title }}</label>
                        </div>
                    </div>
                    <div class="creator__buttons">
                        <button class="green-btn-default inverted" @click="prev()">Back</button>
                        <button class="green-btn-default" @click="saveDefaultView()">Next</button>
                    </div>
                </template>

                <template v-if="step === 4">
                    <img class="creator__logo" src="/img/geoss-logo-blue.png" />
                    <h1 class="creator__header">Finish</h1>
                    <p>
                        Your community portal has been configured successfully.
                    </p>
                    <p>
                        If you want your site to become an officially recognized GEOSS Community Portal, apply for the
                        registration at the link below.
                    </p>
                    <p>
                        Remember you can always access the configuraion panel using your login and password at this link: <a
                            :href="adminUrl" target="_blank">Reconfigure</a>
                    </p>
                    <NuxtLink to="/">
                        <button class="green-btn-default">Finish and close</button>
                    </NuxtLink>
                    <p>Any additional questions can be asked on our <a href="/help-desk" target="_blank">Contact page</a>
                    </p>
                </template>
            </div>
        </div>
    </div>
</template>

<script>
import GeossSearchApiService from '@/services/geoss-search.api.service';
import WebSettingsAPI from '@/api/webSettings'
import ContentAPI from '@/api/content'
import defaultViews from '@/data/views'
import NotificationService from '@/services/notification.service';
import apiClient from '@/api/apiClient'
import { toStringHDMS } from 'ol/coordinate';

export default {
    layout() {
        return 'wizard'
    },
    data() {
        return {
            step: 0,
            settings: null,
            siteData: null,
            views: [],
            username: '',
            password: '',
            defaultView: null,
            baseUrl: this.$config.baseUrl,
            adminUrl: this.$config.adminUrl,
            defaultSiteId: 0,
            newSiteUrl: '',
            newSiteId: '',
            pdfManualUrl: this.$config.pdfManualUrl,
        }
    },
    methods: {
        next() {
            ++this.step
            if (this.step === 1 && this.$nuxt.$auth.loggedIn) {
                this.step = 2;
            }
        },
        prev() {
            --this.step
            if (this.step === 1 && this.$nuxt.$auth.loggedIn) {
                this.step = 0;
            }
        },
        async saveSiteSettings() {
            if (!this.$refs.site_name || this.$refs.site_name === '' || !this.$refs.site_logo || !this.$refs.site_logo.files[0]) {
                NotificationService.show(
                    'Community Portal Configuration',
                    'Please provide both: Site name and Site logo',
                    10000,
                    null,
                    9999,
                    'info'
                );
                return false
            } else {
                try {
                    const formData = new FormData();
                    formData.append("model", JSON.stringify({
                        name: this.$refs.site_name.value,
                        url: this.newSiteUrl,
                        logoDocumentId: 0
                    }))
                    const file = this.$refs.site_logo.files[0];
                    formData.append("files", file, file.name);
                    const response = await apiClient.$post(`${this.adminUrl}/contents/rest/site`, formData, {
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': $nuxt.$auth.getToken('keycloak') ? $nuxt.$auth.getToken('keycloak') : ''
                        }
                    }).catch(error => {
                        console.error('error ', error);
                        throw new Error(error.response.data.detail)
                    })
                    this.newSiteId = response.id;
                    this.next();
                } catch(error) {
                    NotificationService.show(
                        'Community Portal Configuration',
                        error,
                        10000,
                        null,
                        9999,
                        'error'
                    );
                }
            }
        },
        async saveDefaultView() {
            const selectedView = this.$refs.view.filter(e => e.checked)[0];
            if (!selectedView) {
                this.next()
                return
            }

            if (this.defaultView) {
                const viewData = {
                    label: this.defaultView.label,
                    value: this.defaultView.value,
                    title: this.defaultView.title,
                    defaultOption: false,
                }
                await WebSettingsAPI.updateView(this.defaultView.id, viewData, $nuxt.$auth.getToken('keycloak'));
            }

            const id = selectedView.attributes['data-id'].value
            if (id * 1) {
                const value = selectedView.attributes['data-value'].value
                const title = selectedView.attributes['data-name'].value
                const viewData = {
                    label: title,
                    value,
                    title,
                    defaultOption: true,
                }

                await WebSettingsAPI.updateView(id, viewData, $nuxt.$auth.getToken('keycloak'));
                this.next()
            }
        },
        async addDefaultViews() {
            for (const view of defaultViews.views) {
                const viewData = {
                    label: view.label,
                    value: view.value,
                    title: view.title,
                    defaultOption: false,
                }
                await WebSettingsAPI.setView(viewData, $nuxt.$auth.getToken('keycloak'));
            }
            this.views = await GeossSearchApiService.getViewsOptions()
        },
        getCurrentDefaultView() {
            for (const view of this.views) {
                if (view.defaultOption) {
                    this.defaultView = view
                }
            }
        },
        async signIn() {
            if (!this.$refs.login || this.$refs.login.value === '' || !this.$refs.password || this.$refs.password.value === '') {
                NotificationService.show(
                    'Community Portal Configuration',
                    'Please provide correct credentials',
                    10000,
                    null,
                    9999,
                    'info'
                );
                return false
            }

            const formData = new URLSearchParams();
            formData.append('grant_type', 'password');
            formData.append('client_id', this.$config.keycloakClientId);
            formData.append('username', this.username);
            formData.append('password', this.password);
            formData.append('scope', 'openid profile email roles');

            const response = await apiClient.$post(`${this.$config.keycloakBaseUrl}/protocol/openid-connect/token`, formData.toString(), {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                },
                json: true,
            }).catch(e => {
                console.error('error ', e);
                NotificationService.show(
                    'Community Portal Configuration',
                    'Please provide correct credentials',
                    10000,
                    null,
                    9999,
                    'info'
                );
                return false;
            })

            if (response && response.access_token && response.access_token !== '') {
                $nuxt.$auth.setToken(this.$store.state.auth.strategy, `Bearer ${response.access_token}`)
                $nuxt.$auth.strategy._setToken(`Bearer ${response.access_token}`)
                this.next();
            }
        }
    },
    watch: {
        async step() {
            if (this.step === 2) {
                this.views = await GeossSearchApiService.getViewsOptions()
                if (!this.views.length) {
                    this.addDefaultViews();
                } else {
                    this.getCurrentDefaultView();
                }
                this.settings = await WebSettingsAPI.getSiteSettingsRaw(this.defaultSiteId)
                this.siteData = await ContentAPI.getSiteByUrl(this.$route.params.siteurl)
            }
        },
    },
    mounted() {
        if (this.$route.query && this.$route.query.siteUrl && this.$route.query.siteUrl !== '') {
            this.newSiteUrl = this.$route.query.siteUrl
        }
    }
}
</script>

<style lang="scss" scoped>
.community-portal-configuration {
    height: calc(100% - 70px);

    &__sub {
        display: flex;
        flex-direction: column;
        align-items: center;
        max-height: 100%;
    }

    &__wrapper {
        display: flex;
        align-items: center;
        justify-content: center;
        height: 100%;
    }

    .creator {
        &__logo {
            display: inline-block;
            margin: 15px;
        }

        &__header {
            font-size: 2em;
            margin-bottom: 15px;
        }

        &__field {
            margin-bottom: 15px;

            label {
                display: block;
                font-weight: 700;
                font-size: 0.85em;
                margin: 3px 0;
            }

            input {
                display: block;
                border: 1px solid #ccc;
                padding: 3px 5px;
                width: 400px;
            }

            &.required {
                span {
                    color: $red;
                }
            }

            &.radio {
                display: flex;
                align-items: center;

                input {
                    width: 30px;
                }
            }
        }

        &__hint {
            padding: 10px;
            font-size: 0.85em;
            background: #EEF4F8;
        }

        &__buttons {
            width: 100%;
            display: flex;
            justify-content: space-between;
        }
    }

    p {
        margin-bottom: 15px;
        max-width: 500px;
    }

    .green-btn-default {
        margin: 15px 0 30px;

        &.inverted {
            background: white;
            border: 1px solid #209d90;
            color: #209d90;
        }
    }

    .view-options {
        display: block;
        width: 100%;
    }

}
</style>
