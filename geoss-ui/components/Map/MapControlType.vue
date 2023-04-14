<template>
    <div class="map-control-type">
        <button class="map-control-type__trigger disabled-transparent" :class="{ active: showSlider }"
            :title="$tc('mapControls.changeBasemap')" :disabled="disabled"
            v-click-outside="{ fn: closeSlider, excludeSelectors: '.map-control-type__slider-wrapper' }"
            @click="toggleSlider()">
            <i class="icomoon-map-type"></i>
        </button>
        <div class="map-control-type__slider-wrapper" :class="{ active: showSlider }">
            <!-- <Carousel class="map-control-type__slider"
					ref="slider"
					:scrollPerPage="false"
					:mouse-drag="true"
					:paginationEnabled="false"
					:per-page="3"
					:navigationNextLabel="''"
					:navigationPrevLabel="''"
					:navigationEnabled="true">
				<Slide v-for="(type, key) in types" :key="key" class="map-control-type__slide" :class="{active: mapActiveLayerTileId === key}">
					<button @touchstart="touchMapType(type.title)" @touchend="touchMapTypeReset" @click="switchMapLayerTile(key)" :disabled="type.disabled" :title="`${type.title} ${type.disabled ? '(Unavailable)' : ''}`">
						<img :alt="type.title" :src="`${staticPath()}/img/layer-tiles/${type.img}`" />
						<span class="mapname-tip" :class="{show: showTip == type.title}">{{ type.title }}</span>
					</button>
				</Slide>
			</Carousel> -->
        </div>
    </div>
</template>

<script lang="ts">
import { Component, Vue, Prop } from 'nuxt-property-decorator';
// import { Carousel, Slide } from 'vue-carousel';

import LayerTilesService from '@/services/map/layer-tiles.service';
import { MapActions } from '@/store/map/map-actions';
import { MapGetters } from '@/store/map/map-getters';

@Component({
    components: {
        // Carousel,
        // Slide
    }
})
export default class MapControlTypeComponent extends Vue {
    @Prop({ default: false, type: Boolean }) public disabled!: boolean;
    public types = LayerTilesService;
    public showTip = '';
    public tipTimeout: any;

    public showSlider = false;

    get mapActiveLayerTileId() {
        return this.$store.getters[MapGetters.activeLayerTileId];
    }

    public switchMapLayerTile(tileId: string) {
        this.$store.dispatch(MapActions.setActiveLayerTileId, tileId);
    }

    public toggleSlider() {
        this.showSlider = !this.showSlider;
        let activeTileIndex = 0;
        for (const prop of Object.keys(LayerTilesService)) {
            if (prop === this.mapActiveLayerTileId) {
                break;
            }
            activeTileIndex++;
        }
        (this.$refs.slider as any).goToPage(activeTileIndex);
    }

    public closeSlider() {
        this.showSlider = false;
    }

    public touchMapType(name: string) {
        this.showTip = name;
    }

    public touchMapTypeReset() {
        clearTimeout(this.tipTimeout);
        this.tipTimeout = setTimeout(() => this.showTip = '', 666);
    }
}
</script>

<style scoped lang="scss">
.map-control-type {
    position: relative;
    width: auto;
    height: auto;
    position: relative;
    bottom: 5px;
    margin-bottom: 10px;

    .mapname-tip {
        color: #fff;
        font-size: 11px;
        left: 50%;
        opacity: 0;
        position: absolute;
        top: 125%;
        transition: 0.2s ease all;
        transform: translateX(-50%);
        white-space: nowrap;

        &.show {
            opacity: 1;
            top: 115%;
            transition: 0.2s ease all;

            &:after {
                border-style: solid;
                border-width: 4px 4px 0 4px;
                border-color: rgba(255, 255, 255, 0.33) transparent transparent transparent;
                content: '';
                display: block;
                height: 0;
                left: 50%;
                position: absolute;
                top: -60px;
                transform: translateX(-50%);
                width: 0;
                z-index: 10;
            }
        }
    }

    @media (max-width: $breakpoint-lg) {
        bottom: -5px;
        left: -10px;
        margin-right: 12px;
    }

    &__trigger {
        color: white;
        width: 32px;
        height: 32px;
        transition: background-color 250ms ease-in-out;
        border-radius: 50%;

        &.active {
            background-color: $blue-transparent;
        }

        i {
            font-size: 32px;

            @media (max-width: $breakpoint-lg) {
                font-size: 29px;
            }
        }
    }

    &__slider-wrapper {
        overflow: hidden;
        position: absolute;
        width: 0;
        height: 0;
        right: calc(100% + 10px);
        top: 0;
        transition: width 0.5s ease-in-out, height 0.5s ease-in-out;

        @media (max-width: $breakpoint-lg) {
            bottom: calc(100% + 5px);
            left: -115px;
            right: auto;
            top: auto;
            width: 310px;
        }

        @media (max-width: $breakpoint-sm) {
            left: -95px;
        }

        &.active {
            width: 310px;
            height: 100px;
        }
    }

    &__slider {
        min-width: 310px;
        min-height: 100px;
        max-width: 310px;
        max-height: 100px;
        padding: 0 40px;
        top: 0;
        right: 0;
        position: absolute;
        background: $blue-transparent;
        display: flex;
        align-items: center;
        border-radius: 10px;

        @media (max-width: $breakpoint-sm) {
            // /deep/ .VueCarousel {
            //     &-wrapper {
            //         height: 100px;
            //     }

            //     &-inner {
            //         margin-top: 27px;
            //     }
            // }
        }

    }

    &__slide {

        &.active,
        &:hover {
            button:not(:disabled) {
                border-color: #EB6220;
            }
        }

        button {
            margin: 0 5px;
            border: 2px solid transparent;
            position: relative;

            &:disabled {
                img {
                    filter: grayscale(100%);
                }
            }
        }

        img {
            width: 100%;
            height: 100%;
        }
    }
}
</style>
