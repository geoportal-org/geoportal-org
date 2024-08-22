import { useContext, useState } from "react";
import { Formik, FormikHelpers, FormikValues } from "formik";
import { Box, Flex } from "@chakra-ui/react";
import { PrimaryButton, FormField, TextContent } from "@/components";
import { FileRepositoryService } from "@/services/api";
import useCustomToast from "@/utils/useCustomToast";
import useFormatMsg from "@/utils/useFormatMsg";
import { areObjectsEqual, getIdFromUrl, setExistingFormValues, setFormInitialValues } from "@/utils/helpers";
import { addFileForm, editFileForm } from "@/data/forms";
import { scrollbarStyles } from "@/theme/commons";
import { ButtonType, FileRepositoryManageFileProps, ToastStatus } from "@/types";
import { IDocument, IErrorObject } from "@/types/models";
import { SiteContext, SiteContextValue } from "@/context/CurrentSiteContext";

export const FileRepositoryManageFile = ({
    fileId,
    currentFolder,
    path,
    documentsList,
    setDocumentsList,
    file,
}: FileRepositoryManageFileProps) => {
    const fileForm = fileId ? editFileForm : addFileForm;
    const [isSaving, setIsSaving] = useState<boolean>(false);
    const [initValues, setInitValues] = useState<FormikValues>(
        file ? setExistingFormValues(fileForm, file) : setFormInitialValues(fileForm)
    );
    const { showToast } = useCustomToast();
    const { translate } = useFormatMsg();

    //siteId
    const { currentSiteId } = useContext<SiteContextValue>(SiteContext);

    const handleFormSubmit = async (values: FormikValues, actions: FormikHelpers<FormikValues>) => {
        setIsSaving(true);
        fileId ? await updateFile(fileId, values) : await addNewFile(values, actions);
        setIsSaving(false);
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
        } catch (e: any) {
            const { errorStatus, errorInfo } = e;
            let msg = "";
            if (errorInfo.errors.length) {
                msg = errorInfo.errors[0].message;
            } else {
                console.log(errorStatus);
                msg = JSON.parse(errorInfo).detail;
            }
            showErrorInfo(errorStatus && errorStatus === 417 ? "not-unique-file-name" : msg);
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
        } catch (e: any) {
            const { errorStatus, errorInfo } = e;
            let msg = "";
            if (errorInfo.errors.length) {
                msg = errorInfo.errors[0].message;
            } else {
                console.log(errorStatus);
                msg = JSON.parse(errorInfo).detail;
            }
            showErrorInfo(errorStatus && errorStatus === 417 ? "not-unique-file-name" : msg);
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
            siteId: currentSiteId,
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
                disabled={(fileId !== undefined && areObjectsEqual(initValues, values)) || isSaving}
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
