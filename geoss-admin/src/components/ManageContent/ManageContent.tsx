import { useRouter } from "next/router";
import { ReactNode, useState, useEffect, useContext } from "react";
import { Formik, FormikValues, FormikHelpers, FormikTouched, FormikErrors, FormikState } from "formik";
import { Box, useDisclosure, Flex } from "@chakra-ui/react";
import { FormField, Loader, MainContent, Modal, PrimaryButton, TextContent } from "@/components";
import { ValidationService } from "@/services";
import { ContentService, FileRepositoryService } from "@/services/api";
import { addContentForm } from "@/data/forms";
import { acceptedLogoExtensions, defaultUsedLang, initRepositoryPagination, pagesRoutes } from "@/data";
import { IContentData, IDocument } from "@/types/models";
import { ManageContentProps, ButtonType, ButtonVariant, ToastStatus, LocaleNames, SelectSettings } from "@/types";
import useCustomToast from "@/utils/useCustomToast";
import useFormatMsg from "@/utils/useFormatMsg";
import {
    areObjectsEqual,
    createSelectItemsList,
    createTouchedForm,
    generateGenericErrorMessage,
    isTranslatedValueAdded,
    setExistingFormValues,
    setFormInitialValues,
} from "@/utils/helpers";
import { useIntl } from "react-intl";
import { SiteContext, SiteContextValue } from "@/context/CurrentSiteContext";

export const ManageContent = ({ isEditMode = false }: ManageContentProps) => {
    const [currentTranslation, setCurrentTranslation] = useState<LocaleNames>(defaultUsedLang);
    const [modalBody, setModalBody] = useState<ReactNode>();
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [isDraft, setIsDraft] = useState<boolean>(false);
    const [initValues, setInitValues] = useState<FormikValues>(() => setFormInitialValues(addContentForm));
    const [contentId, setContentId] = useState<string>("");
    const [documentsList, setDocumentsList] = useState<SelectSettings>();
    const { isOpen: isOpenModal, onOpen: onOpenModal, onClose: onCloseModal } = useDisclosure();
    const router = useRouter();
    const { translate } = useFormatMsg();
    const { showToast } = useCustomToast();
    const { locale } = useIntl();

    //siteId
    const { currentSiteId } = useContext<SiteContextValue>(SiteContext);

    useEffect(() => {
        if (router.isReady) {
            if (isEditMode) {
                getEditedContent();
                getDocumentsList();
            } else {
                setIsLoading(false);
            }
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [router.isReady]);

    const getDocumentsList = async () => {
        try {
            const {
                _embedded: { document },
            } = await FileRepositoryService.getDocumentsListBySiteId({
                ...initRepositoryPagination,
                siteId: currentSiteId,
            });
            const selectDocumentsList = createSelectItemsList(
                document.filter(({ extension }) => acceptedLogoExtensions.includes(extension)),
                false,
                false,
                locale as LocaleNames
            );
            setDocumentsList(selectDocumentsList);
        } catch (e: any) {
            const msg = generateGenericErrorMessage(e);
            showToast({
                title: translate("general.error"),
                description: translate("pages.sites.siteCreationFail") + " " + msg,
                status: ToastStatus.ERROR,
            });
        }
    };

    const getEditedContent = async () => {
        const id = router.query.id as string;
        setContentId(id);
        try {
            const editedContent = await ContentService.getContent(+id);
            setInitValues(() => setExistingFormValues(addContentForm, editedContent));
            setIsDraft(!editedContent.published);
            setIsLoading(false);
        } catch (e: any) {
            const err = e as { errorInfo: any; errorStatus: number };
            const { errorStatus } = err;
            const is404Error = errorStatus === 404;
            showToast({
                title: translate(is404Error ? "general.invalid-id" : "general.error"),
                description: translate(is404Error ? "pages.manage-content.not-exist" : "information.error.loading"),
                status: is404Error ? ToastStatus.WARNING : ToastStatus.ERROR,
            });
            router.push(pagesRoutes.website);
        }
    };

    const completeMissingTranslations = (
        values: FormikValues,
        setFieldValue: (field: string, value: FormikValues) => void,
        setFieldTouched: (field: string, isTouched?: boolean | undefined, shouldValidate?: boolean) => void,
        selectedLang: LocaleNames
    ) => {
        addContentForm.forEach(({ translationInfo, name, type }) => {
            setFieldTouched(name, false, false);
            if (!translationInfo) {
                return;
            }
            const { genericName } = translationInfo;
            const defaultTranslation = values[genericName][defaultUsedLang] ? defaultUsedLang : currentTranslation;
            if (isTranslatedValueAdded(translationInfo, type, values)) {
                return;
            }
            setFieldValue(name, values[genericName][defaultTranslation]);
        });
        setCurrentTranslation(selectedLang);
    };

    const handleFormSubmit = async (values: FormikValues, actions: FormikHelpers<FormikValues>) => {
        const contentData = getContentInfo(values);

        try {
            const { title } = !isEditMode
                ? await ContentService.createContent(contentData)
                : await ContentService.updateContent(+contentId, contentData);
            !isEditMode && actions.resetForm();
            isEditMode && setInitValues(() => setExistingFormValues(addContentForm, contentData));
            setIsDraft(false);
            showToast({
                title: translate(`pages.manage-content.${isEditMode ? "content-updated" : "content-created"}`),
                description: translate(
                    `pages.manage-content.${isEditMode ? "content-updated-info" : "content-saved-info"}`,
                    {
                        title: title[(locale as LocaleNames) || defaultUsedLang],
                    }
                ),
            });
        } catch (error) {
            showToast({
                title: translate("general.error"),
                description: translate("information.error.content-submit"),
                status: ToastStatus.ERROR,
            });
        }
    };

    const getContentInfo = (values: FormikValues, isPublished = true): IContentData => {
        addContentForm.forEach(({ translationInfo, type }) => {
            if (!translationInfo) {
                return;
            }
            const { genericName, translation } = translationInfo;
            const defaultTranslation = values[genericName][defaultUsedLang] ? defaultUsedLang : currentTranslation;
            if (isTranslatedValueAdded(translationInfo, type, values)) {
                return;
            }
            values[genericName][translation] = values[genericName][defaultTranslation];
        });
        const { title, data, imageUrl } = values;
        const imgVal = imageUrl && imageUrl !== "" ? imageUrl : ''
        return {
            title,
            data,
            published: isPublished,
            siteId: currentSiteId,
            imageUrl: imgVal
        };
    };

    const showContentPreview = (values: FormikValues) => {
        setModalBody(renderContentPreview(values.data));
        onOpenModal();
    };

    const renderContentPreview = (text: string) => (
        <Box w="full" textAlign="initial" dangerouslySetInnerHTML={{ __html: text }} p={4} />
    );

    const renderFormFields = (
        values: FormikValues,
        setFieldValue: (field: string, value: FormikValues) => void,
        setFieldTouched: (field: string, isTouched?: boolean | undefined, shouldValidate?: boolean) => void
    ) => {
        const formFields = addContentForm.map((field) => {
            const isInvisible = field.translationInfo
                ? field.translationInfo.translation !== currentTranslation
                : false;
            const isRequired = field.isRequired && !isInvisible;
            if (field.name === "imageUrl") {
                field.selectSettings = documentsList;
            }
            return (
                <FormField
                    key={field.name}
                    fieldData={{ ...field, isRequired }}
                    invisible={isInvisible}
                    currentLang={currentTranslation}
                    onInputTranslationChange={
                        field.translationInfo
                            ? (selectedLang) =>
                                  completeMissingTranslations(values, setFieldValue, setFieldTouched, selectedLang)
                            : undefined
                    }
                />
            );
        });
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
                isEditMode && setInitValues(() => setExistingFormValues(addContentForm, contentData));
                setIsDraft(true);
                showToast({
                    title: translate(`pages.manage-content.${isEditMode ? "content-updated" : "content-created"}`),
                    description: translate("pages.manage-content.draft-saved", {
                        title: title[(locale as LocaleNames) || defaultUsedLang],
                    }),
                });
            } catch (error) {
                showToast({
                    title: translate("general.error"),
                    description: translate("information.error.content-save"),
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
                    disabled={isEditMode && areObjectsEqual(values, initValues) && isDraft}
                >
                    <TextContent id="form.actions.save-draft" />
                </PrimaryButton>
                <PrimaryButton
                    type={ButtonType.SUBMIT}
                    color="brand.accept"
                    disabled={isEditMode && areObjectsEqual(values, initValues) && !isDraft}
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
                validate={(values) => ValidationService.validateTextEditorContent(values.data, currentTranslation)}
            >
                {(formikProps) => {
                    const {
                        handleSubmit,
                        values,
                        setTouched,
                        validateForm,
                        resetForm,
                        setFieldValue,
                        setFieldTouched,
                    } = formikProps;
                    // hidden preview - probably will not be needed
                    /*const headingActions = [
                        {
                            titleId: "pages.manage-content.preview",
                            onClick: () => showContentPreview(values),
                            disabled: !!ValidationService.validateTextEditorContent(values.data, currentTranslation),
                        },
                    ];*/
                    return (
                        <MainContent
                            titleId={isEditMode ? "pages.manage-content.edit-title" : "pages.manage-content.add-title"}
                            // hidden preview - probably will not be needed
                            // actions={headingActions}
                            backPath={pagesRoutes.website}
                        >
                            <Flex direction="column" maxW="container.m" w="100%" m="0 auto">
                                <form onSubmit={handleSubmit} noValidate>
                                    {renderFormFields(values, setFieldValue, setFieldTouched)}
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
