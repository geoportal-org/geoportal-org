import UCImage from "../components/UseCaseSections/UCImage";
import UCTitle from "../components/UseCaseSections/UCTitle";
import UCList from "../components/UseCaseSections/UCList";
import UCText from "../components/UseCaseSections/UCText";
import FindMore from "../components/FindMore";
import { findMoreLinksNewsPage } from "./findMoreLinks";

export const useCases = [
    {
        id: "creationTool",
        component: (
            <div className="text-black w-full">
                <UCTitle
                    isDarkBg
                    title="Community Portal Self-Creation Tool"
                    subtitle="Find out how to create your Community Portal adjusted for your needs."
                />
                <UCImage
                    side="right"
                    title="What is a Community Portal?"
                    text="The Community Portal of the GEOSS Portal is a specialized feature designed to serve the needs of specific Earth Observation communities. It allows these groups to customize the portals tailoring it to their interests, such as a specific region, theme, or application area.\nThe Community Portal is designed to enhance the usability and relevance of the GEOSS Portal for specific user groups by allowing them to access and interact with the data in a way that is most meaningful to them. It supports a more focused and effective use of Earth observation data, tailored to the specific goals and requirements of different user communities."
                    imageSRC="/1.webp"
                    buttonText="Browse our Community Portals"
                    buttonHref={"/"}
                />
                <UCList
                    isDarkBg
                    title="How your organization can benefit from having a Community Portal?"
                    title1="Customized portal"
                    text1="Users of your community can personalize their portal to focus on specific datasets, tools, and services relevant to their domain."
                    title2="Integration with GEOSS"
                    text2="Your Community Portal is customized, but still integrated with the broader GEOSS framework, ensuring that your community resources are part of the larger network of Earth observation data."
                    title3="User-Specific Tools and Interfaces"
                    text3="The portal may include specialized tools, dashboards, and visualizations for the community's particular needs, helping users to analyse, visualize, and utilize EO data more effectively."
                />
                <UCImage
                    side="left"
                    title="How to create a dedicated Community Portal?"
                    text={`The User interested in creating a Community Portal, once logged in into the GEOSS Portal, can access a download page where the tool/package link is present and download a ready-to-be-installed web page template.  The user is enabled to install the downloaded tool and customize the graphic user interface (GUI) according to the user’s preference, specified in the user requirements\nFinally, representatives of a specific community are enabled, previa acceptance by the opportune governance body, to make available a community specific portal providing capabilities for discovering and accessing data of interest for their community.`}
                    imageSRC="/1.webp"
                    buttonText="If you're interested, contact us"
                    buttonHref="geoss_platform_support@esa.int"
                />
            </div>
        ),
    },
    {
        id: "yellowPagesManagement",
        component: (
            <div className="text-black w-full">
                <UCTitle
                    isDarkBg
                    title="GEOSS Yellow Pages Management"
                    subtitle="Find out how to register on Yellow Pages and become our data provider."
                />
                <UCImage
                    side="right"
                    title="What are Yellow Pages?"
                    text="The GEOSS Yellow pages service implements the simplified registration process for new Data Providers. In this way, the provider can easily publish the data on GEOSS platform and users can be confident that the data available on GEOSS portal come from accredited providers."
                    imageSRC="/1.webp"
                    buttonText="Discover our data provider"
                    buttonHref={"https://www.geoportal.org/community/guest/yellow-pages"}
                />
                <UCList
                    isDarkBg
                    title="What are the benefits of registering to the Yellow Pages?"
                    title1="Visibility of your data in GEOSS"
                    text1="YP enable data providers to make their data visible in GEOSS and to attract the GEOSS users"
                    title2="Verified providers"
                    text2="By providing a centralized providers registration, the Yellow Pages guarantee that the data exposed on GEOSS platform are offered by verified providers."
                    title3="Support to Global Initiatives"
                    text3="Registering on GEOSS allows you to contribute to global efforts in environmental monitoring, disaster risk reduction, climate adaptation, and other critical areas."
                />
                <UCImage
                    side="left"
                    title="How to become our data provider?"
                    text={`In order to register as GEOSS Data Provider, a link is provided to a website where registration can be done.  The Data Provider provides the information needed for the registration in the form presented on the website and submits the registration request.`}
                    imageSRC="/1.webp"
                    buttonText="Register"
                    buttonHref="/dataProviders"
                />
            </div>
        ),
    },
    {
        id: "freeTextSpeach",
        component: (
            <div className="text-black w-full">
                <UCTitle
                    isDarkBg
                    title="Semantically Enabled Free-Text Search"
                    subtitle="A cognitive search module, enhancing data discovery in GEOSS datasets with semantic-enabled search options."
                />
                <UCImage
                    side="right"
                    title="What is Semantically Enabled Free-Text Search?"
                    text="Within the EIFFEL H2020 project, a Large Language Model-based Cognitive search module has been developed and fine-tuned for the Climate Change domain. The module allows the user to pose free text queries for data discovery in GEOSS core datasets available in the GEOSS Portal. In this case study, the cognitive search functionality was integrated and fused with the filtering capabilities of the GEOSS portal. This enhances the overall user experience, as well as the speed and quality of data discovery."
                    imageSRC="/1.webp"
                    buttonText=""
                    buttonHref={""}
                />
                <UCList
                    isDarkBg
                    title="What are the advantages of Semantically enabled free-text search?"
                    title1="Improved Relevance of Results"
                    text1="Semantic search interprets the meaning of a query and resolves ambiguous queries by understanding the context."
                    title2="Better User Experience"
                    text2="Users can search using natural language, without worrying about the exact keywords."
                    title3="Enhanced Discovery"
                    text3="Semantic search expands the scope of results, allowing users to discover information they might not have found through exact keyword matching."
                />
                <UCImage
                    side="left"
                    title="How to use the semantic-enabled search in GEOSS?"
                    text={`GEOSS users through the GEOSS portal user interface have the chance to use three options to search the data they need. The first is the conventional text search, with which the user searches for data in a free text form. Other options are the semantic “Search and then filter” or “Filter and then search”, in both cases the user receives a ranked list of data and can filter the results through the GEOSS Portal’s filters (e.g. spatial filtering on the map). As results, the semantically relative and filtered ranked list is depicted in the GEOSS Portal user interface.`}
                    imageSRC="/1.webp"
                    buttonText=""
                    buttonHref=""
                />
            </div>
        ),
    },
    {
        id: "dataDiscoveryAndAccess",
        component: (
            <div className="text-black w-full">
                <UCTitle
                    isDarkBg
                    title="AI-Powered Data Discovery and Access"
                    subtitle="The Artificial Intelligence (AI)-powered search feature, using Machine Learning (ML), improves user experience and data accessibility, thus supporting environmental and policy goals."
                />
                <UCImage
                    side="right"
                    title="What is AI-Powered Data Discovery and Access?"
                    text="The increasing volume and complexity of Earth Observation data make it challenging for users to find relevant datasets efficiently. Users face difficulties in browsing, downloading, and understanding data, along with the need for personalized data recommendations. An AI-powered search feature addresses these issues by leveraging natural language processing (NLP) and machine learning to improve user experience and data accessibility, thus supporting environmental and policy goals."
                    imageSRC="/1.webp"
                    buttonText=""
                    buttonHref={""}
                />
                <UCList
                    isDarkBg
                    title="Which are the advantages of AI-Powered Data Discovery and Access?"
                    title1="User Experience Improvement"
                    text1="The overall user experience is enhanced by making data discovery and access more intuitive and efficient."
                    title2="Data Accessibility"
                    text2="Accessibility and usability of complex Earth Observation datasets are increased thanks to cutting-edge AI technologies improving data processing and retrieval capabilities."
                    title3="Scalability and Adaptability"
                    text3="The solution can be scaled and adapted to various contexts and user needs."
                />
                <UCImage
                    side="left"
                    title="How to use the AI-Powered Data Discovery and Access in the GEOSS Portal?"
                    text={`The AI-powered search feature allows users to input natural language queries in the search bar to discover relevant Earth Observation datasets. The AI processes the queries, understands the intent and context, and provides users a list of suggested datasets relevant to the query, download instructions, and related datasets.`}
                    imageSRC="/1.webp"
                    buttonText=""
                    buttonHref=""
                />
            </div>
        ),
    },
    {
        id: "landDegradation",
        component: (
            <div className="text-black w-full">
                <UCTitle
                    isDarkBg
                    title="Land Degradation Use Case"
                    subtitle="Discover how to get information about Land Degradation from GEOSS Portal."
                />
                <UCImage
                    side="right"
                    title="Land Degradation Use Case description"
                    text="“I would briefly introduce here SDG and SDG 15.3.1 in particular and clarify that this use case is the computation of this indicator.”"
                    imageSRC="/1.webp"
                    buttonText="See the service (beta version)"
                    buttonHref={
                        "https://geoss.uat.esaportal.eu/?m:activeLayerTileId=osm&targetId=geo_essential_sdg_15.3.1_service&f:phrase=land%20degradation&f:dataSource=services"
                    }
                />
                <UCImage
                    isDarkBg
                    side="left"
                    title="Explore the Land Degradation Use Case."
                    text={`When a user searches for “Land degradation”, a number of resources that matches this search criteria are provided. 	The user can discover different data, services and knowledge and their relationship associated to the SDG 15.3.1 indicator. A dedicated model to compute the SDG indicator is available. The user can inspect the process workflow and search and select data as input to the service. Outputs can be visualized in a dedicated dashboard, with the possibility to share it to the community.`}
                    imageSRC="/1.webp"
                    buttonText="Explore an example of the service output"
                    buttonHref="https://geoss.uat.esaportal.eu/?f:phrase=dashboard&m:activeLayerTileId=osm&f:dataSource=information"
                />
            </div>
        ),
    },
    {
        id: "greenSpaces",
        component: (
            <div className="text-black w-full">
                <UCTitle
                    isDarkBg
                    title="Accessibility to Urban Green Spaces (SDG11.7) Use Case"
                    subtitle="Find out how to calculate urban green space accessibility (SDG 11.7) at the city level using GEOSS Portal, addressing issues related to urban sprawl and efficient land use."
                />
                <UCImage
                    side="right"
                    title="Accessibility to Urban Green Spaces (SDG11.7) Use Case Description"
                    text={`Currently, more than half of the world population live in cities and this number is likely to increase to 66% by 2050. In Europe, about 75% of the population already lives in urban areas, and recent projections estimate that the share of urban population in Europe will increase to 80% by 2050.\n\nConsequently, urban sprawl and inefficient use of land are important issues with many consequences. These issues not only put pressure on urban infrastructures, but also have significant impacts on the use of open and green spaces. Therefore, there is a strong need to optimise the use of available space requiring efficient and effective land use management strategies to enhance inclusive and sustainable urbanisation.\n\nThe EU SDG indicator “Share of urban population without green urban areas in their neighbourhood” is one of the proposed indicators currently on-hold due to methodological issues and lack of data. The proposed EU indicator emphasises the importance of the green portion of the public urban space because it is a significant contribution of nature to people (i.e., a nature-based solution) providing valuable ecosystem services.\n\nGreen spaces contribute in various ways to climate change mitigation (e.g., shade and moisture provision, impact of heatwaves and noise reduction, air filtration and biodiversity promotion), and are also important for social inclusiveness, human health, biodiversity, as well as offering an opportunity for social interaction and increasing people’s quality of life.\n\nThis Use Case provides the possibility to calculate the accessibility to urban green spaces (SDG11.7) at the city scale using the GEOSS platform.`}
                    imageSRC="/1.webp"
                    buttonText=""
                    buttonHref={""}
                />
                <UCImage
                    isDarkBg
                    side="left"
                    title="What users can do about Urban Green Spaces (SDG11.7)?"
                    text={`Through the GEOSS Portal, users can calculate urban green space accessibility (SDG 11.7) at the city level, addressing issues related to urban sprawl and efficient land use. At the end of the calculation, the user can create a Dashboard to show usable information for accessibility to urban green space.\n\nThrough the GEOSS platform, the user can search for “Urban green spaces”, he/she obtains a number of resources that matches his search criteria, then can selects the most suitable sub-indicators available in the GEOSS platform for his/her needs. Then the user discovers a dedicated model to compute the SDG indicator and realizes that there is a Service associated to this model. The GEOSS Platform associates the model to the actual processing services that enable its computation, which the user can access and run in a user-friendly way. In particular, the user can inspect the process workflow and search and select data as input to the service. In addition, the user has the capability to choose a Cloud computing platform of preference among the available. After selected the indicators and model, the user can now start the computation and wait for the results in a dedicated dashboard (that can be further elaborated/modified and shared afterwards). The created dashboard can be saved, or shared with other specific users and can essentially become a new data provider.`}
                    imageSRC="/1.webp"
                    buttonText=""
                    buttonHref=""
                />
            </div>
        ),
    },
    {
        id: "polution",
        component: (
            <div className="text-black w-full">
                <UCTitle
                    isDarkBg
                    title="Nutrient Pollution in European Inland and Coastal Waters Use Case"
                    subtitle="Find out how to quantify nutrient pollution in European waters, thanks to a model developed by CNR-IIA in collaboration with the Joint Research Center (JRC), supporting the EU's Zero Pollution Action Plan."
                />
                <UCImage
                    side="right"
                    title="Nutrient Pollution in European Inland and Coastal Waters” Use Case Description"
                    text={`In Europe, intensive agricultural practices together with high population density represent important sources of nutrients for fresh and coastal waters. Nutrient pollution is one of the major pressures on European aquatic ecosystems altering their condition. At present in the EU more than half of water bodies are not in good ecological status, with nutrient being one of the major causes of degradation.\n\nAmbitious water policies are in place in the European Union for protecting and restoring aquatic ecosystems. Among these, the Urban Wastewater Treatment Directive (UWWTD) has the objective to “protect the environment from adverse effects of wastewater discharges from urban sources and specific industries”.\n\nOne of the objectives is to improve water quality by addressing remaining urban wastewater pollution. To this aim, the GREEN  model, developed by the EC Joint Research Centre, was utilized to quantify the current pressures of point and diffuse nitrogen and phosphorus emissions to European fresh and coastal waters and analyze the effects of different policy scenarios of nutrient reduction.\n\nThe GEOSS Platform allows users to run this model on a selected area of interest.`}
                    imageSRC="/1.webp"
                    buttonText=""
                    buttonHref={""}
                />
                <UCImage
                    isDarkBg
                    side="left"
                    title="Find out how the user can quantify nutrient pollution in the GEOSS Portal?"
                    text={`Through the GEOSS portal, users can select an area of interest and define the settings for the GREEN model execution, including different policy scenarios. GREEN model runs through the Virtual Earth Laboratory (VLab), which utilises the European cloud computing platforms, including the Copernicus DIAS platforms and the European Open Science Cloud, for the execution of scientific models. At the end of the execution, the user is able to visualize different simulation results: the computed yearly average load of nitrogen/phosphorus in the entire selected area, according to the selected policy scenario; the comparison between the calculated total yearly average load, both to the river outlet and in the entire region, for different simulated policy scenarios and a map displaying the difference of nitrogen/phosphorus loads over the entire area of interest, according to two selected policy scenarios.`}
                    imageSRC="/1.webp"
                    buttonText=""
                    buttonHref=""
                />
            </div>
        ),
    },
    {
        id: "biomass",
        component: (
            <div className="text-black w-full">
                <UCTitle
                    isDarkBg
                    title="Above Ground Biomass (AGB)"
                    subtitle="Find out how to map biomass using remote sensing imagery and machine learning models via GEOSS, relevant for carbon stock quantification and ecosystem services."
                />
                <UCImage
                    side="right"
                    title="Above Ground Biomass (AGB)” Use Case Description"
                    text={`The quantification of forest above-ground biomass (AGB) over large areas is used as a proxy for the quantification of carbon stocks, particularly referring to Reduced Emissions from Deforestation and forest Degradation (REDD+) projects, for the quantification of forest resources and ecosystem services, the creation of fuel maps to be used as input to wildfires spread models, and for biodiversity and climate change models. According to the definition of the Intergovernmental Panel on Climate Change (IPCC 2006), Above-Ground Biomass is defined as “All living biomass above the soil including stem, stump, branches, bark, seeds and foliage”.\n\nThrough the GEOSS portal, the user can produce maps of biomass based on vegetation indices (VIs) from remote sensing imagery and compare results obtained by different Machine Learning (ML) models.`}
                    imageSRC="/1.webp"
                    buttonText=""
                    buttonHref={""}
                />
                <UCImage
                    isDarkBg
                    side="left"
                    title="How to map and display biomass information through GEOSS Portal?"
                    text={`Through the GEOSS Portal, the user can select an area of interest and generate the map of biomass based on vegetation indices (VIs) from remote sensing imagery. At the end of the execution, the user can create a Dashboard to display the AGB maps and show differences between maps calculated with different ML models.`}
                    imageSRC="/1.webp"
                    buttonText=""
                    buttonHref=""
                />
            </div>
        ),
    },
    {
        id: "agame",
        component: (
            <div className="text-black w-full">
                <UCTitle
                    isDarkBg
                    title="Gross Primary Production for Monitoring Ecosystem Health within GEOSS (AGAME)"
                    subtitle="Find out how to access time series data on gross primary production to study carbon uptake and predict biosphere conditions, integrated into GEOSS for broader accessibility."
                />
                <UCImage
                    side="right"
                    title="Gross Primary Production for Monitoring Ecosystem Health within GEOSS (AGAME)” Use Case Description"
                    text={`AGAME is a project that provides up-to-date, reliable data about gross primary production (GPP), which helps scientists and decision-makers understand how much carbon ecosystems take in. This information is important for creating environmental policies and making smart decisions about managing our planet's resources. This data is especially useful for people working in biodiversity, from researchers to environmental planners.\n\nAGAME makes available through the GEOSS Platform explicit time series data on gross primary production (GPP). This information is crucial to investigate declines in carbon uptake due to global warming and to predict future biosphere conditions influenced by changing climate and land use.\n\nAGAME uses data from a variety of sources, including the Copernicus Program (Sentinel satellites), and combines it with advanced modelling to provide a full picture of what’s happening on the ground.`}
                    imageSRC="/1.webp"
                    buttonText=""
                    buttonHref={""}
                />
                <UCImage
                    isDarkBg
                    side="left"
                    title="How the AGAME project works within GEOSS?"
                    text={`Through the GEOSS platform, the user can access to the Gross Primary Production data products of the AGAME project using the searching key “Primary Productivity”. These products are shared as static data products into the GEOSS environment.`}
                    imageSRC="/1.webp"
                    buttonText=""
                    buttonHref=""
                />
            </div>
        ),
    },
    {
        id: "insitu",
        component: (
            <div className="text-black w-full">
                <UCTitle
                    isDarkBg
                    title="Harmonized In-Situ Data for crop mapping (MAPS4GPP)"
                    subtitle="Harmonized In-Situ Data for crop mapping (MAPS4GPP)."
                />
                <UCImage
                    side="right"
                    title="Harmonized In-Situ Data for crop mapping (MAPS4GPP)” Use Case description."
                    text={`There are several institutes, projects and programs providing in-situ data in a standardized way (data providers). Currently, there is no central place where these different data sets can be found and used. One of these data providers is the ESA funded WorldCereal project, that collected and harmonized in-situ reference data for crop mapping. The WorldCeral repository contains data from different sources with providers like GEOGLAM-JECAM sites, the Radiant MLHub, the Future Harvest (CGIAR) centers, LACO-Wiki, Geo-Wiki and data from individual project contributions. It would greatly help the GEOGLAM community  (Group on Earth Observations Global Agricultural Monitoring Initiative) to open the in-situ reference data via a hub such as the GEOSS platform. Users can explore and download data (human-readable) and integrate in-situ reference data in dedicated applications (machine-readable). The harmonized in situ reference data, available through the GEOSS platform, can be used to train crop classification algorithms and produce a crop map based on all available reference data shared by the community providing actionable information on the state, change, and forecast of agricultural land use and productivity.`}
                    imageSRC="/1.webp"
                    buttonText=""
                    buttonHref={""}
                />
                <UCImage
                    isDarkBg
                    side="left"
                    title="How to exploring WorldCereal data and generate crop maps using GEOSS Platform?"
                    text={`TThrough the GEOSS Portal, a user can explore the data defining an area, growing season of interest, and optionally a crop type and quality level, after which the WorldCereal processing system generates a map showing the presence crop type or only the selected crop type. The map can be visualized and saved in the user’s personal space in the GEOSS platform.`}
                    imageSRC="/1.webp"
                    buttonText=""
                    buttonHref=""
                />
            </div>
        ),
    },
];
