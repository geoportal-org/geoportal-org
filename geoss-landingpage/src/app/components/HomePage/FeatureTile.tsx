import TriangleArrowRightIcon from "@/src/app/icons/TriangleArrowRightIcon";
import Link from "next/link";
import React from "react";
import Image from "next/image";

type Props = {
    icon: any;
    text: string;
    href: string;
    className?: string;
    image: string;
};

const FeatureTile = ({ icon, text, href, className, image }: Props) => {
    return (
        <Link
            href={href}
            className={`${
                className ? className : ""
            }relative block hover:no-underline hover:scale-105 cursor-pointer flex flex-col bg-[#F2F5F7] p-6 gap-4`}
        >
            <Image
                priority
                fill
                className="object-center object-cover pointer-events-none z-10"
                src={image}
                alt="image-tile"
            />
            <span className="absolute top-0 left-0 hover:scale-105 pointer-events-none z-10 w-full h-full bg-gradient-to-b from-black via-transparent via-80% opacity-80" />
            <span className="z-20">{icon}</span>
            <p className="block h-[140px] mb-12 line-clamp-6 z-20 text-white font-esabold text-lg">{text}</p>
            <TriangleArrowRightIcon className="mt-auto z-20" color="#ffffff" />
        </Link>
    );
};

export default FeatureTile;
