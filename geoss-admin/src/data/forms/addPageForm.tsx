import { Input, Textarea } from "@chakra-ui/react";
import { FormField } from "@/types";
import { Select } from "chakra-react-select";

export const addPageForm: FormField[] = [
    {
        fieldType: Input,
        isRequired: true,
        labelId: "general.title",
        name: "title",
        placeholderId: "form.placeholders.type",
        type: "text",
    },
    {
        fieldType: Textarea,
        isRequired: true,
        labelId: "pages.page.description",
        name: "description",
        placeholderId: "form.placeholders.type",
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
    },
];
