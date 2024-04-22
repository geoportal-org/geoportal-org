import { WorkersService } from "@/services/api/settings/WorkersService";
import { WorkerData, WorkerType } from "@/types/models/workers";
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

    useEffect(() => {
        fetchWorkers();
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
            console.log(e);
        }
    };

    const fetchEsaWorker = async () => {
        try {
            const resEsa = await WorkersService.getWorker(WorkerType.ESA);
            setEsaWorker(resEsa);
        } catch (e: any) {
            console.log(e);
        }
    };

    const fetchEostermWorker = async () => {
        try {
            const resEosterm = await WorkersService.getWorker(WorkerType.EOSTERM);
            setEostermWorker(resEosterm);
        } catch (e: any) {
            console.log(e);
        }
    };

    const fetchEarthWorker = async () => {
        try {
            const resEarth = await WorkersService.getWorker(WorkerType.EARTH);
            setEarthWorker(resEarth);
        } catch (e: any) {
            console.log(e);
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
