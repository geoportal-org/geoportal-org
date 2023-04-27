import { useState } from "react";
import { Formik, FormikHelpers, FormikValues } from "formik";
import { Box, Flex } from "@chakra-ui/react";
import { FormField, PrimaryButton, TextContent } from "@/components";
import { addChildMenuItemForm, addMenuItemForm } from "@/data/forms";
import { areObjectsEqual, setExistingFormValues, setFormInitialValues } from "@/utils/helpers";
import { ButtonType, MenuContentManageProps } from "@/types";
import { scrollbarStyles } from "@/theme/commons";

export const MenuContentManage = ({
    parentMenuId,
    menuItemId,
    manageMenuItem,
    isMainMenuItem,
    menuItem,
}: MenuContentManageProps) => {
    const itemForm = isMainMenuItem ? addMenuItemForm : addChildMenuItemForm;
    const [isSaving, setIsSaving] = useState<boolean>(false);
    const [initValues, setInitValues] = useState<FormikValues>(
        menuItem ? setExistingFormValues(itemForm, menuItem) : setFormInitialValues(itemForm)
    );

    const handleFormSubmit = async (values: FormikValues, actions: FormikHelpers<FormikValues>) => {
        setIsSaving(true);
        await manageMenuItem(parentMenuId, values, actions, setInitValues, menuItemId);
        setIsSaving(false);
    };

    const renderFormFields = () => {
        const formFieldsData = menuItem ? itemForm.map((field) => ({ ...field, automaticFill: undefined })) : itemForm;
        const formFields = formFieldsData.map((field) => <FormField key={field.name} fieldData={field} />);
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
                const { handleSubmit, values } = formikProps;
                return (
                    <Box overflowY="auto" minH="full" h="full" pr={1} sx={scrollbarStyles}>
                        <form onSubmit={handleSubmit} noValidate>
                            {renderFormFields()}
                            {renderFormFooter(values)}
                        </form>
                    </Box>
                );
            }}
        </Formik>
    );
};
