import { AppVueObj } from '@/data/global'

const MapCoordinatesUtils = {
    parseCoordinates(coordinates: string | string[]) {
        if (!coordinates || coordinates === ',,,') {
            // empty coordinates
            return []
        } else if (typeof coordinates === 'string') {
            coordinates = coordinates.trim()
            if (coordinates.indexOf(',') !== -1) {
                // parse from GEOSS format
                // 'W,S,E,N'
                const coordinatesSplit = coordinates.split(',')
                const W = Number(coordinatesSplit[0])
                const S = Number(coordinatesSplit[1])
                const E = Number(coordinatesSplit[2])
                const N = Number(coordinatesSplit[3])

                return [W, S, E, N]
            } else if (coordinates.indexOf(' ') !== -1) {
                // parse from DAB format
                // 'S W N E'
                const coordinatesSplit = coordinates.split(' ')
                const W = Number(coordinatesSplit[1])
                const S = Number(coordinatesSplit[0])
                const E = Number(coordinatesSplit[3])
                const N = Number(coordinatesSplit[2])

                return [W, S, E, N]
            }
        } else if (coordinates.constructor === Array) {
            if (coordinates[0].constructor === Array) {
                // parse from Open Layers feature geometry
                // [[[lon,lat],[lon,lat],[lon,lat],[lon,lat]]]
                const converted = [
                    AppVueObj.ol.proj.transform(
                        coordinates[0][0],
                        'EPSG:3857',
                        'EPSG:4326'
                    ),

                    AppVueObj.ol.proj.transform(
                        coordinates[0][1],
                        'EPSG:3857',
                        'EPSG:4326'
                    ),

                    AppVueObj.ol.proj.transform(
                        coordinates[0][2],
                        'EPSG:3857',
                        'EPSG:4326'
                    ),

                    AppVueObj.ol.proj.transform(
                        coordinates[0][3],
                        'EPSG:3857',
                        'EPSG:4326'
                    )
                ]

                const W = Math.min(converted[0][0], converted[2][0])
                const E = Math.max(converted[0][0], converted[2][0])
                const N = Math.max(converted[0][1], converted[2][1])
                const S = Math.min(converted[0][1], converted[2][1])

                return [W, S, E, N]
            } else {
                // coordinates are in correct format, make sure array contains numbers
                // [W, S, E, N]
                const W = Number(coordinates[0])
                const S = Number(coordinates[1])
                const E = Number(coordinates[2])
                const N = Number(coordinates[3])

                return [W, S, E, N]
            }
        }
        // unknown format
        return []
    },

    normalizeLatitude(coordinates: number[]) {
        const W = coordinates[0]
        let S = coordinates[1]
        const E = coordinates[2]
        let N = coordinates[3]
        if (N > 90) {
            N = 90
        }
        if (S < -90) {
            S = -90
        }
        return [W, S, E, N]
    },

    normalizeLongitude(W: number, E: number) {
        if (Math.abs(E - W) >= 360) {
            W = -180
            E = 180
        } else {
            if (W > 180) {
                W = W - 360 * Math.ceil((W - 180) / 360)
                E = E - 360 * Math.ceil((E - 180) / 360)
            }
            if (E > 180) {
                E = E - 360 * Math.ceil((E - 180) / 360)
            }
            if (W < -180) {
                W = W + 360 * -Math.floor((W + 180) / 360)
                E = E + 360 * -Math.floor((E + 180) / 360)
            }
            if (E < -180) {
                E = E + 360 * -Math.floor((E + 180) / 360)
            }
        }
        return [W, E]
    },

    denormalizeLongitude(W: number, E: number) {
        if (W <= E) {
            // longitude not wrapped
            return [W, E]
        }
        if (Math.abs(180 - W) > Math.abs(-180 - E)) {
            // bigger part on the eastern hemisphere
            return [W, E + 360]
        } else {
            // bigger part on the western hemisphere
            return [W - 360, E]
        }
    },

    coordinatesForDrawing(coordinates: number[] | string[]): any {
        const W = coordinates[0]
        const S = coordinates[1]
        const E = coordinates[2]
        const N = coordinates[3]
        return [
            [
                [E, N],
                [E, S],
                [W, S],
                [W, N]
            ]
        ]
    },

    // converts [W, S, E, N] coordinates to format used for bounding box in metadata
    coordinatesForPresentation(coordinates: number[] | string[]) {
        const W = coordinates[0]
        const S = coordinates[1]
        const E = coordinates[2]
        const N = coordinates[3]
        return '(' + N + '°, ' + W + '°, ' + S + '°, ' + E + '°)'
    },

    isBoxPresentInArr(box: any, boxes: any) {
        for (const item of boxes) {
            if (
                item[0] === box[0] &&
                item[1] === box[1] &&
                item[2] === box[2] &&
                item[3] === box[3]
            ) {
                return true
            }
        }

        return false
    },

    // sample: POLYGON ((27.5299 34.9537,27.1384 36.9095,24.4005 36.6398,24.8597
    // 34.6849,27.5299 34.9537,27.5299 34.9537))
    // <entry><acquisition xmlns='http://eu.flora-research'><footprint>
    parsePolygon(polygonString: string) {
        const start = polygonString.indexOf('(((')
        const end = polygonString.indexOf(')))')
        const polygonStringArray: string[] = polygonString
            .slice(start + 3, end)
            .split(',')
        const polygonTab: any = []
        polygonStringArray.forEach((value) => {
            const values = value.trim().split(' ')
            polygonTab.push([
                parseFloat(values[0]),
                parseFloat(values[values.length - 1])
            ])
        })

        return polygonTab
    },

    // Handle footprints crossing International Date Line
    // Method: Minimal longitudinal spread criterion
    dateLineFix(polygonTab: any) {
        for (let i = 1; i < polygonTab.length; i++) {
            const x1 = polygonTab[i - 1][0]
            const x2 = polygonTab[i][0]
            if (Math.abs(x2 - x1) > 360 - Math.abs(x2 - x1)) {
                if (x2 > x1) {
                    polygonTab[i][0] = x2 - 360
                } else {
                    polygonTab[i][0] = x2 + 360
                }
            }
        }
        return polygonTab
    },

    // checks if coordinates correspond to a point on a map
    isPoint(W: number, S: number, E: number, N: number) {
        const NS = N - S
        const EW = E - W

        return NS <= 0.01 && EW <= 0.01
    },

    // if two normalized boxes overlaps longiude
    // box order matters, calculation is only one way, box1 is left
    boxesOverlap(box1: number[], box2: number[]) {
        const W1 = box1[0]
        const E1 = box1[2]
        const W2 = box2[0]
        const E2 = box2[2]

        if (Math.abs(E1 - W1) >= 360) {
            return true
        } else if (Math.abs(E2 - W2) >= 360) {
            return true
        } else if (W1 <= E1 && W2 >= W1 && W2 <= E1) {
            return true
        } else if (W1 > E1 && (W2 >= W1 || W2 <= E1)) {
            return true
        } else {
            return false
        }
    },

    // megre two overlaping in longitude boxes
    // box order matters, calculation is only one way, box1 is left
    mergeOverlapingBoxes(box1: number[], box2: number[]) {
        const W1 = box1[0]
        const E1 = box1[2]
        const W2 = box2[0]
        const E2 = box2[2]

        const S = Math.min(box1[1], box2[1])
        const N = Math.max(box1[3], box2[3])
        let W = null
        let E = null

        if (Math.abs(E1 - W1) >= 360) {
            W = -180
            E = 180
        } else if (Math.abs(E2 - W2) >= 360) {
            W = -180
            E = 180
        } else if (W1 <= E1 && W2 >= W1 && W2 <= E1) {
            if (W2 <= E2) {
                W = W1
                E = Math.max(E1, E2)
            } else if (E2 < W1) {
                W = W1
                E = E2
            } else {
                W = -180
                E = 180
            }
        } else if (W1 > E1 && (W2 >= W1 || W2 <= E1)) {
            if (W2 <= E2 && W2 >= W1) {
                W = W1
                E = E1
            } else if (E2 < W1) {
                W = W1
                E = Math.max(E1, E2)
            } else {
                W = -180
                E = 180
            }
        }
        return [W, S, E, N]
    },

    // calculates box distance
    // box order matters, calculation is only one way, box1 is left
    calcBoxDistance(box1: number[], box2: number[]) {
        const E1 = box1[2]
        const W2 = box2[0]

        return W2 - E1
    },

    // merges non overlapping boxes
    // box order matters, calculation is only one way, box1 is left
    mergeNonOverlapingBoxes(box1: number[], box2: number[]) {
        const S = Math.min(box1[1], box2[1])
        const N = Math.max(box1[3], box2[3])
        const W = box1[0]
        const E = box2[2]

        return [W, S, E, N]
    },

    // checks if coordinates cover whole or almost whole world,
    // thresholds were chosen based on DAB responses and cannot be higher
    isWholeWorld(W: number, S: number, E: number, N: number) {
        if (N >= 80 && E >= 179 && S <= -80 && W <= -179) {
            return true
        } else {
            return false
        }
    },

    // Return one [W, S, E, N] normalized box containing all boxes.
    mergeBoxes(boxes: number[][]) {
        if (boxes.length === 1) {
            return boxes[0]
        }

        const normalized = []
        for (const box of boxes) {
            const normalizedLongitude = MapCoordinatesUtils.normalizeLongitude(
                box[0],
                box[2]
            )
            const normalizedW = normalizedLongitude[0]
            const normalizedE = normalizedLongitude[1]
            normalized.push([normalizedW, box[1], normalizedE, box[3]])
        }
        boxes = normalized

        let foundOverlapping
        do {
            foundOverlapping = false
            for (let i = 0; i < boxes.length; i++) {
                for (let j = boxes.length - 1; j > i; j--) {
                    if (MapCoordinatesUtils.boxesOverlap(boxes[i], boxes[j])) {
                        boxes[i] = MapCoordinatesUtils.mergeOverlapingBoxes(
                            boxes[i],
                            boxes[j]
                        ) as number[]
                        boxes.splice(j, 1)
                        foundOverlapping = true
                    } else if (
                        MapCoordinatesUtils.boxesOverlap(boxes[j], boxes[i])
                    ) {
                        boxes[i] = MapCoordinatesUtils.mergeOverlapingBoxes(
                            boxes[j],
                            boxes[i]
                        ) as number[]
                        boxes.splice(j, 1)
                        foundOverlapping = true
                    }
                }
            }
        } while (foundOverlapping)

        let closestDistance
        let closest1: number = 0
        let closest2: number = 0
        while (boxes.length > 1) {
            closestDistance = 360
            for (let i = 0; i < boxes.length; i++) {
                for (let j = boxes.length - 1; j > i; j--) {
                    let distance = MapCoordinatesUtils.calcBoxDistance(
                        boxes[i],
                        boxes[j]
                    )
                    if (distance <= closestDistance) {
                        closestDistance = distance
                        closest1 = i
                        closest2 = j
                    }
                    distance = MapCoordinatesUtils.calcBoxDistance(
                        boxes[j],
                        boxes[i]
                    )
                    if (distance <= closestDistance) {
                        closestDistance = distance
                        closest1 = j
                        closest2 = i
                    }
                }
            }

            boxes[closest1] = MapCoordinatesUtils.mergeNonOverlapingBoxes(
                boxes[closest1],
                boxes[closest2]
            )
            boxes.splice(closest2, 1)
        }

        return boxes[0]
    },

    isBox(coordinates: number[]) {
        const W = coordinates[0]
        const S = coordinates[1]
        const E = coordinates[2]
        const N = coordinates[3]
        // in case of mixed ractangles with icons treat all items as rectangles
        return !MapCoordinatesUtils.isPoint(W, S, E, N)
    }
}

export default MapCoordinatesUtils
