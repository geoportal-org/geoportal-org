import { useCallback, useContext, useEffect, useState } from "react";
import { Formik, FormikValues } from "formik";
import { Flex } from "@chakra-ui/react";
import { FormField, FormSection, Loader, MainContent, PrimaryButton, TextContent, TextInfo } from "@/components";
import { FileRepositoryService, WebSettingsService } from "@/services/api";
import { ButtonType, LocaleNames, SelectSettings, ToastStatus } from "@/types";
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
import { webSettingsForm, webSettingsFormFields } from "@/data/forms";
import { useIntl } from "react-intl";
import { SiteContext, SiteContextValue } from "@/context/CurrentSiteContext";

export const WebSettings = () => {
    const [isLoading, setIsLoading] = useState(true);
    const [isError, setIsError] = useState(false);
    const [initValues, setInitValues] = useState<FormikValues>(() => setFormInitialValues(webSettingsFormFields));
    const [savedValues, setSavedValues] = useState<FormikValues>({});
    const [webSettingsList, setWebSettingsList] = useState<IWebSetting[]>([]);
    const { showToast } = useCustomToast();
    const { translate } = useFormatMsg();
    // const { locale } = useIntl();
    const [defaultSourceNameOptionsList, setDefaultSourceNameOptionsList] = useState<{ value: string; label: string }[]>([]);

    //siteId
    const { currentSiteId } = useContext<SiteContextValue>(SiteContext);

    const getCurrentWebSettings = useCallback(async () => {
        setIsLoading(true);
        try {
            const {
                _embedded: { webSettings },
            } = await WebSettingsService.getWebSettings(currentSiteId);
            setWebSettingsList(webSettings);
            setSavedValues(setExistingWebSettingsKeyValues(webSettings, webSettingsFormFields, false));
            setInitValues(setExistingWebSettingsKeyValues(webSettings, webSettingsFormFields));
        } catch (e) {
            console.error(e);
            setIsError(true);
        } finally {
            setIsLoading(false);
        }
    }, [currentSiteId]);

    useEffect(() => {
        //this used to fetch options for logo options, this is now moved to commmunity portal management

        // const getDocumentsList = async () => {
        //     try {
        //         const {
        //             _embedded: { document },
        //         } = await FileRepositoryService.getDocumentsList(initRepositoryPagination);
        //         const selectDocumentsList = createSelectItemsList(
        //             document.filter(({ extension }) => acceptedLogoExtensions.includes(extension)),
        //             false,
        //             false,
        //             locale as LocaleNames
        //         );
        //         setDocumentsList(() => selectDocumentsList);
        //     } catch (e) {
        //         console.error(e);
        //         setIsError(true);
        //     }
        // };

        const getDefaultSourceNameOptions = async () => {
            try {
                const result = await WebSettingsService.getDefaultSourceNameOptions();
                const options: { value: string; label: string }[] = [];
                result.forEach((option: string) => {
                    options.push({
                        value: option,
                        label: option,
                    });
                });
                setDefaultSourceNameOptionsList(() => options);
            } catch (e) {
                console.error(e);
                setIsError(true);
            }
        };

        // getDocumentsList();
        getDefaultSourceNameOptions();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [currentSiteId]);

    useEffect(() => {
        const getWebSettings = async () => await getCurrentWebSettings();
        getWebSettings();
    }, [getCurrentWebSettings]);

    const handleFormSubmit = (values: FormikValues) => {
        const valuesAsString = setFormValuesAsString(values);
        const isChanged = !areObjectsEqual(initValues, valuesAsString);
        isChanged
            ? handleWebSettingsSubmit(values)
            : showInfo("information.info.no-changes", "pages.web.no-changes-info", ToastStatus.INFO);
    };

    const handleWebSettingsSubmit = async (values: FormikValues) => {
        setIsLoading(true);
        const promises: Promise<IWebSetting>[] = [];
        const { newValues, changedValues } = getKeyValueFormChanges(values, savedValues);
        const keyValues = createWebSettingsKeyValues(values, currentSiteId);
        const valuesToSave = getValuesToChange(keyValues, newValues);
        const valuesToUpdate = getValuesToChange(keyValues, changedValues);

        valuesToSave.forEach((value) => promises.push(WebSettingsService.createWebSetting(value)));
        valuesToUpdate.forEach(({ id, ...webSettingData }) =>
            id ? promises.push(WebSettingsService.updateWebSetting(id, webSettingData)) : null
        );

        await Promise.all(promises)
            .then(async () => {
                await getCurrentWebSettings();
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
                    (set === "source" && changedValues.includes("defaultSourceName"))
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
                if (field.name === "defaultSourceName") {
                    field.selectSettings = {
                        isMultiselect: false,
                        options: defaultSourceNameOptionsList
                    };
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

    return (
        <MainContent titleId="nav.settings.section.web">
            {!isLoading && isError && <TextInfo id="information.error.loading" />}
            {isLoading && !isError && <Loader />}
            {!isLoading && !isError && (
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
            )}
        </MainContent>
    );
};
