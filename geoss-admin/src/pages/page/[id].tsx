import { NextPage } from "next";
import { ManagePage } from "@/components";
import { ProtectedNextPage } from "@/types";

const EditPage: ProtectedNextPage = () => <ManagePage isEditMode={true} />;

EditPage.auth = true;

export default EditPage;
