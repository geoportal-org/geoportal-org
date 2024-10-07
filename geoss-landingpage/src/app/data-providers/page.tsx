import { Suspense } from "react";
import ProvidersTitle from "./ProvidersTitle";
import ProvidersSection from "./ProvidersSection";

const page = async () => {
  return (
    <div className="w-full text-black">
      <ProvidersTitle />
      <Suspense
        fallback={
          <div className="w-full px-6 lg:px-48 py-20 lg:py-24 text-center justify-center min-h-[40vh] italic">Loading...</div>
        }
      >
        <ProvidersSection />
      </Suspense>
    </div>
  );
};

export default page;
