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
import { TutorialTagsSettingsManage } from "./TutorialTagsSettingsManage";
import { TutorialTagsService } from "@/services/api";
import { TutorialTagsContext } from "@/context";
import { cutString, setTableSorting } from "@/utils/helpers";
import useCustomToast from "@/utils/useCustomToast";
import useFormatMsg from "@/utils/useFormatMsg";
import { TableActionsSource, ToastStatus } from "@/types";
import { ITutorialTag, ITutorialTagData } from "@/types/models";
import { initPagination } from "@/data";

export const TutorialTagsSettings = () => {
    const [isLoading, setIsLoading] = useState(true);
    const [tagsList, setTagsList] = useState<ITutorialTag[]>([]);
    const [isPageChange, setIsPageChange] = useState(false);
    const [editedTagId, setEditedTagId] = useState<number | null>(null);
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
    const columnHelper = createColumnHelper<ITutorialTag>();
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
                _embedded: { tags },
                page: { totalPages, totalElements },
            } = await TutorialTagsService.getTagList({
                page: table.getState().pagination.pageIndex,
                size: table.getState().pagination.pageSize,
                ...(sorting[0] && setTableSorting(sorting)),
            });
            setTagsList(() => tags);
            setDataInfo(() => ({ totalPages, totalElements }));
        } catch (e) {
            console.log(e);
        } finally {
            setIsLoading(false);
            setIsPageChange(false);
        }
    };

    const onAddAction = () => {
        setEditedTagId(null);
        onSideBarOpen();
    };

    const onTagEditAction = (id: number) => {
        setEditedTagId(id);
        onSideBarOpen();
    };

    const addNewTag = async (values: FormikValues, actions: FormikHelpers<FormikValues>) => {
        const tagData = getTagData(values);
        try {
            const {
                title: { en: tagTitle },
            } = await TutorialTagsService.createTag(tagData);
            handlePaginationParamsChange();
            actions.resetForm();
            showToast({
                title: translate("general.created"),
                description: translate("pages.tags.tag-added", { title: tagTitle }),
            });
        } catch (e) {
            const err = e as { errorInfo: any; errorStatus: number };
            const { errorStatus, errorInfo } = err;
            console.log(errorInfo);
            console.log(errorStatus);
            showErrorInfo(errorStatus && errorStatus === 409 ? "not-unique-tag-id" : "new-tag");
        }
    };

    const updateTag = async (values: FormikValues, id: number, updateFormState: (values: FormikValues) => void) => {
        const tagData = getTagData(values);
        try {
            await TutorialTagsService.updateTag(id, tagData);
            const updatedTag = await TutorialTagsService.getTag(id);
            setTagsList((prevTagsList) => prevTagsList.map((tag) => (tag.id === id ? updatedTag : tag)));
            updateFormState(values);
            showToast({
                title: translate("general.updated"),
                description: translate("pages.tags.tag-updated", { title: updatedTag.title }),
            });
        } catch (e) {
            const err = e as { errorInfo: any; errorStatus: number };
            const { errorStatus, errorInfo } = err;
            console.log(errorInfo);
            console.log(errorStatus);
            showErrorInfo(errorStatus && errorStatus === 409 ? "not-unique-tag-id-update" : "updated-tag");
        }
    };

    const getTagData = (values: FormikValues): ITutorialTagData => {
        const { name, show, type, placement, title, description } = values;
        const isVisible = show === "true";
        return {
            name,
            show: isVisible,
            type,
            placement,
            title: { ru: title, en: title, fr: title, pl: title, es: title, zh: title },
            description: {
                ru: description,
                en: description,
                fr: description,
                pl: description,
                es: description,
                zh: description,
            },
        };
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

    const rowData = useMemo(() => tagsList, [tagsList]);

    const columns = useMemo<ColumnDef<ITutorialTag, any>[]>(
        () => [
            columnHelper.accessor("name", {
                header: translate("general.id"),
                cell: (info) => cutString(info.getValue(), 20),
            }),
            columnHelper.accessor("title", {
                header: translate("general.title"),
                cell: (info) => cutString(info.getValue(), 20),
            }),
            columnHelper.accessor("description", {
                header: translate("general.description"),
                cell: (info) => cutString(info.getValue(), 35),
            }),
            columnHelper.accessor("type", {
                header: translate("pages.tags.type"),
            }),
            columnHelper.accessor("show", {
                header: translate("pages.tags.show"),
                cell: (info) => translate(`general.${info.getValue() ? "yes" : "no"}`).toLowerCase(),
            }),
            columnHelper.accessor("placement", {
                header: translate("pages.tags.placement"),
            }),
            columnHelper.display({
                header: translate("general.actions"),
                id: "actions",
                cell: (info) => (
                    <TableActions
                        itemId={info.row.original.id}
                        item={info.row.original}
                        actionsSource={TableActionsSource.TUTORIAL}
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
            titleId: "pages.tags.add",
            onClick: () => onAddAction(),
        },
    ];

    if (isLoading) {
        return <Loader />;
    }

    return (
        <TutorialTagsContext.Provider value={{ editedTagId, addNewTag, onTagEditAction, updateTag }}>
            <MainContent titleId="nav.settings.section.tutorial" actions={headingActions}>
                {totalElements ? (
                    <>
                        <Table tableData={table} />
                        <TablePagination tableData={table} isPageChange={isPageChange} />
                    </>
                ) : (
                    <TextInfo id="information.info.no-tags" />
                )}
            </MainContent>

            <SideBar
                isOpen={isSideBarOpen}
                onClose={onSideBarClose}
                titleId={`pages.tags.${Number.isInteger(editedTagId) ? "edit" : "add"}`}
                content={<TutorialTagsSettingsManage />}
            />
        </TutorialTagsContext.Provider>
    );
};
