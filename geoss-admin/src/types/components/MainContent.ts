import { ReactNode } from "react";
import { PrimaryButtonProps } from "@/types";

type ActionTitle = {
    titleId: string;
};

type Action = Omit<PrimaryButtonProps, "children">;

export type MainContentAction = ActionTitle & Action;

export type MainContentHeaderProps = {
    titleId: string;
    actions?: MainContentAction[];
};

export type MainContentBodyProps = {
    children: ReactNode;
};

export type MainContentProps = {
    titleId?: string;
    children: ReactNode;
    actions?: MainContentAction[];
};
