import { ComponentStyleConfig } from "@chakra-ui/react";

export const menu: ComponentStyleConfig = {
    variants: {
        geossLangSwitcher: {
            button: {
                borderRadius: "primary",
                p: "2px",
                span: {
                    alignItems: "center",
                    display: "flex",
                    justifyContent: "center",
                },
                _focusVisible: {
                    boxShadow: "outline",
                },
            },
            list: {
                bg: "brand.background",
                borderColor: "brand.divider",
                borderRadius: "secondary",
                color: "brand.mainDark",
                minWidth: "max-content",
                zIndex: "3",
            },
            item: {
                borderEnd: "3px solid transparent",
                borderStart: "3px solid transparent",
                fontSize: "sm",
                letterSpacing: "0.5px",
                px: 5,
                py: 1,
                _focus: {
                    bg: "brand.darkSoft",
                },
                _hover: {
                    bg: "brand.darkSoft",
                },
            },
        },
    },
};
