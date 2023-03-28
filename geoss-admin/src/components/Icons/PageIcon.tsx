import { IconProps } from "@/types";

export const PageIcon = ({ color }: IconProps) => (
    <svg xmlns="http://www.w3.org/2000/svg" width={20} height={20} viewBox="0 0 20 20" xmlSpace="preserve">
        <g strokeWidth={0} />
        <g strokeLinecap="round" strokeLinejoin="round" />
        <path
            d="M0.313 2.188h19.375v15.625H0.313zm0 2.5h19.375m-16.563 -1.25H1.875m3.75 0h-1.25m3.75 0h-1.25"
            fill="none"
            stroke={color}
            strokeWidth={0.8}
            strokeMiterlimit={10}
        />
    </svg>
);
