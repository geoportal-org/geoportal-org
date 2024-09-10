import React from "react";
import GeneralButton from "../GeneralButton";
import Image from "next/image";
import gradientBg from "@/public/gradient-bg.webp";

const Access = () => {
  return (
    <section className="relative w-full flex flex-col items-center justify-center max-lg:px-6 py-28 gap-4">
      <Image
        priority
        fill
        style={{
          zIndex: "-1",
          objectFit: "cover",
          objectPosition: "center",
        }}
        className="pointer-events-none"
        src={gradientBg}
        alt="data-access-section-image"
      />
      <h2 className="text-5xl text-center">Data Discovery and Access</h2>
      <p className="text-sm text-center">
        Browse through thousands of datasets from multiple data providers.
      </p>
      <GeneralButton
        className="mt-8 w-full md:w-auto"
        href="https://www.geoportal.org/"
      >
        Browse Data
      </GeneralButton>
    </section>
  );
};

export default Access;
