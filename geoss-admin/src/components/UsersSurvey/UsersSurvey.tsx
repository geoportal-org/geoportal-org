import { SurveysService } from "@/services/api/users/surveyResultsService";
import { PagesInfo } from "@/types/models/recommendations";
import {
    Button,
    Flex,
    Input,
    Table,
    TableContainer,
    Tbody,
    Td,
    Text,
    Th,
    Thead,
    Tr,
    useBreakpointValue,
} from "@chakra-ui/react";
import React, { useEffect, useState } from "react";
import { initialPagesInfo } from "../Recommendations/DefaultValues";
import { SurveyInfo } from "@/types/models/surveys";
import PagesControls from "../PagesControls/PagesControls";
import { MainContentHeader } from "../MainContent/MainContentHeader";
import useFormatMsg from "@/utils/useFormatMsg";
import jsPDF from "jspdf";
import "jspdf-autotable";
import { Loader } from "../Loader/Loader";

const datePickerStyle: any = {
    width: "300px",
    fontSize: 18,
    border: "1px solid",
    borderColor: "#dedede",
    borderRadius: 4,
    padding: 8,
    marginBottom: 20,
};

export const UsersSurvey = () => {
    const [pagesInfo, setPagesInfo] = useState<PagesInfo>(initialPagesInfo);
    const [isFiltered, setIsFiltered] = useState(false);
    const [surveys, setSurveys] = useState<any>([]);
    const [startDate, setStartDate] = useState<any>("");
    const [endDate, setEndDate] = useState<any>("");
    const [isLoading, setIsLoading] = useState(true);
    const direction = useBreakpointValue({ base: "column", lg: "row" });
    const justify = useBreakpointValue({ base: "center", lg: "flex-start" });
    const { translate } = useFormatMsg();

    const fetchSurveys = async (page = 0, numberOfHits = 20) => {
        try {
            setIsLoading(true);
            let response = await SurveysService.getSurveys(page, numberOfHits);
            setSurveys(response._embedded.surveys);
            setPagesInfo(response.page);
        } catch (e) {
            console.log(e);
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        fetchSurveys();
    }, []);

    const filterResults = async (page = 0) => {
        try {
            setIsLoading(true);
            const isoStartDate = startDate === "" ? "" : new Date(startDate).toISOString();
            const isoEndDate = endDate === "" ? "" : new Date(endDate).toISOString();
            if (isoStartDate !== "" || isoEndDate !== "") {
                let response = await SurveysService.searchSurveys(page, 20, isoStartDate, isoEndDate);
                setSurveys(response._embedded.surveys);
                setPagesInfo(response.page);
                setIsFiltered(true);
            }
        } catch (e) {
            console.log(e);
        } finally {
            setIsLoading(false);
        }
    };

    const exportPDF = async () => {
        const unit = "pt";
        const size = "A4";
        const orientation = "landscape";

        const marginLeft = 40;
        const doc = new jsPDF(orientation, unit, size);
        doc.setFontSize(11);

        let numberOfHits = pagesInfo.totalElements;
        let response;
        const isoStartDate = startDate === "" ? "" : new Date(startDate).toISOString();
        const isoEndDate = endDate === "" ? "" : new Date(endDate).toISOString();
        if (isoStartDate !== "" || isoEndDate !== "") {
            response = await SurveysService.searchSurveys(0, numberOfHits, isoStartDate, isoEndDate);
        } else {
            response = await SurveysService.getSurveys(0, numberOfHits);
        }
        const data = response._embedded.surveys.map((survey: any) => [
            survey.createdOn,
            survey.from,
            survey.classification,
            survey.foundLookingFor,
            survey.lookingFor,
            survey.searchCriteria,
            survey.visualization,
            survey.adequately,
            survey.interest,
            survey.organized,
            survey.impression,
        ]);

        const title = "Surveys results";
        const headers = [
            [
                translate("pages.surveyResults.dateTime"),
                translate("pages.surveyResults.from"),
                translate("pages.surveyResults.classification"),
                translate("pages.surveyResults.fwlf"),
                translate("pages.surveyResults.lookingFor"),
                translate("pages.surveyResults.searchCriteria"),
                translate("pages.surveyResults.visualization"),
                translate("pages.surveyResults.adequately"),
                translate("pages.surveyResults.interest"),
                translate("pages.surveyResults.organized"),
                translate("pages.surveyResults.impression"),
            ],
        ];

        let content = {
            startY: 50,
            head: headers,
            body: data,
        };

        doc.text(title, marginLeft, 40);
        //@ts-ignore
        doc.autoTable(content);
        doc.save("surveyResults.pdf");
    };

    const exportCSV = async () => {
        let numberOfHits = pagesInfo.totalElements;
        let response;
        const isoStartDate = startDate === "" ? "" : new Date(startDate).toISOString();
        const isoEndDate = endDate === "" ? "" : new Date(endDate).toISOString();
        if (isoStartDate !== "" || isoEndDate !== "") {
            response = await SurveysService.searchSurveys(0, numberOfHits, isoStartDate, isoEndDate);
        } else {
            response = await SurveysService.getSurveys(0, numberOfHits);
        }
        const data = response._embedded.surveys;

        const csvContent =
            "data:text/csv;charset=utf-8," +
            `${translate("pages.surveyResults.dateTime")},${translate("pages.surveyResults.from")},${translate(
                "pages.surveyResults.classification"
            )},${translate("pages.surveyResults.fwlf")},${translate("pages.surveyResults.lookingFor")},${translate(
                "pages.surveyResults.searchCriteria"
            )},${translate("pages.surveyResults.visualization")},${translate(
                "pages.surveyResults.adequately"
            )},${translate("pages.surveyResults.interest")},${translate("pages.surveyResults.organized")},${translate(
                "pages.surveyResults.impression"
            )}\n` +
            data
                .map(
                    (row) =>
                        `${row.createdOn},${row.from},${row.classification},${row.foundLookingFor},${row.lookingFor},${row.searchCriteria},${row.visualization},${row.adequately},${row.interest},${row.organized},${row.impression}`
                )
                .join("\n");

        const encodedUri = encodeURI(csvContent);
        const link = document.createElement("a");
        link.setAttribute("href", encodedUri);
        link.setAttribute("download", "surveyResults.csv");
        document.body.appendChild(link);
        link.click();
    };

    if (isLoading) {
        return <Loader />;
    }

    return (
        <Flex direction="column" w={"100%"} overflowY="auto">
            <MainContentHeader titleId="nav.users.section.usersSurvey" />
            <Flex direction="column" w={"100%"} padding={4}>
                <Text fontSize="xl" mt="2" mb="2" color="grey">
                    {translate("pages.surveyResults.filter")}
                </Text>
                <label style={{ width: "300px", fontSize: 16 }} htmlFor="startDate">
                    {translate("pages.surveyResults.startDate")}
                </label>
                <Input
                    style={datePickerStyle}
                    max={endDate}
                    type="date"
                    id="startDate"
                    value={startDate}
                    onChange={(e: any) => {
                        setStartDate(e.target.value);
                    }}
                ></Input>
                <label style={{ width: "300px", fontSize: 16 }} htmlFor="endDate">
                    {translate("pages.surveyResults.endDate")}
                </label>
                <Input
                    style={datePickerStyle}
                    min={startDate}
                    type="date"
                    id="endDate"
                    value={endDate}
                    onChange={(e: any) => {
                        setEndDate(e.target.value);
                    }}
                ></Input>
                <Flex direction={"row"} gap={2} width="300px" justifyContent={"space-between"}>
                    <Button colorScheme="blue" w={"120px"} onClick={() => filterResults()}>
                        {translate("pages.surveyResults.filter")}
                    </Button>
                    <Button
                        colorScheme="blue"
                        w={"120px"}
                        onClick={() => {
                            setStartDate("");
                            setEndDate("");
                            setIsFiltered(false);
                            fetchSurveys();
                        }}
                    >
                        {translate("pages.surveyResults.clearFilters")}
                    </Button>
                </Flex>

                <TableContainer
                    w={"100%"}
                    overflowX={"auto"}
                    marginBottom={4}
                    marginTop={4}
                    border={"1px solid"}
                    borderColor="#dedede"
                    borderRadius={6}
                >
                    <Table variant="striped" size={"sm"}>
                        <Thead
                            backgroundColor={"#f0f0f0"}
                            height={"40px"}
                            borderBottom={"3px solid"}
                            borderColor="#8a8888"
                        >
                            <Tr>
                                <Th textTransform="none" fontSize={"14px"}>
                                    {translate("pages.surveyResults.dateTime")}
                                </Th>
                                <Th textTransform="none" fontSize={"14px"}>
                                    {translate("pages.surveyResults.from")}
                                </Th>
                                <Th textTransform="none" fontSize={"14px"}>
                                    {translate("pages.surveyResults.classification")}
                                </Th>
                                <Th textTransform="none" fontSize={"14px"}>
                                    {translate("pages.surveyResults.fwlf")}
                                </Th>
                                <Th textTransform="none" fontSize={"14px"}>
                                    {translate("pages.surveyResults.lookingFor")}
                                </Th>
                                <Th textTransform="none" fontSize={"14px"}>
                                    {translate("pages.surveyResults.searchCriteria")}
                                </Th>
                                <Th textTransform="none" fontSize={"14px"}>
                                    {translate("pages.surveyResults.visualization")}
                                </Th>
                                <Th textTransform="none" fontSize={"14px"}>
                                    {translate("pages.surveyResults.adequately")}
                                </Th>
                                <Th textTransform="none" fontSize={"14px"}>
                                    {translate("pages.surveyResults.interest")}
                                </Th>
                                <Th textTransform="none" fontSize={"14px"}>
                                    {translate("pages.surveyResults.organized")}
                                </Th>
                                <Th textTransform="none" fontSize={"14px"}>
                                    {translate("pages.surveyResults.impression")}
                                </Th>
                            </Tr>
                        </Thead>
                        <Tbody>
                            {surveys &&
                                surveys.map((survey: SurveyInfo) => {
                                    return (
                                        <Tr key={survey.id}>
                                            <Td>{survey.createdOn}</Td>
                                            <Td>{survey.from}</Td>
                                            <Td>{survey.classification}</Td>
                                            <Td>{survey.foundLookingFor}</Td>
                                            <Td>{survey.lookingFor}</Td>
                                            <Td>{survey.searchCriteria}</Td>
                                            <Td>{survey.visualization}</Td>
                                            <Td>{survey.adequately}</Td>
                                            <Td>{survey.interest}</Td>
                                            <Td>{survey.organized}</Td>
                                            <Td>{survey.impression}</Td>
                                        </Tr>
                                    );
                                })}
                        </Tbody>
                    </Table>
                </TableContainer>
                <Flex direction={direction} alignItems={"center"} justifyContent={"space-between"} gap={5}>
                    <Flex direction={"row"} gap={2} width="300px" justifyContent={justify}>
                        <Button
                            isDisabled={surveys.length > 0 ? false : true}
                            variant="geossOutline"
                            color="blue.500"
                            w={"120px"}
                            h={"30px"}
                            onClick={exportPDF}
                        >
                            Save pdf
                        </Button>
                        <Button
                            isDisabled={surveys.length > 0 ? false : true}
                            variant="geossOutline"
                            color="blue.500"
                            w={"120px"}
                            h={"30px"}
                            onClick={exportCSV}
                        >
                            Save csv
                        </Button>
                    </Flex>
                    <PagesControls
                        pagesInfo={pagesInfo}
                        numberOfElements={surveys.length}
                        fetchFunction={isFiltered ? filterResults : fetchSurveys}
                        isZeroFirst={true}
                    />
                </Flex>
            </Flex>
        </Flex>
    );
};
