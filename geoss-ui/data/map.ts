// import { MapCoordinate } from '@/interfaces/MapCoordinate'
// import Feature from 'ol/Feature'
// import { AppVueObj } from './global'

// export const MapSource = new AppVueObj.ol.source.Vector({
//     wrapX: false,
// })

// const vector: any = new AppVueObj.ol.layer.Vector({
//     source: MapSource,
//     style: new AppVueObj.ol.style.Style({
//         zIndex: 20,
//         fill: new AppVueObj.ol.style.Fill({
//             color: 'rgba(255, 255, 255, 0.2)',
//         }),
//         stroke: new AppVueObj.ol.style.Stroke({
//             color: 'red',
//             width: 2,
//         }),
//         image: new AppVueObj.ol.style.Circle({
//             radius: 7,
//             fill: new AppVueObj.ol.style.Fill({
//                 color: '#ffcc33',
//             }),
//         }),
//     }),
// })

// export function getMapDrawnLayer(rectangle: Feature) {
//     const layer = new AppVueObj.ol.layer.Vector({
//         source: new AppVueObj.ol.source.Vector({
//             features: [rectangle],
//         }),
//         style: new AppVueObj.ol.style.Style({
//             fill: new AppVueObj.ol.style.Fill({
//                 color: 'rgba(255, 255, 255, 0.2)',
//             }),
//             stroke: new AppVueObj.ol.style.Stroke({
//                 color: 'red',
//                 width: 2,
//             }),
//         }),
//     })
//     layer.setZIndex(9)
//     return layer
// }

// export const MapVector = vector

// export const MapDragAndDropInteraction =
//     new AppVueObj.ol.interaction.DragAndDrop({
//         formatConstructors: [
//             new AppVueObj.ol.format.GPX(),
//             new AppVueObj.ol.format.GeoJSON(),
//             new AppVueObj.ol.format.IGC(),
//             new AppVueObj.ol.format.KML(),
//             new AppVueObj.ol.format.TopoJSON(),
//         ],
//         projection: undefined,
//     })

// const what3wordsStyle = new AppVueObj.ol.style.Style({
//     image: new AppVueObj.ol.style.Icon({
//         anchor: [0.5, 1],
//         opacity: 0.75,
//         src: `/img/w3wlogo.png`,
//     }),
// })

// export const getWhat3wordsLayer = (coordinates: MapCoordinate) => {
//     const lon = coordinates.W + (coordinates.E - coordinates.W) / 2
//     const lat = coordinates.N - (coordinates.N - coordinates.S) / 2

//     const w3wIcon = new AppVueObj.ol.Feature({
//         geometry: new AppVueObj.ol.geom.Point([lon, lat]),
//     })

//     w3wIcon.getGeometry()!.transform('EPSG:4326', 'EPSG:3857')

//     w3wIcon.setStyle(what3wordsStyle)

//     const w3wSource = new AppVueObj.ol.source.Vector({
//         features: [w3wIcon],
//     })

//     return new AppVueObj.ol.layer.Vector({
//         source: w3wSource,
//     })
// }

// export const DefaultShapesStyles = {
//     Point: new AppVueObj.ol.style.Style({
//         image: new AppVueObj.ol.style.Circle({
//             fill: new AppVueObj.ol.style.Fill({
//                 color: '#00E278',
//             }),
//             radius: 5,
//             stroke: new AppVueObj.ol.style.Stroke({
//                 color: '#087829',
//                 width: 1,
//             }),
//         }),
//     }),
//     LineString: new AppVueObj.ol.style.Style({
//         stroke: new AppVueObj.ol.style.Stroke({
//             color: '#00E278',
//             width: 3,
//         }),
//     }),
//     Polygon: new AppVueObj.ol.style.Style({
//         fill: new AppVueObj.ol.style.Fill({
//             color: '#00E278',
//         }),
//         stroke: new AppVueObj.ol.style.Stroke({
//             color: '#087829',
//             width: 1,
//         }),
//     }),
//     MultiPoint: new AppVueObj.ol.style.Style({
//         image: new AppVueObj.ol.style.Circle({
//             fill: new AppVueObj.ol.style.Fill({
//                 color: '#00E278',
//             }),
//             radius: 5,
//             stroke: new AppVueObj.ol.style.Stroke({
//                 color: '#087829',
//                 width: 1,
//             }),
//         }),
//     }),
//     MultiLineString: new AppVueObj.ol.style.Style({
//         stroke: new AppVueObj.ol.style.Stroke({
//             color: '#00E278',
//             width: 3,
//         }),
//     }),
//     MultiPolygon: new AppVueObj.ol.style.Style({
//         fill: new AppVueObj.ol.style.Fill({
//             color: '#00E278',
//         }),
//         stroke: new AppVueObj.ol.style.Stroke({
//             color: '#087829',
//             width: 1,
//         }),
//     }),
// }
