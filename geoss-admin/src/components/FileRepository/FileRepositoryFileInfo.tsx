import { useIntl } from "react-intl";
import { Flex, SimpleGrid, Text, Link, IconButton } from "@chakra-ui/react";
import { CopyIcon } from "@chakra-ui/icons";
import { TextContent } from "@/components";
import useFormatMsg from "@/utils/useFormatMsg";
import { getFileInformation } from "@/utils/helpers";
import { FileRepositoryFileInfoProps } from "@/types";
import { defaultUsedLang } from "@/data";

export const FileRepositoryFileInfo = ({ document }: FileRepositoryFileInfoProps) => {
    const { locale } = useIntl();
    const { translate } = useFormatMsg();
    const fileInfo = getFileInformation(document, locale || defaultUsedLang);

    const copyText = async (text: string) => {
        try {
            await navigator.clipboard.writeText(text);
        } catch (err) {
            console.log(err);
        }
    };

    return (
        <Flex direction="column" w="full">
            {fileInfo.map(({ labelId, value, isLink }, idx) => (
                <SimpleGrid
                    key={labelId}
                    spacingX="15px"
                    templateColumns={["1fr 1fr"]}
                    bg={idx % 2 !== 0 ? "brand.darkSoft" : "brand.mainLight"}
                    p="5px"
                    borderRadius="primary"
                    alignItems="center"
                >
                    <Text
                        as="span"
                        wordBreak="break-word"
                        textAlign="right"
                        display="flex"
                        alignItems="center"
                        justifyContent="flex-end"
                    >
                        {!!isLink && !!navigator && !!navigator.clipboard && !!navigator.clipboard.writeText && (
                            <IconButton
                                onClick={() => copyText(value?.toString() || '')}
                                aria-label={translate("pages.file-repository.copy")}
                                size="sm"
                                h="auto"
                                minW="auto"
                                mr={1}
                                py={0.5}
                                px={0.5}
                                variant="ghost"
                                _active={{ color: "blue" }}
                                _hover={{ color: "blue" }}
                                icon={<CopyIcon boxSize={4} />}
                            />
                        )}
                        <TextContent id={labelId} />
                    </Text>
                    {!isLink ? (
                        <Text
                            as="span"
                            whiteSpace="nowrap"
                            fontWeight="bold"
                            textAlign="left"
                            overflow="hidden"
                            textOverflow="ellipsis"
                        >
                            {value || "-"}
                        </Text>
                    ) : (
                        <Link
                            href={value?.toString() || ''}
                            isExternal
                            overflow="hidden"
                            textOverflow="ellipsis"
                            whiteSpace="nowrap"
                            fontWeight="bold"
                        >
                            {value}
                        </Link>
                    )}
                </SimpleGrid>
            ))}
        </Flex>
    );
};
