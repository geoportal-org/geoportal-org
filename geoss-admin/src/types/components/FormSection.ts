import { ReactNode } from "react";

export type FormSectionProps = {
    titleId: string;
    children: ReactNode;
    isLastSection: boolean;
};
