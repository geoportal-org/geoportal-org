import { IconProps } from "@/types";

export const ContentIcon = ({ color }: IconProps) => (
    <svg width={20} height={20} viewBox="0 0 7.5 7.5" fill="none" xmlns="http://www.w3.org/2000/svg" stroke={color}>
        <g strokeWidth={0} />
        <g strokeLinecap="round" strokeLinejoin="round" />
        <path
            d="M1.563 1.875h4.375M1.563 3.125h3.125M1.563 4.375h4.375M1.563 5.625h3.125"
            strokeWidth={0.35}
            strokeLinecap="round"
            strokeLinejoin="round"
        />
    </svg>
);
