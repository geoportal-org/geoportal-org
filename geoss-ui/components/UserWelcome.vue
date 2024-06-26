<template>
    <div>
        <client-only>
            <div class="user-welcome" v-if="isSignedIn">
                {{ $tc('general.hi') }}, {{ userFirstName }}
            </div>
        </client-only>
    </div>
</template>

<script>
import { Component, Vue } from 'nuxt-property-decorator';

@Component
export default class UserWelcomeComponent extends Vue {
    get isSignedIn() {
        return this.$auth.loggedIn;
    }

    get userFirstName() {
        return this.$auth.user.given_name;
    }

    mounted() {
        this.$auth.fetchUser();
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
