import { useEffect } from "react";
import { Field, useFormikContext, FormikValues } from "formik";
import { FormControl, FormLabel, FormErrorMessage, Flex } from "@chakra-ui/react";
import { chakraComponents } from "chakra-react-select";
import { TextContent, TextEditor, TranslationSwitcher, Uploader } from "@/components";
import { chakraSelectStyles } from "@/theme/commons";
import { ValidationService } from "@/services";
import { FormFieldProps, FormFieldSelect } from "@/types";
import useFormatMsg from "@/utils/useFormatMsg";
import { createRelativeUrl, createSlug } from "@/utils/helpers";
import { defaultUsedLang } from "@/data";
import { GroupedFormFieldSelect } from "@/types/data/formField";

export const FormField = ({ fieldData, invisible, onInputTranslationChange, currentLang }: FormFieldProps) => {
    const { translate } = useFormatMsg();
    const {
        values: formValues,
        errors: formErrors,
        setFieldValue,
        setFieldTouched,
        touched,
    } = useFormikContext<FormikValues>();

    const {
        name: fieldName,
        fieldType,
        type: inputType,
        placeholderId,
        labelId: fieldLabelId,
        selectSettings,
        isRequired,
        automaticFill,
        isReadOnly,
        translationInfo,
    } = fieldData;

    const isDefaultField = inputType !== "select" && inputType !== "editor" && inputType !== "file";
    const isSelectField = inputType === "select" && selectSettings;
    const isTextEditor = inputType === "editor";
    const isFileUploader = inputType === "file";
    const isFieldError = !translationInfo
        ? !!formErrors[fieldName]
        : !!formErrors[translationInfo.genericName] &&
          !!(formErrors[translationInfo.genericName] as any)[translationInfo.translation];
    const isFieldTouched = !translationInfo
        ? !!touched[fieldName]
        : !!touched[translationInfo.genericName] &&
          !!(touched[translationInfo.genericName] as any)[translationInfo.translation];

    const automaticFieldFillTypes = {
        slug: () => (automaticFill ? createSlug(formValues[automaticFill.superiorField][defaultUsedLang]) : null),
        "relative-url": () =>
            automaticFill ? createRelativeUrl(formValues[automaticFill.superiorField][defaultUsedLang]) : null,
    };

    useEffect(
        () => {
            const fillField = () => {
                if (!automaticFill) {
                    return;
                }

                const { superiorField, fillType } = automaticFill;
                const superiorValue = formValues[superiorField][defaultUsedLang];
                setFieldValue(fieldName, superiorValue ? automaticFieldFillTypes[fillType]() : "");
            };
            fillField();
        },
        // eslint-disable-next-line react-hooks/exhaustive-deps
        !automaticFill ? [] : [formValues[automaticFill.superiorField][defaultUsedLang]]
    );

    const setSelectValue = () => {
        if (!formValues[fieldName]) {
            return "";
        }
        const values: FormFieldSelect[] = [];
        const options = selectSettings?.options as FormFieldSelect[];

        if (selectSettings?.isGroupedOptions) {
            const groupedOptions = selectSettings.options as GroupedFormFieldSelect[];
            let match = undefined
            groupedOptions.forEach((group) => {
                let option = (group.options.find((option) => option.value === formValues[fieldName]))
                if(option) match = option
            });
            if (match) return match;

        } else {
            if (Array.isArray(formValues[fieldName]) && selectSettings && selectSettings.isMultiselect) {
                formValues[fieldName].forEach((value: string) => {
                    const item = options.find((option) => option.value === value);
                    if (item) {
                        values.push(item);
                    }
                });
            }
            if (values.length) {
                return values;
            }
        }

        return options.find((option) => option.value === formValues[fieldName]);
    };

    const handleSelectChange = (options: FormFieldSelect | FormFieldSelect[]) => {
        const selectValue = Array.isArray(options) ? options.map((option) => option.value) : options.value;
        setFieldValue(fieldName, selectValue);
    };

    const markFieldTouched = () => setFieldTouched(fieldName);

    const handleEditorChange = (text: string) => setFieldValue(fieldName, text);

    const handleFileAdd = (file: File | "") => setFieldValue(fieldName, file);

    return (
        <FormControl
            isRequired={isRequired}
            isInvalid={isFieldError && isFieldTouched}
            w="full"
            display={invisible ? "none" : "block"}
        >
            {!!isTextEditor && translationInfo && onInputTranslationChange ? (
                <Flex align="center" justify="space-between" mb="10px">
                    <FormLabel htmlFor={fieldName} fontWeight="bold" fontSize="sm" margin="0">
                        <TextContent id={fieldLabelId} />:
                    </FormLabel>
                    <TranslationSwitcher
                        currentLang={currentLang || defaultUsedLang}
                        onTranslationChange={onInputTranslationChange}
                    />
                </Flex>
            ) : (
                <FormLabel htmlFor={fieldName} fontWeight="bold" fontSize="sm">
                    <TextContent id={fieldLabelId} />:
                </FormLabel>
            )}

            {isDefaultField && (
                <Flex align="flex-end">
                    <Field
                        as={fieldType}
                        validate={(value: string) => ValidationService.checkForErrors(fieldData, value)}
                        id={fieldName}
                        isReadOnly={isReadOnly}
                        name={fieldName}
                        type={inputType || null}
                        borderColor="brand.dividerDark"
                        focusBorderColor="brand.dark"
                        fontSize="sm"
                        p="0 10px"
                        placeholder={placeholderId && translate(placeholderId)}
                        size="sm"
                        variant="flushed"
                        _placeholder={{ opacity: 1, color: "brand.dividerDark", fontStyle: "italic" }}
                    />
                    {translationInfo && onInputTranslationChange && (
                        <TranslationSwitcher
                            currentLang={currentLang || defaultUsedLang}
                            onTranslationChange={onInputTranslationChange}
                        />
                    )}
                </Flex>
            )}
            {isSelectField && (
                <Field
                    as={fieldType}
                    value={setSelectValue()}
                    validate={(value: string | string[]) => ValidationService.checkForErrors(fieldData, value)}
                    onChange={handleSelectChange}
                    onBlur={markFieldTouched}
                    id={fieldName}
                    name={fieldName}
                    instanceId={fieldName}
                    chakraStyles={chakraSelectStyles}
                    closeMenuOnSelect={!selectSettings.isMultiselect}
                    focusBorderColor="brand.dark"
                    fontSize="sm"
                    hideSelectedOptions={false}
                    isMulti={selectSettings.isMultiselect}
                    options={
                        selectSettings.isTranslated
                            ? selectSettings.options.map((option) => ({ ...option, label: translate(option.label) }))
                            : selectSettings.options
                    }
                    placeholder={placeholderId && translate(placeholderId)}
                    selectedOptionStyle="check"
                    size="sm"
                    useBasicStyles
                    variant="flushed"
                    isReadOnly={isReadOnly}
                    noOptionsMessage={() => translate("form.placeholders.no-options")}
                    components={customSelectComponents(translate, selectSettings.isTranslated)}
                />
            )}
            {isTextEditor && (
                <TextEditor
                    onEditorChange={handleEditorChange}
                    onEditorBlur={markFieldTouched}
                    initialContent={formValues[translationInfo?.genericName!][translationInfo?.translation!]}
                />
            )}
            {isFileUploader && (
                <Uploader
                    fieldData={fieldData}
                    value={formValues[fieldName]}
                    handleUploaderChange={handleFileAdd}
                    handleFileUploaderUse={markFieldTouched}
                    isError={isFieldTouched && isFieldError}
                />
            )}
            {isFieldError && isFieldTouched && (
                <FormErrorMessage mt="5px">
                    <TextContent
                        id={
                            (!translationInfo
                                ? formErrors[fieldName]
                                : (formErrors[translationInfo.genericName] as any)[
                                      translationInfo.translation
                                  ]) as string
                        }
                    />
                </FormErrorMessage>
            )}
        </FormControl>
    );
};

const customSelectComponents = (
    translate: (id: string, values?: { [key: string]: any }) => any,
    isTranslated: boolean = false
): Partial<typeof chakraComponents> => {
    // TBD components with translation for multi select

    return {
        SingleValue: ({ children, ...props }) => (
            <chakraComponents.SingleValue {...props}>
                {isTranslated ? translate(children as string) : children}
            </chakraComponents.SingleValue>
        ),
    };
};
