import { Input } from "@chakra-ui/react";
import { Select } from "chakra-react-select";
import { FormField } from "@/types";

export const addViewForm: FormField[] = [
    {
        fieldType: Input,
        isRequired: true,
        labelId: "pages.views.name",
        name: "label",
        placeholderId: "form.placeholders.type",
        type: "text",
    },
    {
        fieldType: Input,
        isRequired: true,
        labelId: "pages.views.id",
        name: "value",
        placeholderId: "form.placeholders.type",
        type: "text",
    },
    {
        fieldType: Select,
        isRequired: true,
        labelId: "pages.views.default",
        name: "defaultOption",
        placeholderId: "form.placeholders.select-one",
        type: "select",
        selectSettings: {
            isMultiselect: false,
            isTranslated: true,
            options: [
                { label: "general.yes", value: "true" },
                { label: "general.no", value: "false" },
            ],
        },
    },
];
