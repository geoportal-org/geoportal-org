import { Input } from "@chakra-ui/react";
import { FormField } from "@/types";

export const editFileForm: FormField[] = [
    {
        fieldType: Input,
        isRequired: true,
        labelId: "general.title",
        name: "title",
        placeholderId: "form.placeholders.type",
        type: "text",
    },
];
