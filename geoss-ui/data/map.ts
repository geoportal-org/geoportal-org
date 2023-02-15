import ol from '@/utils/ol'

import { MapCoordinate } from '@/interfaces/MapCoordinate'
import { StaticPath } from '@/data/global'
import Feature from 'ol/Feature'

export const MapSource = new ol.source.Vector({
    wrapX: false,
})

const vector: any = new ol.layer.Vector({
    source: MapSource,
    style: new ol.style.Style({
        zIndex: 20,
        fill: new ol.style.Fill({
            color: 'rgba(255, 255, 255, 0.2)',
        }),
        stroke: new ol.style.Stroke({
            color: 'red',
            width: 2,
        }),
        image: new ol.style.Circle({
            radius: 7,
            fill: new ol.style.Fill({
                color: '#ffcc33',
            }),
        }),
    }),
})

export function getMapDrawnLayer(rectangle: Feature) {
    const layer = new ol.layer.Vector({
        source: new ol.source.Vector({
            features: [rectangle],
        }),
        style: new ol.style.Style({
            fill: new ol.style.Fill({
                color: 'rgba(255, 255, 255, 0.2)',
            }),
            stroke: new ol.style.Stroke({
                color: 'red',
                width: 2,
            }),
        }),
    })
    layer.setZIndex(9)
    return layer
}

export const MapVector = vector

export const MapDragAndDropInteraction = new ol.interaction.DragAndDrop({
    formatConstructors: [
        new ol.format.GPX(),
        new ol.format.GeoJSON(),
        new ol.format.IGC(),
        new ol.format.KML(),
        new ol.format.TopoJSON(),
    ],
    projection: undefined,
})

const what3wordsStyle = new ol.style.Style({
    image: new ol.style.Icon({
        anchor: [0.5, 1],
        opacity: 0.75,
        src: `${StaticPath()}/img/w3wlogo.png`,
    }),
})

export const getWhat3wordsLayer = (coordinates: MapCoordinate) => {
    const lon = coordinates.W + (coordinates.E - coordinates.W) / 2
    const lat = coordinates.N - (coordinates.N - coordinates.S) / 2

    const w3wIcon = new ol.Feature({
        geometry: new ol.geom.Point([lon, lat]),
    })

    w3wIcon.getGeometry()!.transform('EPSG:4326', 'EPSG:3857')

    w3wIcon.setStyle(what3wordsStyle)

    const w3wSource = new ol.source.Vector({
        features: [w3wIcon],
    })

    return new ol.layer.Vector({
        source: w3wSource,
    })
}

export const DefaultShapesStyles = {
    Point: new ol.style.Style({
        image: new ol.style.Circle({
            fill: new ol.style.Fill({
                color: '#00E278',
            }),
            radius: 5,
            stroke: new ol.style.Stroke({
                color: '#087829',
                width: 1,
            }),
        }),
    }),
    LineString: new ol.style.Style({
        stroke: new ol.style.Stroke({
            color: '#00E278',
            width: 3,
        }),
    }),
    Polygon: new ol.style.Style({
        fill: new ol.style.Fill({
            color: '#00E278',
        }),
        stroke: new ol.style.Stroke({
            color: '#087829',
            width: 1,
        }),
    }),
    MultiPoint: new ol.style.Style({
        image: new ol.style.Circle({
            fill: new ol.style.Fill({
                color: '#00E278',
            }),
            radius: 5,
            stroke: new ol.style.Stroke({
                color: '#087829',
                width: 1,
            }),
        }),
    }),
    MultiLineString: new ol.style.Style({
        stroke: new ol.style.Stroke({
            color: '#00E278',
            width: 3,
        }),
    }),
    MultiPolygon: new ol.style.Style({
        fill: new ol.style.Fill({
            color: '#00E278',
        }),
        stroke: new ol.style.Stroke({
            color: '#087829',
            width: 1,
        }),
    }),
}
