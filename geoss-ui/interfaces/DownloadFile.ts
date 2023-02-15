export enum DownloadFileStatus {
	inProgress = 'In progress',
	ready = 'Ready',
	failed = 'Failed'
}

export interface DownloadFile {
	format: string;
	download: string;
	type: string;
	status: DownloadFileStatus;
	statusObject: any;
	checkStatusUrl: string;
	checkStatusPayload?: string;
	url: string;
	id: number;
	name: string;
	progressData: {
		timeout: any,
		promise: Promise<any>
	};
}