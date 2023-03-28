import { MainContentBody } from "./MainContentBody";
import { MainContentHeader } from "./MainContentHeader";
import { MainContentProps } from "@/types";

export const MainContent = ({ titleId, children, actions }: MainContentProps) => (
    <>
        {titleId && <MainContentHeader titleId={titleId} actions={actions} />}
        <MainContentBody>{children}</MainContentBody>
    </>
);
