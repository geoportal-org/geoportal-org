import { Table } from "@tanstack/react-table";

export type TablePaginationProps<T> = {
    tableData: Table<T>;
    isPageChange: boolean;
};
