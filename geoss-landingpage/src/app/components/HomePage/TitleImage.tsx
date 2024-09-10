"use client";
import React from "react";
import GeneralButton from "../GeneralButton";
import Image from "next/image";
import titleImage from "@/public/title.webp";
import titleImageMobile from "@/public/title-mobile.webp";
import { useMediaQuery } from "../../hooks/useMediaQuery";

const TitleImage = () => {
  const isMobile = useMediaQuery("(max-width: 1024px)");

  return (
    <section
      className={`relative w-full h-[80vh] flex items-center justify-center md:block`}
    >
      <Image
        fill
        style={{
          objectFit: "cover",
          objectPosition: "center",
        }}
        priority
        className="pointer-events-none"
        src={isMobile ? titleImageMobile : titleImage}
        alt="homepage-image-banner"
      />

      <span className="absolute pointer-events-none w-full h-full bg-gradient-to-r from-black via-transparent via-80% opacity-60" />
      <div className="relative flex flex-col min-h-[80vh] items-center md:items-start justify-center w-full md:max-w-[30%] md:ml-48 gap-4 px-4">
        <h2 className="text-4xl md:text-[3vw] md:leading-tight text-left max-lg:w-full">
          GEOSS Portal - Open access Earth Observation database
        </h2>
        <p className="text-md w-full">
          Online map-based user interface to discover and access Earth
          observation resources.
        </p>
        <GeneralButton className="mt-8 w-full md:w-auto" href="/about">
          Read about GEOSS
        </GeneralButton>
      </div>
    </section>
  );
};

export default TitleImage;
