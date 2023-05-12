import { Box, Flex } from "@chakra-ui/react";
import { LanguageSwitcher } from "@/components";
import { HeaderLogo } from "./HeaderLogo";
import { HeaderMenuToggler } from "./HeaderMenuToggler";
import { HeaderProps } from "@/types";
import { useRouter } from "next/router";
import { unauthenicatedRoutes } from "@/data";
//import { useSession } from "next-auth/react";

export const Header = ({ isMenuOpen, onMenuOpen, onMenuClose }: HeaderProps) => {
    /*const { data: session } = useSession();
    const isUser = !!session?.user;
    const { pathname } = useRouter();
    const isUnauthenticatedRoute = unauthenicatedRoutes.includes(pathname);*/

    return (
        <Flex
            as="header"
            bg="brand.dark"
            color="brand.mainLight"
            px={2.5}
            py={3}
            h="headerHeight"
            justify="space-between"
            align="center"
            fontSize="s"
        >
            <HeaderMenuToggler isOpen={isMenuOpen} onOpen={onMenuOpen} onClose={onMenuClose} />
            {/*{!isUnauthenticatedRoute && isUser ? (
                <HeaderMenuToggler isOpen={isMenuOpen} onOpen={onMenuOpen} onClose={onMenuClose} />
            ) : (
                <Box w="52px"></Box>
            )}*/}
            <HeaderLogo />
            <LanguageSwitcher onMenuClose={onMenuClose} />
        </Flex>
    );
};
