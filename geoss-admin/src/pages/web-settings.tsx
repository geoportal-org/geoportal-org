import { NextPage } from "next";
import { WebSettings as WebSettingsPage } from "@/components";
import { ProtectedNextPage } from "@/types";

const WebSettings: ProtectedNextPage = () => <WebSettingsPage />;

WebSettings.auth = true;

export default WebSettings;
