import React from "react";
import { ProviderData } from "../model/types";
import ProvidersList from "./ProvidersList";

const getServerSideProps = async () => {
  const res = await fetch(
    "https://gpp.devel.esaportal.eu/rest/services-providers"
  );
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
};

const ProvidersSection = async () => {
  try {
    const providersList = await getServerSideProps();
    return (
      <section className="w-full flex flex-col px-6 lg:px-48 pb-20 lg:pb-24 gap-6">
        <ProvidersList list={providersList} />
      </section>
    );
  } catch (e: any) {
    return <div>Error fetching data: {JSON.stringify(e)}</div>;
  }
};

export default ProvidersSection;
