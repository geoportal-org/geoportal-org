import { ComponentStyleConfig } from "@chakra-ui/react";

export const modal: ComponentStyleConfig = {
    variants: {
        geossModal: {
            overlay: {
                bg: "blackAlpha.600",
            },
            dialog: {
                borderRadius: "primary",
                boxShadow: "dialogShadow",
                overflow: "hidden",
            },
            dialogContainer: {
                px: 3,
            },
            header: {
                alignItems: "center",
                backgroundColor: "brand.dark",
                color: "brand.mainLight",
                display: "flex",
                fontSize: "s",
                fontWeight: "normal",
                justifyContent: "flex-start",
                letterSpacing: 0.5,
                lineHeight: "normal",
                minH: 12,
                p: 3,
                pr: 8,
                textTransform: "uppercase",
            },
            body: {
                p: 3,
                textAlign: "center",
            },
            footer: {
                alignItems: "center",
                flexWrap: "wrap-reverse",
                gap: [2, 3],
                justifyContent: "flex-end",
                p: 3,
            },
            closeButton: {
                color: "brand.mainLight",
                h: "24px",
                right: "2px",
                top: "2px",
                w: "24px",
            },
        },
    },
};
