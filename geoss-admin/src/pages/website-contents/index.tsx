import { NextPage } from "next";
import { Contents as ConentsCard } from "@/components";
import { ProtectedNextPage } from "@/types";

const WebsiteContents: ProtectedNextPage = () => <ConentsCard />;

WebsiteContents.auth = true;

export default WebsiteContents;
