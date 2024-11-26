"use client";
import React from "react";
import GeneralButton from "../GeneralButton";
import Image from "next/image";
import titleImage from "@/public/title.webp";
import titleImageMobile from "@/public/title-mobile.webp";
import { useMediaQuery } from "../../hooks/useMediaQuery";

const TitleImage = ({portalUrl} : any) => {
    const isMobile = useMediaQuery("(max-width: 1024px)");
    return (
        <section className={`relative w-full h-[70vh] flex items-center justify-center md:block text-white`}>
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
            <div className="relative flex flex-col h-full items-center md:items-start justify-center w-full md:max-w-[80%] md:ml-48 md:mr-48 gap-4 px-4">
                <h2 className="text-4xl md:text-[3vw] md:leading-tight text-left max-lg:w-full md:max-w-[30%] ">
                    GEOSS Portal: Open Access Earth Observations
                </h2>
                <p className="text-md w-full md:max-w-[30%] ">
                    Online map-based user interface to discover and access Earth observation resources.
                </p>
                <div className="flex flex-col md:flex-row gap-4 w-full mt-8">
                    <GeneralButton className="w-full md:w-auto" href="/about">
                        Read about GEOSS
                    </GeneralButton>
                    <GeneralButton className="w-full md:w-auto bg-white text-black" href={portalUrl}>
                        Browse Data
                    </GeneralButton>
                </div>
            </div>
        </section>
    );
};

export default TitleImage;
