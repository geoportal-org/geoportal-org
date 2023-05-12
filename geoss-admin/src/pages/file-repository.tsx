import { NextPage } from "next";
import { FileRepository as FileRepositoryCard } from "@/components";
import { ProtectedNextPage } from "@/types";

const FileRepository: ProtectedNextPage = () => <FileRepositoryCard />;

FileRepository.auth = true;

export default FileRepository;
