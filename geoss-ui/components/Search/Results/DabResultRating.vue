<template>
    <div class="dab-result-rating">
        <div class="dab-result-rating__comment">
            <label>{{ $tc('popupContent.comment') }}:</label>
            <textarea
                maxlength="500"
                v-model="comment"
                :disabled="!isSignedIn"
            ></textarea>
        </div>
        <div class="d-flex flex--justify-between flex--align-end">
            <div class="dab-result-rating__rating">
                <label>{{ $tc('popupContent.rating') }}:</label>
                <div class="dab-result-rating__stars">
                    <div
                        v-for="num in [5, 4, 3, 2, 1]"
                        :key="num"
                        class="dab-result-rating__star"
                        :class="{ active: score === num }"
                    >
                        <i class="icomoon-star-empty"></i>
                        <i class="icomoon-star" @click="setScore(num)"></i>
                    </div>
                </div>
            </div>
            <div>
                <button
                    class="blue-btn-default"
                    :disabled="!score"
                    @click="rate()"
                >
                    {{ $tc('popupContent.send') }}
                </button>
            </div>
        </div>
        <div
            v-if="comments && comments.length > 0"
            class="comments-section-title"
        >
            {{ $tc('popupContent.userContributedTitleComment') }}
        </div>
        <div
            class="dab-result-rating__user-comment"
            v-for="(comment, index) in comments"
            :key="index"
        >
            <div class="dab-result-rating__stars">
                <div
                    v-for="num in [5, 4, 3, 2, 1]"
                    :key="num"
                    class="dab-result-rating__star"
                    :class="{ active: comment.score === num }"
                >
                    <i class="icomoon-star-empty"></i>
                    <i class="icomoon-star" @click="setScore(num)"></i>
                </div>
            </div>
            <div class="dab-result-rating__comment">{{ comment.comment }}</div>
        </div>
    </div>
</template>

<script lang="ts">
// @ts-nocheck
import { Component, Vue, Prop } from 'nuxt-property-decorator'
import GeossSearchApiService from '@/services/geoss-search.api.service'
import { GeneralGetters } from '@/store/general/general-getters'
import { SearchActions } from '@/store/search/search-actions'
import MouseLeaveService from '@/services/mouse-leave.service'
import PopupCloseService from '@/services/popup-close.service'
import to from '@/utils/to'
import LogService from '@/services/log.service'
import { Liferay } from '@/data/global'
import { DataOrigin } from '@/interfaces/DataSources'
import { BookmarksActions } from '@/store/bookmarks/bookmarks-actions'
import RatingService from '~/services/ratings.service'

@Component
export default class DabResultRatingComponent extends Vue {
    public comment: string = ''
    public score: number = 0

    @Prop({ default: null, type: String }) private id!: string
    @Prop({ default: null, type: String }) private title!: string
    @Prop({ default: () => [], type: Array }) public comments!: Array<{
        score: number
        comment: string
    }>
    @Prop({ default: null, type: Number }) private userScore!: number
    @Prop({ default: null, type: String }) private userComment!: string
    @Prop({ default: 'dab', type: String }) private dataSource!: string
    @Prop({ default: null, type: String }) private referer!: string
    @Prop({ default: () => null, type: Function }) public setAvScore!: any

    get isWidget() {
        return this.$store.getters[GeneralGetters.isWidget]
    }

    get isSignedIn() {
        return this.$nuxt.$auth.loggedIn;
    }

    public setScore(score: number) {
        this.score = score
    }

    public async rate() {
        if (this.comment && this.comment !== '') {
            await this.rateWithComment()
        } else {
            await this.rateWithoutComment()
        }
        PopupCloseService.closePopup('rating')
    }


    public async rateWithComment() {
        try {
            //@ts-ignore
            const token = this.$nuxt.$auth.getToken('keycloak')
            const res = await RatingService.rateWithComment(
                this.id,
                this.title,
                token,
                this.score,
                this.comment
            )
            if (res) {
                this.setAvScore(res.averageScore)
            }
        } catch (e) {
            console.log(e)
        }
        LogService.logElementClick(
            null,
            null,
            this.id,
            null,
            'Send rating',
            null,
            null,
            this.title
        )
    }


    public async rateWithoutComment() {
        try {
            const res = await RatingService.rateWithoutComment(
                this.id,
                this.title,
                this.score
            )
            if (res) {
                this.setAvScore(res.averageScore)
            }
        } catch (e) {
            console.log(e)
        }
    }

}
</script>

<style lang="scss">
.dab-result-rating {
    padding: 30px 25px 50px 25px;

    max-height: 50vh;

    .dab-result-rating__comment {
        word-break: break-word;
    }

    &__comment,
    &__rating {
        label {
            font-size: 20px;
            display: block;
            margin-bottom: 10px;
        }
    }

    &__comment {
        margin-bottom: 20px;

        textarea {
            width: 100%;
            height: 100px;
            border: 2px solid $grey;
            border-radius: 7px;
            resize: none;
            outline: 0;
            padding: 5px;
        }
    }

    &__stars {
        display: inline-flex;
        flex-direction: row-reverse;

        &:not(:hover) {
            .dab-result-rating__star.active {
                .icomoon-star {
                    display: block;
                }

                & ~ div {
                    .icomoon-star {
                        display: block;
                    }
                }
            }
        }
    }

    &__star {
        position: relative;
        min-height: 25px;
        min-width: 25px;
        display: block;
        cursor: pointer;

        &:hover {
            .icomoon-star {
                display: block;
            }

            & ~ div {
                .icomoon-star {
                    display: block;
                }
            }
        }

        i {
            font-size: 25px;
            margin-right: 5px;
        }

        .icomoon-star {
            color: $yellow;
            position: absolute;
            left: 0;
            top: 0;
            display: none;
        }
    }

    &__user-comment {
        &:hover {
            pointer-events: none;
        }

        &:not(:hover) {
            pointer-events: none;
        }
        margin-top: 20px;
        margin-bottom: 20px;
        .dab-result-rating__stars {
            i {
                font-size: 17px;
            }
        }
    }
}

.comments-section-title {
    margin-top: 20px;
    font-size: 21px;
}
</style>
