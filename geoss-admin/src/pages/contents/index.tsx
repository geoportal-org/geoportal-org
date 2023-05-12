import { NextPage } from "next";
import { Contents as ConentsCard } from "@/components";
import { ProtectedNextPage } from "@/types";

const Contents: ProtectedNextPage = () => <ConentsCard />;

Contents.auth = true;

export default Contents;
