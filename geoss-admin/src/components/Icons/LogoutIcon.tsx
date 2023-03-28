import { IconProps } from "@/types";

export const LogoutIcon = ({ color }: IconProps) => (
    <svg width={20} height={20} viewBox="0 0 7.5 7.5" fill="none" xmlns="http://www.w3.org/2000/svg">
        <g strokeWidth={0} />
        <g strokeLinecap="round" strokeLinejoin="round" />
        <path
            d="M4.688 1.25h.938a.625.625 0 0 1 .625.625v3.75a.625.625 0 0 1-.625.625h-.938M2.5 2.5 1.25 3.75m0 0L2.5 5M1.25 3.75H5"
            stroke={color}
            strokeWidth={0.4}
            strokeLinecap="round"
            strokeLinejoin="round"
        />
    </svg>
);
