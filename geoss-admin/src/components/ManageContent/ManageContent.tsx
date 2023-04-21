import { useRouter } from "next/router";
import { ReactNode, useState, useEffect } from "react";
import { Formik, FormikValues, FormikHelpers, FormikTouched, FormikErrors, FormikState } from "formik";
import { Box, useDisclosure, Flex } from "@chakra-ui/react";
import { FormField, Loader, MainContent, Modal, PrimaryButton, TextContent } from "@/components";
import { ValidationService } from "@/services";
import { ContentService } from "@/services/api";
import { addContentForm } from "@/data/forms";
import { pagesRoutes } from "@/data";
import { IContentData } from "@/types/models";
import { ManageContentProps, ButtonType, ButtonVariant, ToastStatus } from "@/types";
import useCustomToast from "@/utils/useCustomToast";
import useFormatMsg from "@/utils/useFormatMsg";
import { areObjectsEqual, createTouchedForm, setExistingFormValues, setFormInitialValues } from "@/utils/helpers";

export const ManageContent = ({ isEditMode = false }: ManageContentProps) => {
    const [modalBody, setModalBody] = useState<ReactNode>();
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [initValues, setInitValues] = useState<FormikValues>(setFormInitialValues(addContentForm));
    const [contentId, setContentId] = useState<string>("");
    const { isOpen: isOpenModal, onOpen: onOpenModal, onClose: onCloseModal } = useDisclosure();
    const router = useRouter();
    const { translate } = useFormatMsg();
    const { showToast } = useCustomToast();

    useEffect(() => {
        if (router.isReady) {
            isEditMode ? getEditedContent() : setIsLoading(false);
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [router.isReady]);

    const getEditedContent = async () => {
        const id = router.query.id as string;
        setContentId(id);
        try {
            const response = await ContentService.getContent(+id);
            setInitValues(() => setExistingFormValues(addContentForm, response));
        } catch (e) {
            console.error(e);
        } finally {
            setIsLoading(false);
        }
    };

    const handleFormSubmit = async (values: FormikValues, actions: FormikHelpers<FormikValues>) => {
        const contentData = getContentInfo(values);
        try {
            const { title } = !isEditMode
                ? await ContentService.createContent(contentData)
                : await ContentService.updateContent(+contentId, contentData);
            !isEditMode && actions.resetForm();
            isEditMode && setInitValues(() => setExistingFormValues(addContentForm, values));
            showToast({
                title: isEditMode ? "Content updated" : "Content created",
                description: `Content ${title} has been ${isEditMode ? "updated" : "created"}`,
            });
        } catch (error) {
            console.error(error);
            showToast({
                title: "Error occured",
                description: "Content has not been submitted - please try again",
                status: ToastStatus.ERROR,
            });
        }
    };

    const getContentInfo = (values: FormikValues, isPublished = true): IContentData => {
        const { title, data } = values;
        return {
            title,
            data,
            published: isPublished,
        };
    };

    const showContentPreview = (values: FormikValues) => {
        setModalBody(renderContentPreview(values.data));
        onOpenModal();
    };

    const renderContentPreview = (text: string) => (
        <Box w="full" textAlign="initial" dangerouslySetInnerHTML={{ __html: text }} p={4} />
    );

    const renderFormFields = () => {
        const formFields = addContentForm.map((field) => <FormField key={field.name} fieldData={field} />);
        return (
            <Flex direction="column" gap={6} mb={6}>
                {formFields}
            </Flex>
        );
    };

    const renderFormFooter = (
        values: FormikValues,
        setTouched: (touched: FormikTouched<FormikValues>, shouldValidate?: boolean) => void,
        validateForm: (values?: any) => Promise<FormikErrors<FormikValues>>,
        resetForm: (nextState?: Partial<FormikState<FormikValues>>) => void
    ) => {
        const saveDraft = async () => {
            const contentData = getContentInfo(values, false);
            try {
                const { title } = !isEditMode
                    ? await ContentService.createContent(contentData)
                    : await ContentService.updateContent(+contentId, contentData);
                !isEditMode && resetForm();
                isEditMode && setInitValues(() => setExistingFormValues(addContentForm, values));
                showToast({
                    title: isEditMode ? "Content updated" : "Content created",
                    description: `Content ${title} has been saved as draft`,
                });
            } catch (error) {
                showToast({
                    title: "Error occured",
                    description: "Content has not been saved - please try again",
                    status: ToastStatus.ERROR,
                });
            }
        };

        const handleDraftSave = () => {
            setTouched(createTouchedForm(addContentForm));
            validateForm().then((errors) => {
                const isValid = !Object.keys(errors).length;
                return isValid ? saveDraft() : null;
            });
        };

        return (
            <Flex gap={3} justifyContent="flex-end" py={1}>
                <PrimaryButton
                    variant={ButtonVariant.GHOST}
                    onClick={handleDraftSave}
                    disabled={isEditMode && areObjectsEqual(values, initValues)}
                >
                    <TextContent id="form.actions.save-draft" />
                </PrimaryButton>
                <PrimaryButton
                    type={ButtonType.SUBMIT}
                    color="brand.accept"
                    disabled={isEditMode && areObjectsEqual(values, initValues)}
                >
                    <TextContent id={isEditMode ? "form.actions.submit-changes" : "form.actions.submit"} />
                </PrimaryButton>
            </Flex>
        );
    };

    if (isLoading) {
        return <Loader />;
    }

    return (
        <>
            <Formik
                initialValues={initValues}
                onSubmit={handleFormSubmit}
                validate={ValidationService.validateTextEditorContent}
            >
                {(formikProps) => {
                    const { handleSubmit, values, setTouched, validateForm, resetForm } = formikProps;
                    const headingActions = [
                        {
                            titleId: "pages.manage-content.preview",
                            onClick: () => showContentPreview(values),
                            disabled: !!ValidationService.validateTextEditorContent(values),
                        },
                    ];
                    return (
                        <MainContent
                            titleId={isEditMode ? "pages.manage-content.edit-title" : "pages.manage-content.add-title"}
                            actions={headingActions}
                            backPath={pagesRoutes.website}
                        >
                            <Flex direction="column" maxW="container.m" w="100%" m="0 auto">
                                <form onSubmit={handleSubmit} noValidate>
                                    {renderFormFields()}
                                    {renderFormFooter(values, setTouched, validateForm, resetForm)}
                                </form>
                            </Flex>
                        </MainContent>
                    );
                }}
            </Formik>

            <Modal
                modalHeader={translate("pages.contents.preview")}
                modalBody={modalBody}
                isModalOpen={isOpenModal}
                onModalClose={onCloseModal}
                size="4xl"
            />
        </>
    );
};
