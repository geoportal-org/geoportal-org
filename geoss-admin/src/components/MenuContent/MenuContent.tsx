import { Dispatch, ReactNode, SetStateAction, useEffect, useRef, useState } from "react";
import { FormikHelpers, FormikValues } from "formik";
import { Tree, NodeModel, MultiBackend, getBackendOptions, DndProvider, TreeMethods } from "@minoru/react-dnd-treeview";
import { DropOptions } from "@minoru/react-dnd-treeview/dist/types";
import { Box, Text, useDisclosure } from "@chakra-ui/react";
import { AddIcon } from "@chakra-ui/icons";
import { SideBar, MainContent, Loader, PrimaryButton, TextContent, Modal, LoadingErrorInfo } from "@/components";
import { MenuContentItem } from "./MenuContentItem";
import { MenuContentPlaceholder } from "./MenuContentPlaceholder";
import { MenuContentItemPreview } from "./MenuContentItemPreview";
import { MenuContentManage } from "./MenuContentManage";
import { MenuService } from "@/services/api";
import {
    areObjectsArraysEqual,
    getIdFromUrl,
    setDecisionModalActions,
    setExistingFormValues,
    sortMenuList,
} from "@/utils/helpers";
import useCustomToast from "@/utils/useCustomToast";
import useFormatMsg from "@/utils/useFormatMsg";
import { ModalAction, MovedItemData, MovedItemInfo, ToastStatus } from "@/types";
import { IMenuItem, IMenuItemData } from "@/types/models";
import { addMenuItemForm } from "@/data/forms";
import styles from "./MenuContent.module.css";

export const MenuContent = () => {
    const [isLoading, setIsLoading] = useState(true);
    const [isError, setIsError] = useState(false);
    const [menuList, setMenuList] = useState<NodeModel<IMenuItem>[]>([]);
    const [sideBarTitle, setSideBarTitle] = useState<string>();
    const [sideBarContent, setSideBarContent] = useState<ReactNode>();
    const [modalContent, setModalContent] = useState<{
        header: string;
        body: ReactNode;
        actions?: ModalAction[];
    }>();
    const itemTreeRef = useRef<TreeMethods>(null);
    const menuListRef = useRef<NodeModel<IMenuItem>[]>([]);
    menuListRef.current = menuList;
    const { isOpen, onOpen, onClose } = useDisclosure();
    const { isOpen: isOpenModal, onOpen: onOpenModal, onClose: onCloseModal } = useDisclosure();
    const { showToast } = useCustomToast();
    const { translate } = useFormatMsg();

    useEffect(() => {
        getMenuList();
    }, []);

    const getMenuList = async () => {
        try {
            const {
                _embedded: { menu: fullMenu },
            } = await MenuService.getMenuList();
            const menuStructure = sortMenuList(fullMenu);
            setMenuList(() => menuStructure);
        } catch (e) {
            setIsError(true);
        } finally {
            setIsLoading(false);
        }
    };

    const handleMenuItemDrop = async (newTree: NodeModel<IMenuItem>[], data: DropOptions) => {
        const updatePromises = [];
        const isMenuChanged = !areObjectsArraysEqual(menuList, newTree);
        const { dragSourceId: updatedItemId } = data;

        if (!isMenuChanged || updatedItemId === undefined) {
            return;
        }

        const updatedItemData = getMovedItemUpdatedData(newTree, data);
        const updatedNewTree: NodeModel<IMenuItem>[] = newTree.map((item) =>
            item.data && +item.id === updatedItemId
                ? {
                      ...item,
                      data: {
                          ...item.data,
                          ...updatedItemData,
                      },
                  }
                : item
        );

        updatePromises.push(MenuService.updateMenuItem(updatedItemData, +updatedItemId));
        updateItemsPriority(newTree, updatedNewTree, data, updatePromises);

        const formattedTree = updatedNewTree.map((item) => ({ ...item, droppable: item.parent === 0 }));

        await Promise.all(updatePromises)
            .then(() => setMenuList(formattedTree))
            .catch((e) => handlePriorityChangeError());
    };

    const getMovedItemUpdatedData = (itemTree: NodeModel<IMenuItem>[], dropData: DropOptions): MovedItemData => {
        const { currentLevelId, currentParentId, currentPriority, isLevelChange, isParentChange } = getMovedItemInfo(
            itemTree,
            dropData
        );
        return {
            ...(isLevelChange && { levelId: currentLevelId }),
            ...(isParentChange && { parentMenuId: currentParentId }),
            priority: currentPriority,
        };
    };

    const getMovedItemInfo = (itemTree: NodeModel<IMenuItem>[], dropData: DropOptions): MovedItemInfo => {
        const { dragSourceId: updatedItemId, dropTarget, dragSource } = dropData;

        const currentLevelId = !dropTarget ? 0 : 1;
        const prevParentId = dragSource ? +dragSource?.parent : undefined;
        const currentParentId = !dropTarget ? 0 : +dropTarget.id;
        const currentPriority =
            itemTree
                .filter((item) => +item.parent === currentParentId)
                .findIndex((item) => +item.id === updatedItemId) + 1;
        const isLevelChange =
            prevParentId !== undefined &&
            ((+prevParentId === 0 && currentParentId !== 0) || (+prevParentId !== 0 && currentParentId === 0));
        const isParentChange = currentParentId !== prevParentId;

        return { currentLevelId, prevParentId, currentParentId, currentPriority, isLevelChange, isParentChange };
    };

    const updateItemsPriority = (
        newTree: NodeModel<IMenuItem>[],
        updatedNewTree: NodeModel<IMenuItem>[],
        dropData: DropOptions,
        updatePromises: Promise<any>[]
    ) => {
        const { currentParentId, prevParentId, isParentChange } = getMovedItemInfo(newTree, dropData);
        setNewItemsPriority(updatedNewTree, currentParentId, updatePromises);
        isParentChange &&
            prevParentId !== undefined &&
            setNewItemsPriority(updatedNewTree, prevParentId, updatePromises);
    };

    const setNewItemsPriority = (
        updatedNewTree: NodeModel<IMenuItem>[],
        parentId: number,
        updatePromises: Promise<any>[]
    ) => {
        updatedNewTree
            .filter((item) => +item.parent === parentId)
            .forEach((item, idx) => {
                if (!item.data) {
                    return;
                }
                const currentItemPriority = idx + 1;
                const itemId = +item.id;
                const isItemChanged = item.data.priority !== currentItemPriority;
                if (isItemChanged) {
                    item.data.priority = currentItemPriority;
                    updatePromises.push(MenuService.updateMenuItem({ priority: currentItemPriority }, itemId));
                }
            });
    };

    const handlePriorityChangeError = () => {
        const prevMenuVersion = structuredClone(menuList);
        setMenuList(prevMenuVersion);
        showErrorInfo("pages.menu.update-error");
    };

    const openAllMenuItems = () => itemTreeRef.current?.openAll();

    const onAddAction = (parentMenuId: number) => {
        setSideBarTitle(parentMenuId === 0 ? "pages.menu.add-item" : "pages.menu.add-subitem");
        setSideBarContent(() => <MenuContentManage parentMenuId={parentMenuId} manageMenuItem={manageMenuItem} />);
        onOpen();
    };

    const onEditAction = (parentMenuId: number, menuItemId: number) => {
        setSideBarTitle("pages.menu.edit");
        setSideBarContent(() => (
            <MenuContentManage parentMenuId={parentMenuId} manageMenuItem={manageMenuItem} menuItemId={menuItemId} />
        ));
        onOpen();
    };

    const onDeleteAction = (menuItem: NodeModel<IMenuItem>) => {
        const title = menuItem.data ? menuItem.data.title : "";
        setModalContent({
            header: translate("pages.menu.delete-title"),
            body: (
                <Text py={4}>
                    <TextContent id="pages.menu.delete-info" title={title} />
                </Text>
            ),
            actions: setDecisionModalActions(
                () => deleteMenuItem(menuItem),
                () => onCloseModal()
            ),
        });
        onOpenModal();
    };

    const deleteMenuItem = (menuItem: NodeModel<IMenuItem>) => {
        if (!menuItem.data) {
            return;
        }

        const updatePromises: Promise<IMenuItem>[] = [];
        const { id: itemId, parent: parentMenuId } = menuItem;
        const newItemList = structuredClone(menuList);
        const itemChildren = menuList.filter((item) => item.parent === itemId);
        const itemIndex = newItemList.findIndex((item) => item.id === itemId);
        newItemList.forEach((item, idx) => {
            if (idx > itemIndex && item.data && item.parent === parentMenuId) {
                item.data.priority--;
                updatePromises.push(MenuService.updateMenuItem({ priority: item.data.priority }, +item.id));
            }
        });
        const itemsIdToDelete = itemChildren.concat(menuItem).map((item) => +item.id);
        deleteMenuItems(itemsIdToDelete, newItemList, menuItem.data.title, updatePromises);
    };

    const deleteMenuItems = async (
        itemsIds: number[],
        itemsList: NodeModel<IMenuItem>[],
        itemTitle: string,
        updatePromises: Promise<IMenuItem>[]
    ) => {
        const deletePromises = itemsIds.map((id) => MenuService.deleteMenuItem(id));
        await Promise.all(deletePromises.concat(updatePromises))
            .then(() => handleItemsDelete(itemsIds, itemsList, itemTitle))
            .catch((e) => showErrorInfo("pages.menu.delete-error"));
    };

    const handleItemsDelete = (itemsIds: number[], itemsList: NodeModel<IMenuItem>[], title: string) => {
        setMenuList(() => itemsList.filter((item) => !itemsIds.includes(+item.id)));
        showToast({
            title: translate("general.deleted"),
            description: translate(`pages.menu.deleted-${itemsIds.length > 1 ? "multi" : "single"}`, { title }),
            status: ToastStatus.ERROR,
        });
        onCloseModal();
    };

    const manageMenuItem = async (
        parentMenuId: number,
        values: FormikValues,
        actions: FormikHelpers<FormikValues>,
        setInitValues: Dispatch<SetStateAction<FormikValues>>,
        menuItemId?: number
    ) =>
        menuItemId !== undefined
            ? updateMenuItem(values, menuItemId, setInitValues)
            : createMenuItem(parentMenuId, values, actions);

    const updateMenuItem = async (
        values: FormikValues,
        menuItemId: number,
        setInitValues: Dispatch<SetStateAction<FormikValues>>
    ) => {
        const menuItemData = getUpdatedItemData(values);
        try {
            const updatedMenuItem = await MenuService.updateMenuItem(menuItemData, menuItemId);
            setMenuList((menuList) =>
                menuList.map((menuItem) =>
                    menuItem.data && +getIdFromUrl(menuItem.data._links.self.href) === menuItemId
                        ? { ...menuItem, text: updatedMenuItem.title, data: updatedMenuItem }
                        : menuItem
                )
            );
            setInitValues(setExistingFormValues(addMenuItemForm, values));
            showToast({
                title: translate("general.updated"),
                description: translate("pages.menu.updated-item", { title: updatedMenuItem.title }),
            });
        } catch (e) {
            showErrorInfo("pages.menu.update-item-error");
        }
    };

    const createMenuItem = async (parentMenuId: number, values: FormikValues, actions: FormikHelpers<FormikValues>) => {
        const menuItemData = getNewItemData(parentMenuId, values);
        try {
            const addedMenuItem = await MenuService.createMenuItem(menuItemData);
            const newMenuItem = createMenuListItem(addedMenuItem, parentMenuId);
            addNewMenuItem(newMenuItem);
            actions.resetForm();
            showToast({
                title: translate("general.created"),
                description: translate("pages.menu.created", { title: addedMenuItem.title }),
            });
        } catch (e) {
            showErrorInfo("pages.menu.create-error");
        }
    };

    const getUpdatedItemData = (values: FormikValues): Omit<IMenuItemData, "levelId" | "priority" | "parentMenuId"> => {
        const { title, imageTitle, imageSource, url } = values;
        return { title, imageTitle, imageSource, url };
    };

    const getNewItemData = (parentMenuId: number, values: FormikValues): IMenuItemData => {
        const { title, imageTitle, imageSource, url } = values;
        const levelId = parentMenuId === 0 ? 0 : 1;
        const priority = getNewItemPriority(parentMenuId);
        return { title, imageTitle, imageSource, url, levelId, parentMenuId, priority };
    };

    const getNewItemPriority = (parentMenuId: number): number =>
        menuListRef.current.filter((item) => item.parent === parentMenuId).length + 1;

    const createMenuListItem = (menuItem: IMenuItem, parent: number): NodeModel<IMenuItem> => {
        const id = +getIdFromUrl(menuItem._links.self.href);
        return {
            id,
            parent,
            text: menuItem.title,
            droppable: parent === 0,
            data: menuItem,
        };
    };

    const addNewMenuItem = (menuItem: NodeModel<IMenuItem>) => {
        const { parent: parentMenuId } = menuItem;
        if (parentMenuId === 0) {
            return setMenuList([...menuListRef.current, menuItem]);
        }
        const newMenuList = structuredClone(menuListRef.current);
        const isSubitems = !!newMenuList.filter((item) => item.parent === parentMenuId).length;
        const itemIndex = newMenuList
            .reverse()
            .findIndex((item) => (isSubitems ? item.parent === parentMenuId : item.id === parentMenuId));
        newMenuList.splice(itemIndex, 0, menuItem);
        setMenuList(() => [...newMenuList.reverse()]);
    };

    const showErrorInfo = (messageId: string) =>
        showToast({
            title: translate("general.error"),
            description: translate(messageId),
            status: ToastStatus.ERROR,
        });

    if (isLoading) {
        return <Loader />;
    }

    return (
        <>
            <MainContent titleId="pages.menu.title">
                {!isError ? (
                    <>
                        <Box py={1}>
                            <PrimaryButton onClick={() => onAddAction(0)}>
                                <AddIcon boxSize={3} mr={2} /> <TextContent id="pages.menu.add" />
                            </PrimaryButton>
                        </Box>
                        <DndProvider backend={MultiBackend} options={getBackendOptions()}>
                            <Tree
                                ref={itemTreeRef}
                                tree={menuList}
                                rootId={0}
                                sort={false}
                                insertDroppableFirst={false}
                                dropTargetOffset={10}
                                canDrop={(tree, { dragSource, dropTargetId }) => {
                                    if (tree.some((item) => item.parent === dragSource?.id) && dropTargetId !== 0) {
                                        return false;
                                    }
                                    if (dragSource?.parent === dropTargetId) {
                                        return true;
                                    }
                                }}
                                render={(node, options) => (
                                    <MenuContentItem
                                        node={node}
                                        {...options}
                                        openAll={openAllMenuItems}
                                        onAddAction={onAddAction}
                                        onDeleteAction={onDeleteAction}
                                        onEditAction={onEditAction}
                                    />
                                )}
                                placeholderRender={() => <MenuContentPlaceholder />}
                                dragPreviewRender={(monitorProps) => (
                                    <MenuContentItemPreview monitorProps={monitorProps} />
                                )}
                                onDrop={handleMenuItemDrop}
                                initialOpen={true}
                                classes={{
                                    root: styles.list,
                                    draggingSource: styles.list__dragging,
                                    placeholder: styles.list__placeholder,
                                    listItem: styles.list__item,
                                    container: styles.list__inner,
                                }}
                            />
                        </DndProvider>
                    </>
                ) : (
                    <LoadingErrorInfo />
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
