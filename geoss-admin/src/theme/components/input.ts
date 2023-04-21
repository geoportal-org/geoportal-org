import { ComponentStyleConfig } from "@chakra-ui/react";

export const input: ComponentStyleConfig = {
    baseStyle: {
        field: {
            borderRadius: "primary",
            _autofill: {
                boxShadow: "0 0 0px 1000px inherit inset",
                transition: "background-color 5000s ease-in-out 0s",
            },
            _placeholder: {
                fontStyle: "italic",
                opacity: 0.6,
            },
            _readOnly: {
                cursor: "not-allowed",
                _focus: {
                    borderColor: "brand.dividerDark",
                },
            },
        },
    },

    sizes: {
        sm: {
            field: {
                borderRadius: "primary",
            },
        },
    },
};
