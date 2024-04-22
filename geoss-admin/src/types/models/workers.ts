export interface WorkerData {
    type: string;
    status: string;
    startTime: string;
    createTime: string;
    endTime: string;
    lastUpdated: string;
    cause: {
        cause: string;
        message: string;
        suppressed: [string];
    };
}

export enum WorkerType {
    ESA = "esa",
    EOSTERM = "eosterm",
    EARTH = "earth",
}
