import { TaskType } from "./userResources";

//userExtension
export interface UserExtensionData {
    totalPages: 0;
    totalElements: 0;
    size: 0;
    content: UserExtensionContent[];
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

export interface UserExtensionContent {
    id: 0;
    userId: string;
    entryName: string;
    description: string;
    taskType: TaskType;
    entryExtension: EntryExtension;
    status: string;
    createDate: string;
    modifiedDate: string;
    hasOtherExtensionsWithSameEntry?: boolean;
}

export interface EntryExtension {
    id: 0;
    code: string;
    dataSource: {
        id: 0;
        name: string;
        code: string;
        isCustom: 0;
    };
    title: string;
    summary: string;
    keywords: string;
    tags: string;
    comment: string;
    workflowInstanceId: 0;
    userId: 0;
    username: string;
    email: string;
    deleted: 0;
    createDate: string;
    modifiedDate: string;
}

export interface CreateUserExtension {
    userId: string;
    entryName: string;
    description: string;
    taskType: TaskType;
    entryExtension: {
        code: string;
        dataSource: 'geoss_cr';
        title: string;
        summary: string;
        keywords: string;
        tags: string;
        userId: string;
        username: string;
        transferOptionExtensions: [
            {
                name: string;
                description: string;
                displayTitle: string;
                protocol: {
                    value: string;
                };
                endpoint: {
                    url: string;
                    urlType: string;
                };
            }
        ];
    };
}

//extension
export interface ExtensionData {
    totalPages: 0;
    totalElements: 0;
    size: 0;
    content: ExtensionContent[];
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

export interface ExtensionContent {
    id: 0;
    code: string;
    dataSource: {
        id: 0;
        name: string;
        code: string;
        isCustom: 0;
    };
    title: string;
    summary: string;
    keywords: string;
    tags: string;
    comment: string;
    workflowInstanceId: 0;
    userId: 0;
    username: string;
    email: string;
    deleted: 0;
    createDate: string;
    modifiedDate: string;
}

//transferOption

export interface ExtensionTransferOption {
    id: 0;
    name: string;
    description: string;
    displayTitle: string;
    entryExtension: {
        id: number;
        code: string;
        dataSource: {
            id: number;
            name: string;
            code: string;
            isCustom: 0;
        };
        title: string;
        summary: string;
        keywords: string;
        tags: string;
        comment: string;
        workflowInstanceId: 0;
        userId: number;
        username: string;
        email: string;
        deleted: 0;
        createDate: string;
        modifiedDate: string;
    };
    endpoint: {
        id: 0;
        url: string;
        urlType: string;
        isCustom: number;
    };
    protocol: {
        id: 0;
        value: string;
        isCustom: number;
    };
    deleted: 0;
    createDate: string;
    modifiedDate: string;
}
