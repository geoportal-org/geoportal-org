import { NextPage } from "next";
import { ManageContent } from "@/components";
import { ProtectedNextPage } from "@/types";

const ContentEdit: ProtectedNextPage = () => <ManageContent isEditMode={true} />;

ContentEdit.auth = true;

export default ContentEdit;
