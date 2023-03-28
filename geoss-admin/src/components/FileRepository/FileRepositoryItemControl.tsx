import { Button, Flex } from "@chakra-ui/react";
import { controlBtns } from "@/data/fieRepositoryItemControls";
import { ControlType, FileRepositoryItemControlProps } from "@/types";

export const FileRepositoryItemControl = ({
    item,
    handleItemDeleteClick,
    handleItemEditClick,
}: FileRepositoryItemControlProps) => {
    const actionBtnClick = {
        [ControlType.EDIT]: () => handleItemEditClick(item),
        [ControlType.DELETE]: () => handleItemDeleteClick(item),
    };

    return (
        <Flex
            bg="brand.background"
            borderRadius="primary"
            boxShadow="controlPanel"
            justify="center"
            maxW="full"
            pos="absolute"
            right="0"
            top="-5px"
            gap="2px"
        >
            {controlBtns.map((btn) => {
                const { icon, actionName, color } = btn;
                return (
                    <Button
                        key={actionName}
                        aria-label={actionName}
                        size="sm"
                        variant="ghost"
                        h="auto"
                        py={1}
                        px={2.5}
                        _active={{ color }}
                        _hover={{ color }}
                        onClick={() => actionBtnClick[actionName]()}
                    >
                        {icon}
                    </Button>
                );
            })}
        </Flex>
    );
};
