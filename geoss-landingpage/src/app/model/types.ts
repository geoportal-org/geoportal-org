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

export type ProviderData = {
    name: string;
    id: string;
    description: string;
    title: string;
    approval_status: string;
    state: string;
    image_url: string;
    extras: any[];
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
