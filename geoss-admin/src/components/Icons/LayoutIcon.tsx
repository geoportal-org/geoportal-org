import { IconProps } from "@/types";

export const LayoutIcon = ({ color }: IconProps) => (
    <svg width={20} height={20} viewBox="0 0 0.6 0.6" fill="none" xmlns="http://www.w3.org/2000/svg">
        <path
            d="M.075.15a.1.1 0 0 1 .1-.1h.25a.1.1 0 0 1 0 .2h-.25a.1.1 0 0 1-.1-.1Zm0 .25a.05.05 0 0 1 .05-.05H.2A.05.05 0 0 1 .25.4v.075a.05.05 0 0 1-.05.05H.125a.05.05 0 0 1-.05-.05V.4ZM.35.438a.088.088 0 1 1 .175 0 .088.088 0 0 1-.175 0Z"
            stroke={color}
            strokeWidth={0.03}
        />
    </svg>
);
