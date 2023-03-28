import { ComponentStyleConfig } from "@chakra-ui/react";

export const checkbox: ComponentStyleConfig = {
    variants: {
        geossTable: {
            control: {
                borderColor: "brand.mainDark",
                borderWidth: 1,
                _checked: {
                    bg: "brand.dark",
                },
            },
        },
    },
};
