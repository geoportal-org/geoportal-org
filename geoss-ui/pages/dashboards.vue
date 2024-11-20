<template>
    <div class="sub-page">
        <div class="sub-page__content">
            <div class="my-workspace-header">
                <h1>{{ $tc('popupTitles.dashboards') }}</h1>
                <NuxtLink to="/" class="close-window">
                    <div class="line-1"></div>
                    <div class="line-2"></div>
                </NuxtLink>
            </div>
            <DashboardList />
        </div>
    </div>
</template>

<script>
import { GeneralGetters } from '@/store/general/general-getters';

export default {
    computed: {
        storeInitialized() {
            return this.$store.getters[GeneralGetters.storeInitialized];
        }
    }
}
</script>

<style lang="scss">
.yellow-pages {
    height: 100%;
    display: flex;
    flex-direction: column;

    &__sub {
        height: calc(100% - 100px)
    }
}
</style>
