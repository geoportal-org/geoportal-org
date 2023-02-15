import MapCoordinatesUtils from '@/services/map/coordinates-utils'
import LayersUtils from '@/services/map/layer-utils'
import { LayerTypes } from '@/interfaces/LayerTypes'
import UtilsService from './utils.service'

export const ResultService = {
    getBoundingBoxesAndPins(item: any) {
        let boxesAndPins: any[] = [] // Candidates for pins and boxes
        const boxes: any[] = [] // Filtered candidates (boxes)
        const pins: any[] = [] // Filtered candidates (pins)

        // Possible datasets
        const itemBoxesData = item.box
        let cswisogeoData = UtilsService.getArrayByString(
            item,
            'gmd:identificationInfo.gmd:MD_DataIdentification.gmd:extent.gmd:EX_Extent.gmd:geographicElement.gmd:EX_GeographicBoundingBox'
        )
        if (!cswisogeoData.length) {
            cswisogeoData = UtilsService.getArrayByString(
                item,
                'gmd:identificationInfo.srv:SV_ServiceIdentification.srv:extent.gmd:EX_Extent.gmd:geographicElement.gmd:EX_GeographicBoundingBox'
            )
        }

        if (itemBoxesData && itemBoxesData.constructor !== Array) {
            boxesAndPins = [itemBoxesData]
        } else if (itemBoxesData && itemBoxesData.constructor === Array) {
            boxesAndPins = [itemBoxesData[0]]
        } else if (cswisogeoData && cswisogeoData.length) {
            for (const coord of cswisogeoData) {
                const W = UtilsService.getPropByString(
                    coord,
                    'gmd:westBoundLongitude.gco:Decimal'
                )
                const E = UtilsService.getPropByString(
                    coord,
                    'gmd:eastBoundLongitude.gco:Decimal'
                )
                const S = UtilsService.getPropByString(
                    coord,
                    'gmd:southBoundLatitude.gco:Decimal'
                )
                const N = UtilsService.getPropByString(
                    coord,
                    'gmd:northBoundLatitude.gco:Decimal'
                )

                if (
                    W &&
                    S &&
                    E &&
                    N &&
                    !boxesAndPins.find(
                        (box) =>
                            box.W === W &&
                            box.S === S &&
                            box.E === E &&
                            box.N === N
                    )
                ) {
                    boxesAndPins.push(
                        `${Number(S)} ${Number(W)} ${Number(N)} ${Number(E)}`
                    )
                }
            }
        } else {
            // If no dataset available return empty arrays
            return { boxes, pins }
        }

        for (const box of boxesAndPins) {
            if (typeof box === 'string' && box.trim()) {
                let coordinates = MapCoordinatesUtils.parseCoordinates(box)
                coordinates = MapCoordinatesUtils.normalizeLatitude(coordinates)

                if (
                    !MapCoordinatesUtils.isBoxPresentInArr(
                        coordinates,
                        boxes
                    ) &&
                    MapCoordinatesUtils.isBox(coordinates)
                ) {
                    boxes.push(coordinates)
                } else if (
                    !MapCoordinatesUtils.isBoxPresentInArr(coordinates, boxes)
                ) {
                    pins.push(coordinates)
                }
            }
        }

        return { boxes, pins }
    },

    getIrisCircles(item: any) {
        let circles: any = null
        const alternativeContent = UtilsService.getPropByString(
            item,
            'gmd:identificationInfo.gmd:MD_DataIdentification.gmd:descriptiveKeywords.gmd:MD_Keywords.gmd:keyword.gco:CharacterString'
        )
        if (item.content === 'QuakeML') {
            const coordinates = item.box
            const depth =
                0.5 * Number(item.verticalextent.minimum) +
                Number(item.verticalextent.maximum)
            const index = item.title.indexOf('earthquake of magnitude ') + 24
            const magnitude = parseFloat(item.title.slice(index, index + 3))
            if (coordinates.trim() && magnitude) {
                circles = new Object()
                circles.coordinates =
                    MapCoordinatesUtils.parseCoordinates(coordinates)
                circles.magnitude = magnitude
                circles.depth = depth
            }
        } else if (alternativeContent === 'QuakeML') {
            // DAB returns three extent tags. Choose one with spatial coordinates
            const extentsArray = UtilsService.getArrayByString(
                item,
                'gmd:identificationInfo.gmd:MD_DataIdentification.gmd:extent'
            )
            const spatialExtent = extentsArray.filter(
                (extent) => extent['gmd:EX_Extent']['gmd:geographicElement']
            )[0]

            const coordinatesArray = UtilsService.getPropByString(
                spatialExtent,
                'gmd:EX_Extent.gmd:geographicElement.gmd:EX_GeographicBoundingBox'
            )
            const W = UtilsService.getPropByString(
                coordinatesArray,
                'gmd:westBoundLongitude.gco:Decimal'
            )
            const E = UtilsService.getPropByString(
                coordinatesArray,
                'gmd:eastBoundLongitude.gco:Decimal'
            )
            const S = UtilsService.getPropByString(
                coordinatesArray,
                'gmd:southBoundLatitude.gco:Decimal'
            )
            const N = UtilsService.getPropByString(
                coordinatesArray,
                'gmd:northBoundLatitude.gco:Decimal'
            )
            const coordinates = `${Number(S)} ${Number(W)} ${Number(
                N
            )} ${Number(E)}`

            // Get depth from vertical extent
            const verticalExtent = extentsArray.filter(
                (extent) => extent['gmd:EX_Extent']['gmd:verticalElement']
            )[0]
            const minDepth = UtilsService.getPropByString(
                verticalExtent,
                'gmd:EX_Extent.gmd:verticalElement.gmd:EX_VerticalExtent.gmd:minimumValue.gco:Real'
            )
            const maxDepth = UtilsService.getPropByString(
                verticalExtent,
                'gmd:EX_Extent.gmd:verticalElement.gmd:EX_VerticalExtent.gmd:maximumValue.gco:Real'
            )
            const depth = 0.5 * Number(minDepth) + Number(maxDepth)

            // Get magnitude (only title contains this information)
            const title = UtilsService.getPropByString(
                item,
                'gmd:identificationInfo.gmd:MD_DataIdentification.gmd:citation.gmd:CI_Citation.gmd:title.gco:CharacterString'
            )
            const index = title.indexOf('earthquake of magnitude ') + 24
            const magnitude = parseFloat(title.slice(index, index + 3))

            if (coordinates.trim() && magnitude) {
                circles = new Object()
                circles.coordinates =
                    MapCoordinatesUtils.parseCoordinates(coordinates)
                circles.magnitude = magnitude
                circles.depth = depth
            }
        }

        return circles
    },

    getFeature(item: any, index: number) {
        const { boxes, pins } = ResultService.getBoundingBoxesAndPins(item)
        const footprint = item.acquisition ? item.acquisition.footprint : null
        const circles = ResultService.getIrisCircles(item)

        let layer = null
        let type = null
        if (footprint && footprint.length) {
            layer = LayersUtils.getPolygonLayerData(footprint, index)
            type = LayerTypes.BOUDNINGPOLYGON
        } else if (circles) {
            layer = LayersUtils.getIrisCircleLayerData(circles, index)
            type = LayerTypes.BOUNDINGCIRCLE
        } else if (boxes.length) {
            layer = LayersUtils.getBoxLayerData(boxes, index)
            type = LayerTypes.BOUNDINGBOX
        } else if (pins.length && item.content !== 'QuakeML') {
            layer = LayersUtils.getPinLayerData(pins, index)
            type = LayerTypes.BOUNDINGPIN
        }

        if (layer && type) {
            layer.boundingType = type
        }

        return layer
    },
}
