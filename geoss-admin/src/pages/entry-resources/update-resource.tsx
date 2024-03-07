import { NextPage } from "next";
import { AddEntity } from "@/components";

const UpdateEntryPage: NextPage = () => <AddEntity isUpdate={true} isUpdateResource={true}/>;

export default UpdateEntryPage;
