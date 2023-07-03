import { useState } from "react";
import { Formik, FormikHelpers, FormikValues } from "formik";
import { Box, Flex } from "@chakra-ui/react";
import { FormField, PrimaryButton, TextContent } from "@/components";
import { addChildMenuItemForm, addMenuItemForm } from "@/data/forms";
import { areObjectsEqual, isTranslatedValueAdded, setExistingFormValues, setFormInitialValues } from "@/utils/helpers";
import { ButtonType, LocaleNames, MenuContentManageProps } from "@/types";
import { scrollbarStyles } from "@/theme/commons";
import { defaultUsedLang } from "@/data";

export const MenuContentManage = ({
    parentMenuId,
    menuItemId,
    manageMenuItem,
    isMainMenuItem,
    menuItem,
}: MenuContentManageProps) => {
    const itemForm = isMainMenuItem ? addMenuItemForm : addChildMenuItemForm;
    const [currentTranslation, setCurrentTranslation] = useState<LocaleNames>(defaultUsedLang);
    const [isSaving, setIsSaving] = useState<boolean>(false);
    const [initValues, setInitValues] = useState<FormikValues>(
        menuItem ? setExistingFormValues(itemForm, menuItem) : setFormInitialValues(itemForm)
    );

    const handleFormSubmit = async (values: FormikValues, actions: FormikHelpers<FormikValues>) => {
        setIsSaving(true);
        await manageMenuItem(parentMenuId, values, actions, setInitValues, currentTranslation, menuItemId);
        setIsSaving(false);
    };

    const completeMissingTranslations = (
        values: FormikValues,
        setFieldValue: (field: string, value: FormikValues) => void,
        setFieldTouched: (field: string, isTouched?: boolean | undefined, shouldValidate?: boolean) => void,
        selectedLang: LocaleNames
    ) => {
        itemForm.forEach(({ translationInfo, name, type }) => {
            setFieldTouched(name, false, false);
            if (!translationInfo) {
                return;
            }
            const { genericName } = translationInfo;
            const defaultTranslation = values[genericName][defaultUsedLang] ? defaultUsedLang : currentTranslation;
            if (isTranslatedValueAdded(translationInfo, type, values)) {
                return;
            }
            setFieldValue(name, values[genericName][defaultTranslation]);
        });
        setCurrentTranslation(selectedLang);
    };

    const renderFormFields = (
        values: FormikValues,
        setFieldValue: (field: string, value: FormikValues) => void,
        setFieldTouched: (field: string, isTouched?: boolean | undefined, shouldValidate?: boolean) => void
    ) => {
        const formFieldsData = menuItem ? itemForm.map((field) => ({ ...field, automaticFill: undefined })) : itemForm;
        const formFields = formFieldsData.map((field) => {
            const isInvisible = field.translationInfo
                ? field.translationInfo.translation !== currentTranslation
                : false;
            const isRequired = field.isRequired && !isInvisible;
            return (
                <FormField
                    key={field.name}
                    fieldData={{ ...field, isRequired }}
                    invisible={isInvisible}
                    currentLang={currentTranslation}
                    onInputTranslationChange={
                        field.translationInfo
                            ? (selectedLang) =>
                                  completeMissingTranslations(values, setFieldValue, setFieldTouched, selectedLang)
                            : undefined
                    }
                />
            );
        });
        return (
            <Flex direction="column" gap={6} mb={6}>
                {formFields}
            </Flex>
        );
    };

    const renderFormFooter = (values: FormikValues) => (
        <Flex justifyContent="flex-end" py={1} w="full">
            <PrimaryButton
                type={ButtonType.SUBMIT}
                disabled={(menuItemId !== undefined && areObjectsEqual(initValues, values)) || isSaving}
            >
                <TextContent id="general.save" />
            </PrimaryButton>
        </Flex>
    );

    return (
        <Formik initialValues={initValues} onSubmit={handleFormSubmit}>
            {(formikProps) => {
                const { handleSubmit, values, setFieldValue, setFieldTouched } = formikProps;
                return (
                    <Box overflowY="auto" minH="full" h="full" pr={1} sx={scrollbarStyles}>
                        <form onSubmit={handleSubmit} noValidate>
                            {renderFormFields(values, setFieldValue, setFieldTouched)}
                            {renderFormFooter(values)}
                        </form>
                    </Box>
                );
            }}
        </Formik>
    );
};
