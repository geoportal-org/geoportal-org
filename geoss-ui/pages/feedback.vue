<template>
    <client-only>
        <div class="sub-page">
            <div class="sub-page__content">
                <div class="my-workspace-header">
                    <h1>{{ $tc('feedback.feedback') }}</h1>
                    <NuxtLink to="/" class="close-window">
                        <div class="line-1"></div>
                        <div class="line-2"></div>
                    </NuxtLink>
                </div>
                <div class="feedback">
                    <div class="feedback__sub" v-if="storeInitialized">
                        <FeedbackStarter />
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
.feedback {
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;

    &__sub {
        // height: calc(100% - 100px)
    }
}
</style>
