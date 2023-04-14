<template>
    <div v-show="enable" :class="`map-progress ${percentage == 100 ? 'complete' : 'loading'}`"
        :style="`width: ${percentage}%;`"></div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from 'nuxt-property-decorator';
import { MapGetters } from '@/store/map/map-getters';

@Component
export default class MapProgressBarComponent extends Vue {
    public percentage = this.progressBarPercentage;
    public enable = this.progressBarEnable;

    get progressBarPercentage() {
        return this.$store.getters[MapGetters.progressBarPercentage];
    }

    get progressBarEnable() {
        return this.$store.getters[MapGetters.progressBarEnable];
    }

    @Watch('progressBarPercentage')
    private onProgressBarPercentage(newValue: number) {
        this.percentage = newValue;
    }

    @Watch('progressBarEnable')
    private onProgressBarEnable(newValue: boolean) {
        this.enable = newValue;
    }
}
</script>

<style scoped lang="scss">
.map-progress {
    position: absolute;
    top: 0;
    left: 0;
    height: 5px;
    background: $blue;
    width: 0;
    transition: width .5s;
    z-index: 999;

    &.loading {
        background: $blue;
        animation-name: background-loading;
        animation-duration: 2s;
        animation-iteration-count: infinite;
    }

    &.complete {
        background: $green;
        opacity: 0;
        transition: opacity 1s ease;
        animation-name: background-complete;
        animation-duration: 1s;
    }
}

@keyframes background-loading {
    0% {
        background-color: $blue;
    }

    50% {
        background-color: $blue-light;
    }

    100% {
        background-color: $blue;
    }
}

@keyframes background-complete {
    from {
        background-color: $green;
    }

    to {
        background-color: $green;
    }
}
</style>
