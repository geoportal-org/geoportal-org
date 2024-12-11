"use client";
import React, { useEffect } from "react";
import { fetchSettings } from "../../api/settingsApi/settingsApi";
import { initMatomo, push } from "../../lib/matomo";
import { usePathname } from "next/navigation";

const Matomo = () => {
    const pathname = usePathname();
    useEffect(() => {
        (async () => {
            const { matomoId } = await fetchSettings();
            const data = await fetch("/api/matomo");
            const { url } = await data.json();
            initMatomo({
                url: url,
                siteId: matomoId.toString(),
            });
        })();
    }, []);

    useEffect(() => {
        push(["setCustomUrl", window.location.href]);
        push(["trackPageView"]);
    }, [pathname]);

    return <></>;
};

export default Matomo;
