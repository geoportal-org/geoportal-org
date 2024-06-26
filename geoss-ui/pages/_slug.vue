<template>
    <div class="sub-page">
        <div class="sub-page__content">
            <div class="my-workspace-header">
                {{ page.title }}
                <NuxtLink to="/" class="close-window">
                    <div class="line-1"></div>
                    <div class="line-2"></div>
                </NuxtLink>
            </div>
            <div class="my-workspace-tab my-workspace-content" v-html="content.data"></div>
        </div>
    </div>
</template>

<script>
import ContentAPI from '@/api/content'
import { GeneralGetters } from '@/store/general/general-getters'

export default {
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

    computed: {
        langLocale() {
            return this.$store.getters[GeneralGetters.langLocale];
        }
    },

    watch: {
        async langLocale(newVal) {
            const slug = this.$route.params.slug;
            const locale = newVal;
            const { generatedPage, generatedContent } = await ContentAPI.generatePage(slug, locale)
            this.page = generatedPage;
            this.content = generatedContent;
        }
    },

    async asyncData({ params, page, content, i18n }) {
        const slug = params.slug;
        const locale = i18n.getLocaleCookie()
        const { generatedPage, generatedContent } = await ContentAPI.generatePage(slug, locale)
        page = generatedPage;
        content = generatedContent;

        return { slug, page, content }
    }
}
</script>

<style lang="scss"></style>
