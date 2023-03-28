import {
    Modal as ChakraModal,
    ModalOverlay,
    ModalContent,
    ModalHeader,
    ModalFooter,
    ModalBody,
    ModalCloseButton,
    Divider,
} from "@chakra-ui/react";
import { TextContent, PrimaryButton } from "@/components";
import { ModalProps } from "@/types";
import { scrollbarStyles } from "@/theme/commons";

export const Modal = ({ onModalClose, isModalOpen, modalHeader, modalBody, actions, size }: ModalProps) => {
    const renderModalFooter = () =>
        actions ? (
            actions.map((action) => (
                <PrimaryButton
                    color={action.color}
                    key={action.label}
                    onClick={action.onClick}
                    variant={action.btnVariant}
                >
                    <TextContent id={action.label} />
                </PrimaryButton>
            ))
        ) : (
            <PrimaryButton onClick={onModalClose}>
                <TextContent id="general.close" />
            </PrimaryButton>
        );

    return (
        <ChakraModal
            blockScrollOnMount={false}
            isCentered
            isOpen={isModalOpen}
            onClose={onModalClose}
            scrollBehavior="inside"
            size={size || "xl"}
            variant="geossModal"
        >
            <ModalOverlay />
            <ModalContent>
                <ModalHeader>{modalHeader || ""}</ModalHeader>
                <ModalCloseButton />
                <ModalBody sx={scrollbarStyles}>{modalBody}</ModalBody>
                <Divider borderColor="blackAlpha.300" />
                <ModalFooter>{renderModalFooter()}</ModalFooter>
            </ModalContent>
        </ChakraModal>
    );
};
