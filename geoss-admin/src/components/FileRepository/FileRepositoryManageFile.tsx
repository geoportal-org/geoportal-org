import { addFileForm } from "@/data/forms";
import { scrollbarStyles } from "@/theme/commons";
import useCustomToast from "@/utils/useCustomToast";
import { Box, Flex } from "@chakra-ui/react";
import { Formik, FormikHelpers, FormikValues } from "formik";
import { useEffect, useState } from "react";
import { PrimaryButton, Loader, FormField, TextContent } from "@/components";
import { ButtonType } from "@/types";
import { areObjectsEqual, setFormInitialValues } from "@/utils/helpers";

type FileRepositoryManageFileProps = {
    fileId?: number;
};

export const FileRepositoryManageFile = ({ fileId }: FileRepositoryManageFileProps) => {
    const [isLoading, setIsLoading] = useState(true);
    const [initValues, setInitValues] = useState<FormikValues>(setFormInitialValues(addFileForm));
    const { showToast } = useCustomToast();

    useEffect(() => {
        fileId ? getFileInfo(fileId) : setIsLoading(false);
    }, []);

    const getFileInfo = (id: number) => {};

    const handleFormSubmit = (values: FormikValues, actions: FormikHelpers<FormikValues>) => {
        fileId ? updateFile(fileId, values) : addNewFile(values, actions);
    };

    const updateFile = (id: number, values: FormikValues) => {};

    const addNewFile = (values: FormikValues, actions: FormikHelpers<FormikValues>) => {};

    const renderFormFields = () => {
        const formFields = addFileForm.map((field) => <FormField key={field.name} fieldData={field} />);
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
                disabled={fileId !== undefined && areObjectsEqual(initValues, values)}
            >
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
