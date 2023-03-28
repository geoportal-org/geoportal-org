import { NavSection, NavItem } from "@/types";

export type NavProps = {
    isOpen: boolean;
    onClose: () => void;
};

export type NavSectionProps = {
    navSection: NavSection;
    onNavClose: () => void;
};

export type NavItemProps = {
    item: NavItem;
    onNavClose: () => void;
};
