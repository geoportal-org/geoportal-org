import { Input } from "@chakra-ui/react";
import { FormField } from "@/types";

export const addChildMenuItemForm: FormField[] = [
    {
        fieldType: Input,
        isRequired: true,
        labelId: "general.title",
        name: "title.en",
        placeholderId: "form.placeholders.type",
        type: "text",
        translationInfo: {
            genericName: "title",
            translation: "en",
        },
    },
    {
        fieldType: Input,
        isRequired: true,
        labelId: "general.title",
        name: "title.es",
        placeholderId: "form.placeholders.type",
        type: "text",
        translationInfo: {
            genericName: "title",
            translation: "es",
        },
    },
    {
        fieldType: Input,
        isRequired: true,
        labelId: "general.title",
        name: "title.fr",
        placeholderId: "form.placeholders.type",
        type: "text",
        translationInfo: {
            genericName: "title",
            translation: "fr",
        },
    },
    {
        fieldType: Input,
        isRequired: true,
        labelId: "general.title",
        name: "title.pl",
        placeholderId: "form.placeholders.type",
        type: "text",
        translationInfo: {
            genericName: "title",
            translation: "pl",
        },
    },
    {
        fieldType: Input,
        isRequired: true,
        labelId: "general.title",
        name: "title.zh",
        placeholderId: "form.placeholders.type",
        type: "text",
        translationInfo: {
            genericName: "title",
            translation: "zh",
        },
    },
    {
        fieldType: Input,
        isRequired: true,
        labelId: "general.title",
        name: "title.ru",
        placeholderId: "form.placeholders.type",
        type: "text",
        translationInfo: {
            genericName: "title",
            translation: "ru",
        },
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
