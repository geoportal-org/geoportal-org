import { MapGetters } from '@/store/map/map-getters'
import { MapActions } from '@/store/map/map-actions'
import { SearchActions } from '@/store/search/search-actions'
import { SearchGetters } from '@/store/search/search-getters'
import { LayerData } from '@/interfaces/LayerData'
import { LayerTypes } from '@/interfaces/LayerTypes'
// import {
//     MapDragAndDropInteraction,
//     DefaultShapesStyles,
// } from '~/plugins/OpenLayers'
import { AppVueObj } from '~/data/global'
import UtilsService from '@/services/utils.service'
import { getWmsLayerInfo } from '@/services/wms.service'
import NotificationService from '@/services/notification.service'
import LayerTilesService from '@/services/map/layer-tiles.service'
import LayersUtils from '@/services/map/layer-utils'

// import Overlay from 'ol/Overlay'
// mport { TileWMS } from 'ol/source'
import Vue from 'vue'
import to from '@/utils/to'
// import { Units } from 'ol/control/ScaleLine'
import { FeatureClass } from 'ol/Feature'
import Map from 'ol/Map'

declare let loadshp: any

const MapUtils = {
    /**
     * Creates controller displaying the coordinates
     * while mouse is over the map
     */
    createMapPositionController: () => {
        return new AppVueObj.ol.control.MousePosition({
            coordinateFormat: AppVueObj.ol.coordinate.createStringXY(4),
            projection: 'EPSG:4326',
            undefinedHTML: '&nbsp;',
        })
    },

    /**
     * Creates controller displaying scale of the map
     */
    createScaleLineController: () => {
        const scaleLineControl = new AppVueObj.ol.control.ScaleLine()
        scaleLineControl.setUnits(AppVueObj.ol.Units.METRIC)
        return scaleLineControl
    },

    /**
     * Creates interaction displaying layer that was first
     * dragged and then dropped into the map area
     */
    createDragAndDropInteraction: () => {
        //@ts-ignore
        AppVueObj.ol.MapDragAndDropInteraction.on(
            'addfeatures',
            async (event: any) => {
                const [, { id, features }] = await to(
                    new Promise((resolve) => {
                        if (event.file.name.indexOf('.zip') > -1) {
                            loadshp(
                                {
                                    url: event.file,
                                    encoding: 'utf-8',
                                },
                                (geojson: any) => {
                                    const collection =
                                        new AppVueObj.ol.Collection(
                                            new AppVueObj.ol.format.GeoJSON().readFeatures(
                                                geojson
                                            )
                                        )

                                    collection.forEach((f: any) => {
                                        f.getGeometry().transform(
                                            'EPSG:4326',
                                            'EPSG:3857'
                                        )
                                    })

                                    resolve({
                                        id: event.file.name,
                                        features: collection,
                                    })
                                }
                            )
                        } else {
                            resolve({
                                id: event.file.name,
                                features: event.features,
                            })
                        }
                    })
                )

                const vectorSource = new AppVueObj.ol.source.Vector({
                    features,
                })
                const layer = new AppVueObj.ol.layer.Vector({
                    source: vectorSource,
                    style: (feature: FeatureClass, resolution: number) => {
                        const featureStyleFunction = feature.getStyleFunction()
                        if (featureStyleFunction) {
                            return featureStyleFunction.call(
                                undefined,
                                feature,
                                resolution
                            )
                        } else {
                            return (<any>AppVueObj.ol.DefaultShapesStyles)[
                                feature.getGeometry()!.getType()
                            ]
                        }
                    },
                })
                layer.setZIndex(8)

                AppVueObj.app.$store.dispatch(MapActions.addLayer, {
                    layer,
                    id,
                    type: LayerTypes.CUSTOM,
                    title: id,
                })

                AppVueObj.app.$store.dispatch(MapActions.zoomInLayer, id)
            }
        )
        return AppVueObj.ol.MapDragAndDropInteraction
    },

    addBoundingBoxInteractions: (map: Map) => {
        /**
         * Checks if mouse is over bouding box
         * and if it is, then remember hovered layer
         * and ask for redrawing bouding boxes to apply proper styles;
         * This is disabled in case some layer has been clicked
         */
        map.on('pointermove', (event) => {
            if (AppVueObj.app.$store.getters[MapGetters.clickedLayerId]) {
                return
            }
            const mouseCoordInMapPixels = event.pixel
            const layer = map.forEachFeatureAtPixel(
                mouseCoordInMapPixels,
                (feature, layer) => {
                    return layer
                }
            )

            if (layer) {
                for (const layerData of AppVueObj.app.$store.getters[
                    MapGetters.boudningLayers
                ] as LayerData[]) {
                    if (layerData.value === layer) {
                        AppVueObj.app.$store.dispatch(
                            MapActions.setHoveredLayerId,
                            layerData.id
                        )
                        AppVueObj.app.$store.dispatch(
                            MapActions.repaintBoudingLayers
                        )
                        return
                    }
                }
            }

            if (
                AppVueObj.app.$store.getters[MapGetters.hoveredLayerId] !== null
            ) {
                AppVueObj.app.$store.dispatch(
                    MapActions.setHoveredLayerId,
                    null
                )
                AppVueObj.app.$store.dispatch(MapActions.repaintBoudingLayers)
            }
        })

        /**
         * Checks if clicked area of the map corresponds
         * to some layer and if it does, then remember
         * this layer and ask for redraw
         */
        map.on('click', (event) => {
            const mouseCoordInMapPixels = event.pixel
            const layer = map.forEachFeatureAtPixel(
                mouseCoordInMapPixels,
                (feature, layer) => {
                    return layer
                }
            )

            if (layer) {
                const resultIdDetails =
                    AppVueObj.app.$store.getters[SearchGetters.resultIdDetails]
                for (const layerData of AppVueObj.app.$store.getters[
                    MapGetters.boudningLayers
                ] as LayerData[]) {
                    if (layerData.value === layer) {
                        AppVueObj.app.$store.dispatch(
                            MapActions.setHoveredLayerId,
                            null
                        )
                        AppVueObj.app.$store.dispatch(
                            MapActions.setClickedLayerId,
                            layerData.id
                        )
                        if (resultIdDetails !== layerData.id) {
                            AppVueObj.app.$store.dispatch(
                                SearchActions.setResultIdDetails,
                                null
                            )
                        }
                        AppVueObj.app.$store.dispatch(
                            MapActions.repaintBoudingLayers
                        )
                        return
                    }
                }
            }

            if (
                AppVueObj.app.$store.getters[MapGetters.clickedLayerId] !== null
            ) {
                AppVueObj.app.$store.dispatch(
                    MapActions.setHoveredLayerId,
                    null
                )
                AppVueObj.app.$store.dispatch(
                    MapActions.setClickedLayerId,
                    null
                )
                AppVueObj.app.$store.dispatch(MapActions.repaintBoudingLayers)
            }
        })

        /**
         * Shuffle bbox mechanism:
         * If bbox is clicked it moves down at the bottom unveiling
         * other bboxes, so user is able to select another one.
         * If all bboxes under the cursor are at the bottom, the default
         * zIndices are restored for all boxes besides selected one.
         */
        map.on('click', (event) => {
            // If bounding layer is disabled or set to invisible in layer menu -> return;
            const allLayers = AppVueObj.app.$store.getters[MapGetters.layers]
            const boundingBoxLayerData: LayerData = allLayers.find(
                (layerData: { id: LayerTypes }) =>
                    layerData.id === LayerTypes.BOUNDING
            )
            if (
                !boundingBoxLayerData.visible ||
                boundingBoxLayerData.transparency === 0
            ) {
                return
            }

            const mouseCoordInMapPixels = event.pixel
            const layer = map.forEachFeatureAtPixel(
                mouseCoordInMapPixels,
                (feature, layer) => {
                    return layer
                }
            )

            if (layer) {
                for (const layerData of AppVueObj.app.$store.getters[
                    MapGetters.boudningLayers
                ] as LayerData[]) {
                    if (layerData.value === layer) {
                        // By deafult all bboxes layers have zIndices > 20
                        if (layerData.value.getZIndex() !== 20) {
                            // Push clicked layer to the bottom
                            layerData.value.setZIndex(20)
                        } else {
                            // Keep clicked layer at the bottom and restore default zIndex of other layers
                            for (const otherLayerData of AppVueObj.app.$store.getters[
                                MapGetters.boudningLayers
                            ].filter(
                                (item: LayerData) => item !== layerData
                            ) as LayerData[]) {
                                otherLayerData.value.setZIndex(
                                    otherLayerData.index! + 21
                                )
                            }
                            layerData.value.setZIndex(20)
                        }
                    }
                }
            }
        })
    },

    /**
     * If UNSD layer is active turn on the listener showing
     * popup with basic information of selected feature (country).
     * The listener works only if exactly ONE layer is added.
     */
    addUNSDStatisticsInteractions: (map: Map) => {
        map.on('singleclick', (event) => {
            const hitLayer = event.target.forEachLayerAtPixel(
                event.pixel,
                (layer: any) => layer,
                {
                    layerFilter: (layer: { getOpacity: () => any }) => {
                        const isCompareLayer =
                            AppVueObj.app.$store.getters[
                                MapGetters.compareLayer
                            ]
                        const compareBarPosition =
                            AppVueObj.app.$store.getters[
                                MapGetters.compareBarPosition
                            ]
                        if (
                            isCompareLayer &&
                            isCompareLayer.value === layer &&
                            event.pixel[0] <= compareBarPosition
                        ) {
                            return false
                        }
                        return layer.getOpacity()
                    },
                }
            )
            if (!hitLayer || (hitLayer && !hitLayer.getOpacity())) {
                return
            }

            // Create placeholder for map popup
            if (!AppVueObj.app.$store.getters[MapGetters.mapTooltip]) {
                const popupOverlay = new AppVueObj.ol.Overlay({
                    element: document.getElementById('map-tooltip'),
                    stopEvent: false,
                } as { element: HTMLElement | undefined; stopEvent: boolean })
                AppVueObj.app.$store.dispatch(
                    MapActions.setMapTooltip,
                    popupOverlay
                )
                map.addOverlay(popupOverlay)
            }
            const popupOverlay = AppVueObj.app.$store.getters[
                MapGetters.mapTooltip
            ] as any
            let overlayPosition
            // @ts-ignore
            popupOverlay.setPosition(undefined)

            const UNSDLayers = AppVueObj.app.$store.getters[
                MapGetters.customLayers
            ] as LayerData[]
            if (UNSDLayers.length) {
                const mouseCoordInMapPixels = event.pixel
                let outmostFeature: any
                const layers: any[] = []
                map.forEachFeatureAtPixel(
                    mouseCoordInMapPixels,
                    (feature, layer) => {
                        if (
                            layer.get('name') &&
                            layer.get('name') === 'UNEP' &&
                            layer.getOpacity() &&
                            layer === hitLayer
                        ) {
                            layers.push(layer)
                            if (!outmostFeature) {
                                outmostFeature = feature
                            }
                        }
                    }
                )

                if (layers.length && outmostFeature) {
                    for (const layer of layers) {
                        if (outmostFeature.get('stats_value')) {
                            // Update popup message and position
                            let desc = outmostFeature.get('description')
                            if (desc.length > 60) {
                                const truncatedDesc = `${desc.substring(
                                    0,
                                    57
                                )}...`
                                desc = truncatedDesc
                            }
                            const message = `Layer: ${desc} <br />
							Country: ${outmostFeature.get('name')} <br />
							Year: ${outmostFeature.get('stats_year')} <br />
							Value: ${UtilsService.roundToFirstDecimal(+outmostFeature.get('stats_value'))}`
                            AppVueObj.app.$store.dispatch(
                                MapActions.setMapTooltipMessage,
                                message
                            )
                            overlayPosition = event.coordinate
                        }
                    }
                }
                // @ts-ignore
                popupOverlay.setPosition(overlayPosition)
            }

            const source = hitLayer.getSource()
            if (source instanceof AppVueObj.ol.source.TileWMS) {
                const viewResolution: number = map.getView().getResolution()!
                const url: string = source.getGetFeatureInfoUrl(
                    event.coordinate,
                    viewResolution,
                    'EPSG:3857',
                    { INFO_FORMAT: 'text/html' }
                )!

                let message = `Layer: ${hitLayer.get('name')} <br />`

                if (url) {
                    getWmsLayerInfo(url)
                        .then((response: string) => {
                            const template = document.createElement('template')
                            template.insertAdjacentHTML(
                                'afterbegin',
                                response.trim()
                            )
                            const featureInfoElement =
                                template.querySelector('.featureInfo')
                            const data: any = {}
                            if (featureInfoElement) {
                                featureInfoElement
                                    .querySelectorAll('th')
                                    .forEach(
                                        (element) =>
                                            (data[element.innerText] = null)
                                    )
                                const keys = Object.keys(data)
                                featureInfoElement
                                    .querySelectorAll('td')
                                    .forEach(
                                        (element, index) =>
                                            (data[keys[index]] =
                                                element.innerText)
                                    )

                                keys.forEach((item) => {
                                    const label =
                                        AppVueObj.app.$tc(
                                            `wms.${item.toLowerCase()}`
                                        ) || item
                                    message += `${label}: ${
                                        data[item] || '-'
                                    }<br>`
                                })
                            } else {
                                message += AppVueObj.app.$tc('general.noData')
                            }

                            AppVueObj.app.$store.dispatch(
                                MapActions.setMapTooltipMessage,
                                message
                            )
                            overlayPosition = event.coordinate
                            popupOverlay.setPosition(overlayPosition)
                        })
                        .catch((e: any) =>
                            AppVueObj.app.$store.dispatch(
                                MapActions.setMapTooltipMessage,
                                AppVueObj.app.$tc('general.errorOccurred')
                            )
                        )
                }
            }
        })
    },

    /**
     * Validates whether the map tile is available and
     * in case it is not, then substitute for reliable working tile
     */
    addOnErrorMapSwitcher: () => {
        const addsat = LayerTilesService.addsat.getLayerTile()
        addsat.getSource().on('tileloaderror', (event: any) => {
            LayersUtils.reloadTile(addsat, event).catch(() => {
                MapUtils.onTileError()
            })
        })

        const mbHybrid = LayerTilesService.mb_hybrid.getLayerTile()
        mbHybrid.getSource().on('tileloaderror', (event: any) => {
            LayersUtils.reloadTile(mbHybrid, event).catch(() => {
                MapUtils.onTileError()
            })
        })

        const addhyb = LayerTilesService.addhyb.getLayerTile()
        addhyb.on('tileloaderror', (event: any) => {
            LayersUtils.reloadTile(addhyb, event).catch(() => {
                MapUtils.onTileError()
            })
        })
    },

    onTileError: () => {
        LayerTilesService.addsat.disabled = true
        LayerTilesService.mb_hybrid.disabled = true
        LayerTilesService.addhyb.disabled = true
        AppVueObj.app.$store.dispatch(
            MapActions.setActiveLayerTileId,
            'WorldStreetBasemap'
        )

        NotificationService.show(
            `${AppVueObj.app.$tc('notifications.basemapUnavailableTitle')}`,
            `${AppVueObj.app.$tc('notifications.basemapUnavailable')}`,
            30000,
            'basemap-unvailable',
            undefined,
            'info'
        )
    },

    /**
     * Add layers that were queued before map initialization
     * Example: Geo-Gnome mountains loaded from backend
     */
    loadQueuedLayers: (map: { addLayer: (arg0: any) => void }) => {
        for (const layer of AppVueObj.app.$store.getters[MapGetters.layers]) {
            if (layer.id !== 'bounding') {
                map.addLayer(layer.value)
            }
        }
    },
}

export default MapUtils
