import { portalOptions } from "@/src/app/model/portalOptions";
import Link from "next/link";
import React, { useEffect, useState } from "react";
import Accordion from "../Accordion";
import CrossIcon from "@/src/app/icons/CrossIcon";
import BurgerMenuIcon from "@/src/app/icons/BurgerMenuIcon";
import Image from "next/image";
import esaImage from "@/public/ESA.webp";
import GOEAImage from "@/public/GOEA.webp";
import uniFlagImage from "@/public/UE.webp";
import { usePathname } from "next/navigation";

const NavMobile = () => {
  const [burgerMenuOpen, setBurgerMenuOpen] = useState(false);
  const pathname = usePathname();

  useEffect(() => {
    setBurgerMenuOpen(false);
  }, [pathname]);

  return (
    <div
      className={`lg:hidden flex flex-row items-center justify-between h-full w-full overflow-x-hidden ${
        !burgerMenuOpen && "p-4"
      }`}
    >
      <Link href="/">
        <Image
          alt="geoss-logo"
          width={150}
          height={30}
          src={"/portalLogo.webp"}
        />
      </Link>
      <button
        className="hover:scale-105"
        onClick={() => setBurgerMenuOpen(true)}
      >
        <BurgerMenuIcon width="30px" height="30px" color="#262626" />
      </button>
      {burgerMenuOpen && (
        <div className="scrollbar flex flex-col items-center justify-between z-30 fixed bottom-0 top-0 w-full overflow-y-scroll bg-white">
          <div className="w-full flex items-center min-h-[48px] pl-4 mb-2">
            <Link href="/">
              <Image
                alt="geoss-logo"
                width={150}
                height={30}
                src={"/portalLogo.webp"}
              />
            </Link>
            <button
              className="ml-auto"
              onClick={() => setBurgerMenuOpen(false)}
            >
              <CrossIcon
                className="hover:scale-125"
                width="2em"
                height="2em"
              />
            </button>
          </div>
          <nav className="relative flex w-full px-4">
            <ul className="flex flex-col items-center w-full h-full divide-y">
              <li className="w-full py-4">
                <Link href="/about">About</Link>
              </li>
              <li className=" w-full py-4">
                <Accordion options={portalOptions} text="Community portals" />
              </li>
              <li className="w-full py-4">
                <Link href="/data-providers">Data providers</Link>
              </li>
              <li className="w-full py-4">
                <Link href="/news">News</Link>
              </li>
              <li className="w-full py-4">
                <Link target="_blank" href="https://www.geoportal.org/">
                  Data Access
                </Link>
              </li>
            </ul>
          </nav>
          <div className="relative flex flex-row items-center justify-around w-full mt-auto py-4">
            <Image alt="GOEA image" width={90} height={30} src={esaImage} />
            <Image alt="ESA image" width={90} height={30} src={GOEAImage} />
            <Image
              alt="UE flag image"
              width={50}
              height={30}
              src={uniFlagImage}
            />
          </div>
        </div>
      )}
    </div>
  );
};

export default NavMobile;
