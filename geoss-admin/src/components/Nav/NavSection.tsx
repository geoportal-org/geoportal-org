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
import { pagesRoutes } from "@/data/pagesRoutes";
import { useContext, useState } from "react";
import { SiteContext } from "@/context/CurrentSiteContext";

export const NavSection = ({ navSection, onNavClose }: NavSectionProps) => {
    const { titleId, items } = navSection;
    const { currentSiteId } = useContext(SiteContext);
    const hidePocFeatures = process.env.NEXT_PUBLIC_HIDE_POC_FEATURES === 'true' ? true : false;

    return (
        <AccordionItem as={ListItem}>
            <AccordionButton>
                <Text as="span">
                    <TextContent id={titleId} />
                </Text>
                <AccordionIcon />
            </AccordionButton>
            <AccordionPanel as={UnorderedList} m={0} styleType="none">
                {items.map((item) => {
                    if (item.href === pagesRoutes.sites && currentSiteId !== 0) return null;
                    if (hidePocFeatures) {
                        if (item.titleId === "nav.extensions.section.entryResources" || item.titleId === "nav.extensions.section.resourceExtensions") {
                            return null;
                        }
                    }
                    return <NavItem key={item.titleId} item={item} onNavClose={onNavClose} />;
                })}
            </AccordionPanel>
        </AccordionItem>
    );
};
