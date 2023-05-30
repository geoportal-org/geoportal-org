export type LocaleNames = "en" | "es" | "fr" | "pl" | "ru" | "zh";

export type TranslatedData = {
    [key in LocaleNames]: string;
};
