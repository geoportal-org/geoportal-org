<template>
    <div class="layers">
        <div v-for="(layer, index) in data" :key="index" class="layer">
            <div class="layer__wrapper">
                <div class="layer__info">
                    <div class="layer__image" v-if="layer.img">
                        <img :src="image" :alt="layer.title" />
                    </div>
                    <div class="layer__title">
                        <span>{{ layer.title }}</span>
                    </div>
                </div>
                <div class="layer__checkbox checkbox" :class="{ 'loading-layer': loadingLayer == layer.url }">
                    <input type="checkbox" :id="`dab-result-layer-${index}`"
                        :checked="isLayerDisplayed(layer.url) || loadingLayer == layer.url"
                        @change="toggleSingleLayer(layer)" />
                    <label :for="`dab-result-layer-${index}`">
                        <i class="icomoon-eye"></i>
                        <span v-if="loadingLayer == layer.url" class="layer-loading">{{ $tc('popupContent.loadingLayer')
                        }}<span>.</span><span>.</span><span>.</span></span>
                        <span v-else>{{ $tc('popupContent.loadOnMap') }}</span>
                    </label>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { Component, Vue, Prop } from 'nuxt-property-decorator';
import LayersUtils from '@/services/map/layer-utils';
import PopupCloseService from '@/services/popup-close.service';

@Component
export default class DabResultLayersComponent extends Vue {
    public loadingLayer = '';

    @Prop(Array) public data!: any;
    @Prop(String) public image!: string;
    @Prop({ default: null, type: String }) private resultCoordinates!: string;

    public isLayerDisplayed(id: any) {
        return LayersUtils.isLayerDisplayed(id);
    }

    public async toggleSingleLayer(layer: any) {
        this.loadingLayer = layer.url;
        await LayersUtils.toggleLayer(layer, this.resultCoordinates, this.image);
        this.loadingLayer = '';
        PopupCloseService.closePopup('layers');
    }
}
</script>

<style lang="scss">
.layers {
    padding: 30px 25px;
}

.layer {
    +.layer {
        border-top: 1px solid #ddd;
        padding-top: 15px;
    }

    &__wrapper {
        align-items: center;
        display: flex;
        flex-wrap: wrap;
        justify-content: space-between;

        >div {
            align-items: center;
            display: flex;

            &.layer__info {
                flex-wrap: wrap;
                margin-bottom: 10px;

                .layer__image {
                    margin-right: 20px;
                    margin-bottom: 5px;

                    img {
                        border: 1px solid #ddd;
                        max-width: 120px;
                        padding: 5px;
                        word-wrap: break-word;
                    }
                }

                .layer__title {
                    font-size: 20px;
                    margin-bottom: 5px;

                    span {
                        width: 100%;
                        word-break: break-word;
                    }
                }
            }

            &.layer__checkbox {
                margin-bottom: 15px;

                &.loading-layer {
                    label {
                        pointer-events: none;
                    }
                }
            }
        }
    }
}


@keyframes loadingDots {
    0% {
        opacity: .1;
    }

    20% {
        opacity: 1;
    }

    100% {
        opacity: .1;
    }
}

.layer-loading {
    span {
        animation-name: loadingDots;
        animation-duration: 1s;
        animation-iteration-count: infinite;
        animation-fill-mode: both;

        &:nth-child(2) {
            animation-delay: .2s;
        }

        &:nth-child(3) {
            animation-delay: .4s;
        }
    }
}
</style>
