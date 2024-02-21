export interface ResourcesData {
    totalPages: 0;
    totalElements: 0;
    size: 0;
    content: ResourceContentData[];
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

export interface ResourceContentData {
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