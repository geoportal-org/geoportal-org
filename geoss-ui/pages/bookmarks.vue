<template>
    <div class="sub-page">
        <div class="sub-page__content">
            <div class="bookmarks_main">
                <BookmarksHeader />
                <BookmarksList />
                <BookmarksPagination />
            </div>
        </div>
    </div>
</template>

<script type="module">
import LiferayService from '~/services/liferay.service'
import { GeneralApiService } from '~/services/general.api.service'
import { SearchEngineActions } from '~/store/searchEngine/search-engine-actions'
import { MapActions } from '~/store/map/map-actions'
import { GeneralActions } from '~/store/general/general-actions'
import { SearchGetters } from '~/store/search/search-getters'
import { SearchActions } from '~/store/search/search-actions'
import { DataSources } from '~/interfaces/DataSources'

export default {
    layout() {
        return 'static'
    },
    data() {
        return {}
    },
    mounted() {
        LiferayService.init()
        const spinnerElem = document.querySelector('.earch-rocket-spinner')
        if (spinnerElem) {
            spinnerElem.classList.add('inactive')
        }

        const promises = [
            GeneralApiService.getSiteSettings(),
            GeneralApiService.getSearchSettings(),
        ]

        Promise.all(promises).then(([siteSettings, searchSettings]) => {
            if (siteSettings) {
                if (siteSettings.name && siteSettings.name !== '') {
                    this.$store.dispatch(
                        SearchEngineActions.setSiteName,
                        siteSettings.name
                    )
                }
                if (siteSettings.logoUrl && siteSettings.logoUrl !== '') {
                    this.$store.dispatch(
                        SearchEngineActions.setSiteLogo,
                        siteSettings.logoUrl
                    )
                }
                if (siteSettings.url && siteSettings.url !== '') {
                    this.$store.dispatch(
                        SearchEngineActions.setSiteUrl,
                        siteSettings.url
                    )
                }
                if (
                    siteSettings.defaultDataSource &&
                    siteSettings.defaultDataSource !== ''
                ) {
                    if (!this.$store.getters[SearchGetters.dataSource]) {
                        this.$store.dispatch(
                            SearchEngineActions.setDefaultSourceName,
                            siteSettings.defaultDataSource
                        )
                        this.$store.dispatch(SearchActions.setDataSource, {
                            value: DataSources.DAB,
                            checkDefault: true,
                        })
                    }
                } else {
                    this.$store.dispatch(SearchActions.setDataSource, {
                        value: DataSources.DAB,
                    })
                }
                if (siteSettings.mapZoom) {
                    this.$store.dispatch(
                        MapActions.setInitialZoom,
                        siteSettings.mapZoom
                    )
                }
                if (siteSettings.longitude && siteSettings.latitude) {
                    this.$store.dispatch(MapActions.setCenter, [
                        siteSettings.longitude,
                        siteSettings.latitude,
                    ])
                }
            }
            if (searchSettings) {
                this.$store.dispatch(
                    SearchEngineActions.setDabBaseUrl,
                    searchSettings['dabBaseUrl']
                )
                this.$store.dispatch(
                    SearchEngineActions.setDabDataProvidersUrl,
                    searchSettings['dabDataProvidersUrl']
                )
                this.$store.dispatch(
                    SearchEngineActions.setInternalOpenSearchUrl,
                    searchSettings['geossCrOpensearchUrl']
                )

                this.$store.dispatch(
                    SearchEngineActions.setW3wKey,
                    searchSettings['w3wKey']
                )
                this.$store.dispatch(
                    SearchEngineActions.setTourUrl,
                    searchSettings['tourUrl']
                )
                this.$store.dispatch(
                    MapActions.setBoxAccessToken,
                    searchSettings['mapBoxAccessToken']
                )
                this.$store.dispatch(
                    MapActions.setGooglesApiKey,
                    searchSettings['googleMapsApiKey']
                )
            }
            this.$store.dispatch(GeneralActions.setStoreInitialized, true)
        })
    },
}
</script>

<style lang="scss" scoped>
.bookmarks {
    &_main {
        display: flex;
        flex-direction: column;
        height: 100%;
        justify-content: space-between;
    }
}
</style>
