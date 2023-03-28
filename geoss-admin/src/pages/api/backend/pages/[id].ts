import type { NextApiRequest, NextApiResponse } from "next";
import { PageService } from "@/services/api";
import { IPageData } from "@/types/models";

export default async function handler(req: NextApiRequest, res: NextApiResponse) {
    const { query } = req;
    const id = query.id as string;
    let result;

    try {
        switch (req.method) {
            case "GET":
                result = await PageService.getPage(+id);
                break;
            case "PUT":
                const pageData = req.body as IPageData;
                result = await PageService.updatePage(+id, pageData);
                break;
            case "DELETE":
                result = await PageService.deletePage(+id);
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
