import { pagesRoutes } from "@/data";
import { signOut, useSession } from "next-auth/react";
import { useRouter } from "next/router";
import { useEffect } from "react";
import { Loader } from "@/components";
import { AuthGuardProps } from "@/types";

export const AuthGuard = ({ children }: AuthGuardProps) => {
    const { data: session, status } = useSession();
    const router = useRouter();
    const isUser = !!session?.user;

    const handleUserLogout = async () => {
        await fetch(`${process.env.NEXT_PUBLIC_URL}/api/auth/signOutProvider`, { method: "PUT" }).then(async (res) =>
            res.ok ? await signOut({ callbackUrl: pagesRoutes.signIn }) : console.error("Error with sign out.")
        );
    };

    useEffect(() => {
        if (Date.now() > Number(session?.expires)) {
            handleUserLogout();
        }
        if (status === "loading") return;
        if (!isUser) router.push(pagesRoutes.signIn);
    }, [status, isUser, router]);

    if (isUser) {
        return children;
    }

    return <Loader />;
};
