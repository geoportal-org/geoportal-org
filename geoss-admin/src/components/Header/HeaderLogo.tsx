import NextLink from "next/link";
//import { useSession } from "next-auth/react";
import { Text, Link } from "@chakra-ui/react";
import { pagesRoutes } from "@/data";

export const HeaderLogo = () => {
    /*const { data: session } = useSession();
    const isUser = !!session?.user;*/

    return (
        <Text visibility={{base: 'hidden', sm: 'visible'}} as="h1" fontWeight="bold" fontSize="22px" flex="1 1 0" textAlign="center">
            <Link as={NextLink} href={pagesRoutes.website} _hover={{}}>
                GEOSS Admin
            </Link>
        </Text>
    );
};
