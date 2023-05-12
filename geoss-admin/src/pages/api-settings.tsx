import { NextPage } from "next";
import { ApiSettings as ApiSettingsPage } from "@/components";
import { ProtectedNextPage } from "@/types";

const ApiSettings: ProtectedNextPage = () => <ApiSettingsPage />;

ApiSettings.auth = true;

export default ApiSettings;
