import {
    ApiIcon,
    CatalogIcon,
    FileIcon,
    LayerIcon,
    LayoutIcon,
    LogoutIcon,
    MenuIcon,
    PageIcon,
    ProfileIcon,
    TutorialIcon,
    WebIcon,
    WebsiteIcon,
} from "@/components/Icons";
import { pagesRoutes } from "./pagesRoutes";
import { NavItem, NavSection } from "@/types";

const navSettingsData: NavItem[] = [
    {
        titleId: "settings.section.api",
        href: pagesRoutes.api,
        Icon: ApiIcon,
    },
    {
        titleId: "settings.section.web",
        href: pagesRoutes.web,
        Icon: WebIcon,
    },
    {
        titleId: "settings.section.catalogs",
        href: pagesRoutes.catalogs,
        Icon: CatalogIcon,
    },
    {
        titleId: "settings.section.views",
        href: pagesRoutes.views,
        Icon: LayoutIcon,
    },
    {
        titleId: "settings.section.layer",
        href: pagesRoutes.defaultLayer,
        Icon: LayerIcon,
    },
    {
        titleId: "settings.section.tutorial",
        href: pagesRoutes.tutorialTags,
        Icon: TutorialIcon,
    },
];

const navContentsData: NavItem[] = [
    {
        titleId: "contents.section.website",
        href: pagesRoutes.website,
        Icon: WebsiteIcon,
    },
    {
        titleId: "contents.section.page",
        href: pagesRoutes.page,
        Icon: PageIcon,
    },
    {
        titleId: "contents.section.repository",
        href: pagesRoutes.fileRepository,
        Icon: FileIcon,
    },
    {
        titleId: "settings.section.menu",
        href: pagesRoutes.menu,
        Icon: MenuIcon,
    },
];

const navProfileData: NavItem[] = [
    {
        titleId: "profile.section.profile",
        href: pagesRoutes.myProfile,
        Icon: ProfileIcon,
    },
    {
        titleId: "profile.section.logout",
        href: "#",
        Icon: LogoutIcon,
    },
];

export const navData: NavSection[] = [
    { titleId: "contents.title", items: navContentsData },
    { titleId: "settings.title", items: navSettingsData },
    { titleId: "profile.title", items: navProfileData },
];

export const navSectionsUrls = [
    navContentsData.map((item) => item.href),
    navSettingsData.map((item) => item.href),
    navProfileData.map((item) => item.href),
];
