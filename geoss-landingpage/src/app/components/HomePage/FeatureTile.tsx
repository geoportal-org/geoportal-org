import TriangleArrowRightIcon from "@/src/app/icons/TriangleArrowRightIcon";
import Link from "next/link";
import React from "react";

type Props = {
  icon: any;
  text: string;
  href: string;
  className?: string;
};

const FeatureTile = ({ icon, text, href, className }: Props) => {
  return (
    <Link
      href={href}
      className={`${
        className ? className : ""
      }block hover:no-underline hover:scale-105 cursor-pointer flex flex-col bg-[#F2F5F7] p-6 gap-4`}
    >
      {icon}
      <p className="block h-[120px] mb-12 line-clamp-6">{text}</p>
      <TriangleArrowRightIcon className="mt-auto" color="#0661A9" />
    </Link>
  );
};

export default FeatureTile;
