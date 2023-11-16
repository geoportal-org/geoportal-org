import { PagesInfo } from "@/types/models/recommendations";
import useFormatMsg from "@/utils/useFormatMsg";
import { ArrowLeftIcon, ArrowRightIcon } from "@chakra-ui/icons";
import { Button, Flex, Select, Text, useBreakpointValue } from "@chakra-ui/react";
import React from "react";

interface Props {
    pagesInfo: PagesInfo;
    numberOfElements: number;
    fetchFunction: (number: number) => {};
    isZeroFirst: boolean;
}

const PagesControls = (props: Props) => {
    const { translate } = useFormatMsg();
    const optionList = [];
    for (let i = 0; i < props.pagesInfo?.totalPages; i++) {
        optionList.push(i + 1);
    }
    const handleChange = (e: any) => {
        props.fetchFunction(e.target.value - 1);
    };

    return (
        <Flex
            direction={useBreakpointValue({ base: "column", md: "row" })}
            alignItems="center"
            justifyContent="flex-end"
            gap="3"
        >
            <Flex alignItems="center" gap="3">
                <Text color="gray.500">
                    {translate("pages.recommendations.showing")}
                    {": "}
                    {props.numberOfElements} {translate("pages.recommendations.of")} {props.pagesInfo.totalElements}{" "}
                    {translate("pages.recommendations.results")}
                </Text>
                <Select
                    disabled={optionList.length > 0 ? false : true}
                    w="100px"
                    value={props.isZeroFirst ? props.pagesInfo.number + 1 : props.pagesInfo.number}
                    onChange={handleChange}
                >
                    {optionList.map((e: number) => {
                        return (
                            <option key={e} value={e}>
                                {translate("pages.recommendations.page")} {e}
                            </option>
                        );
                    })}
                </Select>
            </Flex>
            <Flex gap={useBreakpointValue({ base: "0", md: "3" })}>
                <Button
                    onClick={() => props.fetchFunction(0)}
                    isDisabled={props.pagesInfo.number === (props.isZeroFirst ? 0 : 1) ? true : false}
                    maxW="100px"
                    borderRadius="10"
                    variant="geossOutline"
                    color="blue.300"
                    leftIcon={<ArrowLeftIcon />}
                >
                    {translate("pages.recommendations.first")}
                </Button>
                <Button
                    onClick={() => props.fetchFunction(props.pagesInfo.number - 1)}
                    isDisabled={props.pagesInfo.number === (props.isZeroFirst ? 0 : 1) ? true : false}
                    maxW="100px"
                    borderRadius="10"
                    variant="geossOutline"
                    color="blue.300"
                >
                    {translate("pages.recommendations.previous")}
                </Button>
                <Button
                    onClick={() =>
                        props.fetchFunction(props.isZeroFirst ? props.pagesInfo.number + 1 : props.pagesInfo.number)
                    }
                    isDisabled={
                        props.pagesInfo.totalPages === 0
                            ? true
                            : props.pagesInfo.number ===
                              (props.isZeroFirst ? props.pagesInfo.totalPages - 1 : props.pagesInfo.totalPages)
                            ? true
                            : false
                    }
                    maxW="100px"
                    borderRadius="10"
                    variant="geossOutline"
                    color="blue.300"
                >
                    {translate("pages.recommendations.next")}
                </Button>
                <Button
                    onClick={() => props.fetchFunction(props.pagesInfo.totalPages - 1)}
                    isDisabled={
                        props.pagesInfo.totalPages === 0
                            ? true
                            : props.pagesInfo.number ===
                              (props.isZeroFirst ? props.pagesInfo.totalPages - 1 : props.pagesInfo.totalPages)
                            ? true
                            : false
                    }
                    maxW="100px"
                    borderRadius="10"
                    variant="geossOutline"
                    color="blue.300"
                    rightIcon={<ArrowRightIcon />}
                >
                    {translate("pages.recommendations.last")}
                </Button>
            </Flex>
        </Flex>
    );
};

export default PagesControls;
