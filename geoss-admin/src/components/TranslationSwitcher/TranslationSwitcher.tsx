import { Menu, MenuButton, MenuList, MenuItem, Image } from "@chakra-ui/react";
import { ChevronDownIcon } from "@chakra-ui/icons";
import useFormatMsg from "@/utils/useFormatMsg";
import { ButtonType, LocaleNames, TranslationSwitcherProps } from "@/types";
import { languagesSwitcher } from "@/data";

export const TranslationSwitcher = ({ currentLang, onTranslationChange }: TranslationSwitcherProps) => {
    const { translate } = useFormatMsg();

    const setFlagUrl = () => languagesSwitcher.filter((lang) => lang.locale === currentLang)[0].flagUrl;

    return (
        <Menu variant="geossTranslationSwitcher">
            {({ isOpen }) => (
                <>
                    <MenuButton aria-label={translate("translationSwitcher.add")} type={ButtonType.BUTTON}>
                        <Image
                            src={setFlagUrl()}
                            alt={translate("translationSwitcher.add")}
                            boxSize="2rem"
                            borderRadius="full"
                        />{" "}
                        <ChevronDownIcon
                            color="brand.mainDark"
                            transform={isOpen ? "rotate(-180deg)" : "rotate(0)"}
                            transitionDuration="normal"
                        />
                    </MenuButton>
                    <MenuList>
                        {languagesSwitcher.map((lang) => {
                            const { locale: langLocale, flagUrl } = lang;
                            const isActive = langLocale === currentLang;

                            return (
                                <MenuItem
                                    key={lang.locale}
                                    borderStartColor={isActive ? "brand.dark" : "transparent"}
                                    bg={isActive ? "brand.darkSoft" : "brand.background"}
                                    onClick={() => onTranslationChange(langLocale as LocaleNames)}
                                >
                                    <Image
                                        src={flagUrl}
                                        alt={translate(`translationSwitcher.${langLocale}`)}
                                        boxSize="1.6rem"
                                        borderRadius="full"
                                    />
                                </MenuItem>
                            );
                        })}
                    </MenuList>
                </>
            )}
        </Menu>
    );
};
