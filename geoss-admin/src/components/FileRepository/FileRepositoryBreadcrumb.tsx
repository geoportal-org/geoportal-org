import { Breadcrumb, BreadcrumbItem, BreadcrumbLink, Button } from "@chakra-ui/react";
import useFormatMsg from "@/utils/useFormatMsg";
import { FileRepositoryBreadcrumProps } from "@/types";

export const FileRepositoryBreadcrumb = ({ breadcrumb, handleBreadcrumbClick }: FileRepositoryBreadcrumProps) => {
    const { translate } = useFormatMsg();

    return (
        <Breadcrumb
            bg="brand.background"
            boxShadow="controlPanel"
            borderRadius="primary"
            display="flex"
            justifyContent="flex-start"
            listProps={{ flexWrap: "wrap", justifyContent: "flex-start" }}
            mb={6}
            p={1}
            separator="/"
            spacing={0.25}
            position="sticky"
            top={0}
            zIndex={1}
        >
            {breadcrumb.map((item, idx) => {
                const isLastItem = breadcrumb.length - 1 === idx;
                if (idx !== 0) {
                    return (
                        <BreadcrumbItem key={item.folderId} onClick={() => handleBreadcrumbClick(item.folderId, idx)}>
                            <BreadcrumbLink
                                as={Button}
                                fontWeight={isLastItem ? "bold" : "normal"}
                                size="sm"
                                minW="auto"
                                variant="geossBreadcrumb"
                            >
                                {item.folderTitle || translate("pages.file-repository.root-folder")}
                            </BreadcrumbLink>
                        </BreadcrumbItem>
                    );
                }
            })}
        </Breadcrumb>
    );
};
