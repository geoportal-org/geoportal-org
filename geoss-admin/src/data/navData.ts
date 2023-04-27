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
        titleId: "nav.settings.section.api",
        href: pagesRoutes.api,
        Icon: ApiIcon,
    },
    {
        titleId: "nav.settings.section.web",
        href: pagesRoutes.web,
        Icon: WebIcon,
    },
    // hidden Catalogs in menu
    /*
    {
        titleId: "nav.settings.section.catalogs",
        href: pagesRoutes.catalogs,
        Icon: CatalogIcon,
    },
    */
    {
        titleId: "nav.settings.section.views",
        href: pagesRoutes.views,
        Icon: LayoutIcon,
    },
    {
        titleId: "nav.settings.section.layer",
        href: pagesRoutes.defaultLayer,
        Icon: LayerIcon,
    },
    {
        titleId: "nav.settings.section.tutorial",
        href: pagesRoutes.tutorialTags,
        Icon: TutorialIcon,
    },
];

const navContentsData: NavItem[] = [
    {
        titleId: "nav.contents.section.website",
        href: pagesRoutes.website,
        Icon: WebsiteIcon,
    },
    {
        titleId: "nav.contents.section.page",
        href: pagesRoutes.page,
        Icon: PageIcon,
    },
    {
        titleId: "nav.contents.section.repository",
        href: pagesRoutes.fileRepository,
        Icon: FileIcon,
    },
    {
        titleId: "nav.contents.section.menu",
        href: pagesRoutes.menu,
        Icon: MenuIcon,
    },
];

const navProfileData: NavItem[] = [
    {
        titleId: "nav.profile.section.profile",
        href: pagesRoutes.myProfile,
        Icon: ProfileIcon,
    },
    {
        titleId: "nav.profile.section.logout",
        href: "#",
        Icon: LogoutIcon,
    },
];

export const navData: NavSection[] = [
    { titleId: "nav.contents.title", items: navContentsData },
    { titleId: "nav.settings.title", items: navSettingsData },
    { titleId: "nav.profile.title", items: navProfileData },
];

export const navSectionsUrls = [
    navContentsData.map((item) => item.href),
    navSettingsData.map((item) => item.href),
    navProfileData.map((item) => item.href),
];
