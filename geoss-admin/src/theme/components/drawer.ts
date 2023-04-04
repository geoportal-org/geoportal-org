import { ComponentStyleConfig } from "@chakra-ui/react";

export const drawer: ComponentStyleConfig = {
    baseStyle: {
        overlay: {
            bg: "transparent",
            mt: "12",
            h: "calc(100% - 2 * (var(--chakra-sizes-12)))",
        },
        dialog: {
            mt: "12",
            bg: "brand.background",
            h: "calc(100% - 2 * (var(--chakra-sizes-12)))",
        },
        dialogContainer: {
            mt: "12",
            h: "calc(100% - 2 * (var(--chakra-sizes-12)))",
        },
        header: {
            fontSize: "s",
            pl: 4,
            pr: 4,
            pt: 7,
            lineHeight: "normal",
            pos: "realtive",
            _after: {
                content: "''",
                display: "block",
                width: "full",
                height: "1px",
                bg: "brand.mainDark",
                mt: 0.5,
            },
        },
        closeButton: {
            w: "24px",
            h: "24px",
            top: 1,
            right: 1,
            _hover: {
                bg: "inherit",
            },
        },
        body: {
            p: "4",
        },
    },
    variants: {
        geossMenuDrawer: {
            dialog: {
                borderRight: "1px",
                borderColor: { base: "transparent", sm: "brand.divider" },
            },
        },
        geossDrawer: {
            overlay: {
                mt: 0,
                h: "full",
            },
            dialog: {
                borderLeft: "1px",
                borderColor: { base: "transparent", sm: "brand.divider" },
            },
            dialogContainer: {
                mt: 0,
                h: "full",
            },
            body: {
                flexGrow: 1,
                overflow: "hidden",
            },
        },
    },
    sizes: {
        fullWidth: {
            dialogContainer: {
                width: "100%",
            },
        },
    },
};
