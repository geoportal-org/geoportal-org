import { NextPage } from "next";
import { CatalogsSettings as CatalogsSettingsPage } from "@/components";
import { ProtectedNextPage } from "@/types";

const CatalogsSettings: ProtectedNextPage = () => <CatalogsSettingsPage />;

CatalogsSettings.auth = true;

export default CatalogsSettings;
