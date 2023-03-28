import { Flex, useDisclosure } from "@chakra-ui/react";
import { Footer, Header, Nav } from "@/components";
import { LayoutProps } from "@/types";

export const Layout = ({ children }: LayoutProps) => {
    const { isOpen: isMenuOpen, onOpen: onMenuOpen, onClose: onMenuClose } = useDisclosure();

    return (
        <>
            <Header isMenuOpen={isMenuOpen} onMenuOpen={onMenuOpen} onMenuClose={onMenuClose} />
            <Nav isOpen={isMenuOpen} onClose={onMenuClose} />
            <Flex
                as="main"
                direction="column"
                flexGrow="1"
                overflow="hidden"
                px={2.5}
                py={3}
                bg="brand.background"
                align="center"
            >
                <Flex
                    as="section"
                    h="full"
                    direction="column"
                    maxW="container.2xl"
                    w={{ base: "full", md: "95%", lg: "90%" }}
                    bg="brand.mainLight"
                    borderRadius="primary"
                    color="brand.mainDark"
                >
                    {children}
                </Flex>
            </Flex>
            <Footer />
        </>
    );
};
