import { ChakraStylesConfig } from "chakra-react-select";

export const chakraSelectStyles: ChakraStylesConfig = {
    placeholder: (provided) => ({
        ...provided,
        fontStyle: "italic",
        color: "brand.dividerDark",
    }),
    control: (provided) => ({
        ...provided,
        borderColor: "brand.dividerDark",
    }),
    menuList: (provided) => ({
        ...provided,
        borderColor: "brand.dividerDark",
    }),
    menu: (provided) => ({
        ...provided,
        mt: "-2px",
    }),
    valueContainer: (provided) => ({
        ...provided,
        p: "0 10px",
    }),
    option: (provided, state) => ({
        ...provided,
        _active: { bg: "brand.darkSoft" },
        bg: state.isFocused ? "brand.darkSoft" : "inherit",
    }),
    dropdownIndicator: (provided) => ({
        ...provided,
        color: "brand.dividerDark",
    }),
};
