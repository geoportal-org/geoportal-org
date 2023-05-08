
<template>
    <div class="community-portal-configuration">
        <div class="my-workspace-header">
            Mirror Site Setup
        </div>
        <div class="community-portal-configuration__wrapper">
            <div class="my-workspace-tab my-workspace-content community-portal-configuration__sub">

                <template v-if="step === 0">
                    <img class="creator__logo" src="/img/geoss-logo-blue.png" />
                    <h1 class="creator__header">Mirror Site Setup</h1>
                    <p>
                        Welcome to the Community portal configuration tool. Please follow the displayed steps to perform the
                        configuration correcty.
                    </p>
                    <p>
                        For a detailed explanation of the configuration process, see the instructions file included with the
                        installation files.
                    </p>
                    <button class="green-btn-default" @click="next()">Start Configuration</button>
                    <p>To get more information: <a href="#">View the manual</a></p>
                </template>

                <template v-if="step === 1">
                    <img class="creator__logo" src="/img/geoss-logo-blue.png" />
                    <h1 class="creator__header">Admin login</h1>
                    <p>In order to start a configuration, you need to log in as admin.</p>
                    <div class="creator__field required">
                        <label>Login: <span>*</span></label>
                        <input placeholder="Type here..." type="text" />
                    </div>
                    <div class="creator__field required">
                        <label>Password: <span>*</span></label>
                        <input placeholder="Type here..." type="password" />
                    </div>
                    <button class="green-btn-default" @click="next()">Log in</button>
                    <p>To get more information: <a href="#">View the manual</a></p>
                </template>

                <template v-if="step === 2">
                    <img class="creator__logo" src="/img/geoss-logo-blue.png" />
                    <h1 class="creator__header">Configuration</h1>
                    <div class="creator__field required">
                        <label>Name of the Mirror Site: <span>*</span></label>
                        <input placeholder="Type here..." type="text" />
                    </div>
                    <div class="creator__field required">
                        <label>Upload a logo: <span>*</span></label>
                        <input type="file" />
                    </div>
                    <p class="creator__hint">Additional frontend customization can be conducted via the source code.</p>
                    <div class="creator__buttons">
                        <button class="green-btn-default inverted" @click="prev()">Back</button>
                        <button class="green-btn-default" @click="next()">Next</button>
                    </div>
                </template>

                <template v-if="step === 3">
                    <img class="creator__logo" src="/img/geoss-logo-blue.png" />
                    <h1 class="creator__header">Choose a view</h1>

                    <p>You can select a view (catalogue) to narrow down the search results of data sets on your portal by
                        default. The default view will be set if you don't indicate any of the catalogues.</p>
                    <div class="view-options">
                        <div class="creator__field radio">
                            <input type="radio" id="default" name="default-view" checked="true">
                            <label for="default">Default</label>
                        </div>
                        <div class="creator__field radio" v-for="view of views" :key="view">
                            <input type="radio" :id="view.value" name="default-view">
                            <label :for="view.value">{{ view.title }}</label>
                        </div>
                    </div>
                    <div class="creator__buttons">
                        <button class="green-btn-default inverted" @click="prev()">Back</button>
                        <button class="green-btn-default" @click="next()">Next</button>
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
                            href="https://gpp-admin.devel.esaportal.eu" target="_blank">Reconfigure</a>
                    </p>
                    <NuxtLink to="/">
                        <button class="green-btn-default">Finish and close</button>
                    </NuxtLink>
                    <p>Any additional questions can be asked on our <a href="#">Contact page</a></p>
                </template>
            </div>
        </div>
    </div>
</template>

<script>
import GeossSearchApiService from '@/services/geoss-search.api.service';

export default {
    layout() {
        return 'wizard'
    },
    data() {
        return {
            step: 0,
            views: []
        }
    },
    methods: {
        next() {
            ++this.step
        },
        prev() {
            --this.step
        }
    },
    async mounted() {
        this.views = await GeossSearchApiService.getViewsOptions();
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
