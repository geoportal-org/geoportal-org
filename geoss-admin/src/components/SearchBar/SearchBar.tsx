import { Input, InputGroup, InputRightElement } from "@chakra-ui/react";
import { SearchIcon } from "@chakra-ui/icons";
import { SearchBarProps } from "@/types";

export const SearchBar = ({ setQuery }: SearchBarProps) => {
    const handleChange = (searchQuery: string) => {
        setQuery(searchQuery);
    };

    return (
        <InputGroup size="sm" w="3xs">
            <Input placeholder="Type here..." onChange={(e) => handleChange(e.target.value)} />
            <InputRightElement children={<SearchIcon color="blackAlpha.700" />} />
        </InputGroup>
    );
};
