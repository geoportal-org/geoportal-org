import { TableActionsSource } from "../data/tableActionsType";
import { IContent, ILayer, IPage } from "../models";

export type TableActionsProps = {
    itemId: string | number;
    item: IPage | IContent | ILayer;
    actionsSource: TableActionsSource;
    onDeleteAction: () => void;
};
