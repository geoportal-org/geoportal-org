"use client";
import React, { useEffect, useState } from "react";
import { ProviderElementType } from "../model/types";
import ProviderElement from "./ProviderElement";
import GeneralButton from "../components/GeneralButton";
import Dropdown from "../components/Dropdown";
import SchemaHeader from "../components/SchemaHeader/SchemaHeader";

const sortingOptions = [
    {
        text: "Alphabetical A-Z",
        value: "a-z",
    },
    {
        text: "Alphabetical Z-A",
        value: "z-a",
    },
    {
        text: "Date ASC",
        value: "date-asc",
    },
    {
        text: "Date DESC",
        value: "date-desc",
    },
];

type Props = {
    list: ProviderElementType[];
};

const ProvidersList = ({ list }: Props) => {
    const [elementCount, setElementsCount] = useState(20);
    const [providersList, setProvidersList] = useState(list);
    const [sorting, setSorting] = useState<{ text: string; value: string }>({
        text: "Alphabetical A-Z",
        value: "a-z",
    });
    const [query, setQuery] = useState("");

    useEffect(() => {
        setProvidersList(() =>
            list.filter(
                (provider: ProviderElementType) =>
                    provider.name.toLowerCase().includes(query.toLowerCase()) ||
                    provider.data.shortDescription.toLowerCase().includes(query.toLowerCase())
            )
        );
    }, [query, list]);

    useEffect(() => {
        const tempArr = [...providersList];
        switch (sorting.value) {
            case "a-z":
                tempArr.sort((a, b) => a.name.localeCompare(b.name));
                break;
            case "z-a":
                tempArr.sort((a, b) => b.name.localeCompare(a.name));
                break;
            case "date-asc":
                tempArr.sort((a, b) => a.createDate.localeCompare(b.createDate));
                break;
            case "date-desc":
                tempArr.sort((a, b) => b.createDate.localeCompare(a.createDate));
                break;
        }
        setProvidersList(tempArr);
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [sorting]);

    return (
        <div className="flex flex-col gap-8">
            <SchemaHeader type="providers-list" data={providersList} />

            <div className="w-full flex flex-col lg:flex-row items-center justify-between gap-2 lg:gap-8">
                <input
                    placeholder="Search for data provider"
                    className="w-full lg:w-[70%] inputIcon h-[50px] border border-solid"
                    value={query}
                    onChange={(e: any) => setQuery(e.target.value)}
                />
                <Dropdown
                    className="w-full lg:w-[30%] bg-[#F9FAFB] border border-solid h-[50px]"
                    text={sorting.text}
                    options={sortingOptions}
                    onChange={setSorting}
                />
            </div>
            
            {providersList.length > 0 && (
                <ul className="gap-4">
                    {providersList.map((provider: ProviderElementType, index: number) => {
                        if (index < elementCount) {
                            return (
                                <ProviderElement
                                    key={provider.id}
                                    name={provider.name}
                                    date={provider.createDate}
                                    url={provider.data.websiteInstitutionURL}
                                    description={provider.data.shortDescription}
                                    image_url={provider.data.organizationLogoURL}
                                    principlesStr={provider.data.dataManagementPrinciplesLabel}
                                />
                            );
                        }
                    })}
                </ul>
            )}
            {providersList.length === 0 ? (
                <p className="text-[#5C6369] self-center">No results matching the query</p>
            ) : elementCount < providersList.length ? (
                <GeneralButton
                    className="w-full lg:max-w-[20%] self-center text-white"
                    onClick={() => setElementsCount((prev) => prev + 20)}
                >
                    SHOW MORE
                </GeneralButton>
            ) : null}
        </div>
    );
};

export default ProvidersList;
