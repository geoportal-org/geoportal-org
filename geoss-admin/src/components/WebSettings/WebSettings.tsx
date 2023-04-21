import { useEffect, useState } from "react";
import { Flex } from "@chakra-ui/react";
import { Formik, FormikValues } from "formik";
import { FormField, FormSection, Loader, MainContent, PrimaryButton, TextContent } from "@/components";
import { FileRepositoryService, WebSettingsService } from "@/services/api";
import { ButtonType, SelectSettings, ToastStatus } from "@/types";
import {
    areObjectsEqual,
    createWebSettingsKeyValues,
    createSelectItemsList,
    setFormInitialValues,
    setExistingWebSettingsKeyValues,
    setFormValuesAsString,
    getKeyValueFormChanges,
} from "@/utils/helpers";
import useCustomToast from "@/utils/useCustomToast";
import useFormatMsg from "@/utils/useFormatMsg";
import { IWebSetting, IWebSettingData } from "@/types/models";
import { acceptedLogoExtensions, initRepositoryPagination } from "@/data";
import { webSettingsForm } from "@/data/forms";

export const WebSettings = () => {
    const webSettingsFormFields = webSettingsForm.map((section) => section.data).flat();
    const [isLoading, setIsLoading] = useState(true);
    const [initValues, setInitValues] = useState<FormikValues>(setFormInitialValues(webSettingsFormFields));
    const [savedValues, setSavedValues] = useState<FormikValues>({});
    const [webSettingsList, setWebSettingsList] = useState<IWebSetting[]>([]);
    const [documentsList, setDocumentsList] = useState<SelectSettings>();
    const { showToast } = useCustomToast();
    const { translate } = useFormatMsg();

    useEffect(() => {
        getWebSettingsInfo();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const getWebSettingsInfo = async () => {
        try {
            const {
                _embedded: { document },
            } = await FileRepositoryService.getDocumentsList(initRepositoryPagination);
            const selectDocumentsList = createSelectItemsList(
                document.filter(({ extension }) => acceptedLogoExtensions.includes(extension))
            );
            setDocumentsList(() => selectDocumentsList);
            const {
                _embedded: { webSettings },
            } = await WebSettingsService.getWebSettings();
            setWebSettingsList(webSettings);
            setSavedValues(setExistingWebSettingsKeyValues(webSettings, webSettingsFormFields, false));
            setInitValues(setExistingWebSettingsKeyValues(webSettings, webSettingsFormFields));
        } catch (e) {
            console.error(e);
        } finally {
            setIsLoading(false);
        }
    };

    const handleFormSubmit = (values: FormikValues) => {
        const valuesAsString = setFormValuesAsString(values);
        const isChanged = !areObjectsEqual(initValues, valuesAsString);
        isChanged
            ? handleWebSettingsSubmit(values)
            : showInfo("information.info.no-changes", "pages.web.no-changes-info", ToastStatus.INFO);
    };

    const handleWebSettingsSubmit = async (values: FormikValues) => {
        const promises: Promise<IWebSetting>[] = [];
        const { newValues, changedValues } = getKeyValueFormChanges(values, savedValues);
        const keyValues = createWebSettingsKeyValues(values);
        const valuesToSave = getValuesToChange(keyValues, newValues);
        const valuesToUpdate = getValuesToChange(keyValues, changedValues);

        valuesToSave.forEach((value) => promises.push(WebSettingsService.createWebSetting(value)));
        valuesToUpdate.forEach(({ id, ...webSettingData }) =>
            id ? promises.push(WebSettingsService.updateWebSetting(id, webSettingData)) : null
        );

        await Promise.all(promises)
            .then(async () => {
                setIsLoading(true);
                await getWebSettingsInfo();
                showInfo("general.updated", "pages.web.updated", ToastStatus.SUCCESS);
            })
            .catch((e) => {
                console.log(e);
                showInfo("general.error", "information.error.updated-web");
            })
            .finally(() => setIsLoading(false));
    };

    const getValuesToChange = (
        keyValues: IWebSettingData[],
        changedValues: string[]
    ): (IWebSettingData & { id?: number })[] =>
        keyValues
            .filter(
                ({ key, set }) =>
                    (set !== "source" && changedValues.includes(key)) ||
                    (set === "source" && changedValues.includes("defaultSource"))
            )
            .map((value) => ({
                ...value,
                id: webSettingsList.find(
                    ({ key, set }) =>
                        (value.set !== "source" && value.key === key) || (value.set === "source" && set === "source")
                )?.id,
            }));

    const showInfo = (titleId: string = "general.error", messageId: string, status: ToastStatus = ToastStatus.ERROR) =>
        showToast({
            title: translate(titleId),
            description: translate(messageId),
            status,
        });

    const renderFormFields = () =>
        webSettingsForm.map((section, idx) => {
            const isLastSection = idx === webSettingsForm.length - 1;
            const sectionFields = section.data.map((field) => {
                if (field.name === "source") {
                    field.selectSettings = documentsList;
                }
                return <FormField key={field.name} fieldData={field} />;
            });
            return (
                <FormSection
                    key={section.titleId}
                    titleId={section.titleId}
                    children={sectionFields}
                    isLastSection={isLastSection}
                />
            );
        });

    const renderFormFooter = () => (
        <Flex justifyContent="flex-end" py={1} w="full">
            <PrimaryButton type={ButtonType.SUBMIT}>
                <TextContent id="general.save" />
            </PrimaryButton>
        </Flex>
    );

    if (isLoading) {
        return <Loader />;
    }

    return (
        <MainContent titleId="nav.settings.section.web">
            <Flex direction="column" maxW="container.m" w="100%" m="0 auto">
                <Formik initialValues={initValues} onSubmit={handleFormSubmit}>
                    {(formikProps) => {
                        const { handleSubmit } = formikProps;
                        return (
                            <form onSubmit={handleSubmit} noValidate>
                                {renderFormFields()}
                                {renderFormFooter()}
                            </form>
                        );
                    }}
                </Formik>
            </Flex>
        </MainContent>
    );
};
