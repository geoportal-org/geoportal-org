<template>
    <div class="views-ratings">
        <div
            class="views-ratings__views"
            :data-tutorial-tag="
                resultIdDetails === result.id ? 'result-views' : ''
            "
        >
            <i class="icomoon-editor--eye"></i>
            <span>{{ views || 0 }}</span>
        </div>
        <div
            v-if="avScore !== null"
            class="views-ratings__rating"
            :class="{ 'no-hover': isWidget }"
            title="Stars are used to mark relevant resources"
        >
            <i
                class="icomoon-editor--star"
                :data-tutorial-tag="
                    resultIdDetails === result.id ? 'result-ratings' : ''
                "
            ></i>
            <span>{{ Number(avScore).toFixed(1) }}</span>
            <div v-if="!isWidget" class="views-ratings__ratings-stars">
                <div
                    v-for="num in [5, 4, 3, 2, 1]"
                    :key="num"
                    class="views-ratings__ratings-star"
                    :class="{ active: score === num }"
                >
                    <i
                        class="icomoon-star-empty"
                        v-show="Math.floor(avScore) < num"
                    ></i>
                    <i
                        class="icomoon-star yellow"
                        v-show="Math.floor(avScore) >= num"
                        @click="setScore(num)"
                    ></i>
                </div>
            </div>
        </div>
        <a
            v-if="
                !isWidget &&
                isEntryExtensionEnabled &&
                dataSource !== DataSources.WIKIPEDIA &&
                !extendedViewMode &&
                !hidePocFeatures
            "
            :class="{ disabled: !isSignedIn }"
            class="views-ratings__entry-extension"
            :title="
                isSignedIn
                    ? $tc('popupContent.improveTheResourceDefinition')
                    : $tc('popupContent.youNeedToBeSignedInToImprove')
            "
            @click="entryExtension()"
            :data-tutorial-tag="
                resultIdDetails === result.id ? 'result-improve' : ''
            "
        >
            <i class="icomoon-editor--edit"></i>
        </a>
    </div>
</template>

<script lang="ts">
// @ts-nocheck
import { Component, Vue, Prop, Watch } from 'nuxt-property-decorator'
import { DataSources, DataOrigin } from '@/interfaces/DataSources'
import { PopupActions } from '@/store/popup/popup-actions'
import { SearchGetters } from '@/store/search/search-getters'
import { GeneralGetters } from '@/store/general/general-getters'
import UtilsService from '@/services/utils.service'
import EntryExtension from '@/components/Search/Results/EntryExtension.vue'
import DabResultRating from '@/components/Search/Results/DabResultRating.vue'
import LogService from '@/services/log.service'
import RatingService from '~/services/ratings.service'
import { PopupGetters } from '~/store/popup/popup-getters'

@Component
export default class ViewsAndRatingsComponent extends Vue {
    @Prop({ default: null, type: Object }) public result!: any
    @Prop({ default: false, type: Boolean }) public extendedViewMode!: boolean
    @Prop({ default: null, type: String }) public currentId!: any

    public score = 0
    public DataSources = DataSources
    public views = 0
    public avScore = 0

    @Watch('currentId')
    async onCurrentIdChange() {
        this.views = await this.getCounter()
        this.avScore = await this.getRating()
    }

    @Watch('queue')
    async onQueueChange() {
        if (
            !this.queue.some((content: any) => content.contentId === 'metadata')
        ) {
            this.views = await this.getCounter()
            this.avScore = await this.getRating()
        }
    }

    get hidePocFeatures() {
        return this.$config.hidePocFeatures
    }

    get queue() {
        return this.$store.getters[PopupGetters.queue]
    }

    get resultIdDetails() {
        return this.$store.getters[SearchGetters.resultIdDetails]
    }

    get rootDataOrigin() {
        // original entry's dataSource exclusively from OpenSearch
        const rootDataOrigin = UtilsService.extractCategoriesByAttributeValue(
            this.result,
            'term',
            'dataSource'
        )[0]
        return rootDataOrigin || DataOrigin[this.dataSource] || this.dataSource
    }

    get dataSource() {
        return this.$store.getters[SearchGetters.dataSource]
    }

    get isZenodoType() {
        return (
            (this.isParentRef && this.parentRef === DataSources.ZENODO) ||
            (!this.isParentRef && this.dataSource === DataSources.ZENODO)
        )
    }

    get parentRef() {
        return this.$store.getters[SearchGetters.parentRef]
    }

    get isParentRef() {
        return this.parentRef && this.parentRef.id === this.result.id
    }

    get isWidget() {
        return this.$store.getters[GeneralGetters.isWidget]
    }

    get isSignedIn() {
        return this.$nuxt.$auth.loggedIn;
    }

    get isEntryExtensionEnabled() {
        return this.$store.getters[GeneralGetters.isEntryExtensionEnabled]
    }

    public async setScore(score: number) {
        //@ts-ignore
        const loggedIn = this.$nuxt.$auth.loggedIn
        this.score = score
        if (!loggedIn) {
            await this.rateWithoutComment()
        } else {
            this.openRatingModal()
        }
        LogService.logRecommendationData(
            'Search result',
            'Rating',
            'internal',
            { score: this.score }
        )
    }

    public setAvScore(v: number) {
        this.avScore = v
    }

    public async openRatingModal() {
        const res = await this.getComments()
        const comments: { score: any; comment: any }[] = []

        if (res && res.length > 0) {
            res.forEach((element: any) => {
                comments.push({
                    score: element.score,
                    comment: element.comment
                })
            })
        }

        const props = {
            id: this.result.id,
            title: this.result.title,
            comments,
            userScore: this.score,
            userComment: this.result.rating ? this.result.rating.comment : "",
            dataSource: this.dataSource,
            setAvScore: this.setAvScore
        }

        this.$store.dispatch(PopupActions.openPopup, {
            contentId: 'rating',
            title: this.$tc('popupTitles.rateResouce'),
            component: DabResultRating,
            props
        })
    }

    public async entryExtension() {
        if (this.isSignedIn) {
            const props = {
                id: this.result.id,
                title: this.result.title,
                rootDataOrigin: this.rootDataOrigin
            }
            this.$store.dispatch(PopupActions.openPopup, {
                contentId: 'entry-extension',
                title: this.$tc('popupTitles.improveDefinition'),
                component: EntryExtension,
                props
            })
        }
    }

    public async getCounter() {
        const res = await LogService.fetchCounter(this.result.id)
        if (res && res.counter !== undefined) {
            return res.counter
        } else {
            return 0
        }
    }

    public async getRating() {
        const res = await RatingService.fetchRating(this.result.id)
        if (res) {
            return res.stats[0].averageScore
        } else {
            return 0
        }
    }

    public async getComments() {
        const res = await RatingService.fetchComments(this.result.id)
        if (res) {
            return res
        } else {
            return 0
        }
    }

    public async rateWithoutComment() {
        try {
            const res = await RatingService.rateWithoutComment(
                this.result.id,
                this.result.title,
                this.score
            )

            if (res) {
                this.avScore = res.averageScore
            }
        } catch (e) {
            console.log(e)
        }
    }
}
</script>

<style lang="scss" scoped>
.views-ratings {
    display: flex;
    align-items: center;
    flex: 0 0 auto;
    margin: 0 0 5px;

    .is-parent-ref & {
        color: white;
    }

    @media (max-width: $breakpoint-xs) {
        width: 100%;
        flex-wrap: wrap;
    }

    &__views {
        display: flex;
        align-items: center;

        i {
            color: $green;
            font-size: 25px;
            margin-right: 6px;
            margin-bottom: 1px;

            .is-parent-ref & {
                color: white;
            }
        }

        span {
            white-space: nowrap;
            font-size: 14px;
        }
    }

    &__entry-extension {
        cursor: pointer;
        margin-left: 15px;
        text-decoration: none;

        &.disabled {
            i {
                color: $grey;
            }
        }

        i {
            color: $blue;
            font-size: 20px;

            .is-parent-ref & {
                color: white;
            }
        }
    }

    &__rating {
        display: flex;
        align-items: center;
        margin-left: 15px;
        cursor: pointer;
        position: relative;
        padding: 10px 0;

        &:hover {
            &:after {
                content: '';
                display: block;
                width: 8px;
                height: 8px;
                background: white;
                position: absolute;
                left: 25%;
                transform: translateX(-50%) rotate(45deg);
                bottom: -4px;
                box-shadow: 0 0 5px rgba(0, 0, 0, 0.3);
            }

            .views-ratings__ratings-stars {
                display: inline-flex;

                &:after {
                    content: '';
                    display: block;
                    width: 8px;
                    height: 8px;
                    background: white;
                    position: absolute;
                    left: 64%;
                    transform: translateX(-50%) rotate(45deg);
                    top: -4px;
                    z-index: 1;
                }
            }
        }

        &.no-hover {
            cursor: default;

            &:after {
                content: none;
            }
        }

        i {
            color: $green;
            font-size: 20px;
            margin-right: 5px;
            margin-bottom: 2px;
        }

        > i {
            .is-parent-ref & {
                color: white;
            }
        }

        span {
            white-space: nowrap;
            font-size: 14px;
        }
    }

    &__ratings-stars {
        display: none;
        flex-direction: row-reverse;
        align-items: center;
        position: absolute;
        top: 100%;
        left: 0%;
        transform: translateX(-57%);
        background: white;
        box-shadow: 0 1px 10px rgba(0, 0, 0, 0.2);
        padding: 10px;
        z-index: 1;

        &:hover {
            .icomoon-star {
                display: none !important;
            }

            .icomoon-star-empty {
                display: block !important;
            }
        }
    }

    &__ratings-star {
        padding-right: 8px;
        font-size: 0;

        &:first-child {
            padding: 0;
        }

        i {
            font-size: 13px;
            margin: 0 !important;

            &.yellow {
                color: $yellow;
            }
        }

        &:hover {
            .icomoon-star {
                display: block !important;
            }

            .icomoon-star-empty {
                display: none !important;
            }

            & ~ div {
                .icomoon-star {
                    display: block !important;
                }

                .icomoon-star-empty {
                    display: none !important;
                }
            }
        }
    }
}
</style>
