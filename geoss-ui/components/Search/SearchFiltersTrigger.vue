<template>
    <div class="trigger" :class="{ active: !onlyAdvanced && (active || !activeSection) }">
        <span data-tutorial-tag="searchbar-advanced-search"
            :class="{ 'disable-container': onlyAdvanced && generalFiltersInChange }" @click="toggleFilters()"
            v-show="onlyAdvanced">{{ $tc('generalFilters.advancedSearch') }}</span>
        <span @click="toggleFilters()" v-show="!onlyAdvanced && !active">{{ $tc('generalFilters.filters') }}</span>
        <button data-tutorial-tag="filters-filter-by" v-show="!onlyAdvanced && activeSection && active"
            @click="onSectionChange('nonadvanced')" class="trigger__filters-section filter-by"
            :class="{ active: activeSection !== 'advanced' }">
            {{ $tc('filters.filterBy') }}</button>
        <button data-tutorial-tag="filters-advanced" v-show="!onlyAdvanced && activeSection && active"
            @click="onSectionChange('advanced')" class="trigger__filters-section advanced"
            :class="{ active: activeSection === 'advanced' }">
            {{ $tc('filters.advanced') }}</button>
        <button @click="toggleFilters()" class="trigger__visibility" :title="$tc('filters.toggle')"
            :aria-label="$tc('filters.toggle')"
            :class="{ active: active, 'active-section': !onlyAdvanced, 'disable-container': onlyAdvanced && generalFiltersInChange }">
            <i></i>
        </button>
    </div>
</template>

<script lang="ts">
import { Component, Vue, Prop, Emit } from 'nuxt-property-decorator';
import { GeneralFiltersGetters } from '@/store/generalFilters/general-filters-getters';

@Component
export default class SearchFiltersTriggerComponent extends Vue {
    @Prop({ required: true, type: Boolean }) public active!: boolean;
    @Prop({ type: String }) public activeSection!: any;
    @Prop({ type: Boolean, required: true }) public onlyAdvanced!: boolean;

    get generalFiltersInChange() {
        return this.$store.getters[GeneralFiltersGetters.inChangeProcess];
    }

    public toggleFilters() {
        if (!this.onlyAdvanced || !this.generalFiltersInChange) {
            this.toggle();
        }
    }

    @Emit()
    public onSectionChange(section: string) {
        return section;
    }

    @Emit()
    private toggle() {
        return true;
    }
}
</script>

<style lang="scss" scoped>
.trigger {
    color: white;
    font-size: 20px;
    text-transform: uppercase;
    font-weight: bold;
    white-space: nowrap;
    display: flex;
    align-items: center;
    justify-content: flex-end;
    cursor: pointer;
    width: 100%;
    background-color: $blue-transparent;
    padding: 10px 20px;
    height: 44px;

    @media (max-width: $breakpoint-sm) {
        font-size: 17px;

        .disable-container {
            display: none;
        }
    }

    &.active {
        background-color: transparent;
        padding: 0;
    }

    &__filters-section {
        width: calc(50% - 13px);
        color: white;
        text-transform: uppercase;
        font-size: 20px;
        padding: 10px 0;
        position: relative;
        min-height: 100%;

        @media (max-width: $breakpoint-sm) {
            font-size: 17px;
        }

        &.filter-by {
            margin-right: 2px;
        }

        &.active {
            background: $blue-transparent;
            border-top-left-radius: 20px;
            border-top-right-radius: 19px;
            height: calc(100% + 3px);

            &:before,
            &:after {
                content: '';
                height: 30px;
                position: absolute;
                left: -15px;
                bottom: -15px;
                width: 30px;
                background: radial-gradient(circle at 0 0, rgba(0, 0, 0, 0) 14px, $blue-transparent 15px);
                background-size: 50% 50%;
                background-repeat: no-repeat;
            }

            &:after {
                left: auto;
                right: -30px;
                background: radial-gradient(circle at 100% 0, rgba(0, 0, 0, 0) 14px, $blue-transparent 15px);
                background-size: 50% 50%;
                background-repeat: no-repeat;
            }

            &.filter-by {
                border-top-left-radius: 0;

                &:before {
                    display: none;
                }
            }
        }

        &:not(.active) {
            background: $green-transparent;
            border-bottom-right-radius: 15px;

            &:after {
                content: '';
                height: 30px;
                position: absolute;
                right: -30px;
                top: 0;
                width: 30px;
                background-image: radial-gradient(circle at 100% 100%, rgba(0, 0, 0, 0) 14px, $green-transparent 15px);
                background-size: 50% 50%;
                background-repeat: no-repeat;
            }

            &.advanced {
                border-bottom-right-radius: 0;
                border-bottom-left-radius: 15px;
                border-top-right-radius: 17px;

                &:after {
                    background-image: radial-gradient(circle at 0 100%, rgba(0, 0, 0, 0) 14px, $green-transparent 15px);
                    background-size: 50% 50%;
                    background-repeat: no-repeat;
                    left: -15px;
                    right: auto;
                }

                &:before {
                    content: '';
                    height: 30px;
                    position: absolute;
                    left: auto;
                    right: -30px;
                    bottom: -15px;
                    width: 30px;
                    background: radial-gradient(circle at 100% 0%, rgba(0, 0, 0, 0) 14px, $green-transparent 15px);
                    background-size: 50% 50%;
                    background-repeat: no-repeat;
                }
            }
        }
    }

    &__visibility {
        width: 31px;
        height: 24px;
        position: relative;

        &.active {
            i {

                &:before,
                &:after {
                    transform: rotate(-45deg);
                }

                &:after {
                    transform: rotate(45deg);
                }
            }

            &.active-section {
                padding-left: 0;
                margin-left: 2px;
                width: 60px;
                height: 44px;
                background: $green-transparent;
                border-bottom-left-radius: 15px;

                &:after {
                    content: '';
                    height: 30px;
                    position: absolute;
                    left: -15px;
                    top: 0;
                    width: 30px;
                    background: radial-gradient(circle at 0 100%, rgba(0, 0, 0, 0) 14px, $green-transparent 15px);
                    background-size: 50% 50%;
                    background-repeat: no-repeat;
                }

                i {

                    &:before,
                    &:after {
                        top: 20px;
                        left: 16px;
                        transform: rotate(-45deg);
                    }

                    &:after {
                        left: 25px;
                        transform: rotate(45deg);
                    }
                }
            }
        }

        i {

            &:before,
            &:after {
                content: '';
                top: 10px;
                width: 14px;
                height: 3px;
                background: white;
                left: 7px;
                display: block;
                position: absolute;
                transform: rotate(45deg);
            }

            &:after {
                transform: rotate(-45deg);
                left: 15px;
            }
        }
    }
}
</style>
