import { UserResourcesService } from "@/services/api/users/curatedUserResourcesService";
import { WorkflowService } from "@/services/api/users/curatedWorkflowService";
import { PagesInfo } from "@/types/models/page";
import { ResourceEntry } from "@/types/models/userResources";
import useCustomToast from "@/utils/useCustomToast";
import useFormatMsg from "@/utils/useFormatMsg";
import { ChevronDownIcon } from "@chakra-ui/icons";
import {
    Flex,
    TableContainer,
    Thead,
    Tr,
    Th,
    Tbody,
    Td,
    Menu,
    MenuButton,
    Button,
    MenuList,
    Text,
    Table,
    MenuItem,
} from "@chakra-ui/react";
import { useRouter } from "next/router";
import React, { useEffect, useState } from "react";
import { Loader } from "../Loader/Loader";
import { MainContentHeader } from "../MainContent/MainContentHeader";
import PagesControls from "../PagesControls/PagesControls";
import { initialPagesInfo } from "../Recommendations/DefaultValues";

export const ManageEntities = () => {
    const [isLoading, setIsLoading] = useState(true);
    const [pagesInfo, setPagesInfo] = useState<PagesInfo>(initialPagesInfo);
    const [resources, setResources] = useState<ResourceEntry[]>([]);
    const { translate } = useFormatMsg();
    const { showToast } = useCustomToast();
    const router = useRouter();

    const headingActions = [
        {
            titleId: "pages.entryResources.return",
            onClick: () => router.push("/entry-resources"),
            disabled: false,
        },
    ];

    useEffect(() => {
        fetchResources();
    }, []);

    const fetchResources = async (page = 0, numberOfHits = 20) => {
        try {
            setIsLoading(true);
            let response = await UserResourcesService.getEntries(page, numberOfHits);
            setResources(response.content);
            const pInfo = {
                size: response.size,
                totalElements: response.totalElements,
                totalPages: response.totalPages,
                number: response.number,
            };
            setPagesInfo(pInfo);
        } catch (e) {
            console.log(e);
        } finally {
            setIsLoading(false);
        }
    };

    const deleteEntry = async (entryId: number) => {
        try {
            await WorkflowService.deleteEntry(entryId);
            fetchResources(pagesInfo.number, 20);
            showToast({
                title: "Status changed",
                description: `Entry with ID: ${entryId} has been deleted.`,
            });
        } catch (e) {
            console.log(e);
        }
    };

    if (isLoading) {
        return <Loader />;
    }

    return (
        <Flex direction="column" w={"100%"} overflowY="auto">
            <MainContentHeader titleId="pages.entryResources.entries" actions={headingActions} />
            <Flex direction="column" w={"100%"} padding={4}>
                <TableContainer
                    w={"100%"}
                    overflowX={"auto"}
                    marginBottom={4}
                    marginTop={4}
                    border={"1px solid"}
                    borderColor="#dedede"
                    borderRadius={6}
                >
                    <Table variant="striped" size={"sm"}>
                        <Thead
                            backgroundColor={"#f0f0f0"}
                            height={"40px"}
                            borderBottom={"3px solid"}
                            borderColor="#8a8888"
                        >
                            <Tr>
                                <Th textTransform="none" fontSize={"14px"}>
                                    {translate("pages.entryResources.id")}
                                </Th>
                                <Th textTransform="none" fontSize={"14px"}>
                                    {translate("pages.entryResources.title")}
                                </Th>
                                <Th textTransform="none" fontSize={"14px"}>
                                    {translate("pages.entryResources.summary")}
                                </Th>
                                <Th textTransform="none" fontSize={"14px"} textAlign='end' pr='6'>
                                    {translate("pages.entryResources.actions")}
                                </Th>
                            </Tr>
                        </Thead>
                        <Tbody>
                            {resources &&
                                resources.map((resource: ResourceEntry) => {
                                    return (
                                        <Tr key={resource.id}>
                                            <Td>{resource.id}</Td>
                                            <Td>{resource.title}</Td>
                                            <Td>{resource.summary}</Td>
                                            <Td textAlign='end'>
                                                <Menu>
                                                    <MenuButton
                                                        size="sm"
                                                        colorScheme="teal"
                                                        as={Button}
                                                        rightIcon={<ChevronDownIcon />}
                                                    >
                                                        Actions
                                                    </MenuButton>
                                                    <MenuList>
                                                        <>
                                                            <MenuItem onClick={() => deleteEntry(resource.id)}>
                                                                {translate("pages.entryResources.deleteEntry")}
                                                            </MenuItem>
                                                            <MenuItem
                                                                onClick={() =>
                                                                    router.push({
                                                                        pathname: `/entry-resources/update-entry`,
                                                                        query: { entryId: resource.id },
                                                                    })
                                                                }
                                                            >
                                                                {translate("pages.entryResources.updateEntry")}
                                                            </MenuItem>
                                                        </>
                                                    </MenuList>
                                                </Menu>
                                            </Td>
                                        </Tr>
                                    );
                                })}
                        </Tbody>
                    </Table>
                </TableContainer>
                <PagesControls
                    pagesInfo={pagesInfo}
                    numberOfElements={resources.length}
                    fetchFunction={fetchResources}
                    isZeroFirst={true}
                />
            </Flex>
        </Flex>
    );
};
