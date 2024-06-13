import { Loader } from "@/components";
import { useEffect } from "react";
import { useRouter } from "next/router";
import { signIn } from "next-auth/react";
import { pagesRoutes } from "@/data";

export const SignIn = () => {
    const router = useRouter();

    useEffect(() => {
        if (router.isReady) {
            setTimeout(
                () => signIn("keycloak", { callbackUrl: `${process.env.NEXTAUTH_URL}${pagesRoutes.website}` }),
                0
            );
        }
    }, [router.isReady]);

    return <Loader />;
};
