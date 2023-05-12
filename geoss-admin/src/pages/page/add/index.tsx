import { NextPage } from "next";
import { ManagePage } from "@/components";
import { ProtectedNextPage } from "@/types";

const AddPage: ProtectedNextPage = () => <ManagePage />;

AddPage.auth = true;

export default AddPage;
