import { pagesRoutes } from "@/data";
import { useSession } from "next-auth/react";
import { useRouter } from "next/router";
import { useEffect } from "react";
import { Loader } from "@/components";
import { AuthGuardProps } from "@/types";

export const AuthGuard = ({ children }: AuthGuardProps) => {
    const { data: session, status } = useSession();
    const router = useRouter();
    const isUser = !!session?.user;

    useEffect(() => {
        if (status === "loading") return;
        if (!isUser) router.push(pagesRoutes.signIn);
    }, [status, isUser, router]);

    if (isUser) {
        return children;
    }

    return <Loader />;
};
