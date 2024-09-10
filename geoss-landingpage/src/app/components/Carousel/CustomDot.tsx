import React from "react";

const CustomDot = ({ onClick, active, index, isFeature }: any) => {
  return (
    <li>
      <button
        className={`w-[30px] mx-2 h-[2px] ${
          active
            ? isFeature
              ? "bg-[#23272A]"
              : "bg-[#FFFFFF]"
            : isFeature
            ? "bg-[#A4ABB2]"
            : "bg-[#5C6369]"
        } cursor-pointer`}
        onClick={onClick}
        aria-label={`Go to page ${index + 1}`}
      />
    </li>
  );
};

export default CustomDot;
