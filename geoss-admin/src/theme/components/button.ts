import { ComponentStyleConfig, StyleFunctionProps } from "@chakra-ui/react";

const disabledStyles = {
    borderColor: "grey",
    boxShadow: "none",
    color: "grey",
    opacity: 0.7,
};

export const button: ComponentStyleConfig = {
    baseStyle: {
        borderRadius: "primary",
        fontWeight: "normal",
        transitionDuration: "slower",
        _disabled: {
            ...disabledStyles,
        },
        _hover: {
            color: "brand.mainLight",
            boxShadow: "primaryBtnHover",
        },
    },

    sizes: {
        sm: {
            minW: "28",
        },
    },

    variants: {
        geossOutline: (props: StyleFunctionProps) => {
            const { color } = props;
            return {
                borderColor: color,
                borderWidth: "1px",
                _hover: {
                    bg: color,
                    _disabled: {
                        ...disabledStyles,
                    },
                },
            };
        },
        geossGhost: (props: StyleFunctionProps) => {
            const { color } = props;
            return {
                minW: "2",
                _hover: {
                    bg: `${color}Soft`,
                    boxShadow: "none",
                    color: color,
                    _disabled: {
                        ...disabledStyles,
                    },
                },
            };
        },
    },
};
