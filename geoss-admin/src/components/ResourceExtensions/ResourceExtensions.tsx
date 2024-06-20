import { WorkflowService } from "@/services/api/users/curatedWorkflowService";
import { PagesInfo } from "@/types/models/page";
import useCustomToast from "@/utils/useCustomToast";
import useFormatMsg from "@/utils/useFormatMsg";
import { ChevronDownIcon } from "@chakra-ui/icons";
import {
    Button,
    Flex,
    Table,
    TableContainer,
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
import { ResourceExtensionsService } from "@/services/api/users/curatedResourceExtensionsService";
import { UserExtensionContent } from "@/types/models/userExtensions";

const statuses = {
    draft: "#68a1fc",
    pending: "#f7ea88",
    approved: "#95f788",
    denied: "#ff4a50",
    "in-trash": "#c9c9c9",
};

export const ResourceExtensions = () => {
    const [isLoading, setIsLoading] = useState(true);
    const [pagesInfo, setPagesInfo] = useState<PagesInfo>(initialPagesInfo);
    const [userExtensions, setUserExtensions] = useState<UserExtensionContent[]>([]);
    const { translate } = useFormatMsg();
    const { showToast } = useCustomToast();
    const session = useSession();
    //@ts-ignore
    const userId = session?.data?.userId || "";
    const router = useRouter();

    useEffect(() => {
        fetchExtensions();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const fetchExtensions = async (page = 0, numberOfHits = 20) => {
        try {
            setIsLoading(true);
            let response = await ResourceExtensionsService.getUserExtensions(page, numberOfHits, userId);
            setUserExtensions(response.content);
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

    const approveExtension = async (userExtensionId: number) => {
        try {
            await WorkflowService.approveUserExtension(userExtensionId);
            fetchExtensions(pagesInfo.number, 20);
            showToast({
                title: translate("pages.curatedToastsMessages.statusChange"),
                description: `${translate("pages.curatedToastsMessages.extensionID")} ${userExtensionId} ${translate(
                    "pages.curatedToastsMessages.approved"
                )}.`,
            });
        } catch (e) {
            console.log(e);
        }
    };

    const deleteExtension = async (userExtensionId: number) => {
        try {
            await WorkflowService.deleteUserExtension(userExtensionId);
            fetchExtensions(pagesInfo.number, 20);
            showToast({
                title: translate("pages.curatedToastsMessages.statusChange"),
                description: `${translate("pages.curatedToastsMessages.extensionID")} ${userExtensionId} ${translate(
                    "pages.curatedToastsMessages.deleted"
                )}.`,
            });
        } catch (e) {
            console.log(e);
        }
    };

    const denyExtension = async (userExtensionId: number) => {
        try {
            await WorkflowService.denyUserExtension(userExtensionId);
            fetchExtensions(pagesInfo.number, 20);
            showToast({
                title: translate("pages.curatedToastsMessages.statusChange"),
                description: `${translate("pages.curatedToastsMessages.extensionID")} ${userExtensionId} ${translate(
                    "pages.curatedToastsMessages.denied"
                )}.`,
            });
        } catch (e) {
            console.log(e);
        }
    };

    const pendingExtension = async (userExtensionId: number) => {
        try {
            await WorkflowService.pendingUserExtension(userExtensionId);
            fetchExtensions(pagesInfo.number, 20);
            showToast({
                title: translate("pages.curatedToastsMessages.statusChange"),
                description: `${translate("pages.curatedToastsMessages.extensionID")} ${userExtensionId} ${translate(
                    "pages.curatedToastsMessages.pending"
                )}.`,
            });
        } catch (e) {
            console.log(e);
        }
    };

    const updateResource = async (resourceExtensionId: number) => {
        let extensionFromList: any = {
            ...userExtensions.find((userExtension) => userExtension.id === resourceExtensionId),
        };
        extensionFromList.taskType = "update";
        delete extensionFromList.id;
        delete extensionFromList.createDate;
        delete extensionFromList.modifiedDate;
        delete extensionFromList.status;
        delete extensionFromList.entryExtension.id;
        const extensionForUpdate = { ...extensionFromList };
        extensionForUpdate.entryExtension.dataSource = extensionFromList.entryExtension.dataSource.code;
        try {
            const extension = await ResourceExtensionsService.getEntry(resourceExtensionId);
            let transferOptions: any = [];
            extension.forEach((tOption) => {
                transferOptions.push({
                    name: tOption.name,
                    description: tOption.description || "",
                    title: tOption.displayTitle,
                    protocol: {
                        value: tOption.protocol.value,
                    },
                    endpoint: {
                        url: tOption.endpoint.url,
                        urlType: tOption.endpoint.urlType,
                    },
                });
            });
            extensionForUpdate.entryExtension.transferOptionExtensions = transferOptions;
            await ResourceExtensionsService.createUserExtension(extensionForUpdate);
            fetchExtensions(pagesInfo.number, 20);
            showToast({
                title: translate("pages.curatedToastsMessages.statusChange"),
                description: `${translate("pages.curatedToastsMessages.updateExtension")}.`,
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
            <MainContentHeader titleId="nav.extensions.section.resourceExtensions" />
            <Flex direction="column" w={"100%"} padding={4}>
                <Text fontSize="xl" mt="2" mb="2" color="grey">
                    {translate("pages.entryResources.actionsTitle")}
                </Text>
                <Flex direction="row" justifyContent="flex-start" gap={1}>
                    <PrimaryButton onClick={() => router.push("/resource-extensions/manage-extensions")}>
                        {translate("pages.resourceExtensions.manage")}
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
                            {userExtensions &&
                                userExtensions.map((userExtension: UserExtensionContent) => {
                                    return (
                                        <Tr key={userExtension.id}>
                                            <Td>{userExtension.id}</Td>
                                            <Td>{userExtension.entryName}</Td>
                                            <Td>{userExtension.taskType}</Td>
                                            <Td>{userExtension.createDate}</Td>
                                            <Td>{userExtension.modifiedDate}</Td>
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
                                                    bg={statuses[userExtension.status as keyof typeof statuses]}
                                                >
                                                    {userExtension.status}
                                                </Box>
                                            </Td>
                                            <Td textAlign="end">
                                                <Menu>
                                                    <MenuButton
                                                        isDisabled={
                                                            (userExtension.hasOtherExtensionsWithSameEntry &&
                                                                userExtension.status === "approved") ||
                                                            userExtension.status === "denied"
                                                        }
                                                        size="sm"
                                                        colorScheme={
                                                            (userExtension.hasOtherExtensionsWithSameEntry &&
                                                                userExtension.status === "approved") ||
                                                            userExtension.status === "denied"
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
                                                        {userExtension.status === "draft" && (
                                                            <>
                                                                <MenuItem
                                                                    onClick={() =>
                                                                        router.push({
                                                                            pathname: `/resource-extensions/update-resource`,
                                                                            query: {
                                                                                entryId:
                                                                                    userExtension.entryExtension.id,
                                                                                userExtensionId: userExtension.id,
                                                                                entryName: userExtension.entryName,
                                                                                taskType: userExtension.taskType,
                                                                                description: userExtension.description,
                                                                            },
                                                                        })
                                                                    }
                                                                >
                                                                    {translate("pages.entryResources.editEntry")}
                                                                </MenuItem>
                                                                <MenuItem
                                                                    onClick={() => pendingExtension(userExtension.id)}
                                                                >
                                                                    {translate("pages.entryResources.pending")}
                                                                </MenuItem>
                                                                <MenuItem
                                                                    onClick={() => deleteExtension(userExtension.id)}
                                                                >
                                                                    {translate("pages.entryResources.delete")}
                                                                </MenuItem>
                                                            </>
                                                        )}
                                                        {userExtension.status === "pending" && (
                                                            <>
                                                                <MenuItem
                                                                    onClick={() => approveExtension(userExtension.id)}
                                                                >
                                                                    {translate("pages.entryResources.approve")}
                                                                </MenuItem>
                                                                <MenuItem
                                                                    onClick={() => denyExtension(userExtension.id)}
                                                                >
                                                                    {translate("pages.entryResources.deny")}
                                                                </MenuItem>
                                                            </>
                                                        )}
                                                        {userExtension.status === "denied" && (
                                                            <>
                                                                <MenuItem
                                                                    onClick={() => deleteExtension(userExtension.id)}
                                                                >
                                                                    {translate("pages.entryResources.delete")}
                                                                </MenuItem>
                                                                <MenuItem
                                                                    onClick={() => updateResource(userExtension.id)}
                                                                >
                                                                    {translate("pages.entryResources.update")}
                                                                </MenuItem>
                                                            </>
                                                        )}
                                                        {userExtension.status === "approved" && (
                                                            <MenuItem onClick={() => updateResource(userExtension.id)}>
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
                    numberOfElements={userExtensions.length}
                    fetchFunction={fetchExtensions}
                    isZeroFirst={true}
                />
            </Flex>
        </Flex>
    );
};
