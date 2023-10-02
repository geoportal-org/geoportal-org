import { Box, Button, Image, Text } from "@chakra-ui/react";
import { FileRepositoryItemControl } from "./FileRepositoryItemControl";
import useFormatMsg from "@/utils/useFormatMsg";
import { cutString, getIdFromUrl } from "@/utils/helpers";
import { FileRepositoryItemProps, ToastStatus } from "@/types";
import { useDrag, useDrop } from "react-dnd";
import { FileRepositoryService } from "@/services/api";
import { IDocument } from "@/types/models";
import { IDocumentPatch, IFolder } from "@/types/models/fileRepository";

export const FileRepositoryItem = ({
    handleRepositoryItemClick,
    handleItemDeleteClick,
    handleItemEditClick,
    item,
    getFileRepositoryItems,
    showToast

}: FileRepositoryItemProps) => {
    const { translate } = useFormatMsg();
    const isFolderType = "parentFolderId" in item;
    const currentItem : IDocument | IFolder = item;

    const updateFileLocation = async (item: IDocument, folder: IFolder) => {
        const folderId = getIdFromUrl(folder._links.self.href)
        const fileId = getIdFromUrl(item._links.self.href)
        const fileData: IDocumentPatch = {
            title: item.title,
            fileName: item.fileName,
            extension: item.extension,
            path: item.path,
            folderId: Number(folderId),
        };
        try {
            await FileRepositoryService.updateFileTitle(Number(fileId), fileData);
            getFileRepositoryItems()
            showToast({
                title: "Folder deleted",
                description: `Item ${item.title} (ID: ${fileId}) has been moved to folder ${folder.title}`,
                status: ToastStatus.SUCCESS,
            });
        } catch (e) {
            showToast({
                title: "Error occured",
                description: "File has not been moved - try again",
                status: ToastStatus.ERROR,
            });
            console.log(e);
        }
    };

    const [{ isDragging }, drag, dragPreview] = useDrag(() => ({
        type: "FILE",
        collect: (monitor) => ({
            isDragging: monitor.isDragging(),
        }),
        item: item,
    }));

    const [{ canDrop, isOver }, drop] = useDrop(() => ({
        accept: "FILE",
        collect: (monitor) => ({
            isOver: monitor.isOver(),
            canDrop: monitor.canDrop(),
        }),
        drop: (item : IDocument) => {
            updateFileLocation(item, currentItem as IFolder);
        },
    }));

    return (
        <Box ref={isFolderType ? drop : drag} pos="relative">
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
