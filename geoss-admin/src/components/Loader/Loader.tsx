import { Flex, Spinner, Text } from "@chakra-ui/react";
import { TextContent } from "@/components";

export const Loader = () => (
    <Flex direction="column" gap="10px" align="center" justify="center" py="10px" minH="full" w="full">
        <Spinner size="xl" emptyColor="brand.darkSoft" speed="0.9s" color="brand.dark" thickness="4px" />
        <Text letterSpacing="0.5px">
            <TextContent id="loader.status" />
        </Text>
    </Flex>
);
