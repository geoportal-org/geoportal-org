import { useRouter } from "next/router";
import { useEffect, useMemo, useState } from "react";
import { FormikHelpers, FormikValues } from "formik";
import {
    ColumnDef,
    createColumnHelper,
    getCoreRowModel,
    PaginationState,
    SortingState,
    useReactTable,
} from "@tanstack/react-table";
import { useDisclosure } from "@chakra-ui/react";
import { Loader, MainContent, SideBar, Table, TableActions, TablePagination, TextInfo } from "@/components";
import { DefaultLayerSettingsManage } from "./DefaultLayerSettingsManage";
import { DefaultLayerService } from "@/services/api";
import { DefaultLayerContext } from "@/context";
import { convertIsoDate, setTableSorting } from "@/utils/helpers";
import useCustomToast from "@/utils/useCustomToast";
import useFormatMsg from "@/utils/useFormatMsg";
import { TableActionsSource, ToastStatus } from "@/types";
import { ILayer, ILayerData } from "@/types/models";
import { initPagination } from "@/data";

export const DefaultLayerSettings = () => {
    const [isLoading, setIsLoading] = useState(true);
    const [layersList, setLayersList] = useState<ILayer[]>([]);
    const [isPageChange, setIsPageChange] = useState(false);
    const [editedLayerId, setEditedLayerId] = useState<number | null>(null);
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
    const columnHelper = createColumnHelper<ILayer>();
    const router = useRouter();
    const { translate } = useFormatMsg();
    const { showToast } = useCustomToast();

    useEffect(() => {
        handlePaginationParamsChange();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [pageIndex, pageSize, sorting]);

    const handlePaginationParamsChange = async () => {
        try {
            setIsPageChange(true);
            const {
                _embedded: { layers },
                page: { totalPages, totalElements },
            } = await DefaultLayerService.getLayersList({
                page: table.getState().pagination.pageIndex,
                size: table.getState().pagination.pageSize,
                ...(sorting[0] && setTableSorting(sorting)),
            });
            setLayersList(() => layers);
            setDataInfo(() => ({ totalPages, totalElements }));
        } catch (e) {
            console.error(e);
        } finally {
            setIsLoading(false);
            setIsPageChange(false);
        }
    };

    const onAddAction = () => {
        setEditedLayerId(null);
        onSideBarOpen();
    };

    const onLayerEditAction = (id: number) => {
        setEditedLayerId(id);
        onSideBarOpen();
    };

    const addNewLayer = async (values: FormikValues, actions: FormikHelpers<FormikValues>) => {
        const layerData = getLayerData(values);
        try {
            const { name } = await DefaultLayerService.createLayer(layerData);
            handlePaginationParamsChange();
            actions.resetForm();
            showToast({
                title: translate("general.created"),
                description: translate("pages.layer.layer-added", { title: name }),
            });
        } catch (e) {
            const err = e as { errorInfo: any; errorStatus: number };
            const { errorStatus, errorInfo } = err;
            console.log(errorInfo);
            console.log(errorStatus);
            showErrorInfo(errorStatus && errorStatus === 409 ? "not-unique-layer-name" : "new-layer");
        }
    };

    const updateLayer = async (values: FormikValues, id: number, updateFormState: (values: FormikValues) => void) => {
        const layerData = getLayerData(values);
        try {
            const updatedLayer = await DefaultLayerService.updateLayer(id, layerData);
            setLayersList((prevLayerList) => prevLayerList.map((layer) => (layer.id === id ? updatedLayer : layer)));
            updateFormState(values);
            showToast({
                title: translate("general.updated"),
                description: translate("pages.layer.layer-updated", { title: updatedLayer.name }),
            });
        } catch (e) {
            const err = e as { errorInfo: any; errorStatus: number };
            const { errorStatus, errorInfo } = err;
            console.log(errorInfo);
            console.log(errorStatus);
            showErrorInfo(errorStatus && errorStatus === 409 ? "not-unique-layer-name-update" : "updated-layer");
        }
    };

    const getLayerData = (values: FormikValues): ILayerData => {
        const { name, url, visible } = values;
        const isVisible = visible === "true";
        return { name, url, visible: isVisible };
    };

    const showErrorInfo = (msgId: string) =>
        showToast({
            title: translate("general.error"),
            description: translate(`information.error.${msgId}`),
            status: ToastStatus.ERROR,
        });

    const pagination = useMemo(
        () => ({
            pageIndex,
            pageSize,
        }),
        [pageIndex, pageSize]
    );

    const rowData = useMemo(() => layersList, [layersList]);

    const columns = useMemo<ColumnDef<ILayer, any>[]>(
        () => [
            columnHelper.accessor("id", {
                header: "ID",
                enableSorting: false,
            }),
            columnHelper.accessor("name", {
                header: translate("pages.layer.name"),
            }),
            columnHelper.accessor("url", {
                header: translate("general.url"),
            }),
            columnHelper.accessor("created_on", {
                header: translate("general.creation-date"),
                cell: (info) => (info.getValue() ? convertIsoDate(info.getValue(), router.locale || "en") : "-"),
            }),
            columnHelper.accessor("modified_on", {
                header: translate("general.modification-date"),
                cell: (info) => (info.getValue() ? convertIsoDate(info.getValue(), router.locale || "en") : "-"),
            }),
            columnHelper.accessor("created_by", {
                header: translate("general.author"),
            }),
            columnHelper.accessor("visible", {
                header: translate("pages.layer.visible"),
                cell: (info) => translate(`general.${info.getValue() ? "yes" : "no"}`),
            }),
            columnHelper.display({
                header: translate("general.actions"),
                id: "actions",
                cell: (info) => (
                    <TableActions
                        itemId={info.row.getValue("id")}
                        item={info.row.original}
                        actionsSource={TableActionsSource.LAYER}
                        onDeleteAction={handlePaginationParamsChange}
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
        getCoreRowModel: getCoreRowModel(),
        manualPagination: true,
        manualSorting: true,
        onPaginationChange: setPagination,
        onSortingChange: setSorting,
        pageCount: totalPages,
        state: {
            pagination,
            sorting,
        },
    });

    const headingActions = [
        {
            titleId: "pages.layer.add",
            onClick: () => onAddAction(),
        },
    ];

    if (isLoading) {
        return <Loader />;
    }

    return (
        <DefaultLayerContext.Provider value={{ editedLayerId, addNewLayer, onLayerEditAction, updateLayer }}>
            <MainContent titleId="nav.settings.section.layer" actions={headingActions}>
                {totalElements ? (
                    <>
                        <Table tableData={table} />
                        <TablePagination tableData={table} isPageChange={isPageChange} />
                    </>
                ) : (
                    <TextInfo id="information.info.no-layers" />
                )}
            </MainContent>

            <SideBar
                isOpen={isSideBarOpen}
                onClose={onSideBarClose}
                titleId={`pages.layer.${Number.isInteger(editedLayerId) ? "edit" : "add"}`}
                content={<DefaultLayerSettingsManage />}
            />
        </DefaultLayerContext.Provider>
    );
};
