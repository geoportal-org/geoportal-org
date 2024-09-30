"use client";
import React, { useState } from "react";
import { LinkData } from "../model/types";
import DropdownArrow from "../icons/DropdownArrow";
import { motion, AnimatePresence } from "framer-motion";
import { v4 as uuidv4 } from "uuid";
import Link from "next/link";
import parse from "html-react-parser";

type Option = {
  text: string;
  href?: string;
  subOptions?: LinkData[];
};

type Props = {
  text: string;
  options?: Option[];
  className?: string;
  textAccordion?: boolean;
  content?: string;
};

const Accordion = ({
  text,
  options = [],
  className,
  textAccordion = false,
  content = "",
}: Props) => {
  const [accordionOpen, setAccordionOpen] = useState(false);
  return (
    <div className={`w-full ${className ? className : ""}`}>
      <AnimatePresence>
        <button
          className="flex items-center justify-between w-full"
          onClick={() => setAccordionOpen(!accordionOpen)}
        >
          <span className={`${textAccordion && "text-xl py-6"} text-left`}>
            {text}
          </span>
          <motion.div animate={{ rotate: accordionOpen ? 180 : 0 }}>
            <DropdownArrow
              width={`${textAccordion ? "30px" : "15px"}`}
              height={`${textAccordion ? "30px" : "15px"}`}
              color={`${textAccordion ? "#23272A" : "black"}`}
            />
          </motion.div>{" "}
        </button>
        {accordionOpen && (
          <motion.div
            key={uuidv4()}
            initial={{ opacity: 0, y: -20 }}
            animate={{ opacity: 1, y: 0 }}
            exit={{ opacity: 0, y: -20 }}
            transition={{ ease: "easeInOut", duration: 0.1, delay: 0.1 }}
            className={`${!textAccordion && "mt-4 pl-4"}`}
          >
            {textAccordion && content ? (
              <p className="p-4 whitespace-pre-wrap text-[#5C6369]">
                {parse(content)}
              </p>
            ) : (
              <ul className="text-sm text-black w-full divide-y">
                {options.map((option: Option) => {
                  return (
                    <li className="p-4" key={uuidv4()}>
                      {option.subOptions ? (
                        <Accordion
                          text={option.text}
                          options={option.subOptions}
                        />
                      ) : (
                        <Link target="_blank" href={option.href || "/"} className="block">
                          {option.text}
                        </Link>
                      )}
                    </li>
                  );
                })}
              </ul>
            )}
          </motion.div>
        )}
      </AnimatePresence>
    </div>
  );
};

export default Accordion;
