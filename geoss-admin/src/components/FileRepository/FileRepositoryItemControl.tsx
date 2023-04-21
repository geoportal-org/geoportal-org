import { Button, Flex } from "@chakra-ui/react";
import { controlBtns } from "@/data";
import { ControlType, FileRepositoryItemControlProps } from "@/types";

export const FileRepositoryItemControl = ({
    item,
    handleItemDeleteClick,
    handleItemEditClick,
}: FileRepositoryItemControlProps) => {
    const onClickAction = (actionName: ControlType) => {
        switch (actionName) {
            case ControlType.EDIT:
                handleItemEditClick(item);
                break;
            case ControlType.DELETE:
                handleItemDeleteClick(item);
                break;
        }
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
            gap="1px"
        >
            {controlBtns.map((btn) => {
                const { icon, actionName, color } = btn;
                return (
                    <Button
                        key={actionName}
                        aria-label={actionName.toLowerCase()}
                        size="sm"
                        variant="ghost"
                        h="auto"
                        py={1}
                        px={2}
                        _active={{ color }}
                        _hover={{ color }}
                        onClick={() => onClickAction(actionName)}
                    >
                        {icon}
                    </Button>
                );
            })}
        </Flex>
    );
};
