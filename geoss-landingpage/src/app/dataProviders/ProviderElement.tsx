import React from "react";
import { ProviderData } from "../model/types";
import ImageWithFallback from "../components/ImageWithFallback";
import Link from "next/link";
import Image from "next/image";
import discoverable from "@/public/providersIcons/discoverable.webp";
import accessible from "@/public/providersIcons/accessible.webp";
import standard_encoding_using from "@/public/providersIcons/standard_encoding_using.webp";
import well_documented_metadata from "@/public/providersIcons/well_documented_metadata.webp";
import traceable from "@/public/providersIcons/traceable.webp";
import quality_documented from "@/public/providersIcons/quality_documented.webp";
import preserved from "@/public/providersIcons/preserved.webp";
import periodically_verified from "@/public/providersIcons/periodically_verified.webp";
import reviewed_and_refreshed from "@/public/providersIcons/reviewed_and_refreshed.webp";
import tagged_with_permanent_ID from "@/public/providersIcons/tagged_with_permanent_ID.webp";
import TooltipImage from "./TooltipImage";
import ShareIcon from "../icons/ShareIcon";

const ProviderElement = ({
  name,
  id,
  description,
  title,
  approval_status,
  state,
  image_url,
  extras,
}: ProviderData) => {
  const url = extras.find(
    (element) => element.key === "Institution Web Site URL"
  )?.value;
  const date = extras.find(
    (element) => element.key === "Registration Date"
  )?.value;

  let principles = [];
  const principlesStr = extras.find(
    (element) => element.key === "GEO Data Management Principles Label"
  )?.value;

  if (principlesStr) {
    principles = principlesStr.split(",");
    principles = principles
      .map((principle: string) => {
        const label = principle.split("-")[0];
        if (label) {
          return label.toLowerCase().trim().replace(/ /g, "_");
        } else {
          return null;
        }
      })
      .filter((principle: any) => principle);
  }

  return (
    <li className="flex flex-col lg:flex-row min-h-56 w-full border border-solid lg:gap-4 my-4">
      <ImageWithFallback
        className="relative min-h-20 lg:min-h-56 w-full lg:w-[40%] flex items-center justify-center bg-[#F9FAFB]"
        alt={title}
        height={200}
        width={400}
        src={image_url}
        placeholder={"/geoss-gray.svg"}
      />
      <div className="p-4 w-full lg:w-[60%] flex flex-col items-between justify-between gap-2">
        <div className="flex w-full items-start justify-between gap-8">
          <h2 className="text-xl text-[#061D30]">{title}</h2>
          <button
            className="hover:scale-125 mt-1"
            onClick={() => navigator.clipboard.writeText(url)}
          >
            <ShareIcon width="1.2em" height="1.2em" />
          </button>
        </div>
        <p>Registration date: {date || "No data"}</p>
        <Link target="_blank" className="text-[#01685B] underline" href={url}>
          {url}
        </Link>
        <p className="line-clamp-6 lg:line-clamp-3 text-[#5C6369]">
          {description}
        </p>
        <div className="w-full flex flex-wrap gap-2">
          <TooltipImage
            isDisabled={
              !principles.some((prc: string) => prc === "discoverable")
            }
            width={30}
            height={30}
            alt="discoverable"
            src={discoverable}
            tooltipText="Discoverable"
          />
          <TooltipImage
            isDisabled={!principles.some((prc: string) => prc === "accessible")}
            width={30}
            height={30}
            alt="accessible"
            src={accessible}
            tooltipText="Accessible"
          />
          <TooltipImage
            isDisabled={
              !principles.some(
                (prc: string) => prc === "standard_encoding_using"
              )
            }
            width={30}
            height={30}
            alt="standard_encoding_using"
            src={standard_encoding_using}
            tooltipText="Standard encoding using"
          />
          <TooltipImage
            isDisabled={
              !principles.some(
                (prc: string) => prc === "well_documented_metadata"
              )
            }
            width={30}
            height={30}
            alt="well_documented_metadata"
            src={well_documented_metadata}
            tooltipText="Well documented metadata"
          />
          <TooltipImage
            isDisabled={!principles.some((prc: string) => prc === "traceable")}
            width={30}
            height={30}
            alt="traceable"
            src={traceable}
            tooltipText="Traceable"
          />
          <TooltipImage
            isDisabled={
              !principles.some((prc: string) => prc === "quality_documented")
            }
            width={30}
            height={30}
            alt="quality_documented"
            src={quality_documented}
            tooltipText="Quality documented"
          />
          <TooltipImage
            isDisabled={!principles.some((prc: string) => prc === "preserved")}
            width={30}
            height={30}
            alt="preserved"
            src={preserved}
            tooltipText="Preserved"
          />
          <TooltipImage
            isDisabled={
              !principles.some((prc: string) => prc === "periodically_verified")
            }
            width={30}
            height={30}
            alt="periodically_verified"
            src={periodically_verified}
            tooltipText="Periodically verified"
          />
          <TooltipImage
            isDisabled={
              !principles.some(
                (prc: string) => prc === "reviewed_and_refreshed"
              )
            }
            width={30}
            height={30}
            alt="reviewed_and_refreshed"
            src={reviewed_and_refreshed}
            tooltipText="Reviewed and refreshed"
          />
          <TooltipImage
            isDisabled={
              !principles.some(
                (prc: string) => prc === "tagged_with_permanent_ID"
              )
            }
            width={30}
            height={30}
            alt="tagged_with_permanent_ID"
            src={tagged_with_permanent_ID}
            tooltipText="Tagged with permanent ID"
          />
        </div>
      </div>
    </li>
  );
};

export default ProviderElement;
