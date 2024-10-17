import React from "react";
import Accordion from "../components/Accordion";
import { aboutPageQuestions } from "../model/aboutPageQuestions";
import Image from "next/image";
import questionsImage from "@/public/sentinel.webp";
import { v4 as uuidv4 } from "uuid";
import SchemaHeader from "../components/SchemaHeader/SchemaHeader";

const Questions = () => {
    return (
        <section className="w-full px-6 lg:px-48 py-20 lg:py-24">
            <SchemaHeader type="faq" data={aboutPageQuestions} />
            <h2 className="text-5xl mb-16 lg:mb-20">About</h2>
            <div className="w-full flex flex-col lg:flex-row justify-between gap-16">
                <div id="questions" className="w-full max-lg:order-2 flex flex-col divide-y justify-center">
                    {aboutPageQuestions.map((question) => {
                        return (
                            <Accordion key={uuidv4()} text={question.text} content={question.content} textAccordion />
                        );
                    })}
                </div>
                <div id="image" className="w-full flex max-lg:order-1">
                    <Image
                        priority
                        loading="eager"
                        className="w-full object-cover object-left min-h-[300px] lg:max-h-[50vh]"
                        alt="questionsImage"
                        src={questionsImage}
                    />
                </div>
            </div>
        </section>
    );
};

export default Questions;
