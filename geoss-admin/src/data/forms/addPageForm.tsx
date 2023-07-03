import { Input, Textarea } from "@chakra-ui/react";
import { FormField } from "@/types";
import { Select } from "chakra-react-select";

export const addPageForm: FormField[] = [
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
        fieldType: Textarea,
        isRequired: true,
        labelId: "pages.page.description",
        name: "description.en",
        placeholderId: "form.placeholders.type",
        translationInfo: {
            genericName: "description",
            translation: "en",
        },
    },
    {
        fieldType: Textarea,
        isRequired: true,
        labelId: "pages.page.description",
        name: "description.es",
        placeholderId: "form.placeholders.type",
        translationInfo: {
            genericName: "description",
            translation: "es",
        },
    },
    {
        fieldType: Textarea,
        isRequired: true,
        labelId: "pages.page.description",
        name: "description.fr",
        placeholderId: "form.placeholders.type",
        translationInfo: {
            genericName: "description",
            translation: "fr",
        },
    },
    {
        fieldType: Textarea,
        isRequired: true,
        labelId: "pages.page.description",
        name: "description.zh",
        placeholderId: "form.placeholders.type",
        translationInfo: {
            genericName: "description",
            translation: "zh",
        },
    },
    {
        fieldType: Textarea,
        isRequired: true,
        labelId: "pages.page.description",
        name: "description.ru",
        placeholderId: "form.placeholders.type",
        translationInfo: {
            genericName: "description",
            translation: "ru",
        },
    },
    {
        fieldType: Textarea,
        isRequired: true,
        labelId: "pages.page.description",
        name: "description.pl",
        placeholderId: "form.placeholders.type",
        translationInfo: {
            genericName: "description",
            translation: "pl",
        },
    },
    {
        fieldType: Select,
        isRequired: true,
        labelId: "pages.page.content",
        name: "contentId",
        placeholderId: "form.placeholders.select-one",
        type: "select",
    },
    {
        fieldType: Input,
        isRequired: true,
        labelId: "pages.page.slug",
        name: "slug",
        placeholderId: "form.placeholders.type",
        type: "text",
        validationSchema: "slug",
        automaticFill: {
            superiorField: "title",
            fillType: "slug",
        },
    },
];
