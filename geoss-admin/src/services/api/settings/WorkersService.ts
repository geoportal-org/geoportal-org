import { WorkerData, WorkerType } from "@/types/models/workers";
import { fetcher } from "@/utils/fetcher";

export const WorkersService = {
    baseUrl: `${process.env.NEXT_PUBLIC_API}/workers/thesaurus/rest/jobs`,
    geodabUrl: `${process.env.NEXT_PUBLIC_API}/workers/geodab/rest/jobs/geodab`,
    sdgUrl: `${process.env.NEXT_PUBLIC_API}/workers/sdg/rest/jobs/sdg`,

    getWorker: async (type: WorkerType): Promise<WorkerData> => fetcher({ url: `${WorkersService.baseUrl}/${type}` }),
    runWorker: async (type: WorkerType): Promise<WorkerData> =>
        fetcher({ url: `${WorkersService.baseUrl}/${type}`, method: "POST" }),
    getGeoDabWorker: async (): Promise<WorkerData> => fetcher({ url: `${WorkersService.geodabUrl}` }),
    runGeoDabWorker: async (): Promise<WorkerData> =>
        fetcher({ url: `${WorkersService.geodabUrl}`, method: "POST" }),
    getSDGWorker: async (): Promise<WorkerData> => fetcher({ url: `${WorkersService.sdgUrl}` }),
    runSDGWorker: async (): Promise<WorkerData> => fetcher({ url: `${WorkersService.sdgUrl}`, method: "POST" }),
};
