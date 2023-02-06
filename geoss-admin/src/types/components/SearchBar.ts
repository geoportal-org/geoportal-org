import { Dispatch, SetStateAction } from "react";

export type SearchBarProps = {
    setQuery: Dispatch<SetStateAction<string>>;
};
