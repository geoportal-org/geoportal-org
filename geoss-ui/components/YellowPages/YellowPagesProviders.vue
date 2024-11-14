<template>
    <div class="yellow-pages-providers" ref="results-container" v-bar>
        <div>
            <div
                v-for="provider of currentResults.slice(0, elementsCount)"
                :key="provider.id"
                class="active provider"
                :id="`provider-${provider.id}`"
            >
                <div class="provider__main">
                    <div class="provider__left">
                        <div class="provider__image">
                            <img
                                v-image-preview
                                :src="
                                    getImage(provider.data.organizationLogoURL)
                                "
                                @error="imageLoadError(provider.image_url)"
                                :alt="provider.title"
                            />
                        </div>
                        <div class="provider__side-info">
                            <div>
                                <div class="provider__additional-info">
                                    <label
                                        >{{ $tc('yellowPages.geoAffiliation') }}
                                    </label>
                                    <div>
                                        {{ provider.data.geoAffiliation }}
                                    </div>
                                </div>
                                <div class="provider__additional-info">
                                    <label
                                        >{{ $tc('yellowPages.dataPolicy') }}
                                    </label>
                                    <div>
                                        {{
                                            provider.data.dataPolicy.replace(
                                                /[\[\]"]/g,
                                                ''
                                            )
                                        }}
                                    </div>
                                </div>
                                <div class="provider__additional-info">
                                    <label
                                        >{{
                                            $tc(
                                                'yellowPages.geographicalCoverage'
                                            )
                                        }}
                                    </label>
                                    <div>
                                        {{
                                            provider.data.organizationGeographicalCoverage.replace(
                                                /[\[\]"]/g,
                                                ''
                                            )
                                        }}
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="provider__right">
                        <div class="provider__main-info">
                            <div class="provider__title">{{ provider.name }}</div>
                            <div class="provider__website">
                                <a
                                    :href="provider.data.websiteInstitutionURL"
                                    target="_blank"
                                    >{{ provider.data.websiteInstitutionURL }}</a
                                >
                            </div>
                            <div
                                class="provider__date"
                                v-if="provider.createDate"
                            >
                                <span class="provider__date__label"
                                    >{{
                                        $tc('yellowPages.registrationDate')
                                    }}:</span
                                >
                                <span>
                                    {{
                                        provider.createDate.substring(0, 10) ||
                                        'No data'
                                    }}</span
                                >
                            </div>
                            <div class="provider__description">
                                <div class="active">
                                    {{ provider.data.shortDescription }}
                                </div>
                            </div>
                            <div class="provider__principles">
                                <img
                                    v-for="(
                                        principle, index
                                    ) in availablePrinciples"
                                    :key="index"
                                    :alt="principle"
                                    :class="{
                                        disabled:
                                            provider.data.dataManagementPrinciplesLabel.indexOf(
                                                principle
                                            ) === -1
                                    }"
                                    :src="`/svg/dmp${index + 1}.svg`"
                                    :title="$tc(`${principle}`)"
                                />
                            </div>
                        </div>
                        <!-- <div>
                            <div class="provider__goals-title">
                                {{
                                    $tc(
                                        'yellowPages.sustainableDevelopmentGoals'
                                    )
                                }}:
                            </div>
                            <div>
                                <div class="provider__goals">
                                    <img
                                        v-for="goal in availableGoalsSDG"
                                        :class="{
                                            disabled:
                                                provider.data.relevantSDG.indexOf(
                                                    goal.label
                                                ) === -1
                                        }"
                                        :key="goal.value"
                                        :alt="goal.value"
                                        :src="`/img/sdg/${goal.value}.png`"
                                        :title="
                                            $tc(`yellowPages.${goal.label}`)
                                        "
                                    />
                                </div>
                            </div>
                            <div class="provider__goals-title">
                                {{ $tc('yellowPages.societalBenefitAreas') }}:
                            </div>
                            <div>
                                <div class="provider__goals-benefits">
                                    <img
                                        v-for="goal in availableGoalsSBA"
                                        :class="{
                                            disabled:
                                                provider.data.relevantSBA.indexOf(
                                                    goal.label
                                                ) === -1
                                        }"
                                        :key="goal.value"
                                        :alt="goal.value"
                                        :src="`/svg/sba/${goal.value}.svg`"F
                                        :title="
                                            $tc(`yellowPages.${goal.label}`)
                                        "
                                    />
                                </div>
                            </div>
                        </div> -->
                        <Share
                            class="provider__share-btn"
                            :url="provider.data.websiteInstitutionURL"
                            :survey="true"
                        />
                    </div>
                </div>
            </div>
            <div class="button_container">
                <button class="show_more_button" @click="showMoreResults">
                    Show more
                </button>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
// @ts-nocheck
import { Component, Vue } from 'nuxt-property-decorator'

import { YellowPagesFiltersGetters } from '@/store/yellowPagesFilters/yellow-pages-filters-getters'
import { AvailablePrinciples } from '@/data/principles'
import { SDGs } from '@/data/sdg'
import { SBAs } from '@/data/sba'
import { YellowPagesFiltersActions } from '@/store/yellowPagesFilters/yellow-pages-filters-actions'
import Share from '@/components/Share.vue'

@Component({
    components: {
        Share
    }
})
export default class YellowPagesProvidersComponent extends Vue {
    public availablePrinciples = AvailablePrinciples
    public presentPrinciples = []
    public elementsCount = 10

    public availableGoalsSDG = SDGs
    public presentSDG = []

    public availableGoalsSBA = SBAs
    public presentSBA = []

    public activeProviders = []

    public providerShareActive: string | null = null

    get currentResults() {
        return this.$store.getters[YellowPagesFiltersGetters.currentResults]
    }

    public showMoreResults() {
        this.elementsCount = this.elementsCount + 10
    }

    private async mounted() {
        const res = await this.$store.dispatch(
            YellowPagesFiltersActions.getResults
        )
        this.$store.dispatch(YellowPagesFiltersActions.setCurrentResults, res)
    }
}
</script>

<style lang="scss">
.yellow-pages-providers {
    // background: $white-transparent;
    height: 100%;

    .button_container {
        padding-top: 10px;
        padding-bottom: 10px;
        position: relative;
        width: 100%;
        display: flex;
        justify-content: center;
    }

    .show_more_button {
        background: #0661A9BF;
        padding: 10px 20px 10px 20px;
        color: white;
        &:hover{
            scale: 105%;
        }
    }

    .provider {
        display: flex;
        align-items: stretch;
        flex-wrap: wrap;
        background-color: $white-transparent;
        transition: background-color 250ms ease-in-out;
        position: relative;
        margin-bottom: 5px;

        &:hover,
        &.active {
            background-color: white;
        }

        &.active {
            .provider__image {
                &::before {
                    border-color: $grey;
                }
            }
        }

        &:last-child {
            border-bottom: none;
        }

        &__main {
            display: flex;
            flex-wrap: wrap;
            flex: 1 1 auto;
            max-width: 100%;
            flex-direction: row;

            @media (max-width: $breakpoint-sm) {
                max-width: 100%;
                flex-direction: column-reverse;
            }
        }

        &__left {
            width: 30%;

            @media (max-width: $breakpoint-sm) {
                width: 100%;
            }
        }

        &__right {
            width: 70%;
            padding: 15px 20px;
            min-height: 300px;

            @media (max-width: $breakpoint-sm) {
                padding-bottom: 0;
                width: 100%;
            }
        }

        &__image {
            flex: 0 0 225px;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            padding-right: 20px;
            padding: 15px 20px;
            height: 100%;
            background-color: $white-transparent;
            position: relative;
            max-height: 200px;

            &::before {
                content: '';
                position: absolute;
                top: 15px;
                left: 20px;
                width: calc(100% - 42px);
                height: calc(100% - 32px);
                border: 1px solid;
                border-color: transparent;
                transition: border-color ease-in-out 250ms 250ms;
            }

            @media (max-width: $breakpoint-xl) {
                flex: 0 0 175px;
            }

            @media (max-width: $breakpoint-md) {
                flex: 0 0 100%;
                padding: 20px 40px 20px 20px;
            }

            @media (max-width: $breakpoint-sm) {
                display: none;
            }

            img {
                max-width: 100%;
                max-height: 100%;
                max-width: 63%;
                max-height: 96px;
            }
        }

        &__main-info {
            display: flex;
            flex-direction: column;
            justify-content: space-between;
            height: 100%;
            padding-right: 20px;

            @media (max-width: $breakpoint-md) {
                max-width: 100%;
            }
        }

        &__title {
            font-size: 16px;
            margin-bottom: 5px;
            font-weight: bold;
            cursor: pointer;
        }

        &__description {
            line-height: 20px;
            margin-bottom: 20px;
            overflow: hidden;
            transition: max-height 450ms ease-in-out;
            max-height: 60px;
            font-size: 13px;

            @keyframes white-space-animation {
                from {
                    white-space: normal;
                }

                to {
                    white-space: nowrap;
                }
            }

            div:not(.active) {
                overflow: hidden;
                text-overflow: ellipsis;
                animation: white-space-animation 0s;
                animation-delay: 450ms;
            }
        }

        &__principles {
            img {
                width: 25px;
                margin-right: 5px;
                margin-bottom: 5px;

                &.disabled {
                    filter: grayscale(1);
                }
            }

            @media (max-width: $breakpoint-md) {
                margin-bottom: 15px;
            }
        }

        &__goals {
            margin-bottom: 20px;

            &-title {
                margin-top: 25px;
                margin-bottom: 10px;
                font-size: 14px;
                font-weight: bold;

                & + div:last-child > div {
                    margin-bottom: 0;

                    @media (max-width: $breakpoint-sm) {
                        margin-bottom: 20px;
                    }
                }
            }

            img {
                width: 54px;
                margin-right: 5px;
                margin-bottom: 5px;

                &.disabled {
                    filter: grayscale(1);
                }
            }

            &-benefits {
                img {
                    width: 40px;
                    margin-right: 5px;
                    margin-bottom: 5px;

                    &.disabled {
                        filter: grayscale(1);
                    }
                }
            }
        }

        &__side-info {
            padding-left: 20px;

            @media (max-width: $breakpoint-sm) {
                border-left: none;
                padding-left: 20px;
                max-width: none;
            }
        }

        &__date {
            font-size: 13px;
            margin-bottom: 5px;

            &__label {
                font-style: italic;
                color: $grey-darker;
            }
        }

        &__website {
            margin-bottom: 5px;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;

            a {
                font-size: 13px;
                color: $blue-medium;
            }
        }

        &__additional-info {
            margin-bottom: 5px;
            font-size: 13px;

            label,
            div {
                display: inline-block;
            }

            label {
                font-weight: bold;
                margin-bottom: 5px;
                margin-right: 10px;
            }
        }

        &__more {
            text-align: right;

            button {
                font-size: 17px;
            }
        }

        &__share-btn {
            background: $yellow;
            border-radius: 50%;
            padding: 0;
            color: white;
            margin-left: 10px;
            min-height: 32px;
            min-width: 32px;
            max-height: 32px;
            max-width: 32px;
            border: 1px solid transparent;
            position: absolute;
            top: 15px;
            right: 25px;

            &:hover,
            &.active {
                color: $yellow;
                background: white;
                border: 1px solid $yellow;
                text-decoration: none;
            }
        }

        &__share {
            padding-top: 10px;
            text-align: right;

            .social-sharing {
                padding: 10px;
                background: white;
                border: 1px solid $grey-dark;
                justify-content: flex-end;
                display: inline-flex;

                .copy-link {
                    margin-left: 0;
                }
            }
        }

        &__share__container {
            position: absolute;
            top: 40px;
            right: 25px;
        }
    }
}
</style>
