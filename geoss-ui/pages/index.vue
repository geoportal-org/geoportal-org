<template>
    <div class="sub-page">
        <div class="sub-page__content">
            <welcome-popup />
        </div>
    </div>
</template>

<script lang="ts">
import Vue from 'vue'

export default Vue.extend({
    layout() {
        return 'full'
    },
    name: 'IndexPage',
})
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
