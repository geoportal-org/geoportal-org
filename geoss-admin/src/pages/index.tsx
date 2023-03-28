import { NextPage } from "next";
import { Flex, Text, useDisclosure } from "@chakra-ui/react";
import useCustomToast from "@/utils/useCustomToast";
import { ButtonVariant, ToastStatus } from "@/types";
import { Modal, PrimaryButton, TextContent, MainContent } from "@/components";
import useFormatMsg from "@/utils/useFormatMsg";
import { useRouter } from "next/router";
import { ContentService, PageService } from "@/services/api";

const Home: NextPage = () => {
    const { showToast } = useCustomToast();
    const { translate } = useFormatMsg();
    const router = useRouter();
    const { isOpen, onOpen, onClose } = useDisclosure();

    const showInfo = (status?: ToastStatus) => {
        showToast({
            title: translate("devTest.toastTitle"),
            description: translate("devTest.toastDesc"),
            status,
        });
    };

    const headerActions = [
        {
            titleId: translate("devTest.add"),
            onClick: () => showInfo(ToastStatus.INFO),
        },
        {
            titleId: translate("devTest.remove"),
            color: "brand.cancel",
            variant: ButtonVariant.GHOST,
            onClick: () => showInfo(ToastStatus.ERROR),
        },
    ];

    const getContents = async () => {
        try {
            const contents = await ContentService.getContentsClientSide();
            console.log(contents);
        } catch (e) {
            console.log("failed");
            console.error(e);
        }
    };

    const getPages = async () => {
        try {
            const pages = await PageService.getPagesListClientSide();
            console.log(pages);
        } catch (e) {
            console.log("failed");
            console.error(e);
        }
    };

    return (
        <MainContent titleId={translate("devTest.header")} actions={headerActions}>
            <Text fontWeight="bold" fontSize="22px" mb="30px">
                TBD SIGN-IN PAGE OR REDIRECT TO DEFAULT PAGE
            </Text>

            <Flex direction="column" align="flex-start" gap={3}>
                <PrimaryButton onClick={() => getContents()} variant={ButtonVariant.OUTLINE} color="brand.accept">
                    get data client side - contents
                </PrimaryButton>
                <PrimaryButton onClick={() => getPages()} variant={ButtonVariant.GHOST} color="brand.cancel">
                    get data client side - pages
                </PrimaryButton>
                <PrimaryButton onClick={() => showInfo(ToastStatus.INFO)}>
                    <TextContent id="devTest.btnToastInfo" />
                </PrimaryButton>
                <PrimaryButton
                    onClick={() => showInfo(ToastStatus.WARNING)}
                    variant={ButtonVariant.SOLID}
                    color="orange.400"
                >
                    <TextContent id="devTest.btnToastWarning" />
                </PrimaryButton>

                <PrimaryButton
                    variant={ButtonVariant.SOLID}
                    color="red"
                    onClick={() => router.push("/non-existing-url")}
                >
                    <TextContent id="devTest.btnErrPage" />
                </PrimaryButton>

                <PrimaryButton onClick={onOpen}>
                    <TextContent id="devTest.show" />
                </PrimaryButton>

                <PrimaryButton disabled>
                    <TextContent id="devTest.btnDisabled" />
                </PrimaryButton>
                <PrimaryButton variant={ButtonVariant.GHOST} disabled>
                    <TextContent id="devTest.btnDisabled" />
                </PrimaryButton>
            </Flex>

            {isOpen && (
                <Modal
                    isModalOpen={isOpen}
                    onModalClose={onClose}
                    modalBody="modal body"
                    modalHeader="modal header"
                    actions={[
                        {
                            label: translate("devTest.modalCancel"),
                            btnVariant: ButtonVariant.GHOST,
                            color: "brand.cancel",
                            onClick: onClose,
                        },
                        {
                            label: translate("devTest.modalAccept"),
                            btnVariant: ButtonVariant.OUTLINE,
                            color: "brand.accept",
                            onClick: onClose,
                        },
                    ]}
                />
            )}
        </MainContent>
    );
};

export default Home;
