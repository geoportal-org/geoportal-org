import { DataSource } from './DataSources';

export interface ParentRef {
	dataSource: DataSource;
	id: string;
	entry?: any;
}