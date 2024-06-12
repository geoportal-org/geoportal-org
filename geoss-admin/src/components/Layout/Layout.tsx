import { Flex, useDisclosure } from "@chakra-ui/react";
import { Footer, Header, Nav } from "@/components";
import { LayoutProps } from "@/types";
import { useRouter } from "next/router";
import { useEffect, useState } from "react";
import { SiteContext } from "@/context/CurrentSiteContext";

export const Layout = ({ children }: LayoutProps) => {
    const { isOpen: isMenuOpen, onOpen: onMenuOpen, onClose: onMenuClose } = useDisclosure();
    const router = useRouter();

    const [currentSiteId, setCurrentSiteId] = useState<number>(0);
    const [allSites, setAllSites] = useState<any>([]);

    return (
        <>
            <SiteContext.Provider
                value={{
                    currentSiteId: currentSiteId,
                    setCurrentSiteId: setCurrentSiteId,
                    allSites: allSites,
                    setAllSites: setAllSites,
                }}
            >
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
                        maxW={router.route === "/survey" ? "100%" : "container.2xl"}
                        w={{
                            base: "full",
                            md: router.route === "/survey" ? "100%" : "95%",
                            lg: router.route === "/survey" ? "100%" : "90%",
                        }}
                        bg="brand.mainLight"
                        borderRadius="primary"
                        color="brand.mainDark"
                    >
                        {children}
                    </Flex>
                </Flex>
                <Footer />
            </SiteContext.Provider>
        </>
    );
};
