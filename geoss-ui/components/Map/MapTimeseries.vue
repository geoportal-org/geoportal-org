<template>
    <div class="map-timeseries" v-if="showTimeline">
        <p class="timeseries-label">{{ dimensions[0] }}</p>
        <vue-slider
            v-model="currentTime"
            :data="dimensions"
            :tooltip="'active'"
        ></vue-slider>
        <p class="timeseries-label">{{ dimensions[dimensions.length -1 ] }}</p>
    </div>
</template>

<script lang="ts">
import { Component, Vue } from 'nuxt-property-decorator'
import VueSlider from 'vue-slider-component'
import { MapActions } from '~/store/map/map-actions'
import { MapGetters } from '~/store/map/map-getters'

@Component({
    components: {
        VueSlider
    }
})
export default class MapTimeseriesComponent extends Vue {
    get dimensions() {
        return this.$store.getters[MapGetters.dimensions]
    }

    get showTimeline() {
        return this.$store.getters[MapGetters.showTimeline]
    }

    get currentTime() {
        return this.$store.getters[MapGetters.currentTime]
    }

    set currentTime(value: any) {
        this.$store.dispatch(MapActions.setCurrentTime, value)
    }
}
</script>

<style lang="scss" scoped>
.timeseries-label {
    color:white;
    align-self: center;
    margin-right: 15px;
    margin-left: 15px;
}

.vue-slider {
    margin-left: 0px !important;
    width: 500px !important;
}

.vue-slider-dot-tooltip {
    min-width: 82px !important;
    position:relative !important;
}

.map-timeseries {
    position: fixed;
    left: 50%;
    transform: translateX(-50%);
    bottom: 50px;
    white-space: nowrap;
    z-index: 10;
    background-color: $blue-transparent;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px 5px;
    font-size: 13px;
    text-decoration: none;
    flex-direction: row;
    border-radius: 10px;
}
</style>
