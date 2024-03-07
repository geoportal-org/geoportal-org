import { UserResourcesService } from "@/services/api/users/curatedUserResourcesService";
import { WorkflowService } from "@/services/api/users/curatedWorkflowService";
import { PagesInfo } from "@/types/models/page";
import { CreateUserResource, TaskType, UserResourcesContentData } from "@/types/models/userResources";
import useCustomToast from "@/utils/useCustomToast";
import useFormatMsg from "@/utils/useFormatMsg";
import { ChevronDownIcon } from "@chakra-ui/icons";
import {
    Button,
    Flex,
    Table,
    TableContainer,
    Select,
    Tbody,
    Td,
    Text,
    Th,
    Thead,
    Tr,
    Box,
    MenuButton,
    MenuList,
    MenuItem,
    Menu,
} from "@chakra-ui/react";
import { useRouter } from "next/router";
import React, { useEffect, useState } from "react";
import { Loader } from "../Loader/Loader";
import { MainContentHeader } from "../MainContent/MainContentHeader";
import PagesControls from "../PagesControls/PagesControls";
import { PrimaryButton } from "../PrimaryButton/PrimaryButton";
import { initialPagesInfo } from "../Recommendations/DefaultValues";
import { useSession } from "next-auth/react";

const statuses = {
    draft: "#68a1fc",
    pending: "#f7ea88",
    approved: "#95f788",
    denied: "#ff4a50",
    "in-trash": "#c9c9c9",
};

export const EntryResources = () => {
    const [isLoading, setIsLoading] = useState(true);
    const [pagesInfo, setPagesInfo] = useState<PagesInfo>(initialPagesInfo);
    const [resources, setResources] = useState<UserResourcesContentData[]>([]);
    const { translate } = useFormatMsg();
    const { showToast } = useCustomToast();
    const session = useSession();
    //@ts-ignore
    const userId = session?.data?.userId || "";
    const router = useRouter();

    useEffect(() => {
        fetchResources();
    }, []);

    const fetchResources = async (page = 0, numberOfHits = 20) => {
        try {
            setIsLoading(true);
            let response = await UserResourcesService.getResources(page, numberOfHits, userId);

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

    const approveResource = async (userResourceId: number) => {
        try {
            await WorkflowService.approveUserResource(userResourceId);
            fetchResources(pagesInfo.number, 20);
            showToast({
                title: "Status changed",
                description: `Resource with ID: ${userResourceId} has been approved.`,
            });
        } catch (e) {
            console.log(e);
        }
    };

    const deleteResource = async (userResourceId: number) => {
        try {
            await WorkflowService.deleteUserResource(userResourceId);
            fetchResources(pagesInfo.number, 20);
            showToast({
                title: "Status changed",
                description: `Resource with ID: ${userResourceId} has been deleted.`,
            });
        } catch (e) {
            console.log(e);
        }
    };

    const denyResource = async (userResourceId: number) => {
        try {
            await WorkflowService.denyUserResource(userResourceId);
            fetchResources(pagesInfo.number, 20);
            showToast({
                title: "Status changed",
                description: `Resource with ID: ${userResourceId} has been denied.`,
            });
        } catch (e) {
            console.log(e);
        }
    };

    const pendingResource = async (userResourceId: number) => {
        try {
            await WorkflowService.pendingUserResource(userResourceId);
            fetchResources(pagesInfo.number, 20);
            showToast({
                title: "Status changed",
                description: `Resource with ID: ${userResourceId} has status pending.`,
            });
        } catch (e) {
            console.log(e);
        }
    };

    const updateResource = async (userResourceId: number) => {
        let resourceFromList: any = { ...resources.find((userResource) => userResource.id === userResourceId) };
        resourceFromList.taskType = "update";
        delete resourceFromList.id;
        delete resourceFromList.createDate;
        delete resourceFromList.hasOtherResourcesWithSameEntry;
        delete resourceFromList.modifiedDate;
        delete resourceFromList.status;
        delete resourceFromList.entry.id;

        const resourceForUpdate = { ...resourceFromList };
        resourceForUpdate.entry.type = resourceFromList.entry.type.code;
        resourceForUpdate.entry.source = {
            term: resourceFromList.entry.source.term,
            code: resourceFromList.entry.source.code,
        };
        resourceForUpdate.entry.dataSource = resourceFromList.entry.dataSource.code;
        resourceForUpdate.entry.displayDataSource = resourceFromList.entry.displayDataSource.code;
        resourceForUpdate.entry.definitionType = resourceFromList.entry.definitionType.code;

        try {
            const resource = await UserResourcesService.getEntry(userResourceId);
            let transferOptions: any = [];
            resource.forEach((tOption) => {
                transferOptions.push({
                    name: tOption.name,
                    description: tOption.description,
                    title: tOption.title,
                    protocol: {
                        value: tOption.protocol.value,
                    },
                    endpoint: {
                        url: tOption.endpoint.url,
                        urlType: tOption.endpoint.urlType,
                    },
                });
            });

            await UserResourcesService.createResource(resourceForUpdate);
            fetchResources(pagesInfo.number, 20);
            showToast({
                title: "Status changed",
                description: `Created new resource with update status.`,
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
            <MainContentHeader titleId="nav.users.section.entryResources" />
            <Flex direction="column" w={"100%"} padding={4}>
                <Text fontSize="xl" mt="2" mb="2" color="grey">
                    {translate("pages.entryResources.actionsTitle")}
                </Text>
                <Flex direction="row" justifyContent="flex-start" gap={1}>
                    <PrimaryButton onClick={() => router.push("/entry-resources/add-entity")}>
                        {translate("pages.entryResources.addEntity")}
                    </PrimaryButton>
                    <PrimaryButton onClick={() => router.push("/entry-resources/manage-entities")}>
                        {translate("pages.entryResources.manage")}
                    </PrimaryButton>
                </Flex>
                <Text fontSize="xl" mt="6" color="grey">
                    {translate("pages.entryResources.yourEntities")}
                </Text>
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
                                    {translate("pages.entryResources.entryName")}
                                </Th>
                                <Th textTransform="none" fontSize={"14px"}>
                                    {translate("pages.entryResources.taskType")}
                                </Th>
                                <Th textTransform="none" fontSize={"14px"}>
                                    {translate("pages.entryResources.startDate")}
                                </Th>
                                <Th textTransform="none" fontSize={"14px"}>
                                    {translate("pages.entryResources.modifiedDate")}
                                </Th>
                                <Th textTransform="none" fontSize={"14px"}>
                                    {translate("pages.entryResources.status")}
                                </Th>
                                <Th textTransform="none" fontSize={"14px"} textAlign="end" pr="6">
                                    {translate("pages.entryResources.actions")}
                                </Th>
                            </Tr>
                        </Thead>
                        <Tbody>
                            {resources &&
                                resources.map((resource: UserResourcesContentData) => {
                                    return (
                                        <Tr key={resource.id}>
                                            <Td>{resource.id}</Td>
                                            <Td>{resource.entryName}</Td>
                                            <Td>{resource.taskType}</Td>
                                            <Td>{resource.createDate}</Td>
                                            <Td>{resource.modifiedDate}</Td>
                                            <Td>
                                                <Box
                                                    display="flex"
                                                    justifySelf="center"
                                                    w="fit-content"
                                                    minW="80px"
                                                    alignContent="center"
                                                    justifyContent="center"
                                                    p="1.5"
                                                    borderRadius="4px"
                                                    bg={statuses[resource.status as keyof typeof statuses]}
                                                >
                                                    {resource.status}
                                                </Box>
                                            </Td>
                                            <Td textAlign="end">
                                                <Menu>
                                                    <MenuButton
                                                        isDisabled={
                                                            (resource.hasOtherResourcesWithSameEntry &&
                                                                resource.status === "approved") ||
                                                            resource.status === "denied"
                                                        }
                                                        size="sm"
                                                        colorScheme={
                                                            (resource.hasOtherResourcesWithSameEntry &&
                                                                resource.status === "approved") ||
                                                            resource.status === "denied"
                                                                ? "gray.400"
                                                                : "teal"
                                                        }
                                                        as={Button}
                                                        _hover={{}}
                                                        rightIcon={<ChevronDownIcon />}
                                                    >
                                                        {translate("pages.entryResources.actions")}
                                                    </MenuButton>
                                                    <MenuList>
                                                        {resource.status === "draft" && (
                                                            <>
                                                                <MenuItem
                                                                    onClick={() =>
                                                                        router.push({
                                                                            pathname: `/entry-resources/update-resource`,
                                                                            query: {
                                                                                entryId: resource.entry.id,
                                                                                userResourceId: resource.id,
                                                                                entryName: resource.entryName,
                                                                                taskType: resource.taskType,
                                                                            },
                                                                        })
                                                                    }
                                                                >
                                                                    {translate("pages.entryResources.editEntry")}
                                                                </MenuItem>
                                                                <MenuItem onClick={() => pendingResource(resource.id)}>
                                                                    {translate("pages.entryResources.pending")}
                                                                </MenuItem>
                                                                <MenuItem onClick={() => deleteResource(resource.id)}>
                                                                    {translate("pages.entryResources.delete")}
                                                                </MenuItem>
                                                            </>
                                                        )}
                                                        {resource.status === "pending" && (
                                                            <>
                                                                <MenuItem onClick={() => approveResource(resource.id)}>
                                                                    {translate("pages.entryResources.approve")}
                                                                </MenuItem>
                                                                <MenuItem onClick={() => denyResource(resource.id)}>
                                                                    {translate("pages.entryResources.deny")}
                                                                </MenuItem>
                                                            </>
                                                        )}
                                                        {resource.status === "denied" && (
                                                            <>
                                                                <MenuItem onClick={() => deleteResource(resource.id)}>
                                                                    {translate("pages.entryResources.delete")}
                                                                </MenuItem>
                                                                <MenuItem onClick={() => updateResource(resource.id)}>
                                                                    {translate("pages.entryResources.update")}
                                                                </MenuItem>
                                                            </>
                                                        )}
                                                        {resource.status === "approved" && (
                                                            <MenuItem onClick={() => updateResource(resource.id)}>
                                                                {translate("pages.entryResources.update")}
                                                            </MenuItem>
                                                        )}
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
