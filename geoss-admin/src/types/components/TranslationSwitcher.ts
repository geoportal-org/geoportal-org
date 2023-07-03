import { LocaleNames } from "@/types";

export type TranslationSwitcherProps = {
    currentLang: LocaleNames;
    onTranslationChange: (selectedLang: LocaleNames) => void;
};
