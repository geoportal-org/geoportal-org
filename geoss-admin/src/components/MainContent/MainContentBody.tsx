import { Flex } from "@chakra-ui/react";
import { scrollbarStyles } from "@/theme/commons";
import { MainContentBodyProps } from "@/types";

export const MainContentBody = ({ children }: MainContentBodyProps) => (
    <Flex flexGrow="1" p={4} overflow="hidden" w="full">
        <Flex
            direction="column"
            h="full"
            minH="full"
            overflowY="auto"
            overflowX="hidden"
            p={0}
            px={1}
            sx={scrollbarStyles}
            w="full"
        >
            {children}
        </Flex>
    </Flex>
);
