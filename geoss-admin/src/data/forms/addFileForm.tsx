import { Input } from "@chakra-ui/react";
import { FormField } from "@/types";

export const addFileForm: FormField[] = [
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
        labelId: "pages.file-repository.file",
        name: "file",
        type: "uploader",
    },
];
