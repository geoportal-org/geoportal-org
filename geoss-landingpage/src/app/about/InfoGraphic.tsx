import React from "react";
import Image from 'next/image'
import infoGraphic from '@/public/infoGraphic.webp'

const InfoGraphic = () => {
  return (
    <section className="w-full px-6 lg:px-48 py-20 lg:py-24">
      <Image
        className="w-full object-cover object-left"
        alt="infoGraphic"
        src={infoGraphic}
      />
      <p className="w-full text-center text-[#5C6369] mt-4">
        The overview of the GEOSS Platform and its main components
      </p>
    </section>
  );
};

export default InfoGraphic;
