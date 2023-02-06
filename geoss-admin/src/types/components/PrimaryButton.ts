import { ReactNode } from "react";
import { ButtonType, ButtonVariant } from "../data";

export type PrimaryButtonProps = {
    children: ReactNode;
    color?: string;
    disabled?: boolean;
    onClick?: () => void;
    type?: ButtonType;
    variant?: ButtonVariant;
};
