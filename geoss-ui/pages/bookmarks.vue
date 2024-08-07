<template>
    <client-only>
        <div class="sub-page">
            <div class="sub-page__content">
                <div class="bookmarks">
                    <div class="bookmarks__sub" v-if="storeInitialized">
                        <BookmarksHeader />
                        <BookmarksList />
                        <BookmarksPagination />
                    </div>
                </div>
            </div>
        </div>
    </client-only>
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
.bookmarks {
    height: 100%;
    display: flex;
    flex-direction: column;

    &__sub {
        height: calc(100% - 100px)
    }
}
</style>
