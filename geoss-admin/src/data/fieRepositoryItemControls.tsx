import { ControlType } from "@/types";
import { DeleteIcon, EditIcon } from "@chakra-ui/icons";

export const controlBtns = [
    {
        icon: <EditIcon />,
        actionName: ControlType.EDIT,
        color: "blue",
    },
    {
        icon: <DeleteIcon />,
        actionName: ControlType.DELETE,
        color: "red",
    },
];
