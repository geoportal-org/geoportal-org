import { useRouter } from "next/router";
import { useEffect, useState } from "react";
import { Formik, FormikErrors, FormikHelpers, FormikState, FormikTouched, FormikValues } from "formik";
import { Flex } from "@chakra-ui/react";
import { FormField, Loader, MainContent, PrimaryButton, TextContent } from "@/components";
import { ContentService, PageService } from "@/services/api";
import {
    areObjectsEqual,
    createSelectItemsList,
    createTouchedForm,
    setExistingFormValues,
    setFormInitialValues,
} from "@/utils/helpers";
import useCustomToast from "@/utils/useCustomToast";
import { initContentsPagination, pagesRoutes } from "@/data";
import { addPageForm } from "@/data/forms";
import { ManagePageProps, ButtonType, ButtonVariant, SelectSettings, ToastStatus } from "@/types";
import { IPageData } from "@/types/models";
import useFormatMsg from "@/utils/useFormatMsg";

export const ManagePage = ({ isEditMode = false }: ManagePageProps) => {
    const [initValues, setInitValues] = useState<FormikValues>(() => setFormInitialValues(addPageForm));
    const [contentsList, setContentsList] = useState<SelectSettings>();
    const [pageId, setPageId] = useState<string>("");
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [isDraft, setIsDraft] = useState<boolean>(false);
    const router = useRouter();
    const { translate } = useFormatMsg();
    const { showToast } = useCustomToast();

    useEffect(() => {
        if (router.isReady) {
            getPageData();
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [router.isReady]);

    const getPageData = async () => {
        try {
            const {
                _embedded: { content },
            } = await ContentService.getContentList(initContentsPagination);
            const selectContentsList = createSelectItemsList(content);
            setContentsList(() => selectContentsList);
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
            console.log(err);
            const is404Error = errorStatus === 404;
            showToast({
                title: translate(is404Error ? "general.invalid-id" : "general.error"),
                description: translate(is404Error ? "pages.page.not-exist" : "information.error.loading"),
                status: is404Error ? ToastStatus.WARNING : ToastStatus.ERROR,
            });
            router.push(pagesRoutes.page);
        }
    };

    const handleFormSubmit = async (values: FormikValues, actions: FormikHelpers<FormikValues>) => {
        const pageData = { ...values, contentId: +values.contentId, published: true } as IPageData;
        try {
            const { title } = !isEditMode
                ? await PageService.createPage(pageData)
                : await PageService.updatePage(+pageId, pageData);
            !isEditMode && actions.resetForm();
            isEditMode && setInitValues(() => setExistingFormValues(addPageForm, values));
            setIsDraft(false);
            showToast({
                title: isEditMode ? "Updated" : "Created",
                description: `Page ${title} has been ${isEditMode ? "updated" : "created"}`,
            });
        } catch (e) {
            console.error(e);
            showToast({
                title: "Error occured",
                description: "Page has not been created - please try again",
                status: ToastStatus.ERROR,
            });
        }
    };

    const renderFormFields = () => {
        const formFieldsData = isEditMode
            ? addPageForm.map((field) => ({ ...field, automaticFill: undefined }))
            : addPageForm;
        const formFields = formFieldsData.map((field) => {
            if (field.name === "contentId") {
                field.selectSettings = contentsList;
            }
            return <FormField key={field.name} fieldData={field} />;
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
            const pageData = { ...values, contentId: +values.contentId, published: false } as IPageData;
            try {
                const { title } = !isEditMode
                    ? await PageService.createPage(pageData)
                    : await PageService.updatePage(+pageId, pageData);
                !isEditMode && resetForm();
                isEditMode && setInitValues(() => setExistingFormValues(addPageForm, values));
                setIsDraft(true);
                showToast({
                    title: isEditMode ? "Page updated" : "Page created",
                    description: `Page ${title} has been saved as draft`,
                });
            } catch (error) {
                showToast({
                    title: "Error occured",
                    description: "Page has not been saved - please try again",
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
            titleId={isEditMode ? "pages.manage-page.edit-title" : "pages.manage-page.add-title"}
            backPath={pagesRoutes.page}
        >
            <Flex direction="column" maxW="container.m" w="100%" m="0 auto">
                <Formik initialValues={initValues} onSubmit={handleFormSubmit}>
                    {(formikProps) => {
                        const { handleSubmit, values, setTouched, validateForm, resetForm } = formikProps;
                        return (
                            <form onSubmit={handleSubmit} noValidate>
                                {renderFormFields()}
                                {renderFormFooter(values, setTouched, validateForm, resetForm)}
                            </form>
                        );
                    }}
                </Formik>
            </Flex>
        </MainContent>
    );
};
