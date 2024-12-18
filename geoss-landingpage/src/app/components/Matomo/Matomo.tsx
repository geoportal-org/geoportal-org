"use client";
import React, { useEffect, useState } from "react";
import { initMatomo, push } from "../../lib/matomo";
import { usePathname } from "next/navigation";
import { getCookie } from "cookies-next";
import { setCookie } from "cookies-next/client";
import CookieNotice from "../CookieNotice/CookieNotice";

type Props = {
    portalUrl: string;
};

const Matomo = ({ portalUrl }: Props) => {
    const [showPopup, setShowPopup] = useState(false);
    const pathname = usePathname();
    useEffect(() => {
        const mtmCookieValue = getCookie("gpplp_mtm_consent");
        if (mtmCookieValue === "true") {
            loadMatomo();
        } else if (mtmCookieValue === "false") {
            console.log("Tracking disabled...");
        } else {
            setShowPopup(true);
        }
    }, []);

    const loadMatomo = async () => {
        const settingsData = await fetch("/api/settingsApi");
        const data = await fetch("/api/matomo");
        const { url } = await data.json();
        const { matomoId } = await settingsData.json();
        initMatomo({
            url: url,
            siteId: matomoId.toString(),
        });
    };

    const acceptCookies = () => {
        setCookie("gpplp_mtm_consent", true, { maxAge: 2629800 });
        loadMatomo();
        push(["rememberConsentGiven"]);
        push(["rememberCookieConsentGiven"]);
        setShowPopup(false);
    };

    const rejectCookies = () => {
        setCookie("gpplp_mtm_consent", false, { maxAge: 2629800 });
        push(["forgetConsentGiven"]);
        push(["forgetCookieConsentGiven"]);
        setShowPopup(false);
    };

    useEffect(() => {
        push(["setCustomUrl", window.location.href]);
        push(["trackPageView"]);
    }, [pathname]);

    return (
        <>
            {showPopup && (
                <CookieNotice portalUrl={portalUrl} acceptCookies={acceptCookies} rejectCookies={rejectCookies} />
            )}
        </>
    );
};

export default Matomo;
