import { NewsPage, PaginationData } from "../../model/types";
import { fetchSettings } from "../settingsApi/route";

const apiUrl = process.env.NEXT_PUBLIC_API_URL;

export const getNewsPages = async (currentPage = 0, size = 9) => {
    const { siteId } = await fetchSettings();
    if (siteId === undefined) {
        return { news: null, paginationData: { size: 0, totalElements: 0, totalPages: 0, number: 0 } };
    }
    const response = await fetch(
        `${apiUrl}contents/rest/page/search/findBySiteId?siteId=${siteId}&page=${currentPage}&size=${size}`
    );
    const resJson = await response.json();
    const news = resJson._embedded.page;
    const paginationData: PaginationData = resJson.page;
    return { news, paginationData };
};

export const getNewsPagesWithImages = async (currentPage = 0, size = 9) => {
    const { siteId } = await fetchSettings();
    if (siteId === undefined) {
        return { news: null, paginationData: { size: 0, totalElements: 0, totalPages: 0, number: 0 } };
    }
    const response = await fetch(
        `${apiUrl}contents/rest/page/search/findBySiteId?siteId=${siteId}&page=${currentPage}&size=${size}`
    );
    const resJson = await response.json();
    const news = resJson._embedded.page;
    const newsWithImages =
        (await Promise.all(
            news.map(async (n: any) => {
                const res = await fetch(`${apiUrl}contents/rest/content/${n.contentId}`);
                const resJson = await res.json();
                return { ...n, imageUrl: constructImageUrl(resJson.imageUrl) };
            })
        )) || [];
    const paginationData: PaginationData = resJson.page;
    return { newsWithImages, paginationData };
};

export const getNewsPageBySlug = async (slug: string) => {
    const { news } = await getNewsPages(0, 9999);
    const page: NewsPage = news.filter((page: { slug: string }) => page.slug === slug)[0];
    return page;
};

export const getNewsPageContent = async (slug: string) => {
    const page = await getNewsPageBySlug(slug);
    const content = await fetch(`${apiUrl}contents/rest/content/${page.contentId}`);
    const contentJson = await content.json();
    const imageUrl = constructImageUrl(contentJson.imageUrl);
    return { title: contentJson.title.en, data: contentJson.data.en, date: contentJson.modifiedOn, imageUrl: imageUrl };
};

const constructImageUrl = (id: string) => {
    if (id && id !== "") {
        return `${apiUrl}/contents/rest/document/${id}/content`;
    } else {
        return "/newsPlaceholder1.webp";
    }
};
