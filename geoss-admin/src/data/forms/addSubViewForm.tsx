import { Input } from "@chakra-ui/react";
import { FormField } from "@/types";

export const addSubViewForm: FormField[] = [
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
];
