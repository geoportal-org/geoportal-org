// @ts-ignore
import { Draw, createBox } from 'ol/interaction/Draw'
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

import Polygon from 'ol/geom/Polygon'
import Point from 'ol/geom/Point'
import MultiPoint from 'ol/geom/MultiPoint'
import MultiPolygon from 'ol/geom/MultiPolygon'

import {
    transform,
    fromLonLat,
    getTransform,
    get as getProjection,
} from 'ol/proj'

import { Style, Fill, Stroke, Circle, Icon } from 'ol/style'

import { applyTransform, getTopLeft, getWidth } from 'ol/extent'

const DrawCopy = Draw
DrawCopy.createBox = createBox

const ol = {
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
}

export default ol
