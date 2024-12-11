import React from "react";
import parse from "html-react-parser";
import Link from "next/link";
import GeneralButton from "@/src/app/components/GeneralButton";
import Image from "next/image";
import { getNewsPageContent } from "../../api/newsApi/newsApi";
import SchemaHeader from "../../components/SchemaHeader/SchemaHeader";

const getServerSideProps = async (slug: string) => {
    const { title, data, date, imageUrl } = await getNewsPageContent(slug);
    return {
        title: title,
        date: date,
        img: imageUrl,
        data: data,
    };
};

const page = async ({ params }: { params: { id: string } }) => {
    try {
        const articleData = await getServerSideProps(params.id);
        return (
            <div className="w-full flex flex-col items-start px-6 lg:px-72 xl:px-96 2xl:px-[32rem] 3xl:px-[48rem] py-16 lg:py-24 gap-8 text-black">
                <SchemaHeader type="article" data={{ slug: params.id, ...articleData }} />
                <div className="w-full flex flex-row gap-4 text-2xl">
                    <Link className="text-[#5C6369]" href="/news">
                        News
                    </Link>
                    <p className="text-[#5C6369]">{"/"}</p>
                    <p>{articleData.title}</p>
                </div>
                <div className="relative w-full h-[50vh] object-cover object-center">
                    <Image
                        loading="eager"
                        fill
                        style={{
                            objectFit: "cover",
                            objectPosition: "center",
                        }}
                        alt="news-image"
                        src={articleData.img}
                    />
                </div>
                <h2 className="text-5xl ">{articleData.title}</h2>
                <p className="text-[#23272A] text-2xl">{articleData.date.substring(0, 10)}</p>
                <div className="article-content">{parse(articleData.data)}</div>
                <GeneralButton className="self-center text-white" href="/news">
                    Read more articles
                </GeneralButton>
            </div>
        );
    } catch (e: any) {
        return <div>Error fetching data: {JSON.stringify(e)}</div>;
    }
};

export default page;
