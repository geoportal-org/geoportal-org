import Vue from 'vue'
import Layer from 'ol/layer/Layer'
import Map from 'ol/Map'
import Overlay from 'ol/Overlay'
import LayerTilesService from '@/services/map/layer-tiles.service'
import { LayerData } from '@/interfaces/LayerData'
import { LayerTypes } from '@/interfaces/LayerTypes'
import LayersUtils from '@/services/map/layer-utils'
import MapCoordinatesUtils from '@/services/map/coordinates-utils'
import { MapCoordinate } from '@/interfaces/MapCoordinate'
import { Timers } from '@/data/timers'
import { AppVueObj } from '@/data/global'
import { GeneralGetters } from '@/store/general/general-getters'
import { MapGetters } from './map-getters'

declare global {
    interface Window {
        geossMap: Map
    }
}

const state = () => ({
    boxAccessToken:
        'pk.eyJ1IjoidXJoZW4xMiIsImEiOiJjazJnNm95ZWMwbTk2M21zNXJ5b3BvaXU3In0.xkKCRxWyUIEkyQ9ftAYB5w',
    googleMapsApiKey: '',
    compareBarPosition: null,
    map: null,
    mapTooltip: null,
    mapTooltipMessage: '',
    initialZoom: 2.5,
    center: [0, 0],
    isMapConf: false,
    activeLayerTileId: '',
    compareLayer: null,
    // this is not an actual layer but a controller
    // that is used for controlling all bounding layers
    layers: [
        {
            id: LayerTypes.BOUNDING,
            title: 'Bounding boxes',
            visible: true,
            transparency: 50,
            value: null,
            type: LayerTypes.BOUNDING
        }
    ],
    hoveredLayerId: null,
    clickedLayerId: null,
    activeLayerLegend: null,
    supportedLegendTypes: ['img', 'one-value-scale', 'two-value-scale'],
    showFull: false,
    hideSearching: false,
    hideSeeAlso: false,
    progressBarLoading: 0,
    progressBarLoaded: 0,
    progressBarPercentage: 0,
    progressBarEnable: false
})

const initialState = JSON.parse(JSON.stringify(state()))

let padding = [30, 30, 30, 30]

const getters = {
    boxAccessToken: (state: any) => {
        return state.boxAccessToken
    },
    compareBarPosition: (state: any) => {
        return state.compareBarPosition
    },
    googleMapsApiKey: (state: any) => {
        return state.googleMapsApiKey
    },
    map: (state: any) => {
        return state.map
    },
    mapTooltip: (state: any) => {
        return state.mapTooltip
    },
    mapTooltipMessage: (state: any) => {
        return state.mapTooltipMessage
    },
    initialZoom: (state: any) => {
        return state.initialZoom
    },
    center: (state: any) => {
        return (
            AppVueObj.ol &&
            AppVueObj.ol.proj.transform(state.center, 'EPSG:4326', 'EPSG:3857')
        )
    },
    isMapConf: (state: any) => {
        return state.isMapConf
    },
    activeLayerTileId: (state: any) => {
        return state.activeLayerTileId
    },
    layers: (state: any) => {
        return state.layers
    },
    boudningLayers: (state: any) => {
        return state.layers.filter(
            (layerData: LayerData) =>
                layerData.type.indexOf(LayerTypes.BOUNDING) !== -1 &&
                layerData.type !== LayerTypes.BOUNDING
        )
    },
    customLayers: (state: any) => {
        return state.layers.filter(
            (layerData: LayerData) =>
                layerData.type.indexOf(LayerTypes.CUSTOM) !== -1 &&
                layerData.type === LayerTypes.CUSTOM
        )
    },
    hoveredLayerId: (state: any) => {
        return state.hoveredLayerId
    },
    clickedLayerId: (state: any) => {
        return state.clickedLayerId
    },
    shareParams: (state: any) => {
        let params = ''
        const includeProps = ['activeLayerTileId']
        for (const prop of Object.keys(initialState)) {
            if (
                includeProps.indexOf(prop) !== -1 &&
                JSON.stringify(state[prop]) !==
                    JSON.stringify(initialState[prop])
            ) {
                if (params) {
                    params += '&'
                }
                params += `m:${prop}=${encodeURIComponent(state[prop])}`
            }
        }
        if (params) {
            params += '&'
        }
        return params
    },
    activeLayerLegend: (state: any) => {
        return state.activeLayerLegend
    },
    compareLayer: (state: any) => {
        return state.compareLayer
    },
    supportedLegendTypes: (state: any) => {
        return state.supportedLegendTypes
    },
    showFull: (state: any) => {
        return state.showFull
    },
    hideSearching: (state: any) => {
        return state.hideSearching
    },
    hideSeeAlso: (state: any) => {
        return state.hideSeeAlso
    },
    progressBarLoading: (state: any) => {
        return state.progressBarLoading
    },
    progressBarLoaded: (state: any) => {
        return state.progressBarLoaded
    },
    progressBarPercentage: (state: any) => {
        return state.progressBarPercentage
    },
    progressBarEnable: (state: any) => {
        return state.progressBarEnable
    }
}

const mutations = {
    setStateProp(state: any, data: { prop: any; value: any }) {
        state[data.prop] = data.value
    },
    setActiveCompareLayer(state: any, layer: any) {
        state.compareLayer = layer
    },
    setActiveLayerTileId(state: any, tileId: string) {
        const tileToApply = LayerTilesService[tileId]
        if (tileToApply) {
            state.activeLayerTileId = tileId
        }
        if (window.geossMap && tileToApply) {
            for (const prop of Object.keys(LayerTilesService)) {
                const tile = LayerTilesService[prop]
                if (tile.id !== tileId) {
                    const layers = tile.getLayerTile()
                    if (layers.constructor !== Array) {
                        window.geossMap.removeLayer(layers)
                    } else {
                        layers.map((layer) =>
                            window.geossMap.removeLayer(layer)
                        )
                    }
                }
            }

            const layers = tileToApply.getLayerTile()
            if (layers.constructor !== Array) {
                window.geossMap.addLayer(layers)
            } else {
                layers.map((layer) => window.geossMap.addLayer(layer))
            }
        }
    },
    addLayer(
        state: any,
        {
            layer,
            id,
            index,
            title,
            type,
            transparency,
            coordinate,
            image,
            legend,
            visible
        }: {
            layer: Layer
            id: string
            index: number
            title: string
            type: string
            transparency: number
            coordinate: MapCoordinate
            image: string
            legend: any
            visible: boolean
        }
    ) {
        transparency = transparency || 100
        if (visible === undefined) {
            visible = true
        }

        // if the new layer is of type bounding box then its transparency
        // should be equal to bounding box layer of type BOUDNING
        if (type.indexOf(LayerTypes.BOUNDING) !== -1) {
            const boundingBoxLayerData: LayerData = state.layers.find(
                (layerData: { id: LayerTypes }) =>
                    layerData.id === LayerTypes.BOUNDING
            )
            transparency = boundingBoxLayerData.transparency
            visible = boundingBoxLayerData.visible

            layer.setOpacity(transparency / 100)
            if (visible) {
                layer.setOpacity(transparency / 100)
            } else {
                layer.setOpacity(0)
            }
        } else {
            const layerExists = state.layers.find(
                (layerData: LayerData) => layerData.id === id
            )
            if (layerExists) {
                return
            }
        }

        const newLayer = {
            id,
            title,
            visible,
            transparency,
            value: layer,
            type,
            coordinate,
            image,
            legend
        } as LayerData

        // The index - position on result list - is used for
        // layer shuffling mechanism for bounding boxes
        if (typeof index === 'number') {
            newLayer.index = index
        }

        state.layers.push(newLayer)

        if (window.geossMap) {
            window.geossMap.addLayer(layer)
        }
    },
    changeLayerVisibility(
        state: any,
        { value, id }: { value: boolean; id: string }
    ) {
        const layer = state.layers.find(
            (layer: { id: string }) => layer.id === id
        )
        if (layer) {
            layer.visible = value

            if (layer.value && value) {
                layer.value.setOpacity(layer.transparency / 100)
            } else if (layer.value) {
                layer.value.setOpacity(0)
            }
        }
    },
    changeLayerTransparency(
        state: any,
        { value, id }: { value: number; id: string }
    ) {
        const layer = state.layers.find(
            (layer: { id: string }) => layer.id === id
        )
        if (layer && value !== null && typeof value !== 'undefined') {
            layer.transparency = value

            if (layer.value && layer.visible) {
                layer.value.setOpacity(value / 100)
            }
        }
    },
    removeLayer(state: any, id: string) {
        const layerIndex = state.layers.findIndex(
            (layer: { id: string }) => layer.id === id
        )
        if (layerIndex !== -1) {
            if (window.geossMap) {
                window.geossMap.removeLayer(state.layers[layerIndex].value)
            }
            state.layers.splice(layerIndex, 1)
        }
    },
    repaintBoudingLayers(state: any) {
        const mapBoudningLayers: LayerData[] = state.layers.filter(
            (layerData: LayerData) =>
                layerData.type.indexOf(LayerTypes.BOUNDING) !== -1 &&
                layerData.type !== LayerTypes.BOUNDING
        )
        for (const layerData of mapBoudningLayers) {
            let mode = 0

            if (
                state.hoveredLayerId !== null ||
                state.clickedLayerId !== null
            ) {
                mode = 1
            }

            if (
                state.hoveredLayerId === layerData.id ||
                state.clickedLayerId === layerData.id
            ) {
                mode = 2
            }

            LayersUtils.setLayerStyle(layerData.value, mode, layerData.type)
        }
    },
    centerMap(state: any, { W, S, E, N }: MapCoordinate) {
        if (window.geossMap) {
            const longitudes = MapCoordinatesUtils.denormalizeLongitude(W, E)
            let denormalizedW: number = longitudes[0]
            let denormalizedE: number = longitudes[1]

            // enlarge small extent to avoid very big zoom
            // 0.02 this is arbitrarly chosen size, allows to barely see buildings
            const minSize = 0.02
            if (denormalizedE - denormalizedW < minSize || N - S < minSize) {
                denormalizedW = denormalizedW - minSize / 2
                denormalizedE = denormalizedE + minSize / 2
                S = S - minSize / 2
                N = N + minSize / 2
            }

            let extent: [number, number, number, number] = [
                denormalizedW,
                S,
                denormalizedE,
                N
            ]
            extent = AppVueObj.ol.extent.applyTransform(
                extent,
                AppVueObj.ol.proj.getTransform('EPSG:4326', 'EPSG:3857')
            ) as [number, number, number, number]

            let aoiPaddingLeft = 30
            let aoiPaddingRight = 30
            if (!AppVueObj.app.$store.getters[GeneralGetters.isWidget]) {
                // Set padding not to overlap with searchbox
                const searchContainer: HTMLElement = document.querySelector(
                    '.search-container__wrapper'
                )!
                const isSearchBoxMinimized =
                    AppVueObj.app.$store.getters[MapGetters.showFull]
                const boxLeftOffset =
                    searchContainer.getBoundingClientRect().left
                const boxRightOffset =
                    window.innerWidth -
                    searchContainer.offsetWidth -
                    searchContainer.getBoundingClientRect().left
                const isSearchBoxInCenter =
                    Math.abs(boxRightOffset - boxLeftOffset) < 5
                if (
                    (boxLeftOffset < 200 && boxRightOffset < 200) ||
                    isSearchBoxMinimized ||
                    isSearchBoxInCenter
                ) {
                    aoiPaddingLeft = 30
                    aoiPaddingRight = 30
                } else if (boxRightOffset > boxLeftOffset) {
                    aoiPaddingLeft =
                        boxLeftOffset + searchContainer.offsetWidth + 10
                    aoiPaddingRight = 0
                } else {
                    aoiPaddingRight = window.innerWidth - boxLeftOffset + 10
                    aoiPaddingLeft = 0
                }
            }
            padding = [30, aoiPaddingRight, 30, aoiPaddingLeft]

            window.geossMap.getView().fit(extent, {
                padding,
                duration: Timers.fitExtent
            })
        }
    }
}

const actions = {
    setBoxAccessToken(context: any, value: string) {
        return context.commit('setStateProp', { prop: 'boxAccessToken', value })
    },
    setCompareBarPosition(context: any, value: number) {
        return context.commit('setStateProp', {
            prop: 'compareBarPosition',
            value
        })
    },
    setGoogleMapsApiKey(context: any, value: string) {
        return context.commit('setStateProp', {
            prop: 'googleMapsApiKey',
            value
        })
    },
    setMap(context: any, value: Map) {
        window['geossMap'] = value
        return
        // return context.commit('setStateProp', {
        //     prop: 'map',
        //     value,
        // })
    },
    setMapTooltip(context: any, value: Overlay | null) {
        return context.commit('setStateProp', { prop: 'mapTooltip', value })
    },
    setMapTooltipMessage(context: any, value: string) {
        return context.commit('setStateProp', {
            prop: 'mapTooltipMessage',
            value
        })
    },
    setInitialZoom(context: any, value: number) {
        return context.commit('setStateProp', { prop: 'initialZoom', value })
    },
    setCenter(context: any, value: any) {
        return context.commit('setStateProp', { prop: 'center', value })
    },
    setIsMapConf(context: any, value: any) {
        return context.commit('setStateProp', { prop: 'isMapConf', value })
    },
    setActiveLayerTileId(context: any, tileId: string) {
        // Emergency OSM-only switch
        // return context.commit('setActiveLayerTileId', tileId);
        return context.commit('setActiveLayerTileId', 'osm')
    },
    setActiveCompareLayer(context: any, layer: any) {
        return context.commit('setActiveCompareLayer', layer)
    },
    addLayer(
        { commit, getters }: any,
        {
            layer,
            id,
            index,
            title,
            type,
            transparency,
            coordinate,
            image,
            legend,
            visible
        }: {
            layer: Layer
            id: string
            index: number
            title: string
            type: string
            transparency: number
            coordinate: MapCoordinate
            image: string
            legend: any
            visible: boolean
        }
    ) {
        commit('addLayer', {
            id,
            index,
            layer,
            title,
            type,
            transparency,
            coordinate,
            image,
            legend,
            visible
        })

        if (type.indexOf(LayerTypes.BOUNDING) === -1) {
            const layerData: LayerData = getters.layers.find(
                (item: { id: string }) => item.id === id
            )
            commit('changeLayerTransparency', {
                id,
                value: layerData.transparency
            })
            commit('changeLayerVisibility', { id, value: layerData.visible })
        }
    },
    changeLayerVisibility(
        { commit, getters }: any,
        { id, value }: { id: string; value: boolean }
    ) {
        commit('changeLayerVisibility', { id, value })

        if (id === LayerTypes.BOUNDING) {
            for (const layerData of getters.boudningLayers) {
                commit('changeLayerVisibility', { id: layerData.id, value })
            }
        }
    },
    changeLayerTransparency(
        { commit, getters }: any,
        { id, value }: { id: string; value: number }
    ) {
        commit('changeLayerTransparency', { id, value })

        if (id === LayerTypes.BOUNDING) {
            for (const layerData of getters.boudningLayers) {
                commit('changeLayerTransparency', { id: layerData.id, value })
            }
        }
    },
    removeLayer(context: any, id: string) {
        return context.commit('removeLayer', id)
    },
    removeBoundingLayers({ commit, getters }: any) {
        for (const layerData of getters.boudningLayers as LayerData[]) {
            commit('removeLayer', layerData.id)
        }
    },
    repaintBoudingLayers(context: any) {
        context.commit('repaintBoudingLayers')
    },
    setHoveredLayerId(context: any, value: any) {
        context.commit('setStateProp', { prop: 'hoveredLayerId', value })
    },
    setClickedLayerId(context: any, value: any) {
        context.commit('setStateProp', { prop: 'clickedLayerId', value })
    },
    zoomInLayer({ getters, commit }: any, id: string) {
        const layerData: LayerData = getters.layers.find(
            (layerData: { id: string }) => layerData.id === id
        )
        if (layerData) {
            const source = layerData.value.getSource() as any
            if (source.getExtent) {
                // Infinity coordinate means the source is not ready whatsoever. Zoom is triggerred after the load finishes.
                if (
                    source.getState() !== 'ready' ||
                    source.getExtent()[0] === Infinity
                ) {
                    source.once('change', () => {
                        if (source.getState() === 'ready') {
                            if (source.getFeatures().length > 0) {
                                const extent = source.getExtent()
                                getters.map.getView().fit(extent, {
                                    size: getters.map.getSize(),
                                    duration: Timers.fitExtent,
                                    padding
                                })
                            }
                        }
                    })
                } else {
                    const extent = source.getExtent()
                    getters.map.getView().fit(extent, {
                        size: getters.map.getSize(),
                        duration: Timers.fitExtent,
                        padding
                    })
                }
            } else if (layerData.coordinate) {
                commit('centerMap', layerData.coordinate)
            }
        }
    },
    centerMap(context: any, data: MapCoordinate) {
        context.commit('centerMap', data)
    },
    setActiveLayerLegend({ commit }: any, value: any) {
        commit('setStateProp', { prop: 'activeLayerLegend', value })
    },
    setShowFull({ commit }: any, value: boolean) {
        commit('setStateProp', { prop: 'showFull', value })
        commit('setStateProp', { prop: 'hideSearching', value })
        commit('setStateProp', { prop: 'hideSeeAlso', value })
    },
    setHideSearching({ commit, getters }: any, value: boolean) {
        commit('setStateProp', { prop: 'hideSearching', value })
        if (value === getters.hideSeeAlso) {
            commit('setStateProp', { prop: 'showFull', value })
        }
    },
    setHideSeeAlso({ commit, getters }: any, value: boolean) {
        commit('setStateProp', { prop: 'hideSeeAlso', value })
        if (value === getters.hideSearching) {
            commit('setStateProp', { prop: 'showFull', value })
        }
    },
    setProgressBarLoading({ commit }: any, value: boolean) {
        commit('setStateProp', { prop: 'progressBarLoading', value })
    },
    setProgressBarLoaded({ commit }: any, value: boolean) {
        commit('setStateProp', { prop: 'progressBarLoaded', value })
    },
    setProgressBarPercentage({ commit }: any, value: boolean) {
        commit('setStateProp', { prop: 'progressBarPercentage', value })
    },
    setProgressBarEnable({ commit }: any, value: boolean) {
        commit('setStateProp', { prop: 'progressBarEnable', value })
    }
}

export default {
    namespaced: true,
    state,
    getters,
    actions,
    mutations
}
