'use client'
import React from "react";
import { useMediaQuery } from "../../hooks/useMediaQuery";

type Props = {
    title: string;
    href: string;
};

const UCFrame = ({ href, title }: Props) => {
    const isMobile = useMediaQuery("(max-width: 1023px)");

    return (
        <section className={`w-full flex flex-col px-6 lg:px-48 py-20 lg:py-24 gap-24`}>
            <h2 className="text-2xl font-esabold">{title}</h2>
            <iframe src={href} width="100%" height={isMobile ? '500px' : '800px'} allowFullScreen></iframe>
        </section>
    );
};

export default UCFrame;
