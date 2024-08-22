import { WorkersService } from "@/services/api/settings/WorkersService";
import { ToastStatus } from "@/types";
import { WorkerData, WorkerType } from "@/types/models/workers";
import useCustomToast from "@/utils/useCustomToast";
import useFormatMsg from "@/utils/useFormatMsg";
import { Flex } from "@chakra-ui/react";
import React, { useEffect, useState } from "react";
import { MainContentHeader } from "../MainContent/MainContentHeader";
import WorkerRow from "./WorkerRow";

export enum STATUS {
    COMPLETED = "COMPLETED",
    STARTING = "STARTING",
    STARTED = "STARTED",
    STOPPING = "STOPPING",
    STOPPED = "STOPPED",
    FAILED = "FAILED",
    ABANDONED = "ABANDONED",
    UNKNOWN = "UNKNOWN",
}

export const Workers = () => {
    const [esaWorker, setEsaWorker] = useState<WorkerData>();
    const [eostermWorker, setEostermWorker] = useState<WorkerData>();
    const [earthWorker, setEarthWorker] = useState<WorkerData>();
    const { translate } = useFormatMsg();
    const { showToast } = useCustomToast();

    useEffect(() => {
        fetchWorkers();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    useEffect(() => {
        const timeout = setTimeout(() => {
            fetchWorkers();
        }, 10000);
        return () => clearTimeout(timeout);
    });

    const fetchWorkers = async () => {
        try {
            await fetchEsaWorker();
            await fetchEostermWorker();
            await fetchEarthWorker();
        } catch (e: any) {
            console.error(e)
let msg = "";
            if (e.errorInfo?.length) {
                msg = JSON.parse(e.errorInfo).detail;
            } else {
                                msg = e.errorInfo.message || e.errorInfo.errors[0].message
;
            }
            showToast({
                title: translate("general.error"),
                description: `${msg || ""}`,
                status: ToastStatus.ERROR,
            });
        }
    };

    const fetchEsaWorker = async () => {
        try {
            const resEsa = await WorkersService.getWorker(WorkerType.ESA);
            setEsaWorker(resEsa);
        } catch (e: any) {
            console.error(e)
let msg = "";
            if (e.errorInfo?.length) {
                msg = JSON.parse(e.errorInfo).detail;
            } else {
                                msg = e.errorInfo.message || e.errorInfo.errors[0].message
;
            }
            showToast({
                title: translate("general.error"),
                description: `${msg || ""}`,
                status: ToastStatus.ERROR,
            });
        }
    };

    const fetchEostermWorker = async () => {
        try {
            const resEosterm = await WorkersService.getWorker(WorkerType.EOSTERM);
            setEostermWorker(resEosterm);
        } catch (e: any) {
            console.error(e)
let msg = "";
            if (e.errorInfo?.length) {
                msg = JSON.parse(e.errorInfo).detail;
            } else {
                                msg = e.errorInfo.message || e.errorInfo.errors[0].message
;
            }
            showToast({
                title: translate("general.error"),
                description: `${msg || ""}`,
                status: ToastStatus.ERROR,
            });
        }
    };

    const fetchEarthWorker = async () => {
        try {
            const resEarth = await WorkersService.getWorker(WorkerType.EARTH);
            setEarthWorker(resEarth);
        } catch (e: any) {
            console.error(e)
let msg = "";
            if (e.errorInfo?.length) {
                msg = JSON.parse(e.errorInfo).detail;
            } else {
                                msg = e.errorInfo.message || e.errorInfo.errors[0].message
;
            }
            showToast({
                title: translate("general.error"),
                description: `${msg || ""}`,
                status: ToastStatus.ERROR,
            });
        }
    };

    const runWorker = async (type: WorkerType) => {
        const res = await WorkersService.runWorker(type);
        if (type === WorkerType.ESA) {
            setEsaWorker(res);
        } else if (type === WorkerType.EOSTERM) {
            setEostermWorker(res);
        } else {
            setEarthWorker(res);
        }
    };

    return (
        <Flex direction="column" overflowY="auto">
            <MainContentHeader titleId="nav.settings.section.workers" />
            <Flex direction="column" w={"100%"} padding={4} gap={6}>
                <WorkerRow worker={esaWorker} fetchWorker={fetchEsaWorker} runWorker={runWorker} />
                <WorkerRow worker={eostermWorker} fetchWorker={fetchEostermWorker} runWorker={runWorker} />
                <WorkerRow worker={earthWorker} fetchWorker={fetchEarthWorker} runWorker={runWorker} />
            </Flex>
        </Flex>
    );
};
