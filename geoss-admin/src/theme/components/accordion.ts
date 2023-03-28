import { ComponentStyleConfig } from "@chakra-ui/react";

export const accordion: ComponentStyleConfig = {
    variants: {
        geossNav: {
            root: {
                display: "flex",
                flexDirection: "column",
                gap: "5px",
                h: "full",
                minH: "full",
                overflowX: "hidden",
                overflowY: "auto",
                p: "5px",
            },
            container: {
                border: "none",
                borderBottomColor: "brand.divider",
                borderBottomStyle: "solid",
                borderBottomWidth: "1px",
            },
            button: {
                alignItems: "center",
                borderRadius: "secondary",
                color: "brand.mainDark",
                display: "flex",
                fontSize: "s",
                fontWeight: "bold",
                justifyContent: "space-between",
                letterSpacing: "0.5px",
                mb: "5px",
                px: "10px",
                _expanded: {
                    bg: "brand.darkSoft",
                    color: "brand.dark",
                },
                _hover: {
                    bg: "brand.darkSoft",
                    color: "brand.dark",
                },
            },
            panel: {
                p: "5px 0",
            },
            icon: {
                color: "currentColor",
                fontWeight: "normal",
            },
        },
    },
};
