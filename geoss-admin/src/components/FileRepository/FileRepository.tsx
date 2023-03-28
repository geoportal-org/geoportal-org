import { ReactNode, useEffect, useState } from "react";
import { Grid, Text, useDisclosure } from "@chakra-ui/react";
import { Loader, MainContent, Modal, SideBar, TextContent } from "@/components";
import { FileRepositoryFileInfo } from "./FileRepositoryFileInfo";
import { FileRepositoryBreadcrumb } from "./FileRepositoryBreadcrumb";
import { FileRepositoryItem } from "./FileRepositoryItem";
import { FileRepositoryAddFolder } from "./FileRepositoryAddFolder";
import { FileRepositoryService } from "@/services/api";
import { BreadcrumbItem, MainContentAction, ModalAction, ToastStatus } from "@/types";
import { IDocument, IFolder } from "@/types/models";
import { generatePath, getIdFromUrl, setDecisionModalActions } from "@/utils/helpers";
import useFormatMsg from "@/utils/useFormatMsg";
import useCustomToast from "@/utils/useCustomToast";

export const FileRepository = () => {
    const { isOpen: isOpenModal, onOpen: onOpenModal, onClose: onCloseModal } = useDisclosure();
    const { isOpen, onOpen, onClose } = useDisclosure();
    const { translate } = useFormatMsg();
    const { showToast } = useCustomToast();
    const [sideBarTitle, setSideBarTitle] = useState<string>();
    const [sideBarContent, setSideBarContent] = useState<ReactNode>();
    const [currentFolder, setCurrentFolder] = useState<number>(0);
    const [breadcrumb, setBreadcrumb] = useState<BreadcrumbItem[]>([
        { folderId: 0, folderTitle: translate("pages.file-repository.root-folder") },
    ]);
    const [foldersList, setFoldersList] = useState<IFolder[]>([]);
    const [documentsList, setDocumentsList] = useState<IDocument[]>([]);
    const [modalContent, setModalContent] = useState<{
        header: string;
        body: ReactNode;
        actions?: ModalAction[];
    }>();
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const currentFolders = foldersList.filter((folder) => folder.parentFolderId === currentFolder);
    const currentDocuments = documentsList.filter((document) => document.folderId === currentFolder);
    const isEmptyFolder = !currentFolders.length && !currentDocuments.length;

    useEffect(() => {
        getFileRepositoryItems();
    }, []);

    const getFileRepositoryItems = async () => {
        try {
            const {
                _embedded: { folder },
            } = await FileRepositoryService.getFoldersRoute();
            setFoldersList(() => folder);
            const {
                _embedded: { document },
            } = await FileRepositoryService.getDocumentsRoute();
            setDocumentsList(() => document);
        } catch (e) {
            console.error(e);
        } finally {
            setIsLoading(false);
        }
    };

    const headingActions: MainContentAction[] = [
        {
            titleId: "pages.file-repository.add-folder",
            onClick: () => handleAddFolderClick(),
        },
        {
            titleId: "pages.file-repository.add-file",
            onClick: () => handleAddFileClick(),
        },
    ];

    const handleAddFolderClick = () => {
        setSideBarTitle("pages.file-repository.add-folder");
        setSideBarContent(() => (
            <FileRepositoryAddFolder
                currFolder={currentFolder}
                path={generatePath(breadcrumb)}
                foldersList={foldersList}
                setFoldersList={setFoldersList}
            />
        ));
        onOpen();
    };

    const handleAddFileClick = () => {
        setSideBarTitle("pages.file-repository.add-file");
        setSideBarContent(null);
        onOpen();
    };

    const handleRepositoryItemClick = (item: IFolder | IDocument) => {
        const isFolderType = "parentFolderId" in item;
        isFolderType ? handleFolderClick(item) : handleFileClick(item);
    };

    const handleFolderClick = (item: IFolder) => {
        const folderId = +getIdFromUrl(item._links.self.href);
        const folderTitle = item.title;
        setCurrentFolder(folderId);
        setBreadcrumb([...breadcrumb, { folderId, folderTitle }]);
    };

    const handleFileClick = (item: IDocument) => {
        setModalContent({
            header: translate("pages.file-repository.file-info-modal-header"),
            body: <FileRepositoryFileInfo document={item} />,
        });
        onOpenModal();
    };

    const handleBreadcrumbClick = (folderId: number, index: number) => {
        setCurrentFolder(folderId);
        setBreadcrumb(breadcrumb.filter((_, idx) => idx <= index));
    };

    const handleItemDeleteClick = (item: IFolder | IDocument) => {
        const isFolderType = "parentFolderId" in item;
        const titleId = isFolderType
            ? "pages.file-repository.delete-folder-header"
            : "pages.file-repository.delete-file-header";
        const bodyId = isFolderType
            ? "pages.file-repository.delete-folder-body"
            : "pages.file-repository.delete-file-body";
        setModalContent({
            header: translate(titleId),
            body: (
                <Text py={4}>
                    <TextContent id={bodyId} title={item.title} />
                </Text>
            ),
            actions: setDecisionModalActions(
                () => deleteRepositoryItem(item),
                () => onCloseModal()
            ),
        });
        onOpenModal();
    };

    const deleteRepositoryItem = (item: IFolder | IDocument) => {
        const isFolderType = "parentFolderId" in item;
        const itemId = +getIdFromUrl(item._links.self.href);
        isFolderType ? deleteFolder(item.title, itemId) : deleteDocument(item.title, itemId);
    };

    const deleteFolder = async (title: string, id: number) => {
        try {
            await FileRepositoryService.deleteFolderRoute(id);
            setFoldersList((foldersList) =>
                foldersList.filter((folder) => +getIdFromUrl(folder._links.self.href) !== id)
            );
            showToast({
                title: "Folder deleted",
                description: `Folder ${title} (ID: ${id}) has been deleted`,
            });
            onCloseModal();
        } catch (e) {
            console.error(e);
            showToast({
                title: "Error occured",
                description: "Folder has not been deleted - try again",
                status: ToastStatus.ERROR,
            });
        }
    };

    const deleteDocument = (title: string, id: number) => {
        console.log(`Delete file ${title} (ID: ${id})`);
    };

    const handleItemEditClick = (item: IFolder | IDocument) => {
        const isFolderType = "parentFolderId" in item;
        setSideBarTitle(isFolderType ? "pages.file-repository.folder-editing" : "pages.file-repository.file-editing");
        setSideBarContent(() => (
            <FileRepositoryAddFolder
                folderId={+getIdFromUrl(item._links.self.href)}
                currFolder={currentFolder}
                path={generatePath(breadcrumb)}
                foldersList={foldersList}
                setFoldersList={setFoldersList}
            />
        ));
        onOpen();
    };

    if (isLoading) {
        return <Loader />;
    }

    return (
        <>
            <MainContent titleId="pages.file-repository.title" actions={headingActions}>
                <FileRepositoryBreadcrumb breadcrumb={breadcrumb} handleBreadcrumbClick={handleBreadcrumbClick} />
                {!isEmptyFolder ? (
                    <Grid templateColumns="repeat(auto-fill, minmax(min(100px, 100%), 1fr))" gap={6} m={1}>
                        {currentFolders.map((folder) => {
                            return (
                                <FileRepositoryItem
                                    key={getIdFromUrl(folder._links.self.href)}
                                    item={folder}
                                    handleRepositoryItemClick={handleRepositoryItemClick}
                                    handleItemDeleteClick={handleItemDeleteClick}
                                    handleItemEditClick={handleItemEditClick}
                                />
                            );
                        })}
                        {currentDocuments.map((document) => {
                            return (
                                <FileRepositoryItem
                                    key={getIdFromUrl(document._links.self.href)}
                                    item={document}
                                    handleRepositoryItemClick={handleRepositoryItemClick}
                                    handleItemDeleteClick={handleItemDeleteClick}
                                    handleItemEditClick={handleItemEditClick}
                                />
                            );
                        })}
                    </Grid>
                ) : (
                    <Text textAlign="center" fontSize="s">
                        <TextContent id="pages.file-repository.empty-folder" />
                    </Text>
                )}
            </MainContent>

            <Modal
                modalHeader={modalContent?.header}
                modalBody={modalContent?.body}
                actions={modalContent?.actions}
                onModalClose={onCloseModal}
                isModalOpen={isOpenModal}
            />

            <SideBar isOpen={isOpen} onClose={onClose} titleId={sideBarTitle} content={sideBarContent} />
        </>
    );
};
