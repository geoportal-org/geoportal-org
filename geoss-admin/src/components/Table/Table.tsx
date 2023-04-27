import { TableContainer } from "@chakra-ui/react";
import { TableBody } from "./TableBody";
import { scrollbarStyles } from "@/theme/commons";
import { TableProps } from "@/types";

export const Table = <T extends object>({ tableData, isDisabled }: TableProps<T>) => (
    <TableContainer sx={scrollbarStyles} flexShrink="0">
        <TableBody tableData={tableData} isDisabled={isDisabled} />
    </TableContainer>
);
