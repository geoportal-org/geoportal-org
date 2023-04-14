<template>
    <div class="spinner" :class="{ visible: visible }">
        <div class="spinner__wrapper">
            <img class="spinner__img" :src="`/img/spinner.png`" alt="Spinner" />
            <div v-show="text" class="spinner__text">{{ text }}</div>
            <button v-show="showCancel" class="spinner__cancel-button" title="Cancel request"
                @click="cancelRequest()"></button>
        </div>
    </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import SpinnerService from '@/services/spinner.service';
import { GeneralApiService } from '@/services/general.api.service';

@Component
export default class SpinnerComponent extends Vue {
    public visible = false;
    public text = null;
    public showCancel = false;

    private counter = 0;

    public cancelRequest() {
        GeneralApiService.cancelCurrentrequest();
    }

    private created() {
        SpinnerService.emitter.$on('show', (data: { text: string, showCancel: boolean }) => {
            if (!this.counter) {
                this.visible = true;
            }
            this.counter++;
        });

        SpinnerService.emitter.$on('hide', (force?: boolean) => {
            this.counter--;
            if (force) {
                this.counter = 0;
            }
            if (!this.counter) {
                this.visible = false;
            }
        });
    }
}
</script>

<style lang="scss" scoped>
.spinner {
    width: 100%;
    height: 100%;
    top: 0;
    left: -200%;
    position: fixed;
    z-index: 9998;
    background-color: rgba(0, 0, 0, 0.5);
    opacity: 0;
    transition: opacity 450ms ease-in-out, left 0ms 450ms ease-in-out;

    &.visible {
        left: 0;
        opacity: 1;
        transition: opacity 450ms ease-in-out;
    }

    &__wrapper {
        display: flex;
        align-items: center;
        position: absolute;
        left: 50%;
        top: 50%;
        transform: translate(-50%, -50%);
    }

    &__img {
        width: 120px;
        height: 120px;
        animation: spin 2s linear infinite;

        @media (max-width: $breakpoint-sm) {
            width: 50px;
            height: 50px;
        }
    }

    &__text {
        margin-left: 20px;
        color: #fff;
        font-size: 52px;
        white-space: nowrap;

        @media (max-width: $breakpoint-sm) {
            font-size: 32px;
            margin-left: 10px;
        }
    }

    &__cancel-button {
        background: $red;
        position: relative;
        height: 30px;
        width: 30px;
        border-radius: 50%;
        left: 10px;
        bottom: 30px;

        @media (max-width: $breakpoint-sm) {
            height: 22px;
            width: 22px;
            left: 5px;
        }

        &:before,
        &:after {
            content: '';
            position: absolute;
            left: 5px;
            top: 13px;
            height: 3px;
            width: 20px;
            background: white;
            transform: rotate(45deg);

            @media (max-width: $breakpoint-sm) {
                left: 3px;
                top: 10px;
                width: 16px;
            }
        }

        &:after {
            transform: rotate(-45deg);
        }
    }

    @keyframes spin {
        0% {
            transform: rotate(0deg);
        }

        100% {
            transform: rotate(360deg);
        }
    }
}
</style>
