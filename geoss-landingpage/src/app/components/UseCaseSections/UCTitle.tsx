import React from "react";

type Props = {
  isDarkBg?: boolean;
  title: string;
  subtitle: string;
};

const UCTitle = ({ isDarkBg = false, title, subtitle }: Props) => {
  return (
    <section
      className={`w-full flex flex-col px-6 lg:px-48 py-20 lg:py-24 ${
        isDarkBg && "bg-[#F2F5F7]"
      } gap-6`}
    >
      <h2 className="text-6xl">{title}</h2>
      <p className="max-w-full xl:max-w-[30%] text-2xl text-[#5C6369]">
        {subtitle}
      </p>
    </section>
  );
};

export default UCTitle;
