import type { NextApiRequest, NextApiResponse } from "next";
import { FileRepositoryService } from "@/services/api";

export default async function handler(req: NextApiRequest, res: NextApiResponse) {
    const { query } = req;
    const id = query.id as string;
    let result;

    try {
        switch (req.method) {
            case "GET":
                result = await FileRepositoryService.getFolder(+id);
                break;
            case "PATCH":
                const folderData = req.body;
                result = await FileRepositoryService.updateFolderTitle(+id, folderData);
                break;
            case "DELETE":
                result = await FileRepositoryService.deleteFolder(+id);
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
