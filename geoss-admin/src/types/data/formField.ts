import { CheckboxProps, ComponentWithAs, InputProps, TextareaProps } from "@chakra-ui/react";
import { SelectComponent } from "chakra-react-select";

export type FormField = {
    fieldType?:
        | ComponentWithAs<"input", InputProps>
        | ComponentWithAs<"textarea", TextareaProps>
        | ComponentWithAs<"input", CheckboxProps>
        | SelectComponent;
    isRequired: boolean;
    labelId: string;
    name: string;
    isReadOnly?: boolean;
    defaultValue?: string | number;
    placeholderId?: string;
    type?: string;
    validationSchema?: string;
    selectSettings?: SelectSettings;
    automaticFill?: {
        superiorField: string;
        fillType: "slug" | "relative-url";
    };
};

export type SelectSettings = {
    isMultiselect: boolean;
    isTranslated?: boolean;
    options: FormFieldSelect[];
};

export type FormFieldSelect = {
    label: string;
    value: string;
};

export type FormSection = {
    titleId: string;
    set: string;
    data: FormField[];
};
