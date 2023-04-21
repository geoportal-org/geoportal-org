import { createContext } from "react";
import { FormikHelpers, FormikValues } from "formik";

type TutorialTagsContext = {
    addNewTag: (values: FormikValues, actions: FormikHelpers<FormikValues>) => Promise<void>;
    updateTag: (values: FormikValues, id: number, updateFormState: (values: FormikValues) => void) => Promise<void>;
    onTagEditAction: (id: number) => void;
    editedTagId: number | null;
};

export const TutorialTagsContext = createContext({} as TutorialTagsContext);
