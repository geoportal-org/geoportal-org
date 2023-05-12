import { NextPage } from "next";
import { MenuContent } from "@/components";
import { ProtectedNextPage } from "@/types";

const Menu: ProtectedNextPage = () => <MenuContent />;

Menu.auth = true;

export default Menu;
