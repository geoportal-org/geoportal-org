import { useRouter } from "next/router";
import { ReactNode, useState } from "react";
import { Button, Flex, useDisclosure, Text } from "@chakra-ui/react";
import { Modal, TextContent } from "@/components";
import { ContentService, PageService } from "@/services/api";
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

    const actionBtnClick = {
        [TableActionsSource.PAGES]: (actionName: TableActionsType) => onPagesAction(actionName),
        [TableActionsSource.WEBSITE]: (actionName: TableActionsType) => onContentsAction(actionName),
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
        try {
            await PageService.deletePage(+itemId);
            onDeleteAction();
            onCloseModal();
            showToast({
                title: "Deleted",
                description: `Page ${item.title} (ID: ${itemId}) has been deleted`,
            });
        } catch (error) {
            error instanceof Response &&
                showToast({
                    title: "Error occured",
                    description: `${error.status}`,
                    status: ToastStatus.ERROR,
                });
        }
    };

    const onContentsAction = (actionName: TableActionsType) => {
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
        try {
            await ContentService.deleteContent(+itemId);
            onDeleteAction();
            onCloseModal();
            showToast({
                title: "Deleted",
                description: `Content ${item.title} (ID: ${itemId}) has been deleted`,
            });
        } catch (error) {
            error instanceof Response &&
                showToast({
                    title: "Error occured",
                    description: `${error.status}`,
                    status: ToastStatus.ERROR,
                });
        }
    };

    return (
        <>
            <Flex
                bg="brand.backgroundSelected"
                borderRadius="primary"
                boxShadow="controlPanel"
                gap="5px"
                w="max-content"
            >
                {tableActionsBtns.map((btn) => {
                    const { icon, actionName, color, source } = btn;
                    return source.includes(actionsSource) ? (
                        <Button
                            key={actionName}
                            aria-label={actionName}
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
                    ) : null;
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
