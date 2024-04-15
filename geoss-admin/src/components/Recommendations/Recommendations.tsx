import { RecommendationsService } from "@/services/api/settings/RecommendationsService";
import {
    RecommendationData,
    RecommendationEntityData,
    SavedRecommendationData,
} from "@/types/models/recommendations";
import {
    Alert,
    AlertIcon,
    Button,
    Divider,
    Flex,
    FormControl,
    FormLabel,
    Input,
    Modal,
    ModalBody,
    ModalCloseButton,
    ModalContent,
    ModalFooter,
    ModalHeader,
    ModalOverlay,
    Text,
    useBreakpointValue,
} from "@chakra-ui/react";
import React, { useEffect, useState } from "react";
import { MainContentHeader } from "../MainContent/MainContentHeader";
import useFormatMsg from "@/utils/useFormatMsg";
import { AddIcon } from "@chakra-ui/icons";
import RecommendationRow from "./RecommendationRow";
import PagesControls from "../PagesControls/PagesControls";
import EntityRow from "./EntityRow";
import { initialAddFormValue, initialNewEntity, initialPagesInfo } from "./DefaultValues";
import { PagesInfo } from "@/types/models/page";

interface addForm {
    keywords: string;
    name: string;
    ds: string;
    entity: string;
    weight: number;
}

export const RecommendationsConfig = () => {
    const [modalOpen, setModalOpen] = useState(false);
    const [addFormData, setAddFormData] = useState<addForm>(initialAddFormValue);
    const [recommendations, setRecommendations] = useState<SavedRecommendationData[]>([]);
    const [pagesInfo, setPagesInfo] = useState<PagesInfo>(initialPagesInfo);
    const [currentRecommendationData, setCurrentRecommendationData] = useState<any>();
    const [newEntity, setNewEntity] = useState<RecommendationEntityData>(initialNewEntity);
    const { translate } = useFormatMsg();

    //errors
    const [addFormError, setAddFormError] = useState<boolean>(false);
    const [addEntityError, setAddEntityError] = useState<boolean>(false);

    const direction = useBreakpointValue({ base: "column", md: "row" }) as any;

    useEffect(() => {
        fetchRecommendations();
    }, []);

    const handleModal = (id: number) => {
        setModalOpen(true);
        setCurrentRecommendationData(recommendations.find((e) => e.id === id));
    };

    const fetchRecommendations = async (page = 0) => {
        try {
            let response = await RecommendationsService.getRecommendations(page);
            setRecommendations(response._embedded.recommendationModels);
            setPagesInfo(response.page);
        } catch (e) {
            console.log(e);
        }
    };

    const addRecommendation = async () => {
        if (
            addFormData.ds === "" ||
            addFormData.entity === "" ||
            addFormData.keywords === "" ||
            addFormData.name === "" ||
            addFormData.weight < 0
        ) {
            setAddFormError(true);
        } else {
            setAddFormError(false);
            const keywords = addFormData.keywords.split(",");
            let body: RecommendationData = {
                entities: [
                    {
                        dataSourceCode: addFormData.ds,
                        entityCode: addFormData.entity,
                        name: addFormData.name,
                        orderWeight: addFormData.weight,
                    },
                ],
                keywords: keywords,
            };
            try {
                await RecommendationsService.createRecommendation(body);
                fetchRecommendations(pagesInfo.number - 1);
            } catch (e) {
                console.log(e);
            }
        }
    };

    const deleteRecommendation = async (id: number) => {
        try {
            await RecommendationsService.deleteRecommendation(id);
            fetchRecommendations(pagesInfo.number - 1);
        } catch (e) {
            console.log(e);
        }
    };

    const modifyKeywords = async () => {
        let keywords = null;
        if (Array.isArray(currentRecommendationData.keywords)) {
            keywords = currentRecommendationData.keywords;
        } else {
            keywords = currentRecommendationData?.keywords.split(",");
        }
        try {
            await RecommendationsService.updateKeywordsForRecommendation(currentRecommendationData.id, keywords);
            fetchRecommendations(pagesInfo.number - 1);
            setModalOpen(false);
        } catch (e) {
            console.log(e);
        }
    };

    const addEntity = async (id: number) => {
        if (
            newEntity.dataSourceCode === "" ||
            newEntity.entityCode === "" ||
            newEntity.name === "" ||
            newEntity.orderWeight < 0
        ) {
            setAddEntityError(true);
        } else {
            setAddEntityError(false);
            let entityArray = [];
            entityArray.push(newEntity);
            try {
                await RecommendationsService.addEntityForRecommendation(id, entityArray);
                fetchRecommendations(pagesInfo.number - 1);
                setModalOpen(false);
                setNewEntity(initialNewEntity);
            } catch (e) {
                console.log(e);
            }
        }
    };

    const deleteEntityFromRecommendation = async (recommendationId: number, entityId: number) => {
        try {
            await RecommendationsService.deleteEntityForRecommendation(recommendationId, entityId);
            fetchRecommendations(pagesInfo.number - 1);
            setModalOpen(false);
        } catch (e) {
            console.log(e);
        }
    };

    const updateEntityForRecommendation = async (recommendationId: number, entityData: any) => {
        try {
            await RecommendationsService.updateEntity(recommendationId, entityData.id, entityData);
            fetchRecommendations(pagesInfo.number - 1);
            setModalOpen(false);
        } catch (e) {
            console.log(e);
        }
    };

    //handlers

    const handleAddFormChange = (event: any) => {
        const name = event.target.name;
        const value = event.target.value;
        setAddFormData((values) => ({ ...values, [name]: value }));
    };

    const handleModalChange = (event: any) => {
        const name = event.target.name;
        const value = event.target.value;
        setCurrentRecommendationData((values: any) => ({ ...values, [name]: value }));
    };

    const handleNewEntityChange = (event: any) => {
        const name = event.target.name;
        const value = event.target.value;
        setNewEntity((values) => ({ ...values, [name]: value }));
    };

    const onSubmit = (e: any) => {
        e.preventDefault();
        addRecommendation();
        setAddFormData(initialAddFormValue);
    };

    return (
        <Flex direction="column" overflowY="auto">
            <MainContentHeader titleId="nav.settings.section.recommendations" />
            <Text fontSize="xl" pl="4" mt="6" color="grey">
                {translate("pages.recommendations.add")}
            </Text>
            <form onSubmit={onSubmit}>
                <Flex
                    p="4"
                    direction={direction}
                    justifyContent="space-between"
                    gap={useBreakpointValue({ base: 3, md: 0 })}
                >
                    <FormControl id="keywords" width={useBreakpointValue({ base: "100%", md: "15%" })}>
                        <FormLabel fontSize="sm">{translate("pages.recommendations.keywordsComma")}</FormLabel>
                        <Input
                            id="keywords"
                            name="keywords"
                            variant="flushed"
                            placeholder={translate("general.type")}
                            value={addFormData.keywords}
                            onChange={handleAddFormChange}
                        />
                    </FormControl>
                    <FormControl width={useBreakpointValue({ base: "100%", md: "15%" })}>
                        <FormLabel fontSize="sm">{translate("pages.recommendations.name")}</FormLabel>
                        <Input
                            id="name"
                            name="name"
                            variant="flushed"
                            placeholder={translate("general.type")}
                            value={addFormData.name}
                            onChange={handleAddFormChange}
                        />
                    </FormControl>
                    <FormControl width={useBreakpointValue({ base: "100%", md: "15%" })}>
                        <FormLabel fontSize="sm">{translate("pages.recommendations.ds")}</FormLabel>
                        <Input
                            id="ds"
                            name="ds"
                            variant="flushed"
                            placeholder={translate("general.type")}
                            value={addFormData.ds}
                            onChange={handleAddFormChange}
                        />
                    </FormControl>
                    <FormControl width={useBreakpointValue({ base: "100%", md: "15%" })}>
                        <FormLabel fontSize="sm">{translate("pages.recommendations.entity")}</FormLabel>
                        <Input
                            id="entity"
                            name="entity"
                            variant="flushed"
                            placeholder={translate("general.type")}
                            value={addFormData.entity}
                            onChange={handleAddFormChange}
                        />
                    </FormControl>
                    <FormControl width={useBreakpointValue({ base: "100%", md: "15%" })}>
                        <FormLabel fontSize="sm">{translate("pages.recommendations.weight")}</FormLabel>
                        <Input
                            type="number"
                            min={0}
                            id="weight"
                            name="weight"
                            variant="flushed"
                            placeholder={translate("general.type")}
                            value={addFormData.weight}
                            onChange={handleAddFormChange}
                        />
                    </FormControl>
                </Flex>
                {addFormError && (
                    <Alert borderRadius="10" border="none" height="10" m="4" status="error" variant="solid">
                        <AlertIcon />
                        {translate("pages.recommendations.addFormErrorInfo")}
                    </Alert>
                )}
                <Button type="submit" leftIcon={<AddIcon />} ml="4" w="200px" colorScheme="blue">
                    {translate("pages.recommendations.addButton")}
                </Button>
            </form>

            <Text fontSize="xl" pl="4" mt="6" color="grey">
                {translate("pages.recommendations.list")}
            </Text>
            <Flex p="4" direction="column" gap="5">
                {recommendations.map((element) => {
                    return (
                        <RecommendationRow
                            key={element.id}
                            data={element}
                            deleteMethod={deleteRecommendation}
                            handleModal={handleModal}
                        />
                    );
                })}
                <PagesControls
                    pagesInfo={pagesInfo}
                    numberOfElements={recommendations.length}
                    fetchFunction={fetchRecommendations}
                    isZeroFirst={false}
                />
            </Flex>
            <Modal
                size="6xl"
                blockScrollOnMount={false}
                isOpen={modalOpen}
                onClose={() => setModalOpen(false)}
                isCentered
            >
                <ModalOverlay />
                <ModalContent>
                    <ModalHeader>{translate("pages.recommendations.modalHeader")}</ModalHeader>
                    <ModalCloseButton />
                    <ModalBody pb={6}>
                        <FormControl>
                            <FormLabel color="blue.700">{translate("pages.recommendations.keywordsComma")}</FormLabel>
                            <Flex gap="5" alignItems="center">
                                <Input
                                    name="keywords"
                                    value={currentRecommendationData?.keywords}
                                    onChange={handleModalChange}
                                />{" "}
                                <Button colorScheme="blue" onClick={modifyKeywords}>
                                    Update
                                </Button>
                            </Flex>
                        </FormControl>
                        <Divider borderColor={"gray.300"} mt="5" mb="5" orientation="horizontal" />
                        <FormControl mt={4}>
                            <FormLabel color="blue.700">{translate("pages.recommendations.entities")}</FormLabel>
                            {currentRecommendationData?.entities.map((entity: any, index: number) => {
                                return (
                                    <EntityRow
                                        key={entity.id}
                                        entity={entity}
                                        recommendationId={currentRecommendationData.id}
                                        deleteMethod={deleteEntityFromRecommendation}
                                        updateMethod={updateEntityForRecommendation}
                                        index={index}
                                    />
                                );
                            })}
                        </FormControl>
                        <Divider borderColor={"gray.300"} mt="5" mb="5" orientation="horizontal" />
                        <Flex gap="5" mb="3" alignItems="center" direction={direction}>
                            <FormControl>
                                <FormLabel fontSize="sm">{translate("pages.recommendations.name")}</FormLabel>
                                <Input name="name" value={newEntity.name} onChange={handleNewEntityChange} />
                            </FormControl>
                            <FormControl>
                                <FormLabel fontSize="sm">{translate("pages.recommendations.ds")}</FormLabel>
                                <Input
                                    name="dataSourceCode"
                                    value={newEntity.dataSourceCode}
                                    onChange={handleNewEntityChange}
                                />
                            </FormControl>{" "}
                            <FormControl>
                                <FormLabel fontSize="sm">{translate("pages.recommendations.entity")}</FormLabel>
                                <Input
                                    name="entityCode"
                                    value={newEntity.entityCode}
                                    onChange={handleNewEntityChange}
                                />
                            </FormControl>{" "}
                            <FormControl>
                                <FormLabel fontSize="sm">{translate("pages.recommendations.weight")}</FormLabel>
                                <Input
                                    type="number"
                                    min={0}
                                    name="orderWeight"
                                    value={newEntity.orderWeight}
                                    onChange={handleNewEntityChange}
                                />
                            </FormControl>
                        </Flex>
                        {addEntityError && (
                            <Alert borderRadius="10" border="none" height="10" mb="3" status="error" variant="solid">
                                <AlertIcon />
                                {translate("pages.recommendations.addFormErrorInfo")}
                            </Alert>
                        )}
                        <Flex direction="row" justifyContent="flex-end">
                            <Button
                                leftIcon={<AddIcon />}
                                justifySelf="flex-end"
                                colorScheme="blue"
                                onClick={() => addEntity(currentRecommendationData.id)}
                            >
                                {translate("pages.recommendations.addEntity")}
                            </Button>
                        </Flex>
                    </ModalBody>

                    <ModalFooter>
                        <Button onClick={() => setModalOpen(false)}>{translate("pages.recommendations.cancelButton")}</Button>
                    </ModalFooter>
                </ModalContent>
            </Modal>
        </Flex>
    );
};
