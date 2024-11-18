"use client";
import { useState, useEffect } from "react";
import { useMediaQuery } from "../hooks/useMediaQuery";

type Props = {
  src: string;
  alt: string;
  placeholder: string;
  width: number;
  height: number;
  className?: string;
};

const ImageWithFallback = ({ src, alt, placeholder, width, height, className }: Props) => {
  const [imageSrc, setImageSrc] = useState(placeholder);
  const isMobile = useMediaQuery("(max-width: 1023px)");

  useEffect(() => {
    const img = new Image();
    img.src = src;
    img.onload = () => setImageSrc(src);
    img.onerror = () => setImageSrc(placeholder);
  }, [src, placeholder]);

  return (
    <div className={`${className ? className : null}`}>
      <img
        style={{
          objectFit: "contain",
          objectPosition: "center",
          maxHeight: isMobile ? '70px' :"190px",
          aspectRatio: "auto",
        }}
        width={width}
        height={height}
        src={imageSrc}
        alt={alt}
      />
    </div>
  );
};

export default ImageWithFallback;
