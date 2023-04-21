import { MainContentBody } from "./MainContentBody";
import { MainContentHeader } from "./MainContentHeader";
import { MainContentProps } from "@/types";

export const MainContent = ({ titleId, children, actions, backPath }: MainContentProps) => (
    <>
        {titleId && <MainContentHeader titleId={titleId} actions={actions} backPath={backPath} />}
        <MainContentBody>{children}</MainContentBody>
    </>
);
