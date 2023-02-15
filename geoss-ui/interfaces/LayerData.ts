import { MapCoordinate } from '@/interfaces/MapCoordinate'
import Layer from 'ol/layer/Layer'

export interface LayerData {
    id: string
    index?: number
    title?: string
    visible: boolean
    transparency: number
    value: Layer
    type: string
    coordinate?: MapCoordinate
    image?: string
    legend?: {
        type: string
        data: {
            min: number
            max: number
            value: number
            unit: string
        }
    }
}
