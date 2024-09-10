"use client";
import Image, { StaticImageData } from "next/image";
import { useState } from "react";

type Props = {
  isDisabled?: boolean;
  src: StaticImageData;
  alt: string;
  tooltipText: string;
  width: number;
  height: number;
};

const TooltipImage = ({ isDisabled = false, src, alt, tooltipText, width, height }: Props) => {
  const [isTooltipVisible, setTooltipVisible] = useState(false);

  return (
    <div className={`relative flex items-center justify-center ${isDisabled && 'grayscale'}`}>
      <Image
        src={src}
        alt={alt}
        width={width}
        height={height}
        onMouseEnter={() => setTooltipVisible(true)}
        onMouseLeave={() => setTooltipVisible(false)}
      />

      {isTooltipVisible && (
        <div className="absolute z-10 top-full mt-2 px-2 py-1 text-sm text-white bg-black rounded-md shadow-lg w-max">
          {tooltipText}
        </div>
      )}
    </div>
  );
};

export default TooltipImage;
