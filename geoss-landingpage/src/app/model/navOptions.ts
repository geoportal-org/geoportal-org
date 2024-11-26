const portalUrl = process.env.NEXT_PUBLIC_PORTAL_URL

export const portalOptions = [
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

export const providersOptions = [
  {
    text: `How to Become a Data Provider`,
    href: `/usecases/yellow-pages-management`,
  },
  {
    text: `List of Data Providers`,
    href: `/data-providers`,
  },
];
