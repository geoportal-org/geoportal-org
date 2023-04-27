import { useContext, useState } from "react";
import { Formik, FormikHelpers, FormikValues } from "formik";
import { Box, Flex } from "@chakra-ui/react";
import { TextContent, PrimaryButton, FormField } from "@/components";
import { ViewsContext } from "@/context";
import { areObjectsEqual, setExistingFormValues, setFormInitialValues } from "@/utils/helpers";
import { ButtonType } from "@/types";
import { scrollbarStyles } from "@/theme/commons";
import { addViewForm, addSubViewForm } from "@/data/forms";

export const ViewsSettingsManage = () => {
    const [isSaving, setIsSaving] = useState<boolean>(false);
    const { addNewView, updateView, editedView } = useContext(ViewsContext);
    const currentForm = !editedView || (editedView.viewId && !editedView.parentViewId) ? addViewForm : addSubViewForm;
    const [initValues, setInitValues] = useState<FormikValues>(
        editedView && editedView.item
            ? setExistingFormValues(currentForm, editedView.item)
            : setFormInitialValues(currentForm)
    );

    const handleFormSubmit = async (values: FormikValues, actions: FormikHelpers<FormikValues>) => {
        setIsSaving(true);
        !editedView || (editedView && !editedView.isEditMode)
            ? await addNewView(values, actions)
            : await updateView(values, updateFormState);
        setIsSaving(false);
    };

    const updateFormState = (values: FormikValues) => setInitValues(setExistingFormValues(currentForm, values));

    const renderFormFields = () => {
        const formFields = currentForm.map((field) => <FormField key={field.name} fieldData={field} />);
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
                disabled={(!!editedView && editedView.isEditMode && areObjectsEqual(initValues, values)) || isSaving}
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
