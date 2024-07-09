import { useRouter } from "next/router";
import { useContext, useEffect, useState } from "react";
import { Formik, FormikErrors, FormikHelpers, FormikState, FormikTouched, FormikValues } from "formik";
import { Flex } from "@chakra-ui/react";
import { FormField, Loader, MainContent, PrimaryButton, TextContent } from "@/components";
import { ContentService, PageService } from "@/services/api";
import {
    areObjectsEqual,
    createGroupedSelectItemsList,
    createSelectItemsList,
    createTouchedForm,
    isTranslatedValueAdded,
    setExistingFormValues,
    setFormInitialValues,
} from "@/utils/helpers";
import useCustomToast from "@/utils/useCustomToast";
import { defaultUsedLang, initContentsPagination, pagesRoutes } from "@/data";
import { addPageForm } from "@/data/forms";
import { ManagePageProps, ButtonType, ButtonVariant, ToastStatus, LocaleNames } from "@/types";
import { IContent, IPageData } from "@/types/models";
import useFormatMsg from "@/utils/useFormatMsg";
import { useIntl } from "react-intl";
import { SiteContext, SiteContextValue } from "@/context/CurrentSiteContext";
import { IContentGrouped } from "@/types/models/contents";

export const ManagePage = ({ isEditMode = false }: ManagePageProps) => {
    const [currentTranslation, setCurrentTranslation] = useState<LocaleNames>(defaultUsedLang);
    const [initValues, setInitValues] = useState<FormikValues>(() => setFormInitialValues(addPageForm));
    const [contentsList, setContentsList] = useState<IContent[] | IContentGrouped[]>([]);
    const [pageId, setPageId] = useState<string>("");
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [isDraft, setIsDraft] = useState<boolean>(false);
    const router = useRouter();
    const { translate } = useFormatMsg();
    const { showToast } = useCustomToast();
    const { locale } = useIntl();

    //siteId
    const { currentSiteId } = useContext<SiteContextValue>(SiteContext);

    useEffect(() => {
        if (router.isReady) {
            getPageData();
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [router.isReady, currentSiteId]);

    const getPageData = async () => {
        try {
            const {
                _embedded: { content },
            } = await ContentService.getContentList({ ...initContentsPagination, siteId: currentSiteId });
            const globalSiteContent = await (
                await ContentService.getContentList({ ...initContentsPagination, siteId: 0 })
            )._embedded.content;
            let groupedOptions: IContent[] | IContentGrouped[] = [];

            if (currentSiteId !== 0) {
                groupedOptions = [
                    {
                        label: "Current Community Portal",
                        options: content,
                    },
                    {
                        label: "Global Scope",
                        options: globalSiteContent,
                    },
                ];
            } else {
                groupedOptions = [
                    {
                        label: "Global Scope",
                        options: globalSiteContent,
                    },
                ];
            }

            setContentsList(() => groupedOptions);
            if (isEditMode) {
                const id = router.query.id as string;
                setPageId(id);
                const editedPage = await PageService.getPage(+id);
                setInitValues(() => setExistingFormValues(addPageForm, editedPage));
                setIsDraft(!editedPage.published);
            }
            setIsLoading(false);
        } catch (e) {
            const err = e as { errorInfo: any; errorStatus: number };
            const { errorStatus } = err;
            const is404Error = errorStatus === 404;
            showToast({
                title: translate(is404Error ? "general.invalid-id" : "general.error"),
                description: translate(is404Error ? "pages.manage-page.not-exist" : "information.error.loading"),
                status: is404Error ? ToastStatus.WARNING : ToastStatus.ERROR,
            });
            router.push(pagesRoutes.page);
        }
    };

    const completeMissingTranslations = (
        values: FormikValues,
        setFieldValue: (field: string, value: FormikValues) => void,
        setFieldTouched: (field: string, isTouched?: boolean | undefined, shouldValidate?: boolean) => void,
        selectedLang: LocaleNames
    ) => {
        addPageForm.forEach(({ translationInfo, name, type }) => {
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
        const pageData = getPageEditedData(values);
        try {
            const { title } = !isEditMode
                ? await PageService.createPage(pageData)
                : await PageService.updatePage(+pageId, pageData);
            !isEditMode && actions.resetForm();
            isEditMode && setInitValues(() => setExistingFormValues(addPageForm, pageData));
            setIsDraft(false);
            showToast({
                title: translate(`pages.manage-page.${isEditMode ? "page-updated" : "page-created"}`),
                description: translate(`pages.manage-page.${isEditMode ? "page-updated-info" : "page-created-info"}`, {
                    title: title[(locale as LocaleNames) || defaultUsedLang],
                }),
            });
        } catch (e) {
            showToast({
                title: translate("general.error"),
                description: translate("information.error.page-submit"),
                status: ToastStatus.ERROR,
            });
        }
    };

    const getPageEditedData = (values: FormikValues, isPublished = true): IPageData => {
        addPageForm.forEach(({ translationInfo, type }) => {
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
        const pageData = {
            ...values,
            contentId: +values.contentId,
            published: isPublished,
            siteId: currentSiteId,
        } as IPageData;
        return pageData;
    };

    const renderFormFields = (
        values: FormikValues,
        setFieldValue: (field: string, value: FormikValues) => void,
        setFieldTouched: (field: string, isTouched?: boolean | undefined, shouldValidate?: boolean) => void
    ) => {
        const formFieldsData = isEditMode
            ? addPageForm.map((field) => ({ ...field, automaticFill: undefined }))
            : addPageForm;
        const formFields = formFieldsData.map((field) => {
            const isInvisible = field.translationInfo
                ? field.translationInfo.translation !== currentTranslation
                : false;
            const isRequired = field.isRequired && !isInvisible;
            if (field.name === "contentId") {
                if (field.selectSettings?.isGroupedOptions) {
                    field.selectSettings = createGroupedSelectItemsList(
                        contentsList as IContentGrouped[],
                        field.selectSettings?.isMultiselect,
                        field.selectSettings?.isGroupedOptions,
                        locale as LocaleNames
                    );
                } else {
                    field.selectSettings = createSelectItemsList(
                        contentsList as IContent[],
                        field.selectSettings?.isMultiselect,
                        field.selectSettings?.isGroupedOptions,
                        locale as LocaleNames
                    );
                }
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
            const pageData = getPageEditedData(values, false);
            try {
                const { title } = !isEditMode
                    ? await PageService.createPage(pageData)
                    : await PageService.updatePage(+pageId, pageData);
                !isEditMode && resetForm();
                isEditMode && setInitValues(() => setExistingFormValues(addPageForm, pageData));
                setIsDraft(true);
                showToast({
                    title: translate(`pages.manage-page.${isEditMode ? "page-updated" : "page-created"}`),
                    description: `Page ${title[(locale as LocaleNames) || defaultUsedLang]} has been saved as draft`,
                });
            } catch (error) {
                showToast({
                    title: translate("general.error"),
                    description: translate("information.error.page-save"),
                    status: ToastStatus.ERROR,
                });
            }
        };

        const handleDraftSave = () => {
            setTouched(createTouchedForm(addPageForm));
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
        <MainContent
            titleId={`pages.manage-page.${isEditMode ? "edit-title" : "add-title"}`}
            backPath={pagesRoutes.page}
        >
            <Flex direction="column" maxW="container.m" w="100%" m="0 auto">
                <Formik initialValues={initValues} onSubmit={handleFormSubmit}>
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
                        return (
                            <form onSubmit={handleSubmit} noValidate>
                                {renderFormFields(values, setFieldValue, setFieldTouched)}
                                {renderFormFooter(values, setTouched, validateForm, resetForm)}
                            </form>
                        );
                    }}
                </Formik>
            </Flex>
        </MainContent>
    );
};
