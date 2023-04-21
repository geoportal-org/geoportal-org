import { useRouter } from "next/router";
import { ReactNode, useState, Fragment, useContext } from "react";
import { Button, Flex, useDisclosure, Text, Divider } from "@chakra-ui/react";
import { Modal, TextContent } from "@/components";
import { ContentService, DefaultLayerService, PageService, TutorialTagsService } from "@/services/api";
import { DefaultLayerContext, TutorialTagsContext } from "@/context";
import { tableActionsBtns } from "@/data";
import { ModalAction, TableActionsProps, TableActionsSource, TableActionsType, ToastStatus } from "@/types";
import useFormatMsg from "@/utils/useFormatMsg";
import { setDecisionModalActions } from "@/utils/helpers";
import useCustomToast from "@/utils/useCustomToast";

export const TableActions = ({ itemId, actionsSource, item, onDeleteAction }: TableActionsProps) => {
    const [modalContent, setModalContent] = useState<{
        header: ReactNode;
        body: ReactNode;
        actions?: ModalAction[];
    }>();
    const { isOpen: isOpenModal, onOpen: onOpenModal, onClose: onCloseModal } = useDisclosure();
    const { translate } = useFormatMsg();
    const { showToast } = useCustomToast();
    const router = useRouter();
    const { onLayerEditAction } = useContext(DefaultLayerContext);
    const { onTagEditAction } = useContext(TutorialTagsContext);
    const actionsBtns = tableActionsBtns.filter(({ source }) => source.includes(actionsSource));

    const actionBtnClick = {
        [TableActionsSource.PAGES]: (actionName: TableActionsType) => onPagesAction(actionName),
        [TableActionsSource.WEBSITE]: (actionName: TableActionsType) => onContentsAction(actionName),
        [TableActionsSource.LAYER]: (actionName: TableActionsType) => onLayersAction(actionName),
        [TableActionsSource.TUTORIAL]: (actionName: TableActionsType) => onTutorialTagsAction(actionName),
    };

    const onPagesAction = (actionName: TableActionsType) => {
        switch (actionName) {
            case TableActionsType.DELETE:
                setModalContent({
                    header: translate("pages.page.delete-page-title"),
                    body: (
                        <Text py={4}>
                            <TextContent id="pages.page.delete-page-body" itemId={itemId} />
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
                router.push(`/page/${itemId}`);
                break;
        }
    };

    const deletePage = async () => {
        if (!("title" in item)) {
            return;
        }

        try {
            await PageService.deletePage(+itemId);
            onDeleteAction();
            onCloseModal();
            showToast({
                title: "Deleted",
                description: `Page ${item.title} (ID: ${itemId}) has been deleted`,
            });
        } catch (error) {
            const err = error as { errorInfo: any; errorStatus: number };
            const { errorStatus, errorInfo } = err;
            console.log(errorInfo);
            console.log(errorStatus);
            errorStatus &&
                showToast({
                    title: "Error occured",
                    description: `${errorStatus}`,
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
                setModalContent({
                    header: translate("pages.contents.delete-content-title"),
                    body: (
                        <Text py={4}>
                            <TextContent id="pages.contents.delete-content-body" itemId={itemId} title={item.title} />
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
                router.push(`/contents/${itemId}`);
                break;
            case TableActionsType.PREVIEW:
                const isContent = "content" in item;
                const content = isContent && item.content;
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

        try {
            await ContentService.deleteContent(+itemId);
            onDeleteAction();
            onCloseModal();
            showToast({
                title: "Deleted",
                description: `Content ${item.title} (ID: ${itemId}) has been deleted`,
            });
        } catch (error) {
            const err = error as { errorInfo: any; errorStatus: number };
            const { errorStatus, errorInfo } = err;
            console.log(errorInfo);
            console.log(errorStatus);
            errorStatus &&
                showToast({
                    title: "Error occured",
                    description: `${errorStatus}`,
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
                onLayerEditAction(+itemId);
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
        } catch (e) {
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
                console.log(item);
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
                onTagEditAction(+itemId);
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
        } catch (e) {
            showToast({
                title: translate("general.error"),
                description: translate("pages.tags.delete-error", { title: item.name }),
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
                                _hover={{ color }}
                                onClick={() => actionBtnClick[actionsSource](actionName)}
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
