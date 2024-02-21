import { PagesInfo } from "@/types/models/page";
export interface SurveysData {
    page: PagesInfo;
    _embedded: {
        surveys: SurveyInfo[];
    };
    _links: {
        profile: {
            href: string;
        };
        self: {
            href: string;
        };
    };
}

export interface SurveyInfo {
    adequately: string;
    classification: string;
    createdBy: string;
    createdOn: string;
    foundLookingFor: string;
    from: string;
    id: string;
    impression: string;
    interest: string;
    lookingFor: string;
    modifiedBy: string;
    modifiedOn: string;
    organized: string;
    searchCriteria: string;
    visualization: string;
}

