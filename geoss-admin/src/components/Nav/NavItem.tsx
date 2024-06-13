import NextLink from "next/link";
import { useRouter } from "next/router";
import { signOut } from "next-auth/react";
import { Box, Button, Link, ListItem } from "@chakra-ui/react";
import { TextContent } from "@/components";
import { NavItemProps, ToastStatus } from "@/types";
import useFormatMsg from "@/utils/useFormatMsg";
import { pagesRoutes } from "@/data";
import useCustomToast from "@/utils/useCustomToast";

export const NavItem = ({ item, onNavClose }: NavItemProps) => {
    const { titleId, href, Icon, action } = item;
    const router = useRouter();
    const { translate } = useFormatMsg();
    const { showToast } = useCustomToast();
    const isActiveRoute = `/${router.pathname.split("/")[1]}` === href;

    const setNavButtonAction = () => {
        switch (action?.name) {
            case "logout":
                handleUserLogout();
                break;
        }
    };

    const handleUserLogout = async () => {
        await fetch(`${process.env.NEXTAUTH_URL}/api/auth/signOutProvider`, { method: "PUT" }).then(async (res) =>
            res.ok ? await signOut({ callbackUrl: pagesRoutes.signIn }) : showLogoutErrorMsg()
        );
    };

    const showLogoutErrorMsg = () =>
        showToast({
            title: translate("general.error"),
            description: translate("information.error.logout"),
            status: ToastStatus.ERROR,
        });

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
            {href && !action && (
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
            )}
            {!href && action && (
                <Button variant="geossNavButton" aria-label={translate(action.id)} onClick={setNavButtonAction}>
                    <Icon color="currentColor" />
                    <TextContent id={titleId} />
                </Button>
            )}
        </Box>
    );
};
