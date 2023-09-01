import { SavedRecommendationData } from "@/types/models/recommendations";
import useFormatMsg from "@/utils/useFormatMsg";
import { DeleteIcon, EditIcon } from "@chakra-ui/icons";
import { Button, Flex, Text, useBreakpointValue } from "@chakra-ui/react";
import React from "react";
interface Props {
    data: SavedRecommendationData;
    deleteMethod: (id: number) => void;
    handleModal: (id: number) => void;
}

const RecommendationRow = (props: Props) => {
    const { translate } = useFormatMsg();
    const { entities, id, keywords } = props.data;
    const deleteMethod = props.deleteMethod;
    const handleModal = props.handleModal;

    return (
        <Flex bg="gray.100" w="100%" p="5" borderRadius="20" justifyContent="space-between">
            <Flex direction="column">
                <Flex direction="row" alignItems="center" gap="2">
                    <Text fontSize="xl" color="blue.700">
                        {translate("pages.recommendations.keywords")}:{" "}
                    </Text>
                    <Text fontSize="md" color="gray.500">
                        {keywords.map((word, index) => {
                            return index === keywords.length - 1 ? word : word + ", ";
                        })}
                    </Text>
                </Flex>
                <Flex direction="row" alignItems="center" gap="2">
                    <Text fontSize="lg" color="blue.700">
                        {translate("pages.recommendations.entities")}:{" "}
                    </Text>
                    <Text fontSize="sm" color="gray.500">
                        {entities.map((entity, index) => {
                            return index === entities.length - 1 ? entity.name : entity.name + ", ";
                        })}
                    </Text>
                </Flex>
                <Text fontSize="md" color="gray.700">
                    ID:{id}
                </Text>
            </Flex>
            <Flex alignItems="center" gap="5">
                <Button
                    w={useBreakpointValue({ base: "20px", md: "100px" })}
                    variant="geossOutline"
                    color="red"
                    onClick={() => deleteMethod(id)}
                    leftIcon={useBreakpointValue({ base: undefined, md: <DeleteIcon /> })}
                >
                    {useBreakpointValue({ base: <DeleteIcon />, md: translate("pages.recommendations.remove") })}
                </Button>
                <Button
                    w={useBreakpointValue({ base: "20px", md: "100px" })}
                    variant="geossOutline"
                    color="gray"
                    onClick={() => handleModal(id)}
                    leftIcon={useBreakpointValue({ base: undefined, md: <EditIcon /> })}
                >
                    {useBreakpointValue({ base: <EditIcon />, md: translate("pages.recommendations.edit") })}
                </Button>
            </Flex>
        </Flex>
    );
};

export default RecommendationRow;
