import { useRouter } from "next/router";
import { useEffect, useMemo, useState } from "react";
import {
    ColumnDef,
    createColumnHelper,
    useReactTable,
    getCoreRowModel,
    getPaginationRowModel,
    getSortedRowModel,
} from "@tanstack/react-table";
import { Checkbox } from "@chakra-ui/react";
import { Table, MainContent, TableActions, Loader, Pagination } from "@/components";
import { ContentService } from "@/services/api";
import useFormatMsg from "@/utils/useFormatMsg";
import { convertIsoDate, getIdFromUrl } from "@/utils/helpers";
import { IContent } from "@/types/models";
import { ButtonVariant, TableActionsSource } from "@/types";
import { initPagination, pagesRoutes } from "@/data";

const basicPagination = initPagination;

export const Contents = () => {
    const [contentsList, setContentsList] = useState<IContent[]>([]);
    const [selectedContents, setSelectedContents] = useState<Record<string, boolean>>({});
    const [isLoading, setIsLoading] = useState(true);
    const [totalPages, setTotalPages] = useState<number>(1);
    const { translate } = useFormatMsg();
    const columnHelper = createColumnHelper<IContent>();
    const router = useRouter();

    useEffect(() => {
        const getContentsList = async () => {
            try {
                const {
                    _embedded: { content },
                    page: { totalPages },
                } = await ContentService.getContentList(basicPagination);
                setContentsList(() => content);
                setTotalPages(() => totalPages);
            } catch (e) {
                console.error(e);
            } finally {
                setIsLoading(false);
            }
        };
        getContentsList();
    }, []);

    const navigateToAddContent = () => router.push(pagesRoutes.addContent);

    const deleteAction = () => {
        console.log(selectedContents);
        console.log(table.getFilteredSelectedRowModel());
    };

    const deleteContent = (itemId: string) =>
        setContentsList((contentsList) =>
            contentsList.filter((content) => getIdFromUrl(content._links.self.href) !== itemId)
        );

    const handlePageChange = async (pageNumber: string, itemsPerPage: string) => {
        try {
            const {
                _embedded: { content },
                page: { totalPages },
            } = await ContentService.getContentList({ page: pageNumber, size: itemsPerPage });
            setTotalPages(() => totalPages);
            setContentsList(() => content);
        } catch (e) {
            console.error(e);
        }
    };

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
            <Table tableData={table} />
            <Pagination
                totalPages={totalPages}
                itemsPerPage={+basicPagination.size}
                listLength={contentsList.length}
                onPageChange={handlePageChange}
                table={table}
            />
        </MainContent>
    );
};
