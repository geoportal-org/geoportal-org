import Vue from 'vue'
import MapCoordinatesUtils from '@/services/map/coordinates-utils'
import SearchEngineService from '@/services/search-engine.service'
import GeossSearchApiService from '@/services/geoss-search.api.service'
import ErrorPopup from '@/components/ErrorPopup.vue'
import LogService from '../log.service'
import to from '@/utils/to'
import { AppVueObj } from '@/data/global'
import { Timers } from '@/data/timers'
import { LayerTypes } from '@/interfaces/LayerTypes'
import { LayerData } from '@/interfaces/LayerData'
import { MapGetters } from '@/store/map/map-getters'
import { MapActions } from '@/store/map/map-actions'
import { PopupActions } from '@/store/popup/popup-actions'
import { GeneralGetters } from '@/store/general/general-getters'
import { FeatureClass } from 'ol/Feature'
import Layer from 'ol/layer/Layer'
import VectorLayer from 'ol/layer/Vector'
import TileLayer from 'ol/layer/Tile'
import { $tc } from '~/plugins/i18n'
import UtilsService from '@/services/utils.service';

// @ts-ignore
const ol = Vue.ol

const LayersUtils = {
    getIrisCircleLayerData(
        {
            coordinates,
            magnitude,
            depth
        }: { coordinates: number[]; magnitude: number; depth: number },
        index: number
    ) {
        const center: any = [[coordinates[2], coordinates[3]]]
        const radius = 0.19 + magnitude * 0.075
        let depthLevel = Math.min(Math.ceil(depth / 20000), 5)
        if (!depthLevel) {
            // Attach zero-depth earthquakes to level 1
            depthLevel = 1
        }

        const circleFeature: FeatureClass = new AppVueObj.ol.Feature({
            geometry: new AppVueObj.ol.geom.MultiPoint(center)
        })
        circleFeature.getGeometry()!.transform('EPSG:4326', 'EPSG:3857')

        const layer: any = new AppVueObj.ol.layer.Vector({
            source: new AppVueObj.ol.source.Vector({
                features: [circleFeature]
            })
        })

        layer.radius = radius
        layer.depth = depthLevel
        layer.setZIndex(index + 21)
        LayersUtils.setLayerStyle(layer, 0, LayerTypes.BOUNDINGCIRCLE)

        return layer
    },

    getPolygonLayerData(coordinates: any, index: number) {
        const boxFeature: FeatureClass = new AppVueObj.ol.Feature({
            geometry: new AppVueObj.ol.geom.Polygon([
                MapCoordinatesUtils.dateLineFix(
                    MapCoordinatesUtils.parsePolygon(coordinates)
                )
            ])
        })
        boxFeature.getGeometry()!.transform('EPSG:4326', 'EPSG:3857')

        const layer = new AppVueObj.ol.layer.Vector({
            source: new AppVueObj.ol.source.Vector({
                features: [boxFeature]
            })
        })

        layer.setZIndex(index + 21)
        LayersUtils.setLayerStyle(layer, 0, LayerTypes.BOUDNINGPOLYGON)

        return layer
    },

    getPinLayerData(coordinates: any[], index: number, item: any = null) {
        const W = []
        const S = []
        const E = []
        const N = []

        for (let i = 0; i < coordinates.length; i++) {
            W[i] = coordinates[i][0]
            S[i] = coordinates[i][1]
            E[i] = coordinates[i][2]
            N[i] = coordinates[i][3]
        }

        const pointsArray = []
        for (let i = 0; i < coordinates.length; i++) {
            pointsArray.push([E[i], N[i]])
        }
        const iconFeature: FeatureClass = new AppVueObj.ol.Feature({
            geometry: new AppVueObj.ol.geom.MultiPoint(pointsArray)
        })
        iconFeature.getGeometry()!.transform('EPSG:4326', 'EPSG:3857')

        if (item && item['poi-id']) {
            const poiLinks = [];
            const links: any = UtilsService.getArrayByString(item, 'gmd:distributionInfo.gmd:MD_Distribution.gmd:transferOptions.gmd:MD_DigitalTransferOptions.gmd:onLine');
            for(const link of links) {
                const linkTitle = UtilsService.getPropByString(link, 'gmd:CI_OnlineResource.gmd:description.gco:CharacterString');
                const linkUrl = UtilsService.getPropByString(link, 'gmd:CI_OnlineResource.gmd:linkage.gmd:URL');
                poiLinks.push({
                    title: linkTitle,
                    url: linkUrl
                });
            }
            const poiData = {
                title: item.title,
                links: poiLinks,
                elevation: item.verticalextent.minimum === item.verticalextent.maximum ? item.verticalextent.minimum : `${item.verticalextent.minimum} - ${item.verticalextent.maximum}`,
                cateogry: item['poi-category'],
                country: item['poi-country'],
                id: item['poi-id'],
                network: item['poi-network'],
                organization: item['poi-organization'],
                parameters: item['poi-parameters']
            };
            // @ts-ignore
            iconFeature.poi = poiData;
        }

        const vectorSource = new AppVueObj.ol.source.Vector({
            features: [iconFeature]
        })

        const layer = new AppVueObj.ol.layer.Vector({
            source: vectorSource
        })

        layer.setZIndex(index + 21)
        LayersUtils.setLayerStyle(layer, 0, LayerTypes.BOUNDINGPIN)

        return layer
    },

    getBoxLayerData(boxes: any[], index: number) {
        const W = []
        const S = []
        const E = []
        const N = []

        for (let i = 0; i < boxes.length; i++) {
            W[i] = boxes[i][0]
            S[i] = boxes[i][1]
            E[i] = boxes[i][2]
            N[i] = boxes[i][3]
        }

        const boxesArray = []
        for (let i = 0; i < boxes.length; i++) {
            const denormalizedLongitude =
                MapCoordinatesUtils.denormalizeLongitude(W[i], E[i])
            W[i] = denormalizedLongitude[0]
            E[i] = denormalizedLongitude[1]
            boxesArray.push([
                [
                    [E[i], N[i]],
                    [E[i], S[i]],
                    [W[i], S[i]],
                    [W[i], N[i]],
                    [E[i], N[i]]
                ]
            ])
        }
        const multiPolygon = new AppVueObj.ol.geom.MultiPolygon(boxesArray)
        const boxFeature: FeatureClass = new AppVueObj.ol.Feature({
            geometry: multiPolygon
        })

        boxFeature.getGeometry()!.transform('EPSG:4326', 'EPSG:3857')

        const layer = new AppVueObj.ol.layer.Vector({
            source: new AppVueObj.ol.source.Vector({
                features: [boxFeature]
            })
        })

        const outerBox = MapCoordinatesUtils.mergeBoxes(boxes)
        if (
            MapCoordinatesUtils.isWholeWorld(
                outerBox[0],
                outerBox[1],
                outerBox[2],
                outerBox[3]
            )
        ) {
            // do not draw whole-world features
            return null
        }
        layer.setZIndex(index + 21)
        LayersUtils.setLayerStyle(layer, 0, LayerTypes.BOUNDINGBOX)

        return layer
    },

    setLayerStyle(layer: Layer, mode: number, type: string) {
        let style = null

        switch (type) {
            case LayerTypes.BOUNDINGBOX:
            case LayerTypes.BOUDNINGPOLYGON: {
                const opacityArr = [0.1, 0.1, 0.3, 0]
                const fillColor = 'rgba(255, 255, 0, ' + opacityArr[mode] + ')'
                const strokeColorArr = [
                    'rgb(255, 255, 0)',
                    'transparent',
                    'rgb(255, 255, 0)',
                    'transparent'
                ]

                style = LayersUtils.getBoundingBoxStyle(
                    fillColor,
                    strokeColorArr[mode]
                )
                break
            }
            case LayerTypes.BOUNDINGPIN: {
                const opacityArr = [0.75, 0.4, 1, 0]
                style = LayersUtils.getBoundingPinStyle(opacityArr[mode])
                break
            }
            case LayerTypes.BOUNDINGCIRCLE: {
                const opacityArr = [0.75, 0.4, 1, 0]

                style = LayersUtils.getBoundingCircleStyle(
                    opacityArr[mode],
                    (layer as any).radius,
                    (layer as any).depth
                )
                break
            }
        }

        const feature = (layer.getSource() as any).getFeatures()[0]

        if (typeof feature !== 'undefined') {
            feature.setStyle(style)
        }
    },

    getBoundingBoxStyle(fillColor: string, strokeColor: string) {
        return new AppVueObj.ol.style.Style({
            fill: new AppVueObj.ol.style.Fill({
                color: fillColor
            }),
            stroke: new AppVueObj.ol.style.Stroke({
                color: strokeColor,
                width: 2
            })
        })
    },

    getBoundingPinStyle(transparency: number) {
        return new AppVueObj.ol.style.Style({
            image: new AppVueObj.ol.style.Icon({
                anchor: [0.5, 1],
                opacity: transparency,
                src: `/img/marker.png`
            })
        })
    },

    getBoundingCircleStyle(
        transparency: number,
        radius: number,
        depth: number
    ) {
        return new AppVueObj.ol.style.Style({
            image: new AppVueObj.ol.style.Icon({
                anchor: [0.5, 0.5],
                opacity: transparency,
                src: `/svg/irisCircles${depth}.svg`,
                scale: radius
            })
        })
    },

    createWMS(name: string, url: string) {
        let LAYERS = null
        let VERSION = null
        let TILED = null
        let urlLayer: string = ''

        if (url.indexOf('ANCHOR=complex') > -1) {
            const endURL = url.indexOf('&&&')
            urlLayer = url.slice(0, endURL)
        } else {
            const endURL = url.indexOf('?')
            urlLayer = url.slice(0, endURL)
        }

        if (url.indexOf('LAYERS=') > -1) {
            const startLayers = url.indexOf('LAYERS=')
            const endLayers = url.indexOf('&', startLayers)

            if (startLayers > -1) {
                LAYERS = url.slice(startLayers + 7, endLayers)
            } else {
                LAYERS = ''
            }
        } else if (url.indexOf('name=all') > -1) {
            LAYERS = 'all'
        } else {
            LAYERS = ''
        }

        if (url.indexOf('version=') > -1) {
            const startVersion = url.indexOf('version=')
            const endVersion = url.indexOf('&', startVersion)
            if (startVersion > -1) {
                VERSION = url.slice(startVersion + 8, endVersion)
            } else {
                VERSION = ''
            }
        } else if (url.indexOf('VERSION=') > -1) {
            const startVersion = url.indexOf('VERSION=')
            const endVersion = url.indexOf('&', startVersion)
            if (startVersion > -1) {
                VERSION = url.slice(startVersion + 8, endVersion)
            } else {
                VERSION = ''
            }
        }

        if (url.indexOf('TILED=') > -1) {
            const startTiled = url.indexOf('TILED=')
            const endTiled = url.indexOf('&', startTiled)
            if (startTiled > -1) {
                TILED = url.slice(startTiled + 6, endTiled)
            } else {
                TILED = ''
            }
        }

        const wms = new AppVueObj.ol.layer.Tile({
            source: new AppVueObj.ol.source.TileWMS({
                crossOrigin: 'Anonymous',
                projection: 'EPSG:4326',
                url: urlLayer,
                params: {
                    LAYERS,
                    VERSION,
                    TILED,
                    SRS: 'EPSG:4326'
                }
            })
        })

        wms.setZIndex(8)
        wms.set('name', name)
        wms.set('url', url)
        wms.setVisible(true)
        return wms
    },

    createTMS(url: string) {
        const urlParsed = url.replace('/${z}/${x}/${y}', '/{z}/{x}/{y}')

        const tms = new AppVueObj.ol.layer.Tile({
            source: new AppVueObj.ol.source.XYZ({
                crossOrigin: 'Anonymous',
                projection: 'EPSG:4326',
                tileSize: [512, 512],
                url: urlParsed
            })
        })

        tms.setZIndex(8)
        tms.set('name', url)
        tms.setVisible(true)
        return tms
    },

    createKML(url: string) {
        const proxyUrl = SearchEngineService.getLayerKmlUrl(url)

        const kml = new AppVueObj.ol.layer.Vector({
            source: new AppVueObj.ol.source.Vector({
                url: proxyUrl,
                format: new AppVueObj.ol.format.KML()
            })
        })

        kml.setZIndex(8)
        kml.set('name', url)
        kml.setVisible(true)
        return kml
    },

    createKMZ(url: string) {
        const proxyUrl = SearchEngineService.getLayerKmzUrl(url)

        const kmz = new AppVueObj.ol.layer.Vector({
            source: new AppVueObj.ol.source.Vector({
                url: proxyUrl,
                format: new AppVueObj.ol.format.KML()
            })
        })

        kmz.setZIndex(8)
        kmz.set('name', url)
        kmz.setVisible(true)
        return kmz
    },

    isLayerDisplayed(id: string) {
        const layerData: LayerData | undefined = AppVueObj.app.$store.getters[
            MapGetters.layers
        ].find((layerData: LayerData) => layerData.id === id)
        return layerData && layerData.visible
    },

    progressBarAddLoading() {
        let progressBarLoading =
            AppVueObj.app.$store.getters[MapGetters.progressBarLoading]
        if (!progressBarLoading) {
            LayersUtils.progressBarShow()
        }
        AppVueObj.app.$store.dispatch(
            MapActions.setProgressBarLoading,
            ++progressBarLoading
        )
        LayersUtils.progressBarUpdate()
    },

    progressBarAddLoaded() {
        let progressBarLoaded =
            AppVueObj.app.$store.getters[MapGetters.progressBarLoaded]
        AppVueObj.app.$store.dispatch(
            MapActions.setProgressBarLoaded,
            ++progressBarLoaded
        )
        LayersUtils.progressBarUpdate()
    },

    progressBarUpdate() {
        const progressBarLoading =
            AppVueObj.app.$store.getters[MapGetters.progressBarLoading]
        const progressBarLoaded =
            AppVueObj.app.$store.getters[MapGetters.progressBarLoaded]
        const progressBarPercentage = parseFloat(
            ((progressBarLoaded / progressBarLoading) * 100).toFixed(1)
        )
        AppVueObj.app.$store.dispatch(
            MapActions.setProgressBarPercentage,
            progressBarPercentage
        )
        if (progressBarLoading === progressBarLoaded) {
            setTimeout(() => {
                LayersUtils.progressBarHide()
            }, 1000)
        }
    },

    progressBarShow() {
        AppVueObj.app.$store.dispatch(MapActions.setProgressBarEnable, true)
    },

    progressBarHide() {
        const progressBarLoading =
            AppVueObj.app.$store.getters[MapGetters.progressBarLoading]
        const progressBarLoaded =
            AppVueObj.app.$store.getters[MapGetters.progressBarLoaded]
        if (progressBarLoading === progressBarLoaded) {
            AppVueObj.app.$store.dispatch(MapActions.setProgressBarLoading, 0)
            AppVueObj.app.$store.dispatch(MapActions.setProgressBarLoaded, 0)
            AppVueObj.app.$store.dispatch(
                MapActions.setProgressBarPercentage,
                0
            )
        }
        AppVueObj.app.$store.dispatch(MapActions.setProgressBarEnable, false)
    },

    async toggleLayer(layer: any, coordinates: any, layerImage: any) {
        const layerData: LayerData | undefined = AppVueObj.app.$store.getters[
            MapGetters.layers
        ].find((layerData: LayerData) => layerData.id === layer.url)
        if (layerData) {
            AppVueObj.app.$store.dispatch(MapActions.changeLayerVisibility, {
                id: layer.url,
                value: !layerData.visible
            })
        } else {
            if (coordinates) {
                LogService.logElementClick(
                    coordinates,
                    null,
                    layer.id,
                    'dab',
                    'Toggle layer',
                    null,
                    '',
                    layer.title
                )
            }
            let mapLayer: any = null
            if (layer.type === LayerTypes.WMS) {
                mapLayer = LayersUtils.createWMS(layer.title, layer.url)
            } else if (layer.type === LayerTypes.TMS) {
                mapLayer = LayersUtils.createTMS(layer.url)
            } else if (layer.type === LayerTypes.KML) {
                const [err] = await to(
                    LayersUtils.checkLayerFile(layer.url, 'kml')
                )
                if (!err) {
                    mapLayer = LayersUtils.createKML(layer.url)
                } else {
                    const props = {
                        title: $tc('general.error'),
                        subtitle: err
                    }
                    AppVueObj.app.$store.dispatch(PopupActions.openPopup, {
                        contentId: 'error',
                        component: ErrorPopup,
                        props
                    })
                }
            } else if (layer.type === LayerTypes.KMZ) {
                const [err] = await to(
                    LayersUtils.checkLayerFile(layer.url, 'kmz')
                )
                if (!err) {
                    mapLayer = LayersUtils.createKMZ(layer.url)
                } else {
                    const props = {
                        title: $tc('general.error'),
                        subtitle: err
                    }
                    AppVueObj.app.$store.dispatch(PopupActions.openPopup, {
                        contentId: 'error',
                        component: ErrorPopup,
                        props
                    })
                }
            }
            const [, legend] = await to(
                new Promise((resolve) => {
                    if (layer.legend) {
                        if (layer.legend.type === 'img') {
                            const img = new Image()
                            img.onload = () => {
                                resolve(layer.legend)
                            }
                            img.onerror = resolve
                            img.src = layer.legend.data
                        }
                    } else {
                        resolve(null)
                    }
                })
            )

            LayersUtils.addOnTileLoadStartHandler(mapLayer)
            LayersUtils.addOnTileLoadEndHandler(mapLayer)
            LayersUtils.addOnTileErrorHandler(mapLayer).catch(async () => {
                LogService.logResourceError(
                    false,
                    null,
                    null,
                    'Attempting to map service to get layer',
                    'Map service is not available at the moment',
                    `Layer type: ${LayerTypes.WMS}, layer name: ${layer.title}`
                )

                if (!AppVueObj.app.$store.getters[GeneralGetters.isWidget]) {
                    AppVueObj.app.$store.dispatch(MapActions.setShowFull, false)
                }

                const props = {
                    title: $tc('general.backendError'),
                    subtitle: $tc('popupContent.mapLayerUnavailable'),
                    actions: [
                        {
                            event: 'ignore',
                            label: $tc('general.ignore')
                        },
                        {
                            event: 'disable',
                            label: $tc('general.disable')
                        }
                    ]
                }

                const [, event] = await to(
                    AppVueObj.app.$store.dispatch(PopupActions.openPopup, {
                        contentId: 'error',
                        component: ErrorPopup,
                        props
                    })
                )
                if (event === 'disable') {
                    AppVueObj.app.$store.dispatch(
                        MapActions.removeLayer,
                        layer.url
                    )
                }
            })

            const layerData: any = {
                layer: mapLayer,
                id: layer.url,
                type: LayerTypes.WMS,
                title: layer.title,
                coordinate: null,
                image:
                    layerImage.indexOf('geoss-gray.svg') !== -1
                        ? null
                        : layerImage,
                legend
            }

            if (coordinates) {
                const entryCoordsSplit = coordinates.split(' ')

                const minLongitude = Number(entryCoordsSplit[1])
                const maxLongitude = Number(entryCoordsSplit[3])
                const minLatitude = Number(entryCoordsSplit[0])
                const maxLatitude = Number(entryCoordsSplit[2])

                layerData.coordinate = {
                    W: minLongitude,
                    S: minLatitude,
                    E: maxLongitude,
                    N: maxLatitude
                }
            }

            AppVueObj.app.$store.dispatch(MapActions.addLayer, layerData)

            if (AppVueObj.app.$store.getters[GeneralGetters.isWidget]) {
                AppVueObj.app.$emit('add-layer', mapLayer)
            } else {
                AppVueObj.app.$store.dispatch(
                    MapActions.changeLayerVisibility,
                    { id: LayerTypes.BOUNDING, value: false }
                )

                AppVueObj.app.$store.dispatch(MapActions.setShowFull, true)

                setTimeout(
                    () => {
                        AppVueObj.app.$store.dispatch(
                            MapActions.zoomInLayer,
                            layer.url
                        )
                    },
                    Timers.hideSearchContainer > Timers.closePopup
                        ? Timers.hideSearchContainer
                        : Timers.closePopup
                )
            }
        }
    },

    reloadTile(layer: any, event: any) {
        return new Promise((resolve, reject) => {
            const tileLoadRetryLimit = 3
            const layerLoadRetryLimit = 10
            const currentTile = event.tile
            const currentLayer = layer

            // add property to tile with retry count
            let tileLoadRetryCount = 0
            if (currentTile.loadRetryCount) {
                tileLoadRetryCount = currentTile.loadRetryCount
            }
            currentTile.loadRetryCount = tileLoadRetryCount + 1

            // add property to layer with retry count
            let layerLoadRetryCount = 0
            if (currentLayer.loadRetryCount) {
                layerLoadRetryCount = currentLayer.loadRetryCount
            }
            currentLayer.loadRetryCount = layerLoadRetryCount + 1

            if (
                tileLoadRetryCount >= tileLoadRetryLimit ||
                layerLoadRetryCount >= layerLoadRetryLimit
            ) {
                reject()
            } else {
                event.tile.load()
                resolve
            }
        })
    },

    addOnTileLoadStartHandler(layer: VectorLayer | TileLayer) {
        layer.getSource().on('tileloadstart', () => {
            LayersUtils.progressBarAddLoading()
        })
    },

    addOnTileLoadEndHandler(layer: VectorLayer | TileLayer) {
        layer.getSource().on('tileloadend', () => {
            LayersUtils.progressBarAddLoaded()
        })
    },

    addOnTileErrorHandler(layer: VectorLayer | TileLayer) {
        layer.getSource().on('tileloaderror', () => {
            LayersUtils.progressBarAddLoaded()
        })
        return new Promise((resolve, reject) => {
            if (!layer.getSource) {
                return resolve
            }
            layer.getSource().on('tileloaderror', (event) => {
                LayersUtils.reloadTile(layer, event).catch(reject)
            })
        })
    },

    checkLayerFile(urlToResource: string, type: string) {
        return GeossSearchApiService.loadLayerFile(urlToResource, type).then(
            (data: any) => {
                if (typeof data === 'object') {
                    if (
                        !String(data.result).localeCompare('true') &&
                        !type.localeCompare('kml')
                    ) {
                        return Promise.resolve()
                    } else if (!type.localeCompare('kml')) {
                        if (String(data.resultDetails)) {
                            return Promise.reject(
                                `Can not add file type kml to map layer, because it is error: ${String(
                                    data.resultDetails
                                )}.
                                  However, the file can be downloaded <a href="${urlToResource}" target="_blank">here</a>.`
                            )
                        } else {
                            return Promise.reject(
                                `Can not add file type kml to map layer, because the file is too big.
                                However, the file can be downloaded <a href="${urlToResource}" target="_blank">here</a>.`
                            )
                        }
                    } else if (
                        !String(data.result).localeCompare('true') &&
                        !type.localeCompare('kmz')
                    ) {
                        return Promise.resolve()
                    } else if (!type.localeCompare('kmz')) {
                        if (String(data.resultDetails)) {
                            return Promise.reject(
                                `Can not add file type kmz to map layer, because it is error: ${String(
                                    data.resultDetails
                                )}.
                                  However, the file can be downloaded <a href="${urlToResource}" target="_blank">here</a>.`
                            )
                        } else {
                            return Promise.reject(
                                `Can not add file type kmz to map layer, because the file is too big.
                                However, the file can be downloaded <a href="${urlToResource}" target="_blank">here</a>.`
                            )
                        }
                    }
                } else {
                    return Promise.reject('Request error')
                }
            },
            (err: any) => {
                return Promise.reject(`Request error: ${err}`)
            }
        )
    },

    createStatisticsLayer(title: string, statistics: any, year: string) {
        const arr: number[] = Object.values(statistics)
        const min = Math.min(...arr)
        const max = Math.max(...arr)

        const statisticsLayer = new AppVueObj.ol.layer.Vector({
            source: new AppVueObj.ol.source.Vector({
                format: new AppVueObj.ol.format.GeoJSON(),
                url: `/data/countriesUNSD.geojson`
            }),
            style: (feature: { [key: string]: any }) => {
                const layerName = feature.get('name')
                if (statistics[layerName]) {
                    feature.set('description', title)
                    feature.set('stats_value', statistics[layerName])
                    feature.set('stats_year', year)
                    return new AppVueObj.ol.style.Style({
                        fill: new AppVueObj.ol.style.Fill({
                            color: LayersUtils.getColorScale(
                                statistics[layerName],
                                min,
                                max
                            )
                        }),
                        stroke: new AppVueObj.ol.style.Stroke({
                            color: 'rgb(0,0,0)',
                            width: 1
                        })
                    })
                }
                return new AppVueObj.ol.style.Style({
                    stroke: new AppVueObj.ol.style.Stroke({
                        color: 'rgb(0,0,0)',
                        width: 1
                    })
                })
            }
        })
        statisticsLayer.set('name', 'UNEP')
        statisticsLayer.setZIndex(8)
        return statisticsLayer
    },

    getColorScale(val: number, min: number, max: number) {
        const range = max - min
        if (range === 0) {
            val = 100
        } else {
            val = ((val - min) / range) * 100
        }
        let r = 0
        let g = 0
        const b = 0
        if (val < 50) {
            r = 255
            g = Math.round(5.1 * val)
        } else {
            g = 255
            r = Math.round(510 - 5.1 * val)
        }
        const h = r * 0x10000 + g * 0x100 + b * 0x1
        return '#' + ('000000' + h.toString(16)).slice(-6)
    }
}

export default LayersUtils
