import { useEffect, useState } from "react";
import { Formik, FormikHelpers, FormikValues } from "formik";
import { Box, Flex } from "@chakra-ui/react";
import { Loader, TextContent, PrimaryButton, FormField } from "@/components";
import { FileRepositoryService } from "@/services/api";
import { createFolderForm } from "@/data/forms";
import { ButtonType, FileRepositoryAddFolderProps } from "@/types";
import { IFolderData } from "@/types/models";
import { areObjectsEqual, getIdFromUrl, setExistingFormValues, setFormInitialValues } from "@/utils/helpers";
import useCustomToast from "@/utils/useCustomToast";

export const FileRepositoryAddFolder = ({
    folderId,
    currFolder,
    path,
    foldersList,
    setFoldersList,
}: FileRepositoryAddFolderProps) => {
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [initValues, setInitValues] = useState<FormikValues>(setFormInitialValues(createFolderForm));
    const { showToast } = useCustomToast();

    useEffect(() => {
        folderId ? getFolderInfo(folderId) : setIsLoading(false);
    }, []);

    const getFolderInfo = async (id: number) => {
        try {
            // test client side
            const editedFolder = await FileRepositoryService.getFolder(id);
            setInitValues(setExistingFormValues(createFolderForm, editedFolder));
        } catch (e) {
            console.error(e);
        } finally {
            setIsLoading(false);
        }
    };

    const handleFormSubmit = (values: FormikValues, actions: FormikHelpers<FormikValues>) => {
        folderId ? updateFolder(folderId, values) : createNewFolder(values, actions);
    };

    const updateFolder = async (id: number, values: FormikValues) => {
        const folderData: Pick<IFolderData, "title"> = { title: values.title };
        try {
            // test client side
            const updatedFolder = await FileRepositoryService.updateFolderTitle(id, folderData);
            setFoldersList((foldersList) =>
                foldersList.map((folder) => (+getIdFromUrl(folder._links.self.href) === id ? updatedFolder : folder))
            );
            setInitValues(setExistingFormValues(createFolderForm, values));
            showToast({
                title: "Folder updated",
                description: "Folder title updated",
            });
        } catch (e) {
            console.error(e);
        }
    };

    const createNewFolder = async (values: FormikValues, actions: FormikHelpers<FormikValues>) => {
        const folderData: IFolderData = { title: values.title, parentFolderId: currFolder, path };
        try {
            // test client side
            const addedFolder = await FileRepositoryService.createFolder(folderData);
            const newFolderId = +getIdFromUrl(addedFolder._links.folder.href);
            // test client side
            const newFolder = await FileRepositoryService.getFolder(newFolderId);
            setFoldersList([...foldersList, newFolder]);
            actions.resetForm();
            showToast({
                title: "Folder created",
                description: `Folder ${newFolder.title} has been created`,
            });
        } catch (e) {
            console.error(e);
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
                color="brand.accept"
                disabled={folderId !== undefined && areObjectsEqual(initValues, values)}
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
                    <Box>
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
