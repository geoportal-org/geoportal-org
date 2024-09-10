import React from "react";
import { NewsTileData } from "../model/types";
import Link from "next/link";
import Image from "next/image";

const NewsTile = ({ img, date, text, id }: NewsTileData) => {
  return (
    <Link
      href={`/news/${id}`}
      className="relativeblock flex flex-col gap-4 hover:scale-[1.02] hover:no-underline cursor-pointer"
    >
      <div className="relative w-full h-[50vw] md:h-[30vw] xl:h-[15vw] object-cover object-center">
        <Image
          priority
          fill
          style={{
            objectFit: "cover",
          }}
          alt="news-image"
          src={img}
        />
      </div>

      <p>{date}</p>
      <p className="min-h-[56px] text-xl">{text}</p>
    </Link>
  );
};

export default NewsTile;
