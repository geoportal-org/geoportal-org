import { WorkerData, WorkerType } from "@/types/models/workers";
import { fetcher } from "@/utils/fetcher";

export const WorkersService = {
    baseUrl: `${process.env.NEXT_PUBLIC_API}/workers/thesaurus/rest/jobs`,

    getWorker: async (type: WorkerType): Promise<WorkerData> => fetcher({ url: `${WorkersService.baseUrl}/${type}` }),
    runWorker: async (type: WorkerType): Promise<WorkerData> =>
        fetcher({ url: `${WorkersService.baseUrl}/${type}`, method: "POST" }),
};
