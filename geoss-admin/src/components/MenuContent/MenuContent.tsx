import { Dispatch, ReactNode, SetStateAction, useEffect, useRef, useState } from "react";
import { FormikHelpers, FormikValues } from "formik";
import { Tree, NodeModel, MultiBackend, getBackendOptions, DndProvider, TreeMethods } from "@minoru/react-dnd-treeview";
import { DropOptions } from "@minoru/react-dnd-treeview/dist/types";
import { Text, useDisclosure } from "@chakra-ui/react";
import { SideBar, MainContent, Loader, TextContent, Modal, TextInfo } from "@/components";
import { MenuContentItem } from "./MenuContentItem";
import { MenuContentPlaceholder } from "./MenuContentPlaceholder";
import { MenuContentItemPreview } from "./MenuContentItemPreview";
import { MenuContentManage } from "./MenuContentManage";
import { MenuService } from "@/services/api";
import {
    areObjectsArraysEqual,
    getIdFromUrl,
    isTranslatedValueAdded,
    setDecisionModalActions,
    setExistingFormValues,
    sortMenuList,
} from "@/utils/helpers";
import useCustomToast from "@/utils/useCustomToast";
import useFormatMsg from "@/utils/useFormatMsg";
import { LocaleNames, ModalAction, MovedItemData, MovedItemInfo, ToastStatus } from "@/types";
import { IMenuItem, IMenuItemData } from "@/types/models";
import { defaultUsedLang, initMenuPagination } from "@/data";
import { addChildMenuItemForm, addMenuItemForm } from "@/data/forms";
import styles from "./MenuContent.module.css";
import { useIntl } from "react-intl";

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
    const { locale } = useIntl();

    useEffect(() => {
        getMenuList();
    }, []);

    const getMenuList = async () => {
        try {
            const {
                _embedded: { menu: fullMenu },
            } = await MenuService.getMenuList(initMenuPagination);
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
        const isMainMenuItem = parentMenuId === 0;
        setSideBarTitle(isMainMenuItem ? "pages.menu.add-item" : "pages.menu.add-subitem");
        setSideBarContent(() => (
            <MenuContentManage
                parentMenuId={parentMenuId}
                manageMenuItem={manageMenuItem}
                isMainMenuItem={isMainMenuItem}
            />
        ));
        onOpen();
    };

    const onEditAction = (parentMenuId: number, menuItemId: number, menuItem: IMenuItem) => {
        const isMainMenuItem = parentMenuId === 0;
        setSideBarTitle(isMainMenuItem ? "pages.menu.edit" : "pages.menu.edit-subitem");
        setSideBarContent(() => (
            <MenuContentManage
                parentMenuId={parentMenuId}
                manageMenuItem={manageMenuItem}
                menuItemId={menuItemId}
                isMainMenuItem={isMainMenuItem}
                menuItem={menuItem}
            />
        ));
        onOpen();
    };

    const onDeleteAction = (menuItem: NodeModel<IMenuItem>) => {
        const title = menuItem.data ? menuItem.data.title[(locale as LocaleNames) || defaultUsedLang] : "";
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
        deleteMenuItems(
            itemsIdToDelete,
            newItemList,
            menuItem.data.title[(locale as LocaleNames) || defaultUsedLang],
            updatePromises
        );
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
        currentLang: LocaleNames,
        menuItemId?: number
    ) =>
        menuItemId !== undefined
            ? await updateMenuItem(values, menuItemId, setInitValues, parentMenuId, currentLang)
            : await createMenuItem(parentMenuId, values, actions, currentLang);

    const updateMenuItem = async (
        values: FormikValues,
        menuItemId: number,
        setInitValues: Dispatch<SetStateAction<FormikValues>>,
        parentMenuId: number,
        currentLang: LocaleNames
    ) => {
        const isMainMenuItem = parentMenuId === 0;
        const menuItemData = getUpdatedItemData(values, isMainMenuItem, currentLang);

        try {
            const updatedMenuItem = await MenuService.updateMenuItem(menuItemData, menuItemId);
            setMenuList((menuList) =>
                menuList.map((menuItem) =>
                    menuItem.data && +getIdFromUrl(menuItem.data._links.self.href) === menuItemId
                        ? {
                              ...menuItem,
                              text: updatedMenuItem.title[(locale as LocaleNames) || defaultUsedLang],
                              data: updatedMenuItem,
                          }
                        : menuItem
                )
            );
            setInitValues(setExistingFormValues(isMainMenuItem ? addMenuItemForm : addChildMenuItemForm, menuItemData));
            showToast({
                title: translate("general.updated"),
                description: translate("pages.menu.updated-item", {
                    title: updatedMenuItem.title[(locale as LocaleNames) || defaultUsedLang],
                }),
            });
        } catch (e) {
            showErrorInfo("pages.menu.update-item-error");
        }
    };

    const createMenuItem = async (
        parentMenuId: number,
        values: FormikValues,
        actions: FormikHelpers<FormikValues>,
        currentLang: LocaleNames
    ) => {
        const isMainMenuItem = parentMenuId === 0;
        const menuItemData = getNewItemData(parentMenuId, values, currentLang, isMainMenuItem);

        try {
            const addedMenuItem = await MenuService.createMenuItem(menuItemData);
            const newMenuItem = createMenuListItem(addedMenuItem, parentMenuId);
            addNewMenuItem(newMenuItem);
            actions.resetForm();
            showToast({
                title: translate("general.created"),
                description: translate("pages.menu.created", {
                    title: addedMenuItem.title[(locale as LocaleNames) || defaultUsedLang],
                }),
            });
        } catch (e) {
            showErrorInfo("pages.menu.create-error");
        }
    };

    const getUpdatedItemData = (
        values: FormikValues,
        isMainMenuItem: boolean,
        currentLang: LocaleNames
    ): Omit<IMenuItemData, "levelId" | "priority" | "parentMenuId"> => {
        const currentForm = isMainMenuItem ? addMenuItemForm : addChildMenuItemForm;
        currentForm.forEach(({ translationInfo, type }) => {
            if (!translationInfo) {
                return;
            }
            const { genericName, translation } = translationInfo;
            const defaultTranslation = values[genericName][defaultUsedLang] ? defaultUsedLang : currentLang;
            if (isTranslatedValueAdded(translationInfo, type, values)) {
                return;
            }
            values[genericName][translation] = values[genericName][defaultTranslation];
        });
        const { title, imageTitle, imageSource, url } = values;
        return { title, imageTitle, imageSource, url };
    };

    const getNewItemData = (
        parentMenuId: number,
        values: FormikValues,
        currentLang: LocaleNames,
        isMainMenuItem: boolean
    ): IMenuItemData => {
        const currentForm = isMainMenuItem ? addMenuItemForm : addChildMenuItemForm;
        currentForm.forEach(({ translationInfo, type }) => {
            if (!translationInfo) {
                return;
            }
            const { genericName, translation } = translationInfo;
            const defaultTranslation = values[genericName][defaultUsedLang] ? defaultUsedLang : currentLang;
            if (isTranslatedValueAdded(translationInfo, type, values)) {
                return;
            }
            values[genericName][translation] = values[genericName][defaultTranslation];
        });
        const levelId = parentMenuId === 0 ? 0 : 1;
        const priority = getNewItemPriority(parentMenuId);
        const { title, imageTitle = {}, imageSource = "", url } = values;
        return { title, imageTitle, imageSource, url, levelId, parentMenuId, priority };
    };

    const getNewItemPriority = (parentMenuId: number): number =>
        menuListRef.current.filter((item) => item.parent === parentMenuId).length + 1;

    const createMenuListItem = (menuItem: IMenuItem, parent: number): NodeModel<IMenuItem> => {
        const id = +getIdFromUrl(menuItem._links.self.href);
        return {
            id,
            parent,
            text: menuItem.title[(locale as LocaleNames) || defaultUsedLang],
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

    const headingActions = [
        {
            titleId: "pages.menu.add",
            onClick: () => onAddAction(0),
            disabled: isLoading,
        },
    ];

    return (
        <>
            <MainContent titleId="nav.contents.section.menu" actions={headingActions}>
                {isLoading && <Loader />}
                {!isLoading && !isError && !!menuList.length && (
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
                            dragPreviewRender={(monitorProps) => <MenuContentItemPreview monitorProps={monitorProps} />}
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
                )}
                {!isLoading && !isError && !menuList.length && <TextInfo id="information.info.no-menu-items" />}
                {!isLoading && isError && <TextInfo id="information.error.loading" />}
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
