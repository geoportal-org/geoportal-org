import { createContext, Dispatch, SetStateAction } from "react";

export interface SiteContextValue {
    currentSiteId: number;
    setCurrentSiteId: Dispatch<SetStateAction<number>>;
    allSites: [],
    setAllSites: Dispatch<SetStateAction<[]>>
}

export const SiteContext = createContext({} as SiteContextValue);
