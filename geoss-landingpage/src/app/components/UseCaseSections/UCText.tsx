import React from "react";

type Props = {
  isDarkBg?: boolean;
  title: string;
  text: string;
};

const UCText = ({ isDarkBg, title, text }: Props) => {
  return (
    <section
      className={`w-full flex flex-col xl:flex-row px-6 lg:px-48 py-20 lg:py-24 ${
        isDarkBg && "bg-[#F2F5F7]"
      } gap-16 lg:gap-24`}
    >
      <h2 className="text-5xl xl:w-[35%]">{title}</h2>
      <span className="xl:max-w-[65%] gap-16 xl:columns-2 whitespace-pre-wrap text-[#5C6369]">{text}</span>
    </section>
  );
};

export default UCText;
