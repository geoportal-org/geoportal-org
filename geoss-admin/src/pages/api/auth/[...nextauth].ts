import NextAuth, { NextAuthOptions } from "next-auth";
import KeycloakProvider from "next-auth/providers/keycloak";
import { pagesRoutes } from "@/data";
import type { JWT } from "next-auth/jwt";

async function refreshAccessToken(token: JWT) {
    try {
        const refreshTokenUrl = `${process.env.KEYCLOAK_BASE_URL}/protocol/openid-connect/token`;
        const refreshTokenParams = new URLSearchParams();
        refreshTokenParams.append("client_id", process.env.KEYCLOAK_CLIENT_ID!);
        refreshTokenParams.append("client_secret", process.env.KEYCLOAK_CLIENT_SECRET!);
        refreshTokenParams.append("grant_type", "refresh_token");
        refreshTokenParams.append("refresh_token", token.refreshToken as string);

        const refreshTokenRequestOptions = {
            method: "POST",
            headers: new Headers({
                "Content-Type": "application/x-www-form-urlencoded",
            }),
            body: refreshTokenParams,
        };

        const response = await fetch(refreshTokenUrl, refreshTokenRequestOptions);
        const refreshedToken = await response.json();

        if (!response.ok) {
            throw refreshedToken;
        }

        return {
            ...token,
            accessToken: refreshedToken.access_token,
            accessTokenExpired: Date.now() + refreshedToken.expires_in * 1000,
            refreshTokenExpired: Date.now() + refreshedToken.refresh_expires_in * 1000,
            refreshToken: refreshedToken.refresh_token ?? token.refreshToken,
        };
    } catch (error) {
        console.log(error);
        return {
            ...token,
            error: "RefreshAccessTokenError",
        };
    }
}

export const authOptions: NextAuthOptions = {
    providers: [
        KeycloakProvider({
            clientId: process.env.KEYCLOAK_CLIENT_ID!,
            clientSecret: process.env.KEYCLOAK_CLIENT_SECRET!,
            issuer: process.env.KEYCLOAK_BASE_URL,
            // extended default providers options - scope - to get user roles
            authorization: { params: { scope: "openid email profile roles" } },
        }),
    ],
    pages: {
        signIn: `${pagesRoutes.signIn}`,
    },
    callbacks: {
        jwt: async ({ token, account, user, profile }) => {
            if (account && user) {
                // just fired first time - when signed in

                token.accessToken = account.access_token;
                token.refreshToken = account.refresh_token;
                token.accessTokenExpired = account.expires_at! * 1000;
                token.refreshTokenExpired = Date.now() + +account.refresh_expires_in! * 1000;
                token.user = user;
                token.tokenId = account.id_token;
                return token;
            }

            if (Date.now() < token.accessTokenExpired!) {
                return token;
            }

            return refreshAccessToken(token);
        },
        session: async ({ session, token }) => {
            session.accessToken = token.accessToken as string;
            session.tokenId = token.tokenId as string;
            return session;
        },
    },
};

export default NextAuth(authOptions);
