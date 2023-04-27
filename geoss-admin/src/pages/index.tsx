import { NextPage } from "next";
import { Text } from "@chakra-ui/react";
import { MainContent } from "@/components";

const Home: NextPage = () => (
    <MainContent titleId={"SIGN IN"}>
        <Text fontWeight="bold" fontSize="22px" mb="30px">
            TBD SIGN-IN PAGE OR REDIRECT TO DEFAULT PAGE
        </Text>
    </MainContent>
);

export default Home;
