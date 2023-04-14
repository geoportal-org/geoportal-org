<template>
    <div class="map-control-layers">
        <button class="map-control-layers__trigger disabled-transparent" @click="toggleBox()"
            :title="$tc('mapControls.layers')"
            :disabled="(!boundingLayers.length && mapLayers.length < 2) || isExtendedViewActive"
            v-click-outside="{ fn: closeBox, excludeSelectors: '.map-control-layers__inner-wrapper, .tutorial-tag, .tutorial-mode, .tutorial-off' }">
            <i class="icomoon-layers"></i>
        </button>
        <div class="map-control-layers__wrapper" :class="{ active: showBox }" v-if="mapLayers.length">
            <div class="map-control-layers__inner-wrapper">
                <div class="map-control-layers__title">
                    <i class="icomoon-layers"></i>
                    {{ $tc('map.layersHandling') }}
                    <button v-if="!isWidget" class="compare-mode-button disabled-transparent" @click="compareLayerClick()"
                        :disabled="comparisonModeDisallowed && !comparisonModeEnabled" :title="$tc('map.compareLayer')"
                        :class="{ active: comparisonModeEnabled }" data-tutorial-tag="layers-handling-compare-mode">
                        <span v-if="!comparisonModeEnabled">Compare mode</span>
                        <span v-else>Standard mode</span>
                    </button>
                </div>
                <div class="map-control-layers__layers" v-bar>
                    <div :class="{ 'compare-mode': comparisonModeEnabled }">
                        <div class="map-control-layers__layer" v-for="layerData in mapLayers" :key="layerData.id"
                            v-if="(comparisonModeEnabled && layerData.type !== LayerTypes.BOUNDING && layerData.visible) || !comparisonModeEnabled">
                            <div class="d-flex flex--align-center flex--justify-between">
                                <div class="d-flex flex--align-center full-width">
                                    <span class="map-control-layers__layer-img bounding"
                                        v-if="layerData.type === LayerTypes.BOUNDING"></span>
                                    <span class="map-control-layers__layer-img area-of-interest"
                                        v-if="layerData.type === LayerTypes.SELECTEDAREA"></span>
                                    <div class="map-control-layers__layer-img preview"
                                        v-if="layerData.type !== LayerTypes.BOUNDING && layerData.type !== LayerTypes.SELECTEDAREA">
                                        <span v-if="!layerData.image">{{ $tc('map.previewUnavailable') }}</span>
                                        <img v-else :src="layerData.image" />
                                    </div>
                                    <div class="checkbox" v-if="!comparisonModeEnabled"
                                        data-tutorial-tag="layers-handling-visibility">
                                        <input type="checkbox" :id="layerData.id" :checked="layerData.visible"
                                            @change="changeLayerVisibility($event, layerData)" />
                                        <label :for="layerData.id">
                                            <i class="icomoon-eye"></i>
                                            <span>{{ (layerData.type === LayerTypes.SELECTEDAREA ?
                                                $tc('mapControls.areaOfInterest') : layerData.title) }}</span>
                                        </label>
                                    </div>
                                    <div class="checkbox" v-if="comparisonModeEnabled"
                                        :class="{ disabled: comparedLayersIds.length >= 2 && comparedLayersIds.indexOf(layerData.id) === -1 }"
                                        data-tutorial-tag="layers-handling-comparison">
                                        <input type="checkbox" :id="'compare-layer-' + layerData.id"
                                            :disabled="comparedLayersIds.length >= 2 && comparedLayersIds.indexOf(layerData.id) === -1"
                                            @change="setLayerComparisonState($event, layerData)" />
                                        <label :for="'compare-layer-' + layerData.id">
                                            <i class="icomoon-tick"></i>
                                            <span>{{ (layerData.type === LayerTypes.SELECTEDAREA ?
                                                $tc('mapControls.areaOfInterest') : layerData.title) }}</span>
                                        </label>
                                    </div>
                                </div>
                                <div class="map-control-layers__actions" v-if="!comparisonModeEnabled">
                                    <button class="map-control-layers__center-map" @click="centerMap(layerData)"
                                        :title="$tc('map.centerMapOnLayer')"
                                        v-if="(layerData.id !== LayerTypes.BOUNDING && layerData.coordinate) || layerData.type === LayerTypes.SELECTEDAREA"
                                        data-tutorial-tag="layers-handling-center-map">
                                        <i class="icomoon-center"></i>
                                    </button>
                                    <button class="map-control-layers__settings" @click="openLayerSettings(layerData)"
                                        :title="$tc('map.layerSettings')"
                                        :class="{ active: activeSettingsLayerId === layerData.id }"
                                        data-tutorial-tag="layers-handling-settings">
                                        <i class="icomoon-settings"></i>
                                    </button>
                                    <button class="map-control-layers__legend" :title="$tc('map.legend')"
                                        :class="{ active: (activeLayerLegend && activeLayerLegend.id === layerData.id) }"
                                        v-if="layerData.legend && supportedLegendTypes.includes(layerData.legend.type)"
                                        @click="toggleLegend(layerData)" data-tutorial-tag="layers-handling-legend">
                                        <i class="icomoon-legend"></i>
                                    </button>
                                    <button class="map-control-layers__remove cross"
                                        v-if="layerData.id !== LayerTypes.BOUNDING" :title="$tc('map.removeLayer')"
                                        @click="removeLayer(layerData.id)" data-tutorial-tag="layers-handling-remove">
                                    </button>
                                </div>
                            </div>
                            <div v-if="!comparisonModeEnabled">
                                <CollapseTransition>
                                    <div class="map-control-layers__transparency-control"
                                        v-show="activeSettingsLayerId === layerData.id">
                                        <div class="transparency-control__labels">
                                            <span>Tranparent</span>
                                            <span>Opaque</span>
                                        </div>
                                        <!-- <vue-slider :ref="`layer-transparency-slider-${layerData.id}`" :real-time="true"
                                            :use-keyboard="true" tooltip="always" class="transparency-control-slider"
                                            @change="changeLayerTransparency(layerData, $event)"
                                            :value="layerData.transparency"></vue-slider> -->
                                        <div class="transparency-control__labels">
                                            <span>0%</span>
                                            <span>50%</span>
                                            <span>100%</span>
                                        </div>
                                    </div>
                                </CollapseTransition>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { Component, Vue, Prop, Watch } from 'nuxt-property-decorator';
// import VueSlider from '@/components/Slider/index';
import { MapActions } from '@/store/map/map-actions';
import { LayerData } from '@/interfaces/LayerData';
import { LayerTypes } from '@/interfaces/LayerTypes';
import { MapGetters } from '@/store/map/map-getters';
import { GeneralGetters } from '@/store/general/general-getters';
import { ExtendedViewGetters } from '@/store/extendedView/extended-view-getters';
import TutorialTagsService from '@/services/tutorial-tags.service';
import CollapseTransition from '@/plugins/CollapseTransition';

@Component({
    components: {
        //   VueSlider,
        CollapseTransition,
    }
})
export default class MapControlLayersComponent extends Vue {
    [x: string]: any;
    @Prop({ default: false, type: Boolean }) public disabled!: boolean;
    public comparisonModeDisallowed = true;
    public showBox = false;
    public activeSettingsLayerId: string | null = null;
    public LayerTypes = LayerTypes;
    public activeCompareLayerId = null;
    public comparisonModeEnabled = false;
    public comparedLayersIds: string[] = [];
    private mapElement: HTMLElement | null = null;
    private map: any = null;
    private comparisonBar: HTMLDivElement | null = null;
    private comparisonBarVerticalLine: HTMLDivElement | null = null;

    private comparisonBarPressed = false;

    private compareLayersList: any = {};
    private compareLayerZIndex: number | undefined;

    get isWidget() {
        return this.$store.getters[GeneralGetters.isWidget];
    }

    get mapActiveLayerTileId() {
        return this.$store.getters[MapGetters.activeLayerTileId];
    }

    get supportedLegendTypes() {
        return this.$store.getters[MapGetters.supportedLegendTypes];
    }

    get mapLayers() {
        // Returns all layers from Layers Handling menu
        return this.$store.getters[MapGetters.layers].filter((layerData: LayerData) => (
            (layerData.type.indexOf(LayerTypes.BOUNDING) === -1 || layerData.type === LayerTypes.BOUNDING))
        );
    }

    get wmsMapLayers() {
        // Returns all layers from Layer Handling Menu without Bounding and AOI layers
        return this.$store.getters[MapGetters.layers].filter((layerData: LayerData) => (
            (layerData.type.indexOf(LayerTypes.BOUNDING) === -1 && layerData.type !== LayerTypes.SELECTEDAREA))
        );
    }

    get boundingLayers() {
        // Returns detailed subset of bounding layer i.e. boundingPins, boundingBoxes, boundingPolygons, boundingCircles
        return this.$store.getters[MapGetters.layers].filter((layerData: LayerData) => (
            (layerData.type.indexOf(LayerTypes.BOUNDING) !== -1 && layerData.type !== LayerTypes.BOUNDING))
        );
    }

    get activeCompareLayer() {
        return this.$store.getters[MapGetters.compareLayer];
    }

    get activeLayerLegend() {
        return this.$store.getters[MapGetters.activeLayerLegend];
    }

    get isExtendedViewActive() {
        return this.$store.getters[ExtendedViewGetters.isExtendedViewActive];
    }

    public centerMap(layerData: LayerData) {
        this.$store.dispatch(MapActions.zoomInLayer, layerData.id);
    }

    public async openLayerSettings(layerData: LayerData) {
        if (this.activeSettingsLayerId === layerData.id) {
            this.activeSettingsLayerId = null;
        } else {
            this.activeSettingsLayerId = layerData.id;
        }
    }

    public addLayerToComparisonList(layer: LayerData) {
        this.compareLayersList[layer.id] = {
            defaultZIndex: layer.value.getZIndex(),
            layer,
            checked: false
        };
    }

    public compareLayerClick() {
        const wmsMapLayers = this.wmsMapLayers.filter((layer: { visible: any; }) => layer.visible);
        this.comparisonModeDisallowed = wmsMapLayers.length < 2;
        if (this.comparisonModeDisallowed) {
            return;
        }
        this.comparisonModeEnabled = !this.comparisonModeEnabled;
        if (this.comparisonModeEnabled) {
            this.compareLayerZIndex = wmsMapLayers.map((layer: { value: { getZIndex: () => any; }; }) => layer.value.getZIndex()).reduce((p: number, c: number) => p < c ? c : p, 0) + 1;
            wmsMapLayers.forEach((layer: any) => this.addLayerToComparisonList(layer));
        }
        this.activeSettingsLayerId = null;
        this.$store.dispatch(MapActions.setActiveLayerLegend, null);

        if (!this.comparisonModeEnabled) {
            Object.keys(this.compareLayersList).forEach(item => this.compareLayersList[item].layer.value.setZIndex(this.compareLayersList[item].defaultZIndex));
            this.$store.dispatch(MapActions.setActiveCompareLayer, null);
            this.compareLayersList = {};
            this.comparedLayersIds = [];
            this.compareLayerZIndex = undefined;
        }
    }

    public removeLayer(layerDataId: string) {
        this.$store.dispatch(MapActions.removeLayer, layerDataId);
        if (this.activeLayerLegend && this.activeLayerLegend.id === layerDataId) {
            this.$store.dispatch(MapActions.setActiveLayerLegend, this.getTopmostActiveLayer());
        }
        const wmsMapLayers = this.wmsMapLayers.filter((layer: { visible: any; }) => layer.visible);
        this.comparisonModeDisallowed = wmsMapLayers.length < 2;
        if (wmsMapLayers.length < 2 || this.activeCompareLayerId === layerDataId) {
            this.$store.dispatch(MapActions.setActiveCompareLayer, null);
        }
    }

    public changeLayerTransparency(layerData: LayerData, value: number) {
        this.$store.dispatch(MapActions.changeLayerTransparency, { id: layerData.id, value });
    }

    public changeLayerVisibility(event: Event, layerData: LayerData) {
        const target = (event.target as HTMLInputElement);
        this.$store.dispatch(MapActions.changeLayerVisibility, { value: target.checked, id: layerData.id });
        if (layerData.legend && this.supportedLegendTypes.includes(layerData.legend.type)) {
            if (target.checked && !this.activeLayerLegend) {
                this.toggleLegend(layerData);
            }
            if (!target.checked && this.activeLayerLegend && this.activeLayerLegend.id === layerData.id) {
                this.$store.dispatch(MapActions.setActiveLayerLegend, this.getTopmostActiveLayer());
            }
        }


        const wmsMapLayers = this.wmsMapLayers.filter((layer: { visible: any; }) => layer.visible);
        this.comparisonModeDisallowed = wmsMapLayers.length < 2;
        if (wmsMapLayers.length < 2 || this.activeCompareLayerId === layerData.id) {
            this.$store.dispatch(MapActions.setActiveCompareLayer, null);
        }
    }

    public setLayerComparisonState(event: Event, layerData: LayerData) {
        const target = (event.target as HTMLInputElement);
        if (!this.compareLayersList[layerData.id]) {
            this.addLayerToComparisonList(layerData);
        }
        this.compareLayersList[layerData.id].checked = target.checked;
        if (!target.checked) {
            layerData.value.setZIndex(this.compareLayersList[layerData.id].defaultZIndex);
        }
        this.comparedLayersIds = Object.keys(this.compareLayersList).filter(key => this.compareLayersList[key].checked);
        const comparisonLayersKeys = Object.keys(this.compareLayersList);
        const checkedLayersCount = comparisonLayersKeys.filter(key => this.compareLayersList[key].checked).length;
        if (checkedLayersCount > 1) {
            comparisonLayersKeys.forEach(key => key !== layerData.id && this.compareLayersList[key].layer.value.setZIndex(this.compareLayersList[key].defaultZIndex));
            this.$store.dispatch(MapActions.setActiveCompareLayer, this.compareLayersList[layerData.id].layer);
            this.activeCompareLayerId = this.compareLayersList[layerData.id].layer.id;
            this.activeCompareLayer.value.setZIndex(this.compareLayerZIndex);
            this.activeCompareLayer.value.changed();
            this.showComparisonBar();
        } else {
            if (this.activeCompareLayer && this.activeCompareLayer !== layerData) {
                const defaultLayerZIndex = this.compareLayersList[this.activeCompareLayer.id].defaultZIndex;
                this.activeCompareLayer.value.setZIndex(defaultLayerZIndex);
            }
            this.$store.dispatch(MapActions.setActiveCompareLayer, null);
        }
    }

    public switchMapLayerTile(tileId: string) {
        this.$store.dispatch(MapActions.setActiveLayerTileId, tileId);
    }

    public toggleBox() {
        this.showBox = !this.showBox;
        this.activeSettingsLayerId = null;
        TutorialTagsService.refreshTagsGroup('layers-handling', this.showBox, 500);
    }

    public closeBox() {
        this.showBox = false;
        this.activeSettingsLayerId = null;
        TutorialTagsService.refreshTagsGroup('layers-handling', this.showBox, 500);
    }

    public toggleLegend(layerData: LayerData) {
        if (this.activeLayerLegend === layerData) {
            this.$store.dispatch(MapActions.setActiveLayerLegend, null);
        } else {
            this.$store.dispatch(MapActions.setActiveLayerLegend, layerData);
        }
    }

    @Watch('activeCompareLayer')
    private onActiveCompareLayerChange(newLayer: { value: { on: (arg0: string, arg1: { (event: any): void; (event: any): void; }) => void; }; }, oldLayer: { value: { un: (arg0: string, arg1: { (event: any): void; (event: any): void; }) => void; }; }) {
        if (oldLayer) {
            oldLayer.value.un('precompose', this.handleActiveComparisonLayerPrecomposeEvent);
            oldLayer.value.un('postcompose', this.restoreLayerContext);
        }
        if (!newLayer) {
            this.activeCompareLayerId = null;
            this.comparisonBarPressed = false;
            this.hideComparisonBar();
        } else {
            newLayer.value.on('precompose', this.handleActiveComparisonLayerPrecomposeEvent);
            newLayer.value.on('postcompose', this.restoreLayerContext);
        }
        if (this.map) {
            this.map.render();
        }
    }

    private restoreLayerContext(event: any) {
        event.context.restore();
    }

    private handleActiveComparisonLayerPrecomposeEvent(event: { context: any; }) {
        const percentage = this.getBarPosition() / this.mapElement!.offsetWidth;
        const ctx = event.context;
        const width = ctx.canvas.width * percentage;
        this.$store.dispatch(MapActions.setCompareBarPosition, this.getBarPosition());
        ctx.save();
        ctx.beginPath();
        ctx.rect(width, 0, ctx.canvas.width - width, ctx.canvas.height);
        ctx.clip();
    }

    @Watch('wmsMapLayers')
    private onWmsMapLayersChange(val: string | any[], oldVal: string | any[]) {
        if (val.length > oldVal.length) {
            // If layer has been just added try to open its legend
            this.$store.dispatch(MapActions.setActiveLayerLegend, this.getTopmostActiveLayer());
        }
        const wmsMapLayers = this.wmsMapLayers.filter((layer: { visible: any; }) => layer.visible);
        this.comparisonModeDisallowed = wmsMapLayers.length < 2;
    }

    private getBarPosition(): number {
        let comparisonBarOffsetLeft = this.comparisonBar!.offsetLeft;
        comparisonBarOffsetLeft += this.comparisonBarVerticalLine!.offsetLeft + this.comparisonBarVerticalLine!.offsetWidth / 2;
        return comparisonBarOffsetLeft;
    }

    private getTopmostActiveLayer() {
        // Returns topmost active layer with legend that is different than curent one
        const lastIndex = this.wmsMapLayers.length - 1;
        for (let i = lastIndex; i >= 0; i--) {
            if (this.wmsMapLayers[i].legend && this.supportedLegendTypes.includes(this.wmsMapLayers[i].legend.type) && this.wmsMapLayers[i].visible) {
                if (!this.activeLayerLegend || this.wmsMapLayers[i].id !== this.activeLayerLegend.id) {
                    return this.wmsMapLayers[i];
                }
            }
        }
        return null;
    }

    private mounted() {
        if (!this.isWidget) {
            this.map = this.$store.getters[MapGetters.map];
            this.comparisonBar = document.querySelector('#comparison-bar');
            this.comparisonBarVerticalLine = this.comparisonBar!.querySelector('.vertical-line');
            this.mapElement = document.querySelector('#map');

            this.comparisonBar!.addEventListener('mousedown', event => {
                this.comparisonBarPressed = true;
                document.addEventListener('mousemove', this.comparisonBarMoveEventHandler);
            });
            this.comparisonBar!.addEventListener('mouseup', event => {
                this.comparisonBarPressed = false;
                document.removeEventListener('mousemove', this.comparisonBarMoveEventHandler);
            });

            this.comparisonBar!.addEventListener('touchstart', event => {
                this.comparisonBarPressed = true;
                document.addEventListener('touchmove', this.comparisonBarMoveEventHandler);
            });

            this.comparisonBar!.addEventListener('touchend', event => {
                this.comparisonBarPressed = false;
                document.removeEventListener('touchmove', this.comparisonBarMoveEventHandler);
            });
        }
    }

    private comparisonBarMoveEventHandler(event: TouchEvent | any) {
        const calculateOffset = (pointerPosition: number, mapContainerWidth: number) => {
            return (pointerPosition / mapContainerWidth) * 100;
        };
        if (!this.comparisonBarPressed || !this.activeCompareLayer) {
            return;
        }
        let offsetX = 0;
        if (event.targetTouches && event.targetTouches.length) {
            offsetX = calculateOffset(event.targetTouches[0].clientX, this.mapElement!.clientWidth);
        } else {
            offsetX = calculateOffset(event.x, this.mapElement!.clientWidth);
        }
        this.comparisonBar!.style.left = offsetX + '%';
        event.preventDefault();
        this.activeCompareLayer.value.changed();
    }

    private showComparisonBar() {
        this.comparisonBar!.style.display = 'block';
    }

    private hideComparisonBar() {
        this.comparisonBar!.style.display = 'none';
    }
}
</script>

<style scoped lang="scss">
.map-control-layers {
    position: relative;
    width: auto;
    height: auto;

    &__trigger {
        width: 32px;
        height: 32px;
        transition: background-color 250ms ease-in-out;
        border-radius: 50%;
        color: white;
        font-size: 32px;

        @media (max-width: $breakpoint-lg) {
            font-size: 29px;
        }

    }

    &__wrapper {
        white-space: nowrap;
        position: absolute;
        bottom: -10px;
        right: calc(100% + 20px);
        width: 0;
        transition: right 0s 0.5s linear, left 0s 0.5s linear, width 0s 0.5s linear;

        @media (max-width: $breakpoint-lg) {
            bottom: calc(100% + 15px);
            overflow: hidden;
            transform: none;
            left: -55px;
            right: auto;
            max-width: calc(100vw - 10px);
        }

        &.active {
            transition: none;
            width: 400px;

            @media (max-width: $breakpoint-lg) {
                transform: none;
                right: auto;
            }

            .map-control-layers__inner-wrapper {
                width: 100%;
                margin-left: 0;
                opacity: 1;
                transition: width 0.5s 100ms ease-in-out, margin-left 0.5s 100ms ease-in-out, opacity 0.5s ease-in-out;

                @media (max-width: $breakpoint-lg) {
                    transform: translateY(0%);
                    transition: transform 0.5s 100ms ease-in-out, opacity 0.5s ease-in-out;
                }

                &:before {
                    opacity: 1;
                    transition: opacity 0.5s ease-in-out;
                }
            }
        }
    }

    &__inner-wrapper {
        width: 0%;
        transition: width 0.5s ease-in-out, margin-left 0.5s ease-in-out, opacity 0.5s ease-in-out;
        overflow: hidden;
        border-radius: 5px;
        margin-left: 100%;
        opacity: 0;

        @media (max-width: $breakpoint-lg) {
            width: 100%;
            margin-left: 0;
            transform: translateY(100%);
            transition: transform 0.5s ease-in-out, opacity 0.5s ease-in-out;
            padding-bottom: 10px;
        }

        &:before {
            content: '';
            border-top: 10px solid transparent;
            border-left: 10px solid $white-transparent;
            border-bottom: 10px solid transparent;
            position: absolute;
            right: -10px;
            bottom: 20px;
            opacity: 0;
            transition: opacity 0.5s ease-in-out;

            @media (max-width: $breakpoint-lg) {
                bottom: 0;
                left: 62px;
                right: auto;
                margin: 0;
                border-top-color: $white-transparent;
                border-left-color: transparent;
                border-bottom: none;
                border-right: 10px solid transparent;
            }
        }
    }

    &__title {
        background: $green-transparent;
        color: white;
        padding: 7px 10px;
        display: flex;
        align-items: center;
        font-size: 18px;

        i {
            margin-right: 10px;
        }
    }

    &__no-layers {
        background: $white-transparent;
        padding: 15px 10px;
    }

    &__layers {
        max-height: 150px;
    }

    &__layer {
        background: $white-transparent;
        min-width: 300px;
        padding: 10px 20px 10px 10px;
        position: relative;
        font-size: 15px;

        .checkbox label {
            color: black;
            max-width: 175px;
            min-width: 150px;
            overflow-wrap: break-word;
            padding-right: 10px;
        }
    }

    &__layer-img {
        width: 60px;
        height: 40px;
        margin-right: 10px;
        flex: 0 0 auto;

        &.bounding {
            background-color: #f7e274;
            border: 2px solid #fec515;
        }

        &.area-of-interest {
            border: 2px solid $red;
            background: rgba(white, 0.4);
        }

        &.preview {
            white-space: normal;
            overflow: hidden;
            font-size: 12px;
            border: 1px solid black;

            span {
                padding: 7px 3px;
                display: block;
            }

            img {
                width: 100%;
                height: 100%;
            }
        }
    }

    .compare-mode-button {
        color: #FFFFFF;
        margin-left: auto;
    }

    &__actions {
        display: flex;

        &.compare-mode-action {
            margin-left: auto;
        }

        button {
            width: 20px;
            height: 20px;
            border: 1px solid #ABAAAC;
            border-radius: 50%;
            padding: 2px;

            &:hover,
            &.active {
                background: #ABAAAC;

                &:not(:disabled) i {
                    color: white;
                }
            }

            i {
                font-size: 14px;
                color: #ABAAAC;
            }

            &+button {
                margin-left: 5px;
            }
        }
    }

    &__legend {
        position: relative;

        .icomoon-legend {
            font-size: 19px;
            left: 0px;
            position: absolute;
            top: 0;
        }
    }

    &__remove {
        min-height: 20px !important;
        min-width: 20px !important;

        &:hover {

            &:before,
            &:after {
                background: white;
            }
        }

        &:before,
        &:after {
            width: 14px;
            background: #ABAAAC;
            left: 2px;
            top: 9px;
            height: 1px;
        }
    }

    &__transparency-control {
        width: 100%;

        .transparency-control__labels {
            display: flex;
            justify-content: space-between;
            font-size: 13px;
            font-weight: 400;
            color: black;

            &:first-child {
                padding-top: 20px;
            }
        }
    }
}

@-moz-document url-prefix() {
    .with-dockbar .map-control-layers__actions button {
        padding: 1px;
    }
}
</style>

<style lang="scss">
.transparency-control-slider {
    .vue-slider-rail {
        background: linear-gradient(to right, #ffffff, $green) !important;
        border: 1px solid $green;
    }

    .vue-slider-process {
        background: none;
    }

    .vue-slider-tooltip {
        background-color: $green;
        border-color: $green;
    }
}
</style>
