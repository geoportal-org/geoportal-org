import { useRouter } from "next/router";
import { Flex, SimpleGrid, Text } from "@chakra-ui/react";
import { TextContent } from "@/components";
import { FileRepositoryFileInfoProps } from "@/types";
import { getFileInformation } from "@/utils/helpers";

export const FileRepositoryFileInfo = ({ document }: FileRepositoryFileInfoProps) => {
    const router = useRouter();
    const fileInfo = getFileInformation(document, router.locale || "en");

    return (
        <Flex direction="column" w="full">
            {fileInfo.map((info, idx) => (
                <SimpleGrid
                    key={info.labelId}
                    spacingX="15px"
                    templateColumns={["1fr 1fr"]}
                    bg={idx % 2 !== 0 ? "brand.darkSoft" : "brand.mainLight"}
                    p="5px"
                    borderRadius="primary"
                >
                    <Text
                        as="span"
                        wordBreak="break-word"
                        textAlign="right"
                        display="flex"
                        alignItems="center"
                        justifyContent="flex-end"
                    >
                        <TextContent id={info.labelId} />
                    </Text>
                    <Text
                        as="span"
                        wordBreak="break-word"
                        fontWeight="bold"
                        textAlign="left"
                        display="flex"
                        alignItems="center"
                        justifyContent="flex-start"
                    >
                        {info.value || "-"}
                    </Text>
                </SimpleGrid>
            ))}
        </Flex>
    );
};
