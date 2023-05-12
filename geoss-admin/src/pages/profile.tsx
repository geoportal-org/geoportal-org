import { ProtectedNextPage } from "@/types";
import { NextPage } from "next";

const Profile: ProtectedNextPage = () => <div>Profile</div>;

Profile.auth = true;

export default Profile;
