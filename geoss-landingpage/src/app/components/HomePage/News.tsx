import { NewsTileData } from "@/src/app/model/types";
import Link from "next/link";
import React from "react";
import NewsTile from "../NewsTile";
import { v4 as uuidv4 } from "uuid";

async function getServerSideProps() {
  //Placeholder for fetching featured news from contents
  const featuredNews: NewsTileData[] = [
    {
      img: "/newsPlaceholder1.webp",
      date: "20.07.2023",
      text: "We will participate to the Open Earth Monitor Global workshop in Bolzano 4-6 October 2023",
      id: "testSlug",
    },
    {
      img: "/title.webp",
      date: "20.07.2023",
      text: "We will participate",
      id: "testSlug",
    },
    {
      img: "/newsPlaceholder3.webp",
      date: "20.07.2023",
      text: "We will participate to the Open Earth Monitor Global workshop in Bolzano 4-6 October 2023",
      id: "testSlug",
    },
  ];
  return featuredNews;
}

const News = async () => {
  try {
    const featuredNews = await getServerSideProps();
    return (
      <section className="relative flex flex-col w-full text-black py-28 px-6 lg:px-48 bg-white">
        <div className="w-full flex items-center justify-between mb-28">
          <h2 className="text-4xl">News</h2>
          <Link className="text-[#01685B] underline" href="/news">
            See all news
          </Link>
        </div>
        <div className="w-full grid grid-cols-1 xl:grid-cols-3 gap-8">
          {featuredNews.map((news: NewsTileData) => {
            return (
              <NewsTile
                key={uuidv4()}
                id={news.id}
                img={news.img}
                date={news.date}
                text={news.text}
              />
            );
          })}
        </div>
      </section>
    );
  } catch (e: any) {
    return <div>Error fetching data: {JSON.stringify(e)}</div>;
  }
};

export default News;
