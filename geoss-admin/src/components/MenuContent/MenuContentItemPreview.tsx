import { Flex, Text } from "@chakra-ui/react";
import { MenuContentItemPreviewProps } from "@/types";

export const MenuContentItemPreview = ({ monitorProps }: MenuContentItemPreviewProps) => {
    const {
        item: { text },
    } = monitorProps;

    return (
        <Flex
            bg="brand.dark"
            color="brand.mainLight"
            p={1.5}
            w="max-content"
            minW="150px"
            maxW="250px"
            borderRadius="primary"
            align="center"
            justify="center"
        >
            <Text overflow="hidden" whiteSpace="nowrap" textOverflow="ellipsis">
                {text}
            </Text>
        </Flex>
    );
};
