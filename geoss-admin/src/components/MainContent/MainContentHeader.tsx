import { Box, Flex, Text } from "@chakra-ui/react";
import { PrimaryButton, TextContent } from "@/components";
import { MainContentHeaderProps } from "@/types";

export const MainContentHeader = ({ titleId: headerTitleId, actions }: MainContentHeaderProps) => {
    const renderHeaderActions = () =>
        actions?.map((action) => {
            const { titleId, ...rest } = action;
            return (
                <PrimaryButton {...rest} key={titleId}>
                    <TextContent id={titleId} />
                </PrimaryButton>
            );
        });

    return (
        <Box as="header" px={4} py={2}>
            <Flex
                align="center"
                borderBottom="1px solid"
                borderColor="brand.divider"
                gap={3}
                justify="space-between"
                py={2}
                wrap="wrap"
            >
                <Text as="h2" fontSize="lg" fontWeight="bold">
                    <TextContent id={headerTitleId} />
                </Text>
                {actions && (
                    <Flex gap="4px" wrap="wrap">
                        {renderHeaderActions()}
                    </Flex>
                )}
            </Flex>
        </Box>
    );
};
