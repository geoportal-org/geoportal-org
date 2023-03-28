import { Breadcrumb, BreadcrumbItem, BreadcrumbLink, Button } from "@chakra-ui/react";
import { ChevronRightIcon } from "@chakra-ui/icons";
import { HomeIcon } from "../Icons";
import useFormatMsg from "@/utils/useFormatMsg";
import { FileRepositoryBreadcrumProps } from "@/types";

export const FileRepositoryBreadcrumb = ({ breadcrumb, handleBreadcrumbClick }: FileRepositoryBreadcrumProps) => {
    const { translate } = useFormatMsg();

    return (
        <Breadcrumb
            bg="brand.background"
            borderRadius="primary"
            boxShadow="controlPanel"
            display="flex"
            justifyContent="center"
            listProps={{ flexWrap: "wrap", justifyContent: "center" }}
            m={1}
            mb={6}
            p={2}
            separator={<ChevronRightIcon boxSize={4} />}
            spacing={0.25}
        >
            {breadcrumb.map((item, idx) => {
                const isLastItem = breadcrumb.length - 1 === idx;
                return (
                    <BreadcrumbItem key={item.folderId} onClick={() => handleBreadcrumbClick(item.folderId, idx)}>
                        <BreadcrumbLink
                            as={Button}
                            fontWeight={isLastItem ? "bold" : "normal"}
                            iconSpacing="2px"
                            leftIcon={idx === 0 ? <HomeIcon /> : null}
                            size="sm"
                            variant="geossBreadcrumb"
                            _hover={{ textDecoration: "none" }}
                        >
                            {idx !== 0 ? item.folderTitle : translate("pages.file-repository.root-folder")}
                        </BreadcrumbLink>
                    </BreadcrumbItem>
                );
            })}
        </Breadcrumb>
    );
};
