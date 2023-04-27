import { AddIcon, DeleteIcon, EditIcon, ViewIcon } from "@chakra-ui/icons";
import { TableActionsSource, TableActionsType } from "@/types";

export const tableActionsBtns = [
    // hidden content preview
    /*{
        icon: <ViewIcon />,
        actionName: TableActionsType.PREVIEW,
        color: "green",
        source: [TableActionsSource.WEBSITE],
    },*/
    {
        icon: <AddIcon />,
        actionName: TableActionsType.ADD,
        color: "green",
        source: [TableActionsSource.VIEWS],
        isMainRowOnlyAction: true,
    },
    {
        icon: <EditIcon />,
        actionName: TableActionsType.EDIT,
        color: "blue",
        source: [
            TableActionsSource.WEBSITE,
            TableActionsSource.PAGES,
            TableActionsSource.LAYER,
            TableActionsSource.TUTORIAL,
            TableActionsSource.VIEWS,
        ],
    },
    {
        icon: <DeleteIcon />,
        actionName: TableActionsType.DELETE,
        color: "red",
        source: [
            TableActionsSource.WEBSITE,
            TableActionsSource.PAGES,
            TableActionsSource.LAYER,
            TableActionsSource.TUTORIAL,
            TableActionsSource.VIEWS,
        ],
    },
];
