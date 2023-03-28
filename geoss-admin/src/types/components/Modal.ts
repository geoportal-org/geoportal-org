import { ReactNode } from "react";
import { ButtonVariant } from "@/types";

export type ModalAction = {
    btnVariant: ButtonVariant;
    color: string;
    label: string;
    onClick: () => void;
};

export type ModalProps = {
    actions?: ModalAction[];
    isModalOpen: boolean;
    modalBody: ReactNode;
    modalHeader?: ReactNode;
    onModalClose: () => void;
    size?: string;
};
