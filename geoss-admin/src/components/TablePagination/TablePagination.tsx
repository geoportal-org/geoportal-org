import { useEffect } from "react";
import { Button, Flex, Menu, MenuButton, MenuItem, MenuList, Spinner, Text } from "@chakra-ui/react";
import { ChevronDownIcon, ChevronLeftIcon, ChevronRightIcon } from "@chakra-ui/icons";
import { TextContent } from "@/components";
import { itemsPerPageOptions } from "@/data";
import { TablePaginationProps } from "@/types";

export const TablePagination = <T extends object>({ tableData, isPageChange }: TablePaginationProps<T>) => {
    const { pageIndex, pageSize } = tableData.getState().pagination;
    const {
        previousPage,
        getCanPreviousPage,
        nextPage,
        getCanNextPage,
        getPageCount,
        setPageIndex,
        setPageSize,
        getRowModel,
    } = tableData;
    const pages = new Array(getPageCount()).fill(0).map((_, index) => index + 1);
    const rows = getRowModel().rows;
    const rowsLength = getRowModel().rows.length;

    useEffect(() => {
        checkLastPage();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [rows]);

    const checkLastPage = () => {
        if (rowsLength === 0 && pageIndex !== 0) {
            previousPage();
        }
    };

    return (
        <Flex align="center" justify="flex-end" my={1}>
            {isPageChange && (
                <Spinner size="sm" emptyColor="brand.darkSoft" speed="0.9s" color="brand.dark" thickness="2px" mr={1} />
            )}
            <Flex align="center" gap={1}>
                <Button
                    size="xs"
                    variant="geossPagination"
                    onClick={() => previousPage()}
                    isDisabled={!getCanPreviousPage() || isPageChange}
                >
                    <ChevronLeftIcon color="currentColor" boxSize={4} />
                </Button>
                {getPageCount() !== 1 ? (
                    <Flex align="center" gap={1}>
                        <Text as="span">
                            <TextContent id="pagination.page" />{" "}
                        </Text>
                        <Menu variant="geossLangSwitcher">
                            {({ isOpen }) => (
                                <>
                                    <MenuButton disabled={isPageChange}>
                                        {pageIndex + 1}
                                        <ChevronDownIcon
                                            transform={isOpen ? "rotate(-180deg)" : "rotate(0)"}
                                            transitionDuration="normal"
                                        />
                                    </MenuButton>
                                    <MenuList>
                                        {pages.map((page) => {
                                            const isActive = pageIndex + 1 === page;

                                            return (
                                                <MenuItem
                                                    key={page}
                                                    borderStartColor={isActive ? "brand.dark" : "transparent"}
                                                    bg={isActive ? "brand.darkSoft" : "brand.background"}
                                                    onClick={() => (!isActive ? setPageIndex(page - 1) : null)}
                                                >
                                                    {page}
                                                </MenuItem>
                                            );
                                        })}
                                    </MenuList>
                                </>
                            )}
                        </Menu>
                        <Text as="span">/ {pages.length}</Text>
                    </Flex>
                ) : (
                    <Text>
                        <TextContent id="pagination.page" /> {pageIndex + 1} / {getPageCount()}
                    </Text>
                )}
                <Button
                    size="xs"
                    variant="geossPagination"
                    onClick={() => nextPage()}
                    isDisabled={!getCanNextPage() || isPageChange}
                >
                    <ChevronRightIcon color="currentColor" boxSize={4} />
                </Button>
            </Flex>
            <Flex align="center">
                <Menu variant="geossLangSwitcher">
                    {({ isOpen }) => (
                        <>
                            <MenuButton disabled={isPageChange}>
                                <TextContent id="pagination.show" /> {pageSize}
                                <ChevronDownIcon
                                    transform={isOpen ? "rotate(-180deg)" : "rotate(0)"}
                                    transitionDuration="normal"
                                />
                            </MenuButton>
                            <MenuList>
                                {itemsPerPageOptions.map((itemsSize) => {
                                    const isActive = pageSize === itemsSize;

                                    return (
                                        <MenuItem
                                            key={itemsSize}
                                            borderStartColor={isActive ? "brand.dark" : "transparent"}
                                            bg={isActive ? "brand.darkSoft" : "brand.background"}
                                            onClick={() => (!isActive ? setPageSize(itemsSize) : null)}
                                        >
                                            {itemsSize}
                                        </MenuItem>
                                    );
                                })}
                            </MenuList>
                        </>
                    )}
                </Menu>
            </Flex>
        </Flex>
    );
};
