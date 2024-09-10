"use client";
import React, { useRef } from "react";
import Carousel from "react-multi-carousel";
import "react-multi-carousel/lib/styles.css";
import CustomDot from "./CustomDot";
import FeatureTile from "../HomePage/FeatureTile";
import ImageTile from "../HomePage/ImageTile";
import { useMediaQuery } from "@/src/app/hooks/useMediaQuery";

type Props = {
  itemsArray: any[];
  className?: string;
  isFeature: boolean;
};

const CustomCarousel = ({ itemsArray, className, isFeature }: Props) => {
  const carouselRef = useRef(null);
  const isMobile = useMediaQuery("(max-width: 464px)");

  const responsiveFeatures = {
    smallDesktop: {
      breakpoint: { max: 2100, min: 1324 },
      items: 4,
      partialVisibilityGutter: 50,
    },
    tablet: {
      breakpoint: { max: 1324, min: 464 },
      items: 3,
      partialVisibilityGutter: 15,
    },
    mobile: {
      breakpoint: { max: 464, min: 0 },
      items: 2,
      partialVisibilityGutter: 20,
    },
  };

  const responsiveImages = {
    superLargeDesktop: {
      breakpoint: { max: 4000, min: 3000 },
      items: 6,
      partialVisibilityGutter: 60,
    },
    largeDesktop: {
      breakpoint: { max: 3000, min: 2100 },
      items: 6,
      partialVisibilityGutter: 60,
    },
    smallDesktop: {
      breakpoint: { max: 2100, min: 1324 },
      items: 4,
      partialVisibilityGutter: 50,
    },
    tablet: {
      breakpoint: { max: 1324, min: 464 },
      items: 2,
      partialVisibilityGutter: 30,
    },
    mobile: {
      breakpoint: { max: 464, min: 0 },
      items: 1,
      partialVisibilityGutter: 50,
    },
  };

  return (
    <div className={`relative w-full ${className ? className : ""}`}>
      <Carousel
        ref={carouselRef}
        additionalTransfrom={0}
        arrows
        partialVisbile={true}
        removeArrowOnDeviceType={["tablet", "mobile"]}
        autoPlaySpeed={3000}
        centerMode={false}
        className="w-full"
        containerClass={`${isFeature ? "h-[400px]" : "h-[550px] "}`}
        dotListClass=""
        draggable={false}
        focusOnSelect={false}
        infinite={false}
        itemClass={`px-2 max-w-[420px]`}
        keyBoardControl
        minimumTouchDrag={80}
        pauseOnHover
        renderArrowsWhenDisabled={false}
        renderButtonGroupOutside={false}
        renderDotsOutside={false}
        customDot={<CustomDot isFeature={isFeature} />}
        responsive={isFeature ? responsiveFeatures : responsiveImages}
        rewind={false}
        rewindWithAnimation={false}
        rtl={false}
        shouldResetAutoplay
        showDots={true}
        sliderClass=""
        slidesToSlide={2}
        swipeable
      >
        {itemsArray.map((option, index) => {
          if (isFeature) {
            return (
              <FeatureTile
                key={index}
                text={option.text}
                icon={option.icon}
                href={option.href}
              />
            );
          } else {
            return (
              <ImageTile
                key={index}
                text={option.text}
                href={option.href}
                bg={option.img}
              />
            );
          }
        })}
      </Carousel>
    </div>
  );
};

export default CustomCarousel;
