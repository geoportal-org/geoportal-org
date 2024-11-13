import Link from "next/link";
import React, { ReactNode } from "react";

type ButtonProps = {
    children: ReactNode;
    onClick?: () => void;
    className?: string;
    href?: string;
};

const GeneralButton = ({ children, onClick, className, href }: ButtonProps) => {
    return (
        <>
            {href ? (
                href.includes("mailto:") ? (
                    <a
                        href={href}
                        className={`bg-[#0661A9] px-8 py-3 text-base text-center flex items-center justify-center hover:scale-105 ${className}`}
                    >
                        {children}
                    </a>
                ) : (
                    <Link
                        href={href}
                        className={`bg-[#0661A9] px-8 py-3 text-base text-center flex items-center justify-center hover:scale-105 ${className}`}
                    >
                        {children}
                    </Link>
                )
            ) : (
                <button
                    onClick={onClick}
                    className={`bg-[#0661A9] px-8 py-3 text-base text-center flex items-center justify-center hover:scale-105 ${className}`}
                >
                    {children}
                </button>
            )}
        </>
    );
};

export default GeneralButton;
