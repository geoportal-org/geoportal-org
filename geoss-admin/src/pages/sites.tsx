import { CustomError } from "@/components";
import SitesManager from "@/components/SitesManager/SitesManager";
import { SiteContext, SiteContextValue } from "@/context/CurrentSiteContext";
import { NextPage } from "next";
import { useContext } from "react";

const Sites: NextPage = () => {
    const { currentSiteId: currentSite } = useContext<SiteContextValue>(SiteContext);
    return currentSite === 0 ? <SitesManager /> : <CustomError />;
};

export default Sites;
