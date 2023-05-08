<template>
    <main>
        <div class="sub-page">
            <div class="sub-page__content">
                <Nuxt />
            </div>
        </div>
    </main>
</template>

<style lang="scss" scoped>
// @import "~/assets/scss/reset.scss";
@import "~/assets/scss/general.scss";

body {
    @include regular-page-body;
}

.sub-page {
    @include regular-page;
    background: #ccc;

    &__content {
        @include regular-page-content;
        background: white !important;
        max-width: 800px !important;
    }
}
</style>
