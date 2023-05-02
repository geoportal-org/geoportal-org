import NextAuth, { NextAuthOptions } from "next-auth";
import KeycloakProvider from "next-auth/providers/keycloak";
import { pagesRoutes } from "@/data";

export const authOptions: NextAuthOptions = {
    providers: [
        KeycloakProvider({
            clientId: process.env.KEYCLOAK_CLIENT_ID!,
            clientSecret: process.env.KEYCLOAK_CLIENT_SECRET!,
            issuer: process.env.KEYCLOAK_BASE_URL,
        }),
    ],
    /*providers: [
        KeycloakProvider({
            id: "keycloak",
            name: "Keycloak",
            clientId: process.env.KEYCLOAK_CLIENT_ID!,
            clientSecret: process.env.KEYCLOAK_CLIENT_SECRET!,
            issuer: process.env.KEYCLOAK_BASE_URL,
            requestTokenUrl: `${process.env.KEYCLOAK_BASE_URL}/openid-connect/auth`,
            authorization: {
                params: {
                    scope: "openid email profile",
                },
            },
            checks: ["pkce", "state"],
            idToken: true,
            profile(profile) {
                console.log(profile);
                return {
                    id: profile.sub,
                    ...profile,
                };
            },
        }),
    ],
    session: {
        strategy: "jwt",
    },*/
    pages: {
        signIn: `${pagesRoutes.signIn}`,
    },
    /*callbacks: {
        async redirect({ url, baseUrl }) {
            return Promise.resolve(url.startsWith(baseUrl) ? url : baseUrl);
        },
        async signIn({ account, user }) {
            console.log("xxxx");
            console.log(account);
            console.log(user);
            if (account && user) {
                return true;
            } else {
                return false;
            }
        },
        jwt({ token, user, account }) {
            //Initial sign in
            if (account && user) {
                // Add access_token, refresh_token and expirations to the token right after signin
                token.accessToken = account.access_token;
                token.refreshToken = account.refresh_token;
                token.accessTokenExpired = account.expires_at! * 1000;
                token.refreshTokenExpired =
            Date.now() + account.refresh_expires_in! * 1000;
                token.user = user;
            }
            return token;

            // Return previous token if the access token has not expired yet
            if (Date.now() < token.accessTokenExpired) {
          return token;
        }
  
        // Access token has expired, try to update it
        return refreshAccessToken(token);
        },
        session({ session, token }) {
            console.log(session);
            console.log(token);
            return session;
        },
    },*/
    callbacks: {
        jwt: async ({ token, user }) => {
            console.log(user);
            return token;
        },
        session: async ({ session, token }) => {
            console.log(session);
            console.log(token);
            return session;
        },
    },
};

export default NextAuth(authOptions);
