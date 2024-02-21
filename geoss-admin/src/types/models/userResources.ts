export interface UserResourcesData {
    totalPages: 0;
    totalElements: 0;
    size: 0;
    content: UserResourcesContentData[];
    number: 0;
    sort: {
        empty: true;
        unsorted: true;
        sorted: true;
    };
    first: true;
    last: true;
    numberOfElements: 0;
    pageable: {
        offset: 0;
        sort: {
            empty: true;
            unsorted: true;
            sorted: true;
        };
        pageNumber: 0;
        pageSize: 0;
        paged: true;
        unpaged: true;
    };
    empty: true;
}

export interface UserResourcesContentData {
    id: 0;
    userId: string;
    entryName: string;
    taskType: string;
    entry: ResourceEntry;
    status: string;
    createDate: string;
    modifiedDate: string;
    hasOtherResourcesWithSameEntry: boolean;
}

export interface EntriesData {
    totalPages: 0;
    totalElements: 0;
    size: 0;
    content: ResourceEntry[];
    number: 0;
    sort: {
        empty: true;
        unsorted: true;
        sorted: true;
    };
    first: true;
    last: true;
    numberOfElements: 0;
    pageable: {
        offset: 0;
        sort: {
            empty: true;
            unsorted: true;
            sorted: true;
        };
        pageNumber: 0;
        pageSize: 0;
        paged: true;
        unpaged: true;
    };
    empty: true;
}

export interface ResourceEntry {
    id: 0;
    title: string;
    summary: string;
    logo: string;
    coverage: string;
    type: {
        id: 0;
        name: string;
        code: string;
        isCustom: number;
    };
    dashboardContents: {
        id: 0;
        content: string;
    };
    accessPolicy: {
        id: 0;
        code: string;
        name: string;
        isCustom: number;
    };
    keywords: string;
    tags: string;
    code: string;
    scoreWeight: 0;
    organisation: {
        id: 0;
        title: string;
        contact: string;
        email: string;
        contactEmail: string;
        isCustom: number;
    };
    publishDate: string;
    source: {
        id: 0;
        term: string;
        code: string;
        isCustom: number;
    };
    dataSource: {
        id: 0;
        name: string;
        code: string;
        isCustom: number;
    };
    displayDataSource: {
        id: 0;
        name: string;
        code: string;
        isCustom: number;
    };
    workflowInstanceId: 0;
    definitionType: {
        id: 0;
        code: 0;
        name: string;
    };
    deleted: 0;
    createDate: string;
    modifiedDate: string;
}

export interface CreateUserResource {
    userId: string;
    entryName: string;
    taskType: TaskType;
    entry: {
        id? : number;
        title: string;
        summary: string;
        logo: string;
        coverage: string;
        type: string;
        dashboardContents: {
            content: string;
        };
        accessPolicy: {
            name: string;
            code: string;
        };
        keywords: string;
        tags: string;
        code: string;
        organisation: {
            title: string;
            email: string;
            contact: string;
            contactEmail: string;
        };
        source: {
            term: string;
            code: string;
        };
        dataSource: string;
        displayDataSource: string;
        definitionType: string;
        userId: string;
        transferOptions: LinkType[];
    };
}

export interface TransferOption {
    id: 0;
    name: string;
    description: string;
    title: string;
    protocol: {
        id: number;
        value: string;
        isCustom: number;
    };
    endpoint: {
        id: number;
        url: string;
        urlType: string;
        isCustom: number;
    };
    entry: ResourceEntry;
    deleted: number;
    createDate: string;
    modifiedDate: string;
}

export interface LinkType {
    name: string;
    description: string;
    title: string;
    protocol: {
        value: string;
    };
    endpoint: {
        url: string;
        urlType: string;
    };
}

export enum TaskType {
    CREATE = "create",
    UPDATE = "update",
    REMOVE = "remove",
}
