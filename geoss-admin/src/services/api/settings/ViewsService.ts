import { QueryParams } from "@/types";
import { ISubView, ISubViewData, IView, IViewData, IViewList } from "@/types/models";
import { fetcher } from "@/utils/fetcher";

export const ViewsService = {
    baseUrl: `${process.env.NEXT_PUBLIC_API}/settings/rest/views`,

    getViewsList: async (query?: QueryParams): Promise<IViewList> => fetcher({ url: ViewsService.baseUrl, query }),

    getView: async (id: number): Promise<IView> => fetcher({ url: `${ViewsService.baseUrl}/${id}` }),

    createView: async (viewData: IViewData): Promise<IView> =>
        fetcher({ url: ViewsService.baseUrl, method: "POST", body: viewData }),

    updateView: async (id: number, viewData: IViewData): Promise<IView> =>
        fetcher({ url: `${ViewsService.baseUrl}/${id}`, method: "PUT", body: viewData }),

    patchView: async (id: number, viewData: Partial<IViewData>): Promise<IView> =>
        fetcher({ url: `${ViewsService.baseUrl}/${id}`, method: "PATCH", body: viewData }),

    deleteView: async (id: number): Promise<null> =>
        fetcher({ url: `${ViewsService.baseUrl}/${id}`, method: "DELETE" }),

    deleteViewSubViews: async (id: number): Promise<null> =>
        fetcher({ url: `${ViewsService.baseUrl}/${id}/options`, method: "DELETE" }),

    createSubView: async (id: number, subViewData: ISubViewData): Promise<ISubView> =>
        fetcher({ url: `${ViewsService.baseUrl}/${id}/options`, method: "POST", body: subViewData }),

    getSubView: async (viewId: number, subViewId: number): Promise<ISubView> =>
        fetcher({ url: `${ViewsService.baseUrl}/${viewId}/options/${subViewId}` }),

    updateSubView: async (viewId: number, subViewId: number, subViewData: ISubViewData): Promise<ISubView> =>
        fetcher({ url: `${ViewsService.baseUrl}/${viewId}/options/${subViewId}`, method: "PUT", body: subViewData }),

    patchSubView: async (viewId: number, subViewId: number, subViewData: Partial<ISubViewData>): Promise<ISubView> =>
        fetcher({ url: `${ViewsService.baseUrl}/${viewId}/options/${subViewId}`, method: "PATCH", body: subViewData }),

    deleteSubView: async (viewId: number, subViewId: number): Promise<null> =>
        fetcher({ url: `${ViewsService.baseUrl}/${viewId}/options/${subViewId}`, method: "DELETE" }),
};
