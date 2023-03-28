import { ReactNode } from "react";

export type SideBarProps = {
    isOpen: boolean;
    onClose: () => void;
    titleId?: string;
    content: ReactNode;
};
