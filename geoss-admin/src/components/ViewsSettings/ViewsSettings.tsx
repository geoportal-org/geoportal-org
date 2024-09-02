import { useRouter } from "next/router";
import { useContext, useEffect, useMemo, useState } from "react";
import {
    ColumnDef,
    createColumnHelper,
    ExpandedState,
    getCoreRowModel,
    getExpandedRowModel,
    PaginationState,
    SortingState,
    useReactTable,
} from "@tanstack/react-table";
import { FormikHelpers, FormikValues } from "formik";
import { Box, useDisclosure } from "@chakra-ui/react";
import { Loader, MainContent, SideBar, Table, TableActions, TablePagination, TextInfo } from "@/components";
import { ViewsSettingsExpandController } from "./ViewsSettingsExpandController";
import { ViewsSettingsManage } from "./ViewsSettingsManage";
import { ViewsService } from "@/services/api";
import { ViewsContext } from "@/context";
import {
    cutString,
    generateGenericErrorMessage,
    getViewsActionsMsgIds,
    getViewsSideBarTitleId,
    prepareViewsToShow,
    setTableSorting,
} from "@/utils/helpers";
import useCustomToast from "@/utils/useCustomToast";
import useFormatMsg from "@/utils/useFormatMsg";
import { MainContentAction, TableActionsSource, ToastStatus, FormattedView, EditedViewInfo } from "@/types";
import { ISubViewData, IViewData } from "@/types/models";
import { extendedViewsPagination, initPagination } from "@/data";
import { SiteContext, SiteContextValue } from "@/context/CurrentSiteContext";

export const ViewsSettings = () => {
    const [isLoading, setIsLoading] = useState(true);
    const [expanded, setExpanded] = useState<ExpandedState>({});
    const [isPageChange, setIsPageChange] = useState(false);
    const [viewsList, setViewsList] = useState<FormattedView[]>([]);
    const [editedView, setEditedView] = useState<EditedViewInfo | null>(null);
    const [{ totalPages, totalElements }, setDataInfo] = useState<{ totalPages: number; totalElements: number }>({
        totalPages: 0,
        totalElements: 0,
    });
    const [{ pageIndex, pageSize }, setPagination] = useState<PaginationState>({
        pageIndex: initPagination.page,
        pageSize: initPagination.size,
    });
    const [sorting, setSorting] = useState<SortingState>([]);
    const { isOpen: isSideBarOpen, onOpen: onSideBarOpen, onClose: onSideBarClose } = useDisclosure();
    const columnHelper = createColumnHelper<FormattedView>();
    const { translate } = useFormatMsg();
    const { showToast } = useCustomToast();
    const router = useRouter();

    //siteId
    const { currentSiteId } = useContext<SiteContextValue>(SiteContext);

    useEffect(() => {
        handlePaginationParamsChange();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [pageIndex, pageSize, sorting]);

    const handlePaginationParamsChange = async () => {
        try {
            setIsPageChange(true);
            const {
                _embedded: { views },
                page: { totalElements, totalPages },
            } = await ViewsService.getViewsList({
                page: table.getState().pagination.pageIndex,
                size: table.getState().pagination.pageSize,
                ...(sorting[0] && setTableSorting(sorting)),
            });
            const viewsData = prepareViewsToShow(views);
            setViewsList(() => viewsData);
            setDataInfo(() => ({ totalPages, totalElements }));
        } catch (e: any) {
            console.error(e)
            const msg = generateGenericErrorMessage(e)

            showToast({
                title: translate("general.error"),
                description: `${msg || ""}`,
                status: ToastStatus.ERROR,
            });
        } finally {
            setIsPageChange(false);
            setIsLoading(false);
        }
    };

    const onAddViewAction = () => enableEditMode(null);

    const onEditViewAction = (viewId: number, item: FormattedView) => {
        const viewData = { viewId, parentViewId: undefined, isEditMode: true, item };
        enableEditMode(viewData);
    };

    const onAddSubViewAction = (parentViewId: number) => {
        const viewData = { parentViewId, viewId: undefined, isEditMode: false, item: undefined };
        enableEditMode(viewData);
    };

    const onEditSubViewAction = (parentViewId: number, viewId: number, item: FormattedView) => {
        const viewData = { parentViewId, viewId, isEditMode: true, item };
        enableEditMode(viewData);
    };

    const enableEditMode = (viewData: EditedViewInfo | null) => {
        setEditedView(viewData);
        onSideBarOpen();
    };

    const addNewView = async (values: FormikValues, actions: FormikHelpers<FormikValues>) => {
        const viewData = getViewData(values);
        const isMainView = "defaultOption" in viewData;
        const { successMsgId, errorMsgId } = getViewsActionsMsgIds(isMainView);
        try {
            if (isMainView) {
                const { defaultOption, id: defaultOptionId } = await ViewsService.createView(viewData);
                defaultOption && (await updateDefaultOption(defaultOptionId));
            } else if (editedView?.parentViewId) {
                await ViewsService.createSubView(editedView?.parentViewId, viewData);
            }
            handlePaginationParamsChange();
            actions.resetForm();
            showInfo("general.created", successMsgId, { title: viewData.label }, ToastStatus.SUCCESS);
        } catch (e: any) {
            console.error(e)
            const msg = generateGenericErrorMessage(e)

            showToast({
                title: translate("general.error"),
                description: `${msg || ""}`,
                status: ToastStatus.ERROR,
            });
            showInfo("general.error", errorMsgId);
        }
    };

    const updateView = async (values: FormikValues, updateFormState: (values: FormikValues) => void) => {
        const viewData = getViewData(values);
        const isMainView = "defaultOption" in viewData;
        const { successMsgId, errorMsgId } = getViewsActionsMsgIds(isMainView, false);
        try {
            if (isMainView && editedView?.viewId) {
                const { defaultOption, id: defaultOptionId } = await ViewsService.patchView(
                    editedView.viewId,
                    viewData
                );
                defaultOption && (await updateDefaultOption(defaultOptionId));
            } else if (editedView?.parentViewId && editedView.viewId) {
                await ViewsService.updateSubView(editedView.parentViewId, editedView.viewId, viewData);
            }
            handlePaginationParamsChange();
            updateFormState(values);
            showInfo("general.updated", successMsgId, { title: viewData.label }, ToastStatus.SUCCESS);
        } catch (e: any) {
            console.error(e)
            const msg = generateGenericErrorMessage(e)

            showToast({
                title: translate("general.error"),
                description: `${msg || ""}`,
                status: ToastStatus.ERROR,
            });
            showInfo("general.error", errorMsgId);
        }
    };

    const getViewData = (values: FormikValues): IViewData | ISubViewData => {
        const { label, value, ...rest } = values;
        return {
            label,
            value,
            title: label,
            ...(Object.keys(rest).length && { defaultOption: rest.defaultOption === "true" }),
            siteId: currentSiteId,
        };
    };

    const updateDefaultOption = async (defaultOptionId: number) => {
        const {
            _embedded: { views: extendedViewsList },
        } = await ViewsService.getViewsList(extendedViewsPagination);
        const defaultView = extendedViewsList.find((view) => view.defaultOption && view.id !== defaultOptionId);
        defaultView && (await ViewsService.patchView(defaultView.id, { defaultOption: false }));
    };

    const showInfo = (
        titleId: string,
        msgId: string,
        values?: { [index: string]: string },
        status: ToastStatus = ToastStatus.ERROR
    ) =>
        showToast({
            title: translate(titleId),
            description: translate(msgId, values),
            status,
        });

    const pagination = useMemo(
        () => ({
            pageIndex,
            pageSize,
        }),
        [pageIndex, pageSize]
    );

    const rowData = useMemo(() => viewsList, [viewsList]);

    const columns = useMemo<ColumnDef<FormattedView, any>[]>(
        () => [
            columnHelper.display({
                header: ({ table }) => {
                    const { rows, flatRows } = table.getExpandedRowModel();
                    const isAllExpanded = rows.length === flatRows.length;
                    return (
                        <ViewsSettingsExpandController
                            isExpanded={isAllExpanded && table.getCanSomeRowsExpand()}
                            onExpand={() => table.toggleAllRowsExpanded()}
                            msgId="pages.views.expand-all"
                            isDisabled={!table.getCanSomeRowsExpand() || isPageChange}
                        />
                    );
                },
                cell: ({ row }) =>
                    row.getCanExpand() ? (
                        <ViewsSettingsExpandController
                            isExpanded={row.getIsExpanded()}
                            onExpand={row.getToggleExpandedHandler()}
                            msgId="pages.views.expand"
                            isDisabled={isPageChange}
                        />
                    ) : null,
                id: "expand",
            }),
            columnHelper.accessor("label", {
                header: translate("pages.views.name"),
                cell: ({ row, getValue }) => <Box pl={row.depth ? "4" : "0"}>{cutString(getValue(), 30)}</Box>,
            }),
            columnHelper.accessor("value", {
                header: translate("pages.views.id"),
                cell: ({ getValue }) => cutString(getValue(), 35),
            }),
            columnHelper.accessor("parentViewName", {
                header: translate("pages.views.parent"),
                cell: ({ getValue }) => (getValue() ? cutString(getValue(), 30) : ""),
                enableSorting: false,
            }),
            columnHelper.accessor("defaultOption", {
                header: translate("pages.views.default"),
                cell: (info) => {
                    if (!info.row.original.subRows) {
                        return "";
                    }
                    return translate(`general.${info.getValue() ? "yes" : "no"}`).toLowerCase();
                },
            }),
            columnHelper.display({
                header: translate("general.actions"),
                id: "actions",
                cell: (info) => (
                    <TableActions
                        itemId={info.row.original.id}
                        item={info.row.original}
                        actionsSource={TableActionsSource.VIEWS}
                        onDeleteAction={handlePaginationParamsChange}
                        disabled={isPageChange}
                    />
                ),
            }),
        ],
        // eslint-disable-next-line react-hooks/exhaustive-deps
        [router.locale, isPageChange]
    );

    const table = useReactTable({
        columns,
        data: rowData,
        getCoreRowModel: getCoreRowModel(),
        getExpandedRowModel: getExpandedRowModel(),
        getSubRows: (row) => row.subRows,
        manualPagination: true,
        manualSorting: true,
        enableExpanding: true,
        onExpandedChange: setExpanded,
        onPaginationChange: setPagination,
        onSortingChange: setSorting,
        pageCount: totalPages,
        state: {
            pagination,
            expanded,
            sorting,
        },
    });

    const headingActions: MainContentAction[] = [
        {
            titleId: "pages.views.add",
            onClick: () => onAddViewAction(),
            disabled: isPageChange || isLoading,
        },
    ];

    return (
        <ViewsContext.Provider
            value={{ onEditViewAction, onAddSubViewAction, onEditSubViewAction, addNewView, updateView, editedView }}
        >
            <MainContent titleId="nav.settings.section.views" actions={headingActions}>
                {isLoading && <Loader />}
                {!!totalElements && !isLoading && (
                    <>
                        <Table tableData={table} isDisabled={isPageChange} />
                        <TablePagination tableData={table} isPageChange={isPageChange} />
                    </>
                )}
                {!totalElements && !isLoading && <TextInfo id="information.info.no-views" />}
            </MainContent>

            <SideBar
                isOpen={isSideBarOpen}
                onClose={onSideBarClose}
                titleId={getViewsSideBarTitleId(editedView)}
                content={<ViewsSettingsManage />}
            />
        </ViewsContext.Provider>
    );
};
