import { Input, InputGroup, InputRightElement } from "@chakra-ui/react";
import { SearchIcon } from "@chakra-ui/icons";
import useFormatMsg from "@/utils/useFormatMsg";
import { SearchBarProps } from "@/types";

export const SearchBar = ({ setQuery }: SearchBarProps) => {
    const { translate } = useFormatMsg();

    const handleQueryChange = (searchQuery: string) => {
        setQuery(searchQuery);
    };

    return (
        <InputGroup size="sm" w="3xs">
            <Input placeholder={translate("general.type")} onChange={(e: any) => handleQueryChange(e.target.value)} />
            <InputRightElement>
                <SearchIcon color="blackAlpha.700" />
            </InputRightElement>
        </InputGroup>
    );
};
