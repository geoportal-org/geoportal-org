import { FormikValues } from "formik";
import { NodeModel } from "@minoru/react-dnd-treeview";
import {
    BreadcrumbItem,
    ButtonVariant,
    FileRepositoryTreeItem,
    FormField,
    ModalAction,
    NestedMsgs,
    SelectSettings,
} from "@/types";
import { IContent, IDocument, IFolder, IMenuItem } from "@/types/models";
import { navSectionsUrls } from "@/data";
import { SortingState } from "@tanstack/react-table";

export const getActiveNavSection = (activeRoute: string): number => {
    const activeSectionIndex = navSectionsUrls.findIndex((section) => section.includes(activeRoute));
    return activeSectionIndex !== -1 ? activeSectionIndex : 0;
};

export const flattenMessages = (nestedMessages: NestedMsgs, prefix = ""): { [key: string]: string } => {
    if (nestedMessages === null) {
        return {};
    }
    return Object.keys(nestedMessages).reduce((messages, key) => {
        const value = nestedMessages[key];
        const prefixedKey = prefix ? `${prefix}.${key}` : key;

        if (typeof value === "string") {
            Object.assign(messages, { [prefixedKey]: value });
        } else {
            Object.assign(messages, flattenMessages(value, prefixedKey));
        }

        return messages;
    }, {});
};

export const convertIsoDate = (isoStringDate: string, locale: string): string => {
    const date = new Date(isoStringDate);
    return date.toLocaleDateString(locale, {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
    });
};

export const getIdFromUrl = (url: string): string => url.split("/").reverse()[0];

export const cutString = (text: string, maxChars: number): string =>
    text.length > maxChars ? `${text.slice(0, maxChars)}...` : text;

export const getFileInformation = (file: IDocument, locale: string): { labelId: string; value: string }[] => {
    const { _links, modifiedBy, folderId, path, ...fileInfo } = file;
    return Object.keys(fileInfo).map((key) => ({
        labelId: `pages.file-repository.file-${key}`,
        value: !key.toLowerCase().includes("date")
            ? fileInfo[key as keyof typeof fileInfo]
            : convertIsoDate(fileInfo[key as keyof typeof fileInfo], locale),
    }));
};

export const setDecisionModalActions = (confirmAction: () => void, cancelAction: () => void): ModalAction[] => [
    {
        btnVariant: ButtonVariant.GHOST,
        color: "brand.cancel",
        label: "general.cancel",
        onClick: cancelAction,
    },
    {
        btnVariant: ButtonVariant.OUTLINE,
        color: "brand.accept",
        label: "general.confirm",
        onClick: confirmAction,
    },
];

export const setFormInitialValues = (formFields: FormField[]): FormikValues => {
    const initialValues: FormikValues = {};
    formFields.forEach((field) => (initialValues[field.name] = ""));
    return initialValues;
};

export const setExistingFormValues = (formFields: FormField[], values: FormikValues): FormikValues => {
    const existingValues: FormikValues = {};
    formFields.forEach((field) => (existingValues[field.name] = values[field.name].toString()));
    return existingValues;
};

export const areObjectsArraysEqual = <T>(arrayOne: T[], arrayTwo: T[]): boolean =>
    arrayOne.length === arrayTwo.length && arrayOne.every((obj, index) => areObjectsEqual(obj, arrayTwo[index]));

export const areObjectsEqual = (objOne: any, objTwo: any): boolean =>
    typeof objOne === "object" && Object.keys(objOne).length > 0
        ? Object.keys(objOne).length === Object.keys(objTwo).length &&
          Object.keys(objOne).every((key) => areObjectsEqual(objOne[key], objTwo[key]))
        : objOne === objTwo;

export const generatePath = (breadcrumb: BreadcrumbItem[]): string =>
    breadcrumb.map((breadcrumb) => breadcrumb.folderId).join("/");

export const createSelectContentsList = (contents: IContent[]): SelectSettings => {
    const options = contents.map((content) => {
        const contentId = getIdFromUrl(content._links.self.href);
        return {
            label: `${content.title} (ID: ${contentId})`,
            value: contentId,
        };
    });
    return {
        isMultiselect: false,
        options,
    };
};

export const createSlug = (value: string): string =>
    value.trim().toLowerCase().replace(/\s\s+/g, " ").replaceAll(" ", "-");

export const createRelativeUrl = (value: string): string => `/${createSlug(value)}`;

export const createTouchedForm = (formFields: FormField[]): { [index: string]: boolean } => {
    const touchedForm: { [index: string]: boolean } = {};
    formFields.forEach((field) => (touchedForm[field.name] = true));
    return touchedForm;
};

export const sortMenuList = (menuList: IMenuItem[]): NodeModel<IMenuItem>[] => {
    const orderedMenu: (IMenuItem & { subitems?: IMenuItem[] })[] = [];
    menuList.forEach((menuItem) => menuItem.levelId === 0 && orderedMenu.push({ ...menuItem, subitems: [] }));
    orderedMenu.sort((a, b) => a.priority - b.priority);
    orderedMenu.forEach((menuItem) => {
        const itemId = getIdFromUrl(menuItem._links.self.href);
        menuList.forEach(
            (item) =>
                item.levelId === 1 && item.parentMenuId === +itemId && menuItem.subitems && menuItem.subitems.push(item)
        );
    });
    orderedMenu.forEach((menuItem) => menuItem.subitems && menuItem.subitems.sort((a, b) => a.priority - b.priority));
    const preparedMenu: IMenuItem[] = [];
    orderedMenu.forEach((menuItem) => {
        const subitems = menuItem.subitems;
        delete menuItem.subitems;
        preparedMenu.push(menuItem);
        if (subitems && subitems.length) {
            subitems.forEach((item) => preparedMenu.push(item));
        }
    });
    return preparedMenu.map((menuItem) => ({
        id: +getIdFromUrl(menuItem._links.self.href),
        parent: menuItem.parentMenuId,
        text: menuItem.title,
        droppable: menuItem.parentMenuId === 0,
        data: menuItem,
    }));
};

export const setTableSorting = (sortingInfo: SortingState): { sort: string } => {
    const { id: sortingProperty, desc } = sortingInfo[0];
    const sortingDirection = desc ? "desc" : "asc";
    return {
        sort: `${sortingProperty},${sortingDirection}`,
    };
};

// create folders & documents structure ready to create directory tree
export const createFileTree = (folders: IFolder[], documents: IDocument[]): FileRepositoryTreeItem[] => {
    const fileTree: FileRepositoryTreeItem[] = [
        {
            name: "Home",
            id: 0,
            items: [],
        },
    ];

    folders
        .sort((a, b) => a.parentFolderId - b.parentFolderId)
        .forEach((folder) => assignFileTreeItem(folder, fileTree));
    documents.sort((a, b) => a.folderId - b.folderId).forEach((document) => assignFileTreeItem(document, fileTree));

    return fileTree;
};

const assignFileTreeItem = (newItem: IFolder | IDocument, fileTreeItem: FileRepositoryTreeItem[]) => {
    const isFolderType = "parentFolderId" in newItem;
    const item: FileRepositoryTreeItem = {
        name: newItem.title,
        id: +getIdFromUrl(newItem._links.self.href),
        ...(isFolderType && { items: [] }),
    };

    fileTreeItem.forEach((treeItem) => {
        if (treeItem.items) {
            return (isFolderType && treeItem.id === newItem.parentFolderId) ||
                (!isFolderType && treeItem.id === newItem.folderId)
                ? treeItem.items.push(item)
                : assignFileTreeItem(newItem, treeItem.items);
        }
    });
};
