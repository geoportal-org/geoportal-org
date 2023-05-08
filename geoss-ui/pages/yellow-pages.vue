<template>
    <div class="yellow-pages">
        <client-only>

            <!-- <UserWelcome /> -->
            <Notification />
            <Spinner />
            <Popup />
            <ImagePreview />
            <PrivacyPolicy />
            <!-- <SendFeedback /> -->
            <TakeATour />
            <!-- <TutorialTags /> -->
            <div class="yellow-pages__sub" v-if="storeInitialized">
                <YellowPagesHeader />
                <YellowPagesProviders />
                <YellowPagesPagination />
            </div>
            <div class="geoss-data-pickers"></div>
            <portal-target name="custom-select-container"></portal-target>
        </client-only>
    </div>
</template>

<script lang="ts">
import { Component, Vue } from 'nuxt-property-decorator';
import { SearchEngineActions } from '@/store/searchEngine/search-engine-actions';
import { GeneralGetters } from '@/store/general/general-getters';
import { GeneralApiService } from '@/services/general.api.service';
import { GeneralActions } from '@/store/general/general-actions';
import { AppVueObj } from '@/data/global';

@Component({
    layout: 'default',
})
export default class App extends Vue {
    get storeInitialized() {
        return this.$store.getters[GeneralGetters.storeInitialized];
    }

    private mounted() {
        //      LiferayService.init();

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

        const spinnerElem: HTMLElement | null = document.querySelector('.earch-rocket-spinner');
        if (spinnerElem) {
            spinnerElem.classList.add('inactive');
        }

    }
}
</script>

<style lang="scss">
@import "@/assets/scss/reset.scss";
@import "@/assets/scss/general.scss";

body {
    @include regular-page-body;
}

// .yellow-pages {
//     @include regular-page;

//     &__content {
//         @include regular-page-content;
//     }
// }

.yellow-pages {
    height: 100%;
    display: flex;
    flex-direction: column;

    &__sub {
        height: calc(100% - 100px)
    }
}
</style>
