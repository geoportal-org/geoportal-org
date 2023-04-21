import { useContext, useEffect, useState } from "react";
import { Formik, FormikHelpers, FormikValues } from "formik";
import { Box, Flex } from "@chakra-ui/react";
import { TextContent, PrimaryButton, Loader, FormField } from "@/components";
import { DefaultLayerService } from "@/services/api";
import { DefaultLayerContext } from "@/context";
import { areObjectsEqual, setExistingFormValues, setFormInitialValues } from "@/utils/helpers";
import { ButtonType } from "@/types";
import { scrollbarStyles } from "@/theme/commons";
import { addLayerForm } from "@/data/forms";

export const DefaultLayerSettingsManage = () => {
    const [isLoading, setIsLoading] = useState(true);
    const [initValues, setInitValues] = useState<FormikValues>(setFormInitialValues(addLayerForm));
    const { addNewLayer, updateLayer, editedLayerId } = useContext(DefaultLayerContext);

    useEffect(() => {
        editedLayerId && Number.isInteger(editedLayerId) ? getLayerInfo(editedLayerId) : setIsLoading(false);
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const getLayerInfo = async (id: number) => {
        try {
            const editedLayerItem = await DefaultLayerService.getLayer(id);
            setInitValues(setExistingFormValues(addLayerForm, editedLayerItem));
        } catch (e) {
            console.error(e);
        } finally {
            setIsLoading(false);
        }
    };

    const handleFormSubmit = async (values: FormikValues, actions: FormikHelpers<FormikValues>) =>
        !editedLayerId ? await addNewLayer(values, actions) : await updateLayer(values, editedLayerId, updateFormState);

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
            <PrimaryButton type={ButtonType.SUBMIT} disabled={!!editedLayerId && areObjectsEqual(initValues, values)}>
                <TextContent id="general.save" />
            </PrimaryButton>
        </Flex>
    );

    if (isLoading) {
        return <Loader />;
    }

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
