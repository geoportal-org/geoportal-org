import { useState } from "react";
import { ListItem, UnorderedList, Button, Flex } from "@chakra-ui/react";
import { ChevronRightIcon } from "@chakra-ui/icons";
import { FolderIcon, SingleFileIcon } from "../Icons";
import { FileRepositoryTreeItem as FileItemType } from "@/types";

export const FileRepositoryTreeItem = ({ item }: { item: FileItemType }) => {
    const { name, items } = item;
    const [isExpanded, setIsExpanded] = useState(false);
    return (
        <ListItem>
            <Flex gap="2px">
                {items ? (
                    <Button variant="geossFolder" size="sm" onClick={() => setIsExpanded(!isExpanded)} color="black">
                        <ChevronRightIcon
                            transform={isExpanded ? "rotate(90deg)" : "rotate(0)"}
                            transitionDuration="normal"
                        />
                        <FolderIcon />
                        {name}
                    </Button>
                ) : (
                    <>
                        <SingleFileIcon />
                        {name}
                    </>
                )}
            </Flex>
            {items && (
                <UnorderedList
                    variant="geossFileTree"
                    pl="10px"
                    m={0}
                    mt="5px"
                    styleType="none"
                    display={isExpanded ? "flex" : "none"}
                    color="black"
                    flexDir="column"
                >
                    {items.map((el) => (
                        <FileRepositoryTreeItem key={el.name} item={el} />
                    ))}
                </UnorderedList>
            )}
        </ListItem>
    );
};
