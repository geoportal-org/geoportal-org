import { TableActionsSource, FormattedView } from "@/types";
import { IContent, ILayer, IPage, ITutorialTag } from "@/types/models";

export type TableActionsProps = {
    itemId: string | number;
    item: IPage | IContent | ILayer | ITutorialTag | FormattedView;
    actionsSource: TableActionsSource;
    onDeleteAction: () => void;
    disabled: boolean;
};
