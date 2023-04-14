import { Input } from "@chakra-ui/react";
import { FormField } from "@/types";

export const addMenuItemForm: FormField[] = [
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
        labelId: "pages.menu.img-title",
        name: "imageTitle",
        placeholderId: "form.placeholders.type",
        type: "text",
    },
    {
        fieldType: Input,
        isRequired: true,
        labelId: "pages.menu.img-url",
        name: "imageSource",
        placeholderId: "form.placeholders.type",
        type: "text",
        validationSchema: "url",
    },
    {
        fieldType: Input,
        isRequired: true,
        labelId: "general.url",
        name: "url",
        placeholderId: "form.placeholders.type",
        type: "text",
        validationSchema: "relative-full-url",
        automaticFill: {
            superiorField: "title",
            fillType: "relative-url",
        },
    },
];
