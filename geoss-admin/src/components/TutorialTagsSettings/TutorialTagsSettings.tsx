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
import { cutString, isTranslatedValueAdded, setTableSorting } from "@/utils/helpers";
import useCustomToast from "@/utils/useCustomToast";
import useFormatMsg from "@/utils/useFormatMsg";
import { LocaleNames, TableActionsSource, ToastStatus } from "@/types";
import { ITutorialTag, ITutorialTagData } from "@/types/models";
import { defaultUsedLang, initPagination } from "@/data";
import { addTutorialTagForm } from "@/data/forms";

export const TutorialTagsSettings = () => {
    const [isLoading, setIsLoading] = useState(true);
    const [tagsList, setTagsList] = useState<ITutorialTag[]>([]);
    const [isPageChange, setIsPageChange] = useState(false);
    const [editedTag, setEditedTag] = useState<ITutorialTag | null>(null);
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
            setIsPageChange(false);
            setIsLoading(false);
        }
    };

    const onAddAction = () => {
        setEditedTag(null);
        onSideBarOpen();
    };

    const onTagEditAction = (tagData: ITutorialTag) => {
        setEditedTag(tagData);
        onSideBarOpen();
    };

    const addNewTag = async (
        values: FormikValues,
        actions: FormikHelpers<FormikValues>,
        currentTranslation: LocaleNames
    ) => {
        const tagData = getTagData(values, currentTranslation);
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
            showErrorInfo(errorStatus && errorStatus === 409 ? "not-unique-tag-id" : "new-tag");
        }
    };

    const updateTag = async (
        values: FormikValues,
        id: number,
        updateFormState: (values: FormikValues) => void,
        currentTranslation: LocaleNames
    ) => {
        const tagData = getTagData(values, currentTranslation);
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
            showErrorInfo(errorStatus && errorStatus === 409 ? "not-unique-tag-id-update" : "updated-tag");
        }
    };

    const getTagData = (values: FormikValues, currentTranslation: LocaleNames): ITutorialTagData => {
        addTutorialTagForm.forEach(({ translationInfo, type }) => {
            if (!translationInfo) {
                return;
            }
            const { genericName, translation } = translationInfo;
            const defaultTranslation = values[genericName][defaultUsedLang] ? defaultUsedLang : currentTranslation;
            if (isTranslatedValueAdded(translationInfo, type, values)) {
                return;
            }
            values[genericName][translation] = values[genericName][defaultTranslation];
        });

        const { name, show, type, placement, title, description } = values;
        const isVisible = show === "true";
        return {
            name,
            show: isVisible,
            type,
            placement,
            title,
            description,
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
                cell: (info) => cutString(info.getValue(), 30),
            }),
            columnHelper.accessor("title", {
                header: translate("general.title"),
                cell: (info) => cutString(info.getValue()[router.locale as LocaleNames], 30),
            }),
            columnHelper.accessor("description", {
                header: translate("general.description"),
                cell: (info) => cutString(info.getValue()[router.locale as LocaleNames], 35),
            }),
            columnHelper.accessor("type", {
                header: translate("pages.tags.type"),
                cell: (info) => translate(`pages.tags.${info.getValue()}`).toLowerCase(),
            }),
            columnHelper.accessor("show", {
                header: translate("pages.tags.show"),
                cell: (info) => translate(`general.${info.getValue() ? "yes" : "no"}`).toLowerCase(),
            }),
            columnHelper.accessor("placement", {
                header: translate("pages.tags.placement"),
                cell: (info) => translate(`pages.tags.${info.getValue()}`),
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
            disabled: isPageChange || isLoading,
        },
    ];

    return (
        <TutorialTagsContext.Provider value={{ editedTag, addNewTag, onTagEditAction, updateTag }}>
            <MainContent titleId="nav.settings.section.tutorial" actions={headingActions}>
                {isLoading && <Loader />}
                {!!totalElements && !isLoading && (
                    <>
                        <Table tableData={table} isDisabled={isPageChange} />
                        <TablePagination tableData={table} isPageChange={isPageChange} />
                    </>
                )}
                {!isLoading && !totalElements && <TextInfo id="information.info.no-tags" />}
            </MainContent>

            <SideBar
                isOpen={isSideBarOpen}
                onClose={onSideBarClose}
                titleId={`pages.tags.${editedTag ? "edit" : "add"}`}
                content={<TutorialTagsSettingsManage />}
            />
        </TutorialTagsContext.Provider>
    );
};
