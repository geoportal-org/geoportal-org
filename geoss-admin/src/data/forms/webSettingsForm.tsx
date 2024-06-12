import { Input } from "@chakra-ui/react";
import { Select } from "chakra-react-select";
import { FormField, FormSection } from "@/types";

const logoForm: FormField[] = [
    {
        fieldType: Input,
        isRequired: true,
        labelId: "general.title",
        name: "title",
        placeholderId: "form.placeholders.type",
        type: "text",
    },
    {
        fieldType: Select,
        isRequired: true,
        labelId: "pages.web.source",
        name: "source",
        placeholderId: "form.placeholders.select-one",
        type: "select",
    },
];

const defaultSourceNameForm: FormField[] = [
    {
        fieldType: Input,
        isRequired: true,
        labelId: "pages.web.default-source-name",
        name: "defaultSource",
        placeholderId: "form.placeholders.type",
        type: "text",
        isReadOnly: true,
        defaultValue: "GEOSS",
    },
];

const mapScopeForm: FormField[] = [
    {
        fieldType: Input,
        isRequired: true,
        labelId: "pages.web.longitude",
        name: "longitude",
        placeholderId: "form.placeholders.type",
        type: "number",
        validationSchema: "longitude",
    },
    {
        fieldType: Input,
        isRequired: true,
        labelId: "pages.web.latitude",
        name: "latitude",
        placeholderId: "form.placeholders.type",
        type: "number",
        validationSchema: "latitude",
    },
    {
        fieldType: Input,
        isRequired: true,
        labelId: "pages.web.zoom",
        name: "zoom",
        placeholderId: "form.placeholders.type",
        type: "number",
        validationSchema: "zero-or-positive-integer",
    },
];

export const webSettingsForm: FormSection[] = [
    // { titleId: "pages.web.logo", set: "logo", data: logoForm },
    { titleId: "pages.web.default-source", set: "source", data: defaultSourceNameForm },
    { titleId: "pages.web.map", set: "map", data: mapScopeForm },
];

export const webSettingsFormFields: FormField[] = webSettingsForm.map((section) => section.data).flat();
