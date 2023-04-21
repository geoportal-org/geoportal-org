import { Input, Textarea } from "@chakra-ui/react";
import { Select } from "chakra-react-select";
import { FormField } from "@/types";
import { TagPlacement, TagType } from "@/types/models";

export const addTutorialTagForm: FormField[] = [
    {
        fieldType: Input,
        isRequired: true,
        labelId: "general.id",
        name: "name",
        placeholderId: "form.placeholders.type",
        type: "text",
    },
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
        labelId: "general.description",
        name: "description",
        placeholderId: "form.placeholders.type",
    },
    {
        fieldType: Select,
        isRequired: true,
        labelId: "pages.tags.type",
        name: "type",
        placeholderId: "form.placeholders.select-one",
        type: "select",
        selectSettings: {
            isMultiselect: false,
            isTranslated: true,
            options: [
                { label: "pages.tags.tip", value: TagType.TIP },
                { label: "pages.tags.new", value: TagType.NEW },
            ],
        },
    },
    {
        fieldType: Select,
        isRequired: true,
        labelId: "pages.tags.show",
        name: "show",
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
    {
        fieldType: Select,
        isRequired: true,
        labelId: "pages.tags.placement",
        name: "placement",
        placeholderId: "form.placeholders.select-one",
        type: "select",
        selectSettings: {
            isMultiselect: false,
            isTranslated: true,
            options: [
                { label: "pages.tags.left-top", value: TagPlacement.LEFT_TOP },
                { label: "pages.tags.left-bottom", value: TagPlacement.LEFT_BOTTOM },
                { label: "pages.tags.left-center", value: TagPlacement.LEFT_CENTER },
                { label: "pages.tags.right-top", value: TagPlacement.RIGHT_TOP },
                { label: "pages.tags.right-bottom", value: TagPlacement.RIGHT_BOTTOM },
                { label: "pages.tags.right-center", value: TagPlacement.RIGHT_CENTER },
                { label: "pages.tags.top-center", value: TagPlacement.TOP_CENTER },
                { label: "pages.tags.bottom-center", value: TagPlacement.BOTTOM_CENTER },
                { label: "pages.tags.center-center", value: TagPlacement.CENTER_CENTER },
            ],
        },
    },
];
