import type { NextApiRequest, NextApiResponse } from "next";
import { getServerSession } from "next-auth";
import { authOptions } from "./[...nextauth]";

export default async function signOutProvider(req: NextApiRequest, res: NextApiResponse) {
    if (req.method === "PUT") {
        const session = await getServerSession(req, res, authOptions);
        if (session?.tokenId) {
            try {
                const endProviderSessionUrl = `${process.env.KEYCLOAK_BASE_URL}/protocol/openid-connect/logout`;
                const endProviderSessionParams = new URLSearchParams();
                endProviderSessionParams.append("id_token_hint", session.tokenId);
                await fetch(`${endProviderSessionUrl}?${endProviderSessionParams}`);
                res.status(200).json(null);
            } catch (error) {
                res.status(500).json(null);
            }
        } else {
            res.status(200).json(null);
        }
    }
}
