import { useRouter } from "next/router";
import { useMemo, useState, useEffect } from "react";
import {
    ColumnDef,
    createColumnHelper,
    useReactTable,
    getCoreRowModel,
    getPaginationRowModel,
    getSortedRowModel,
} from "@tanstack/react-table";
import { Checkbox } from "@chakra-ui/react";
import { Loader, MainContent, Pagination, Table, TableActions } from "@/components";
import { PageService } from "@/services/api";
import { initPagination, pagesRoutes } from "@/data";
import useFormatMsg from "@/utils/useFormatMsg";
import { convertIsoDate, getIdFromUrl } from "@/utils/helpers";
import { ButtonVariant, MainContentAction, TableActionsSource } from "@/types";
import { IPage } from "@/types/models";

const basicPagination = initPagination;

export const Pages = () => {
    const [pagesList, setPagesList] = useState<IPage[]>([]);
    const [selectedContents, setSelectedContents] = useState<Record<string, boolean>>({});
    const [isLoading, setIsLoading] = useState(true);
    const [totalPages, setTotalPages] = useState<number>(1);
    const { translate } = useFormatMsg();
    const columnHelper = createColumnHelper<IPage>();
    const router = useRouter();

    useEffect(() => {
        const getPagesList = async () => {
            try {
                const {
                    _embedded: { page },
                    page: { totalPages },
                } = await PageService.getPagesList(basicPagination);
                setTotalPages(() => totalPages);
                setPagesList(() => page);
            } catch (e) {
                console.error(e);
            } finally {
                setIsLoading(false);
            }
        };
        getPagesList();
    }, []);

    const navigateToAddPage = () => router.push(pagesRoutes.addPage);

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

    const deletePage = (itemId: string) =>
        setPagesList((pagesList) => pagesList.filter((page) => getIdFromUrl(page._links.self.href) !== itemId));

    const handlePageChange = async (pageNumber: string, itemsPerPage: string) => {
        try {
            const {
                _embedded: { page },
                page: { totalPages },
            } = await PageService.getPagesList({ page: pageNumber, size: itemsPerPage });
            setTotalPages(() => totalPages);
            setPagesList(() => page);
        } catch (e) {
            console.error(e);
        }
    };

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
        [router.locale]
    );

    const table = useReactTable({
        data: rowData,
        columns,
        state: {
            rowSelection: selectedContents,
        },
        enableRowSelection: true,
        onRowSelectionChange: setSelectedContents,
        getCoreRowModel: getCoreRowModel(),
        getPaginationRowModel: getPaginationRowModel(),
        getSortedRowModel: getSortedRowModel(),
        initialState: { pagination: { pageSize: +basicPagination.size } },
    });

    if (isLoading) {
        return <Loader />;
    }

    return (
        <MainContent titleId="pages.page.title" actions={headingActions}>
            <Table tableData={table} />
            <Pagination
                totalPages={totalPages}
                itemsPerPage={+basicPagination.size}
                listLength={pagesList.length}
                onPageChange={handlePageChange}
                table={table}
            />
        </MainContent>
    );
};
