import { GetServerSideProps, NextPage, InferGetServerSidePropsType } from "next";
import { Box, Flex, Text } from "@chakra-ui/react";
import { MainContent, PrimaryButton } from "@/components";
import { authOptions } from "./api/auth/[...nextauth]";

import { getProviders, LiteralUnion, ClientSafeProvider, signIn } from "next-auth/react";
import { getServerSession } from "next-auth/next";
import { BuiltInProviderType } from "next-auth/providers";
import { Session } from "next-auth/core/types";
import { useEffect, useState } from "react";

interface HomeProps {
    // providers: Record<LiteralUnion<BuiltInProviderType, string>, ClientSafeProvider> | null;
    session: Session | null;
}

const Home: NextPage<HomeProps> = ({ session }) => {
    //const providers = await getProviders();
    //console.log(providers);
    const [providers, setProviders] = useState<Record<
        LiteralUnion<BuiltInProviderType, string>,
        ClientSafeProvider
    > | null>();

    console.log(session);
    useEffect(() => {
        const getProvidersList = async () => {
            try {
                const providers = await getProviders();
                console.log(providers);
                setProviders(providers);
            } catch (e) {
                console.log(e);
            }
        };
        getProvidersList();
    }, []);

    return (
        <MainContent titleId={"SIGN IN"}>
            <Text fontWeight="bold" fontSize="22px" mb="30px">
                TBD SIGN-IN PAGE OR REDIRECT TO DEFAULT PAGE
            </Text>
            <Flex>
                {providers &&
                    Object.values(providers).map((provider) => (
                        <Flex key={provider.name}>
                            <PrimaryButton onClick={() => signIn(provider.id)}>
                                Sign in with {provider.name}
                            </PrimaryButton>
                        </Flex>
                    ))}
            </Flex>
        </MainContent>
    );
};

export default Home;

export const getServerSideProps: GetServerSideProps<HomeProps> = async (context) => {
    console.log(context);
    const { req, res } = context;

    // const providers = getProviders();

    // console.log(providers);
    const session = await getServerSession(req, res, authOptions);
    console.log(session);

    if (session) {
        return {
            redirect: { destination: "/contents", permanent: true },
        };
    }

    return {
        props: {
            //providers,
            session,
        },
    };
};

/*export async function getServerSideProps({ req, res }: any) {
    //const { req } = context;
    const providers = await getProviders();
    const session = await getServerSession(req, res, authOptions);

    return {
        props: {
            providers,
            //csrfToken: await getCsrfToken({req, res}),
            session,
        },
    };
}*/
