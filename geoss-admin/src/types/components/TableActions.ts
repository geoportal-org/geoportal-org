import { TableActionsSource } from "../data/tableActionsType";
import { IContent, ILayer, IPage, ITutorialTag } from "../models";

export type TableActionsProps = {
    itemId: string | number;
    item: IPage | IContent | ILayer | ITutorialTag;
    actionsSource: TableActionsSource;
    onDeleteAction: () => void;
};
