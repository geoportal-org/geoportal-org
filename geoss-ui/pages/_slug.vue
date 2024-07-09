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
import { SearchEngineGetters } from '@/store/searchEngine/search-engine-getters';

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
        storeInitialized() {
            return this.$store.getters[GeneralGetters.storeInitialized];
        },
        langLocale() {
            return this.$store.getters[GeneralGetters.langLocale];
        },
        siteId() {
            return this.$store.getters[SearchEngineGetters.siteId];
        },
    },

    watch: {
        async langLocale(newVal) {
            const slug = this.$route.params.slug;
            const locale = newVal;
            const { generatedPage, generatedContent } = await ContentAPI.generatePage(slug, locale, this.siteId)
            this.page = generatedPage;
            this.content = generatedContent;
        }
    },

    mounted() {
        document.querySelector('.my-workspace-tab').querySelectorAll('.version').forEach(element => {
            element.addEventListener('click', () => {
                element.closest('.my-workspace-left').classList.toggle('changes-visible');
            });
        });
    },

    async fetch() {
        const slug = this.$route.params.slug;
        const locale = this.langLocale;
        const { generatedPage, generatedContent } = await ContentAPI.generatePage(slug, locale, this.siteId)
        this.page = generatedPage;
        this.content = generatedContent;
    }
}
</script>

<style lang="scss">
@import "~/assets/scss/content-pages.scss";
</style>
