import { useRouter } from "next/router";
import { useIntl } from "react-intl";
import { Menu, MenuButton, MenuList, MenuItem, Image } from "@chakra-ui/react";
import { ChevronDownIcon } from "@chakra-ui/icons";
import useFormatMsg from "@/utils/useFormatMsg";
import { LanguageSwitcherProps } from "@/types";
import { languagesSwitcher } from "@/data";

export const LanguageSwitcher = ({ onMenuClose }: LanguageSwitcherProps) => {
    const { locale } = useIntl();
    const { translate } = useFormatMsg();
    const router = useRouter();
    const { pathname, asPath, query } = router;

    const setFlagUrl = () => languagesSwitcher.filter((lang) => lang.locale === locale)[0].flagUrl;

    const changeLang = (langLocale: string) =>
        langLocale !== locale && router.push({ pathname, query }, asPath, { locale: langLocale });

    return (
        <Menu variant="geossLangSwitcher">
            {({ isOpen }) => (
                <>
                    <MenuButton onClick={onMenuClose} aria-label={translate("languageSwitcher.toggle")}>
                        <Image
                            src={setFlagUrl()}
                            alt={translate(`languageSwitcher.${locale}`)}
                            boxSize="2rem"
                            borderRadius="full"
                        />{" "}
                        <ChevronDownIcon
                            color="brand.mainLight"
                            transform={isOpen ? "rotate(-180deg)" : "rotate(0)"}
                            transitionDuration="normal"
                        />
                    </MenuButton>
                    <MenuList>
                        {languagesSwitcher.map((lang) => {
                            const { locale: langLocale, flagUrl, title } = lang;
                            const isActive = langLocale === locale;

                            return (
                                <MenuItem
                                    key={lang.locale}
                                    borderStartColor={isActive ? "brand.dark" : "transparent"}
                                    bg={isActive ? "brand.darkSoft" : "brand.background"}
                                    onClick={() => changeLang(langLocale)}
                                >
                                    <Image
                                        src={flagUrl}
                                        alt={translate(`languageSwitcher.${langLocale}`)}
                                        boxSize="1.5rem"
                                        mr="7.5px"
                                        borderRadius="full"
                                    />
                                    {title}
                                </MenuItem>
                            );
                        })}
                    </MenuList>
                </>
            )}
        </Menu>
    );
};
