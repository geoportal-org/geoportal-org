import useFormatMsg from "@/utils/useFormatMsg";
import { CheckIcon, DeleteIcon, EditIcon } from "@chakra-ui/icons";
import { Button, Flex, FormControl, FormLabel, Input, Tooltip, useBreakpointValue } from "@chakra-ui/react";
import React, { useState } from "react";

const EntityRow = (props: any) => {
    let { entity, index, deleteMethod, updateMethod, recommendationId } = props;
    const { translate } = useFormatMsg();
    const [entityData, setEntityData] = useState<any>(entity);
    const direction = useBreakpointValue({ base: "column", md: "row" }) as any;

    const handleChange = (event: any) => {
        const name = event.target.name;
        const value = event.target.value;
        setEntityData((values: any) => ({ ...values, [name]: value }));
    };

    return (
        <Flex gap="3" mt="1" mb="1" alignItems="flex-end" direction={direction}>
            <FormControl>
                {index === 0 && <FormLabel fontSize="sm">{translate("pages.recommendations.name")}</FormLabel>}
                <Input name="name" onChange={handleChange} value={entityData.name} />
            </FormControl>
            <FormControl>
                {index === 0 && <FormLabel fontSize="sm">{translate("pages.recommendations.ds")}</FormLabel>}
                <Input
                    isDisabled={true}
                    name="dataSourceCode"
                    onChange={handleChange}
                    value={entityData.dataSourceCode}
                />
            </FormControl>{" "}
            <FormControl>
                {index === 0 && <FormLabel fontSize="sm">{translate("pages.recommendations.entity")}</FormLabel>}
                <Input isDisabled={true} name="entityCode" onChange={handleChange} value={entityData.entityCode} />
            </FormControl>{" "}
            <FormControl>
                {index === 0 && <FormLabel fontSize="sm">{translate("pages.recommendations.weight")}</FormLabel>}
                <Input name="orderWeight" onChange={handleChange} value={entityData.orderWeight} />
            </FormControl>
            <Flex gap='2'>
                <Tooltip label={translate("pages.recommendations.updateEntity")}>
                    <Button
                        isDisabled={entity === entityData ? true : false}
                        variant="geossOutline"
                        color="green"
                        onClick={() => updateMethod(recommendationId, entityData)}
                    >
                        <CheckIcon />
                    </Button>
                </Tooltip>
                <Tooltip label={translate("pages.recommendations.deleteEntity")}>
                    <Button
                        variant="geossOutline"
                        color="red"
                        onClick={() => deleteMethod(recommendationId, entity.id)}
                    >
                        <DeleteIcon />
                    </Button>
                </Tooltip>
            </Flex>
        </Flex>
    );
};

export default EntityRow;
