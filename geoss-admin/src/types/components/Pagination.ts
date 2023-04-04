import { Table } from "@tanstack/react-table";

export type PaginationProps<T> = {
    totalPages: number;
    itemsPerPage: number;
    listLength: number;
    onPageChange: (page: string, itemsPerPage: string) => void;
    table?: Table<T>;
};
