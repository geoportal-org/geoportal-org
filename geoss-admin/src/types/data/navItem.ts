import { ComponentType } from "react";
import { IconProps } from "@/types";

export type NavItem = {
    titleId: string;
    href: string;
    Icon: ComponentType<IconProps>;
};

export type NavSection = {
    titleId: string;
    items: NavItem[];
};
