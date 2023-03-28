import { extendTheme, Theme } from "@chakra-ui/react";
import { fontSizes, fonts, colors, radii, sizes, shadows } from "./foundations";
import { alert, button, input, modal, drawer, accordion, menu, table, checkbox } from "./components";
import { global } from "./global";

const defaultThemeOverrides = {
    colors,
    fonts,
    fontSizes,
    radii,
    shadows,
    sizes,
    styles: global,
    components: {
        Accordion: accordion,
        Alert: alert,
        Button: button,
        Checkbox: checkbox,
        Drawer: drawer,
        Input: input,
        Menu: menu,
        Modal: modal,
        Table: table,
    },
};

const geossTheme: Partial<Theme> = extendTheme(defaultThemeOverrides);

export default geossTheme;
