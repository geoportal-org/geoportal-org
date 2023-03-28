import { IconProps } from "@/types";

export const CatalogIcon = ({ color }: IconProps) => (
    <svg
        width={20}
        height={20}
        viewBox="0 0 7.5 7.5"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
        stroke={color}
        strokeWidth={0}
    >
        <g />
        <g strokeLinecap="round" strokeLinejoin="round" />
        <g fill={color} stroke="none">
            <path d="M1.328 5.781H.859V2.343a.847.847 0 0 1 .838-.859h.753a.237.237 0 0 1 .181.078l.834 1.009H5a.847.847 0 0 1 .85.867v.156h-.469v-.156A.381.381 0 0 0 5 3.047H3.353a.231.231 0 0 1-.178-.084l-.834-1.01h-.644a.381.381 0 0 0-.369.391v3.438Z" />
            <path d="M5.35 6.016H1.094a.237.237 0 0 1-.2-.112.234.234 0 0 1 0-.231L1.95 3.642a.241.241 0 0 1 .209-.125h4.247a.237.237 0 0 1 .2.112.234.234 0 0 1 0 .231L5.55 5.891a.241.241 0 0 1-.2.125Zm-3.872-.469h3.731l.813-1.563H2.291l-.813 1.563Z" />
        </g>
    </svg>
);
