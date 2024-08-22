import { useContext, useState } from "react";
import { Formik, FormikHelpers, FormikValues } from "formik";
import { Box, Flex } from "@chakra-ui/react";
import { TextContent, PrimaryButton, FormField } from "@/components";
import { FileRepositoryService } from "@/services/api";
import { areObjectsEqual, getIdFromUrl, setExistingFormValues, setFormInitialValues } from "@/utils/helpers";
import useCustomToast from "@/utils/useCustomToast";
import { scrollbarStyles } from "@/theme/commons";
import { createFolderForm } from "@/data/forms";
import { ButtonType, FileRepositoryManageFolderProps, ToastStatus } from "@/types";
import { IFolderData } from "@/types/models";
import { SiteContext, SiteContextValue } from "@/context/CurrentSiteContext";
import useFormatMsg from "@/utils/useFormatMsg";

export const FileRepositoryManageFolder = ({
    folderId,
    currFolder,
    path,
    foldersList,
    setFoldersList,
    folder,
}: FileRepositoryManageFolderProps) => {
    const [isSaving, setIsSaving] = useState<boolean>(false);
    const [initValues, setInitValues] = useState<FormikValues>(
        folder ? setExistingFormValues(createFolderForm, folder) : setFormInitialValues(createFolderForm)
    );
    const { showToast } = useCustomToast();
    const { translate } = useFormatMsg();
    //siteId
    const { currentSiteId } = useContext<SiteContextValue>(SiteContext);

    const handleFormSubmit = async (values: FormikValues, actions: FormikHelpers<FormikValues>) => {
        setIsSaving(true);
        folderId ? await updateFolder(folderId, values) : await createNewFolder(values, actions);
        setIsSaving(false);
    };

    const updateFolder = async (id: number, values: FormikValues) => {
        const folderData = { title: values.title, path };
        try {
            const updatedFolder = await FileRepositoryService.updateFolder(id, folderData);
            setFoldersList((prevFoldersList) =>
                prevFoldersList.map((folder) =>
                    +getIdFromUrl(folder._links.self.href) === id ? updatedFolder : folder
                )
            );
            setInitValues(setExistingFormValues(createFolderForm, values));
            showToast({
                title: "Folder updated",
                description: "Folder title updated",
            });
        } catch (e: any) {
            console.error(e);
            let msg = "";
            if (e.errorInfo?.length) {
                msg = JSON.parse(e.errorInfo).detail;
            } else {
                msg = e.errorInfo.message || e.errorInfo.errors[0].message;
            }
            showToast({
                title: translate("general.error"),
                description: `${msg || ""}`,
                status: ToastStatus.ERROR,
            });
        }
    };

    const createNewFolder = async (values: FormikValues, actions: FormikHelpers<FormikValues>) => {
        const folderData: IFolderData = {
            title: values.title,
            parentFolderId: currFolder,
            path,
            siteId: currentSiteId,
        };
        try {
            const addedFolder = await FileRepositoryService.createFolder(folderData);
            const newFolderId = +getIdFromUrl(addedFolder._links.folder.href);
            const newFolder = await FileRepositoryService.getFolder(newFolderId);
            setFoldersList([...foldersList.current, newFolder]);
            actions.resetForm();
            showToast({
                title: "Folder created",
                description: `Folder ${newFolder.title} has been created`,
            });
        } catch (e: any) {
            let msg = "";
            if (e.errorInfo?.length) {
                msg = JSON.parse(e.errorInfo).detail;
            } else {
                msg = e.errorInfo.message || e.errorInfo.errors[0].message;
            }
            console.log(e);
            console.log(msg);
            showToast({
                title: translate("general.error"),
                description: `${msg || ""}`,
                status: ToastStatus.ERROR,
            });
        }
    };

    const renderFormFields = () => {
        const formFields = createFolderForm.map((field) => <FormField key={field.name} fieldData={field} />);
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
                disabled={(folderId !== undefined && areObjectsEqual(initValues, values)) || isSaving}
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
