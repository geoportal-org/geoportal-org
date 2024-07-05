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
    ModalOverlay,
    Modal,
    ModalHeader,
    ModalBody,
    ModalCloseButton,
    ModalContent,
    ModalFooter,
    Input,
    FormControl,
    FormLabel,
    Image,
    Select,
} from "@chakra-ui/react";
import React, { useContext, useEffect, useRef, useState } from "react";
import { MainContentHeader } from "../MainContent/MainContentHeader";
import { SitesService } from "@/services/api/SitesService";
import { SiteData } from "@/types/models/sites";
import { createSelectItemsList, getIdFromUrl } from "@/utils/helpers";
import { LocaleNames, ToastStatus } from "@/types";
import { SiteContext, SiteContextValue } from "@/context/CurrentSiteContext";
import { FileRepositoryService } from "@/services/api";
import { acceptedLogoExtensions, initRepositoryPagination } from "@/data";
import { useIntl } from "react-intl";

const defaultData = {
    name: "",
    url: "",
};

const SitesManager = () => {
    const { translate } = useFormatMsg();
    const [sites, setSites] = useState<SiteData[]>([]);
    const [documentsList, setDocumentsList] = useState<any[]>();
    const [confirmDeleteModalOpen, setConfirmDeleteModalOpen] = useState<boolean>(false);
    const [currentSiteId, setCurrentSiteId] = useState("");
    const [currentLogoId, setCurrentLogoId] = useState(0);
    const [modalOpen, setModalOpen] = useState<boolean>(false);
    const [editMode, setEditMode] = useState<boolean>(false);
    const [siteData, setSiteData] = useState(defaultData);
    const [imageFile, setImageFile] = useState<File>();
    const [imageURL, setImageURL] = useState<string>("");
    const fileInputRef = useRef(null);
    const { locale } = useIntl();

    const { showToast } = useCustomToast();
    const { setAllSites } = useContext<SiteContextValue>(SiteContext);

    useEffect(() => {
        getSitesList();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    useEffect(() => {
        getDocumentsList();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [currentSiteId]);

    useEffect(() => {
        if (documentsList && documentsList?.length > 0) {
            setModalOpen(true);
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [documentsList]);

    const headingActions = [
        {
            titleId: "pages.sites.addSite",
            onClick: () => setModalOpen(true),
            disabled: false,
        },
    ];

    const getSitesList = async () => {
        const res = await SitesService.getSitesWithoutGlobal();
        setSites(res._embedded.site);
    };

    const getAllSitesList = async () => {
        const res = await SitesService.getSites();
        setAllSites(res._embedded.site);
    };

    const getDocumentsList = async () => {
        try {
            const {
                _embedded: { document },
            } = await FileRepositoryService.getDocumentsListBySiteId({
                ...initRepositoryPagination,
                siteId: currentSiteId,
            });
            const selectDocumentsList = createSelectItemsList(
                document.filter(({ extension }) => acceptedLogoExtensions.includes(extension)),
                false,
                locale as LocaleNames
            );
            setDocumentsList(() => selectDocumentsList.options);
        } catch (e: any) {
            showToast({
                title: translate("general.error"),
                description: translate("pages.sites.siteCreationFail") + " " + e.errorInfo.errors[0].message,
                status: ToastStatus.ERROR,
            });
        }
    };

    const handleModalSubmit = async (e: any) => {
        e.preventDefault();
        try {
            if (editMode) {
                const body = {
                    ...siteData,
                    logoDocumentId: currentLogoId,
                };
                await SitesService.updateSite(body, currentSiteId);
                showToast({
                    title: translate("general.updated"),
                    description: translate("pages.sites.siteUpdatedMsg") + " ID: " + currentSiteId,
                });
            } else {
                const formData = new FormData();
                formData.append("model", JSON.stringify(siteData));
                const file = imageFile as File;
                formData.append("files", file, file.name);
                await SitesService.createSite(formData);
                showToast({
                    title: translate("general.created"),
                    description: translate("pages.sites.siteCreatedMsg"),
                });
            }
        } catch (e: any) {
            showToast({
                title: translate("general.error"),
                description:
                    translate(`${editMode ? "pages.sites.siteUpdateFail" : "pages.sites.siteCreationFail"}`) +
                    " " +
                    e?.errorInfo?.errors[0]?.message,
                status: ToastStatus.ERROR,
            });
        }
        refresh();
    };

    const deleteSite = async () => {
        try {
            await SitesService.deleteSite(currentSiteId);
            setConfirmDeleteModalOpen(false);
            showToast({
                title: translate("general.deleted"),
                description: translate("pages.sites.siteDeletedMsg") + " ID: " + currentSiteId,
            });
        } catch (e: any) {
            showToast({
                title: translate("general.error"),
                description: translate("pages.sites.siteDeleteFail") + " " + e?.errorInfo?.errors[0]?.message,
                status: ToastStatus.ERROR,
            });
        }
        refresh();
    };

    const handleInputChange = (e: any) => {
        const { name, value } = e.target;
        setSiteData({
            ...siteData,
            [name]: value,
        });
    };

    const handleImageChange = (event: any) => {
        const file = event.target.files[0];
        if (file) {
            setImageFile(file);
            // Create a URL for the image preview
            const url = URL.createObjectURL(file);
            setImageURL(url);
        }
    };

    const handleImageSelectChange = (event: any) => {
        setCurrentLogoId(event.target.value);
    };

    const refresh = () => {
        setCurrentSiteId("");
        setSiteData(defaultData);
        setModalOpen(false);
        setEditMode(false);
        setImageFile(undefined);
        setImageURL("");
        getSitesList();
        getAllSitesList();
    };

    return (
        <Flex direction="column" w={"100%"} overflowY="auto">
            <MainContentHeader titleId="nav.extensions.section.sites" actions={headingActions} />
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
                                    {translate("pages.sites.id")}
                                </Th>
                                <Th textTransform="none" fontSize={"14px"}>
                                    {translate("pages.sites.name")}
                                </Th>
                                <Th textTransform="none" fontSize={"14px"}>
                                    {translate("pages.sites.url")}
                                </Th>
                                <Th textTransform="none" fontSize={"14px"}>
                                    {translate("pages.sites.modifiedOn")}
                                </Th>
                                <Th textTransform="none" fontSize={"14px"} textAlign="end" pr="6">
                                    {translate("pages.entryResources.actions")}
                                </Th>
                            </Tr>
                        </Thead>
                        <Tbody>
                            {sites &&
                                sites.map((site: SiteData) => {
                                    return (
                                        <Tr key={getIdFromUrl(site._links.self.href)}>
                                            <Td>{getIdFromUrl(site._links.self.href)}</Td>
                                            <Td>{site.name}</Td>
                                            <Td>{site.url}</Td>
                                            <Td>{site.modifiedOn}</Td>
                                            <Td textAlign="end">
                                                <Menu>
                                                    <MenuButton
                                                        size="sm"
                                                        colorScheme={"teal"}
                                                        as={Button}
                                                        _hover={{}}
                                                        rightIcon={<ChevronDownIcon />}
                                                    >
                                                        {translate("pages.entryResources.actions")}
                                                    </MenuButton>
                                                    <MenuList>
                                                        <>
                                                            <MenuItem
                                                                onClick={() => {
                                                                    setEditMode(true);
                                                                    setCurrentSiteId(
                                                                        getIdFromUrl(site._links.self.href)
                                                                    );
                                                                    setCurrentLogoId(site.logoDocumentId);
                                                                    setSiteData({
                                                                        name: site.name,
                                                                        url: site.url,
                                                                    });
                                                                }}
                                                            >
                                                                {translate("pages.sites.editSite")}
                                                            </MenuItem>
                                                            <MenuItem
                                                                onClick={() => {
                                                                    setCurrentSiteId(
                                                                        getIdFromUrl(site._links.self.href)
                                                                    );
                                                                    setConfirmDeleteModalOpen(true);
                                                                }}
                                                            >
                                                                {translate("pages.sites.deleteSite")}
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
            </Flex>
            <Modal
                size="lg"
                blockScrollOnMount={false}
                isOpen={modalOpen}
                onClose={() => {
                    setSiteData(defaultData);
                    setModalOpen(false);
                    setEditMode(false);
                    setImageFile(undefined);
                    setImageURL("");
                }}
                isCentered
            >
                <ModalOverlay />
                <ModalContent>
                    <ModalHeader>
                        {editMode ? translate("pages.sites.editSite") : translate("pages.sites.addSite")}
                    </ModalHeader>
                    <ModalCloseButton />
                    <form onSubmit={handleModalSubmit}>
                        <ModalBody>
                            <FormControl isRequired mb={4}>
                                <FormLabel fontSize={14} htmlFor="name" mb={0}>
                                    {translate("pages.sites.name")}
                                </FormLabel>
                                <Input
                                    type="text"
                                    id="name"
                                    name="name"
                                    value={siteData.name}
                                    onChange={(event: any) => handleInputChange(event)}
                                />
                            </FormControl>
                            <FormControl isRequired mb={4}>
                                <FormLabel fontSize={14} htmlFor="url" mb={0}>
                                    {translate("pages.sites.url")}
                                </FormLabel>
                                <Input
                                    type="text"
                                    id="url"
                                    name="url"
                                    value={siteData.url}
                                    onChange={(event: any) => handleInputChange(event)}
                                    mt={1}
                                />
                            </FormControl>
                            <FormControl isRequired>
                                <FormLabel fontSize={14} htmlFor="image" mb={0}>
                                    {translate("pages.sites.image")}
                                </FormLabel>
                                {editMode ? (
                                    <Select
                                        id="type"
                                        name="type"
                                        variant="outline"
                                        style={{ border: "none", borderBottom: "1px solid", borderRadius: "0" }}
                                        _focus={{ border: "none", outline: "none", boxShadow: "none" }}
                                        colorScheme={"blue"}
                                        width="full"
                                        value={currentLogoId}
                                        onChange={handleImageSelectChange}
                                    >
                                        {documentsList?.map((doc: any) => {
                                            return (
                                                <option style={{ color: "black" }} key={doc.value} value={doc.value}>
                                                    {doc.label}
                                                </option>
                                            );
                                        })}
                                    </Select>
                                ) : (
                                    <Flex w="full" direction={"column"} alignItems="center" gap={2}>
                                        <Button
                                            h="30px"
                                            w="full"
                                            colorScheme={"blue"}
                                            onClick={() => {
                                                //@ts-ignore
                                                fileInputRef.current?.click();
                                            }}
                                        >
                                            {translate("pages.sites.addImage")}
                                        </Button>{" "}
                                        <Input
                                            ref={fileInputRef}
                                            display="none"
                                            w="full"
                                            type="file"
                                            alignContent="center"
                                            justifyContent="start"
                                            id="image"
                                            name="image"
                                            accept="image/*"
                                            onChange={handleImageChange}
                                        />
                                        {imageURL && (
                                            <Image
                                                src={imageURL}
                                                alt="Selected Image"
                                                boxSize="300px"
                                                objectFit="cover"
                                            />
                                        )}
                                    </Flex>
                                )}
                            </FormControl>
                        </ModalBody>

                        <ModalFooter gap={2}>
                            <Button colorScheme={"blue"} onClick={() => setModalOpen(false)}>
                                {translate("pages.recommendations.cancelButton")}
                            </Button>
                            <Button colorScheme={"green"} type="submit">
                                {translate("pages.sites.confirm")}
                            </Button>
                        </ModalFooter>
                    </form>
                </ModalContent>
            </Modal>
            <Modal
                size="lg"
                blockScrollOnMount={false}
                isOpen={confirmDeleteModalOpen}
                onClose={() => {
                    setConfirmDeleteModalOpen(false);
                }}
                isCentered
            >
                <ModalOverlay />
                <ModalContent>
                    <ModalHeader>{translate("pages.sites.deleteSite")}</ModalHeader>
                    <ModalCloseButton />
                    <ModalBody>
                        <Text fontSize={15}>
                            {translate("pages.sites.confirmDeleteMsg")} {currentSiteId}
                            {"?"}
                        </Text>
                    </ModalBody>

                    <ModalFooter gap={2}>
                        <Button colorScheme={"blue"} onClick={() => setConfirmDeleteModalOpen(false)}>
                            {translate("pages.recommendations.cancelButton")}
                        </Button>
                        <Button colorScheme={"red"} onClick={deleteSite}>
                            {translate("pages.sites.delete")}
                        </Button>
                    </ModalFooter>
                </ModalContent>
            </Modal>
        </Flex>
    );
};

export default SitesManager;
