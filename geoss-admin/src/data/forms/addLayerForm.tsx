import { Input } from "@chakra-ui/react";
import { FormField } from "@/types";
import { Select } from "chakra-react-select";

export const addLayerForm: FormField[] = [
    {
        fieldType: Input,
        isRequired: true,
        labelId: "pages.layer.name",
        name: "name",
        placeholderId: "form.placeholders.type",
        type: "text",
    },
    {
        fieldType: Input,
        isRequired: true,
        labelId: "general.url",
        name: "url",
        placeholderId: "form.placeholders.type",
        type: "text",
        validationSchema: "relative-full-url",
    },
    {
        fieldType: Select,
        isRequired: true,
        labelId: "pages.layer.visibility",
        name: "visible",
        placeholderId: "form.placeholders.select-one",
        type: "select",
        selectSettings: {
            isMultiselect: false,
            isTranslated: true,
            options: [
                { label: "pages.layer.visible", value: "true" },
                { label: "pages.layer.invisible", value: "false" },
            ],
        },
    },
];
