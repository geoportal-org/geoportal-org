"use client";
import React from "react";
import { PaginationData } from "../model/types";
import Link from "next/link";
import { v4 as uuidv4 } from "uuid";
import TriangleArrowRightIcon from "../icons/TriangleArrowRightIcon";
import TriangleArrowLeftIcon from "../icons/TriangleArrowLeftIcon";

type Props = {
  pagination: PaginationData;
};

const Pagination = ({ pagination }: Props) => {
  const { number, totalPages } = pagination;

  const createPageLinks = () => {
    let startPage = Math.max(1, number);
    let endPage = Math.min(totalPages, number + 1);

    // Ensure we always show 3 pages if possible
    if (number === 0) {
      endPage = Math.min(totalPages, 3);
    } else if (number === totalPages) {
      startPage = Math.max(1, totalPages - 2);
    }

    const links = [];
    for (let i = startPage; i <= endPage; i++) {
      links.push(
        <Link
          scroll={false}
          prefetch
          className={`px-4 py-2 text-[#23272A] ${
            i === number + 1
              ? "bg-[#F2F5F7] border border-[#A4ABB2]"
              : "text-black hover:bg-[#F2F5F7]"
          }`}
          key={i}
          href={`?page=${i}`}
        >
          {i}
        </Link>
      );
    }
    if (number > 2) {
      links.unshift(
        <p className="text-xl" key={uuidv4()}>
          ...
        </p>
      );
      links.unshift(
        <Link
          scroll={false}
          prefetch
          className={`px-4 py-2 text-[#23272A] ${
            number === 1
              ? "bg-[#F2F5F7] border border-[#A4ABB2]"
              : "text-black hover:bg-[#F2F5F7]"
          }`}
          key={1}
          href={`?page=${1}`}
        >
          1
        </Link>
      );
    }
    if (totalPages >= 4 && endPage !== totalPages) {
      links.push(
        <p className="text-xl" key={uuidv4()}>
          ...
        </p>
      );
      links.push(
        <Link
          scroll={false}
          prefetch
          className={`px-4 py-2 text-[#23272A] ${
            totalPages === number
              ? "bg-[#F2F5F7] border border-[#A4ABB2]"
              : "text-black hover:bg-[#F2F5F7]"
          }`}
          key={totalPages}
          href={`?page=${totalPages}`}
        >
          {totalPages}
        </Link>
      );
    }

    return links;
  };
  return (
    <div className="w-full flex flex-row items-center justify-center py-8 gap-2 text-lg">
      <Link
        aria-label="previous-page"
        className={`${number + 1 === 1 && "pointer-events-none"}`}
        scroll={false}
        prefetch
        href={`${number + 1 === 1 ? `` : `?page=${number}`}`}
      >
        <TriangleArrowLeftIcon
          color={number + 1 === 1 ? "#A4ABB2" : "#23272A"}
        />{" "}
      </Link>
      {createPageLinks()}
      <Link
        aria-label="next-page"
        className={`${number + 1 === totalPages && "pointer-events-none"}`}
        scroll={false}
        prefetch
        href={`${number + 1 === totalPages ? `` : `?page=${number + 2}`}`}
      >
        <TriangleArrowRightIcon
          color={number + 1 === totalPages ? "#A4ABB2" : "#23272A"}
        />
      </Link>{" "}
    </div>
  );
};

export default Pagination;
