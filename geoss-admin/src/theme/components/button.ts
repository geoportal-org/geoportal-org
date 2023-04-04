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
        color: "brand.mainLight",
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
            px: 4,
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
        geossSolid: (props: StyleFunctionProps) => {
            const { bg } = props;
            return {
                bg: bg,
                borderColor: bg,
                borderWidth: "1px",
                _disabled: {
                    bg: "brand.mainLight",
                },
                _hover: {
                    bg: "brand.mainLight",
                    boxShadow: "none",
                    color: bg,
                    _disabled: {
                        ...disabledStyles,
                    },
                },
            };
        },
        geossBreadcrumb: {
            h: "auto",
            p: 1,
            _hover: {
                color: "brand.mainDark",
                boxShadow: "none",
                bg: "brand.darkSoft",
            },
        },
        geossFileItem: {
            alignItems: "center",
            bg: "transparent",
            color: "brand.mainDark",
            display: "flex",
            flexDirection: "column",
            fontSize: "sm",
            h: "full",
            justifyContent: "flex-start",
            p: 2,
            _hover: {
                bg: "brand.darkSoft",
                boxShadow: "none",
                color: "brand.mainDark",
            },
        },
        geossPagination: {
            p: 0,
            color: "brand.mainDark",
            _hover: {
                bg: "brand.darkSoft",
                color: "brand.mainDark",
                boxShadow: "none",
            },
        },
    },
};
