import { Input } from "@chakra-ui/react";
import { FormField } from "@/types";

export const addContentForm: FormField[] = [
    {
        fieldType: Input,
        isRequired: true,
        labelId: "general.title",
        name: "title",
        placeholderId: "form.placeholders.type",
        type: "text",
    },
    {
        isRequired: true,
        labelId: "pages.contents.content",
        name: "data",
        type: "editor",
    },
];
