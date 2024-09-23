import React from "react";
import UCListIcon from "../../icons/UCListIcon";

type Props = {
  isDarkBg?: boolean;
  title: string;
  title1: string;
  text1: string;
  title2: string;
  text2: string;
  title3: string;
  text3: string;
};

const UCList = ({
  isDarkBg = false,
  title,
  title1,
  text1,
  title2,
  text2,
  title3,
  text3,
}: Props) => {
  return (
    <section
      className={`w-full flex flex-col px-6 lg:px-48 py-20 lg:py-24 ${
        isDarkBg && "bg-[#F2F5F7]"
      } gap-24`}
    >
      <h2 className="text-2xl font-esabold">{title}</h2>
      <div className="w-full flex flex-col xl:flex-row gap-16 xl:gap-24">
        <div className="flex flex-col gap-4 xl:w-[33%]">
          <UCListIcon width="50px" height="50px" />
          <h3 className="text-2xl">{title1}</h3>
          <p>{text1}</p>
        </div>
        <div className="flex flex-col gap-4 xl:w-[33%]">
          <UCListIcon width="50px" height="50px" />
          <h3 className="text-2xl">{title2}</h3>
          <p>{text2}</p>
        </div>
        <div className="flex flex-col gap-4 xl:w-[33%]">
          <UCListIcon width="50px" height="50px" />
          <h3 className="text-2xl">{title3}</h3>
          <p>{text3}</p>
        </div>
      </div>
    </section>
  );
};

export default UCList;
