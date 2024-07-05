import { SiteContext, SiteContextValue } from "@/context/CurrentSiteContext";
import { SitesService } from "@/services/api/SitesService";
import { ToastStatus } from "@/types";
import { getIdFromUrl } from "@/utils/helpers";
import useCustomToast from "@/utils/useCustomToast";
import useFormatMsg from "@/utils/useFormatMsg";
import { Flex, Select } from "@chakra-ui/react";
import { useRouter } from "next/router";
import React, { useContext, useEffect, useState } from "react";

const HeaderSiteSelect = () => {
    const { translate } = useFormatMsg();
    const { showToast } = useCustomToast();
    const { currentSiteId, setCurrentSiteId, allSites, setAllSites } = useContext<SiteContextValue>(SiteContext);
    const router = useRouter();

    useEffect(() => {
        getAllSitesList();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    useEffect(() => {
        if (router.asPath === "/sites" && currentSiteId !== 0) {
            router.push("/");
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [currentSiteId]);

    const getAllSitesList = async () => {
        try {
            const res = await SitesService.getSites();
            setAllSites(res._embedded.site);
        } catch (e: any) {
            showToast({
                title: translate("general.error"),
                description: translate("pages.sites.errorFetchingSelectSites"),
                status: ToastStatus.ERROR,
            });
        }
    };

    const handleSelectChange = (v: any) => {
        setCurrentSiteId(Number(v.target.value));
    };

    return (
        <Flex position={'absolute'} left='70px' zIndex={'100000'}>
            <Select
                id="type"
                name="type"
                variant="outline"
                style={{ border: "none", borderBottom: "1px solid", borderRadius: "0" }}
                _focus={{ border: "none", outline: "none", boxShadow: "none" }}
                colorScheme={"gray"}
                width={{base: '100px', md:'200px', lg: '300px'}}
                value={currentSiteId}
                onChange={handleSelectChange}
            >
                {allSites.map((site: any) => {
                    return (
                        <option
                            style={{ color: "black" }}
                            key={getIdFromUrl(site._links.self.href)}
                            value={getIdFromUrl(site._links.self.href)}
                        >
                            {site.name}
                        </option>
                    );
                })}
            </Select>
        </Flex>
    );
};

export default HeaderSiteSelect;
