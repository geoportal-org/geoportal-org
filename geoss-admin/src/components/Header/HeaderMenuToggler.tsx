import useFormatMsg from "@/utils/useFormatMsg";
import { Button, Text, Box } from "@chakra-ui/react";

export const HeaderMenuToggler = ({ onClose, onOpen, isOpen }: any) => {
    const { translate } = useFormatMsg();

    return (
        <Box w="52px">
            <Button
                aria-label={translate(isOpen ? "nav.close" : "nav.open")}
                h="34px"
                onClick={isOpen ? onClose : onOpen}
                p="2px"
                size="xs"
                variant="unstyled"
                w="34px"
                _hover={{ transform: "scale(1.1)" }}
            >
                <Text
                    as="span"
                    bg="brand.mainLight"
                    w="full"
                    h="3px"
                    display="block"
                    borderRadius="xl"
                    pos="relative"
                    transition=".3s cubic-bezier(0.68, -0.6, 0.32, 1.6)"
                    transformOrigin={isOpen ? "top" : ""}
                    transform={isOpen ? "rotatez(-45deg)" : ""}
                    _before={{
                        content: '""',
                        display: "block",
                        h: "3px",
                        w: "50%",
                        bg: "brand.mainLight",
                        pos: "absolute",
                        top: "-10px",
                        borderRadius: "xl",
                        transition: ".3s cubic-bezier(0.68, -0.6, 0.32, 1.6)",
                        transformOrigin: isOpen && "bottom",
                        transform: isOpen && "rotatez(90deg) translate(1px,-6px) ",
                    }}
                    _after={{
                        content: '""',
                        display: "block",
                        h: "3px",
                        w: isOpen ? "50%" : "75%",
                        bg: "brand.mainLight",
                        pos: "absolute",
                        top: "10px",
                        borderRadius: "xl",
                        transition: ".3s cubic-bezier(0.68, -0.6, 0.32, 1.6)",
                        transformOrigin: isOpen && "bottom",
                        transform: isOpen && "translate(6px, -4px) rotatez(90deg)",
                    }}
                ></Text>
            </Button>
        </Box>
    );
};
