<template>
    <div class="user-welcome" v-if="isSignedIn">{{ $tc('general.hi') }}, {{ userFirstName }}</div>
</template>

<script lang="ts">
// @ts-nocheck
import { Component, Vue } from 'nuxt-property-decorator';
import { UserGetters } from '@/store/user/user-getters';
import { Liferay } from '@/data/global';

@Component
export default class UserWelcomeComponent extends Vue {
    get isSignedIn() {
        return !this.$store.getters[UserGetters.isSignedIn];
    }

    get userFirstName() {
        return 'xxx'
        // return (typeof Liferay !== 'undefined' ? Liferay.ThemeDisplay.getUserName().split(' ')[0] : '');
    }
}
</script>

<style lang="scss" scoped>
.user-welcome {
    position: absolute;
    z-index: 9990;
    right: 0;
    top: 0;
    background: rgba(89, 82, 17, 0.9);
    padding: 2px 45px 2px 45px;
    color: #fff;
    font-size: 15px;
}
</style>
