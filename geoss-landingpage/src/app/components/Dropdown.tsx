"use client";
import React, {
  Dispatch,
  SetStateAction,
  useEffect,
  useRef,
  useState,
} from "react";
import { LinkData } from "../model/types";
import Accordion from "./Accordion";
import { motion, AnimatePresence } from "framer-motion";
import DropdownArrow from "../icons/DropdownArrow";
import { v4 as uuidv4 } from "uuid";
import Link from "next/link";

type Option = {
  text: string;
  href?: string;
  subOptions?: LinkData[];
  value?: string;
};

type Props = {
  className?: string;
  text: string;
  options: Option[];
  onChange?: Dispatch<SetStateAction<any>>;
};

const Dropdown = ({ className, options, text, onChange }: Props) => {
  const [dropDownOpen, setDropDownOpen] = useState(false);
  const dropDownRef = useRef(null);

  useEffect(() => {
    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      // dispose
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [dropDownRef]);

  const handleClickOutside = (e: any) => {
    //@ts-ignore
    if (!dropDownRef.current?.contains(e.target)) {
      setDropDownOpen(false);
    }
  };

  return (
    <div
      ref={dropDownRef}
      className={`${className ? className : null} relative text-black`}
    >
      <AnimatePresence>
        <button
          tabIndex={0}
          onClick={() => setDropDownOpen(!dropDownOpen)}
          className={`flex items-center justify-between h-full w-full py-2 rounded gap-2 ${onChange && 'px-4'}`}
        >
          {onChange ? (
            <p className="text-[#5C6369]">
              Sort by: <span className="text-black">{text}</span>
            </p>
          ) : (
            text
          )}
          <motion.div animate={{ rotate: dropDownOpen ? 180 : 0 }}>
            <DropdownArrow width="15px" height="15px" color="black" />
          </motion.div>
        </button>
        {dropDownOpen && (
          <motion.div
            key={uuidv4()}
            initial={{ opacity: 0, y: -20 }}
            animate={{ opacity: 1, y: onChange ? 5 : 0 }}
            exit={{ opacity: 0, y: -20 }}
            transition={{ ease: "easeInOut", duration: 0.1 }}
            className={`absolute shadow-lg ${
              onChange ? "w-full" : "w-[140%]"
            } z-10 font-normal rounded bg-white text-black flex grow`}
          >
            <ul className="text-sm w-full divide-y">
              {options.map((option: Option) => {
                return (
                  <li className="p-4" key={uuidv4()}>
                    {option.subOptions ? (
                      <Accordion
                        text={option.text}
                        options={option.subOptions}
                      />
                    ) : option.href ? (
                      <Link href={option.href} className="block">
                        {option.text}
                      </Link>
                    ) : onChange ? (
                      <button
                      className="w-full h-full text-start"
                        onClick={() => {
                          onChange(option);
                          setDropDownOpen(false);
                        }}
                      >
                        {option.text}
                      </button>
                    ) : null}
                  </li>
                );
              })}
            </ul>
          </motion.div>
        )}
      </AnimatePresence>
    </div>
  );
};

export default Dropdown;
