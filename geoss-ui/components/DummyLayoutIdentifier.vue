<template>
    <div>
        Layout: {{ layoutId }}
    </div>
</template>

<script lang="ts">
export default {
    props: ['layoutId']
}
</script>

<style lang="scss" scoped>
div {
    display: flex;
    justify-content: center;
    padding: 10px;
    background: $green-transparent;
    color: $white;
}
</style>
