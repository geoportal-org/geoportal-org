import { FormField, FormSection } from "@/types";
import { Input } from "@chakra-ui/react";

const externalForm: FormField[] = [
    {
        fieldType: Input,
        isRequired: true,
        labelId: "pages.api.google",
        name: "googleMapsApiKey",
        placeholderId: "form.placeholders.type",
        type: "text",
    },
    {
        fieldType: Input,
        isRequired: true,
        labelId: "pages.api.w3w",
        name: "w3wKey",
        placeholderId: "form.placeholders.type",
        type: "text",
    },
];

const dabForm: FormField[] = [
    {
        fieldType: Input,
        isRequired: true,
        labelId: "pages.api.dab-base",
        name: "dabBaseUrl",
        placeholderId: "form.placeholders.type",
        type: "text",
        validationSchema: "url",
    },
    {
        fieldType: Input,
        isRequired: true,
        labelId: "pages.api.dab-data-providers",
        name: "dabDataProvidersUrl",
        placeholderId: "form.placeholders.type",
        type: "text",
        validationSchema: "url",
    },
    {
        fieldType: Input,
        isRequired: true,
        labelId: "pages.api.dab-view-api",
        name: "dabViewApiKey",
        placeholderId: "form.placeholders.type",
        type: "text",
    },
    {
        fieldType: Input,
        isRequired: true,
        labelId: "pages.api.dab-view-base",
        name: "dabViewBaseUrl",
        placeholderId: "form.placeholders.type",
        type: "text",
        validationSchema: "url",
    },
];

const knowledgeProducerForm: FormField[] = [
    {
        fieldType: Input,
        isRequired: true,
        labelId: "pages.api.kp-base",
        name: "kpBaseUrl",
        placeholderId: "form.placeholders.type",
        type: "text",
        validationSchema: "url",
    },
    {
        fieldType: Input,
        isRequired: true,
        labelId: "pages.api.kp-api",
        name: "kpApiKey",
        placeholderId: "form.placeholders.type",
        type: "text",
    },
];

const geossForm: FormField[] = [
    {
        fieldType: Input,
        isRequired: true,
        labelId: "pages.api.geoss-opensearch",
        name: "geossCrOpensearchUrl",
        placeholderId: "form.placeholders.type",
        type: "text",
        validationSchema: "url",
    },
    {
        fieldType: Input,
        isRequired: true,
        labelId: "pages.api.geoss-sync-manager",
        name: "geossCrSyncManagerUrl",
        placeholderId: "form.placeholders.type",
        type: "text",
        validationSchema: "url",
    },
    {
        fieldType: Input,
        isRequired: true,
        labelId: "pages.api.geoss-sync-manager-key",
        name: "geossCrSyncManagerSecretKey",
        placeholderId: "form.placeholders.type",
        type: "text",
    },
    {
        fieldType: Input,
        isRequired: true,
        labelId: "pages.api.geoss-relations-limit",
        name: "geossCrEntryRelationsLimit",
        placeholderId: "form.placeholders.type",
        type: "text",
    },
    {
        fieldType: Input,
        isRequired: true,
        labelId: "pages.api.geoss-transfer-limit",
        name: "geossCrTransferOptionsLimit",
        placeholderId: "form.placeholders.type",
        type: "text",
    },
];

const otherForm: FormField[] = [
    {
        fieldType: Input,
        isRequired: true,
        labelId: "pages.api.other-next-geoss-base",
        name: "nextGeossBaseUrl",
        placeholderId: "form.placeholders.type",
        type: "text",
        validationSchema: "url",
    },
];

export const apiSettingsForm: FormSection[] = [
    { titleId: "pages.api.external", set: "external", data: externalForm },
    { titleId: "pages.api.dab", set: "dab", data: dabForm },
    { titleId: "pages.api.knowledge-producer", set: "knowledge_producer", data: knowledgeProducerForm },
    //{ titleId: "pages.api.geoss", set: "geoss", data: geossForm }, GEOSS CR not included yet
    { titleId: "pages.api.other", set: "other", data: otherForm },
];

export const apiSettingsFormFields = apiSettingsForm.map((section) => section.data).flat();
