<template>
    <client-only>
        <div>
            <Map />
            <MapControls />
            <SearchContainer />
            <MapAttribution />
        </div>
    </client-only>
</template>

<script>
import { GeneralGetters } from '@/store/general/general-getters';

export default {
    computed: {
        storeInitialized() {
            return this.$store.getters[GeneralGetters.storeInitialized];
        },
        isBulkDownloadEnabled() {
            return this.$store.getters[GeneralGetters.isBulkDownloadEnabled];
        }
    }
}
</script>

<style lang="scss"></style>
