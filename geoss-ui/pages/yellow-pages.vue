<template>
    <client-only>
        <div class="yellow-pages">
            <div class="yellow-pages__sub" v-if="storeInitialized">
                <YellowPagesHeader />
                <YellowPagesProviders />
                <YellowPagesPagination />
            </div>
        </div>
    </client-only>
</template>

<script>
import { GeneralGetters } from '@/store/general/general-getters';

export default {
    layout() {
        return 'default'
    },

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
