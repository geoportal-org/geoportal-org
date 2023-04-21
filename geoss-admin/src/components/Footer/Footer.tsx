import { Flex, Text } from "@chakra-ui/react";

export const Footer = () => (
    <Flex
        as="footer"
        fontSize="s"
        bg="brand.dark"
        color="brand.mainLight"
        px={2.5}
        py={3}
        h="headerHeight"
        justify="flex-end"
        align="center"
    >
        <Text>&copy; {new Date().getFullYear()}</Text>
    </Flex>
);
