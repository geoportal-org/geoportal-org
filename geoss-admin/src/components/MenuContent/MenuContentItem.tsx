import { useEffect } from "react";
import { Button, Flex, Text } from "@chakra-ui/react";
import { DragHandleIcon } from "@chakra-ui/icons";
import useFormatMsg from "@/utils/useFormatMsg";
import { menuItemControls } from "@/data";
import { ControlType, MenuContentItemProps } from "@/types";

export const MenuContentItem = ({
    node,
    depth,
    handleRef,
    openAll,
    onAddAction,
    onDeleteAction,
    onEditAction,
}: MenuContentItemProps) => {
    const { translate } = useFormatMsg();

    useEffect(() => {
        openAll();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [node]);

    const onClickAction = (actionName: ControlType) => {
        switch (actionName) {
            case ControlType.ADD:
                onAddAction(+node.id);
                break;
            case ControlType.DELETE:
                onDeleteAction(node);
                break;
            case ControlType.EDIT:
                node.data && onEditAction(+node.parent, +node.id, node.data);
                break;
        }
    };

    return (
        <Flex
            p={4}
            pl={0}
            w="full"
            borderRadius="primary"
            bg={depth === 0 ? "brand.dark" : "brand.light"}
            color="brand.mainLight"
            fontWeight="bold"
            align="center"
            boxShadow="dialogShadow"
        >
            <Flex
                px={2}
                align="center"
                justify="center"
                ref={handleRef}
                cursor="grab"
                _hover={{ color: "#62CDFF" }}
                transitionDuration="normal"
                sx={{ touchAction: "none" }}
            >
                <DragHandleIcon boxSize={5} transitionDuration="normal" />
            </Flex>
            <Text flexGrow={1} textOverflow="ellipsis" whiteSpace="nowrap" overflow="hidden">
                {node.text}
            </Text>
            <Flex>
                {menuItemControls.map((btn) => {
                    const { icon, actionName, color, availableDepth } = btn;
                    return availableDepth.includes(depth) ? (
                        <Button
                            key={actionName}
                            aria-label={translate(`pages.menu.${actionName.toLowerCase()}`)}
                            size="sm"
                            variant="ghost"
                            h="auto"
                            py={0.5}
                            px={0}
                            _active={{ color }}
                            _hover={{ color }}
                            _disabled={{ color }}
                            onClick={() => onClickAction(actionName)}
                            cursor="pointer"
                        >
                            {icon}
                        </Button>
                    ) : null;
                })}
            </Flex>
        </Flex>
    );
};
