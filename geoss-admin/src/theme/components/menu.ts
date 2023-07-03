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
                _disabled: {
                    cursor: "not-allowed",
                },
            },
            list: {
                bg: "brand.background",
                borderColor: "brand.divider",
                borderRadius: "secondary",
                color: "brand.mainDark",
                minWidth: "max-content",
                zIndex: "10",
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
        geossTranslationSwitcher: {
            button: {
                borderRadius: "primary",
                p: "2px",
                m: "0 4px",
                minW: "max-content",
                span: {
                    alignItems: "center",
                    display: "flex",
                    justifyContent: "center",
                },
                _focusVisible: {
                    boxShadow: "outline",
                },
                _disabled: {
                    cursor: "not-allowed",
                },
            },
            list: {
                bg: "brand.background",
                borderColor: "brand.divider",
                borderRadius: "secondary",
                color: "brand.mainDark",
                minWidth: "max-content",
                zIndex: "10",
            },
            item: {
                borderEnd: "3px solid transparent",
                borderStart: "3px solid transparent",
                fontSize: "sm",
                py: 1,
                px: 2,
                _focus: {
                    bg: "brand.darkSoft",
                },
                _hover: {
                    bg: "brand.darkSoft",
                },
            },
        },
        geossPagination: {
            button: {
                borderRadius: "primary",
                _focusVisible: {
                    boxShadow: "outline",
                },
            },
        },
    },
};
