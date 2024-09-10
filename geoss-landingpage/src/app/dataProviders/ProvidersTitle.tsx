import React from "react";

const ProvidersTitle = () => {
  return (
    <section className="w-full flex flex-col px-6 lg:px-48 py-20 lg:py-24 gap-6">
      <h2 className="text-5xl lg:max-w-[50%]">
        {`GEOSS Yellow Pages: List of the Data Providers`}
      </h2>
      <p className="max-w-full lg:max-w-[50%] text-[#5C6369]">
        {`Join our vibrant community of data providers and contribute to the
        global knowledge network! Explore how you can become a data provider by
        visiting our website's "How to Become a Data Provider" page.`}
      </p>
    </section>
  );
};

export default ProvidersTitle;
