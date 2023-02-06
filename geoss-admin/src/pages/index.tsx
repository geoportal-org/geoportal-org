import { NextPage } from "next";
import { Box } from "@chakra-ui/react";

const Home: NextPage = () => {
    return (
        <Box color="brand.accept" fontSize="20px" p={2} bg="brand.light" w="100px" borderRadius="primary">
            home page
        </Box>
    );
};

export default Home;
