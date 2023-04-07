import { useEffect } from "react";
import { Field, useFormikContext, FormikValues } from "formik";
import { FormControl, FormLabel, FormErrorMessage } from "@chakra-ui/react";
import { TextContent, TextEditor, Uploader } from "@/components";
import { chakraSelectStyles } from "@/theme/commons";
import { ValidationService } from "@/services";
import { FormFieldProps, FormFieldSelect } from "@/types";
import useFormatMsg from "@/utils/useFormatMsg";
import { createRelativeUrl, createSlug } from "@/utils/helpers";

export const FormField = ({ fieldData }: FormFieldProps) => {
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
    } = fieldData;

    const isDefaultField = inputType !== "select" && inputType !== "editor" && inputType !== "file";
    const isSelectField = inputType === "select" && selectSettings;
    const isTextEditor = inputType === "editor";
    const isFileUploader = inputType === "file";
    const isFieldError = !!formErrors[fieldName];
    const isFieldTouched = !!touched[fieldName];

    const automaticFieldFillTypes = {
        slug: () => (automaticFill ? createSlug(formValues[automaticFill.superiorField]) : null),
        "relative-url": () => (automaticFill ? createRelativeUrl(formValues[automaticFill.superiorField]) : null),
    };

    useEffect(
        () => {
            const fillField = () => {
                if (!automaticFill) {
                    return;
                }

                const { superiorField, fillType } = automaticFill;
                const superiorValue = formValues[superiorField];
                setFieldValue(fieldName, superiorValue ? automaticFieldFillTypes[fillType]() : "");
            };
            fillField();
        },
        !automaticFill ? [] : [formValues[automaticFill.superiorField], touched[automaticFill.superiorField]]
    );

    const setSelectValue = () => {
        if (!formValues[fieldName]) {
            return "";
        }

        const values: FormFieldSelect[] = [];
        if (Array.isArray(formValues[fieldName]) && selectSettings && selectSettings.isMultiselect) {
            formValues[fieldName].forEach((value: string) => {
                const item = selectSettings.options.find((option) => option.value === value);
                if (item) {
                    values.push(item);
                }
            });
        }

        if (values.length) {
            return values;
        }

        return selectSettings?.options.find((option) => option.value === formValues[fieldName]);
    };

    const handleSelectChange = (options: FormFieldSelect | FormFieldSelect[]) => {
        const selectValue = Array.isArray(options) ? options.map((option) => option.value) : options.value;
        setFieldValue(fieldName, selectValue);
    };

    const markFieldTouched = () => setFieldTouched(fieldName);

    const handleEditorChange = (text: string) => setFieldValue(fieldName, text);

    const handleEditorOnBlur = () => markFieldTouched();

    const handleFileAdd = (file: File | "") => setFieldValue(fieldName, file);

    return (
        <FormControl isRequired={isRequired} isInvalid={isFieldError && isFieldTouched} w="full">
            <FormLabel
                htmlFor={fieldName}
                fontWeight="bold"
                fontSize="sm"
                m={isTextEditor || isFileUploader ? "0 0 10px 0" : 0}
            >
                <TextContent id={fieldLabelId} />:
            </FormLabel>
            {isDefaultField && (
                <Field
                    as={fieldType}
                    validate={(value: string) => ValidationService.checkForErrors(fieldData, value)}
                    id={fieldName}
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
                    options={selectSettings.options}
                    placeholder={placeholderId && translate(placeholderId)}
                    selectedOptionStyle="check"
                    size="sm"
                    useBasicStyles
                    variant="flushed"
                />
            )}
            {isTextEditor && (
                <TextEditor
                    onEditorChange={handleEditorChange}
                    onEditorBlur={handleEditorOnBlur}
                    initialContent={formValues[fieldName]}
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
                    <TextContent id={formErrors[fieldName] as string} />
                </FormErrorMessage>
            )}
        </FormControl>
    );
};
