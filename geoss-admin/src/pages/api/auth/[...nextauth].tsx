import NextAuth, { NextAuthOptions } from "next-auth";
import KeycloackProvider from "next-auth/providers/keycloak";

export const authOptions: NextAuthOptions = {
    providers: [
        KeycloackProvider({
            clientId: process.env.KEYCLOAK_ID as string,
            clientSecret: process.env.KEYCLOAK_SECRET as string,
            issuer: process.env.KEYCLOAK_ISSUER as string,
        }),
    ],
    /*pages: {
        //signIn: "/",
    },*/
};

export default NextAuth(authOptions);
