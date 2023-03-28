import { Box } from "@chakra-ui/react";
import { TextContent } from "@/components";
import { DataStatusProps } from "@/types";

export const DataStatus = ({ isPublished }: DataStatusProps) => (
    <Box
        border="1px solid"
        borderColor={isPublished ? "brand.accept" : "brand.cancel"}
        textAlign="center"
        py={0.5}
        px={1}
        borderRadius="primary"
    >
        <TextContent id={isPublished ? "general.published" : "general.draft"} />
    </Box>
);
