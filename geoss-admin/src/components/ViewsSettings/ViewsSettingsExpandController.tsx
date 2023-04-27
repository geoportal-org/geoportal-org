import { IconButton } from "@chakra-ui/react";
import { ChevronDownIcon } from "@chakra-ui/icons";
import useFormatMsg from "@/utils/useFormatMsg";
import { ViewsSettingsExpandControllerProps } from "@/types";

export const ViewsSettingsExpandController = ({
    isExpanded,
    onExpand,
    msgId,
    isDisabled = false,
}: ViewsSettingsExpandControllerProps) => {
    const { translate } = useFormatMsg();

    return (
        <IconButton
            aria-label={translate(msgId)}
            onClick={onExpand}
            icon={
                <ChevronDownIcon
                    boxSize={5}
                    transform={isExpanded ? "rotate(-180deg)" : "rotate(0)"}
                    transitionDuration="normal"
                />
            }
            size="xs"
            variant="ghost"
            isDisabled={isDisabled}
            border="1px solid"
            borderColor="brand.dark"
            color="brand.dark"
            _hover={{}}
            _active={{}}
        />
    );
};
