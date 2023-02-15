<template>
    <div class="sub-page">
        <div class="sub-page__content">
            <h1>Subpage: {{ slug }}</h1>
        </div>
    </div>
</template>

<script>
export default {
    layout() {
        return 'default'
    },
    async asyncData({ params }) {
        const slug = params.slug;
        return { slug }
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
