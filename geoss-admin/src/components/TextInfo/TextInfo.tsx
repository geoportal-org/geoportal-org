import { Text } from "@chakra-ui/react";
import { TextContent } from "@/components";
import { TextInfoProps } from "@/types";

export const TextInfo = ({ id }: TextInfoProps) => (
    <Text fontSize="s">
        <TextContent id={id} />
    </Text>
);
