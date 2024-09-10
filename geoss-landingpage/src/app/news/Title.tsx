import Link from "next/link";
import React from "react";

const Title = () => {
  return (
    <section className="w-full flex flex-col px-6 lg:px-48 py-20 lg:py-24 bg-[#F2F5F7] gap-6">
      <h2 className="text-5xl ">News</h2>
      <p className="max-w-full lg:max-w-[50%] text-[#5C6369]">
        In this section you can see the latest news about the GEOSS Platform:
        recent activities like workshops, meetings, symposia and news about
        GEOSS enhancements.
      </p>
      <Link
        href={
          "https://www.geoportal.org/documents/20194/0/EDGE-WP3-DEL-D3.5-v2.0.pdf/ffcd644a-d196-4a84-a02e-2954dfa8a213"
        }
        className="text-[#01685B] underline"
        target="_blank"
      >
        See all on the GPP project website{" "}
      </Link>
    </section>
  );
};

export default Title;
