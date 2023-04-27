import { Dispatch, RefObject, SetStateAction } from "react";
import { FormikHelpers, FormikValues } from "formik";
import { DragLayerMonitorProps } from "@minoru/react-dnd-treeview";
import { NodeModel } from "@minoru/react-dnd-treeview/dist/types";
import { IMenuItem } from "@/types/models";

export type MenuContentItemProps = {
    node: NodeModel<IMenuItem>;
    depth: number;
    handleRef: RefObject<any>;
    openAll: () => void;
    onAddAction: (parentMenuId: number) => void;
    onDeleteAction: (item: NodeModel<IMenuItem>) => void;
    onEditAction: (parentMenuId: number, menuItemId: number, menuItem: IMenuItem) => void;
};

export type MenuContentItemPreviewProps = {
    monitorProps: DragLayerMonitorProps<IMenuItem>;
};

export type MenuContentManageProps = {
    parentMenuId: number;
    menuItemId?: number;
    manageMenuItem: (
        parentMenuId: number,
        values: FormikValues,
        actions: FormikHelpers<FormikValues>,
        setInitValues: Dispatch<SetStateAction<FormikValues>>,
        menuItemId?: number
    ) => Promise<void>;
    isMainMenuItem: boolean;
    menuItem?: IMenuItem;
};

export type MovedItemInfo = {
    currentLevelId: number;
    prevParentId: number | undefined;
    currentParentId: number;
    currentPriority: number;
    isLevelChange: boolean;
    isParentChange: boolean;
};

export type MovedItemData = {
    priority: number;
    parentMenuId?: number;
    levelId?: number;
};
