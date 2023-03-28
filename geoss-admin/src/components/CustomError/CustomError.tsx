import { Flex, Text, Divider, Box, useBreakpointValue, keyframes } from "@chakra-ui/react";
import { MainContent, TextContent } from "@/components";

export const CustomError = () => {
    const orientation = useBreakpointValue({ base: "horizontal", md: "vertical" }) as any;

    return (
        <MainContent>
            <Flex
                align="center"
                direction={["column", null, "row"]}
                gap={["0", null, "40px"]}
                h="full"
                justify="center"
                overflow={["hidden", null, "visible"]}
                textAlign="center"
                w="full"
            >
                <Flex
                    align="flex-end"
                    flex="1"
                    fontSize={["8xl", null, "9xl"]}
                    fontWeight="bold"
                    justify="flex-end"
                    letterSpacing="wider"
                    lineHeight="normal"
                    transition="color .3s ease-out"
                    zIndex="2"
                >
                    <Box
                        color="brand.dark"
                        cursor="default"
                        _hover={{
                            color: "brand.darkSoft",
                        }}
                    >
                        <Text
                            as="span"
                            display="inline-block"
                            lineHeight="0.7"
                            pos="relative"
                            sx={{ perspective: "1000px", perspectiveOrigin: "500% 50%" }}
                        >
                            <Text
                                as="span"
                                display="inline-block"
                                pos="relative"
                                transform="rotateX(0)"
                                transformOrigin="50% 100% 0px"
                                animation={easyoutelasticAnimation}
                            >
                                4
                            </Text>
                        </Text>
                        <Text as="span" display="inline-block" pos="relative">
                            0
                        </Text>
                        <Text
                            as="span"
                            display="inline-block"
                            lineHeight="0.7"
                            pos="relative"
                            sx={{ perspective: "none", perspectiveOrigin: "50% 50%" }}
                        >
                            <Text
                                as="span"
                                animation={rotatedropAnimation}
                                display="inline-block"
                                pos="relative"
                                transform="rotate(0deg)"
                                transformOrigin="100% 100% 0px"
                            >
                                4
                            </Text>
                        </Text>
                    </Box>
                </Flex>
                <Divider
                    borderColor="brand.divider"
                    maxH="250px"
                    maxW="350px"
                    opacity={1}
                    orientation={orientation}
                    zIndex="1"
                />
                <Text
                    flex="1"
                    fontSize="18px"
                    letterSpacing="wide"
                    mt={["20px", "20px", "0"]}
                    textAlign={["center", null, "left"]}
                    textTransform="uppercase"
                    zIndex="2"
                >
                    <TextContent id="pages.error.info" />
                </Text>
            </Flex>
        </MainContent>
    );
};

const easyoutelastic = keyframes`
0% {
    transform: rotateX(0);
  }
  9% {
    transform: rotateX(210deg);
  }
  13% {
    transform: rotateX(150deg);
  }
  16% {
    transform: rotateX(200deg);
  }
  18% {
    transform: rotateX(170deg);
  }
  20% {
    transform: rotateX(180deg);
  }
  60% {
    transform: rotateX(180deg);
  }
  80% {
    transform: rotateX(0);
  }
  100% {
    transform: rotateX(0);
  }
}
`;

const rotatedrop = keyframes`
0% {
    transform: rotate(0);
  }
  10% {
    transform: rotate(30deg);
  }
  15% {
    transform: rotate(90deg);
  }
  70% {
    transform: rotate(90deg);
  }
  80% {
    transform: rotate(0);
  }
  100% {
    transform: rotateX(0);
  }
`;

const easyoutelasticAnimation = `${easyoutelastic} 8s 8`;
const rotatedropAnimation = `${rotatedrop} 8s 8`;
