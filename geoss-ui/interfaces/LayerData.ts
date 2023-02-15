import ol from '@/ol';

import { MapCoordinate } from '@/interfaces/MapCoordinate';

export interface LayerData {
	id: string;
	index?: number;
	title?: string;
	visible: boolean;
	transparency: number;
	value: ol.layer.Layer;
	type: string;
	coordinate?: MapCoordinate;
	image?: string;
	legend?: {
		type: string,
		data: {
			min: number,
			max: number,
			value: number,
			unit: string
		}
	};
}