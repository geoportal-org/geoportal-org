import React from "react";
import { ProviderData } from "../model/types";
import ProvidersList from "./ProvidersList";

const getServerSideProps = async () => {
    try {
        const url = process.env.NEXT_PUBLIC_DATA_PROVIDERS_URL || '';
        const res = await fetch(url);
        if (Number(res.status) >= 300 || Number(res.status) < 200) {
            return {
                status: res.status,
                statusText: res.statusText,
            };
        } else {
            const list = await res.json();
            let providersList = list.organizations;
            providersList.forEach((provider: ProviderData) => {
                if (
                    !provider.image_url.includes("https://") &&
                    !provider.image_url.includes("http://") &&
                    provider.image_url.length > 0
                ) {
                    provider.image_url = "https://" + provider.image_url;
                }

                if (provider.image_url.includes("See ")) {
                    provider.image_url = "https://www.ecmwf.int/";
                }
            });

            return providersList;
        }
    } catch (e: any) {
        console.error(e);
    }
};

const ProvidersSection = async () => {
    try {
        const props = await getServerSideProps();
        if (props.status && props.status !== 200 && props.statusText) {
            return (
                <section className="w-full flex flex-col px-6 lg:px-48 pb-20 lg:pb-24 gap-6 italic text-center min-h-[40vh] justify-center">
                    Status: {props.status} - {props.statusText}
                </section>
            );
        } else {
            return (
                <section className="w-full flex flex-col px-6 lg:px-48 pb-20 lg:pb-24 gap-6">
                    <ProvidersList list={props} />
                </section>
            );
        }
    } catch (e: any) {
        return <div>Error fetching data: {JSON.stringify(e)}</div>;
    }
};

export default ProvidersSection;
