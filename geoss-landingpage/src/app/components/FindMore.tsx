import Link from "next/link";
import React from "react";
import { LinkData } from "../model/types";
import { v4 as uuidv4 } from "uuid";

type Props = {
  isDarkBg?: boolean;
  links: LinkData[];
};

const FindMore = ({ isDarkBg = false, links }: Props) => {
  return (
    <section
      className={`w-full flex flex-col gap-2 px-6 lg:px-48 py-20 lg:py-24 ${
        isDarkBg && "bg-[#F2F5F7]"
      }`}
    >
      <h2 className="text-4xl mb-8">Find more information about:</h2>
      {links.map((link: LinkData) => {
        return (
          <Link
            key={uuidv4()}
            href={link.href}
            className="text-[#01685B] underline"
          >
            {link.text}
          </Link>
        );
      })}
    </section>
  );
};

export default FindMore;
