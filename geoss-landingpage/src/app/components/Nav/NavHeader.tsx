"use client";
import { useMediaQuery } from "@/src/app/hooks/useMediaQuery";
import NavMobile from "./NavMobile";
import NavDesktop from "./NavDesktop";

const NavHeader = ({portalUrl, portalOptions}: any) => {
  const isMobile = useMediaQuery("(max-width: 1023px)");

  return (
    <div className="sticky z-[9999] top-0 relative flex items-center justify-between w-full bg-white h-[48px] lg:h-[86px] text-black lg:px-48">
      {isMobile ? <NavMobile portalUrl={portalUrl} portalOptions={portalOptions}/> : <NavDesktop portalUrl={portalUrl} portalOptions={portalOptions}/>}
    </div>
  );
};

export default NavHeader;
