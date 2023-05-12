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
            maxW: "full",
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
        geossNavButton: {
            p: 2.5,
            display: "flex",
            borderRadius: "secondary",
            gap: "10px",
            color: "black",
            alignItems: "center",
            justifyContent: "flex-start",
            letterSpacing: "0.5px",
            transitionDuration: "slower",
            w: "95%",
            m: "0 auto",
            fontSize: "sm",
            _hover: {
                bg: "brand.dark",
                color: "brand.mainLight",
                transform: "scale(1.05)",
            },
        },
        geossSignInButton: {
            py: 5,
            textTransform: "uppercase",
            letterSpacing: "1px",
            maxW: "200px",
            w: "full",
            bg: "brand.dark",
            color: "brand.mainLight",
            fontSize: "lg",
        },
    },
};
