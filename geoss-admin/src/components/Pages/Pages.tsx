import { useRouter } from "next/router";
import { useMemo, useState, useEffect, useContext } from "react";
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
import { Loader, MainContent, Modal, Table, TableActions, TablePagination, TextContent, TextInfo } from "@/components";
import { PageService } from "@/services/api";
import { initPagination, pagesRoutes } from "@/data";
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
import { ButtonVariant, LocaleNames, MainContentAction, TableActionsSource, ToastStatus } from "@/types";
import { IPage } from "@/types/models";
import { SiteContext, SiteContextValue } from "@/context/CurrentSiteContext";

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
    const { isOpen, onOpen, onClose } = useDisclosure();
    const { translate } = useFormatMsg();
    const { showToast } = useCustomToast();
    const columnHelper = createColumnHelper<IPage>();
    const router = useRouter();
    const decisionModalActions = setDecisionModalActions(
        async () => await deleteSelectedPages(),
        () => onClose()
    );

    //siteId
    const { currentSiteId } = useContext<SiteContextValue>(SiteContext);

    useEffect(() => {
        handlePaginationParamsChange();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [pageIndex, pageSize, sorting, currentSiteId]);

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

            //get pages for current site
            const sitePages = page.filter((pagePiece) => pagePiece.siteId === currentSiteId);

            setPagesList(() => sitePages);
            setDataInfo(() => ({ totalPages, totalElements }));
        } catch (e) {
            console.error(e);
        } finally {
            setIsPageChange(false);
            setIsLoading(false);
        }
    };

    const deleteSelectedPages = async () => {
        try {
            await PageService.deletePages({ ids: getSelectedTableItemsIds(table).join(",") });
            handlePaginationParamsChange();
            onClose();
            showToast({
                title: translate("general.deleted"),
                description: translate("pages.page.selected-deleted"),
            });
        } catch (e) {
            console.log(e);
            showToast({
                title: translate("general.error"),
                description: translate("information.error.loading"),
                status: ToastStatus.ERROR,
            });
        }
    };

    const navigateToAddPage = () => router.push(pagesRoutes.addPage);

    const rowData = useMemo(() => pagesList, [pagesList]);

    const columns = useMemo<ColumnDef<IPage, any>[]>(
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
                header: translate("pages.page.page-title"),
                cell: ({ getValue }) => cutString(getValue()[router.locale as LocaleNames], 30),
            }),
            columnHelper.accessor("createdOn", {
                header: translate("pages.page.creation-date"),
                cell: (info) => (info.getValue() ? convertIsoDate(info.getValue(), router.locale || "en") : "-"),
            }),
            columnHelper.accessor("modifiedOn", {
                header: translate("pages.page.modification-date"),
                cell: (info) => (info.getValue() ? convertIsoDate(info.getValue(), router.locale || "en") : "-"),
            }),
            columnHelper.accessor("createdBy", {
                header: translate("pages.page.author"),
                cell: ({ getValue }) => cutString(getValue(), 30),
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

    const headingActions: MainContentAction[] = [
        { titleId: "pages.page.add", onClick: navigateToAddPage, disabled: isPageChange || isLoading },
        {
            titleId: "pages.page.delete-selected",
            variant: ButtonVariant.GHOST,
            color: "brand.cancel",
            onClick: () => onOpen(),
            disabled: !Object.keys(selectedContents).length || isPageChange || isLoading,
        },
    ];

    return (
        <>
            <MainContent titleId="nav.contents.section.page" actions={headingActions}>
                {isLoading && <Loader />}
                {!!totalElements && !isLoading && (
                    <>
                        <Table tableData={table} isDisabled={isPageChange} />
                        <TablePagination tableData={table} isPageChange={isPageChange} />
                    </>
                )}
                {!totalElements && !isLoading && <TextInfo id="information.info.no-pages" />}
            </MainContent>

            <Modal
                modalHeader={translate("pages.page.delete-page-title")}
                modalBody={
                    <Text py={4}>
                        <TextContent
                            id={`pages.page.${
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
