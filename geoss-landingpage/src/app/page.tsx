import Access from "./components/HomePage/Access";
import Carousels from "./components/HomePage/Carousels";
import Features from "./components/HomePage/Features";
import News from "./components/HomePage/News";
import TitleImage from "./components/HomePage/TitleImage";

export default function Home() {
  return (
    <div className="flex min-h-screen flex-col items-center justify-start">
      <TitleImage />
      <Access />
      <Features />
      <Carousels />
      <News/>
    </div>
  );
}
