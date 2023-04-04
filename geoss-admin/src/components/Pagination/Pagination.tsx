import { useEffect, useState } from "react";
import { Button, Flex, Menu, MenuButton, MenuItem, MenuList, Text } from "@chakra-ui/react";
import { ChevronDownIcon, ChevronLeftIcon, ChevronRightIcon } from "@chakra-ui/icons";
import { TextContent } from "@/components";
import { PaginationProps } from "@/types";
import { itemsPerPageOptions } from "@/data";

export const Pagination = <T extends object>({
    totalPages,
    itemsPerPage,
    listLength,
    onPageChange,
    table,
}: PaginationProps<T>) => {
    const [currentPage, setCurrentPage] = useState<number>(1);
    const [pageSize, setPageSize] = useState<number>(itemsPerPage);
    const pages = new Array(totalPages).fill(0).map((_, index) => index + 1);

    useEffect(() => {
        checkCurrentPage();
    }, [totalPages]);

    /*useEffect(() => {
        checkEmptyPage();
    }, [listLength]);*/

    const checkCurrentPage = () => {
        if (totalPages && currentPage > totalPages) {
            goToLastPage();
        }
    };

    const checkEmptyPage = () => {
        if (listLength === 0 && currentPage > 1) {
            handlePageChange("prev");
        }
    };

    const goToLastPage = () => {
        totalPages && setCurrentPage(totalPages);
        onPageChange((totalPages - 1).toString(), pageSize.toString());
    };

    const handlePageChange = (direction: "prev" | "next") => {
        onPageChange(direction === "prev" ? (currentPage - 2).toString() : currentPage.toString(), pageSize.toString());
        setCurrentPage((currentPage) => (direction === "prev" ? currentPage - 1 : currentPage + 1));
    };

    const goToPage = (page: number) => {
        onPageChange((page - 1).toString(), pageSize.toString());
        setCurrentPage(page);
    };

    const handleItemsPerPageChange = (itemsSize: number) => {
        onPageChange((currentPage - 1).toString(), itemsSize.toString());
        setPageSize(itemsSize);
        if (table) {
            table.setPageSize(itemsSize);
        }
    };

    return (
        <Flex align="center" justify="flex-end" my={1}>
            <Flex align="center" gap={1}>
                <Button
                    size="xs"
                    variant="geossPagination"
                    onClick={() => handlePageChange("prev")}
                    isDisabled={currentPage === 1}
                >
                    <ChevronLeftIcon color="currentColor" boxSize={4} />
                </Button>
                {totalPages !== 1 ? (
                    <Flex align="center" gap={1}>
                        <Text as="span">
                            <TextContent id="pagination.page" />{" "}
                        </Text>
                        <Menu variant="geossLangSwitcher">
                            {({ isOpen }) => (
                                <>
                                    <MenuButton>
                                        {currentPage}
                                        <ChevronDownIcon
                                            transform={isOpen ? "rotate(-180deg)" : "rotate(0)"}
                                            transitionDuration="normal"
                                        />
                                    </MenuButton>
                                    <MenuList>
                                        {pages.map((page) => {
                                            const isActive = currentPage === page;

                                            return (
                                                <MenuItem
                                                    key={page}
                                                    borderStartColor={isActive ? "brand.dark" : "transparent"}
                                                    bg={isActive ? "brand.darkSoft" : "brand.background"}
                                                    onClick={() => (!isActive ? goToPage(page) : null)}
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
                        <TextContent id="pagination.page" /> {currentPage} / {totalPages}
                    </Text>
                )}
                <Button
                    size="xs"
                    variant="geossPagination"
                    onClick={() => handlePageChange("next")}
                    isDisabled={currentPage === totalPages}
                >
                    <ChevronRightIcon color="currentColor" boxSize={4} />
                </Button>
            </Flex>
            <Flex align="center">
                <Menu variant="geossLangSwitcher">
                    {({ isOpen }) => (
                        <>
                            <MenuButton>
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
                                            onClick={() => (!isActive ? handleItemsPerPageChange(itemsSize) : null)}
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
