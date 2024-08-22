import { ResourceExtensionsService } from "@/services/api/users/curatedResourceExtensionsService";
import { WorkflowService } from "@/services/api/users/curatedWorkflowService";
import { ToastStatus } from "@/types";
import { PagesInfo } from "@/types/models/page";
import { ExtensionContent } from "@/types/models/userExtensions";
import { generateGenericErrorMessage } from "@/utils/helpers";
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
    Table,
    MenuItem,
} from "@chakra-ui/react";
import { useRouter } from "next/router";
import React, { useEffect, useState } from "react";
import { Loader } from "../Loader/Loader";
import { MainContentHeader } from "../MainContent/MainContentHeader";
import PagesControls from "../PagesControls/PagesControls";
import { initialPagesInfo } from "../Recommendations/DefaultValues";

export const ManageExtensions = () => {
    const [isLoading, setIsLoading] = useState(true);
    const [pagesInfo, setPagesInfo] = useState<PagesInfo>(initialPagesInfo);
    const [extensions, setExtensions] = useState<ExtensionContent[]>([]);
    const { translate } = useFormatMsg();
    const { showToast } = useCustomToast();
    const router = useRouter();

    const headingActions = [
        {
            titleId: "pages.entryResources.return",
            onClick: () => router.push("/resource-extensions"),
            disabled: false,
        },
    ];

    useEffect(() => {
        fetchExtensions();
    }, []);

    const fetchExtensions = async (page = 0, numberOfHits = 20) => {
        try {
            setIsLoading(true);
            let response = await ResourceExtensionsService.getExtensions(page, numberOfHits);
            setExtensions(response.content);
            const pInfo = {
                size: response.size,
                totalElements: response.totalElements,
                totalPages: response.totalPages,
                number: response.number,
            };
            setPagesInfo(pInfo);
        } catch (e: any) {
            console.error(e)
            const msg = generateGenericErrorMessage(e)

            showToast({
                title: translate("general.error"),
                description: `${msg || ""}`,
                status: ToastStatus.ERROR,
            });
        } finally {
            setIsLoading(false);
        }
    };

    const deleteEntry = async (entryExtensionId: number) => {
        try {
            await WorkflowService.deleteExtensionEntry(entryExtensionId);
            fetchExtensions(pagesInfo.number, 20);
            showToast({
                title: translate("pages.curatedToastsMessages.statusChange"),
                description: `${translate("pages.curatedToastsMessages.entryID")} ${entryExtensionId} ${translate(
                    "pages.curatedToastsMessages.deleted"
                )}.`,
            });
        } catch (e: any) {
            console.error(e)
            const msg = generateGenericErrorMessage(e)

            showToast({
                title: translate("general.error"),
                description: `${msg || ""}`,
                status: ToastStatus.ERROR,
            });
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
                                <Th textTransform="none" fontSize={"14px"} textAlign="end" pr="6">
                                    {translate("pages.entryResources.actions")}
                                </Th>
                            </Tr>
                        </Thead>
                        <Tbody>
                            {extensions &&
                                extensions.map((extension: ExtensionContent) => {
                                    return (
                                        <Tr key={extension.id}>
                                            <Td>{extension.id}</Td>
                                            <Td>{extension.title}</Td>
                                            <Td>{extension.summary}</Td>
                                            <Td textAlign="end">
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
                                                            <MenuItem onClick={() => deleteEntry(extension.id)}>
                                                                {translate("pages.entryResources.deleteEntry")}
                                                            </MenuItem>
                                                            <MenuItem
                                                                onClick={() =>
                                                                    router.push({
                                                                        pathname: `/resource-extensions/update-entry`,
                                                                        query: { entryId: extension.id },
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
                    numberOfElements={extensions.length}
                    fetchFunction={fetchExtensions}
                    isZeroFirst={true}
                />
            </Flex>
        </Flex>
    );
};
