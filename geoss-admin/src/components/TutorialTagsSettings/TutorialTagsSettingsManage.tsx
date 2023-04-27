import { useContext, useState } from "react";
import { Formik, FormikHelpers, FormikValues } from "formik";
import { Box, Flex } from "@chakra-ui/react";
import { TextContent, PrimaryButton, FormField } from "@/components";
import { TutorialTagsContext } from "@/context";
import { areObjectsEqual, setExistingFormValues, setFormInitialValues } from "@/utils/helpers";
import { ButtonType } from "@/types";
import { scrollbarStyles } from "@/theme/commons";
import { addTutorialTagForm } from "@/data/forms";

export const TutorialTagsSettingsManage = () => {
    const [isSaving, setIsSaving] = useState<boolean>(false);
    const { addNewTag, updateTag, editedTag } = useContext(TutorialTagsContext);
    const [initValues, setInitValues] = useState<FormikValues>(
        editedTag ? setExistingFormValues(addTutorialTagForm, editedTag) : setFormInitialValues(addTutorialTagForm)
    );

    const handleFormSubmit = async (values: FormikValues, actions: FormikHelpers<FormikValues>) => {
        setIsSaving(true);
        !editedTag ? await addNewTag(values, actions) : await updateTag(values, editedTag.id, updateFormState);
        setIsSaving(false);
    };

    const updateFormState = (values: FormikValues) => setInitValues(setExistingFormValues(addTutorialTagForm, values));

    const renderFormFields = () => {
        const formFields = addTutorialTagForm.map((field) => <FormField key={field.name} fieldData={field} />);
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
                disabled={(!!editedTag && areObjectsEqual(initValues, values)) || isSaving}
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
