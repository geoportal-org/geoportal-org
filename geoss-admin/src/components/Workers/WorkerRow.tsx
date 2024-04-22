import { WorkerData, WorkerType } from "@/types/models/workers";
import useFormatMsg from "@/utils/useFormatMsg";
import { Flex, IconButton, Text } from "@chakra-ui/react";
import PlayIcon from "../Icons/PlayIcon";
import RefreshIcon from "../Icons/RefreshIcon";
import { STATUS } from "./Workers";

type Props = {
    worker: WorkerData | undefined;
    fetchWorker: () => void;
    runWorker: (type: WorkerType) => void;
};

const WorkerRow = ({ worker, fetchWorker, runWorker }: Props) => {
    const { translate } = useFormatMsg();

    const getWorkerType = () => {
        return (worker?.type as WorkerType) || WorkerType.ESA;
    };
    const checkIfWorkerIsRunning = () => {
        return worker?.status === STATUS.STARTING ||
            worker?.status === STATUS.STARTED ||
            worker?.status === STATUS.STOPPING
            ? true
            : false;
    };
    const getStatusColor = () => {
        switch (worker?.status) {
            case STATUS.STARTED:
            case STATUS.STARTING:
            case STATUS.STOPPING:
                return "orange.300";
            case STATUS.ABANDONED:
            case STATUS.FAILED:
            case STATUS.STOPPED:
                return "red.500";
            case STATUS.COMPLETED:
                return "green.400";
            default:
                return "grey";
        }
    };
    return (
        <Flex
            direction={{ base: "column", md: "row" }}
            bg="gray.100"
            w="100%"
            p="5"
            borderRadius="20"
            justifyContent="space-between"
            alignItems={{ base: "flex-start", md: "center" }}
            gap={4}
        >
            <Flex direction={"column"} width={{ base: "100%", md: "15%" }}>
                <Text fontSize="lg" color="blue.700" textTransform="uppercase">
                    {worker?.type}
                </Text>
                <Text fontSize="sm" color={getStatusColor()}>
                    <Text as={"span"} color="blue.700">
                        {translate("pages.workers.status")}:
                    </Text>{" "}
                    {worker?.status}
                </Text>
                {worker?.cause && worker.status !== STATUS.COMPLETED && (
                    <Text fontSize="sm" color="blue.700">
                        {translate("pages.workers.cause")}: {worker?.cause?.message}
                    </Text>
                )}
            </Flex>
            <Flex direction={"row"} gap={8} width={"100%"}>
                <Flex direction={"column"}>
                    <Text fontSize="xs" color="blue.700" flexDirection={"column"}>
                        <Text as={"span"} fontSize="sm">
                            {" "}
                            {translate("pages.workers.created")}:{" "}
                        </Text>

                        {new Date(worker?.createTime || "").toDateString() +
                            " " +
                            new Date(worker?.createTime || "").toLocaleTimeString()}
                    </Text>
                    <Text fontSize="xs" color="blue.700">
                        <Text as={"span"} fontSize="sm">
                            {translate("pages.workers.started")}:{" "}
                        </Text>
                        {new Date(worker?.startTime || "").toDateString() +
                            " " +
                            new Date(worker?.startTime || "").toLocaleTimeString()}{" "}
                    </Text>
                </Flex>
                <Flex direction={"column"}>
                    <Text fontSize="xs" color="blue.700">
                        <Text as={"span"} fontSize="sm">
                            {translate("pages.workers.ended")}:{" "}
                        </Text>
                        {new Date(worker?.startTime || "").toDateString() +
                            " " +
                            new Date(worker?.startTime || "").toLocaleTimeString()}{" "}
                    </Text>
                    <Text fontSize="xs" color="blue.700">
                        <Text as={"span"} fontSize="sm">
                            {translate("pages.workers.updated")}:{" "}
                        </Text>
                        {new Date(worker?.lastUpdated || "").toDateString() +
                            " " +
                            new Date(worker?.lastUpdated || "").toLocaleTimeString()}{" "}
                    </Text>
                </Flex>
            </Flex>
            <Flex direction={"row"} gap={2} alignItems="center" height={"100%"}>
                <IconButton
                    aria-label="refreshworker"
                    w={"20px"}
                    variant="geossOutline"
                    color="blue.300"
                    onClick={() => fetchWorker()}
                    icon={<RefreshIcon />}
                />
                <IconButton
                    isDisabled={checkIfWorkerIsRunning()}
                    aria-label="startworker"
                    w={"20px"}
                    variant="geossOutline"
                    color="blue.300"
                    onClick={() => runWorker(getWorkerType())}
                    icon={<PlayIcon />}
                />
            </Flex>
        </Flex>
    );
};

export default WorkerRow;
