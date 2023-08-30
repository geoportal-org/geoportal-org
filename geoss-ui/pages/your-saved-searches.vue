<template>
    <div>
        <div class="my-workspace-header">
            My Workspace / Your Saved Searches
            <NuxtLink to="/" class="close-window">
                <div class="line-1"></div>
                <div class="line-2"></div>
            </NuxtLink>
        </div>
        <div class="my-workspace-tab my-workspace-content">
            Your Saved Searches
        </div>
    </div>
</template>

<script>
export default {
    layout() {
        return 'default'
    },

    data() {
        return {}
    },
}
</script>

<style lang="scss"></style>
