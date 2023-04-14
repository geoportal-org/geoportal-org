import { Flex, Table, Tbody, Th, Thead, Tr, Td } from "@chakra-ui/react";
import { ChevronDownIcon, ChevronUpIcon, UpDownIcon } from "@chakra-ui/icons";
import { flexRender } from "@tanstack/react-table";
import { firstTableCellBorderStyles, lastTableCellBorderStyles } from "@/theme/commons";
import { TableProps } from "@/types";

export const TableBody = <T extends object>({ tableData }: TableProps<T>) => {
    return (
        <Table size="sm" variant="geossTable">
            <Thead>
                {tableData.getHeaderGroups().map((headerGroup) => (
                    <Tr key={headerGroup.id}>
                        {headerGroup.headers.map((header) => (
                            <Th key={header.id}>
                                {header.isPlaceholder ? null : (
                                    <Flex
                                        align="center"
                                        justify="space-between"
                                        gap={1}
                                        cursor={header.column.getCanSort() ? "pointer" : "auto"}
                                        onClick={header.column.getToggleSortingHandler()}
                                    >
                                        {flexRender(header.column.columnDef.header, header.getContext())}
                                        {{
                                            asc: <ChevronUpIcon />,
                                            desc: <ChevronDownIcon />,
                                        }[header.column.getIsSorted() as string] ?? null}
                                        {header.column.getCanSort() && !header.column.getIsSorted() && (
                                            <UpDownIcon boxSize={2.5} />
                                        )}
                                    </Flex>
                                )}
                            </Th>
                        ))}
                    </Tr>
                ))}
            </Thead>
            <Tbody>
                {tableData.getRowModel().rows.map((row) => {
                    const isSelectedRow = row.getIsSelected();
                    return (
                        <Tr key={row.id}>
                            {row.getVisibleCells().map((cell) => (
                                <Td
                                    key={cell.id}
                                    borderColor={isSelectedRow ? "brand.dark" : "transparent"}
                                    _first={{
                                        ...firstTableCellBorderStyles,
                                        borderColor: isSelectedRow ? "brand.dark" : "transparent",
                                    }}
                                    _last={{
                                        ...lastTableCellBorderStyles,
                                        borderColor: isSelectedRow ? "brand.dark" : "transparent",
                                    }}
                                >
                                    {flexRender(cell.column.columnDef.cell, cell.getContext())}
                                </Td>
                            ))}
                        </Tr>
                    );
                })}
            </Tbody>
        </Table>
    );
};
