import { createContext } from "react";
import { FormikHelpers, FormikValues } from "formik";

type DefaultLayerContext = {
    addNewLayer: (values: FormikValues, actions: FormikHelpers<FormikValues>) => Promise<void>;
    updateLayer: (values: FormikValues, id: number, updateFormState: (values: FormikValues) => void) => Promise<void>;
    onLayerEditAction: (id: number) => void;
    editedLayerId: number | null;
};

export const DefaultLayerContext = createContext({} as DefaultLayerContext);
