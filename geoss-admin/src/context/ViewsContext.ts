import { createContext } from "react";
import { FormikHelpers, FormikValues } from "formik";
import { EditedViewInfo, FormattedView } from "@/types";

type ViewsContext = {
    onEditViewAction: (viewId: number, item: FormattedView) => void;
    onAddSubViewAction: (parentViewId: number) => void;
    onEditSubViewAction: (parentViewId: number, viewId: number, item: FormattedView) => void;
    addNewView: (values: FormikValues, actions: FormikHelpers<FormikValues>) => Promise<void>;
    updateView: (values: FormikValues, updateFormState: (values: FormikValues) => void) => Promise<void>;
    editedView: EditedViewInfo | null;
};

export const ViewsContext = createContext({} as ViewsContext);
