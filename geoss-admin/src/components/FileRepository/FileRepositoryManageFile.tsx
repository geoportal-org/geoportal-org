import { useEffect, useState } from "react";
import { Formik, FormikHelpers, FormikValues } from "formik";
import { Box, Flex } from "@chakra-ui/react";
import { PrimaryButton, Loader, FormField, TextContent } from "@/components";
import { FileRepositoryService } from "@/services/api";
import useCustomToast from "@/utils/useCustomToast";
import useFormatMsg from "@/utils/useFormatMsg";
import { areObjectsEqual, getIdFromUrl, setExistingFormValues, setFormInitialValues } from "@/utils/helpers";
import { addFileForm, editFileForm } from "@/data/forms";
import { scrollbarStyles } from "@/theme/commons";
import { ButtonType, FileRepositoryManageFileProps, ToastStatus } from "@/types";
import { IDocument, IErrorObject } from "@/types/models";

export const FileRepositoryManageFile = ({
    fileId,
    currentFolder,
    path,
    documentsList,
    setDocumentsList,
}: FileRepositoryManageFileProps) => {
    const fileForm = fileId ? editFileForm : addFileForm;
    const [isLoading, setIsLoading] = useState(true);
    const [initValues, setInitValues] = useState<FormikValues>(setFormInitialValues(fileForm));
    const { showToast } = useCustomToast();
    const { translate } = useFormatMsg();

    useEffect(() => {
        fileId ? getFileInfo(fileId) : setIsLoading(false);
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const getFileInfo = async (id: number) => {
        try {
            const editedFile = await FileRepositoryService.getFile(id);
            setInitValues(setExistingFormValues(fileForm, editedFile));
        } catch (e) {
            console.log(e);
        } finally {
            setIsLoading(false);
        }
    };

    const handleFormSubmit = (values: FormikValues, actions: FormikHelpers<FormikValues>) => {
        fileId ? updateFile(fileId, values) : addNewFile(values, actions);
    };

    const updateFile = async (id: number, values: FormikValues) => {
        const fileData: Pick<IDocument, "title"> = { title: values.title };
        try {
            const updatedFile = await FileRepositoryService.updateFileTitle(id, fileData);
            setDocumentsList((prevDocumentsList) =>
                prevDocumentsList.map((file) => (+getIdFromUrl(file._links.self.href) === id ? updatedFile : file))
            );
            setInitValues(setExistingFormValues(fileForm, values));
            showToast({
                title: "File updated",
                description: "File title updated",
            });
        } catch (e) {
            const err = e as { errorInfo: IErrorObject; errorStatus: number };
            const { errorStatus, errorInfo } = err;
            console.log(errorInfo);
            console.log(errorStatus);
            showErrorInfo(errorStatus && errorStatus === 417 ? "not-unique-file-name" : errorStatus.toString());
        }
    };

    const addNewFile = async (values: FormikValues, actions: FormikHelpers<FormikValues>) => {
        const fileData = prepareFileToUpload(values);

        try {
            const addedFile = await FileRepositoryService.uploadFile(fileData);
            const newFileId = +getIdFromUrl(addedFile._links.document.href);
            const newFile = await FileRepositoryService.getFile(newFileId);
            setDocumentsList([...documentsList.current, newFile]);
            actions.resetForm();
            showToast({
                title: "File uploaded",
                description: `File ${addedFile.title} has been uploaded`,
            });
        } catch (e) {
            const err = e as { errorInfo: IErrorObject; errorStatus: number };
            const { errorStatus, errorInfo } = err;
            console.log(errorInfo);
            console.log(errorStatus);
            showErrorInfo(errorStatus && errorStatus === 417 ? "not-unique-file-name" : errorStatus.toString());
        }
    };

    const prepareFileToUpload = (values: FormikValues): FormData => {
        const formData = new FormData();
        const file = values.file as File;
        const fileInfo = getNewFileData(values);
        formData.set("files", file, file.name);
        formData.set("model", JSON.stringify(fileInfo));
        return formData;
    };

    const getNewFileData = (values: FormikValues) => {
        const file = values.file as File;
        const name = file.name;
        const extension = name.substring(name.lastIndexOf(".") + 1, name.length);
        return {
            title: values.title,
            fileName: name,
            extension,
            path: path,
            folderId: currentFolder,
        };
    };

    const renderFormFields = () => {
        const formFields = fileForm.map((field) => <FormField key={field.name} fieldData={field} />);
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
                <TextContent id={fileId ? "general.save" : "general.upload"} />
            </PrimaryButton>
        </Flex>
    );

    const showErrorInfo = (msgId: string) =>
        showToast({
            title: translate("general.error"),
            description: translate(`information.error.${msgId}`),
            status: ToastStatus.ERROR,
        });

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
