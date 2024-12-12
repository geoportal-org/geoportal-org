export interface SettingsType {
    siteId: number;
    providersUrl: string;
    providersLogin: string;
    providersPass: string;
    matomoId: number | string;
    error?: any;
}

export async function fetchSettings() {
    const apiUrl = process.env.NEXT_PUBLIC_API_URL;

    let result: SettingsType = {
        siteId: 0,
        providersUrl: "",
        providersLogin: "",
        providersPass: "",
        matomoId: "",
    };
    try {
        const siteIdResponse = await fetch(`${apiUrl}contents/rest/site/search/findByUrl?url=lp`, {
            cache: "no-store",
        });
        const siteIdJson = await siteIdResponse.json();
        const apiSettingsResponse = await fetch(
            `${apiUrl}settings/rest/sites/${siteIdJson.id}/api-settings?page=0&size=100`,
            {
                cache: "no-store",
            }
        );
        const webSettingsResponse = await fetch(
            `${apiUrl}settings/rest/sites/${siteIdJson.id}/web-settings?page=0&size=100`,
            {
                cache: "no-store",
            }
        );
        const apiSettingsJson = await apiSettingsResponse.json();
        const webSettingsJson = await webSettingsResponse.json();

        const url = apiSettingsJson._embedded.apiSettings.find((e: any) => e.key === "dabDataProvidersUrl").value;
        const login = apiSettingsJson._embedded.apiSettings.find(
            (e: any) => e.key === "dabDataProvidersUsername"
        ).value;
        const pass = apiSettingsJson._embedded.apiSettings.find((e: any) => e.key === "dabDataProvidersPassword").value;
        result = {
            siteId: siteIdJson.id,
            providersUrl: url,
            providersLogin: login,
            providersPass: pass,
            matomoId: webSettingsJson._embedded.webSettings.find((e: any) => e.set === "matomo").value || 0,
        };
    } catch (e: any) {
        result = {
            siteId: 0,
            providersUrl: "",
            providersLogin: "",
            providersPass: "",
            matomoId: "",
            error: e,
        };
    }

    return result;
}
