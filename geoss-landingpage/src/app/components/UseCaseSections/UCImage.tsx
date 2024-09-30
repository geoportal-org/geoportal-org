import React from "react";
import GeneralButton from "../GeneralButton";
import Image from "next/image";
import parse from 'html-react-parser';

type Props = {
    isDarkBg?: boolean;
    side: "left" | "right";
    title: string;
    text: any;
    imageSRC: string;
    buttonText: string;
    buttonHref: string;
};

const UCImage = ({ isDarkBg = false, side, title, text, imageSRC, buttonText, buttonHref }: Props) => {
    return (
        <section className={`w-full px-6 lg:px-48 py-20 lg:py-24 ${isDarkBg && "bg-[#F2F5F7]"}`}>
            <div className={`w-full flex flex-col xl:flex-row justify-between gap-12 xl:gap-32`}>
                <div id="text" className={`w-full flex flex-col justify-center gap-8 ${side === "left" && "order-2"}`}>
                    <h2 className="text-5xl">{title}</h2>
                    <div className="text-[#5C6369] whitespace-pre-wrap [&_a]:text-[#3483eb]">{parse(text)}</div>
                    {buttonText !== "" && (
                        <GeneralButton className="xl:max-w-[70%] 2xl:max-w-[50%] 3xl:max-w-[40%]" href={buttonHref}>
                            {buttonText}
                        </GeneralButton>
                    )}
                </div>
                <div id="image" className={`w-full my-auto flex ${side === "left" && "order-1"}`}>
                    <Image
                        priority
                        loading="eager"
                        className="w-full object-contain object-center min-h-[300px] lg:max-h-[50vh]"
                        alt="questionsImage"
                        width={400}
                        height={200}
                        src={imageSRC}
                    />
                </div>
            </div>
        </section>
    );
};

export default UCImage;
