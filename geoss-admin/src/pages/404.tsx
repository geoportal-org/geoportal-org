import { CustomError } from "@/components";
import { NonProtectedNextPage } from "@/types";

const Custom404: NonProtectedNextPage = () => <CustomError />;

Custom404.nonAuth = true;

export default Custom404;
