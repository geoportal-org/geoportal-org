import { NextPage } from "next";

export type ProtectedNextPage = NextPage & authOptionsType;

type authOptionsType = {
    auth: boolean;
};
