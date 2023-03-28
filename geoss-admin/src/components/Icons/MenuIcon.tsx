import { IconProps } from "@/types";

export const MenuIcon = ({ color }: IconProps) => (
    <svg width={20} height={20} viewBox="0 0 7.5 7.5" fill="none" xmlns="http://www.w3.org/2000/svg" stroke="#000">
        <g strokeWidth={0} />
        <g strokeLinecap="round" strokeLinejoin="round" />
        <path
            d="M1.25 5.313h5m-5-1.563h5m-5-1.562h5"
            stroke={color}
            strokeWidth={0.469}
            strokeLinecap="round"
            strokeLinejoin="round"
        />
    </svg>
);
