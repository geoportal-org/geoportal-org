import { NextPage } from "next";
import { DefaultLayerSettings as DefaultLayerSettingsPage } from "@/components";
import { ProtectedNextPage } from "@/types";

const DefaultLayerSettings: ProtectedNextPage = () => <DefaultLayerSettingsPage />;

DefaultLayerSettings.auth = true;

export default DefaultLayerSettings;
