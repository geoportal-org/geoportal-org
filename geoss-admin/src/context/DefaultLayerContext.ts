import { createContext } from "react";
import { FormikHelpers, FormikValues } from "formik";
import { ILayer } from "@/types/models";

type DefaultLayerContext = {
    addNewLayer: (values: FormikValues, actions: FormikHelpers<FormikValues>) => Promise<void>;
    updateLayer: (values: FormikValues, id: number, updateFormState: (values: FormikValues) => void) => Promise<void>;
    onLayerEditAction: (layerData: ILayer) => void;
    editedLayer: ILayer | null;
};

export const DefaultLayerContext = createContext({} as DefaultLayerContext);
