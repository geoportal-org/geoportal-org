import { NextPage } from "next";
import { ViewsSettings as ViewsSettingsPage } from "@/components";
import { ProtectedNextPage } from "@/types";

const ViewsSettings: ProtectedNextPage = () => <ViewsSettingsPage />;

ViewsSettings.auth = true;

export default ViewsSettings;
