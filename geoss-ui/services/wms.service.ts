import { makeRequest } from '@/services/general.api.service';

export function getWmsLayerInfo(url: string) {
	return makeRequest('get', url, null, true)
		.then(data => {
			if (!data || data.status === 500) {
				return null;
			}
			return data;
		})
		.catch(error => Promise.resolve(error));
}
