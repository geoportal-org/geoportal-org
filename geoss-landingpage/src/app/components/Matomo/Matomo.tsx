"use client";
import React, { useEffect } from "react";
import { initMatomo, push } from "../../lib/matomo";
import { usePathname } from "next/navigation";

const Matomo = () => {
    const pathname = usePathname();
    useEffect(() => {
        (async () => {
            const settingsData = await fetch("/api/settingsApi");
            const data = await fetch("/api/matomo");
            const { url } = await data.json();
            const { matomoId } = await settingsData.json();
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
