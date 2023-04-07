import { FormikValues } from "formik";
import { FormField } from "@/types";

export type UploaderProps = {
    fieldData: FormField;
    value: FormikValues;
    handleUploaderChange: (file: File | "") => void;
    handleFileUploaderUse: () => void;
    isError: boolean;
};
