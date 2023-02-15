<template>
    <div>
        <NuxtLink to="/">Main page</NuxtLink>
        <NuxtLink to="/pl">Main page PL</NuxtLink>
        <NuxtLink to="/example-sub-page">Example page</NuxtLink>
    </div>
</template>

<script>
export default {

}
</script>

<style lang="scss" scoped>
div {
    display: flex;
    justify-content: center;
    position: relative;
    z-index: 1;
    background: $blue-transparent;

    >* {
        margin: 0;
        padding: 10px 20px;
        color: $white;
    }
}
</style>
