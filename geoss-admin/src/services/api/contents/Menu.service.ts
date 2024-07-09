import { QueryParams } from "@/types";
import { IMenuItem, IMenuItemData, IMenuList, IFoundMenuList } from "@/types/models";
import { fetcher } from "@/utils/fetcher";

export const MenuService = {
    baseUrl: `${process.env.NEXT_PUBLIC_API}/contents/rest/menu`,

    getMenuListBySiteId: async (query?: QueryParams): Promise<IMenuList> => fetcher({ url: `${MenuService.baseUrl}/search/findBySiteId`, query }),

    createMenuItem: async (menuItemData: IMenuItemData): Promise<IMenuItem> =>
        fetcher({ url: MenuService.baseUrl, body: menuItemData, method: "POST" }),

    getMenuItem: async (id: number): Promise<IMenuItem> => fetcher({ url: `${MenuService.baseUrl}/${id}` }),

    updateMenuItem: async (menuItemData: Partial<IMenuItemData>, id: number): Promise<IMenuItem> =>
        fetcher({ url: `${MenuService.baseUrl}/${id}`, body: menuItemData, method: "PATCH" }),

    deleteMenuItem: async (id: number) => fetcher({ url: `${MenuService.baseUrl}/${id}`, method: "DELETE" }),

    findMenuItems: async (query?: QueryParams): Promise<IFoundMenuList> =>
        fetcher({ url: `${MenuService.baseUrl}/search/findByLevelId`, query }),
};
