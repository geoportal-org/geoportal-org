import { AppVueObj } from '~/data/global'
import Draw, { createBox } from 'ol/interaction/Draw'
import DragAndDrop from 'ol/interaction/DragAndDrop'
import * as interaction from 'ol/interaction'

import Map from 'ol/Map'

import View from 'ol/View'

import Collection from 'ol/Collection'

import { createStringXY } from 'ol/coordinate.js'

import { GeoJSON, GPX, IGC, KML, TopoJSON } from 'ol/format'

import { Vector, XYZ, TileArcGISRest, TileWMS, WMTS } from 'ol/source'

import WMTSTileGrid from 'ol/tilegrid/WMTS'

import VectorLayer from 'ol/layer/Vector'
import Tile from 'ol/layer/Tile'

import Feature from 'ol/Feature'

import Attribution from 'ol/control/Attribution'
import MousePosition from 'ol/control/MousePosition'
import ScaleLine from 'ol/control/ScaleLine'
import { Units } from 'ol/control/ScaleLine'

import Polygon from 'ol/geom/Polygon'
import Point from 'ol/geom/Point'
import MultiPoint from 'ol/geom/MultiPoint'
import MultiPolygon from 'ol/geom/MultiPolygon'
import { MapCoordinate } from '@/interfaces/MapCoordinate'
import Overlay from 'ol/Overlay'

import {
    transform,
    fromLonLat,
    getTransform,
    get as getProjection,
} from 'ol/proj'

import { Style, Fill, Stroke, Circle, Icon } from 'ol/style'

import { applyTransform, getTopLeft, getWidth } from 'ol/extent'

const DrawCopy: any = Draw
DrawCopy.createBox = createBox

export const ol = {
    interaction: {
        DragAndDrop,
        Draw: DrawCopy,
        defaults: (interaction as any).defaults,
    },
    Map,
    View,
    Collection,
    coordinate: {
        createStringXY,
    },
    format: {
        GeoJSON,
        GPX,
        IGC,
        KML,
        TopoJSON,
    },
    tilegrid: {
        WMTSTileGrid,
    },
    source: {
        Vector,
        XYZ,
        TileArcGISRest,
        TileWMS,
        WMTS,
    },
    layer: {
        Vector: VectorLayer,
        Tile,
    },
    Feature,
    control: {
        Attribution,
        MousePosition,
        ScaleLine,
    },
    geom: {
        MousePosition,
        MultiPoint,
        MultiPolygon,
        Polygon,
        Point,
    },
    proj: {
        transform,
        fromLonLat,
        getTransform,
        getProjection,
    },
    style: {
        Style,
        Fill,
        Stroke,
        Circle,
        Icon,
    },
    extent: {
        applyTransform,
        getTopLeft,
        getWidth,
    },
    Overlay,
    Units,
}

AppVueObj.ol = ol

// from /data/map.ts
AppVueObj.ol.MapSource = new AppVueObj.ol.source.Vector({
    wrapX: false,
})

const vector: any = new AppVueObj.ol.layer.Vector({
    source: AppVueObj.ol.MapSource,
    style: new AppVueObj.ol.style.Style({
        zIndex: 20,
        fill: new AppVueObj.ol.style.Fill({
            color: 'rgba(255, 255, 255, 0.2)',
        }),
        stroke: new AppVueObj.ol.style.Stroke({
            color: 'red',
            width: 2,
        }),
        image: new AppVueObj.ol.style.Circle({
            radius: 7,
            fill: new AppVueObj.ol.style.Fill({
                color: '#ffcc33',
            }),
        }),
    }),
})

AppVueObj.ol.getMapDrawnLayer = (rectangle: Feature) => {
    const layer = new AppVueObj.ol.layer.Vector({
        source: new AppVueObj.ol.source.Vector({
            features: [rectangle],
        }),
        style: new AppVueObj.ol.style.Style({
            fill: new AppVueObj.ol.style.Fill({
                color: 'rgba(255, 255, 255, 0.2)',
            }),
            stroke: new AppVueObj.ol.style.Stroke({
                color: 'red',
                width: 2,
            }),
        }),
    })
    layer.setZIndex(9)
    return layer
}

AppVueObj.ol.MapVector = vector

AppVueObj.ol.MapDragAndDropInteraction =
    new AppVueObj.ol.interaction.DragAndDrop({
        formatConstructors: [
            new AppVueObj.ol.format.GPX(),
            new AppVueObj.ol.format.GeoJSON(),
            new AppVueObj.ol.format.IGC(),
            new AppVueObj.ol.format.KML(),
            new AppVueObj.ol.format.TopoJSON(),
        ],
        projection: undefined,
    })

const what3wordsStyle = new AppVueObj.ol.style.Style({
    image: new AppVueObj.ol.style.Icon({
        anchor: [0.5, 1],
        opacity: 0.75,
        src: `/img/w3wlogo.png`,
    }),
})

AppVueObj.ol.getWhat3wordsLayer = (coordinates: MapCoordinate) => {
    const lon = coordinates.W + (coordinates.E - coordinates.W) / 2
    const lat = coordinates.N - (coordinates.N - coordinates.S) / 2

    const w3wIcon = new AppVueObj.ol.Feature({
        geometry: new AppVueObj.ol.geom.Point([lon, lat]),
    })

    w3wIcon.getGeometry()!.transform('EPSG:4326', 'EPSG:3857')

    w3wIcon.setStyle(what3wordsStyle)

    const w3wSource = new AppVueObj.ol.source.Vector({
        features: [w3wIcon],
    })

    return new AppVueObj.ol.layer.Vector({
        source: w3wSource,
    })
}

AppVueObj.ol.DefaultShapesStyles = {
    Point: new AppVueObj.ol.style.Style({
        image: new AppVueObj.ol.style.Circle({
            fill: new AppVueObj.ol.style.Fill({
                color: '#00E278',
            }),
            radius: 5,
            stroke: new AppVueObj.ol.style.Stroke({
                color: '#087829',
                width: 1,
            }),
        }),
    }),
    LineString: new AppVueObj.ol.style.Style({
        stroke: new AppVueObj.ol.style.Stroke({
            color: '#00E278',
            width: 3,
        }),
    }),
    Polygon: new AppVueObj.ol.style.Style({
        fill: new AppVueObj.ol.style.Fill({
            color: '#00E278',
        }),
        stroke: new AppVueObj.ol.style.Stroke({
            color: '#087829',
            width: 1,
        }),
    }),
    MultiPoint: new AppVueObj.ol.style.Style({
        image: new AppVueObj.ol.style.Circle({
            fill: new AppVueObj.ol.style.Fill({
                color: '#00E278',
            }),
            radius: 5,
            stroke: new AppVueObj.ol.style.Stroke({
                color: '#087829',
                width: 1,
            }),
        }),
    }),
    MultiLineString: new AppVueObj.ol.style.Style({
        stroke: new AppVueObj.ol.style.Stroke({
            color: '#00E278',
            width: 3,
        }),
    }),
    MultiPolygon: new AppVueObj.ol.style.Style({
        fill: new AppVueObj.ol.style.Fill({
            color: '#00E278',
        }),
        stroke: new AppVueObj.ol.style.Stroke({
            color: '#087829',
            width: 1,
        }),
    }),
}
