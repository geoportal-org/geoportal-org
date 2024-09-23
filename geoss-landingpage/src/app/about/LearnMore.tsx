import Link from "next/link";
import React from "react";

const LearnMore = () => {
  return (
    <section className="w-full bg-[#F9FAFB] px-6 lg:px-48 py-20 lg:py-24">
      <h2 className="text-4xl mb-16 lg:mb-20">Learn more about GEOSS Portal</h2>
      <div className="flex flex-col lg:flex-row w-full justify-between gap-8">
        <span className="flex flex-col gap-4 text-xl py-8 border-t border-[#DBDEE0]">
          <p>Explore GEOSS Platform with Tutorial Mode</p>
          <Link
            href={"https://www.geoportal.org/community/guest/tutorials  "}
            className="text-[#01685B] underline"
          >
            Access Tutorial Mode
          </Link>
        </span>
        <span className="flex flex-col gap-4 text-xl py-8 border-t border-[#DBDEE0]">
          <p>Examples of the GEOSS Platform capabilities from real life</p>
          <Link
            href={"https://prezi.com/view/Z68hB3z0c2d95hdKLSGh/"}
            className="text-[#01685B] underline"
          >
            Explore scenarios
          </Link>
        </span>
        <span className="flex flex-col gap-4 text-xl py-8 border-t border-[#DBDEE0]">
          <p>See GEOSS Platform Video Tutorials</p>
          <Link
            href={"https://www.youtube.com/channel/UCZwhJZI76s7K9eAcBXAPyrw"}
            className="text-[#01685B] underline"
          >
            See videos
          </Link>
        </span>
      </div>
      <p className="mt-12 text-xl">
        {" "}
        Want to learn more?{" "}
        <Link
          href={
            "https://www.geoportal.org/documents/20194/0/EDGE-WP3-DEL-D3.5-v2.0.pdf/ffcd644a-d196-4a84-a02e-2954dfa8a213"
          }
          className="text-[#01685B] underline"
        >
          Click to see the manual
        </Link>
      </p>
    </section>
  );
};

export default LearnMore;
