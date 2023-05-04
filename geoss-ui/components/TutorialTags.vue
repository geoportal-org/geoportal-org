<template>
    <div class="tutorial-tags" v-if="!isWidget">
        <template v-for="(tag, index) in tags">
            <div class="tutorial-tag" :class="{ 'active': activeTagId === tag.id, 'on-screen': showTagOnScreen(tag) }"
                v-if="showTag(tag)" :key="index"
                :style="'top: ' + tag.top + 'px; left: ' + tag.left + 'px; z-index: ' + getZIndex(tag) + ';'">
                <label :for="tag.id" class="tutorial-tag__label">
                    <input type="checkbox" :id="tag.id" class="tutorial-tag__input" name="tutorial-tag" />
                    <span v-if="getType(tag) === 'new'" class="tutorial-tag__badge new" @click="tagClick(tag.id)">{{
                        $tc(`tutorial.${getType(tag)}`) }}</span>
                    <span v-else class="tutorial-tag__badge" @click="tagClick(tag.id)">
                        <i
                            :class="{ 'icomoon-lightbulb-off': activeTagId !== tag.id, 'icomoon-lightbulb-on': activeTagId === tag.id }"></i>
                    </span>
                    <div class="tutorial-tag__description" :style="getDescriptionPosition(tag.top, tag.left)"
                        @click="tagClick(tag.id)">
                        <b class="tutorial-tag__title">{{ getFromLangLocale(tag, 'title') }}</b>
                        <span class="tutorial-tag__close" @click="closeNewTag(tag)">✕</span>
                        <div v-bar>
                            <div v-html="getFromLangLocale(tag, 'description')"></div>
                        </div>
                    </div>
                </label>
            </div>
        </template>

        <span class="tutorial-mode" data-tutorial-tag="tutorial-mode-switch" @click="tutorialSwitch()">
            <i :class="{ 'icomoon-lightbulb-off': !showTutorial, 'icomoon-lightbulb-on': showTutorial }"></i>
            <span>{{ $tc('tutorial.mode') }}</span>
        </span>

        <button v-if="showTutorial" class="tutorial-off green-btn-default" @click="tutorialSwitch()">
            <span>{{ $tc('tutorial.close') }}</span>
        </button>
    </div>
</template>

<script lang="ts">
// @ts-nocheck
import { Component, Vue } from 'nuxt-property-decorator';
import TutorialTagsService from '@/services/tutorial-tags.service';
import { GeneralGetters } from '@/store/general/general-getters';
import { GeneralApiService } from '@/services/general.api.service';
import to from '@/utils/to';

@Component
export default class TutorialTagsComponent extends Vue {
    public tags = [];
    public showTutorial = false;
    public tagsBlacklist = [];
    public activeTagId = '';
    public defaultTag = {
        show: true,
        description: [{ zh_CN: '' }, { es_ES: '' }, { pl_PL: '' }, { ru_RU: '' }, { fr_FR: '' }, { en_US: 'Turn on/off tutorial mode' }],
        id: 'tutorial-mode-switch',
        placement: 'left-top',
        title: [{ zh_CN: '' }, { es_ES: '' }, { pl_PL: '' }, { ru_RU: '' }, { fr_FR: '' }, { en_US: 'Tutorial mode switch' }],
        type: 'new',
    };

    get isWidget() {
        return this.$store.getters[GeneralGetters.isWidget];
    }

    get langLocale() {
        return this.$store.getters[GeneralGetters.langLocale];
    }

    public async tutorialSwitch() {
        this.showTutorial = !this.showTutorial;
        if (this.showTutorial && (!this.tags.length || this.tags.length === 1)) {
            [, this.tags] = await to(GeneralApiService.getTutorialTags());
            this.tags.sort((a, b) => a.type === b.type ? 0 : +(a.type < b.type) || -1);
            for (const tag of this.tags) {
                tag.startPosition = {
                    left: 0,
                    top: 0
                };
            }
        } else {
            this.tags = [this.defaultTag];
        }
    }

    public showTag(tag) {
        const tagEnabled = tag.show;
        const tutorialOn = this.showTutorial;
        const tagHasContents = tag.title && tag.description && tag.title !== '' && tag.description !== '';
        let tagIsNew = this.getType(tag) === 'new';
        const tagOnBlacklist = tagIsNew && this.tagsBlacklist.includes(tag.id);
        if (tagOnBlacklist) {
            tag.type = 'tip';
            tagIsNew = false;
        }
        return tagEnabled && tagHasContents && (tutorialOn || tagIsNew) && !tagOnBlacklist;
    }

    public showTagOnScreen(tag) {
        return tag.top > 15 && tag.left > 15 && tag.left < (window.innerWidth - 17) && tag.top < (window.innerHeight - 15);
    }

    public getPositions(event = null) {
        const eventGroup = event && event.group ? event.group : null;
        this.$nextTick(() => {
            if (this.tags.length) {
                for (const tag of this.tags) {
                    if (!eventGroup || (eventGroup && tag.id.indexOf(eventGroup) === 0)) {
                        this.$set(tag, 'top', TutorialTagsService.getPositions(tag, event).top);
                        this.$set(tag, 'left', TutorialTagsService.getPositions(tag, event).left);
                    }
                }
            }
        });
    }

    public getDescriptionPosition(top, left) {
        const positionHorizontal = TutorialTagsService.getDescriptionPositionHorizontal(left * 1);
        const positionVertical = TutorialTagsService.getDescriptionPositionVertical(top * 1);
        return positionVertical + positionHorizontal;
    }

    public closeNewTag(tag) {
        if ((this.tagsBlacklist.length && !this.tagsBlacklist.includes(tag.id)) || !this.tagsBlacklist.length) {
            this.tagsBlacklist.push(tag.id);
        }
        const expires = new Date();
        expires.setFullYear(expires.getFullYear() + 10);
        this.$cookies.set('TUTORIAL_TAGS_BLACKLIST', JSON.stringify(this.tagsBlacklist.join()), expires);
    }

    public tagClick(tagId) {
        if (this.activeTagId === tagId) {
            this.activeTagId = '';
        } else {
            this.activeTagId = tagId;
        }
    }

    public getZIndex(tag) {
        if (tag.id.indexOf('tutorial-') === 0) {
            return '99';
        } else if (tag.id.indexOf('header-') === 0) {
            return '999';
        } else if (tag.id.indexOf('popup-') === 0) {
            return '9999';
        } else {
            return '';
        }
    }

    public getType(tag) {
        let tagType = 'tip';
        if (tag.type && tag.type === 'new') {
            tagType = tag.type;
        }
        return tagType;
    }

    public getFromLangLocale(tag, type) {
        for (const locale of tag[type]) {
            if (locale[this.langLocale]) {
                return locale[this.langLocale];
            }
        }
        return '';
    }

    public async mounted() {
        this.tags = [this.defaultTag];
        this.tagsBlacklist = this.$cookies.get('TUTORIAL_TAGS_BLACKLIST') ? JSON.parse(this.$cookies.get('TUTORIAL_TAGS_BLACKLIST')).split(',') : [];

        TutorialTagsService.eventBus.$on('getPositions', event => {
            this.getPositions(event);
        });

        TutorialTagsService.eventBus.$on('blacklist-refresh', () => {
            this.tagsBlacklist = [];
        });

        window.addEventListener('resize', this.getPositions);
    }
}
</script>

<style lang="scss" scoped>
.tutorial {
    &-tags {
        @media (max-width: $breakpoint-lg) {
            display: none;
        }
    }

    &-mode {
        position: fixed;
        bottom: 10px;
        right: 150px;
        z-index: 10;
        color: white;
        display: flex;
        align-items: center;
        font-size: 13px;
        text-decoration: none;
        cursor: pointer;

        span,
        i {
            text-shadow: $text-shadow-black-darker;
        }

        i {
            margin-right: 5px;
            font-size: 20px;
            position: relative;
            top: -5px;
            margin-bottom: -3px;
        }

        &:hover {
            span {
                text-decoration: underline;
                color: #fff;
            }

            i {
                animation: wobble 0.5s both;
            }
        }
    }

    &-off {
        position: fixed;
        bottom: 2%;
        left: 50%;
        transform: translate(-50%, -50%);
        z-index: 2;
        font-size: 1em;
        padding: 10px 20px;
    }

    &-tag {
        transform: translate(-50%, -50%);
        position: fixed;
        z-index: 9;
        font-size: 0.85em;
        opacity: 0;

        &.on-screen {
            opacity: 1;
            transition: ease .2s opacity;
        }

        &.active {
            z-index: 99999 !important;

            .tutorial-tag {
                &__badge {
                    border: 2px solid rgba(255, 255, 255, 0.8);
                }

                &__description {
                    display: block;
                }
            }
        }

        &__label {
            cursor: pointer;
            line-height: 1;
        }

        &__badge {
            display: block;
            background: $green;
            color: white;
            padding: 4px;
            border-radius: 50%;
            text-align: center;
            display: inline-block;
            border: 2px solid transparent;
            box-shadow: 0 0 5px rgba(0, 0, 0, 0.2);

            &.new {
                background: $red;
                padding: 2px 3px 3px;
                border-radius: 10px;
                min-width: 40px;
            }
        }

        &__input {
            display: none;
        }

        &__title {
            display: block;
            font-size: 1rem;
            margin-bottom: 10px;
            padding-right: 15px;
        }

        &__description {
            display: none;
            position: absolute;
            padding: 15px;
            color: black;
            background: rgba(255, 255, 255, 0.9);
            border-radius: 5px;
            box-shadow: 0 0 15px rgba(0, 0, 0, 0.25);
            text-transform: none;
            text-align: left;
            cursor: default;

            .vb {
                max-height: 5em;
                width: 250px;

                &-content {
                    min-height: 3em;
                }
            }
        }

        &__close {
            position: absolute;
            top: 17px;
            right: 15px;
            cursor: pointer;
        }
    }
}
</style>
