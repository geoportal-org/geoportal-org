import { Flex } from "@chakra-ui/react";
import { useSession } from "next-auth/react";
import { LanguageSwitcher } from "@/components";
import { HeaderLogo } from "./HeaderLogo";
import { HeaderMenuToggler } from "./HeaderMenuToggler";
import { HeaderProps } from "@/types";
import { useRouter } from "next/router";
import { unauthenicatedRoutes } from "@/data";
import HeaderSiteSelect from "./HeaderSiteSelect";

export const Header = ({ isMenuOpen, onMenuOpen, onMenuClose }: HeaderProps) => {
    const { data: session } = useSession();
    const isUser = !!session?.user;
    const { pathname } = useRouter();
    const isUnauthenticatedRoute = unauthenicatedRoutes.includes(pathname);

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
            {!isUnauthenticatedRoute && isUser ? (
                <>
                    <Flex align={'center'}>
                        <HeaderMenuToggler isOpen={isMenuOpen} onOpen={onMenuOpen} onClose={onMenuClose} />
                        <HeaderSiteSelect />
                    </Flex>

                    <HeaderLogo />
                    <LanguageSwitcher onMenuClose={onMenuClose} />
                </>
            ) : null}
        </Flex>
    );
};
