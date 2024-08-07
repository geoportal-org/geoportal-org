<template>
    <div class="custom-select">
        <button ref="optionsTrigger" class="custom-select__trigger" @click="toggleOpened":disabled="!options.length || buttonDisabled"
            v-click-outside="{ fn: closeContainer, excludeSelectors: '.custom-select__container' }" :class="{ active: opened }">
            <slot name="trigger" v-bind:selectedOption="selectedOption">
                <span v-line-clamp:20="2">{{title}}</span>
            </slot>
        </button>
        <div class="custom-select__icons">
            <button v-if="options.length" @click="clear()" class="custom-select__clear cross" :aria-label="$tc('generalFilters.cancel')" v-show="clearable && ((value && !multiple) || (multiple && value.length))"></button>
            <span class="custom-select__icon">
                <slot name="icon"></slot>
            </span>
        </div>
        <portal v-if="options.length && opened" to="custom-select-container" :disabled="!appendToBody">
            <div ref="optionsContainer" class="custom-select__container" :class="{appendToBody}" :style="optionsContainerStyles">
                <div class="custom-select__search" v-if="filterable">
                    <span class="search__icon">
                        <i class="icomoon-search"></i>
                    </span>
                    <input type="text" :value="searchValue" @input="searchValue = $event.target.value" :placeholder="$tc('placeholders.search')" />
                    <button class="search__clear" :title="$tc('placeholders.clearSearch')" :aria-label="$tc('placeholders.clearSearch')" @click="clearSearch()">
                        <i class="icomoon-close"></i>
                    </button>
                </div>
                <div class="custom-select__options" v-bar>
                    <div>
                        <div v-for="option of filteredOptions" @click="toggleSelectOption(option)"
                            :key="`${option[idProp]}-${option[textProp]}`" class="custom-select__option"
                            :class="{ selected: isSelected(option[idProp]), 'with-checkbox': multiple, disabled: option.disabled }">
                            <slot name="option" v-bind:option="option">
                                <span>{{ option[textProp] }}</span>
                            </slot>
                        </div>
                    </div>
                </div>
            </div>
        </portal>
    </div>
</template>

<script lang="ts">
// @ts-nocheck
import { Component, Prop, Vue, Model } from 'nuxt-property-decorator';

@Component({})
export default class CustomSelectComponent extends Vue {
    @Prop({ default: false, type: Boolean }) public filterable!: boolean;
    @Prop({ default: 'text', type: String }) public textProp!: string;
    @Prop({ default: 'id', type: String }) public idProp!: string;
    @Prop({ default: false, type: Boolean }) public multiple!: boolean;
    @Prop({ default: true, type: Boolean }) public clearable!: boolean;
    @Prop({ default: false, type: Boolean }) public hideSelectedOption!: boolean;
    @Prop({ default: false, type: Boolean }) public buttonDisabled!: boolean;
    public opened = false;
    public searchValue = '';
    public optionsContainerStyles = null;

    @Prop({ default: () => [], type: Array }) public options!: any[];
    @Prop({ default: false, type: Boolean }) public appendToBody!: boolean;
    @Prop({ default: '', type: String }) public placeholder!: string;
    @Model('input') public value!: string | number | string[] | number[];
    public scrollableParent: HTMLElement | null = null;

    get title() {
        if (this.multiple) {
            const value = (this.value as Array<string | number>);
            if (!value.length) {
                return this.placeholder;
            } else if (value.length === 1) {
                const selectedOption = this.options.find(option => option[this.idProp] === value[0]);
                return selectedOption ? selectedOption[this.textProp] : this.placeholder;
            } else {
                return `${value.length} selected`;
            }
        } else {
            const selectedOption = this.options.find(option => option[this.idProp] === this.value);
            return selectedOption ? selectedOption[this.textProp] : this.placeholder;
        }
    }

    get filteredOptions() {
        let filteredOptions = this.options.filter(item => item[this.textProp].toLowerCase().indexOf(this.searchValue.toLowerCase()) !== -1);

        if (this.hideSelectedOption && this.value) {
            filteredOptions = filteredOptions.filter(option => {
                if (this.multiple) {
                    return !this.selectedOption.find(selectedOption => selectedOption[this.idProp] === option[this.idProp]);
                } else {
                    return option[this.idProp] !== this.selectedOption[this.idProp];
                }
            });
        }
        if (!this.multiple && !filteredOptions.length) {
            return [{
                title: 'No results',
                disabled: true
            }];
        }
        return filteredOptions;
    }

    get selectedOption() {
        let selectedOption = null;
        if (this.multiple) {
            const value = (this.value as Array<string | number>);
            selectedOption = this.options.find(option => !!value.find(val => option[this.idProp] === val));
        } else {
            selectedOption = this.options.find(option => option[this.idProp] === this.value);
        }

        return selectedOption;
    }

    public closeContainer() {
        this.opened = false;
        this.searchValue = '';
        if (this.appendToBody) {
            this.removeScrollListener();
        }
    }

    public isSelected(optionId: string | number) {
        if (this.multiple) {
            return (this.value as Array<string | number>).indexOf(optionId) !== -1;
        } else {
            return this.value === optionId;
        }
    }

    public async toggleOpened() {
        this.opened = !this.opened;
        this.searchValue = '';
        if (this.opened && this.appendToBody) {
            await this.$nextTick();
            this.setPositionAndSize();
            this.addScrollListener();
        } else if (this.appendToBody) {
            this.removeScrollListener();
        }
    }

    public toggleSelectOption(option) {
        if (option.disabled) {
            return;
        }
        if (this.multiple) {
            const value = JSON.parse(JSON.stringify(this.value as Array<string | number>));
            const index = value.indexOf(option[this.idProp]);
            if (index !== -1) {
                value.splice(index, 1);
            } else {
                value.push(option[this.idProp]);
            }
            this.$emit('input', value);
        } else {
            if (this.value !== option[this.idProp]) {
                this.$emit('input', option[this.idProp]);
            }
            this.closeContainer();
        }
    }

    public clearSearch() {
        this.searchValue = '';
    }

    public clear() {
        if (this.multiple) {
            this.$emit('input', []);
        } else {
            this.$emit('input', '');
        }
    }

    private getScrollParent(node: HTMLElement): HTMLElement | null {
        if (!node) {
            return null;
        }

        if (node.scrollHeight > node.clientHeight) {
            return node;
        } else {
            return this.getScrollParent((node.parentNode as HTMLElement));
        }
    }

    private setPositionAndSize() {
        const optionsContainer = (this.$refs.optionsContainer as HTMLElement);
        const bounds = (this.$refs.optionsTrigger as HTMLElement).getBoundingClientRect();
        const boundsBottom = window.innerHeight - bounds.top - bounds.height;
        const width = bounds.width < 300 ? 300 : bounds.width;
        const left = bounds.left > (window.innerWidth - width) ? (window.innerWidth - width) : bounds.left;
        let top = bounds.top + bounds.height + 3 + 'px';
        let bottom = 'auto';
        if (optionsContainer && boundsBottom < optionsContainer.clientHeight + 5) {
            top = 'auto';
            bottom = boundsBottom + bounds.height + 3 + 'px';
        }
        this.optionsContainerStyles = {
            'width': width + 'px',
            'top': top,
            'bottom': bottom,
            'left':  left + 'px',
            'z-index': 9997
        };
    }

    private addScrollListener() {
        this.scrollableParent = this.getScrollParent((this.$refs.optionsTrigger as HTMLElement));
        if (this.scrollableParent) {
            this.scrollableParent.addEventListener('scroll', this.setPositionAndSize);
        }
    }

    private removeScrollListener() {
        if (this.scrollableParent) {
            this.scrollableParent.removeEventListener('scroll', this.setPositionAndSize);
        }
    }
}
</script>

<style lang="scss">
.custom-select {
    position: relative;

    &__trigger {
        font-size: 14px;
        padding: 0 50px 0 30px;
        background: white;
        height: 37px;
        display: flex;
        align-items: center;
        width: 100%;

        &:disabled {
            opacity: 0.65;
            color: #333 !important;

            &:hover {
                color: #333 !important;
            }
        }

        &.active:after {
            transform: rotate(180deg);
        }

        &:after {
            display: inline-block;
            width: 0;
            height: 0;
            vertical-align: top;
            border-top: 4px solid black;
            border-right: 4px solid transparent;
            border-left: 4px solid transparent;
            content: '';
            margin-left: 10px;
        }
    }

    &__icons {
        position: absolute;
        right: 5px;
        top: 50%;
        transform: translateY(-50%);
        display: flex;
        align-items: center;
    }

    &__icon i {
        font-size: 21px;
        color: $blue;
        font-weight: bold;
    }

    &__clear {
        &:before,
        &:after {
            background-color: black;
            width: 18px;
            left: 3px;
        }
    }

    &__container {
        position: absolute;
        max-height: 200px;
        background: white;
        z-index: 6;
        top: calc(100% + 3px);
        left: 0;
        min-width: 100%;
        border: 1px solid rgba(0, 0, 0, 0.2);
        border-radius: 4px;
        display: flex;
        flex-direction: column;
        max-width: 100%;

        &.appendToBody {
            min-width: 0;
        }
    }

    &__search {
        margin: 5px;
        width: calc(100% - 10px);
        display: flex;
        height: 37px;
        align-items: stretch;
        font-size: 14px;
        flex: 1 0 auto;

        .search__icon {
            background-color: #eee;
            padding: 10px 12px;
            border: 1px solid #ccc;
            border-top-left-radius: 4px;
            border-bottom-left-radius: 4px;
        }

        input {
            flex: 1 1 auto;
            border: none;
            outline: 0;
            height: auto;
            padding: 0 12px;
            border-top: 1px solid #ccc;
            border-bottom: 1px solid #ccc;
            min-width: calc(100% - 80px);
        }

        .search__clear {
            padding: 10px 12px;
            border: 1px solid #ccc;
            border-top-right-radius: 4px;
            border-bottom-right-radius: 4px;

            &:hover {
                background-color: #eee;
            }

            i {
                font-size: 14px;
            }
        }
    }

    &__options {
        overflow: auto;
    }

    &__option {
        font-size: 14px;
        padding: 13px 20px 13px 10px;
        position: relative;
        overflow: hidden;
        width: 100%;
        text-overflow: ellipsis;

        &:not(.disabled) {
            cursor: pointer;
        }

        &.with-checkbox {
            padding: 13px 20px 13px 40px;
            &:before {
                content: '';
                display: block;
                width: 18px;
                height: 18px;
                border: 2px solid #333;
                position: absolute;
                left: 14px;
                top: 11px;
                box-sizing: border-box;
            }

            &:after {
                content: 'x';
                position: absolute;
                left: 20px;
                top: 12px;
                font-weight: 700;
                color: #fff;
                display: none;
            }
        }

        &:hover:not(.disabled):not(.selected) {
            background-color: #eee;
        }

        &.selected {
            background-color: $blue;
            color: white;

            &:before {
                border-color: white;
            }

            &:after {
                display: block;
            }
        }
    }
}

.with-dockbar {
    .custom-select {
        &__container {
            &.appendToBody {
                margin-top: -44px;
            }
        }
    }
}
</style>
