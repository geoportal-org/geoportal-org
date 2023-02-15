export type DataSource = 'dab' | 'data' | 'amerigeoss' | 'nextgeoss' | 'information' | 'zenodo' | 'wikipedia' | 'services';

export enum DataSources {
	DAB = 'dab',
	DATA = 'data',
	AMERIGEOSS = 'amerigeoss',
	NEXTGEOSS = 'nextgeoss',
	INFORMATION = 'information',
	ZENODO = 'zenodo',
	WIKIPEDIA = 'wikipedia',
	SERVICES = 'services'
}

export enum DataSourcesMask {
	dab = DataSources.DAB,
	cr_data = DataSources.DATA,
	amerigeoss_ckan = DataSources.AMERIGEOSS,
	nextgeoss = DataSources.NEXTGEOSS,
	cr_information = DataSources.INFORMATION,
	zenodo = DataSources.ZENODO,
	wikipedia = DataSources.WIKIPEDIA,
	cr_services = DataSources.SERVICES
}

export const DataPromiseId = {
	[DataSources.DAB]: 'dab',
	[DataSources.DATA]: 'cr_data',
	[DataSources.AMERIGEOSS]: 'amerigeoss_ckan',
	[DataSources.NEXTGEOSS]: 'nextgeoss',
	[DataSources.INFORMATION]: 'cr_information',
	[DataSources.ZENODO]: 'zenodo',
	[DataSources.WIKIPEDIA]: 'wikipedia',
	[DataSources.SERVICES]: 'cr_services'
};

export const DataSourceGroup = {
	[DataSources.DAB]: 'data',
	[DataSources.DATA]: 'data',
	[DataSources.AMERIGEOSS]: 'data',
	[DataSources.NEXTGEOSS]: 'data',
	[DataSources.INFORMATION]: 'information',
	[DataSources.ZENODO]: 'information',
	[DataSources.WIKIPEDIA]: 'information',
	[DataSources.SERVICES]: 'services'
};

// Corresponds to ds parameter
export const DataOrigin = {
	[DataSources.DAB]: 'dab',
	[DataSources.DATA]: 'geoss_cr',
	[DataSources.AMERIGEOSS]: 'amerigeoss_ckan',
	[DataSources.NEXTGEOSS]: 'nextgeoss',
	[DataSources.INFORMATION]: 'geoss_cr',
	[DataSources.ZENODO]: 'zenodo',
	[DataSources.WIKIPEDIA]: 'wikipedia',
	[DataSources.SERVICES]: 'geoss_cr'
};

export const DataOriginMask = {
	dab: 'GEOSS',
	geoss_cr: 'GEOSS Curated',
	amerigeoss_ckan: 'AmeriGEO',
	nextgeoss: 'NextGEOSS',
	zenodo: 'Zenodo',
	wikipedia: 'Wikipedia'
};

export const AlternateSourcesMap = {
	[DataSources.DAB]: `GEOSS`,
	[DataSources.DATA]: `GEOSS Curated`,
	[DataSources.AMERIGEOSS]: `AmeriGEO`,
	[DataSources.NEXTGEOSS]: `NextGEOSS`,
	[DataSources.INFORMATION]: `GEOSS Curated`,
	[DataSources.ZENODO]: `Zenodo`,
	[DataSources.WIKIPEDIA]: `Wikipedia`,
	[DataSources.SERVICES]: `GEOSS Curated`
};