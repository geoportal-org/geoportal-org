import NextLink from "next/link";
import { Box, Flex, Link, Text } from "@chakra-ui/react";
import { ArrowLeftIcon } from "@chakra-ui/icons";
import { PrimaryButton, TextContent } from "@/components";
import { MainContentHeaderProps } from "@/types";

export const MainContentHeader = ({ titleId: headerTitleId, actions, backPath }: MainContentHeaderProps) => {
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
                {backPath ? (
                    <Flex align="center" gap={2}>
                        <Link
                            as={NextLink}
                            alignItems="center"
                            bg="brand.dark"
                            borderRadius="50%"
                            color="brand.mainLight"
                            display="flex"
                            h={7}
                            href={backPath}
                            justifyContent="center"
                            w={7}
                        >
                            <ArrowLeftIcon />
                        </Link>
                        <Text as="h2" fontSize="lg" fontWeight="bold">
                            <TextContent id={headerTitleId} />
                        </Text>
                    </Flex>
                ) : (
                    <Text as="h2" fontSize="lg" fontWeight="bold">
                        <TextContent id={headerTitleId} />
                    </Text>
                )}
                {actions && (
                    <Flex gap="4px" wrap="wrap">
                        {renderHeaderActions()}
                    </Flex>
                )}
            </Flex>
        </Box>
    );
};
