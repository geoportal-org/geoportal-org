import { NextPage } from "next";

export type NonProtectedNextPage = NextPage & authOptionsType;

type authOptionsType = {
    nonAuth: boolean;
};
