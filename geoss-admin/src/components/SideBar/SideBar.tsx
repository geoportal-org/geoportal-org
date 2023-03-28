import { Drawer, DrawerBody, DrawerOverlay, DrawerContent, DrawerCloseButton, DrawerHeader } from "@chakra-ui/react";
import { TextContent } from "@/components";
import { SideBarProps } from "@/types";

export const SideBar = ({ isOpen, onClose, content, titleId }: SideBarProps) => {
    return (
        <Drawer
            isOpen={isOpen}
            placement="right"
            onClose={onClose}
            variant="geossDrawer"
            size={{ base: "fullWidth", sm: "xs" }}
        >
            <DrawerOverlay />
            <DrawerContent>
                <DrawerCloseButton />
                <DrawerHeader>
                    <TextContent id={titleId || ""} />
                </DrawerHeader>
                <DrawerBody>{content}</DrawerBody>
            </DrawerContent>
        </Drawer>
    );
};
