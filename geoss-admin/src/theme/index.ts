import { extendTheme, Theme } from "@chakra-ui/react";
import { fontSizes, fonts, colors, radii, sizes, shadows } from "./foundations";
import { alert, button, input } from "./components";
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
        Alert: alert,
        Button: button,
        Input: input,
    },
};

const geossTheme: Partial<Theme> = extendTheme(defaultThemeOverrides);

export default geossTheme;
