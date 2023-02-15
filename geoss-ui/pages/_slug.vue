<template>
    <h1>{{ slug }}</h1>
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
