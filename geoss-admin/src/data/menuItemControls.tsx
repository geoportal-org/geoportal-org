import { ControlType } from "@/types";
import { AddIcon, DeleteIcon, EditIcon } from "@chakra-ui/icons";

export const menuItemControls = [
    {
        icon: <AddIcon boxSize={4} />,
        actionName: ControlType.ADD,
        color: "#62CDFF",
        availableDepth: [0],
    },
    {
        icon: <EditIcon boxSize={4} />,
        actionName: ControlType.EDIT,
        color: "#62CDFF",
        availableDepth: [0, 1],
    },
    {
        icon: <DeleteIcon boxSize={4} />,
        actionName: ControlType.DELETE,
        color: "#EB455F",
        availableDepth: [0, 1],
    },
];
