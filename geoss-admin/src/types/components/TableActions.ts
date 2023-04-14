import { TableActionsSource } from "../data/tableActionsType";
import { IContent, IPage } from "../models";

export type TableActionsProps = {
    itemId: string;
    item: IPage | IContent;
    actionsSource: TableActionsSource;
    onDeleteAction: () => void;
};
