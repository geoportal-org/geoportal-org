import {
    AccordionButton,
    AccordionIcon,
    AccordionItem,
    AccordionPanel,
    ListItem,
    Text,
    UnorderedList,
} from "@chakra-ui/react";
import { TextContent } from "@/components";
import { NavItem } from "./NavItem";
import { NavSectionProps } from "@/types";

export const NavSection = ({ navSection, onNavClose }: NavSectionProps) => {
    const { titleId, items } = navSection;

    return (
        <AccordionItem as={ListItem}>
            <AccordionButton>
                <Text as="span">
                    <TextContent id={titleId} />
                </Text>
                <AccordionIcon />
            </AccordionButton>
            <AccordionPanel as={UnorderedList} m={0} styleType="none">
                {items.map((item) => (
                    <NavItem key={item.titleId} item={item} onNavClose={onNavClose} />
                ))}
            </AccordionPanel>
        </AccordionItem>
    );
};
