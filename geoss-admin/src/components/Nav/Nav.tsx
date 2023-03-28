import { useRouter } from "next/router";
import { Drawer, DrawerBody, DrawerOverlay, DrawerContent, Accordion, UnorderedList } from "@chakra-ui/react";
import { NavSection } from "./NavSection";
import { getActiveNavSection } from "@/utils/helpers";
import { navData } from "@/data";
import { scrollbarStyles } from "@/theme/commons";
import { NavProps } from "@/types";

export const Nav = ({ isOpen, onClose }: NavProps) => {
    const activeRoute = useRouter().pathname;
    const activeSectionIndex = getActiveNavSection(activeRoute);

    return (
        <Drawer
            isOpen={isOpen}
            placement="left"
            onClose={onClose}
            variant="geossMenuDrawer"
            size={{ base: "fullWidth", sm: "xs" }}
        >
            <DrawerOverlay />
            <DrawerContent>
                <DrawerBody>
                    <Accordion
                        defaultIndex={[activeSectionIndex]}
                        allowMultiple
                        variant="geossNav"
                        as={UnorderedList}
                        styleType="none"
                        m="0"
                        sx={scrollbarStyles}
                    >
                        {navData.map((navSection) => (
                            <NavSection key={navSection.titleId} navSection={navSection} onNavClose={onClose} />
                        ))}
                    </Accordion>
                </DrawerBody>
            </DrawerContent>
        </Drawer>
    );
};
