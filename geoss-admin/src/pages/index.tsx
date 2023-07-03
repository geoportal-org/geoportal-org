import { GetServerSideProps } from "next";
import { SignIn } from "@/components";
import { pagesRoutes } from "@/data";
import { getServerSession } from "next-auth/next";
import { authOptions } from "./api/auth/[...nextauth]";
import { NonProtectedNextPage } from "@/types";

const Home: NonProtectedNextPage = () => <SignIn />;

Home.nonAuth = true;

export default Home;

export const getServerSideProps: GetServerSideProps = async (context) => {
    const { req, res } = context;
    const session = await getServerSession(req, res, authOptions);

    if (session) {
        return {
            redirect: { destination: pagesRoutes.website, permanent: false },
        };
    }

    return {
        props: {},
    };
};
