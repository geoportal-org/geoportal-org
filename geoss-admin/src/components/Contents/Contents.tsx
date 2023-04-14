import { useRouter } from "next/router";
import { useEffect, useMemo, useState } from "react";
import {
    ColumnDef,
    createColumnHelper,
    useReactTable,
    getCoreRowModel,
    PaginationState,
    SortingState,
    RowSelectionState,
} from "@tanstack/react-table";
import { Checkbox } from "@chakra-ui/react";
import { Table, MainContent, TableActions, Loader, TablePagination, TextInfo } from "@/components";
import { ContentService } from "@/services/api";
import useFormatMsg from "@/utils/useFormatMsg";
import { convertIsoDate, getIdFromUrl, setTableSorting } from "@/utils/helpers";
import { IContent } from "@/types/models";
import { ButtonVariant, TableActionsSource } from "@/types";
import { initPagination, pagesRoutes } from "@/data";

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
    const { translate } = useFormatMsg();
    const columnHelper = createColumnHelper<IContent>();
    const router = useRouter();

    useEffect(() => {
        handlePaginationParamsChange();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [pageIndex, pageSize, sorting]);

    const pagination = useMemo(
        () => ({
            pageIndex,
            pageSize,
        }),
        [pageIndex, pageSize]
    );

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
                ...(sorting[0] && setTableSorting(sorting)),
            });
            setContentsList(() => content);
            setDataInfo(() => ({ totalPages, totalElements }));
        } catch (e) {
            console.error(e);
        } finally {
            setIsLoading(false);
            setIsPageChange(false);
        }
    };

    const navigateToAddContent = () => router.push(pagesRoutes.addContent);

    const deleteAction = () => {
        const deleteIds = table.getFilteredSelectedRowModel().rows.map((row) => +getIdFromUrl(row.getValue("id")));
        console.log(deleteIds);
        console.log(table.getFilteredSelectedRowModel());
    };

    const deleteContent = () => handlePaginationParamsChange();

    const rowData = useMemo(() => contentsList, [contentsList]);

    const columns = useMemo<ColumnDef<IContent, any>[]>(
        () => [
            columnHelper.display({
                header: ({ table }) => (
                    <Checkbox
                        isChecked={table.getIsAllPageRowsSelected()}
                        onChange={table.getToggleAllPageRowsSelectedHandler()}
                        variant="geossTable"
                    />
                ),
                cell: ({ row }) => (
                    <Checkbox
                        isChecked={row.getIsSelected()}
                        onChange={row.getToggleSelectedHandler()}
                        variant="geossTable"
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
            }),
            columnHelper.accessor("createdDate", {
                header: translate("pages.contents.creation-date"),
                cell: (info) => (info.getValue() ? convertIsoDate(info.getValue(), router.locale || "en") : "-"),
            }),
            columnHelper.accessor("modifiedDate", {
                header: translate("pages.contents.modification-date"),
                cell: (info) => (info.getValue() ? convertIsoDate(info.getValue(), router.locale || "en") : "-"),
            }),
            columnHelper.accessor("createdBy", {
                header: translate("pages.contents.author"),
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
                        onDeleteAction={deleteContent}
                    />
                ),
            }),
        ],
        // eslint-disable-next-line react-hooks/exhaustive-deps
        [router.locale]
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
        },
        {
            titleId: "pages.contents.delete-selected",
            variant: ButtonVariant.GHOST,
            color: "brand.cancel",
            onClick: () => deleteAction(),
            disabled: !table.getFilteredSelectedRowModel().rows.length,
        },
    ];

    if (isLoading) {
        return <Loader />;
    }

    return (
        <MainContent titleId="pages.contents.title" actions={headingActions}>
            {totalElements ? (
                <>
                    <Table tableData={table} />
                    <TablePagination tableData={table} isPageChange={isPageChange} />
                </>
            ) : (
                <TextInfo id="information.info.no-contents" />
            )}
        </MainContent>
    );
};
