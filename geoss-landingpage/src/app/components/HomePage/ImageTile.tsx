import Link from "next/link";
import React from "react";
import Image from "next/image";

type Props = {
  text: string;
  href: string;
  bg: string;
  className?: string;
};

const ImageTile = ({ text, href, bg, className }: Props) => {
  return (
    <div className="relative hover:scale-[1.02] cursor-pointer ">
      <Image
        priority
        fill
        className="object-center object-cover pointer-events-none"
        src={bg}
        alt="image-tile"
      />
      <span className="absolute hover:scale-[1.02] pointer-events-none z-10 w-full h-full bg-gradient-to-t from-black via-transparent via-40% opacity-60" />
      <Link
        href={href}
        target="_blank"
        className={`${
          className ? className : ""
        }hover:no-underline block relative flex flex-col p-4 min-h-[460px]`}
      >
        <p className="z-10 mt-auto text-xl">{text}</p>
      </Link>
    </div>
  );
};

export default ImageTile;
