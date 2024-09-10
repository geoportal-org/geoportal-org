import React from "react";
import { NewsTileData, PaginationData } from "../model/types";
import NewsTile from "../components/NewsTile";
import { v4 as uuidv4 } from "uuid";
import Pagination from "../components/Pagination";
import Link from "next/link";

type Props = {
  newsList: NewsTileData[];
  paginationData: PaginationData;
};

const NewsGrid = async ({ newsList, paginationData }: Props) => {
  return (
    <section className="w-full flex flex-col items-center px-6 lg:px-48 py-20 lg:py-24 gap-16">
      <div className="w-full grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-4">
        {newsList.map((news: NewsTileData) => {
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
      <Pagination pagination={paginationData} />
      <Link
        href={
          "https://www.geoportal.org/documents/20194/0/EDGE-WP3-DEL-D3.5-v2.0.pdf/ffcd644a-d196-4a84-a02e-2954dfa8a213"
        }
        className="text-[#01685B] underline"
        target="_blank"
      >
        See all on the GPP project website
      </Link>
    </section>
  );
};

export default NewsGrid;
