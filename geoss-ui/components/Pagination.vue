<template>
    <div class="pagination" data-tutorial-tag="search-container-pagination">
        <span class="pagination__summary" v-if="showSummary">
            {{`${$tc('pagination.showing')} ${resultsFrom()} - ${resultsTo()} ${$tc('pagination.of')} ${total} ${$tc('pagination.results')}.`}}
        </span>
        <button :disabled="startIndex === initialIndex" @click="switchPageResults('prev', true)" class="pagination__first disabled-transparent"></button>
        <button :disabled="startIndex === initialIndex" @click="switchPageResults('prev')" class="pagination__prev disabled-transparent"></button>
        <input ref="input" type="text" :value="input" @input="validateInput(false)" @focus="onFocus" @blur="validateInput(true)" @keyup.enter="blurInput()" />
        <button :disabled="currentPage === totalPages" @click="switchPageResults('next')" class="pagination__next disabled-transparent"></button>
        <button :disabled="currentPage === totalPages" @click="switchPageResults('next', true)" class="pagination__last disabled-transparent"></button>
    </div>
</template>

<script lang="ts">
// @ts-nocheck
import { Component, Prop, Vue, Emit } from 'nuxt-property-decorator';

@Component
export default class PaginationComponent extends Vue {
    @Prop({default: false, type: Boolean}) private showSummary!: boolean;
    @Prop({required: true, type: Number}) private startIndex!: number;
    @Prop({required: true, type: Number}) private resultsPerPage!: number;
    @Prop({required: true, type: Number}) private total!: number;
    @Prop({default: 0, type: Number}) private initialIndex!: number;

    private lastValue = null;

    get currentPage() {
        return Math.floor((this.startIndex + this.initialIndex) / this.resultsPerPage) + 1;
    }

    get totalPages() {
        const val = Math.floor(this.total / this.resultsPerPage);
        return (val !== this.total / this.resultsPerPage ? val + 1 : val);
    }

    get input() {
        return `${this.currentPage} of ${this.totalPages}`;
    }

    public resultsFrom() {
        return ((this.currentPage - 1) * this.resultsPerPage) + 1;
    }

    public resultsTo() {
        const remainder = this.currentPage === this.totalPages ? this.total % this.resultsPerPage : 0;
        return remainder ? (this.currentPage - 1) * this.resultsPerPage + remainder : this.currentPage * this.resultsPerPage;
    }

    public setCaretPosition(pos: any) {
        const inputElem = this.$refs.input as HTMLInputElement;
        inputElem.focus();
        inputElem.setSelectionRange(pos, pos);
    }

    public onFocus() {
        const pos = (this.$refs.input as HTMLInputElement).value.indexOf('of');
        setTimeout(() => {
            this.setCaretPosition(pos - 1);
        }, 0);
    }

    public blurInput() {
        const inputElem = this.$refs.input as HTMLInputElement;
        inputElem.blur();
    }

    public validateInput(submit: boolean) {
        const inputElem = this.$refs.input as HTMLInputElement;

        const nonEditablePart = this.input.substr(this.input.indexOf('of') - 1);
        const edittedPart = inputElem.value.substr(0, inputElem.value.indexOf('of') - 1);

        if (inputElem.value.indexOf(nonEditablePart) === -1 ||
            (edittedPart && !/^[1-9]([0-9]*)$/.test(edittedPart)) ||
            (submit && !edittedPart) || parseInt(edittedPart, 10) > this.totalPages) {
            inputElem.value = (blur || !this.lastValue ? this.input : this.lastValue);
            if (!submit) {
                this.onFocus();
            } else {
                this.lastValue = null;
            }
        } else {
            this.lastValue = inputElem.value;
        }

        if (submit) {
            const startIndex = (parseInt(edittedPart, 10) - 1) * this.resultsPerPage + this.initialIndex;

            if (this.startIndex !== startIndex) {
                this.onStartIndexChange(startIndex);
            }
        }
    }

    public switchPageResults(direction: 'prev' | 'next', max?: boolean) {
        let startIndex: number = this.startIndex;
        if (direction === 'prev') {
            if (max) {
                startIndex = this.initialIndex;
            } else {
                startIndex = (startIndex <= this.resultsPerPage ? this.initialIndex : startIndex - this.resultsPerPage);
            }
        } else {
            if (max) {
                startIndex = Math.floor(this.total / this.resultsPerPage) * this.resultsPerPage;
                if (startIndex === this.total) {
                    startIndex = this.total - this.resultsPerPage + this.initialIndex;
                }
            } else {
                startIndex = startIndex + this.resultsPerPage;
            }
        }
        this.onStartIndexChange(startIndex);
    }

    @Emit()
    private onStartIndexChange(value: number) {
        return value;
    }
}
</script>

<style lang="scss" scoped>
.pagination {
    flex: 0 0 auto;
    flex-grow: 1;
    flex-basis: 0;
    background-color: $green-transparent;
    display: flex;
    color: white;
    align-items: center;
    justify-content: flex-end;
    font-size: 14px;
    padding: 10px 20px;
    margin: 5px 0 0;

    @media (max-width: $breakpoint-md) {
        padding-left: 10px;
    }

    @media (max-width: $breakpoint-sm) {
        padding-left: 20px;
        justify-content: center;
    }

    &:only-child {
        margin-left: auto;
    }

    &__summary {
        margin-right: auto;
    }

    &__prev,
    &__next,
    &__first,
    &__last {
        font-size: 14px;
        text-transform: uppercase;
        color: white;
        position: relative;
        width: 20px;
        height: 15px;

        &:before,
        &:after {
            content: '';
            position: absolute;
            width: 10px;
            height: 2px;
            background-color: white;
            left: 3px;
            top: 4px;
            transform: rotate(-40deg);
        }

        &:after {
            top: 9px;
            transform: rotate(40deg);
        }

        &:disabled {
            border-color: rgba($grey, 0.75);

            &:before,
            &:after {
                background-color: rgba($grey, 0.75);
            }
        }
    }

    &__next,
    &__last {
        &:before, &:after {
            left: auto;
            right: 3px;
            transform: rotate(40deg);
        }

        &:after {
            transform: rotate(-40deg);
        }
    }

    &__first {
        border-left: 2px solid white;
    }

    &__last {
        border-right: 2px solid white;
    }

    input {
        background: rgba(white, 0.25);
        border: 1px solid transparent;
        border-radius: 15px;
        padding: 3px 15px;
        width: 130px;
        color: white;
    }
}
</style>
