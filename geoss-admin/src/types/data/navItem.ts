import { ComponentType } from "react";
import { IconProps } from "@/types";

export type NavItem = {
    titleId: string;
    href?: string;
    isExternal?: boolean;
    Icon: ComponentType<IconProps>;
    action?: {
        id: string;
        name: "logout";
    };
};

export type NavSection = {
    titleId: string;
    items: NavItem[];
};
