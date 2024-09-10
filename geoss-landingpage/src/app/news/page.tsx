import React, { Suspense } from "react";
import Title from "./Title";
import FindMore from "../components/FindMore";
import NewsGrid from "./NewsGrid";
import { NewsTileData, PaginationData } from "../model/types";
import { findMoreLinksNewsPage } from "../model/findMoreLinks";

async function getServerSideProps(searchParams: {
  [key: string]: string | string[] | undefined;
}) {
  const currentPage = Number(searchParams.page) || 1;
  const paginationData: PaginationData = {
    size: 9,
    totalElements: 51,
    totalPages: 6,
    currentPage: Number(searchParams.page) || 1,
  };
  let newsList: NewsTileData[] = [];
  //Placeholder for fetching featured news from contents
  switch (currentPage) {
    case 1:
      newsList = [
        {
          img: "/newsPlaceholder1.webp",
          date: "20.07.2023",
          text: "We will participate to the Open Earth Monitor Global workshop in Bolzano 4-6 October 2023",
          id: "testSlug",
        },
        {
          img: "/newsPlaceholder1.webp",
          date: "20.07.2023",
          text: "We will participate to the Open Earth Monitor Global workshop in Bolzano 4-6 October 2023",
          id: "testSlug",
        },
        {
          img: "/newsPlaceholder1.webp",
          date: "20.07.2023",
          text: "We will participate to the Open Earth Monitor Global workshop in Bolzano 4-6 October 2023",
          id: "testSlug",
        },
        {
          img: "/newsPlaceholder1.webp",
          date: "20.07.2023",
          text: "We will participate to the Open Earth Monitor Global workshop in Bolzano 4-6 October 2023",
          id: "testSlug",
        },
        {
          img: "/newsPlaceholder1.webp",
          date: "20.07.2023",
          text: "We will participate to the Open Earth Monitor Global workshop in Bolzano 4-6 October 2023",
          id: "testSlug",
        },
        {
          img: "/newsPlaceholder1.webp",
          date: "20.07.2023",
          text: "We will participate to the Open Earth Monitor Global workshop in Bolzano 4-6 October 2023",
          id: "testSlug",
        },
        {
          img: "/newsPlaceholder1.webp",
          date: "20.07.2023",
          text: "We will participate to the Open Earth Monitor Global workshop in Bolzano 4-6 October 2023",
          id: "testSlug",
        },
        {
          img: "/newsPlaceholder1.webp",
          date: "20.07.2023",
          text: "We will participate to the Open Earth Monitor Global workshop in Bolzano 4-6 October 2023",
          id: "testSlug",
        },
        {
          img: "/newsPlaceholder1.webp",
          date: "20.07.2023",
          text: "We will participate to the Open Earth Monitor Global workshop in Bolzano 4-6 October 2023",
          id: "testSlug",
        },
      ];
      break;
    case 2:
      newsList = [
        {
          img: "/title.webp",
          date: "20.07.2023",
          text: "We will participate",
          id: "testSlug",
        },
        {
          img: "/title.webp",
          date: "20.07.2023",
          text: "We will participate",
          id: "testSlug",
        },
        {
          img: "/title.webp",
          date: "20.07.2023",
          text: "We will participate",
          id: "testSlug",
        },
        {
          img: "/title.webp",
          date: "20.07.2023",
          text: "We will participate",
          id: "testSlug",
        },
        {
          img: "/title.webp",
          date: "20.07.2023",
          text: "We will participate",
          id: "testSlug",
        },
        {
          img: "/title.webp",
          date: "20.07.2023",
          text: "We will participate",
          id: "testSlug",
        },
        {
          img: "/title.webp",
          date: "20.07.2023",
          text: "We will participate",
          id: "testSlug",
        },
        {
          img: "/title.webp",
          date: "20.07.2023",
          text: "We will participate",
          id: "testSlug",
        },
        {
          img: "/title.webp",
          date: "20.07.2023",
          text: "We will participate",
          id: "testSlug",
        },
      ];
      break;
    case 3:
      newsList = [
        {
          img: "/title.webp",
          date: "20.07.2023",
          text: "We will participate",
          id: "testSlug",
        },
        {
          img: "/title.webp",
          date: "20.07.2023",
          text: "We will participate",
          id: "testSlug",
        },
        {
          img: "/title.webp",
          date: "20.07.2023",
          text: "We will participate",
          id: "testSlug",
        },
        {
          img: "/title.webp",
          date: "20.07.2023",
          text: "We will participate",
          id: "testSlug",
        },
        {
          img: "/title.webp",
          date: "20.07.2023",
          text: "We will participate",
          id: "testSlug",
        },
        {
          img: "/title.webp",
          date: "20.07.2023",
          text: "We will participate",
          id: "testSlug",
        },
        {
          img: "/title.webp",
          date: "20.07.2023",
          text: "We will participate",
          id: "testSlug",
        },
        {
          img: "/title.webp",
          date: "20.07.2023",
          text: "We will participate",
          id: "testSlug",
        },
        {
          img: "/title.webp",
          date: "20.07.2023",
          text: "We will participate",
          id: "testSlug",
        },
      ];
      break;
    case 4:
      newsList = [
        {
          img: "/title.webp",
          date: "20.07.2023",
          text: "We will participate",
          id: "testSlug",
        },
        {
          img: "/title.webp",
          date: "20.07.2023",
          text: "We will participate",
          id: "testSlug",
        },
        {
          img: "/title.webp",
          date: "20.07.2023",
          text: "We will participate",
          id: "testSlug",
        },
        {
          img: "/title.webp",
          date: "20.07.2023",
          text: "We will participate",
          id: "testSlug",
        },
        {
          img: "/title.webp",
          date: "20.07.2023",
          text: "We will participate",
          id: "testSlug",
        },
        {
          img: "/title.webp",
          date: "20.07.2023",
          text: "We will participate",
          id: "testSlug",
        },
        {
          img: "/title.webp",
          date: "20.07.2023",
          text: "We will participate",
          id: "testSlug",
        },
        {
          img: "/title.webp",
          date: "20.07.2023",
          text: "We will participate",
          id: "testSlug",
        },
        {
          img: "/title.webp",
          date: "20.07.2023",
          text: "We will participate",
          id: "testSlug",
        },
      ];
      break;
    case 5:
      newsList = [
        {
          img: "/title.webp",
          date: "20.07.2023",
          text: "We will participate",
          id: "testSlug",
        },
        {
          img: "/title.webp",
          date: "20.07.2023",
          text: "We will participate",
          id: "testSlug",
        },
        {
          img: "/title.webp",
          date: "20.07.2023",
          text: "We will participate",
          id: "testSlug",
        },
        {
          img: "/title.webp",
          date: "20.07.2023",
          text: "We will participate",
          id: "testSlug",
        },
        {
          img: "/title.webp",
          date: "20.07.2023",
          text: "We will participate",
          id: "testSlug",
        },
        {
          img: "/title.webp",
          date: "20.07.2023",
          text: "We will participate",
          id: "testSlug",
        },
        {
          img: "/title.webp",
          date: "20.07.2023",
          text: "We will participate",
          id: "testSlug",
        },
        {
          img: "/title.webp",
          date: "20.07.2023",
          text: "We will participate",
          id: "testSlug",
        },
        {
          img: "/title.webp",
          date: "20.07.2023",
          text: "We will participate",
          id: "testSlug",
        },
      ];
      break;
    case 6:
      newsList = [
        {
          img: "/newsPlaceholder3.webp",
          date: "20.07.2023",
          text: "We will participate to the Open Earth Monitor Global workshop in Bolzano 4-6 October 2023",
          id: "testSlug",
        },
        {
          img: "/newsPlaceholder3.webp",
          date: "20.07.2023",
          text: "We will participate to the Open Earth Monitor Global workshop in Bolzano 4-6 October 2023",
          id: "testSlug",
        },
        {
          img: "/newsPlaceholder3.webp",
          date: "20.07.2023",
          text: "We will participate to the Open Earth Monitor Global workshop in Bolzano 4-6 October 2023",
          id: "testSlug",
        },
        {
          img: "/newsPlaceholder3.webp",
          date: "20.07.2023",
          text: "We will participate to the Open Earth Monitor Global workshop in Bolzano 4-6 October 2023",
          id: "testSlug",
        },
        {
          img: "/newsPlaceholder3.webp",
          date: "20.07.2023",
          text: "We will participate to the Open Earth Monitor Global workshop in Bolzano 4-6 October 2023",
          id: "testSlug",
        },
      ];
      break;
    default:
      newsList = [];
  }
  return { paginationData, newsList };
}

const page = async ({
  searchParams,
}: {
  searchParams: { [key: string]: string | string[] | undefined };
}) => {
  try {
    const newsData = await getServerSideProps(searchParams);
    return (
      <div className="w-full text-black">
        <Title />
        <Suspense fallback={<>Loading...</>}>
          <NewsGrid
            newsList={newsData.newsList}
            paginationData={newsData.paginationData}
          />
        </Suspense>
        <FindMore links={findMoreLinksNewsPage} />
      </div>
    );
  } catch (e: any) {
    return <div>Error fetching data: {JSON.stringify(e)}</div>;
  }
};

export default page;
