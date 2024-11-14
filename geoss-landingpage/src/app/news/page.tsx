import React, { Suspense } from "react";
import Title from "./Title";
import FindMore from "../components/FindMore";
import NewsGrid from "./NewsGrid";
import { NewsTileData } from "../model/types";
import { findMoreLinksNewsPage } from "../model/findMoreLinks";
import { getNewsPagesWithImages } from "../api/newsApi";
import SchemaHeader from "../components/SchemaHeader/SchemaHeader";
import { Metadata } from "next";

export const metadata: Metadata = {
    title: "GEOSS News",
    description: "GEOSS News page",
};

async function getServerSideProps(searchParams: { [key: string]: string | string[] | undefined }) {
    const currentPage = Number(searchParams.page) && Number(searchParams.page) > 0 ? Number(searchParams.page) - 1 : 0;
    const newsList: NewsTileData[] = [];
    const { newsWithImages, paginationData } = await getNewsPagesWithImages(currentPage);
    if (newsWithImages) {
        newsWithImages.forEach((n: any) => {
            newsList.push({
                img: n.imageUrl,
                title: n.title.en,
                date: n.modifiedOn,
                text: n.description.en,
                id: n.slug,
            });
        });
    }
    return { paginationData, newsList };
}

const page = async ({ searchParams }: { searchParams: { [key: string]: string | string[] | undefined } }) => {
    try {
        const newsData = await getServerSideProps(searchParams);
        if (newsData.paginationData.totalElements === 0) {
            return <div className="w-full min-h-[70vh] text-black text-center align-middle">No news found</div>;
        }
        return (
            <div className="w-full text-black">
                <SchemaHeader type="articles-list" data={newsData.newsList} />
                <Title />
                <Suspense fallback={<>Loading...</>}>
                    <NewsGrid newsList={newsData.newsList} paginationData={newsData.paginationData} />
                </Suspense>
                <FindMore links={findMoreLinksNewsPage} />
            </div>
        );
    } catch (e: any) {
        console.log(e);
        return <div>Error fetching data: {JSON.stringify(e)}</div>;
    }
};

export default page;
