import { TableContainer } from "@chakra-ui/react";
import { TableBody } from "./TableBody";
import { scrollbarStyles } from "@/theme/commons";
import { TableProps } from "@/types";

export const Table = <T extends object>({ tableData }: TableProps<T>) => (
    <TableContainer sx={scrollbarStyles} flexShrink="0">
        <TableBody tableData={tableData} />
    </TableContainer>
);
