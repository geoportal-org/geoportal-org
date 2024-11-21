import NextAuth, { NextAuthOptions } from "next-auth";
import KeycloakProvider from "next-auth/providers/keycloak";
import type { JWT } from "next-auth/jwt";
import { pagesRoutes } from "@/data";
import { jwtDecode } from "jwt-decode";

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
            // accessToken: refreshedToken.access_token,
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
    debug: true,
    providers: [
        KeycloakProvider({
            clientId: process.env.KEYCLOAK_CLIENT_ID!,
            clientSecret: process.env.KEYCLOAK_CLIENT_SECRET!,
            issuer: process.env.KEYCLOAK_BASE_URL,
            authorization: { params: { scope: "openid email profile roles" } },
        }),
    ],
    pages: {
        signIn: `${pagesRoutes.signIn}`,
    },
    session: {
        strategy: "jwt",
    },
    secret: process.env.NEXTAUTH_SECRET,
    jwt: {
        secret: process.env.NEXTAUTH_SECRET,
        maxAge: 5 * 60 * 1000,
    },
    callbacks: {
        jwt: async ({ token, account, user }) => {
            if (account && user) {
                const decodedToken = jwtDecode(account.id_token || "");
                console.log("DECODED TOKEN");
                console.log(decodedToken);
                // token.accessToken = account.access_token;
                // token.accessTokenExpired = account.expires_at;
                let hasRole = false;
                if (
                    //@ts-ignore
                    decodedToken.resource_access["geoss-admin"] &&
                    //@ts-ignore
                    decodedToken.resource_access["geoss-admin"].roles.some((e) => e === "manage_admin")
                ) {
                    hasRole = true;
                }
                token.hasRole = hasRole;
                token.refreshToken = account.refresh_token;
                token.tokenId = account.id_token;
                token.user = user;
                return token;
                //@ts-ignore
            } else if (Date.now() < token.accessTokenExpired) {
                return token;
            } else {
                return refreshAccessToken(token);
            }
        },
        session: async ({ session, token }) => {
            // session.accessToken = token.accessToken as string;
            session.expires = token.refreshTokenExpired as string;
            session.tokenId = token.tokenId as string;
            //@ts-ignore
            session.userId = token.user.id;
            //@ts-ignore
            session.hasRole = token.hasRole;
            return session;
        },
    },
};

export default NextAuth(authOptions);
