import Vue from 'vue'
import { MapGetters } from '@/store/map/map-getters'
import { AppVueObj } from '@/data/global'

// @ts-ignore
const ol = Vue.ol

const LayerTilesService: { [key: string]: any } = {
    osm: {
        img: '',
        title: 'OSM',
        disabled: false,
        tileLayer: null,
        getLayerTile: () => {
            const projection = ol.proj.getProjection('EPSG:3857')
            const projectionExtent = projection.getExtent()
            const origin = ol.extent.getTopLeft(projectionExtent)
            const size = ol.extent.getWidth(projectionExtent) / 256
            const resolutions = new Array(19)
            const matrixIds = new Array(19)
            for (let z = 0; z < 19; ++z) {
                resolutions[z] = size / Math.pow(2, z)
                matrixIds[z] = z
            }

            LayerTilesService.osm.tileLayer =
                LayerTilesService.osm.tileLayer ||
                new ol.layer.Tile({
                    /*
                     * Original OSM tile layer service was changed to GISCO service
                     * source: new ol.source.XYZ({ url: `https://c.tile.openstreetmap.org/{z}/{x}/{y}.png` })
                     */
                    source: new ol.source.WMTS({
                        url: 'https://gisco-services.ec.europa.eu/maps/service?',
                        layer: 'OSMCartoComposite',
                        matrixSet: 'EPSG3857',
                        format: 'image/png',
                        projection,
                        tileGrid: new ol.tilegrid.WMTSTileGrid({
                            origin,
                            resolutions,
                            matrixIds,
                        }),
                        style: 'default',
                        wrapX: true,
                    }),
                })
            return LayerTilesService.osm.tileLayer
        },
    },

    addsat: {
        img: 'mb_satellite.jpg',
        title: 'Default Basemap',
        disabled: false,
        tileLayer: null,
        getLayerTile: () => {
            LayerTilesService.addsat.tileLayer =
                LayerTilesService.addsat.tileLayer ||
                new ol.layer.Tile({
                    source: new ol.source.XYZ({
                        crossOrigin: 'Anonymous',
                        tileSize: [512, 512],
                        url: `https://api.mapbox.com/styles/v1/mapbox/satellite-v9/tiles/{z}/{x}/{y}?access_token=${
                            AppVueObj.app.$store.getters[
                                MapGetters.boxAccessToken
                            ]
                        }`,
                    }),
                })
            return LayerTilesService.addsat.tileLayer
        },
    },

    mb_hybrid: {
        img: 'mb_hybrid.jpg',
        title: 'Hybrid MapBox&OpenLayers Basemap',
        disabled: false,
        tileLayer: null,
        getLayerTile: () => {
            LayerTilesService.mb_hybrid.tileLayer =
                LayerTilesService.mb_hybrid.tileLayer ||
                new ol.layer.Tile({
                    source: new ol.source.XYZ({
                        crossOrigin: 'Anonymous',
                        tileSize: [512, 512],
                        url: `https://api.mapbox.com/styles/v1/mapbox/streets-v8/tiles/{z}/{x}/{y}?access_token=${
                            AppVueObj.app.$store.getters[
                                MapGetters.boxAccessToken
                            ]
                        }`,
                    }),
                })
            return LayerTilesService.mb_hybrid.tileLayer
        },
    },

    addhyb: {
        img: 'mb_openstreetmap.jpg',
        title: 'OpenStreetMap Basemap',
        disabled: false,
        tileLayer: null,
        getLayerTile: () => {
            LayerTilesService.addhyb.tileLayer =
                LayerTilesService.addhyb.tileLayer ||
                new ol.layer.Tile({
                    source: new ol.source.XYZ({
                        crossOrigin: 'Anonymous',
                        tileSize: [512, 512],
                        url: `https://api.mapbox.com/styles/v1/mapbox/satellite-streets-v9/tiles/{z}/{x}/{y}?access_token=${
                            AppVueObj.app.$store.getters[
                                MapGetters.boxAccessToken
                            ]
                        }`,
                    }),
                })
            return LayerTilesService.addhyb.tileLayer
        },
    },

    oceanBasemap: {
        img: 'oceans.jpg',
        title: 'Ocean Basemap',
        disabled: false,
        tileLayer: null,
        getLayerTile: () => {
            LayerTilesService.oceanBasemap.tileLayer =
                LayerTilesService.oceanBasemap.tileLayer ||
                new ol.layer.Tile({
                    source: new ol.source.TileArcGISRest({
                        crossOrigin: 'Anonymous',
                        url: 'http://services.arcgisonline.com/arcgis/rest/services/Ocean_Basemap/MapServer',
                    } as any),
                })
            return LayerTilesService.oceanBasemap.tileLayer
        },
    },

    topographicBasemap: {
        img: 'topographic.jpg',
        title: 'Topographic Basemap',
        disabled: false,
        tileLayer: null,
        getLayerTile: () => {
            LayerTilesService.topographicBasemap.tileLayer =
                LayerTilesService.topographicBasemap.tileLayer ||
                new ol.layer.Tile({
                    source: new ol.source.TileArcGISRest({
                        crossOrigin: 'Anonymous',
                        url: 'http://services.arcgisonline.com/arcgis/rest/services/World_Topo_Map/MapServer',
                    } as any),
                })
            return LayerTilesService.topographicBasemap.tileLayer
        },
    },

    WorldStreetBasemap: {
        img: 'streetMaps.jpg',
        title: 'World Street Basemap',
        disabled: false,
        tileLayer: null,
        getLayerTile: () => {
            LayerTilesService.WorldStreetBasemap.tileLayer = LayerTilesService
                .WorldStreetBasemap.tileLayer || [
                new ol.layer.Tile({
                    source: new ol.source.TileArcGISRest({
                        crossOrigin: 'Anonymous',
                        url: 'http://services.arcgisonline.com/arcgis/rest/services/World_Imagery/MapServer',
                    } as any),
                }),
                new ol.layer.Tile({
                    source: new ol.source.TileArcGISRest({
                        crossOrigin: 'Anonymous',
                        url: 'http://services.arcgisonline.com/ArcGIS/rest/services/Reference/World_Boundaries_and_Places/MapServer',
                    } as any),
                }),
            ]
            return LayerTilesService.WorldStreetBasemap.tileLayer
        },
    },

    dark_all: {
        img: 'dark.png',
        title: 'Dark Basemap',
        disabled: false,
        tileLayer: null,
        getLayerTile: () => {
            LayerTilesService.dark_all.tileLayer =
                LayerTilesService.dark_all.tileLayer ||
                new ol.layer.Tile({
                    source: new ol.source.XYZ({
                        crossOrigin: 'Anonymous',
                        url: 'https://cartodb-basemaps-{1-4}.global.ssl.fastly.net/dark_all/{z}/{x}/{y}.png',
                    }),
                })
            return LayerTilesService.dark_all.tileLayer
        },
    },

    dark_nolabels: {
        img: 'dark-no.png',
        title: 'Dark Basemap without labels',
        disabled: false,
        tileLayer: null,
        getLayerTile: () => {
            LayerTilesService.dark_nolabels.tileLayer =
                LayerTilesService.dark_nolabels.tileLayer ||
                new ol.layer.Tile({
                    source: new ol.source.XYZ({
                        crossOrigin: 'Anonymous',
                        url: 'https://cartodb-basemaps-{1-4}.global.ssl.fastly.net/dark_nolabels/{z}/{x}/{y}.png',
                    }),
                })
            return LayerTilesService.dark_nolabels.tileLayer
        },
    },

    light_all: {
        img: 'light.png',
        title: 'Light Basemap',
        disabled: false,
        tileLayer: null,
        getLayerTile: () => {
            LayerTilesService.light_all.tileLayer =
                LayerTilesService.light_all.tileLayer ||
                new ol.layer.Tile({
                    source: new ol.source.XYZ({
                        crossOrigin: 'Anonymous',
                        url: 'https://cartodb-basemaps-{1-4}.global.ssl.fastly.net/light_all/{z}/{x}/{y}.png',
                    }),
                })
            return LayerTilesService.light_all.tileLayer
        },
    },

    light_nolabels: {
        img: 'light-no.png',
        title: 'Light Basemap without labels',
        disabled: false,
        tileLayer: null,
        getLayerTile: () => {
            LayerTilesService.light_nolabels.tileLayer =
                LayerTilesService.light_nolabels.tileLayer ||
                new ol.layer.Tile({
                    source: new ol.source.XYZ({
                        crossOrigin: 'Anonymous',
                        url: 'https://cartodb-basemaps-{1-4}.global.ssl.fastly.net/light_nolabels/{z}/{x}/{y}.png',
                    }),
                })
            return LayerTilesService.light_nolabels.tileLayer
        },
    },
}

export default LayerTilesService
