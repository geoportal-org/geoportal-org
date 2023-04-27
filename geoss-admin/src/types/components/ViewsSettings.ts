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
    created_by: string;
    created_on: string;
    modified_by: string;
    modified_on: string;
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
