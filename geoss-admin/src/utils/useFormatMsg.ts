import { useIntl } from "react-intl";
import { flattenMessages } from "./helpers";
import { contentMessages } from "@/content";

const defaultTextContent = flattenMessages(contentMessages.en);

const useFormatMsg = () => {
    const { formatMessage } = useIntl();
    const translate = (id: string, values?: { [key: string]: any }) =>
        formatMessage({ id, defaultMessage: defaultTextContent[id] }, values);
    return { translate };
};

export default useFormatMsg;
