import { GetServerSideProps, NextPage } from "next";
import { SignIn } from "@/components";
import { ClientSafeProvider, LiteralUnion } from "next-auth/react";
import { BuiltInProviderType } from "next-auth/providers";
import { pagesRoutes } from "@/data";

interface HomeProps {
    providers: Record<LiteralUnion<BuiltInProviderType, string>, ClientSafeProvider> | null;
}

const Home: NextPage = () => {
    return <SignIn />;
};

export default Home;

export const getServerSideProps: GetServerSideProps = async (context) => {
    const { req, res } = context;

    //const session = await getServerSession(req, res, authOptions);
    ///const providers = await getProviders();

    //if (session) {
    return {
        redirect: { destination: pagesRoutes.website, permanent: false },
    };
    //}

    /*return {
        props: {
            providers,
        },
    };*/
};
