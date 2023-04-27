import { useEffect, useState } from "react";
import { Flex } from "@chakra-ui/react";
import { Formik, FormikValues } from "formik";
import { FormField, FormSection, Loader, MainContent, PrimaryButton, TextContent } from "@/components";
import { ApiSettingsService } from "@/services/api";
import {
    areObjectsEqual,
    createApiSettingsKeyValues,
    getKeyValueFormChanges,
    setExistingApiSettingsKeyValues,
    setFormInitialValues,
} from "@/utils/helpers";
import useCustomToast from "@/utils/useCustomToast";
import useFormatMsg from "@/utils/useFormatMsg";
import { ButtonType, ToastStatus } from "@/types";
import { IApiSetting, IApiSettingData } from "@/types/models";
import { apiSettingsForm, apiSettingsFormFields } from "@/data/forms";

export const ApiSettings = () => {
    const [isLoading, setIsLoading] = useState(true);
    const [initValues, setInitValues] = useState<FormikValues>(() => setFormInitialValues(apiSettingsFormFields));
    const [savedValues, setSavedValues] = useState<FormikValues>({});
    const [apiSettingsList, setApiSettingList] = useState<IApiSetting[]>([]);
    const { showToast } = useCustomToast();
    const { translate } = useFormatMsg();

    useEffect(() => {
        getApiSettingsInfo();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const getApiSettingsInfo = async () => {
        try {
            const {
                _embedded: { apiSettings },
            } = await ApiSettingsService.getApiSettings();
            setApiSettingList(apiSettings);
            setSavedValues(setExistingApiSettingsKeyValues(apiSettings, apiSettingsFormFields, false));
            setInitValues(setExistingApiSettingsKeyValues(apiSettings, apiSettingsFormFields));
        } catch (e) {
            console.error(e);
        } finally {
            setIsLoading(false);
        }
    };

    const handleFormSubmit = (values: FormikValues) => {
        const isChanged = !areObjectsEqual(initValues, values);
        isChanged
            ? handleApiSettingsSubmit(values)
            : showInfo("information.info.no-changes", "pages.api.no-changes-info", ToastStatus.INFO);
    };

    const handleApiSettingsSubmit = async (values: FormikValues) => {
        const promises: Promise<IApiSetting>[] = [];
        const { newValues, changedValues } = getKeyValueFormChanges(values, savedValues);
        const keyValues = createApiSettingsKeyValues(values);
        const valuesToSave = getValuesToChange(keyValues, newValues);
        const valuesToUpdate = getValuesToChange(keyValues, changedValues);

        valuesToSave.forEach((value) => promises.push(ApiSettingsService.createApiSetting(value)));
        valuesToUpdate.forEach(({ id, ...apiSettingData }) =>
            id ? promises.push(ApiSettingsService.updateApiSetting(id, apiSettingData)) : null
        );

        await Promise.all(promises)
            .then(async () => {
                setIsLoading(true);
                await getApiSettingsInfo();
                showInfo("general.updated", "pages.api.updated", ToastStatus.SUCCESS);
            })
            .catch((e) => {
                console.log(e);
                showInfo("general.error", "information.error.updated-api");
            })
            .finally(() => setIsLoading(false));
    };

    const getValuesToChange = (
        keyValues: IApiSettingData[],
        changedValues: string[]
    ): (IApiSettingData & { id?: number })[] =>
        keyValues
            .filter(({ key }) => changedValues.includes(key))
            .map((value) => ({
                ...value,
                id: apiSettingsList.find(({ key }) => value.key === key)?.id,
            }));

    const showInfo = (titleId: string = "general.error", messageId: string, status: ToastStatus = ToastStatus.ERROR) =>
        showToast({
            title: translate(titleId),
            description: translate(messageId),
            status,
        });

    const renderFormFields = () =>
        apiSettingsForm.map((section, idx) => {
            const isLastSection = idx === apiSettingsForm.length - 1;
            const sectionFields = section.data.map((field) => <FormField key={field.name} fieldData={field} />);
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
        <MainContent titleId="nav.settings.section.api">
            {isLoading && <Loader />}
            {!isLoading && (
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
