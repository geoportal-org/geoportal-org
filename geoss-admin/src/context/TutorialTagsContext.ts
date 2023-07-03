import { createContext } from "react";
import { FormikHelpers, FormikValues } from "formik";
import { ITutorialTag } from "@/types/models";
import { LocaleNames } from "@/types";

type TutorialTagsContext = {
    addNewTag: (values: FormikValues, actions: FormikHelpers<FormikValues>, currentLang: LocaleNames) => Promise<void>;
    updateTag: (
        values: FormikValues,
        id: number,
        updateFormState: (values: FormikValues) => void,
        currentLang: LocaleNames
    ) => Promise<void>;
    onTagEditAction: (tutorialTag: ITutorialTag) => void;
    editedTag: ITutorialTag | null;
};

export const TutorialTagsContext = createContext({} as TutorialTagsContext);
