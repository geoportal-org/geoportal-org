import { DeleteIcon, EditIcon, ViewIcon } from "@chakra-ui/icons";
import { TableActionsSource, TableActionsType } from "@/types";

export const tableActionsBtns = [
    {
        icon: <ViewIcon />,
        actionName: TableActionsType.PREVIEW,
        color: "green",
        source: [TableActionsSource.WEBSITE],
    },
    {
        icon: <EditIcon />,
        actionName: TableActionsType.EDIT,
        color: "blue",
        source: [TableActionsSource.WEBSITE, TableActionsSource.PAGES],
    },
    {
        icon: <DeleteIcon />,
        actionName: TableActionsType.DELETE,
        color: "red",
        source: [TableActionsSource.WEBSITE, TableActionsSource.PAGES],
    },
];
