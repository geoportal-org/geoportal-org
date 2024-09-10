import React from "react";
import NotFoundPage from "../../not-found";
import { useCases } from "../../model/useCases";

const getServerSideProps = (id: string) => {
  return useCases.find((useCase) => useCase.id === id) || null
};

const page = async ({ params }: { params: { id: string } }) => {
  try {
    const useCaseData = await getServerSideProps(params.id);
    if (useCaseData) {
      return useCaseData.component;
    } else {
      return <NotFoundPage />;
    }
  } catch (e: any) {
    return <div>Error fetching data: {JSON.stringify(e)}</div>;
  }
};

export default page;
