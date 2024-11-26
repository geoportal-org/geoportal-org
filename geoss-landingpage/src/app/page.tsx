import Carousels from "./components/HomePage/Carousels";
import Features from "./components/HomePage/Features";
import News from "./components/HomePage/News";
import TitleImage from "./components/HomePage/TitleImage";

export default function Home() {
    const portalUrl = process.env.NEXT_PUBLIC_PORTAL_URL
    return (
        <div className="flex min-h-screen flex-col items-center justify-start">
            <TitleImage portalUrl={portalUrl}/>
            <Features portalUrl={portalUrl}/>
            <Carousels />
            <News />
        </div>
    );
}
