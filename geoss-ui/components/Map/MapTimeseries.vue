<template>
    <div class="map-timeseries" v-if="showTimeline">
        <p class="timeseries-label">Timeseries</p>
        <vue-slider
            v-model="currentTime"
            :data="dimensions"
            :tooltip="'active'"
        ></vue-slider>
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
    public data = ['A', 'B', 'C', 'D', 'E', 'F', 'G']

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
    color:black;
    align-self: center;
    margin-right: 30px;
    font-size: 20px;

}

.vue-slider {
    margin-left: 0px !important;
    width: 600px !important;
}

.vue-slider-dot-tooltip-inner {
    min-width: 82px;
}

.map-timeseries {
    position: fixed;
    bottom: 50px;
    right: 10px;
    min-width: 700px;
    z-index: 10;
    background-color: rgba(140, 140, 140, 0.2);
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px 10px;
    font-size: 13px;
    text-decoration: none;
    flex-direction: row;
    border-radius: 10px;
}
</style>
