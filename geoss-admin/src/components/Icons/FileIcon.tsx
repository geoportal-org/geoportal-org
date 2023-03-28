import { IconProps } from "@/types";

export const FileIcon = ({ color }: IconProps) => (
    <svg width={20} height={20} viewBox="0 0 7.5 7.5" xmlns="http://www.w3.org/2000/svg" fill={color}>
        <g strokeWidth={0} />
        <g strokeLinecap="round" strokeLinejoin="round" />
        <path d="M4.688 6.563h0.313v0.625H0.938V2.188h0.625v0.313H1.25v4.375h3.438zm0.938 -0.625H2.188V1.563h0.313V1.25H1.875v5h4.063v-0.625h-0.313zm0.125 -5.625L6.875 1.438V5.313H2.813V0.313zM6.563 1.875h-1.25V0.625h-2.188v4.375h3.438zm0 -0.409L5.722 0.625H5.625v0.938h0.938z" />
        <path fill="none" d="M0 0h7.5v7.5H0z" />
    </svg>
);
