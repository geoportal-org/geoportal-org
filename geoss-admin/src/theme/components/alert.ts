import { ComponentStyleConfig, StyleFunctionProps } from "@chakra-ui/react";
import { ToastStatus } from "@/types";

const setAlertColor = (status: ToastStatus): { bgColor: string; borderColor: string } => {
    switch (status) {
        case ToastStatus.SUCCESS:
            return {
                bgColor: "brand.acceptTransparent",
                borderColor: "green.700",
            };
        case ToastStatus.ERROR:
            return {
                bgColor: "brand.cancelTransparent",
                borderColor: "red",
            };
        case ToastStatus.INFO:
            return {
                bgColor: "brand.lightTransparent",
                borderColor: "brand.dark",
            };
        case ToastStatus.WARNING:
            return {
                bgColor: "brand.warningTransparent",
                borderColor: "orange.400",
            };
    }
};

export const alert: ComponentStyleConfig = {
    baseStyle: {
        container: {
            borderStartWidth: { base: "6px", "2xl": "0" },
            borderTopWidth: { base: "0", "2xl": "6px" },
            color: "brand.mainLight",
            maxW: { base: "280px", sm: "container.xs" },
        },
        description: {
            lineHeight: "normal",
        },
    },
    variants: {
        geossAlert: (props: StyleFunctionProps) => {
            const { status } = props;
            const { bgColor, borderColor } = setAlertColor(status);
            return {
                container: {
                    backgroundColor: bgColor,
                    borderStartColor: borderColor,
                    borderTopColor: borderColor,
                },
            };
        },
    },
};
