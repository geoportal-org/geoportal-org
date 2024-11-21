<template>
    <div class="privacy-policy" :class="{active}">
        <div class="privacy-policy__text">
            <span>{{$tc('privacyPolicy.statement')}} </span>
            <a href="/cookie-notice" target="_blank">{{$tc('privacyPolicy.cookieLink')}}</a><span> {{$tc('privacyPolicy.cookieText')}}</span>
        </div>
        <div class="text-center">
            <button @click="accept()" class="blue-btn-default">{{$tc('privacyPolicy.accept')}}</button>
            <button @click="decline()" class="blue-btn-default">{{$tc('privacyPolicy.decline')}}</button>
        </div>
    </div>
</template>

<script lang="ts">
import { Component, Vue } from 'nuxt-property-decorator';

import { Timers } from '@/data/timers';

const _PAQ = '_paq';

@Component
export default class PrivacyPolicyComponent extends Vue {
    public active = false;

    public accept() {
        this.active = false;

        if (window[_PAQ]) {
            window[_PAQ].push(['rememberConsentGiven']);
        }
    }

    public decline() {
        this.active = false;

        if (window[_PAQ]) {
            window[_PAQ].push(['forgetConsentGiven']);
        }
    }

    private mounted() {
        const cookie = this.$cookies.get('mtm_consent');
        const cookieDeclined = this.$cookies.get('mtm_consent_removed');

        if (!cookie && !cookieDeclined) {
            setTimeout(() => {
                this.active = true;
            }, Timers.privacyPolicy);
        }
    }
}
</script>

<style lang="scss" scoped>
.privacy-policy {
    background: $green-transparent;
    color: white;
    font-size: 17px;
    padding: 20px;
    position: fixed;
    bottom: 0;
    left: 0;
    width: 100%;
    z-index: 9996;
    display: flex;
    transform: translateY(100%);
    transition: transform 450ms ease-in-out;

    @media (max-width: $breakpoint-md) {
        padding: 10px;
        flex-direction: column;
    }

    &.active {
        transform: translateY(0);

        @media (min-width: $breakpoint-md) {
            gap: 20px;
        }
    }

    &__text {
        @media (max-width: $breakpoint-md) {
            margin-bottom: 10px;
        }
    }

    a {
        color: $blue;
    }

    button {
        padding-left: 30px;
        padding-right: 30px;
        margin-bottom: 0;
        display: flex;
        align-items: center;
    }

    .text-center {
        display: flex;
        justify-content: space-evenly;

        @media (min-width: $breakpoint-md) {
            max-height: 30px;
            gap: 20px;
        }
    }
}
</style>
