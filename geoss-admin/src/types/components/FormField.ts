import { FormField, LocaleNames } from "@/types";

export type FormFieldProps = {
    fieldData: FormField;
    invisible?: boolean;
    onInputTranslationChange?: (selectedLang: LocaleNames) => void;
    currentLang?: LocaleNames;
};
