import { createContext } from "react";
import { FormikHelpers, FormikValues } from "formik";
import { ITutorialTag } from "@/types/models";

type TutorialTagsContext = {
    addNewTag: (values: FormikValues, actions: FormikHelpers<FormikValues>) => Promise<void>;
    updateTag: (values: FormikValues, id: number, updateFormState: (values: FormikValues) => void) => Promise<void>;
    onTagEditAction: (tutorialTag: ITutorialTag) => void;
    editedTag: ITutorialTag | null;
};

export const TutorialTagsContext = createContext({} as TutorialTagsContext);
