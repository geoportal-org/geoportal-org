<template>
    <main>
        <RocketSpinner />
        <Header />
        <Menu />

        <UserWelcome />
        <Notification />
        <Spinner />
        <Popup />
        <ImagePreview />
        <PrivacyPolicy />
        <!-- <SendFeedback /> -->
        <TakeATour />
        <!-- <TutorialTags /> -->

        <div class="sub-page">
            <div class="sub-page__content">
                <Nuxt />
            </div>
        </div>

        <div class="geoss-data-pickers"></div>
        <portal-target name="custom-select-container"></portal-target>
    </main>
</template>

<script>
import { SearchEngineActions } from '@/store/searchEngine/search-engine-actions';
import { GeneralApiService } from '@/services/general.api.service';
import { GeneralActions } from '@/store/general/general-actions';
import { GeneralGetters } from '@/store/general/general-getters';
import { AppVueObj } from '@/data/global';

export default {
    computed: {
        storeInitialized() {
            return this.$store.getters[GeneralGetters.storeInitialized];
        }
    },

    mounted() {
        AppVueObj.app.$store = this.$store;

        if (!this.storeInitialized) {
            const promises = [
                GeneralApiService.getSiteSettings(),
                GeneralApiService.getSearchSettings()
            ];

            Promise.all(promises).then(([siteSettings, searchSettings]) => {
                if (siteSettings) {
                    if (siteSettings.name && siteSettings.name !== '') {
                        this.$store.dispatch(SearchEngineActions.setSiteName, siteSettings.name);
                    }
                    if (siteSettings.logoUrl && siteSettings.logoUrl !== '') {
                        this.$store.dispatch(SearchEngineActions.setSiteLogo, siteSettings.logoUrl);
                    }
                    if (siteSettings.url && siteSettings.url !== '') {
                        this.$store.dispatch(SearchEngineActions.setSiteUrl, siteSettings.url);
                    }
                }
                if (searchSettings) {
                    if (searchSettings) {
                        this.$store.dispatch(SearchEngineActions.setDabDataProvidersUrl, searchSettings['dabDataProvidersUrl']);
                        this.$store.dispatch(SearchEngineActions.setTourUrl, searchSettings['tourUrl']);
                    }
                }
                this.$store.dispatch(GeneralActions.setStoreInitialized, true);
            });
        }

        const spinnerElem = document.querySelector('.earch-rocket-spinner');
        if (spinnerElem) {
            spinnerElem.classList.add('inactive');
        }
    }
}
</script>


<style lang="scss">
@import "~/assets/scss/reset.scss";
@import "~/assets/scss/general.scss";

body {
    @include regular-page-body;
}

.sub-page {
    @include regular-page;

    &__content {
        @include regular-page-content;
    }
}
</style>
