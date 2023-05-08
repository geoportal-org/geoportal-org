<template>
    <div>
        <div class="my-workspace-header">
            {{ page.title }}
            <NuxtLink to="/" class="close-window">
                <div class="line-1"></div>
                <div class="line-2"></div>
            </NuxtLink>
        </div>
        <div class="my-workspace-tab my-workspace-content" v-html="content.data"></div>
    </div>
</template>

<script>
import ContentAPI from '@/api/content'

export default {
    layout() {
        return 'static'
    },
    data() {
        return {
            page: {
                title: '-'
            },
            content: {
                data: '-'
            }
        }
    },

    async asyncData({ params, page, content }) {
        const slug = params.slug;
        const receivedPage = await ContentAPI.getPage(slug);

        page = receivedPage || { title: 'Missing page' };
        content = receivedPage ? await ContentAPI.getContent(receivedPage.contentId) : {
            data: 'No page found for slug: ' + slug
        };

        return { slug, page, content }
    }
}
</script>

<style lang="scss"></style>
