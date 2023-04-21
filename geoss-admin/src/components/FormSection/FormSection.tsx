import { Flex, Text } from "@chakra-ui/react";
import { TextContent } from "@/components";
import { FormSectionProps } from "@/types";

export const FormSection = ({ children, titleId, isLastSection }: FormSectionProps) => (
    <Flex direction="column" mb={isLastSection ? 6 : 10}>
        <Text
            bg="brand.darkSoft"
            borderColor="brand.dark"
            borderRadius="primary"
            color="brand.dark"
            fontSize="s"
            fontWeight="bold"
            mb={3}
            p={2}
        >
            <TextContent id={titleId} />
        </Text>
        <Flex direction="column" gap={6} w="calc(100% - 16px)" margin="0 auto">
            {children}
        </Flex>
    </Flex>
);
