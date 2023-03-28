import { Flex } from "@chakra-ui/react";
import { LanguageSwitcher } from "@/components";
import { HeaderLogo } from "./HeaderLogo";
import { HeaderMenuToggler } from "./HeaderMenuToggler";
import { HeaderProps } from "@/types";

export const Header = ({ isMenuOpen, onMenuOpen, onMenuClose }: HeaderProps) => (
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
        <HeaderLogo />
        <LanguageSwitcher onMenuClose={onMenuClose} />
    </Flex>
);
