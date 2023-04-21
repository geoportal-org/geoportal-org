import { useContext, useEffect, useState } from "react";
import { Formik, FormikHelpers, FormikValues } from "formik";
import { Box, Flex } from "@chakra-ui/react";
import { TextContent, PrimaryButton, Loader, FormField } from "@/components";
import { TutorialTagsService } from "@/services/api";
import { TutorialTagsContext } from "@/context";
import { areObjectsEqual, setExistingFormValues, setFormInitialValues } from "@/utils/helpers";
import { ButtonType } from "@/types";
import { scrollbarStyles } from "@/theme/commons";
import { addTutorialTagForm } from "@/data/forms";

export const TutorialTagsSettingsManage = () => {
    const [isLoading, setIsLoading] = useState(true);
    const [initValues, setInitValues] = useState<FormikValues>(setFormInitialValues(addTutorialTagForm));
    const { addNewTag, updateTag, editedTagId } = useContext(TutorialTagsContext);

    useEffect(() => {
        editedTagId && Number.isInteger(editedTagId) ? getTagInfo(editedTagId) : setIsLoading(false);
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const getTagInfo = async (id: number) => {
        try {
            const editedTag = await TutorialTagsService.getTag(id);
            setInitValues(setExistingFormValues(addTutorialTagForm, editedTag));
        } catch (e) {
            console.log(e);
        } finally {
            setIsLoading(false);
        }
    };

    const handleFormSubmit = async (values: FormikValues, actions: FormikHelpers<FormikValues>) =>
        !editedTagId ? await addNewTag(values, actions) : await updateTag(values, editedTagId, updateFormState);

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
            <PrimaryButton type={ButtonType.SUBMIT} disabled={!!editedTagId && areObjectsEqual(initValues, values)}>
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
