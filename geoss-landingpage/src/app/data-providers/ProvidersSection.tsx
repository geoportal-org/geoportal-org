import ProvidersList from "./ProvidersList";
import { fetchSettings } from "../api/settingsApi";

const getServerSideProps = async () => {
    try {
        const { providersUrl, providersLogin, providersPass } = await fetchSettings();
        const headers = new Headers();
        const authString = "Basic " + btoa(`${providersLogin}:${providersPass}`);
        headers.append("Authorization", authString);

        const providersResponse = await fetch(`${providersUrl}?page=0&size=9999`, {
            method: "GET",
            headers: headers,
        });
        const json = await providersResponse.json();
        if (Number(providersResponse.status) >= 300 || Number(providersResponse.status) < 200) {
            return {
                status: providersResponse.status,
                statusText: providersResponse.statusText,
            };
        } else {
            return { data: json._embedded.dataProviderResultModels, status: "ok" };
        }
    } catch (e: any) {
        console.error(e);
    }
};

const ProvidersSection = async () => {
    const props = await getServerSideProps();
    if (props && props.data && props.data.length > 0 && props.status === "ok") {
        return (
            <section className="w-full flex flex-col px-6 lg:px-48 pb-20 lg:pb-24 gap-6">
                <ProvidersList list={props.data} />
            </section>
        );
    } else {
        if (props) {
            return (
                <section className="w-full flex flex-col px-6 lg:px-48 pb-20 lg:pb-24 gap-6 italic text-center min-h-[40vh] justify-center">
                    Status: {props.status} - {props.statusText}
                </section>
            );
        } else {
            return (
                <section className="w-full flex flex-col px-6 lg:px-48 pb-20 lg:pb-24 gap-6 italic text-center min-h-[40vh] justify-center">
                    Unknown error
                </section>
            );
        }
    }
};

export default ProvidersSection;
