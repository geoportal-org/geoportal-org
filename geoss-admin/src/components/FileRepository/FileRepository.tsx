import { ReactNode, useContext, useEffect, useRef, useState } from "react";
import { Grid, Text, useDisclosure } from "@chakra-ui/react";
import { Loader, MainContent, Modal, SideBar, TextContent, TextInfo } from "@/components";
import { FileRepositoryFileInfo } from "./FileRepositoryFileInfo";
import { FileRepositoryBreadcrumb } from "./FileRepositoryBreadcrumb";
import { FileRepositoryItem } from "./FileRepositoryItem";
import { FileRepositoryManageFolder } from "./FileRepositoryManageFolder";
import { FileRepositoryManageFile } from "./FileRepositoryManageFile";
import { FileRepositoryService } from "@/services/api";
import { generatePath, getIdFromUrl, setDecisionModalActions } from "@/utils/helpers";
import useFormatMsg from "@/utils/useFormatMsg";
import useCustomToast from "@/utils/useCustomToast";
import { initRepositoryPagination } from "@/data";
import { BreadcrumbItem, MainContentAction, ModalAction, ToastStatus } from "@/types";
import { IDocument, IFolder } from "@/types/models";
import { HTML5Backend } from "react-dnd-html5-backend";
import { DndProvider } from "react-dnd";
import { SiteContext, SiteContextValue } from "@/context/CurrentSiteContext";

export const FileRepository = () => {
    //siteId
    const { currentSiteId } = useContext<SiteContextValue>(SiteContext);

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
    const foldersListRef = useRef<IFolder[]>([]);
    foldersListRef.current = foldersList;
    const documentsListRef = useRef<IDocument[]>([]);
    documentsListRef.current = documentsList;
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
        setCurrentFolder(0);
        setBreadcrumb([{ folderId: 0, folderTitle: translate("pages.file-repository.root-folder") }]);
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [currentSiteId]);

    const getFileRepositoryItems = async () => {
        try {
            const {
                _embedded: { folder },
            } = await FileRepositoryService.getFoldersList({ ...initRepositoryPagination, siteId: currentSiteId });
            setFoldersList(() => folder);
            const {
                _embedded: { document },
            } = await FileRepositoryService.getDocumentsListBySiteId({
                ...initRepositoryPagination,
                siteId: currentSiteId,
            });
            setDocumentsList(() => document);
        } catch (e: any) {
            console.error(e);
            let msg = "";
            if (e.errorInfo?.length) {
                msg = JSON.parse(e.errorInfo).detail;
            } else {
                msg = e.errorInfo.message || e.errorInfo.errors[0].message;
            }
            showToast({
                title: translate("general.error"),
                description: `${msg || ""}`,
                status: ToastStatus.ERROR,
            });
        } finally {
            setIsLoading(false);
        }
    };

    const headingActions: MainContentAction[] = [
        {
            titleId: "pages.file-repository.add-folder",
            onClick: () => handleAddFolderClick(),
            disabled: isLoading,
        },
        {
            titleId: "pages.file-repository.add-file",
            onClick: () => handleAddFileClick(),
            disabled: isLoading,
        },
    ];

    const handleAddFolderClick = () => {
        setSideBarTitle("pages.file-repository.add-folder");
        setSideBarContent(() => (
            <FileRepositoryManageFolder
                currFolder={currentFolder}
                path={generatePath(breadcrumb)}
                foldersList={foldersListRef}
                setFoldersList={setFoldersList}
            />
        ));
        onOpen();
    };

    const handleAddFileClick = () => {
        setSideBarTitle("pages.file-repository.add-file");
        setSideBarContent(() => (
            <FileRepositoryManageFile
                currentFolder={currentFolder}
                path={generatePath(breadcrumb)}
                documentsList={documentsListRef}
                setDocumentsList={setDocumentsList}
            />
        ));
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
            await FileRepositoryService.deleteFolder(id);
            setFoldersList((foldersList) =>
                foldersList.filter((folder) => +getIdFromUrl(folder._links.self.href) !== id)
            );
            showToast({
                title: "Folder deleted",
                description: `Folder ${title} (ID: ${id}) has been deleted along with its contents`,
            });
            onCloseModal();
        } catch (e: any) {
            console.error(e);
            let msg = "";
            if (e.errorInfo?.length) {
                msg = JSON.parse(e.errorInfo).detail;
            } else {
                msg = e.errorInfo.message || e.errorInfo.errors[0].message;
            }
            showToast({
                title: translate("general.error"),
                description: `${msg || ""}`,
                status: ToastStatus.ERROR,
            });
            showErrorInfo("pages.file-repository.delete-folder-error");
        }
    };

    const deleteDocument = async (title: string, id: number) => {
        try {
            await FileRepositoryService.deleteFile(id);
            setDocumentsList((documentsList) =>
                documentsList.filter((file) => +getIdFromUrl(file._links.self.href) !== id)
            );
            showToast({
                title: "File deleted",
                description: `File ${title} (ID: ${id}) has been delted`,
            });
            onCloseModal();
        } catch (e: any) {
            console.log(e);
            showToast({
                title: "Error occured",
                description: "File has not been deleted - try again",
                status: ToastStatus.ERROR,
            });
        }
    };

    const handleItemEditClick = (item: IFolder | IDocument) => {
        const isFolderType = "parentFolderId" in item;
        setSideBarTitle(isFolderType ? "pages.file-repository.folder-editing" : "pages.file-repository.file-editing");
        setSideBarContent(() =>
            isFolderType ? (
                <FileRepositoryManageFolder
                    folderId={+getIdFromUrl(item._links.self.href)}
                    currFolder={currentFolder}
                    path={generatePath(breadcrumb)}
                    foldersList={foldersListRef}
                    setFoldersList={setFoldersList}
                    folder={item}
                />
            ) : (
                <FileRepositoryManageFile
                    currentFolder={currentFolder}
                    path={generatePath(breadcrumb)}
                    documentsList={documentsListRef}
                    setDocumentsList={setDocumentsList}
                    fileId={+getIdFromUrl(item._links.self.href)}
                    file={item}
                />
            )
        );
        onOpen();
    };

    const showErrorInfo = (messageId: string) =>
        showToast({
            title: translate("general.error"),
            description: translate(messageId),
            status: ToastStatus.ERROR,
        });

    return (
        <>
            <DndProvider backend={HTML5Backend}>
                <MainContent titleId="nav.contents.section.repository" actions={headingActions}>
                    {isLoading && <Loader />}
                    {!isLoading && (
                        <FileRepositoryBreadcrumb
                            breadcrumb={breadcrumb}
                            handleBreadcrumbClick={handleBreadcrumbClick}
                        />
                    )}
                    {!isLoading && !isEmptyFolder && (
                        <Grid templateColumns="repeat(auto-fill, minmax(min(90px, 100%), 1fr))" gap={6} m={1}>
                            {currentFolders.map((folder) => {
                                return (
                                    <FileRepositoryItem
                                        key={getIdFromUrl(folder._links.self.href)}
                                        item={folder}
                                        handleRepositoryItemClick={handleRepositoryItemClick}
                                        handleItemDeleteClick={handleItemDeleteClick}
                                        handleItemEditClick={handleItemEditClick}
                                        getFileRepositoryItems={getFileRepositoryItems}
                                        showToast={showToast}
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
                                        getFileRepositoryItems={getFileRepositoryItems}
                                        showToast={showToast}
                                    />
                                );
                            })}
                        </Grid>
                    )}
                    {!isLoading && isEmptyFolder && <TextInfo id="pages.file-repository.empty-folder" />}
                </MainContent>

                <Modal
                    modalHeader={modalContent?.header}
                    modalBody={modalContent?.body}
                    actions={modalContent?.actions}
                    onModalClose={onCloseModal}
                    isModalOpen={isOpenModal}
                />

                <SideBar isOpen={isOpen} onClose={onClose} titleId={sideBarTitle} content={sideBarContent} />
            </DndProvider>
        </>
    );
};
