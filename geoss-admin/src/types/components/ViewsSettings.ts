export type ViewsSettingsExpandControllerProps = {
    isExpanded: boolean;
    onExpand: () => void;
    msgId: string;
    isDisabled?: boolean;
};

export type FormattedView = {
    id: number;
    label: string;
    title: string;
    value: string;
    defaultOption?: boolean;
    createdBy: string;
    createdOn: string;
    modifiedBy: string;
    modifiedOn: string;
    parentViewName?: string;
    parentViewId?: number;
    subRows: FormattedView[];
};

export type EditedViewInfo = {
    viewId?: number;
    parentViewId?: number;
    isEditMode: boolean;
    item?: FormattedView;
};
