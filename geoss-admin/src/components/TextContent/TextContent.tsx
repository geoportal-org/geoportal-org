import { FormattedMessage } from "react-intl";
import { flattenMessages } from "@/utils/helpers";
import { contentMessages } from "@/content";
import { TextContentProps } from "@/types";

const defaultTextContent = flattenMessages(contentMessages.en);

export const TextContent = ({ id, ...values }: TextContentProps) => (
    <FormattedMessage
        id={id}
        values={{ ...values, b: (chunks) => <strong>{chunks}</strong>, u: (chunks) => <u>{chunks}</u> }}
        defaultMessage={defaultTextContent[id]}
    />
);
