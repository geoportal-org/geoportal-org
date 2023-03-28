import { ReactNode } from "react";
import { ButtonType, ButtonVariant } from "@/types";

export type PrimaryButtonProps = {
    children: ReactNode;
    color?: string;
    disabled?: boolean;
    onClick?: () => void;
    type?: ButtonType;
    variant?: ButtonVariant;
};
