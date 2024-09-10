import Link from "next/link";
import React from "react";
import ESA from "@/public/ESA.webp";
import Institute from "@/public/institute.webp";
import Uni from "@/public/uni.webp";
import Eversis from "@/public/eversis.webp";
import Image from "next/image";

const Footer = () => {
  return (
    <div className="relative flex flex-col w-full text-black px-6 lg:px-48 bg-[#01685B1A] divide-y-2 divide-[#01685B]">
      <section className="grid max-lg:grid-rows-2 lg:grid-cols-2 w-full py-10 gap-4">
        <div className="flex flex-col w-full">
          <h2 className="text-xl text-[#5C6369]">CONTACT</h2>
          <span className="h-full flex items-center text-2xl">
            geoss_platform_support@esa.int
          </span>
        </div>
        <div className="flex flex-col gap-2 w-full">
          <h2 className="text-xl text-[#5C6369]">PARTNERS</h2>
          <div className="max-lg:grid grid-cols-2 lg:flex h-full gap-8 justify-between">
            <div className="relative flex items-center justify-center">
              <Image
                height={100}
                width={200}
                style={{
                  objectFit: "contain",
                  objectPosition: "center",
                  maxHeight: "100px",
                }}
                alt="esa-image"
                src={ESA}
              />
            </div>
            <div className="relative flex items-center justify-center">
              <Image
                height={100}
                width={200}
                style={{
                  objectFit: "contain",
                  objectPosition: "center",
                  maxHeight: "100px",
                }}
                alt="esa-image"
                src={Institute}
              />
            </div>
            <div className="relative flex items-center justify-center">
              <Image
                height={100}
                width={200}
                style={{
                  objectFit: "contain",
                  objectPosition: "center",
                  maxHeight: "100px",
                }}
                alt="esa-image"
                src={Uni}
              />
            </div>
            <div className="relative flex items-center justify-center">
              <Image
                height={100}
                width={200}
                style={{
                  objectFit: "contain",
                  objectPosition: "center",
                  maxHeight: "100px",
                  aspectRatio: "auto",
                }}
                alt="esa-image"
                src={Eversis}
              />
            </div>
          </div>
        </div>
      </section>
      <section className="flex flex-col lg:flex-row w-full py-8 gap-4">
        <h2>GPP © 2024</h2>
        <p className="text-[#5C6369] max-w-[70%]">
          The project receives funding from the European Union’s Horizon 2020
          Research and innovation programme under grant agreement No 101039118.
        </p>
        <Link
          className="lg:ml-auto text-[#01685B] underline lg:text-end"
          href="https://www.geoportal.org/terms-conditions"
        >
          Terms and Conditions
        </Link>
      </section>
    </div>
  );
};

export default Footer;
