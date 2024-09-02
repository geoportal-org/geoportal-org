import { ResourceExtensionsService } from "@/services/api/users/curatedResourceExtensionsService";
import { UserResourcesService } from "@/services/api/users/curatedUserResourcesService";
import { ButtonVariant, ToastStatus } from "@/types";
import { LinkType, TaskType } from "@/types/models/userResources";
import { generateGenericErrorMessage } from "@/utils/helpers";
import useCustomToast from "@/utils/useCustomToast";
import useFormatMsg from "@/utils/useFormatMsg";
import {
    Box,
    Button,
    Divider,
    Flex,
    FormControl,
    FormLabel,
    Grid,
    Input,
    Select,
    Text,
    Textarea,
    Tooltip,
} from "@chakra-ui/react";
import { useSession } from "next-auth/react";
import { useRouter } from "next/router";
import React, { useEffect, useState } from "react";
import { CrossIcon, PlusIcon, TutorialIcon } from "../Icons";
import { MainContentHeader } from "../MainContent/MainContentHeader";

const defaultFormState = {
    title: "",
    summary: "",
    keywords: "",
    tags: "",
};

const defaultLinkState = {
    id: 0,
    title: "",
    name: "",
    description: "",
    endpoint: "",
    endpointUrl: "",
    endpointType: "",
    customEndpoint: "",
    protocol: "",
    protocolValue: "",
    customProtocol: "",
};

type Props = {
    isUpdateUserExtension?: boolean;
};

export const EditExtension = ({ isUpdateUserExtension = false }: Props) => {
    //enums
    const [endpoints, setEndpoints] = useState<any[]>([]);
    const [urlTypes, setUrlTypes] = useState<any[]>([]);
    const [protocols, setProtocols] = useState<any[]>([]);

    const [formData, setFormData] = useState(defaultFormState);
    const [links, setLinks] = useState([defaultLinkState]);
    const [defaultEntryVal, setDefaultEntryVal] = useState<any>();
    const { showToast } = useCustomToast();

    const session = useSession();
    const router = useRouter();
    const { translate } = useFormatMsg();
    const headingActions = [
        {
            titleId: "pages.entryResources.return",
            onClick: () => router.push("/resource-extensions"),
            disabled: false,
        },
    ];

    useEffect(() => {
        if (!router.isReady) return;
        getSelectOptions();
        getEntry();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [router.isReady]);

    const handleSubmit = async (e: any) => {
        e.preventDefault();
        try {
            if (!isUpdateUserExtension) {
                const body = createEntryBody();
                await ResourceExtensionsService.updateEntry(Number(router.query.entryId), body);
            } else {
                const body = createExtensionBody();
                await ResourceExtensionsService.updateExtension(Number(router.query.userExtensionId), body);
            }
            const tOptions = getTransferOptions();
            await ResourceExtensionsService.updateTransferOptionsForExtension(tOptions, Number(router.query.entryId));
        } catch (e: any) {
            console.error(e)
            const msg = generateGenericErrorMessage(e)

            showToast({
                title: translate("general.error"),
                description: `${msg || ""}`,
                status: ToastStatus.ERROR,
            });
        } finally {
            router.push("/resource-extensions");
        }
    };

    const createEntryBody = () => {
        return {
            code: defaultEntryVal.code,
            dataSource: defaultEntryVal.dataSource.code,
            title: formData.title,
            summary: formData.summary,
            keywords: formData.keywords,
            tags: formData.tags,
            //@ts-ignore
            userId: session?.data?.userId || "",
            username: defaultEntryVal.username,
            transferOptionExtensions: [],
        };
    };

    const createExtensionBody = () => {
        return {
            //@ts-ignore
            userId: session?.data?.userId || "",
            entryName: router.query.entryName as string,
            description: (router.query.description as string) || "",
            taskType: router.query.taskType as TaskType,
            entryExtension: {
                code: defaultEntryVal.code,
                dataSource: defaultEntryVal.dataSource.code,
                title: formData.title,
                summary: formData.summary,
                keywords: formData.keywords,
                tags: formData.tags,
                //@ts-ignore
                userId: session?.data?.userId || "",
                username: defaultEntryVal.username,
                transferOptionExtensions: [],
            },
        };
    };

    const getEntry = async () => {
        const entryId: number = Number(router.query.entryId) || 0;
        try {
            const response = await ResourceExtensionsService.getEntry(entryId);
            const entry = response[0].entryExtension;

            setDefaultEntryVal(entry);
            let data = { ...defaultFormState };
            data.title = entry.title;
            data.keywords = entry.keywords;
            data.summary = entry.summary;
            data.tags = entry.tags;

            let links: any = [];
            response.forEach((transferOption) => {
                let link = { ...defaultLinkState };
                link.id = transferOption.id;
                link.title = transferOption.name;
                link.description = transferOption.description;
                link.name = transferOption.name;
                if (transferOption.endpoint.isCustom === 1) {
                    link.endpoint = "customEndpoint";
                    link.customEndpoint = transferOption.endpoint.url;
                } else {
                    link.endpointUrl = transferOption.endpoint.url;
                }
                link.endpointType = transferOption.endpoint.urlType.toUpperCase();

                if (transferOption.protocol.isCustom === 1) {
                    link.protocol = "customProtocol";
                    link.customProtocol = transferOption.protocol.value;
                } else {
                    link.protocolValue = transferOption.protocol.value;
                    link.protocol = transferOption.protocol.value;
                }
                links.push(link);
            });
            setLinks(links);
            setFormData(data);
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

    const handleInputChange = (e: any) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    const handleLinksInputChange = (e: any, index: number) => {
        const { name, value } = e.target;
        const newArr = [...links];
        newArr[index] = { ...newArr[index], [name]: value };
        setLinks(newArr);
    };

    const getSelectOptions = async () => {
        try {
            const endpointsVal = localStorage.getItem("endpoints");
            if (endpointsVal && endpointsVal?.length > 0) {
                let endpoints: any[] = JSON.parse(endpointsVal);
                setEndpoints(endpoints);
            } else {
                const newEndpoints = await UserResourcesService.getEndpoints();
                localStorage.setItem("endpoints", JSON.stringify(newEndpoints));
                setEndpoints(newEndpoints);
            }

            const urlTypesVal = localStorage.getItem("urlTypes");
            if (urlTypesVal && urlTypesVal?.length > 0) {
                let urlTypes: any[] = JSON.parse(urlTypesVal);
                setUrlTypes(urlTypes);
            } else {
                const newUrlTypes = await UserResourcesService.getUrlTypes();
                localStorage.setItem("urlTypes", JSON.stringify(newUrlTypes));
                setEndpoints(newUrlTypes);
            }

            const protocolsVal = localStorage.getItem("protocols");
            if (protocolsVal && protocolsVal?.length > 0) {
                let protocols: any[] = JSON.parse(protocolsVal);
                setProtocols(protocols);
            } else {
                const newProtocols = await UserResourcesService.getProtocols();
                localStorage.setItem("protocols", JSON.stringify(newProtocols));
                setProtocols(newProtocols);
            }
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

    const getTransferOptions = () => {
        const transferOptions: LinkType[] = [];
        links.forEach((link: any) => {
            transferOptions.push({
                id: link.id,
                name: link.name,
                description: link.description,
                title: link.title,
                protocol: {
                    value: link.protocol === "customProtocol" ? link.customProtocol : link.protocolValue,
                },
                endpoint: {
                    url: link.endpoint === "customEndpoint" ? link.customEndpoint : link.endpointUrl,
                    urlType: link.endpointType,
                },
            });
        });
        return transferOptions;
    };

    const renderSectionDivider = (text: string, addLink = false) => {
        return (
            <Box position="relative" py={3}>
                <Flex w="100%" justifyContent="space-between">
                    <Text fontSize="lg" color="grey">
                        {translate(text)}
                    </Text>{" "}
                    {addLink && (
                        <Button
                            onClick={() => {
                                const newArr = [...links];
                                newArr.push(defaultLinkState);
                                setLinks(newArr);
                            }}
                            display="flex"
                            size="sm"
                            variant={ButtonVariant.GHOST}
                            color={"brand.dark"}
                            bg={"inherit"}
                            whiteSpace="break-spaces"
                            alignItems="center"
                            gap={1}
                        >
                            <PlusIcon color="#0661A9" />
                            {translate("pages.entryResources.addEntityPage.addLinkButton")}
                        </Button>
                    )}
                </Flex>

                <Divider />
            </Box>
        );
    };

    const generateEntryContentForm = () => {
        return (
            <Grid templateColumns={{ base: "1fr", lg: "repeat(2, 1fr)" }} gap={3} pt={3}>
                <Flex direction="column" gap={3}>
                    <FormControl isRequired>
                        <FormLabel fontSize={14} htmlFor="title">
                            {translate("pages.entryResources.addEntityPage.title")}
                        </FormLabel>
                        <Input
                            disabled={true}
                            type="text"
                            id="title"
                            name="title"
                            value={formData.title}
                            onChange={handleInputChange}
                        ></Input>
                    </FormControl>
                    <FormControl>
                        <FormLabel fontSize={14} htmlFor="summary">
                            {translate("pages.entryResources.addEntityPage.summary")}
                        </FormLabel>
                        <Textarea
                            id="summary"
                            name="summary"
                            value={formData.summary}
                            onChange={handleInputChange}
                            h="150px"
                            maxH="300px"
                        ></Textarea>
                    </FormControl>
                </Flex>
                <Flex direction="column" gap={3}>
                    <FormControl gridColumn={2} gridRow={3} isRequired>
                        <FormLabel fontSize={14} htmlFor="keywords" display="flex" alignItems="center" gap={1}>
                            {translate("pages.entryResources.addEntityPage.keywords")}
                            <Tooltip
                                label={translate("pages.entryResources.addEntityPage.keywordsTooltip")}
                                shouldWrapChildren
                                placement="top"
                            >
                                <TutorialIcon color="#0661A9" width={15} height={15} />
                            </Tooltip>
                        </FormLabel>
                        <Input
                            type="text"
                            id="keywords"
                            name="keywords"
                            value={formData.keywords}
                            onChange={handleInputChange}
                        ></Input>
                    </FormControl>
                    <FormControl gridColumn={2} gridRow={3} isRequired>
                        <FormLabel fontSize={14} htmlFor="tags" display="flex" alignItems="center" gap={1}>
                            {translate("pages.entryResources.addEntityPage.tags")}
                            <Tooltip
                                label={translate("pages.entryResources.addEntityPage.tagsTooltip")}
                                shouldWrapChildren
                                placement="top"
                            >
                                <TutorialIcon color="#0661A9" width={15} height={15} />
                            </Tooltip>
                        </FormLabel>
                        <Input
                            type="text"
                            id="tags"
                            name="tags"
                            value={formData.tags}
                            onChange={handleInputChange}
                        ></Input>
                    </FormControl>
                </Flex>
            </Grid>
        );
    };

    const generateLinkOption = (index: number) => {
        return (
            <div key={index}>
                <Grid templateColumns={{ base: "1fr", lg: "1fr repeat(3, 12fr) 3fr" }} gap={3} pt={3}>
                    <Text>
                        {index + 1}
                        {"."}
                    </Text>
                    <Flex direction="column" gap={3}>
                        <FormControl isRequired>
                            <FormLabel fontSize={14} htmlFor="title">
                                {translate("pages.entryResources.addEntityPage.title")}
                            </FormLabel>
                            <Input
                                type="text"
                                id="title"
                                name="title"
                                value={links[index].title}
                                onChange={(event: any) => handleLinksInputChange(event, index)}
                            ></Input>
                        </FormControl>
                        <FormControl>
                            <FormLabel fontSize={14} htmlFor="name">
                                {translate("pages.entryResources.addEntityPage.name")}
                            </FormLabel>
                            <Input
                                type="text"
                                id="name"
                                name="name"
                                value={links[index].name}
                                onChange={(event: any) => handleLinksInputChange(event, index)}
                            ></Input>
                        </FormControl>
                    </Flex>
                    <FormControl>
                        <FormLabel fontSize={14} htmlFor="description">
                            {translate("pages.entryResources.addEntityPage.description")}
                        </FormLabel>
                        <Textarea
                            id="description"
                            name="description"
                            value={links[index].description}
                            onChange={(event: any) => handleLinksInputChange(event, index)}
                            h="80px"
                            maxH="300px"
                        ></Textarea>
                    </FormControl>
                    <Flex direction="column" gap={3}>
                        <FormControl isRequired>
                            <FormLabel fontSize={14} htmlFor="endpoint">
                                {translate("pages.entryResources.addEntityPage.endpoint")}
                            </FormLabel>
                            <Select
                                id="endpoint"
                                name="endpoint"
                                value={links[index].endpoint}
                                placeholder={translate("pages.entryResources.addEntityPage.endpointPlaceholder")}
                                onChange={(event: any) => {
                                    const { value } = event.target;
                                    const endpoint = endpoints.find((endpoint) => endpoint.url === value);
                                    const newArr = [...links];
                                    newArr[index] = {
                                        ...newArr[index],
                                        endpoint: value,
                                        endpointUrl: value === "customEndpoint" ? "" : value,
                                        endpointType: endpoint && endpoint.urlType,
                                    };
                                    setLinks(newArr);
                                }}
                            >
                                {endpoints.map((endpoint: any) => {
                                    return (
                                        <option key={endpoint.id} value={endpoint.url}>
                                            {endpoint.url}
                                        </option>
                                    );
                                })}
                                <option value="customEndpoint">
                                    {translate("pages.entryResources.addEntityPage.customEndpoint")}
                                </option>
                            </Select>
                            {links[index].endpoint === "customEndpoint" && (
                                <>
                                    <Input
                                        type="text"
                                        id="customEndpoint"
                                        name="customEndpoint"
                                        value={links[index].customEndpoint}
                                        onChange={(event: any) => handleLinksInputChange(event, index)}
                                        placeholder={translate(
                                            "pages.entryResources.addEntityPage.endpointUrlPlaceholder"
                                        )}
                                        mt={1}
                                        mb={1}
                                    />
                                    <Select
                                        id="endpointType"
                                        name="endpointType"
                                        value={links[index].endpointType}
                                        placeholder={translate(
                                            "pages.entryResources.addEntityPage.endpointTypePlaceholder"
                                        )}
                                        onChange={(event: any) => handleLinksInputChange(event, index)}
                                    >
                                        {urlTypes.map((url: any) => {
                                            return (
                                                <option key={url} value={url}>
                                                    {url}
                                                </option>
                                            );
                                        })}
                                    </Select>
                                </>
                            )}
                        </FormControl>
                        <FormControl gridColumn={2} gridRow={3} isRequired>
                            <FormLabel fontSize={14} htmlFor="protocol">
                                {translate("pages.entryResources.addEntityPage.protocol")}
                            </FormLabel>
                            <Select
                                id="protocol"
                                name="protocol"
                                value={links[index].protocol}
                                placeholder={translate("pages.entryResources.addEntityPage.protocolPlaceholder")}
                                onChange={(event: any) => {
                                    const { value } = event.target;
                                    const protocol = protocols.find((protocol) => protocol.value === value);
                                    const newArr = [...links];
                                    newArr[index] = {
                                        ...newArr[index],
                                        protocol: value,
                                        protocolValue: protocol === "customProtocol" ? "" : value,
                                    };
                                    setLinks(newArr);
                                }}
                            >
                                {protocols.map((protocol: any) => {
                                    return (
                                        <option key={protocol.id} value={protocol.value}>
                                            {protocol.value}
                                        </option>
                                    );
                                })}
                                <option value="customProtocol">
                                    {translate("pages.entryResources.addEntityPage.customProtocol")}
                                </option>
                            </Select>
                            {links[index].protocol === "customProtocol" && (
                                <Input
                                    type="text"
                                    id="customProtocol"
                                    name="customProtocol"
                                    value={links[index].customProtocol}
                                    onChange={(event: any) => handleLinksInputChange(event, index)}
                                    mt={1}
                                    placeholder={translate(
                                        "pages.entryResources.addEntityPage.customProtocolPlaceholder"
                                    )}
                                />
                            )}
                        </FormControl>
                    </Flex>
                    {links.length > 1 && (
                        <Button
                            onClick={() => {
                                const newArr = [...links];
                                newArr.splice(index, 1);
                                setLinks(newArr);
                            }}
                            display="flex"
                            size="sm"
                            variant={ButtonVariant.GHOST}
                            color={"brand.dark"}
                            bg={"inherit"}
                            alignSelf="center"
                            whiteSpace="break-spaces"
                            alignItems="center"
                            gap={1}
                        >
                            <CrossIcon />
                            {translate("pages.entryResources.addEntityPage.removeOptionButton")}
                        </Button>
                    )}
                </Grid>
                {index !== links.length - 1 && <Divider mt={8} mb={4} />}
            </div>
        );
    };

    return (
        <Flex direction="column" w={"100%"} overflowY="auto">
            <MainContentHeader titleId={"pages.entryResources.addEntityPage.updateTitle"} actions={headingActions} />
            <Flex direction="column" w={"100%"} padding={4}>
                <form onSubmit={handleSubmit}>
                    {renderSectionDivider("pages.entryResources.addEntityPage.entryContent")}
                    {generateEntryContentForm()}
                    {renderSectionDivider("pages.entryResources.addEntityPage.linkOptions", true)}
                    {links.map((link: any, index: number) => generateLinkOption(index))}
                    <Flex direction="row" w="100%" justifyContent="flex-end" pt={6}>
                        <Button type="submit" colorScheme="blue" w={"120px"} h="2em">
                            {translate("pages.entryResources.updateEntry")}
                        </Button>
                    </Flex>
                </form>
            </Flex>
        </Flex>
    );
};
