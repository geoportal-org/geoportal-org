import UCImage from "../components/UseCaseSections/UCImage";
import UCTitle from "../components/UseCaseSections/UCTitle";
import UCList from "../components/UseCaseSections/UCList";
import UCText from "../components/UseCaseSections/UCText";
import FindMore from "../components/FindMore";
import { findMoreLinksNewsPage } from "./findMoreLinks";

const data = [
  {
    href: 1,
    titleText: "GEOSS Community Portals",
    subTitleText:
      "Find out what are they, how to create one, and how your community can benefit from them.",
    videoTitle: "What are Community Portals?",
    videoText: `The GEOSS Portal is an online map-based user interface which allows users to discover and access Earth observation data and resources from different providers from all over the world. \n\nThe portal is implemented and operated by the European Space Agency and provides a single internet discovery and access point to the ever-growing quantities of heterogeneous collections of Earth observations from satellites, airplanes, drones and in-situ sensors at global, regional and local scales through the Global Earth Observation System of Systems (GEOSS).`,
    imageSRC: "/1.webp",
    videoButtonText: "See the list of Community Portals",
    videoButtonHref: "/",
    listTitle:
      "How your organization can benefit from having a Community Portal?",
    listTitle1: "Benefit 1",
    listText1:
      "Lorem ipsum dolor sit amet consectetur. A vestibulum egestas dapibus facilisis nunc at amet.",
    listTitle2: "Benefit 2",
    listText2:
      "Lorem ipsum dolor sit amet consectetur. A vestibulum egestas dapibus facilisis nunc at amet.",
    listTitle3: "Benefit 3",
    listText3:
      "Lorem ipsum dolor sit amet consectetur. A vestibulum egestas dapibus facilisis nunc at amet.",
    videoTitle2: "How to become a GEOSS Data Provider?",
    videoText2: `The GEOSS Portal is an online map-based user interface which allows users to discover and access Earth observation data and resources from different providers from all over the world.`,
    imageSRC2: "/1.webp",
    videoButtonText2: "Read about the Community Portal creation tool",
    videoButtonHref2: "/",
    textTitle: "How to become a GEOSS Data Provider?",
    textSubtext: `Lorem ipsum dolor sit amet consectetur. A vestibulum egestas dapibus facilisis nunc at amet. Morbi enim felis nec lectus leo feugiat dignissim molestie. Tortor cursus lorem hendrerit ullamcorper nibh sollicitudin felis. Mi orci duis id nam porttitor eu vel aliquam donec.\n\nLorem ipsum dolor sit amet consectetur. A vestibulum egestas dapibus facilisis nunc at amet. Morbi enim felis nec lectus leo feugiat dignissim molestie. Tortor cursus lorem hendrerit ullamcorper nibh sollicitudin felis. Mi orci duis id nam porttitor eu vel aliquam donec. Lorem ipsum dolor sit amet consectetur. A vestibulum egestas dapibus facilisis nunc at amet. Morbi enim felis nec lectus leo feugiat dignissim molestie. Tortor cursus lorem hendrerit ullamcorper nibh sollicitudin felis. Mi orci duis id nam porttitor eu vel aliquam donec.\n\nLorem ipsum dolor sit amet consectetur. A vestibulum egestas dapibus facilisis nunc at amet. Morbi enim felis nec lectus leo feugiat dignissim molestie. Tortor cursus lorem hendrerit ullamcorper nibh sollicitudin felis. Mi orci duis id nam porttitor eu vel aliquam donec.`,
  },
  {
    href: 2,
    titleText: "Community Portal Self-Creation Tool",
    subTitleText:
      "Find out how to create your Community Portal adjusted for your needs.",
    videoTitle: "What is a Community Portal?",
    videoText: `The Community Portal of the GEOSS Portal is a specialized feature designed to serve the needs of specific Earth Observation communities. It allows these groups to customize the portals tailoring it to their interests, such as a specific region, theme, or application area.\nThe Community Portal is designed to enhance the usability and relevance of the GEOSS Portal for specific user groups by allowing them to access and interact with the data in a way that is most meaningful to them. It supports a more focused and effective use of Earth observation data, tailored to the specific goals and requirements of different user communities.`,
    imageSRC: "/1.webp",
    videoButtonText: "Browse our Community Portals",
    videoButtonHref: "/",
    listTitle:
      "How your organization can benefit from having a Community Portal?",
    listTitle1: "Customized portal",
    listText1:
      "Users of your community can personalize their portal to focus on specific datasets, tools, and services relevant to their domain.",
    listTitle2: "Integration with GEOSS",
    listText2:
      "Your Community Portal is customized, but still integrated with the broader GEOSS framework, ensuring that your community resources are part of the larger network of Earth observation data.",
    listTitle3: "User-Specific Tools and Interfaces",
    listText3:
      "The portal may include specialized tools, dashboards, and visualizations for the community's particular needs, helping users to analyse, visualize, and utilize EO data more effectively.",
    videoTitle2: "How to create a dedicated Community Portal?",
    videoText2: `The User interested in creating a Community Portal, once logged in into the GEOSS Portal, can access a download page where the tool/package link is present and download a ready-to-be-installed web page template.  The user is enabled to install the downloaded tool and customize the graphic user interface (GUI) according to the user’s preference, specified in the user requirements\nFinally, representatives of a specific community are enabled, previa acceptance by the opportune governance body, to make available a community specific portal providing capabilities for discovering and accessing data of interest for their community.`,
    imageSRC2: "/1.webp",
    videoButtonText2: "If you're interested, contact us",
    videoButtonHref2: "geoss_platform_support@esa.int",
    textTitle: "",
    textSubtext: "",
  },
];

export const useCases = [
  {
    id: "test",
    component: (
      <div className="text-black w-full">
        <UCTitle
          isDarkBg
          title={data[0].titleText}
          subtitle={data[0].subTitleText}
        />
        <UCImage
          side="right"
          title={data[0].videoTitle}
          text={data[0].videoText}
          imageSRC={data[0].imageSRC}
          buttonText={data[0].videoButtonText}
          buttonHref={data[0].videoButtonHref}
        />
        <UCList
          isDarkBg
          title={data[0].listTitle}
          title1={data[0].listTitle1}
          text1={data[0].listText1}
          title2={data[0].listTitle2}
          text2={data[0].listText2}
          title3={data[0].listTitle3}
          text3={data[0].listText3}
        />
        <UCImage
          side="left"
          title={data[0].videoTitle2}
          text={data[0].videoText2}
          imageSRC={data[0].imageSRC2}
          buttonText={data[0].videoButtonText2}
          buttonHref={data[0].videoButtonHref2}
        />
        <UCText isDarkBg title={data[0].textTitle} text={data[0].textSubtext} />
        <FindMore links={findMoreLinksNewsPage} />
      </div>
    ),
  },
  {
    id: "creationTool",
    component: (
      <div className="text-black w-full">
        <UCTitle
          isDarkBg
          title={data[1].titleText}
          subtitle={data[1].subTitleText}
        />
        <UCImage
          side="right"
          title={data[1].videoTitle}
          text={data[1].videoText}
          imageSRC={data[1].imageSRC}
          buttonText={data[1].videoButtonText}
          buttonHref={data[1].videoButtonHref}
        />
        <UCList
          isDarkBg
          title={data[1].listTitle}
          title1={data[1].listTitle1}
          text1={data[1].listText1}
          title2={data[1].listTitle2}
          text2={data[1].listText2}
          title3={data[1].listTitle3}
          text3={data[1].listText3}
        />
        <UCImage
          side="left"
          title={data[1].videoTitle2}
          text={data[1].videoText2}
          imageSRC={data[1].imageSRC2}
          buttonText={data[1].videoButtonText2}
          buttonHref={data[1].videoButtonHref2}
        />
      </div>
    ),
  },
];
