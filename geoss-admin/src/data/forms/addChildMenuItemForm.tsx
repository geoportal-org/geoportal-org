import { Input } from "@chakra-ui/react";
import { FormField } from "@/types";

export const addChildMenuItemForm: FormField[] = [
    {
        fieldType: Input,
        isRequired: true,
        labelId: "general.title",
        name: "title",
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
        validationSchema: "relative-url",
        automaticFill: {
            superiorField: "title",
            fillType: "relative-url",
        },
    },
];
