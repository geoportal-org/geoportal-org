import { useContext, useState } from "react";
import { Formik, FormikHelpers, FormikValues } from "formik";
import { Box, Flex } from "@chakra-ui/react";
import { TextContent, PrimaryButton, FormField } from "@/components";
import { DefaultLayerContext } from "@/context";
import { areObjectsEqual, setExistingFormValues, setFormInitialValues } from "@/utils/helpers";
import { ButtonType } from "@/types";
import { scrollbarStyles } from "@/theme/commons";
import { addLayerForm } from "@/data/forms";

export const DefaultLayerSettingsManage = () => {
    const [isSaving, setIsSaving] = useState<boolean>(false);
    const { addNewLayer, updateLayer, editedLayer } = useContext(DefaultLayerContext);
    const [initValues, setInitValues] = useState<FormikValues>(
        editedLayer ? setExistingFormValues(addLayerForm, editedLayer) : setFormInitialValues(addLayerForm)
    );

    const handleFormSubmit = async (values: FormikValues, actions: FormikHelpers<FormikValues>) => {
        setIsSaving(true);
        !editedLayer ? await addNewLayer(values, actions) : await updateLayer(values, editedLayer.id, updateFormState);
        setIsSaving(false);
    };

    const updateFormState = (values: FormikValues) => setInitValues(setExistingFormValues(addLayerForm, values));

    const renderFormFields = () => {
        const formFields = addLayerForm.map((field) => <FormField key={field.name} fieldData={field} />);
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
                disabled={(!!editedLayer && areObjectsEqual(initValues, values)) || isSaving}
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
