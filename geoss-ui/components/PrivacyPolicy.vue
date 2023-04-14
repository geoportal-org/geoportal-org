<template>
    <div class="privacy-policy" :class="{ active }">
        <div class="privacy-policy__text">
            <span>{{ $tc('privacyPolicy.statement') }} </span>
            <a href="/terms-conditions" target="_blank">{{ $tc('privacyPolicy.cookieLink') }}</a>
        </div>
        <div class="text-center">
            <button @click="accept()" class="blue-btn-default">{{ $tc('privacyPolicy.accept') }}</button>
        </div>
    </div>
</template>

<script lang="ts">
import { Component, Vue } from 'nuxt-property-decorator';

import { Timers } from '@/data/timers';

@Component
export default class PrivacyPolicyComponent extends Vue {
    public active = false;

    public accept() {
        this.active = false;

        const now: any = new Date();
        now.setFullYear(now.getFullYear() + 1);
        this.$cookies.set('privacy-policy-accepted', 'true', now);
    }

    private mounted() {
        if (this.$cookies.get('privacy-policy-accepted') !== 'true') {
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
    }

    &__text {
        @media (max-width: $breakpoint-md) {
            margin-bottom: 15px;
        }
    }

    a {
        color: white;
    }

    button {
        margin-left: 30px;
        padding-left: 30px;
        padding-right: 30px;
        margin-bottom: 0;
    }
}
</style>
