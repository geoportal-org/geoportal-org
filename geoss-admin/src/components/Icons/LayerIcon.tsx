import { IconProps } from "@/types";

export const LayerIcon = ({ color }: IconProps) => (
    <svg width={20} height={20} viewBox="0 0 7.5 7.5" fill="none" xmlns="http://www.w3.org/2000/svg" stroke={color}>
        <g strokeWidth={0} />
        <g strokeLinecap="round" strokeLinejoin="round" />
        <g strokeWidth={0.469} strokeLinecap="round" strokeLinejoin="round">
            <path d="m4.066.913 1.843.818c.532.235.532.622 0 .857l-1.843.818c-.21.094-.553.094-.763 0l-1.844-.818c-.531-.235-.531-.622 0-.857L3.303.913c.21-.094.553-.094.763 0Z" />
            <path d="M.938 3.438c0 .262.196.565.437.671l2.122.944a.62.62 0 0 0 .506 0l2.122-.944a.8.8 0 0 0 .438-.671" />
            <path d="M.938 5c0 .291.171.553.437.672l2.122.944a.62.62 0 0 0 .506 0l2.122-.944A.736.736 0 0 0 6.563 5" />
        </g>
    </svg>
);
