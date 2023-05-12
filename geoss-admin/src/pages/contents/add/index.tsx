import { NextPage } from "next";
import { ManageContent } from "@/components";
import { ProtectedNextPage } from "@/types";

const AddContent: ProtectedNextPage = () => <ManageContent />;

AddContent.auth = true;

export default AddContent;
