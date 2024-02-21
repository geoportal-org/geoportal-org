import { UserResourcesService } from "@/services/api/users/curatedUserResourcesService";
import { ButtonVariant } from "@/types";
import { CreateUserResource, LinkType, ResourceEntry, TaskType, TransferOption } from "@/types/models/userResources";
import useFormatMsg from "@/utils/useFormatMsg";
import {
    Box,
    Button,
    Divider,
    Flex,
    FormControl,
    FormHelperText,
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
    logo: "",
    summary: "",
    coverage: "",
    keywords: "",
    tags: "",
    type: "",
    customAccessPolicy: "",
    accessPolicyName: "",
    accessPolicyCode: "",
    dashboardContents: "",
    organisation: "",
    organisationTitle: "",
    organisationEmail: "",
    organisationContact: "",
    organisationContactEmail: "",
    customSource: "",
    sourceTerm: "",
    sourceCode: "",
};

const defaultLinkState = {
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
    isUpdate?: boolean;
};

export const AddEntity = ({ isUpdate = false }: Props) => {
    //enums
    const [types, setTypes] = useState<any[]>([]);
    const [accessPolicies, setAccessPolicies] = useState<any[]>([]);
    const [organisations, setOrganisations] = useState<any[]>([]);
    const [sources, setSources] = useState<any[]>([]);
    const [endpoints, setEndpoints] = useState<any[]>([]);
    const [urlTypes, setUrlTypes] = useState<any[]>([]);
    const [protocols, setProtocols] = useState<any[]>([]);

    //states
    const [formData, setFormData] = useState(defaultFormState);
    const [links, setLinks] = useState([defaultLinkState]);
    const [coverageError, setCoverageError] = useState(false);
    const [defaultEntryVal, setDefaultEntryVal] = useState<any>();

    const session = useSession();
    const router = useRouter();
    const { translate } = useFormatMsg();
    const validCoverage = /\[\d+,\s*\d+\],\[\d+,\s*\d+\]/;
    const headingActions = [
        {
            titleId: "pages.entryResources.return",
            onClick: () => router.push("/entry-resources"),
            disabled: false,
        },
    ];

    useEffect(() => {
        if (!router.isReady) return;
        getSelectOptions();
        if (isUpdate) {
            getEntry();
        }
    }, [router.isReady]);

    const getSelectOptions = async () => {
        try {
            const typesVal = localStorage.getItem("types");
            if (typesVal && typesVal?.length > 0) {
                const types: any[] = JSON.parse(typesVal);
                setTypes(types);
            } else {
                const newTypes = await UserResourcesService.getTypes();
                localStorage.setItem("types", JSON.stringify(newTypes));
                setTypes(newTypes);
            }

            const accessPoliciesVal = localStorage.getItem("accessPolicies");
            if (accessPoliciesVal && accessPoliciesVal?.length > 0) {
                const accessPolicies: any[] = JSON.parse(accessPoliciesVal);
                setAccessPolicies(accessPolicies);
            } else {
                const newAccessPolicies = await UserResourcesService.getAccessPolicies();
                localStorage.setItem("accessPolicies", JSON.stringify(newAccessPolicies));
                setAccessPolicies(newAccessPolicies);
            }

            const organisationsVal = localStorage.getItem("organisations");
            if (organisationsVal && organisationsVal?.length > 0) {
                const organisations: any[] = JSON.parse(organisationsVal);
                setOrganisations(organisations);
            } else {
                const newOrganisations = await UserResourcesService.getOrganisations();
                localStorage.setItem("organisations", JSON.stringify(newOrganisations));
                setOrganisations(newOrganisations);
            }

            const sourcesVal = localStorage.getItem("sources");
            if (sourcesVal && sourcesVal?.length > 0) {
                let sources: any[] = JSON.parse(sourcesVal);
                setSources(sources);
            } else {
                const newSources = await UserResourcesService.getSources();
                localStorage.setItem("sources", JSON.stringify(newSources));
                setSources(newSources);
            }

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
        } catch (e) {
            console.log(e);
        }
    };

    const getEntry = async () => {
        const entryId: number = Number(router.query.entryId) || 0;
        try {
            const response = await UserResourcesService.getEntry(entryId);
            const entry = response[0].entry;
            setDefaultEntryVal(entry);
            
            //entry data
            let data = { ...defaultFormState };
            data.title = entry.title;
            data.summary = entry.summary;
            data.logo = entry.logo;
            data.coverage = entry.coverage;
            data.sourceCode = entry.source.code;
            data.sourceTerm = entry.source.term;
            data.dashboardContents = entry.dashboardContents?.content;
            if (entry.accessPolicy.isCustom === 1) {
                data.customAccessPolicy = entry.accessPolicy.name;
                data.accessPolicyName = "customAccessPolicy";
            } else {
                data.accessPolicyName = entry.accessPolicy.name;
            }
            data.accessPolicyCode = entry.accessPolicy.code;
            data.keywords = entry.keywords;
            data.tags = entry.tags;
            if (entry.organisation.isCustom === 1) {
                data.organisation = "customOrganisation";
            } else {
                data.organisation = entry.organisation.title;
            }
            data.organisationContact = entry.organisation.contact;
            data.organisationContactEmail = entry.organisation.contactEmail;
            data.organisationEmail = entry.organisation.email;
            data.organisationTitle = entry.organisation.title;
            if (entry.source.isCustom === 1) {
                data.customSource = entry.source.term;
                data.sourceTerm = "customSource";
            } else {
                data.sourceTerm = entry.source.term;
            }
            data.sourceCode = entry.source.code;
            data.type = entry.type.code;

            //links
            let links: any = [];
            response.forEach((transferOption) => {
                let link = { ...defaultLinkState };
                link.title = transferOption.title;
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
        } catch (e) {
            console.log(e);
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

    const handleSubmit = async (e: any) => {
        e.preventDefault();
        if (!validCoverage.test(formData.coverage)) {
            setCoverageError(true);
        } else {
            setCoverageError(false);
            try {
                if (isUpdate) {
                    const body = createEntryBody();
                    await UserResourcesService.updateEntry(body, Number(router.query.entryId));
                } else {
                    const body = createResourceBody();
                    await UserResourcesService.createResource(body);
                }
            } catch (e) {
                console.log(e);
            } finally {
                router.push("/entry-resources");
            }
        }
    };

    const createResourceBody = () => {
        return {
            //@ts-ignore
            userId: session?.data?.userId || "",
            entryName: formData.title,
            taskType: TaskType.CREATE,
            entry: {
                title: formData.title,
                summary: formData.summary,
                logo: formData.logo,
                coverage: formData.coverage,
                type: formData.type,
                dashboardContents: {
                    content: formData.dashboardContents,
                },
                accessPolicy: {
                    name:
                        formData.accessPolicyName === "customAccessPolicy"
                            ? formData.customAccessPolicy
                            : formData.accessPolicyName,
                    code:
                        formData.accessPolicyName === "customAccessPolicy"
                            ? formData.customAccessPolicy
                            : formData.accessPolicyName,
                },
                keywords: formData.keywords,
                tags: formData.tags,
                code: "string",
                organisation: {
                    title: formData.organisationTitle,
                    email: formData.organisationEmail,
                    contact: formData.organisationContact,
                    contactEmail: formData.organisationContactEmail,
                },
                source: {
                    term: formData.sourceTerm === "customSource" ? formData.customSource : formData.sourceTerm,
                    code: formData.sourceTerm === "customSource" ? formData.customSource : formData.sourceCode,
                },
                dataSource: "dab",
                displayDataSource: "dab",
                definitionType: "0",
                //@ts-ignore
                userId: session?.data?.userId || "",
                transferOptions: getTransferOptions(),
            },
        };
    };

    const createEntryBody = () => {
        return {
            title: formData.title,
            summary: formData.summary,
            logo: formData.logo,
            coverage: formData.coverage,
            type: formData.type,
            dashboardContents: {
                content: formData.dashboardContents,
            },
            accessPolicy: {
                name:
                    formData.accessPolicyName === "customAccessPolicy"
                        ? formData.customAccessPolicy
                        : formData.accessPolicyName,
                code:
                    formData.accessPolicyName === "customAccessPolicy"
                        ? formData.customAccessPolicy
                        : formData.accessPolicyName,
            },
            keywords: formData.keywords,
            tags: formData.tags,
            code: defaultEntryVal?.code || "",
            organisation: {
                title: formData.organisationTitle,
                email: formData.organisationEmail,
                contact: formData.organisationContact,
                contactEmail: formData.organisationContactEmail,
            },
            source: {
                term: formData.sourceTerm === "customSource" ? formData.customSource : formData.sourceTerm,
                code: formData.sourceTerm === "customSource" ? formData.customSource : formData.sourceCode,
            },
            dataSource: "dab",
            displayDataSource: "dab",
            definitionType: "0",
            //@ts-ignore
            userId: session?.data?.userId || "",
            transferOptions: getTransferOptions(),
        };
    };

    const getTransferOptions = () => {
        const transferOptions: LinkType[] = [];
        links.forEach((link: any) => {
            transferOptions.push({
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

    const generateEntryContentForm = () => {
        return (
            <Grid templateColumns={{ base: "1fr", lg: "repeat(2, 1fr)" }} gap={3} pt={3}>
                <FormControl isRequired>
                    <FormLabel fontSize={14} htmlFor="title">
                        Title
                    </FormLabel>
                    <Input
                        disabled={isUpdate}
                        type="text"
                        id="title"
                        name="title"
                        value={formData.title}
                        onChange={handleInputChange}
                    ></Input>
                </FormControl>
                <FormControl>
                    <FormLabel fontSize={14} htmlFor="logo" display="flex" alignItems="center" gap={1}>
                        Logo{" "}
                        <Tooltip label="tessst" shouldWrapChildren placement="top">
                            <TutorialIcon color="#0661A9" width={15} height={15} />
                        </Tooltip>
                    </FormLabel>
                    <Input type="text" id="logo" name="logo" value={formData.logo} onChange={handleInputChange}></Input>
                </FormControl>
                <FormControl>
                    <FormLabel fontSize={14} htmlFor="summary">
                        Summary
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
                <Flex direction="column" gap={3}>
                    <FormControl isRequired>
                        <FormLabel fontSize={14} htmlFor="coverage" display="flex" alignItems="center" gap={1}>
                            Coverage{" "}
                            <Tooltip label="tessst" shouldWrapChildren placement="top">
                                <TutorialIcon color="#0661A9" width={15} height={15} />
                            </Tooltip>
                        </FormLabel>
                        <Input
                            type="text"
                            id="coverage"
                            name="coverage"
                            value={formData.coverage}
                            onChange={handleInputChange}
                            placeholder="[-180,90],[180,-90]"
                        ></Input>
                        {coverageError && (
                            <FormHelperText color="red">
                                Incorrect coverage format. Use the format from the example.
                            </FormHelperText>
                        )}
                    </FormControl>
                    <FormControl gridColumn={2} gridRow={3} isRequired>
                        <FormLabel fontSize={14} htmlFor="keywords" display="flex" alignItems="center" gap={1}>
                            Keywords{" "}
                            <Tooltip label="tessst" shouldWrapChildren placement="top">
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
                            Tags{" "}
                            <Tooltip label="tessst" shouldWrapChildren placement="top">
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

    const generateAdditionalInfoForm = () => {
        return (
            <Grid templateColumns={{ base: "1fr", lg: "repeat(2, 1fr)" }} gap={3} pt={3}>
                <FormControl isRequired>
                    <FormLabel fontSize={14} htmlFor="type">
                        Type
                    </FormLabel>
                    <Select
                        id="type"
                        name="type"
                        value={formData.type}
                        onChange={handleInputChange}
                        placeholder="Select type"
                    >
                        {types.map((type: any) => {
                            return (
                                <option key={type.id} value={type.code}>
                                    {type.name}
                                </option>
                            );
                        })}
                    </Select>
                </FormControl>
                <FormControl isRequired>
                    <FormLabel fontSize={14} htmlFor="accessPolicy">
                        Access Policy
                    </FormLabel>
                    <Select
                        id="accessPolicy"
                        name="accessPolicy"
                        value={formData.accessPolicyName}
                        placeholder="Select access policy"
                        onChange={(event: any) => {
                            const { value } = event.target;
                            const policy = accessPolicies.find((policy) => policy.name === value);
                            setFormData({
                                ...formData,
                                accessPolicyName: value,
                                accessPolicyCode: policy ? policy.code : "",
                            });
                        }}
                    >
                        {accessPolicies.map((accessPolicy: any) => {
                            return (
                                <option key={accessPolicy.id} value={accessPolicy.code}>
                                    {accessPolicy.name}
                                </option>
                            );
                        })}
                        <option value="customAccessPolicy">Custom access policy</option>
                    </Select>
                    {formData.accessPolicyName === "customAccessPolicy" && (
                        <Input
                            type="text"
                            mt={1}
                            placeholder="Custom access policy"
                            id="customAccessPolicy"
                            name="customAccessPolicy"
                            value={formData.customAccessPolicy}
                            onChange={handleInputChange}
                        />
                    )}
                </FormControl>
                <FormControl>
                    <FormLabel fontSize={14} htmlFor="dashboardContents">
                        Dashboard contents
                    </FormLabel>
                    <Textarea
                        id="dashboardContents"
                        name="dashboardContents"
                        value={formData.dashboardContents}
                        onChange={handleInputChange}
                        h="80px"
                        maxH="300px"
                    ></Textarea>
                </FormControl>
                <Flex direction="column" gap={3}>
                    <FormControl isRequired>
                        <FormLabel fontSize={14} htmlFor="organisation">
                            Organisation
                        </FormLabel>
                        <Select
                            id="organisation"
                            name="organisation"
                            value={formData.organisation}
                            onChange={(event: any) => {
                                const { value } = event.target;
                                const organisation = organisations.find((org) => org.title === value);
                                setFormData({
                                    ...formData,
                                    organisation: value,
                                    organisationTitle: value === "customOrganisation" ? "" : value,
                                    organisationEmail: organisation ? organisation.email : "",
                                    organisationContact: organisation ? organisation.contact : "",
                                    organisationContactEmail: organisation ? organisation.contactEmail : "",
                                });
                            }}
                            placeholder="Select organisation"
                        >
                            {organisations.map((organisation: any) => {
                                return (
                                    <option key={organisation.id} value={organisation.title}>
                                        {organisation.title}
                                    </option>
                                );
                            })}
                            <option value="customOrganisation">Custom organisation</option>
                        </Select>
                        {formData.organisation === "customOrganisation" && (
                            <>
                                <Input
                                    type="text"
                                    id="organisationTitle"
                                    name="organisationTitle"
                                    value={formData.organisationTitle}
                                    onChange={handleInputChange}
                                    placeholder="Organisation title"
                                    mb={1}
                                    mt={1}
                                />
                                <Input
                                    type="text"
                                    id="organisationEmail"
                                    name="organisationEmail"
                                    value={formData.organisationEmail}
                                    onChange={handleInputChange}
                                    placeholder="Organisation email"
                                    mb={1}
                                />
                                <Input
                                    type="text"
                                    id="organisationContact"
                                    name="organisationContact"
                                    value={formData.organisationContact}
                                    onChange={handleInputChange}
                                    placeholder="Organisation contact"
                                    mb={1}
                                />
                                <Input
                                    type="text"
                                    id="organisationContactEmail"
                                    name="organisationContactEmail"
                                    value={formData.organisationContactEmail}
                                    onChange={handleInputChange}
                                    placeholder="Organisation contact email"
                                />
                            </>
                        )}
                    </FormControl>
                    <FormControl isRequired>
                        <FormLabel fontSize={14} htmlFor="source">
                            Source
                        </FormLabel>
                        <Select
                            id="source"
                            name="source"
                            value={formData.sourceTerm}
                            onChange={(event: any) => {
                                const { value } = event.target;
                                const source = sources.find((src) => src.term === value);
                                setFormData({
                                    ...formData,
                                    sourceTerm: value,
                                    sourceCode: source ? source.code : "",
                                });
                            }}
                            placeholder="Select source"
                        >
                            {sources.map((source: any) => {
                                return (
                                    <option key={source.id} value={source.term}>
                                        {source.term}
                                    </option>
                                );
                            })}
                            <option value="customSource">Custom source</option>
                        </Select>
                        {formData.sourceTerm === "customSource" && (
                            <Input
                                type="text"
                                id="customSource"
                                name="customSource"
                                value={formData.customSource}
                                onChange={handleInputChange}
                                mt={1}
                                placeholder="Custom source"
                            />
                        )}
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
                                Title
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
                                Name
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
                            Description
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
                                Endpoint
                            </FormLabel>
                            <Select
                                id="endpoint"
                                name="endpoint"
                                value={links[index].endpoint}
                                placeholder="Select endpoint"
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
                                <option value="customEndpoint">Custom endpoint</option>
                            </Select>
                            {links[index].endpoint === "customEndpoint" && (
                                <>
                                    <Input
                                        type="text"
                                        id="customEndpoint"
                                        name="customEndpoint"
                                        value={links[index].customEndpoint}
                                        onChange={(event: any) => handleLinksInputChange(event, index)}
                                        placeholder="Endpoint URL parameter"
                                        mt={1}
                                        mb={1}
                                    />
                                    <Select
                                        id="endpointType"
                                        name="endpointType"
                                        value={links[index].endpointType}
                                        placeholder="Select endpointType"
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
                                Protocol
                            </FormLabel>
                            <Select
                                id="protocol"
                                name="protocol"
                                value={links[index].protocol}
                                placeholder="Select protocol"
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
                                <option value="customProtocol">Custom protocol</option>
                            </Select>
                            {links[index].protocol === "customProtocol" && (
                                <Input
                                    type="text"
                                    id="customProtocol"
                                    name="customProtocol"
                                    value={links[index].customProtocol}
                                    onChange={(event: any) => handleLinksInputChange(event, index)}
                                    mt={1}
                                    placeholder="Custom protocol"
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

    return (
        <Flex direction="column" w={"100%"} overflowY="auto">
            <MainContentHeader
                titleId={
                    isUpdate
                        ? "pages.entryResources.addEntityPage.updateTitle"
                        : "pages.entryResources.addEntityPage.title"
                }
                actions={headingActions}
            />
            <Flex direction="column" w={"100%"} padding={4}>
                <form onSubmit={handleSubmit}>
                    {renderSectionDivider("pages.entryResources.addEntityPage.entryContent")}
                    {generateEntryContentForm()}
                    {renderSectionDivider("pages.entryResources.addEntityPage.additionalInfo")}
                    {generateAdditionalInfoForm()}
                    {renderSectionDivider("pages.entryResources.addEntityPage.linkOptions", true)}
                    {links.map((link: any, index: number) => generateLinkOption(index))}
                    <Flex direction="row" w="100%" justifyContent="flex-end" pt={6}>
                        <Button type="submit" colorScheme="blue" w={"120px"} h="2em">
                            {isUpdate
                                ? translate("pages.entryResources.updateEntry")
                                : translate("pages.entryResources.addEntityPage.submitButton")}
                        </Button>
                    </Flex>
                </form>
            </Flex>
        </Flex>
    );
};
