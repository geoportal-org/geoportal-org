import type { Metadata } from "next";
import "./globals.css";
import Footer from "./components/Footer/Footer";
import NavHeader from "./components/Nav/NavHeader";

export const metadata: Metadata = {
  title: "Geoss landing page",
  description: "Generated by create next app",
};

export const revalidate = 60

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  const portalUrl = process.env.NEXT_PUBLIC_PORTAL_URL;
  const portalOptions = [
    {
      text: `About Community Portals`,
      href: `/usecases/community-portals`,
    },
    {
      text: `List of Community Portals`,
      subOptions: [
        {
          text: `All-Atlantic Ocean Data`,
          href: `${portalUrl}/community/aaod`,
        },
        {
          text: `AmeriGEO`,
          href: `${portalUrl}/community/amerigeoss`,
        },
        {
          text: `DBAR`,
          href: `${portalUrl}/community/dbar`,
        },
        {
          text: `Energic OD`,
          href: `${portalUrl}/community/energic-od`,
        },
        {
          text: `Envidat Community`,
          href: `${portalUrl}/community/envidat-community`,
        },
        {
          text: `GOS4M`,
          href: `${portalUrl}/community/gos4m`,
        },
        {
          text: `GTN-H Community`,
          href: `${portalUrl}/community/gtn-h-community`,
        },
      ],
    },
  ];
  return (
    <html lang="en">
      <body className="scrollbar font-esa">
        <NavHeader portalUrl={portalUrl} portalOptions={portalOptions}/>
        <main>{children}</main>
        <Footer />
      </body>
    </html>
  );
}
