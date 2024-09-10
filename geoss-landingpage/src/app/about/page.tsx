import React from "react";
import Questions from "./Questions";
import LearnMore from "./LearnMore";
import InfoGraphic from "./InfoGraphic";
import FindMore from "../components/FindMore";
import { findMoreLinksAboutPage } from "../model/findMoreLinks";

const page = () => {
  return (
    <div className="w-full text-black">
      <Questions />
      <LearnMore />
      <InfoGraphic />
      <FindMore links={findMoreLinksAboutPage}/>
    </div>
  );
};

export default page;
