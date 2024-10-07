import { portalOptions } from "@/src/app/model/portalOptions";
import Link from "next/link";
import React from "react";
import Dropdown from "../Dropdown";
import esaImage from "@/public/ESA.webp";
import GOEAImage from "@/public/GOEA.webp";
import uniFlagImage from "@/public/UE.webp";
import Image from "next/image";

const NavDesktop = () => {
  return (
    <div className="max-lg:hidden flex w-full items-center justify-between">
      <nav>
        <ul className="flex flex-row items-center gap-2 xl:gap-4 h-full">
          <li className="xl:mr-12">
            <Link href="/">
              <Image alt="geoss-logo" width={60} height={60} src="/logo.webp" />
            </Link>
          </li>
          <li>
            <Link href="/about">About</Link>
          </li>
          <li>
            <Dropdown options={portalOptions} text="Community portals" />
          </li>
          <li>
            <Link href="/data-providers">Data providers</Link>
          </li>
          <li>
            <Link href="/news">News</Link>
          </li>
          <li>
            <Link target="_blank" href="https://www.geoportal.org/">Data Access</Link>
          </li>
        </ul>
      </nav>
      <div className="flex flex-row items-center h-full gap-4">
        <Image alt="GOEA image" width={80} height={40} src={GOEAImage} />
        <Image alt="ESA image" width={60} height={40} src={esaImage} />
        <Image alt="UE flag image" width={50} height={40} src={uniFlagImage} />
      </div>
    </div>
  );
};

export default NavDesktop;
