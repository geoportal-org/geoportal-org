import { useRouter } from "next/router";
import { ReactNode, useState, Fragment, useContext, useMemo } from "react";
import { Button, Flex, useDisclosure, Text, Divider } from "@chakra-ui/react";
import { Modal, TextContent } from "@/components";
import { ContentService, DefaultLayerService, PageService, TutorialTagsService, ViewsService } from "@/services/api";
import { DefaultLayerContext, TutorialTagsContext, ViewsContext } from "@/context";
import { defaultUsedLang, pagesRoutes, tableActionsBtns } from "@/data";
import {
    ModalAction,
    TableActionsProps,
    TableActionsSource,
    TableActionsType,
    ToastStatus,
    TranslatedData,
    LocaleNames,
} from "@/types";
import useFormatMsg from "@/utils/useFormatMsg";
import { setDecisionModalActions } from "@/utils/helpers";
import useCustomToast from "@/utils/useCustomToast";
import { ILayer, ITutorialTag } from "@/types/models";
import { useIntl } from "react-intl";

export const TableActions = ({ itemId, actionsSource, item, onDeleteAction, disabled }: TableActionsProps) => {
    const [modalContent, setModalContent] = useState<{
        header: ReactNode;
        body: ReactNode;
        actions?: ModalAction[];
    }>();
    const { isOpen: isOpenModal, onOpen: onOpenModal, onClose: onCloseModal } = useDisclosure();
    const { translate } = useFormatMsg();
    const { showToast } = useCustomToast();
    const router = useRouter();
    const { locale } = useIntl();
    const { onLayerEditAction } = useContext(DefaultLayerContext);
    const { onTagEditAction } = useContext(TutorialTagsContext);
    const { onAddSubViewAction, onEditSubViewAction, onEditViewAction } = useContext(ViewsContext);

    const actionsBtns = useMemo(
        () =>
            tableActionsBtns.filter(({ source, isMainRowOnlyAction }) =>
                !isMainRowOnlyAction
                    ? source.includes(actionsSource)
                    : source.includes(actionsSource) && !("parentViewId" in item)
            ),
        [actionsSource, item]
    );

    const actionBtnClick = {
        [TableActionsSource.PAGES]: (actionName: TableActionsType) => onPagesAction(actionName),
        [TableActionsSource.WEBSITE]: (actionName: TableActionsType) => onContentsAction(actionName),
        [TableActionsSource.LAYER]: (actionName: TableActionsType) => onLayersAction(actionName),
        [TableActionsSource.TUTORIAL]: (actionName: TableActionsType) => onTutorialTagsAction(actionName),
        [TableActionsSource.VIEWS]: (actionName: TableActionsType) => onViewsAction(actionName),
    };

    const onPagesAction = (actionName: TableActionsType) => {
        if (!("title" in item)) {
            return;
        }
        const title = item.title as TranslatedData;

        switch (actionName) {
            case TableActionsType.DELETE:
                setModalContent({
                    header: translate("pages.page.delete-page-title"),
                    body: (
                        <Text py={4}>
                            <TextContent
                                id="pages.page.delete-page-body"
                                itemId={itemId}
                                title={title[(locale as LocaleNames) || defaultUsedLang]}
                            />
                        </Text>
                    ),
                    actions: setDecisionModalActions(
                        async () => await deletePage(),
                        () => onCloseModal()
                    ),
                });
                onOpenModal();
                break;
            case TableActionsType.EDIT:
                router.push(`${pagesRoutes.page}/${itemId}`);
                break;
        }
    };

    const deletePage = async () => {
        if (!("title" in item)) {
            return;
        }
        const title = item.title as TranslatedData;
        try {
            await PageService.deletePage(+itemId);
            onDeleteAction();
            onCloseModal();
            showToast({
                title: translate("general.deleted"),
                description: translate("pages.page.deleted", {
                    title: title[(locale as LocaleNames) || defaultUsedLang],
                    itemId,
                }),
            });
        } catch (e: any) {
            const { errorStatus, errorInfo } = e;
            console.log(errorStatus);
            const msg = JSON.parse(errorInfo).detail;
            errorStatus &&
                showToast({
                    title: translate("general.error"),
                    description: `${errorStatus} + ${msg}`,
                    status: ToastStatus.ERROR,
                });
        }
    };

    const onContentsAction = (actionName: TableActionsType) => {
        if (!("title" in item)) {
            return;
        }

        switch (actionName) {
            case TableActionsType.DELETE:
                const translatedTitle = item.title as TranslatedData;
                setModalContent({
                    header: translate("pages.contents.delete-content-title"),
                    body: (
                        <Text py={4}>
                            <TextContent
                                id="pages.contents.delete-content-body"
                                itemId={itemId}
                                title={translatedTitle[(locale as LocaleNames) || defaultUsedLang]}
                            />
                        </Text>
                    ),
                    actions: setDecisionModalActions(
                        async () => await deleteContent(),
                        () => onCloseModal()
                    ),
                });
                onOpenModal();
                break;
            case TableActionsType.EDIT:
                router.push(`${pagesRoutes.website}/${itemId}`);
                break;
            case TableActionsType.PREVIEW:
                const isContent = "content" in item;
                setModalContent({
                    header: translate("pages.contents.preview-content-title", { id: itemId }),
                    body: translate("pages.contents.preview-content-body"),
                });
                onOpenModal();
                break;
        }
    };

    const deleteContent = async () => {
        if (!("title" in item)) {
            return;
        }
        const title = item.title as TranslatedData;
        try {
            await ContentService.deleteContent(+itemId);
            onDeleteAction();
            onCloseModal();
            showToast({
                title: translate("general.deleted"),
                description: translate("pages.contents.content-deleted", {
                    title: title[(locale as LocaleNames) || defaultUsedLang],
                    itemId,
                }),
            });
        } catch (e: any) {
            const { errorStatus, errorInfo } = e;
            let msg = "";
            if (errorInfo.errors.length) {
                msg = errorInfo.errors[0].message;
            } else {
                console.log(errorStatus);
                msg = JSON.parse(errorInfo).detail;
            }
            errorStatus &&
                showToast({
                    title: translate("general.error"),
                    description: `${errorStatus} + ${msg}`,
                    status: ToastStatus.ERROR,
                });
        }
    };

    const onLayersAction = (actionName: TableActionsType) => {
        if (!("name" in item)) {
            return;
        }

        switch (actionName) {
            case TableActionsType.DELETE:
                setModalContent({
                    header: translate("pages.layer.delete-layer-title"),
                    body: (
                        <Text py={4}>
                            <TextContent id="pages.layer.delete-layer-body" itemId={itemId} title={item.name} />
                        </Text>
                    ),
                    actions: setDecisionModalActions(
                        async () => await deleteLayer(),
                        () => onCloseModal()
                    ),
                });
                onOpenModal();
                break;
            case TableActionsType.EDIT:
                onLayerEditAction(item as ILayer);
                break;
        }
    };

    const deleteLayer = async () => {
        if (!("name" in item)) {
            return;
        }

        try {
            await DefaultLayerService.deleteLayer(+itemId);
            onDeleteAction();
            onCloseModal();
            showToast({
                title: translate("general.deleted"),
                description: translate("pages.layer.delete-confirmation", { title: item.name, itemId }),
            });
        } catch (e: any) {
            showToast({
                title: translate("general.error"),
                description: translate("pages.layer.delete-error", { title: item.name, itemId }),
                status: ToastStatus.ERROR,
            });
        }
    };

    const onTutorialTagsAction = (actionName: TableActionsType) => {
        if (!("name" in item)) {
            return;
        }

        switch (actionName) {
            case TableActionsType.DELETE:
                setModalContent({
                    header: translate("pages.tags.delete-tag-title"),
                    body: (
                        <Text py={4}>
                            <TextContent id="pages.tags.delete-tag-body" title={item.name} />
                        </Text>
                    ),
                    actions: setDecisionModalActions(
                        async () => await deleteTag(),
                        () => onCloseModal()
                    ),
                });
                onOpenModal();
                break;
            case TableActionsType.EDIT:
                onTagEditAction(item as ITutorialTag);
                break;
        }
    };

    const deleteTag = async () => {
        if (!("name" in item)) {
            return;
        }

        try {
            await TutorialTagsService.deleteTag(+itemId);
            onDeleteAction();
            onCloseModal();
            showToast({
                title: translate("general.deleted"),
                description: translate("pages.tags.delete-confirmation", { title: item.name }),
            });
        } catch (e: any) {
            showToast({
                title: translate("general.error"),
                description: translate("pages.tags.delete-error", { title: item.name }),
                status: ToastStatus.ERROR,
            });
        }
    };

    const onViewsAction = (actionName: TableActionsType) => {
        if (!("label" in item)) {
            return;
        }

        const isNestedViewAction = !!item.parentViewId;
        const isSubViews = item.subRows && item.subRows.length;

        switch (actionName) {
            case TableActionsType.DELETE:
                setModalContent({
                    header: translate(
                        item.parentViewId ? "pages.views.delete-nested-title" : "pages.views.delete-view-title"
                    ),
                    body: (
                        <Text py={4}>
                            <TextContent
                                id={
                                    !isNestedViewAction
                                        ? isSubViews
                                            ? "pages.views.delete-view-with-nested-body"
                                            : "pages.views.delete-view-body"
                                        : "pages.views.delete-nested-body"
                                }
                                title={item.label}
                            />
                        </Text>
                    ),
                    actions: setDecisionModalActions(
                        async () => await deleteView(),
                        () => onCloseModal()
                    ),
                });
                onOpenModal();
                break;
            case TableActionsType.EDIT:
                item.parentViewId
                    ? onEditSubViewAction(item.parentViewId, item.id, item)
                    : onEditViewAction(item.id, item);
                break;
            case TableActionsType.ADD:
                onAddSubViewAction(item.id);
                break;
        }
    };

    const deleteView = async () => {
        if (!("label" in item)) {
            return;
        }

        const isSubViews = item.subRows && item.subRows.length;
        const successMsgId = `pages.views.delete-${
            item.parentViewId ? "nested-confirmation" : isSubViews ? "with-nested-confirmation" : "confirmation"
        }`;
        const errorMsgId = `pages.views.delete-${item.parentViewId ? "nested-error" : "error"}`;
        try {
            !item.parentViewId
                ? await ViewsService.deleteView(+itemId)
                : await ViewsService.deleteSubView(item.parentViewId, +itemId);
            isSubViews && (await ViewsService.deleteViewSubViews(+itemId));
            onDeleteAction();
            onCloseModal();
            showToast({
                title: translate("general.deleted"),
                description: translate(successMsgId, { title: item.label }),
            });
        } catch (e: any) {
            showToast({
                title: translate("general.error"),
                description: translate(errorMsgId, { title: item.label }),
                status: ToastStatus.ERROR,
            });
        }
    };

    return (
        <>
            <Flex gap="5px" w="max-content">
                {actionsBtns.map((btn, index) => {
                    const isLast = index === actionsBtns.length - 1;
                    const { icon, actionName, color } = btn;
                    return (
                        <Fragment key={actionName}>
                            <Button
                                aria-label={actionName.toLowerCase()}
                                size="sm"
                                variant="ghost"
                                py={1}
                                px={2.5}
                                h="auto"
                                _active={{ color }}
                                onClick={() => actionBtnClick[actionsSource](actionName)}
                                isDisabled={disabled}
                                _hover={{
                                    color,
                                }}
                                _disabled={{
                                    cursor: "not-allowed",
                                    color: "brand.mainDark",
                                }}
                            >
                                {icon}
                            </Button>
                            {!isLast && (
                                <Divider orientation="vertical" borderColor="brand.dividerDark" opacity="1" h="auto" />
                            )}
                        </Fragment>
                    );
                })}
            </Flex>

            <Modal
                modalHeader={modalContent?.header}
                modalBody={modalContent?.body}
                actions={modalContent?.actions}
                onModalClose={onCloseModal}
                isModalOpen={isOpenModal}
            />
        </>
    );
};
