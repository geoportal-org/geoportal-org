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
    placeholderId?: string;
    type?: string;
    validationSchema?: string;
    selectSettings?: SelectSettings;
    fieldDependence?: {
        fieldName: string;
    };
};

export type SelectSettings = {
    isMultiselect: boolean;
    options: FormFieldSelect[];
};

export type FormFieldSelect = {
    label: string;
    value: string;
};
