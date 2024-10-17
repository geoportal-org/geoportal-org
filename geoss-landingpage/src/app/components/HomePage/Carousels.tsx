import React from "react";
// import CustomCarousel from "../Carousel/CustomCarousel";
import { portalCarouselOptions } from "@/src/app/model/portalCarouselOptions";
import Link from "next/link";
import { catalogueCarouselOptions } from "@/src/app/model/catalogueCarouselOptions";
import { thematicCarouselOptions } from "@/src/app/model/thematicCarouselOptions";
import dynamic from "next/dynamic";
import SchemaHeader from "../SchemaHeader/SchemaHeader";
const CustomCarousel = dynamic(() => import("../Carousel/CustomCarousel"), {
    ssr: true,
});

const Carousels = () => {
    return (
        <section
            className={`relative w-full text-white min-h-[30vh] bg-[#061D30] py-32 px-6 lg:px-48 flex items-center justify-center md:block`}
        >
            <SchemaHeader type="items-list" data={portalCarouselOptions}/>
            <SchemaHeader type="items-list" data={catalogueCarouselOptions} />
            <SchemaHeader type="items-list" data={thematicCarouselOptions} />

            <div className="flex flex-col w-full gap-24">
                <div>
                    <h2 className="text-4xl">Commmunity Portals</h2>
                    <CustomCarousel itemsArray={portalCarouselOptions} isFeature={false} />{" "}
                </div>
                <div>
                    <h2 className="text-4xl">Catalogues</h2>
                    <CustomCarousel itemsArray={catalogueCarouselOptions} isFeature={false} />{" "}
                </div>{" "}
                <div>
                    <h2 className="text-4xl">Thematic Areas</h2>
                    <CustomCarousel itemsArray={thematicCarouselOptions} isFeature={false} />{" "}
                </div>
                <h1 className="w-full text-center text-xl my-12">
                    No matching category?{" "}
                    <Link className="text-[#209D90] underline" href="/">
                        Create your Community Portal
                    </Link>{" "}
                    and configure desired data filters.
                </h1>
            </div>
        </section>
    );
};

export default Carousels;
