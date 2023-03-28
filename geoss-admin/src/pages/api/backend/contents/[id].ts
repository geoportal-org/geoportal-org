import type { NextApiRequest, NextApiResponse } from "next";
import { ContentService } from "@/services/api";

export default async function handler(req: NextApiRequest, res: NextApiResponse) {
    const { query } = req;
    const id = query.id as string;
    let result;

    try {
        switch (req.method) {
            case "GET":
                result = await ContentService.getContent(+id);
                break;
            case "PUT":
                const contentData = req.body;
                result = await ContentService.updateContent(+id, contentData);
                break;
            case "DELETE":
                result = await ContentService.deleteContent(+id);
                break;
        }
        req.method !== "DELETE" ? res.status(res.statusCode).send(result) : res.status(204).end();
    } catch (e) {
        const isResponse = e instanceof Response;
        const status = isResponse ? e.status : 500;
        const statusText = isResponse ? e.statusText : "Invalid API call";
        res.status(status).end(statusText);
    }
}
