const portalUrl = process.env.NEXT_PUBLIC_PORTAL_URL

export const catalogueCarouselOptions = [
  {
    text: `AtlantOS`,
    href: `${portalUrl}/?f:sources=atlantosid`,
    img: `/atlantOS-catalog.webp`,
  },
  {
    text: `CCI Open Data`,
    href: `${portalUrl}/?f:sources=cciodpdbid`,
    img: `/cci-open-data.webp`,
  },
  {
    text: `CEOS WGISS Integrated Catalog`,
    href: `${portalUrl}/?f:sources=UUID-d9a3ab80-f5e1-43c3-92cd-fbbbf005aded`,
    img: `/wgiss-integrated-catalog.webp`,
  },
  {
    text: `China GEOSS`,
    href: `${portalUrl}/?f:sources=chinageosatellite`,
    img: `/china-geoss.webp`,
  },
  {
    text: `Digital Globe`,
    href: `${portalUrl}/?f:sources=digitalglobeid`,
    img: `/digital-globe.webp`,
  },
  {
    text: `European Environment Agency SDI Catalog`,
    href: `${portalUrl}/?f:sources=UUID-456602db-4275-4410-8b68-436fd23ace69`,
    img: `/european-environment-agency-catalog.webp`,
  },
  {
    text: `Federated EO Gateway (FedEO)`,
    href: `${portalUrl}/?f:sources=UUID-9056c936-ecd2-4b82-b448-f9619b9ede1c`,
    img: `/federated-eo-gateway-ceos.webp`,
  },
  {
    text: `Global Mercury Observing system (GMOS)`,
    href: `${portalUrl}/?f:sources=atlantosid`,
    img: `/global-mercury-observing-system-database.webp`,
  },
  {
    text: `Joint Research Centre Data Catalog`,
    href: `${portalUrl}/?f:sources=jrcdatacatalogdbid`,
    img: `/join-research-centre-data-catalog.webp`,
  },
  {
    text: `NASA Earth Observations (NEO) WMS`,
    href: `${portalUrl}/?f:sources=FROMREGISTRY--regprefseparator--registrytestid1--regprefseparator--47a896d3-a332-46a3-ad25-518ce1c374da`,
    img: `/nasa-earth-observations.webp`,
  },
  {
    text: `Sentinel`,
    href: `${portalUrl}/?f:sources=sentinelscihudtest`,
    img: `/sentinel.webp`,
  },
  {
    text: `USGS Earthquake Events`,
    href: `${portalUrl}/?f:sources=UUID-e101622c-6ae1-4b68-bfea-8acf067e31dd`,
    img: `/earthquake-events.webp`,
  },
  {
    text: `USGS Landsat 8`,
    href: `${portalUrl}/?f:sources=landsat8dbidawstest`,
    img: `/landsat-8.webp`,
  },
];
