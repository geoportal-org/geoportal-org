import type { NextApiRequest, NextApiResponse } from "next";
import { FileRepositoryService } from "@/services/api";
import { QueryParams } from "@/types";

export default async function handler(req: NextApiRequest, res: NextApiResponse) {
    let result;
    try {
        switch (req.method) {
            case "GET":
                const query = req.query as QueryParams;
                result = await FileRepositoryService.getFoldersList(query);
                break;
            case "POST":
                const folderData = req.body;
                result = await FileRepositoryService.createFolder(folderData);
                break;
        }
        res.status(res.statusCode).send(result);
    } catch (e) {
        const isResponse = e instanceof Response;
        const status = isResponse ? e.status : 500;
        const statusText = isResponse ? e.statusText : "Invalid API call";
        res.status(status).end(statusText);
    }
}
