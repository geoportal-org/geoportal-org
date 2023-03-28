export const table = {
    variants: {
        geossTable: {
            table: {
                mb: 1.5,
                borderCollapse: "separate",
                borderSpacing: "0 2px",
            },
            th: {
                borderBottom: "1px solid",
                borderBottomColor: "brand.divider",
                color: "brand.mainDark",
                fontSize: "15px",
                letterSpacing: "0",
                p: 2.5,
                textTransform: "none",
                _first: {
                    borderLeft: "1px solid transparent",
                },
            },
            td: {
                p: 2.5,
                borderTop: "1px solid",
                borderBottom: "1px solid",
                borderColor: "currentColor",
            },
            tr: {
                _even: {
                    td: {
                        bg: "brand.darkSoft",
                    },
                },
            },
        },
    },
};
