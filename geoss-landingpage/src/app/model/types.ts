export type LinkData = {
    text: string;
    href: string;
};

export type Feature = {
    icon: any;
    text: string;
    href: string;
};

export type NewsTileData = {
    img: string;
    title?: string;
    date: string;
    text: string;
    id: string;
};

export type PaginationData = {
    size: number;
    totalElements: number;
    totalPages: number;
    number: number;
};

export type ProviderElementType = {
    formId: number;
    id: number;
    name: string;
    data: ProviderData;
    version: number;
    createDate: string;
    _links: any;
};

export type ProviderData = {
    providerName: string;
    acronym: string;
    shortDescription: string;
    websiteInstitutionURL: string;
    organizationGeographicalCoverage: string;
    countriesOrRegionsNames: string;
    geoCoverageOther: string;
    geoAffiliation: string;
    officialFocalPointName: string;
    officialFocalPointEmail: string;
    technicalFocalPointName: string;
    technicalFocalPointEmail: string;
    onlineResourceType: string;
    knowledgeBodyType: string;
    dataAccessibility: string;
    dataPolicy: string;
    dataCore: string;
    dataCoreOther: string;
    dataManagementPrinciplesLabel: string;
    relevantSBA: string;
    relevantSDG: string;
    otherInitiative: string;
    serviceEndpoint: string;
    organizationLogoURL: string;
    terms: string;
};

export type ProviderElementProps = {
    name: string;
    date: string;
    url: string;
    description: string;
    image_url: string;
    principlesStr: string;
};

export type NewsPage = {
    title: string | any;
    description: string;
    contentId: number;
    slug: string;
    published: boolean;
    createdBy: string;
    createdDate: string;
    modifiedBy: string;
    modifiedDate: string;
    _links: {
        page: {
            href: string;
        };
        self: {
            href: string;
        };
    };
};
