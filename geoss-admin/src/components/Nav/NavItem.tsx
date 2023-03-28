import NextLink from "next/link";
import { useRouter } from "next/router";
import { Box, Link, ListItem } from "@chakra-ui/react";
import { TextContent } from "@/components";
import { NavItemProps } from "@/types";

export const NavItem = ({ item, onNavClose }: NavItemProps) => {
    const { titleId, href, Icon } = item;
    const router = useRouter();
    const isActiveRoute = `/${router.pathname.split("/")[1]}` === href;

    return (
        <Box
            as={ListItem}
            borderStart="3px solid"
            borderTopRightRadius="secondary"
            borderBottomRightRadius="secondary"
            borderStartColor={isActiveRoute ? "brand.dark" : "transparent"}
            bg={isActiveRoute ? "brand.darkSoft" : "transparent"}
            _hover={{
                borderStartColor: "transparent",
                bg: "transparent",
            }}
        >
            <Link
                as={NextLink}
                onClick={onNavClose}
                href={href}
                p={2.5}
                borderRadius="secondary"
                display="flex"
                color="black"
                alignItems="center"
                justifyContent="flex-start"
                gap="10px"
                letterSpacing="0.5px"
                transitionDuration="slower"
                w="95%"
                m="0 auto"
                _hover={{
                    bg: "brand.dark",
                    color: "brand.mainLight",
                    transform: "scale(1.05)",
                }}
            >
                <Icon color="currentColor" />
                <TextContent id={titleId} />
            </Link>
        </Box>
    );
};
