"use client";
import { featuresOptions } from "@/src/app/model/featuresOptions";
import React from "react";
import FeatureTile from "./FeatureTile";
import "react-multi-carousel/lib/styles.css";
import { v4 as uuidv4 } from "uuid";
import { useMediaQuery } from "@/src/app/hooks/useMediaQuery";
import dynamic from "next/dynamic";
import SchemaHeader from "../SchemaHeader/SchemaHeader";
// import CustomCarousel from "../Carousel/CustomCarousel";
const CustomCarousel = dynamic(() => import("../Carousel/CustomCarousel"), {
    ssr: true,
});
const Features = () => {
    const isMobile = useMediaQuery("(max-width: 1023px)");

    return (
        <section
            className={`relative w-full bg-white min-h-[50vh] flex flex-col items-center justify-center px-6 lg:px-48 py-20 text-black gap-20`}
        >
            <SchemaHeader type="items-list" data={featuresOptions} internalUrl/>
            <h1 className="text-5xl">Explore GEOSS main features and use cases</h1>
            {isMobile ? (
                <CustomCarousel itemsArray={featuresOptions} isFeature={true} />
            ) : (
                <div className="grid max-xl:grid-cols-4 xl:grid-cols-5 2xl:grid-cols-6 3xl:grid-cols-8 grid-flow-row auto-cols-fr w-full gap-6 ">
                    {featuresOptions.map((option) => {
                        return <FeatureTile key={uuidv4()} text={option.text} icon={option.icon} href={option.href} image={option.image || ''}/>;
                    })}
                </div>
            )}
        </section>
    );
};

export default Features;
