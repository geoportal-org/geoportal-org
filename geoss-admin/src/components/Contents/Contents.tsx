import { useRouter } from "next/router";
import { useContext, useEffect, useMemo, useState } from "react";
import {
    ColumnDef,
    createColumnHelper,
    useReactTable,
    getCoreRowModel,
    PaginationState,
    SortingState,
    RowSelectionState,
} from "@tanstack/react-table";
import { Checkbox, Text, useDisclosure } from "@chakra-ui/react";
import { Table, MainContent, TableActions, Loader, TablePagination, TextInfo, Modal, TextContent } from "@/components";
import { ContentService } from "@/services/api";
import useFormatMsg from "@/utils/useFormatMsg";
import useCustomToast from "@/utils/useCustomToast";
import {
    convertIsoDate,
    cutString,
    getIdFromUrl,
    getSelectedTableItemsIds,
    setDecisionModalActions,
    setTableSorting,
} from "@/utils/helpers";
import { IContent } from "@/types/models";
import { ButtonVariant, LocaleNames, TableActionsSource, ToastStatus } from "@/types";
import { initPagination, pagesRoutes } from "@/data";
import { SiteContext, SiteContextValue } from "@/context/CurrentSiteContext";

export const Contents = () => {
    const [contentsList, setContentsList] = useState<IContent[]>([]);
    const [selectedContents, setSelectedContents] = useState<RowSelectionState>({});
    const [isLoading, setIsLoading] = useState(true);
    const [isPageChange, setIsPageChange] = useState(false);
    const [{ totalPages, totalElements }, setDataInfo] = useState<{ totalPages: number; totalElements: number }>({
        totalPages: 0,
        totalElements: 0,
    });
    const [{ pageIndex, pageSize }, setPagination] = useState<PaginationState>({
        pageIndex: initPagination.page,
        pageSize: initPagination.size,
    });
    const [sorting, setSorting] = useState<SortingState>([]);
    const { isOpen, onOpen, onClose } = useDisclosure();
    const { translate } = useFormatMsg();
    const { showToast } = useCustomToast();
    const columnHelper = createColumnHelper<IContent>();
    const router = useRouter();
    const decisionModalActions = setDecisionModalActions(
        async () => await deleteSelectedContents(),
        () => onClose()
    );

    //siteId
    const { currentSiteId } = useContext<SiteContextValue>(SiteContext);

    useEffect(() => {
        handlePaginationParamsChange();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [pageIndex, pageSize, sorting, currentSiteId]);

    const handlePaginationParamsChange = async () => {
        try {
            setIsPageChange(true);
            table.resetRowSelection();
            const {
                _embedded: { content },
                page: { totalPages, totalElements },
            } = await ContentService.getContentList({
                page: table.getState().pagination.pageIndex,
                size: table.getState().pagination.pageSize,
                siteId: currentSiteId,
                ...(sorting[0] && setTableSorting(sorting)),
            });
            setContentsList(() => content);
            setDataInfo(() => ({ totalPages, totalElements }));
        } catch (e: any) {
            console.error(e)
let msg = "";
            if (e.errorInfo?.length) {
                msg = JSON.parse(e.errorInfo).detail;
            } else {
                                msg = e.errorInfo.message || e.errorInfo.errors[0].message
;
            }
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

    const deleteSelectedContents = async () => {
        try {
            await ContentService.deleteContents({ ids: getSelectedTableItemsIds(table).join(",") });
            handlePaginationParamsChange();
            onClose();
            showToast({
                title: translate("general.deleted"),
                description: translate("pages.contents.selected-deleted"),
            });
        } catch (e: any) {
            console.error(e)
let msg = "";
            if (e.errorInfo?.length) {
                msg = JSON.parse(e.errorInfo).detail;
            } else {
                                msg = e.errorInfo.message || e.errorInfo.errors[0].message
;
            }
            showToast({
                title: translate("general.error"),
                description: translate("information.error.loading") + `${msg || ""}`,
                status: ToastStatus.ERROR,
            });
        }
    };

    const navigateToAddContent = () => router.push(pagesRoutes.addContent);

    const pagination = useMemo(
        () => ({
            pageIndex,
            pageSize,
        }),
        [pageIndex, pageSize]
    );

    const rowData = useMemo(() => contentsList, [contentsList]);

    const columns = useMemo<ColumnDef<IContent, any>[]>(
        () => [
            columnHelper.display({
                header: ({ table }) => (
                    <Checkbox
                        isChecked={table.getIsAllPageRowsSelected()}
                        onChange={table.getToggleAllPageRowsSelectedHandler()}
                        variant="geossTable"
                        isDisabled={isPageChange}
                    />
                ),
                cell: ({ row }) => (
                    <Checkbox
                        isChecked={row.getIsSelected()}
                        onChange={row.getToggleSelectedHandler()}
                        variant="geossTable"
                        isDisabled={isPageChange}
                    />
                ),
                id: "check",
            }),
            columnHelper.accessor("_links.self.href", {
                header: "ID",
                cell: (info) => getIdFromUrl(info.getValue()),
                id: "id",
                enableSorting: false,
            }),
            columnHelper.accessor("title", {
                header: translate("pages.contents.content-title"),
                cell: ({ getValue }) => cutString(getValue()[router.locale as LocaleNames], 30),
            }),
            columnHelper.accessor("createdOn", {
                header: translate("pages.contents.creation-date"),
                cell: (info) => (info.getValue() ? convertIsoDate(info.getValue(), router.locale || "en") : "-"),
            }),
            columnHelper.accessor("modifiedOn", {
                header: translate("pages.contents.modification-date"),
                cell: (info) => (info.getValue() ? convertIsoDate(info.getValue(), router.locale || "en") : "-"),
            }),
            columnHelper.accessor("createdBy", {
                header: translate("pages.contents.author"),
                cell: ({ getValue }) => cutString(getValue(), 25),
            }),
            columnHelper.accessor("published", {
                header: translate("pages.contents.status"),
                cell: (info) => translate(info.getValue() ? "general.published" : "general.draft"),
            }),
            columnHelper.display({
                header: translate("pages.contents.actions"),
                id: "actions",
                cell: (info) => (
                    <TableActions
                        itemId={getIdFromUrl(info.row.getValue("id"))}
                        item={info.row.original}
                        actionsSource={TableActionsSource.WEBSITE}
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
        enableRowSelection: true,
        getCoreRowModel: getCoreRowModel(),
        manualPagination: true,
        manualSorting: true,
        onPaginationChange: setPagination,
        onRowSelectionChange: setSelectedContents,
        onSortingChange: setSorting,
        pageCount: totalPages,
        state: {
            pagination,
            rowSelection: selectedContents,
            sorting,
        },
    });

    const headingActions = [
        {
            titleId: "pages.contents.add",
            onClick: navigateToAddContent,
            disabled: isPageChange || isLoading,
        },
        {
            titleId: "pages.contents.delete-selected",
            variant: ButtonVariant.GHOST,
            color: "brand.cancel",
            onClick: () => onOpen(),
            disabled: !table.getFilteredSelectedRowModel().rows.length || isPageChange || isLoading,
        },
    ];

    return (
        <>
            <MainContent titleId="nav.contents.section.website" actions={headingActions}>
                {isLoading && <Loader />}
                {!!totalElements && !isLoading && (
                    <>
                        <Table tableData={table} isDisabled={isPageChange} />
                        <TablePagination tableData={table} isPageChange={isPageChange} />
                    </>
                )}
                {!totalElements && !isLoading && <TextInfo id="information.info.no-contents" />}
            </MainContent>

            <Modal
                modalHeader={translate("pages.contents.delete-content-title")}
                modalBody={
                    <Text py={4}>
                        <TextContent
                            id={`pages.contents.${
                                table.getFilteredSelectedRowModel().rows.length === 1
                                    ? "delete-selected-single-info"
                                    : "delete-selected-info"
                            }`}
                            itemId={getSelectedTableItemsIds(table).join(", ")}
                        />
                    </Text>
                }
                onModalClose={onClose}
                isModalOpen={isOpen}
                actions={decisionModalActions}
            />
        </>
    );
};
