import { NextPage } from "next";
import { Pages as PagesCard } from "@/components";
import { ProtectedNextPage } from "@/types";

const Page: ProtectedNextPage = () => <PagesCard />;

Page.auth = true;

export default Page;
