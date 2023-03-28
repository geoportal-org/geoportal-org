import { FormField } from "@/types";
import { regExp } from "@/data";

export const ValidationService = {
    validationRules: {
        email: {
            regExp: regExp.email,
            errorMsgId: "form.errors.invalid-email",
        },
        url: {
            regExp: regExp.url,
            errorMsgId: "form.errors.invalid-url",
        },
        username: {
            regExp: regExp.username,
            errorMsgId: "form.errors.invalid-username",
        },
        password: {
            regExp: regExp.password,
            errorMsgId: "form.errors.invalid-password",
        },
        slug: {
            regExp: regExp.slug,
            errorMsgId: "form.errors.invalid-slug",
        },
    },

    checkForErrors: (field: FormField, fieldValue: string | string[]): string | undefined => {
        const { isRequired } = field;

        return isRequired
            ? ValidationService.validateRequiredField(field, fieldValue)
            : ValidationService.validateNotRequiredField(field, fieldValue);
    },

    validateRequiredField: (field: FormField, fieldValue: string | string[]): string | undefined => {
        const { type = "text", validationSchema } = field;
        let error = ValidationService.validateRequiredOnEmpty(fieldValue, type);
        if (!error && !Array.isArray(fieldValue) && validationSchema) {
            error = ValidationService.validateOnValidationSchema(validationSchema, fieldValue);
        }
        return error;
    },

    validateNotRequiredField: (field: FormField, fieldValue: string | string[]): string | undefined => {
        let error;
        const { validationSchema } = field;
        const isEmptyField = ValidationService.checkIsFieldEmpty(fieldValue);
        if (!isEmptyField && validationSchema && !Array.isArray(fieldValue)) {
            error = ValidationService.validateOnValidationSchema(validationSchema, fieldValue);
        }
        return error;
    },

    validateRequiredOnEmpty: (fieldValue: string | string[], type: string): string | undefined => {
        if ((type === "select" || type === "checkbox") && Array.isArray(fieldValue)) {
            return ValidationService.validateMultiSelectOnEmpty(fieldValue);
        }
        if (!Array.isArray(fieldValue)) {
            return ValidationService.validateInputOnEmpty(fieldValue);
        }
    },

    validateMultiSelectOnEmpty: (fieldValue: string[]): string | undefined => {
        let error;
        if (!fieldValue.length) {
            error = "form.errors.required";
        }
        return error;
    },

    validateInputOnEmpty: (fieldValue: string): string | undefined => {
        let error;
        if (!fieldValue || !fieldValue.trim()) {
            error = "form.errors.required";
        }
        return error;
    },

    validateOnValidationSchema: (validationSchema: string, fieldValue: string): string | undefined => {
        let error;
        const validationRule =
            ValidationService.validationRules[validationSchema as keyof typeof ValidationService.validationRules];
        if (validationRule) {
            const isFieldValid = ValidationService.isMatchRegExp(fieldValue, validationSchema);
            const errorMsg =
                ValidationService.validationRules[validationSchema as keyof typeof ValidationService.validationRules]
                    .errorMsgId;
            error = isFieldValid ? error : errorMsg;
        }
        return error;
    },

    isMatchRegExp: (fieldValue: string, validationSchema: string): boolean => {
        const validationRegExp =
            ValidationService.validationRules[validationSchema as keyof typeof ValidationService.validationRules]
                .regExp;
        return validationRegExp.test(fieldValue);
    },

    checkIsFieldEmpty: (fieldValue: string | string[]): boolean => {
        if (Array.isArray(fieldValue)) {
            return !!fieldValue.length;
        }
        return !fieldValue || !fieldValue.trim();
    },

    validateTextEditorContent: (values: { [index: string]: string }): { [index: string]: string } | undefined => {
        let errors;
        const isEditorFilled = !ValidationService.checkIsFieldEmpty(values.data);
        const isEditorValidFilled = values.data !== "<p><br></p>";
        if (!isEditorFilled || !isEditorValidFilled) {
            errors = { data: "form.errors.required" };
        }
        return errors;
    },

    checkPasswordMatch: (values: { [index: string]: string }): { [index: string]: string } | undefined => {
        let errors;
        const isPasswordFilled =
            !ValidationService.checkIsFieldEmpty(values.password) &&
            !ValidationService.checkIsFieldEmpty(values.passwordConfirm);
        const isPasswordMatch = values.password === values.passwordConfirm;
        if (isPasswordFilled && !isPasswordMatch) {
            errors = { passwordConfirm: "form.errors.passwords-not-match" };
        }
        return errors;
    },

    isNotRequiredFieldFilled: (isRequired: boolean, fieldValue: string | string[]): boolean => {
        if (isRequired) {
            return true;
        }
        if (Array.isArray(fieldValue)) {
            return !!fieldValue.length;
        }
        return !!(fieldValue && fieldValue.trim());
    },
};
