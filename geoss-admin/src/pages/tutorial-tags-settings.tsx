import { NextPage } from "next";
import { TutorialTagsSettings as TutorialTagsSettingsPage } from "@/components";
import { ProtectedNextPage } from "@/types";

const TutorialTagsSettings: ProtectedNextPage = () => <TutorialTagsSettingsPage />;

TutorialTagsSettings.auth = true;

export default TutorialTagsSettings;
