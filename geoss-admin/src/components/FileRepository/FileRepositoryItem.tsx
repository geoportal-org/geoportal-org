import { Box, Button, Image, Text } from "@chakra-ui/react";
import { FileRepositoryItemControl } from "./FileRepositoryItemControl";
import useFormatMsg from "@/utils/useFormatMsg";
import { cutString } from "@/utils/helpers";
import { FileRepositoryItemProps } from "@/types";

export const FileRepositoryItem = ({
    handleRepositoryItemClick,
    handleItemDeleteClick,
    handleItemEditClick,
    item,
}: FileRepositoryItemProps) => {
    const { translate } = useFormatMsg();
    const isFolderType = "parentFolderId" in item;

    return (
        <Box pos="relative">
            <Button
                variant="geossFileItem"
                title={item.title}
                aria-label={translate(
                    isFolderType ? "pages.file-repository.open-folder" : "pages.file-repository.file-preview",
                    { title: item.title }
                )}
                onClick={() => handleRepositoryItemClick(item)}
            >
                <Image
                    src={isFolderType ? "/images/folder-icon.png" : "/images/file-icon.png"}
                    alt={translate(
                        isFolderType ? "pages.file-repository.folder-icon" : "pages.file-repository.file-icon",
                        { title: item.title }
                    )}
                />
                <Text w="full" whiteSpace="normal" textAlign="center">
                    {cutString(item.title, 25)}
                </Text>
            </Button>
            <FileRepositoryItemControl
                item={item}
                handleItemDeleteClick={handleItemDeleteClick}
                handleItemEditClick={handleItemEditClick}
            />
        </Box>
    );
};
