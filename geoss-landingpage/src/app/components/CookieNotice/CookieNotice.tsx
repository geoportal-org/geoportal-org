import { motion } from "framer-motion";
import React from "react";
import { v4 as uuidv4 } from "uuid";
import GeneralButton from "../GeneralButton";
import Link from "next/link";
import CookieIcon from "../../icons/CookieIcon";

type Props = {
    portalUrl: string;
    acceptCookies: () => void;
    rejectCookies: () => void;
};

const CookieNotice = ({ portalUrl, acceptCookies, rejectCookies }: Props) => {
    return (
        <motion.div
            key={uuidv4()}
            initial={{ y: "100%" }}
            animate={{ y: 0 }}
            exit={{ y: "100%" }}
            transition={{ duration: 0.2, ease: "easeIn" }}
            className="fixed flex flex-row items-center bottom-2 mx-auto inset-x-0 w-[70%] rounded-md z-[3000] bg-[#209d90] min-h-[150px] bg-opacity-90 p-4 gap-8 text-justify"
        >
            <CookieIcon width="4em" height="4em" />
            <div className="w-full flex flex-col gap-6 text-white">
                <p className="text-white text-xl">
                    We only use Cookies to track visits to our site. We do not store any personal information about
                    users. If you wish to restrict or block the use of Cookies, please refer to the instructions on the{" "}
                    <Link
                        className="lg:ml-auto text-[#0661A9] underline lg:text-end"
                        href={`${portalUrl}/terms-conditions`}
                    >
                        Terms and Conditions
                    </Link>{" "}
                    page for further information or to change your preferences.
                </p>
                <div className="self-end flex flex-row gap-6 text-white">
                    <GeneralButton onClick={acceptCookies} className="max-h-[40px]">
                        Accept All
                    </GeneralButton>
                    <GeneralButton onClick={rejectCookies} className="max-h-[40px]">
                        Reject All
                    </GeneralButton>
                </div>
            </div>
        </motion.div>
    );
};

export default CookieNotice;
