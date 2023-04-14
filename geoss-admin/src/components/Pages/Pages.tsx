import { useRouter } from "next/router";
import { useMemo, useState, useEffect } from "react";
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
import { Loader, MainContent, Table, TableActions, TablePagination, TextInfo } from "@/components";
import { PageService } from "@/services/api";
import { initPagination, pagesRoutes } from "@/data";
import useFormatMsg from "@/utils/useFormatMsg";
import { convertIsoDate, getIdFromUrl, setTableSorting } from "@/utils/helpers";
import { ButtonVariant, MainContentAction, TableActionsSource } from "@/types";
import { IPage } from "@/types/models";

export const Pages = () => {
    const [pagesList, setPagesList] = useState<IPage[]>([]);
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
    const columnHelper = createColumnHelper<IPage>();
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
                _embedded: { page },
                page: { totalPages, totalElements },
            } = await PageService.getPagesList({
                page: table.getState().pagination.pageIndex,
                size: table.getState().pagination.pageSize,
                ...(sorting[0] && setTableSorting(sorting)),
            });
            setPagesList(() => page);
            setDataInfo(() => ({ totalPages, totalElements }));
        } catch (e) {
            console.error(e);
        } finally {
            setIsLoading(false);
            setIsPageChange(false);
        }
    };

    const navigateToAddPage = () => router.push(pagesRoutes.addPage);

    const deletePage = () => handlePaginationParamsChange();

    const rowData = useMemo(() => pagesList, [pagesList]);

    const columns = useMemo<ColumnDef<IPage, any>[]>(
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
                header: translate("pages.page.page-title"),
            }),
            columnHelper.accessor("createdDate", {
                header: translate("pages.page.creation-date"),
                cell: (info) => (info.getValue() ? convertIsoDate(info.getValue(), router.locale || "en") : "-"),
            }),
            columnHelper.accessor("modifiedDate", {
                header: translate("pages.page.modification-date"),
                cell: (info) => (info.getValue() ? convertIsoDate(info.getValue(), router.locale || "en") : "-"),
            }),
            columnHelper.accessor("createdBy", {
                header: translate("pages.page.author"),
            }),
            columnHelper.accessor("published", {
                header: translate("pages.contents.status"),
                cell: (info) => translate(info.getValue() ? "general.published" : "general.draft"),
            }),
            columnHelper.display({
                header: translate("pages.page.actions"),
                id: "actions",
                cell: (info) => (
                    <TableActions
                        itemId={getIdFromUrl(info.row.getValue("id"))}
                        item={info.row.original}
                        actionsSource={TableActionsSource.PAGES}
                        onDeleteAction={deletePage}
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

    const headingActions: MainContentAction[] = [
        { titleId: "pages.page.add", onClick: navigateToAddPage },
        {
            titleId: "pages.page.delete-selected",
            variant: ButtonVariant.GHOST,
            color: "brand.cancel",
            onClick: () => console.log(table.getFilteredSelectedRowModel()),
            disabled: !Object.keys(selectedContents).length,
        },
    ];

    if (isLoading) {
        return <Loader />;
    }

    return (
        <MainContent titleId="pages.page.title" actions={headingActions}>
            {totalElements ? (
                <>
                    <Table tableData={table} />
                    <TablePagination tableData={table} isPageChange={isPageChange} />
                </>
            ) : (
                <TextInfo id="information.info.no-pages" />
            )}
        </MainContent>
    );
};
