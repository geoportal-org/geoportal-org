<template>
    <div class="user-contributions" v-if="userExtensions.length">
        <div class="user-contributions__label">
            {{ $tc(`popupContent.userContributedTitle${model.charAt(0).toUpperCase() + model.slice(1)}`) }}
        </div>
        <div class="user-contributions__item" v-for="(extension, index) in userExtensions" :key="index"
            v-show="showItem(extension)">
            <div class="user-contributions__item-metadata">
                <div>
                    <i class="icomoon-editor--user"></i>
                </div>
                <div>
                    <b>{{ extension.userName }}</b>
                    <span>{{ formatDate(extension.modifiedDate || extension.createdDate) }}</span>
                </div>
            </div>
            <div v-if="model === 'summary'" class="user-contributions__item-content">
                {{ extension[model] }}
            </div>
            <div v-if="model === 'keywords'" class="user-contributions__item-content">
                <div v-for="(keyword, index) of extension[model]" :key="index">
                    <a class="metadata__keyword" @click="keywordSearch(keyword)">{{ keyword }}</a>
                </div>
            </div>
            <div v-if="model === 'transferOptions'" class="user-contributions__item-content">
                <div v-for="(link, index) in extension[model]" :key="index">
                    <div v-show="index === arrayIndex" class="metadata__link" :title="link.displayTitle">
                        <div>
                            <a target="_blank" :href="link.url" class="link">{{ link.name ? link.name : link.url }}</a>
                            <div v-if="link.description && link.description !== ''">{{ link.description }}</div>
                        </div>
                    </div>
                </div>
            </div>
            <div v-if="model === 'comment'" class="user-contributions__item-content">
                {{ extension[model] }}
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { Component, Vue, Prop, Emit } from 'nuxt-property-decorator';
import UtilsService from '@/services/utils.service';
import { GeneralGetters } from '@/store/general/general-getters';

@Component
export default class UserContributionRemoveElementComponent extends Vue {
    @Prop(String) public model!: string;
    @Prop(String) public extensionId!: string;
    @Prop(Object) public data!: any;
    @Prop(Number) public arrayIndex!: number;
    public userExtensions = this.model === 'comment' ? this.userContributionsComments : this.userContributionsExtensions;

    get userContributionsExtensions() {
        let data = null;
        data = UtilsService.getArrayByString(
            this.data.data,
            'userContributions.extensions'
        );
        return data;
    }

    get userContributionsComments() {
        let data = null;
        data = UtilsService.getArrayByString(
            this.data.data,
            'userContributions.comments'
        );
        return data;
    }

    get langLocale() {
        return this.$store.getters[GeneralGetters.langLocale];
    }

    public formatDate(date: string) {
        const dateObj = new Date(date);
        let displayDate = `${dateObj.toLocaleString(this.langLocale.replace(/_/g, '-'), { month: 'long' })} ${dateObj.getDate()}, ${dateObj.getFullYear()}`;
        if (this.langLocale === 'pl_PL') {
            displayDate = `${dateObj.getDate()} ${dateObj.toLocaleString(this.langLocale.replace(/_/g, '-'), { month: 'long' })} ${dateObj.getFullYear()}`;
        }
        return displayDate;
    }

    public showItem(extension: any) {
        return extension.entryExtensionId === this.extensionId;
    }

    @Emit()
    public keywordSearch(keyword: any) {
        return keyword;
    }
}
</script>

<style lang="scss">
// styles from UserContributionsMetadata.vue
</style>
